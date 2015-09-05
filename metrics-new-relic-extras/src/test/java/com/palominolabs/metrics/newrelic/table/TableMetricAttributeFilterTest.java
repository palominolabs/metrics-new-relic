package com.palominolabs.metrics.newrelic.table;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.palominolabs.metrics.newrelic.AllDisabledMetricAttributeFilter;
import com.palominolabs.metrics.newrelic.AllEnabledMetricAttributeFilter;
import com.palominolabs.metrics.newrelic.table.TableMetricAttributeFilter.NewRelicMetric;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TableMetricAttributeFilterTest {

    private final Timer timer = new Timer();

    @Test
    public void metricSetToFalseInConfigFile() throws Exception {
        TableMetricAttributeFilter instance =
                new TableMetricAttributeFilter(getTable("testing-config.yml"),
                        new AllEnabledMetricAttributeFilter());

        assertThat(instance.recordTimerMin("name2", timer), equalTo(false));
    }

    @Test
    public void metricSetToTrueInConfigFile() throws Exception {
        TableMetricAttributeFilter instance = new TableMetricAttributeFilter(getTable("testing-config.yml"),
                new AllDisabledMetricAttributeFilter());

        assertThat(instance.recordTimerMax("name2", timer), equalTo(true));
        assertThat(instance.recordCounterCount("name1", new Counter()), equalTo(true));
    }

    @Test
    public void metricNotDefinedInConfigFile_useFallback() throws Exception {
        TableMetricAttributeFilter instance = new TableMetricAttributeFilter(getTable("testing-config.yml"),
                new AllDisabledMetricAttributeFilter());

        assertThat(instance.recordTimer15MinuteRate("name2", timer), equalTo(false));
        assertThat(instance.recordTimerMax("name_notDefined", timer), equalTo(false));
    }

    @Test
    public void nullFallback_useAllDisabledMetricAttributeFilter() {
        Table<String, NewRelicMetric, Boolean> metricConfig = ImmutableTable.<String, NewRelicMetric, Boolean>builder()
                .put("metricName1", NewRelicMetric.TIMER_MAX, true)
                .put("metricName1", NewRelicMetric.TIMER_MIN, false)
                .put("metricName2", NewRelicMetric.COUNTER_COUNT, true)
                .build();

        TableMetricAttributeFilter instance =
                new TableMetricAttributeFilter(metricConfig, null);

        assertThat(instance.recordTimer15MinuteRate("metricName1", timer), equalTo(false));
        assertThat(instance.recordTimerMax("metricName1", timer), equalTo(true));
        assertThat(instance.recordTimer75thPercentile("metricName3", timer), equalTo(false));
        assertThat(instance.recordTimer95thPercentile("metricName3", timer), equalTo(false));
        assertThat(instance.recordTimer99thPercentile("metricName3", timer), equalTo(false));
        assertThat(instance.recordTimer999thPercentile("metricName3", timer), equalTo(false));
        assertThat(instance.recordTimer1MinuteRate("metricName3", timer), equalTo(false));
        assertThat(instance.recordTimer5MinuteRate("metricName3", timer), equalTo(false));
    }

    @Test
    public void nullTable_throwException() {
        try {
            new TableMetricAttributeFilter(null, new AllEnabledMetricAttributeFilter());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), equalTo("table cannot be null"));
        }
    }

    private Table<String, NewRelicMetric, Boolean> getTable(String resource) throws IOException {
        return new YamlMetricsAttributeTableLoader().loadTable(getClass().getResourceAsStream(resource));
    }
}
