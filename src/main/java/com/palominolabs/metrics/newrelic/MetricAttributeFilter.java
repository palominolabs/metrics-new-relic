package com.palominolabs.metrics.newrelic;

import com.codahale.metrics.Metric;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Controls which specific data are reported for any metric.
 *
 * The various methods control whether or not that particular piece of data will be reported to New Relic as a metric.
 * As an example, {@link MetricAttributeFilter#recordTimer98thPercentile(String, com.codahale.metrics.Metric)} should
 * return true if and only if you want the 98th percentile for a timer to be recorded in New Relic, and that has no
 * bearing on the 98th percentile data for a Histogram (use {@link MetricAttributeFilter#recordHistogram98thPercentile(String,
 * com.codahale.metrics.Metric)} for that).
 *
 * Convenient starting points for your own implementations are {@link AllEnabledMetricAttributeFilter} for a blacklist
 * approach and {@link AllDisabledMetricAttributeFilter} for a whitelist approach.
 */
@ThreadSafe
public interface MetricAttributeFilter {
    boolean recordTimerMin(String name, Metric metric);

    boolean recordTimerMax(String name, Metric metric);

    boolean recordTimerMean(String name, Metric metric);

    boolean recordTimerStdDev(String name, Metric metric);

    boolean recordTimerMedian(String name, Metric metric);

    boolean recordTimer75thPercentile(String name, Metric metric);

    boolean recordTimer95thPercentile(String name, Metric metric);

    boolean recordTimer98thPercentile(String name, Metric metric);

    boolean recordTimer99thPercentile(String name, Metric metric);

    boolean recordTimer999thPercentile(String name, Metric metric);

    boolean recordTimerCount(String name, Metric metric);

    boolean recordTimerMeanRate(String name, Metric metric);

    boolean recordTimer1MinuteRate(String name, Metric metric);

    boolean recordTimer5MinuteRate(String name, Metric metric);

    boolean recordTimer15MinuteRate(String name, Metric metric);

    boolean recordHistogramMin(String name, Metric metric);

    boolean recordHistogramMax(String name, Metric metric);

    boolean recordHistogramMean(String name, Metric metric);

    boolean recordHistogramStdDev(String name, Metric metric);

    boolean recordHistogramMedian(String name, Metric metric);

    boolean recordHistogram75thPercentile(String name, Metric metric);

    boolean recordHistogram95thPercentile(String name, Metric metric);

    boolean recordHistogram98thPercentile(String name, Metric metric);

    boolean recordHistogram99thPercentile(String name, Metric metric);

    boolean recordHistogram999thPercentile(String name, Metric metric);

    boolean recordMeterCount(String name, Metric metric);

    boolean recordMeterMeanRate(String name, Metric metric);

    boolean recordMeter1MinuteRate(String name, Metric metric);

    boolean recordMeter5MinuteRate(String name, Metric metric);

    boolean recordMeter15MinuteRate(String name, Metric metric);

    boolean recordCounterCount(String name, Metric metric);

    boolean recordGaugeValue(String name, Metric metric);
}
