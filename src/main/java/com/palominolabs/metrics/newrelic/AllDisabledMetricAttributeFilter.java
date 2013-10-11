package com.palominolabs.metrics.newrelic;

import com.codahale.metrics.Metric;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Convenience implementation of {@link MetricAttributeFilter} that defaults to disabling all attributes.
 */
@ThreadSafe
public class AllDisabledMetricAttributeFilter implements MetricAttributeFilter {
    @Override
    public boolean recordTimerMin(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimerMax(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimerMean(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimerStdDev(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimerMedian(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimer75thPercentile(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimer95thPercentile(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimer98thPercentile(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimer99thPercentile(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimer999thPercentile(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimerCount(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimerMeanRate(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimer1MinuteRate(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimer5MinuteRate(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordTimer15MinuteRate(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordHistogramMin(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordHistogramMax(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordHistogramMean(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordHistogramStdDev(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordHistogramMedian(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordHistogram75thPercentile(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordHistogram95thPercentile(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordHistogram98thPercentile(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordHistogram99thPercentile(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordHistogram999thPercentile(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordMeterCount(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordMeterMeanRate(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordMeter1MinuteRate(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordMeter5MinuteRate(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordMeter15MinuteRate(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordCounterCount(String name, Metric metric) {
        return false;
    }

    @Override
    public boolean recordGaugeValue(String name, Metric metric) {
        return false;
    }
}
