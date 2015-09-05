package com.palominolabs.metrics.newrelic.table;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Table;
import com.palominolabs.metrics.newrelic.AllDisabledMetricAttributeFilter;
import com.palominolabs.metrics.newrelic.MetricAttributeFilter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Convenience implementation of {@link MetricAttributeFilter} with grain level customization.
 *
 * For example:
 * <pre>
 *     {@code
 *      Table<String, NewRelicMetric, Boolean> metricConfig = ImmutableTable.<String, NewRelicMetric, Boolean>builder()
 *          .put("metricName1", NewRelicMetric.TIMER_MAX, true)
 *          .put("metricName1", NewRelicMetric.TIMER_MIN, false)
 *          .put("metricName2", NewRelicMetric.COUNTER_COUNT, true)
 *          .build();
 *
 *      new TableMetricAttributeFilter(new MetricAttributeTableSupplier(metricConfig), new
 * AllDisabledMetricAttributeFilter());
 *     }
 * </pre>
 * Constructor receives a {@link MetricAttributeFilter} which will be used as fallback for all metrics not specified in
 * configuration. In case <i>fallback</i> is null, {@link AllDisabledMetricAttributeFilter} will be used.
 */
@ThreadSafe
public class TableMetricAttributeFilter implements MetricAttributeFilter {

    private final Table<String, NewRelicMetric, Boolean> enabledMetrics;
    private final MetricAttributeFilter fallback;

    /**
     * @param table    table containing metrics config. Must be immutable or otherwise threadsafe.
     * @param fallback to be used when there metrics config has no entry for the metric to be reported. If null,
     *                 AllDisabledMetricAttributeFilter will be used.
     */
    public TableMetricAttributeFilter(@Nonnull Table<String, NewRelicMetric, Boolean> table,
            @Nullable MetricAttributeFilter fallback) {
        Preconditions.checkArgument(table != null, "table cannot be null");
        this.enabledMetrics = table;
        this.fallback = getFallbackMetricFilter(fallback);
    }

    /*
     * This approach has some reduction in duplication of logic surrounding the fallback, but it also means that
     * an allocation happens for each record call. If this proves to be a problem, could inline all the
     * Supplier<Boolean> instances. Reporting isn't a very hot code path though, and these allocations should all die
     * in newgen, so it's probably not a big issue.
     */

