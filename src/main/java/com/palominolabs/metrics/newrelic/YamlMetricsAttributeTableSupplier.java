package com.palominolabs.metrics.newrelic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.palominolabs.metrics.newrelic.TableMetricAttributeFilter.NewRelicMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * Supplier of a {@link Table} created by reading a yaml file defined as follows:
 * <pre>
 *     metricName1:
 *          TIMER_MAX: true
 *          TIMER_MIN: false
 *     metricName2:
 *          COUNTER_COUNT: true
 * </pre>
 * <p/>
 */
public class YamlMetricsAttributeTableSupplier implements Supplier<Table<String, NewRelicMetric, Boolean>> {

    private static final Logger LOG = LoggerFactory.getLogger(YamlMetricsAttributeTableSupplier.class);
    private final URI yamlSource;

    public YamlMetricsAttributeTableSupplier(@Nonnull URI yamlSource) {
        Preconditions.checkArgument(yamlSource != null, "yamlSource cannot be null");
        this.yamlSource = yamlSource;
    }

    @Override
    public Table<String, NewRelicMetric, Boolean> get() {

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            Map<String, Map<NewRelicMetric, Boolean>> o = mapper
                    .readValue(yamlSource.toURL(), new TypeReference<Map<String, Map<NewRelicMetric, Boolean>>>() {});
            return table(o);
        } catch (IOException e) {
            LOG.error("Could not parse NewRelic metrics config file. Check expected format", e);
            throw new IllegalArgumentException("NewRelic metrics config file has wrong format", e);
        }

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
