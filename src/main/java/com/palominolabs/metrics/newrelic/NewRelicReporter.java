package com.palominolabs.metrics.newrelic;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metered;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import com.newrelic.api.agent.NewRelic;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * A reporter for Metrics that writes to New Relic as "custom metrics".
 *
 * New Relic wants to keep custom metrics to a total of about 2000, but 2000 custom metrics can easily be reached when
 * every {@link com.codahale.metrics.Timer} can produce 15 New Relic metrics. See https://docs.newrelic.com/docs/features/custom-metric-collection
 * for more.
 *
 * To keep the number of custom metrics under control, provide appropriate implementations of {@link MetricFilter} and
 * {@link MetricAttributeFilter}.
 */
@ThreadSafe
public final class NewRelicReporter extends ScheduledReporter {

    private final MetricAttributeFilter attributeFilter;

    /**
     * @see ScheduledReporter#ScheduledReporter(MetricRegistry, String, MetricFilter, TimeUnit, TimeUnit)
     */
    public NewRelicReporter(MetricRegistry registry, String name, MetricFilter filter,
        MetricAttributeFilter attributeFilter, TimeUnit rateUnit, TimeUnit durationUnit) {
        super(registry, name, filter, rateUnit, durationUnit);
        this.attributeFilter = attributeFilter;
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters,
        SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
        for (Map.Entry<String, Gauge> gaugeEntry : gauges.entrySet()) {
            doGauge(gaugeEntry.getKey(), gaugeEntry.getValue());
        }

        for (Map.Entry<String, Counter> counterEntry : counters.entrySet()) {
            String name = counterEntry.getKey();
            Counter counter = counterEntry.getValue();
            if (attributeFilter.recordCounterCount(name, counter)) {
                record(name + "/count", counter.getCount());
            }
        }

        for (Map.Entry<String, Histogram> histogramEntry : histograms.entrySet()) {
            String name = histogramEntry.getKey();
            Snapshot snapshot = histogramEntry.getValue().getSnapshot();

            doSnapshot(name, snapshot, "", false, histogramEntry.getValue());
        }

        for (Map.Entry<String, Meter> meterEntry : meters.entrySet()) {
            doMetered(meterEntry.getKey(), meterEntry.getValue(), false);
        }

        for (Map.Entry<String, Timer> timerEntry : timers.entrySet()) {
            Timer timer = timerEntry.getValue();
            String name = "/timer/" + timerEntry.getKey();
            Snapshot snapshot = timer.getSnapshot();

            doMetered(timerEntry.getKey(), timer, true);

            doSnapshot(name, snapshot, "/" + getDurationUnit(), true, timer);
        }
    }

    /**
     * @param name       metric name
     * @param snapshot   snapshot to record
     * @param nameSuffix name suffx to append
     * @param isTimer    true if this is for a timer, false for a histogram
     */
    private void doSnapshot(String name, Snapshot snapshot, String nameSuffix, boolean isTimer, Metric metric) {
        if (isTimer ? attributeFilter.recordTimerMin(name, metric) : attributeFilter.recordHistogramMin(name, metric)) {
            record(name + "/min" + nameSuffix, (float) convertDuration(snapshot.getMin()));
        }
        if (isTimer ? attributeFilter.recordTimerMax(name, metric) : attributeFilter.recordHistogramMax(name, metric)) {
            record(name + "/max" + nameSuffix, (float) convertDuration(snapshot.getMax()));
        }
        if (isTimer ? attributeFilter.recordTimerMean(name, metric) :
            attributeFilter.recordHistogramMean(name, metric)) {
            record(name + "/mean" + nameSuffix, (float) convertDuration(snapshot.getMean()));
        }
        if (isTimer ? attributeFilter.recordTimerStdDev(name, metric) :
            attributeFilter.recordHistogramStdDev(name, metric)) {
            record(name + "/stdDev" + nameSuffix, (float) convertDuration(snapshot.getStdDev()));
        }
        if (isTimer ? attributeFilter.recordTimerMedian(name, metric) :
            attributeFilter.recordHistogramMedian(name, metric)) {
            record(name + "/median" + nameSuffix, (float) convertDuration(snapshot.getMedian()));
        }
        if (isTimer ? attributeFilter.recordTimer75thPercentile(name, metric) :
            attributeFilter.recordHistogram75thPercentile(name, metric)) {
            record(name + "/75th" + nameSuffix, (float) convertDuration(snapshot.get75thPercentile()));
        }
        if (isTimer ? attributeFilter.recordTimer95thPercentile(name, metric) :
            attributeFilter.recordHistogram95thPercentile(name, metric)) {
            record(name + "/95th" + nameSuffix, (float) convertDuration(snapshot.get95thPercentile()));
        }
        if (isTimer ? attributeFilter.recordTimer98thPercentile(name, metric) :
            attributeFilter.recordHistogram98thPercentile(name, metric)) {
            record(name + "/98th" + nameSuffix, (float) convertDuration(snapshot.get98thPercentile()));
        }
        if (isTimer ? attributeFilter.recordTimer99thPercentile(name, metric) :
            attributeFilter.recordHistogram99thPercentile(name, metric)) {
            record(name + "/99th" + nameSuffix, (float) convertDuration(snapshot.get99thPercentile()));
        }
        if (isTimer ? attributeFilter.recordTimer999thPercentile(name, metric) :
            attributeFilter.recordHistogram999thPercentile(name, metric)) {
            record(name + "/99.9th" + nameSuffix, (float) convertDuration(snapshot.get999thPercentile()));
        }
    }

    /**
     * @param name    metric name
     * @param metered metered info
     * @param isTimer true if this is for a timer, false for a meter
     */
    private void doMetered(String name, Metered metered, boolean isTimer) {
        if (isTimer ? attributeFilter.recordTimerCount(name, metered) :
            attributeFilter.recordMeterCount(name, metered)) {
            record(name + "/count", metered.getCount());
        }
        if (isTimer ? attributeFilter.recordTimerMeanRate(name, metered) :
            attributeFilter.recordMeterMeanRate(name, metered)) {
            record(name + "/meanRate/" + getRateUnit(), (float) convertRate(metered.getMeanRate()));
        }
        if (isTimer ? attributeFilter.recordTimer1MinuteRate(name, metered) :
            attributeFilter.recordMeter1MinuteRate(name, metered)) {
            record(name + "/1MinuteRate/" + getRateUnit(), (float) convertRate(metered.getOneMinuteRate()));
        }
        if (isTimer ? attributeFilter.recordTimer5MinuteRate(name, metered) :
            attributeFilter.recordMeter5MinuteRate(name, metered)) {
            record(name + "/5MinuteRate/" + getRateUnit(), (float) convertRate(metered.getFiveMinuteRate()));
        }
        if (isTimer ? attributeFilter.recordTimer15MinuteRate(name, metered) :
            attributeFilter.recordMeter15MinuteRate(name, metered)) {
            record(name + "/15MinuteRate/" + getRateUnit(), (float) convertRate(metered.getFifteenMinuteRate()));
        }
    }

    private void doGauge(String name, Gauge gauge) {
        Object gaugeValue = gauge.getValue();

        if (gaugeValue instanceof Number) {
            float n = ((Number) gaugeValue).floatValue();
            if (!Float.isNaN(n) && !Float.isInfinite(n) && attributeFilter.recordGaugeValue(name, gauge)) {
                record(name, n);
            }
        }
    }

    private void record(String name, float value) {
        NewRelic.recordMetric("Custom/" + name, value);
    }
}
