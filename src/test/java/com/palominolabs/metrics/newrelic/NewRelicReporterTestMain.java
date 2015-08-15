package com.palominolabs.metrics.newrelic;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demo class for testing how the various metrics show up in new relic.
 */
public class NewRelicReporterTestMain {
    public static void main(String[] args) {

        // create some various metrics and update them

        MetricRegistry registry = new MetricRegistry();

        final AtomicInteger gaugeInteger = new AtomicInteger();

        registry.register(name("gauge"), new Gauge<Integer>() {

            @Override
            public Integer getValue() {
                return gaugeInteger.get();
            }
        });

        final Counter counter = registry.counter(name("counter"));

        final Histogram histogram = registry.histogram(name("histogram"));

        final Meter meter = registry.meter(name("meter"));

        final Timer timer = registry.timer(name("timer"));

        NewRelicReporter reporter = NewRelicReporter.forRegistry(registry)
                .name("new relic reporter")
                .filter(MetricFilter.ALL)
                .attributeFilter(new AllEnabledMetricAttributeFilter())
                .rateUnit(TimeUnit.SECONDS)
                .durationUnit(TimeUnit.MILLISECONDS)
                .metricNamePrefix("foo/")
                .build();

        reporter.start(60, TimeUnit.SECONDS);

        ScheduledExecutorService svc = Executors.newScheduledThreadPool(1);

        final Random random = new Random();

        svc.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Updating");
                gaugeInteger.incrementAndGet();
                counter.inc();
                histogram.update(random.nextInt(10));
                meter.mark();
                timer.update(random.nextInt(10), TimeUnit.MILLISECONDS);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private static String name(String s) {
        return MetricRegistry.name(NewRelicReporterTestMain.class, s);
    }
}
