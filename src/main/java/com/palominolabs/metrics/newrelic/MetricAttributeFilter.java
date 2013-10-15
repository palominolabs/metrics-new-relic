package com.palominolabs.metrics.newrelic;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Controls which specific data are reported for any metric.
 *
 * The various methods control whether or not that particular piece of data will be reported to New Relic as a metric.
 * As an example, {@link MetricAttributeFilter#recordTimer98thPercentile(String, com.codahale.metrics.Timer)} should
 * return true if and only if you want the 98th percentile for a timer to be recorded in New Relic, and that has no
 * bearing on the 98th percentile data for a Histogram (use {@link MetricAttributeFilter#recordHistogram98thPercentile(String,
 * com.codahale.metrics.Histogram)} for that).
 *
 * Convenient starting points for your own implementations are {@link AllEnabledMetricAttributeFilter} for a blacklist
 * approach and {@link AllDisabledMetricAttributeFilter} for a whitelist approach.
 */
@ThreadSafe
public interface MetricAttributeFilter {
    boolean recordTimerMin(String name, Timer metric);

    boolean recordTimerMax(String name, Timer metric);

    boolean recordTimerMean(String name, Timer metric);

    boolean recordTimerStdDev(String name, Timer metric);

    boolean recordTimerMedian(String name, Timer metric);

    boolean recordTimer75thPercentile(String name, Timer metric);

    boolean recordTimer95thPercentile(String name, Timer metric);

    boolean recordTimer98thPercentile(String name, Timer metric);

    boolean recordTimer99thPercentile(String name, Timer metric);

    boolean recordTimer999thPercentile(String name, Timer metric);

    boolean recordTimerCount(String name, Timer metric);

    boolean recordTimerMeanRate(String name, Timer metric);

    boolean recordTimer1MinuteRate(String name, Timer metric);

    boolean recordTimer5MinuteRate(String name, Timer metric);

    boolean recordTimer15MinuteRate(String name, Timer metric);

    boolean recordHistogramMin(String name, Histogram metric);

    boolean recordHistogramMax(String name, Histogram metric);

    boolean recordHistogramMean(String name, Histogram metric);

    boolean recordHistogramStdDev(String name, Histogram metric);

    boolean recordHistogramMedian(String name, Histogram metric);

    boolean recordHistogram75thPercentile(String name, Histogram metric);

    boolean recordHistogram95thPercentile(String name, Histogram metric);

    boolean recordHistogram98thPercentile(String name, Histogram metric);

    boolean recordHistogram99thPercentile(String name, Histogram metric);

    boolean recordHistogram999thPercentile(String name, Histogram metric);

    boolean recordMeterCount(String name, Meter metric);

    boolean recordMeterMeanRate(String name, Meter metric);

    boolean recordMeter1MinuteRate(String name, Meter metric);

    boolean recordMeter5MinuteRate(String name, Meter metric);

    boolean recordMeter15MinuteRate(String name, Meter metric);

    boolean recordCounterCount(String name, Counter metric);

    boolean recordGaugeValue(String name, Gauge metric);
}
