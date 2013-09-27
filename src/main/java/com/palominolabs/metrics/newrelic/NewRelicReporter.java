package com.palominolabs.metrics.newrelic;

import com.newrelic.api.agent.NewRelic;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.Histogram;
import com.yammer.metrics.core.Metered;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricProcessor;
import com.yammer.metrics.core.MetricsRegistry;
import com.yammer.metrics.core.Sampling;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.reporting.AbstractPollingReporter;
import com.yammer.metrics.reporting.AbstractReporter;
import com.yammer.metrics.stats.Snapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class NewRelicReporter extends AbstractPollingReporter implements MetricProcessor<String> {

    private static final Logger logger = LoggerFactory.getLogger(NewRelicReporter.class);

    /**
     * Convenience method to start a reporter on a registry.
     *
     * @param registry metrics registry to report on
     * @param period   the period between successive outputs
     * @param unit     the time unit of {@code period}
     */
    public static void enable(MetricsRegistry registry, long period, TimeUnit unit) {
        NewRelicReporter reporter = new NewRelicReporter(registry, "new-relic-reporter");
        reporter.start(period, unit);
    }

    /**
     * @param registry the {@link MetricsRegistry} containing the metrics this reporter will report
     * @param name     the reporter's name
     * @see AbstractReporter#AbstractReporter(MetricsRegistry)
     */
    protected NewRelicReporter(MetricsRegistry registry, String name) {
        super(registry, name);
    }

    @Override
    public void run() {
        final Set<Map.Entry<MetricName, Metric>> metrics = getMetricsRegistry().allMetrics().entrySet();

        for (Map.Entry<MetricName, Metric> entry : metrics) {
            try {
                final Metric metric = entry.getValue();
                metric.processWith(this, entry.getKey(), null);
            } catch (Exception e) {
                logger.warn("Could not process metric " + entry.getKey(), e);
            }
        }
    }

    @Override
    public void processMeter(MetricName name, Metered meter, String context) throws Exception {
        doMetered(buildNameString(name), meter);
    }

    @Override
    public void processCounter(MetricName name, Counter counter, String context) throws Exception {
        NewRelic.recordMetric(buildNameString(name) + ".count", counter.count());
    }

    @Override
    public void processHistogram(MetricName name, Histogram histogram, String context) throws Exception {
        doSampling(buildNameString(name), histogram);
    }

    @Override
    public void processTimer(MetricName name, Timer timer, String context) throws Exception {
        doMetered(buildNameString(name), timer);
        doSampling(buildNameString(name), timer);

        NewRelic.recordMetric(buildNameString(name) + ".mean", (float) timer.mean());
    }

    @Override
    public void processGauge(MetricName name, Gauge<?> gauge, String context) throws Exception {
        NewRelic.recordMetric(buildNameString(name), (Float) gauge.value());
    }

    @Nonnull
    private String buildNameString(@Nullable MetricName name) {
        if (name == null) {
            return "";
        }
        final String qualifiedTypeName = name.getGroup() + "." + name.getType() + "." + name.getName();
        return name.hasScope() ? qualifiedTypeName + '.' + name.getScope() : qualifiedTypeName;
    }

    private void doMetered(String name, Metered meter) {
        NewRelic.recordMetric(name + ".count", meter.count());
        NewRelic.recordMetric(name + ".meanRate", (float) meter.meanRate());
        NewRelic.recordMetric(name + ".1MinuteRate", (float) meter.oneMinuteRate());
    }

    private void doSampling(String name, Sampling sampling) {
        Snapshot snapshot = sampling.getSnapshot();
        NewRelic.recordMetric(name + ".median", (float) snapshot.getMedian());
        NewRelic.recordMetric(name + ".75th", (float) snapshot.get75thPercentile());
        NewRelic.recordMetric(name + ".99th", (float) snapshot.get99thPercentile());
    }
}