    @Override
    public boolean recordTimerMin(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_MIN, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimerMin(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimerMax(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_MAX, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimerMax(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimerMean(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_MEAN, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimerMean(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimerStdDev(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_STD_DEV, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimerStdDev(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimerMedian(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_MEDIAN, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimerMedian(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimer75thPercentile(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_75TH_PERCENTILE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimer75thPercentile(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimer95thPercentile(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_95TH_PERCENTILE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimer95thPercentile(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimer98thPercentile(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_98TH_PERCENTILE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimer98thPercentile(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimer99thPercentile(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_99TH_PERCENTILE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimer99thPercentile(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimer999thPercentile(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_999TH_PERCENTILE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimer999thPercentile(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimerCount(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_COUNT, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimerCount(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimerMeanRate(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_MEAN_RATE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimerMeanRate(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimer1MinuteRate(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_1_MINUTE_RATE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimer1MinuteRate(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimer5MinuteRate(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_5_MINUTE_RATE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimer5MinuteRate(name, metric);
            }
        });
    }

    @Override
    public boolean recordTimer15MinuteRate(final String name, final Timer metric) {
        return isEnabledWithFallback(name, NewRelicMetric.TIMER_15_MINUTE_RATE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordTimer15MinuteRate(name, metric);
            }
        });
    }

    @Override
    public boolean recordHistogramMin(final String name, final Histogram metric) {
        return isEnabledWithFallback(name, NewRelicMetric.HISTOGRAM_MIN, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordHistogramMin(name, metric);
            }
        });
    }

    @Override
    public boolean recordHistogramMax(final String name, final Histogram metric) {
        return isEnabledWithFallback(name, NewRelicMetric.HISTOGRAM_MAX, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordHistogramMax(name, metric);
            }
        });
    }

    @Override
    public boolean recordHistogramMean(final String name, final Histogram metric) {
        return isEnabledWithFallback(name, NewRelicMetric.HISTOGRAM_MEAN, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordHistogramMean(name, metric);
            }
        });
    }

    @Override
    public boolean recordHistogramStdDev(final String name, final Histogram metric) {
        return isEnabledWithFallback(name, NewRelicMetric.HISTOGRAM_STD_DEV, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordHistogramStdDev(name, metric);
            }
        });
    }

    @Override
    public boolean recordHistogramMedian(final String name, final Histogram metric) {
        return isEnabledWithFallback(name, NewRelicMetric.HISTOGRAM_MEDIAN, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordHistogramMedian(name, metric);
            }
        });
    }

    @Override
    public boolean recordHistogram75thPercentile(final String name, final Histogram metric) {
        return isEnabledWithFallback(name, NewRelicMetric.HISTOGRAM_75TH_PERCENTILE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordHistogram75thPercentile(name, metric);
            }
        });
    }

    @Override
    public boolean recordHistogram95thPercentile(final String name, final Histogram metric) {
        return isEnabledWithFallback(name, NewRelicMetric.HISTOGRAM_95TH_PERCENTILE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordHistogram95thPercentile(name, metric);
            }
        });
    }

    @Override
    public boolean recordHistogram98thPercentile(final String name, final Histogram metric) {
        return isEnabledWithFallback(name, NewRelicMetric.HISTOGRAM_98TH_PERCENTILE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordHistogram98thPercentile(name, metric);
            }
        });
    }

    @Override
    public boolean recordHistogram99thPercentile(final String name, final Histogram metric) {
        return isEnabledWithFallback(name, NewRelicMetric.HISTOGRAM_99TH_PERCENTILE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordHistogram99thPercentile(name, metric);
            }
        });
    }

    @Override
    public boolean recordHistogram999thPercentile(final String name, final Histogram metric) {
        return isEnabledWithFallback(name, NewRelicMetric.HISTOGRAM_999TH_PERCENTILE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordHistogram999thPercentile(name, metric);
            }
        });
    }

    @Override
    public boolean recordMeterCount(final String name, final Meter metric) {
        return isEnabledWithFallback(name, NewRelicMetric.METER_COUNT, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordMeterCount(name, metric);
            }
        });
    }

    @Override
    public boolean recordMeterMeanRate(final String name, final Meter metric) {
        return isEnabledWithFallback(name, NewRelicMetric.METER_RATE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordMeterMeanRate(name, metric);
            }
        });
    }

    @Override
    public boolean recordMeter1MinuteRate(final String name, final Meter metric) {
        return isEnabledWithFallback(name, NewRelicMetric.METER_1_MINUTE_RATE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordMeter1MinuteRate(name, metric);
            }
        });
    }

    @Override
    public boolean recordMeter5MinuteRate(final String name, final Meter metric) {
        return isEnabledWithFallback(name, NewRelicMetric.METER_5_MINUTE_RATE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordMeter5MinuteRate(name, metric);
            }
        });
    }

    @Override
    public boolean recordMeter15MinuteRate(final String name, final Meter metric) {
        return isEnabledWithFallback(name, NewRelicMetric.METER_15_MINUTE_RATE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordMeter15MinuteRate(name, metric);
            }
        });
    }

    @Override
    public boolean recordCounterCount(final String name, final Counter metric) {
        return isEnabledWithFallback(name, NewRelicMetric.COUNTER_COUNT, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordCounterCount(name, metric);
            }
        });
    }

    @Override
    public boolean recordGaugeValue(final String name, final Gauge metric) {
        return isEnabledWithFallback(name, NewRelicMetric.GAUGE_VALUE, new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return fallback.recordGaugeValue(name, metric);
            }
        });
    }

    private MetricAttributeFilter getFallbackMetricFilter(MetricAttributeFilter fallback) {
        return fallback == null ?
                new AllDisabledMetricAttributeFilter() :
                fallback;
    }

    private boolean isEnabledWithFallback(final String name, NewRelicMetric newRelicMetric,
            Supplier<Boolean> fallbackSupplier) {
        return Optional.fromNullable(enabledMetrics.get(name, newRelicMetric)).or(fallbackSupplier);
    }

    public enum NewRelicMetric {

        TIMER_MIN,
        TIMER_MAX,
        TIMER_MEAN,
        TIMER_STD_DEV,
        TIMER_MEDIAN,
        TIMER_75TH_PERCENTILE,
        TIMER_95TH_PERCENTILE,
        TIMER_98TH_PERCENTILE,
        TIMER_99TH_PERCENTILE,
        TIMER_999TH_PERCENTILE,
        TIMER_COUNT,
        TIMER_MEAN_RATE,
        TIMER_1_MINUTE_RATE,
        TIMER_5_MINUTE_RATE,
        TIMER_15_MINUTE_RATE,
        HISTOGRAM_MIN,
        HISTOGRAM_MAX,
        HISTOGRAM_MEAN,
        HISTOGRAM_STD_DEV,
        HISTOGRAM_MEDIAN,
        HISTOGRAM_75TH_PERCENTILE,
        HISTOGRAM_95TH_PERCENTILE,
        HISTOGRAM_98TH_PERCENTILE,
        HISTOGRAM_99TH_PERCENTILE,
        HISTOGRAM_999TH_PERCENTILE,
        METER_COUNT,
        METER_RATE,
        METER_1_MINUTE_RATE,
        METER_5_MINUTE_RATE,
        METER_15_MINUTE_RATE,
        COUNTER_COUNT,
        GAUGE_VALUE,

    }
}
