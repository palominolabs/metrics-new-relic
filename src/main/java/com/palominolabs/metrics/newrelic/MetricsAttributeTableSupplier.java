package com.palominolabs.metrics.newrelic;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Table;

import javax.annotation.Nonnull;

/**
 * Supplier of a {@link Table} containing if a metrics attribute is enabled or disabled.<br>
 * Table must be defined as follows:
 * <ul>
 * <li>Row key: metricName</li>
 * <li>Column key: NewRelic metric</li>
 * <li>Value: enabled/disabled</li>
 * </ul>
 */
public class MetricsAttributeTableSupplier implements Supplier<Table<String, TableMetricAttributeFilter.NewRelicMetric, Boolean>> {

    private final Table<String, TableMetricAttributeFilter.NewRelicMetric, Boolean> table;

    public MetricsAttributeTableSupplier(
            @Nonnull Table<String, TableMetricAttributeFilter.NewRelicMetric, Boolean> table) {
        Preconditions.checkArgument(table != null, "table cannot be null");
        this.table = table;
    }

    @Override
    public Table<String, TableMetricAttributeFilter.NewRelicMetric, Boolean> get() {
        return table;
    }
}
