package com.palominolabs.metrics.newrelic;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Convenience implementation of {@link MetricAttributeFilter} that defaults to enabling all attributes.
 */
@ThreadSafe
public class AllEnabledMetricAttributeFilter implements MetricAttributeFilter {
    @Override
    public boolean recordTimerMin(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimerMax(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimerMean(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimerStdDev(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimerMedian(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimer75thPercentile(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimer95thPercentile(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimer98thPercentile(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimer99thPercentile(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimer999thPercentile(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimerCount(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimerMeanRate(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimer1MinuteRate(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimer5MinuteRate(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordTimer15MinuteRate(String name, Timer metric) {
        return true;
    }

    @Override
    public boolean recordHistogramMin(String name, Histogram metric) {
        return true;
    }

    @Override
    public boolean recordHistogramMax(String name, Histogram metric) {
        return true;
    }

    @Override
    public boolean recordHistogramMean(String name, Histogram metric) {
        return true;
    }

    @Override
    public boolean recordHistogramStdDev(String name, Histogram metric) {
        return true;
    }

    @Override
    public boolean recordHistogramMedian(String name, Histogram metric) {
        return true;
    }

    @Override
    public boolean recordHistogram75thPercentile(String name, Histogram metric) {
        return true;
    }

    @Override
    public boolean recordHistogram95thPercentile(String name, Histogram metric) {
        return true;
    }

    @Override
    public boolean recordHistogram98thPercentile(String name, Histogram metric) {
        return true;
    }

    @Override
    public boolean recordHistogram99thPercentile(String name, Histogram metric) {
        return true;
    }

    @Override
    public boolean recordHistogram999thPercentile(String name, Histogram metric) {
        return true;
    }

    @Override
    public boolean recordMeterCount(String name, Meter metric) {
        return true;
    }

    @Override
    public boolean recordMeterMeanRate(String name, Meter metric) {
        return true;
    }

    @Override
    public boolean recordMeter1MinuteRate(String name, Meter metric) {
        return true;
    }

    @Override
    public boolean recordMeter5MinuteRate(String name, Meter metric) {
        return true;
    }

    @Override
    public boolean recordMeter15MinuteRate(String name, Meter metric) {
        return true;
    }

    @Override
    public boolean recordCounterCount(String name, Counter metric) {
        return true;
    }

    @Override
    public boolean recordGaugeValue(String name, Gauge metric) {
        return true;
    }
}
