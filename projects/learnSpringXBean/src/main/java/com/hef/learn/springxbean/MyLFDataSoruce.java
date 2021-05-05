package com.hef.learn.springxbean;

import java.util.Collection;
import java.util.Map;

/**
 * @org.apache.xbean.XBean element="data-source" rootElement="true"
 * @org.apache.xbean.Defaults {code:xml}
 */
public class MyLFDataSoruce {

    private Map<String, XDataSource> dataSourceMap;

    private Collection<XRuleConfiguration> configurations;

    public MyLFDataSoruce(final Map<String, XDataSource> dataSourceMap, Collection<XRuleConfiguration> configurations) {
        this.dataSourceMap = dataSourceMap;
        this.configurations = configurations;
    }

    public Collection<XRuleConfiguration> getConfigurations() {
        return configurations;
    }

    public Map<String, XDataSource> getDataSourceMap() {
        return dataSourceMap;
    }
}
