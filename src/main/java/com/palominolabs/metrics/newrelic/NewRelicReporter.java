package com.palominolabs.metrics.newrelic;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metered;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Sampling;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;
import com.newrelic.api.agent.NewRelic;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

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
            NewRelic.recordMetric(counterEntry.getKey() + ".count", counterEntry.getValue().getCount());
        }

        for (Map.Entry<String, Histogram> histogramEntry : histograms.entrySet()) {
            doSampling(histogramEntry.getKey(), histogramEntry.getValue());
        }

        for (Map.Entry<String, Meter> meterEntry : meters.entrySet()) {
            doMetered(meterEntry.getKey(), meterEntry.getValue());
        }

        for (Map.Entry<String, Timer> timerEntry : timers.entrySet()) {
            doMetered(timerEntry.getKey(), timerEntry.getValue());
            doSampling(timerEntry.getKey(), timerEntry.getValue());

            NewRelic.recordMetric(timerEntry.getKey() + ".mean", (float) timerEntry.getValue().getMeanRate());
        }
    }

    private void doMetered(String name, Metered meter) {
        NewRelic.recordMetric(name + ".count", meter.getCount());
        NewRelic.recordMetric(name + ".meanRate", (float) meter.getMeanRate());
        NewRelic.recordMetric(name + ".1MinuteRate", (float) meter.getOneMinuteRate());
    }

    private void doSampling(String name, Sampling sampling) {
        Snapshot snapshot = sampling.getSnapshot();
        NewRelic.recordMetric(name + ".median", (float) snapshot.getMedian());
        NewRelic.recordMetric(name + ".75th", (float) snapshot.get75thPercentile());
        NewRelic.recordMetric(name + ".99th", (float) snapshot.get99thPercentile());
    }

    private void doGauge(String name, Gauge gauge) {
        Object gaugeValue = gauge.getValue();

        if (gaugeValue instanceof Number) {
            float n = ((Number) gaugeValue).floatValue();
            if (!Float.isNaN(n) && !Float.isInfinite(n)) {
                NewRelic.recordMetric(name, n);
            }
        }
    }
}
