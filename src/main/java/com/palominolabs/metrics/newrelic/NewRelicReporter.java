package com.palominolabs.metrics.newrelic;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metered;
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
 * A reporter for metrics that writes to New Relic.
 */
@ThreadSafe
public final class NewRelicReporter extends ScheduledReporter {

    /**
     * @see ScheduledReporter#ScheduledReporter(MetricRegistry, String, MetricFilter, TimeUnit, TimeUnit)
     */
    public NewRelicReporter(MetricRegistry registry, String name, MetricFilter filter, TimeUnit rateUnit,
        TimeUnit durationUnit) {
        super(registry, name, filter, rateUnit, durationUnit);
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters,
        SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
        for (Map.Entry<String, Gauge> gaugeEntry : gauges.entrySet()) {
            doGauge(gaugeEntry.getKey(), gaugeEntry.getValue());
        }

        for (Map.Entry<String, Counter> counterEntry : counters.entrySet()) {
            record(counterEntry.getKey() + "/count", counterEntry.getValue().getCount());
        }

        for (Map.Entry<String, Histogram> histogramEntry : histograms.entrySet()) {
            String name = histogramEntry.getKey();
            Snapshot snapshot = histogramEntry.getValue().getSnapshot();

            doSnapshot(name, snapshot, "");
        }

        for (Map.Entry<String, Meter> meterEntry : meters.entrySet()) {
            doMetered(meterEntry.getKey(), meterEntry.getValue());
        }

        for (Map.Entry<String, Timer> timerEntry : timers.entrySet()) {
            Timer timer = timerEntry.getValue();
            String name = "/timer/" + timerEntry.getKey();
            Snapshot snapshot = timer.getSnapshot();

            doMetered(timerEntry.getKey(), timer);

            doSnapshot(name, snapshot, "/" + getDurationUnit());
        }
    }

    private void doSnapshot(String name, Snapshot snapshot, String nameSuffix) {
        record(name + "/min" + nameSuffix, (float) convertDuration(snapshot.getMin()));
        record(name + "/max" + nameSuffix, (float) convertDuration(snapshot.getMax()));
        record(name + "/mean" + nameSuffix, (float) convertDuration(snapshot.getMean()));
        record(name + "/stdDev" + nameSuffix, (float) convertDuration(snapshot.getStdDev()));
        record(name + "/median" + nameSuffix, (float) convertDuration(snapshot.getMedian()));
        record(name + "/75th" + nameSuffix, (float) convertDuration(snapshot.get75thPercentile()));
        record(name + "/95th" + nameSuffix, (float) convertDuration(snapshot.get95thPercentile()));
        record(name + "/98th" + nameSuffix, (float) convertDuration(snapshot.get98thPercentile()));
        record(name + "/99th" + nameSuffix, (float) convertDuration(snapshot.get99thPercentile()));
        record(name + "/99.9th" + nameSuffix, (float) convertDuration(snapshot.get999thPercentile()));
    }

    private void doMetered(String name, Metered meter) {
        record(name + "/count", meter.getCount());
        record(name + "/meanRate/" + getRateUnit(), (float) convertRate(meter.getMeanRate()));
        record(name + "/1MinuteRate/" + getRateUnit(), (float) convertRate(meter.getOneMinuteRate()));
        record(name + "/5MinuteRate/" + getRateUnit(), (float) convertRate(meter.getFiveMinuteRate()));
        record(name + "/15MinuteRate/" + getRateUnit(), (float) convertRate(meter.getFifteenMinuteRate()));
    }

    private void doGauge(String name, Gauge gauge) {
        Object gaugeValue = gauge.getValue();

        if (gaugeValue instanceof Number) {
            float n = ((Number) gaugeValue).floatValue();
            if (!Float.isNaN(n) && !Float.isInfinite(n)) {
                record(name, n);
            }
        }
    }

    private void record(String name, float value) {
        NewRelic.recordMetric("Custom/" + name, value);
    }
}
