package com.palominolabs.metrics.newrelic;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.palominolabs.metrics.newrelic.TableMetricAttributeFilter.NewRelicMetric;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class MetricsAttributeTableSupplierTest {

    @Test
    public void get() throws Exception {
        Table<String, NewRelicMetric, Boolean> metricConfig = ImmutableTable.<String, NewRelicMetric, Boolean>builder()
                .put("metricName1", NewRelicMetric.TIMER_MAX, true)
                .put("metricName1", NewRelicMetric.TIMER_MIN, false)
                .put("metricName2", NewRelicMetric.COUNTER_COUNT, true)
                .build();

        Table<String, NewRelicMetric, Boolean> table = new MetricsAttributeTableSupplier(metricConfig).get();

        MatcherAssert.assertThat(table, CoreMatchers.sameInstance(metricConfig));
    }


    @Test
    public void nullTableThrowsException(){
        try {
            new MetricsAttributeTableSupplier(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), CoreMatchers.equalTo("table cannot be null"));
        }
    }
}