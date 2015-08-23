This library provides a reporter for [Metrics](http://metrics.codahale.com/) that writes to [New Relic](http://newrelic.com/).

Each of the main types of Metrics (Gauges, Counters, Histograms, Meters, Timers) have all available data (percentiles, min, max, etc. as applicable) reported as "custom metrics" in New Relic.

## Usage

To upload all metrics in your `MetricRegistry` with all available data (not filtering at all), this will do:
```
NewRelicReporter reporter = NewRelicReporter.forRegistry(registry)
                .name("new relic reporter")
                .filter(MetricFilter.ALL)
                .attributeFilter(new AllEnabledMetricAttributeFilter())
                .rateUnit(TimeUnit.SECONDS)
                .durationUnit(TimeUnit.MILLISECONDS)
                .metricNamePrefix("foo/")
                .build();

reporter.start(1, TimeUnit.MINUTES);
```

This will report all attributes of all metrics to New Relic using seconds as the rate unit and milliseconds as the duration unit. Data will be reported via the New Relic Java API once a minute.

## Custom metrics in New Relic

As an example, a Histogram would have the following custom metrics recorded:

- `Custom/metricName/75th`
- `Custom/metricName/95th`
- `Custom/metricName/98th`
- `Custom/metricName/99th`
- `Custom/metricName/99.9th`
- `Custom/metricName/min`
- `Custom/metricName/max`
- `Custom/metricName/mean`
- `Custom/metricName/median`
- `Custom/metricName/stdDev`

A Timer would have the above attributes as well as the various calculated rates, like:

- `Custom/metricName/15MinuteRate/second`

In this case, the trailing `second` means that the rate unit is set to seconds.

You can then proceed to use those metrics in New Relic custom dashboards.

You can also specify a non-empty metric name prefix if you wish to further segregate your various metrics. With a prefix of `foo/`, your metrics would appear as `Custom/foo/metricName/max`, for instance.

## Limiting what's reported to New Relic

According to [New Relic's custom metric best practices](https://docs.newrelic.com/docs/features/custom-metric-collection#best_practices), they want to keep the number of custom metrics under 2000 or so. Since each Timer might generate a dozen custom metrics in New Relic, it's pretty easy to hit this limit.

There are two ways you can keep the number of custom metrics under control. To choose which specific metrics to report on, use a [MetricFilter](https://github.com/codahale/metrics/blob/master/metrics-core/src/main/java/com/codahale/metrics/MetricFilter.java). If you want to report on all metrics, use `MetricFilter.ALL`; otherwise, provide your own implementation of `MetricFilter`.

To limit which attributes of each metric will be reported to New Relic, [MetricAttributeFilter](https://github.com/palominolabs/metrics-new-relic/blob/master/src/main/java/com/palominolabs/metrics/newrelic/MetricAttributeFilter.java) has boolean methods for every attribute, such as `boolean recordTimerMedian(String name, Timer metric)`. All such methods will be passed the metric name and the metric object itself for arbitration on whether or not the relevant attribute will be reported. For convenient implementation of both whitelist and blacklist approaches, [AllEnabledMetricAttributeFilter](https://github.com/palominolabs/metrics-new-relic/blob/master/src/main/java/com/palominolabs/metrics/newrelic/AllEnabledMetricAttributeFilter.java), [AllDisabledMetricAttributeFilter](https://github.com/palominolabs/metrics-new-relic/blob/master/src/main/java/com/palominolabs/metrics/newrelic/AllDisabledMetricAttributeFilter.java) and [AllDisabledMetricAttributeFilter](https://github.com/palominolabs/metrics-new-relic/blob/master/src/main/java/com/palominolabs/metrics/newrelic/TableMetricAttributeFilter.java) are provided.