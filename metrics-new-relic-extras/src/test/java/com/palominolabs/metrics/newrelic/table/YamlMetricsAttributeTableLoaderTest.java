package com.palominolabs.metrics.newrelic.table;

import com.google.common.collect.Table;
import java.io.InputStream;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class YamlMetricsAttributeTableLoaderTest {

    @Test
    public void getReturnTableReadFromFile() throws Exception {
        YamlMetricsAttributeTableLoader loader = new YamlMetricsAttributeTableLoader();
        InputStream stream = getClass().getResourceAsStream("testing-config.yml");
        Table<String, TableMetricAttributeFilter.NewRelicMetric, Boolean> table = loader.loadTable(stream);

        assertThat(table.get("name2", TableMetricAttributeFilter.NewRelicMetric.TIMER_MAX), equalTo(true));
        assertThat(table.get("name1", TableMetricAttributeFilter.NewRelicMetric.COUNTER_COUNT), equalTo(true));
    }
}
