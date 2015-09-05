package com.palominolabs.metrics.newrelic;

import com.google.common.collect.Table;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class YamlMetricsAttributeTableSupplierTest {

    @Test
    public void getReturnTableReadFromFile() throws Exception {
        Table<String, TableMetricAttributeFilter.NewRelicMetric, Boolean> table = new YamlMetricsAttributeTableSupplier(
                YamlMetricsAttributeTableSupplierTest.class.getResource("/testing-config.yml").toURI()).get();

        assertThat(table.get("name2", TableMetricAttributeFilter.NewRelicMetric.TIMER_MAX), equalTo(true));
        assertThat(table.get("name1", TableMetricAttributeFilter.NewRelicMetric.COUNTER_COUNT), equalTo(true));
    }

    @Test
    public void nullTableThrowsException(){
        try {
            new YamlMetricsAttributeTableSupplier(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), CoreMatchers.equalTo("yamlSource cannot be null"));
        }
    }
}