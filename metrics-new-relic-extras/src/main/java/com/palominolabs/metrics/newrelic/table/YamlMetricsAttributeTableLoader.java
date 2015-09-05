package com.palominolabs.metrics.newrelic.table;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.palominolabs.metrics.newrelic.table.TableMetricAttributeFilter.NewRelicMetric;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Supplier of a {@link Table} created by reading a yaml file defined as follows:
 *
 * <pre>
 *     metricName1:
 *          TIMER_MAX: true
 *          TIMER_MIN: false
 *     metricName2:
 *          COUNTER_COUNT: true
 * </pre>
 *
 * See {@link TableMetricAttributeFilter.NewRelicMetric} for all the metric options.
 */
@Immutable
public class YamlMetricsAttributeTableLoader {

    @Nonnull
    private final ObjectReader objectReader;

    /**
     * Make a loader with default deserialization.
     */
    public YamlMetricsAttributeTableLoader() {
        this(new ObjectMapper(new YAMLFactory()).reader());
    }

    /**
     * @param objectReader reader to use to deserialize yaml
     */
    public YamlMetricsAttributeTableLoader(@Nonnull ObjectReader objectReader) {
        this.objectReader = objectReader;
    }

    /**
     * @param inputStream yaml data
     * @return Table of metric toggles
     * @throws IOException if loading fails
     */
    public Table<String, NewRelicMetric, Boolean> loadTable(@Nonnull InputStream inputStream) throws IOException {
        Map<String, Map<NewRelicMetric, Boolean>> m =
                objectReader.forType(new TypeReference<Map<String, Map<NewRelicMetric, Boolean>>>() {})
                        .readValue(inputStream);
        return table(m);
    }

    private static <R, C, V> Table<R, C, V> table(Map<R, Map<C, V>> fromTable) {
        Table<R, C, V> table = HashBasedTable.create();
        for (R rowKey : fromTable.keySet()) {
            Map<C, V> rowMap = fromTable.get(rowKey);
            for (C columnKey : rowMap.keySet()) {
                V value = rowMap.get(columnKey);
                table.put(rowKey, columnKey, value);
            }
        }
        return table;
    }
}
