package com.palominolabs.metrics.newrelic;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Convenience implementation of {@link MetricAttributeFilter} that defaults to disabling all attributes. This is not a
 * useful class to use directly since it will result in nothing being reported.
 */
@ThreadSafe
public class AllDisabledMetricAttributeFilter implements MetricAttributeFilter {
    @Override
    public boolean recordTimerMin(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimerMax(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimerMean(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimerStdDev(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimerMedian(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimer75thPercentile(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimer95thPercentile(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimer98thPercentile(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimer99thPercentile(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimer999thPercentile(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimerCount(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimerMeanRate(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimer1MinuteRate(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimer5MinuteRate(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordTimer15MinuteRate(String name, Timer metric) {
        return false;
    }

    @Override
    public boolean recordHistogramMin(String name, Histogram metric) {
        return false;
    }

    @Override
    public boolean recordHistogramMax(String name, Histogram metric) {
        return false;
    }

    @Override
    public boolean recordHistogramMean(String name, Histogram metric) {
        return false;
    }

    @Override
    public boolean recordHistogramStdDev(String name, Histogram metric) {
        return false;
    }

    @Override
    public boolean recordHistogramMedian(String name, Histogram metric) {
        return false;
    }

    @Override
    public boolean recordHistogram75thPercentile(String name, Histogram metric) {
        return false;
    }

    @Override
    public boolean recordHistogram95thPercentile(String name, Histogram metric) {
        return false;
    }

    @Override
    public boolean recordHistogram98thPercentile(String name, Histogram metric) {
        return false;
    }

    @Override
    public boolean recordHistogram99thPercentile(String name, Histogram metric) {
        return false;
    }

    @Override
    public boolean recordHistogram999thPercentile(String name, Histogram metric) {
        return false;
    }

    @Override
    public boolean recordMeterCount(String name, Meter metric) {
        return false;
    }

    @Override
    public boolean recordMeterMeanRate(String name, Meter metric) {
        return false;
    }

    @Override
    public boolean recordMeter1MinuteRate(String name, Meter metric) {
        return false;
    }

    @Override
    public boolean recordMeter5MinuteRate(String name, Meter metric) {
        return false;
    }

    @Override
    public boolean recordMeter15MinuteRate(String name, Meter metric) {
        return false;
    }

    @Override
    public boolean recordCounterCount(String name, Counter metric) {
        return false;
    }

    @Override
    public boolean recordGaugeValue(String name, Gauge metric) {
        return false;
    }
}
