package com.entiy;

import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
public class IPEntiy implements Serializable {

    private static final Long serialVersionUID = 8918858618799627841L;

    public Integer ipInteger;

    public String ip;

    public Integer ipType;

    public Integer ipPort;

    public Set<Integer> portSet;

    public Map<String, Long> dataSourceMap;

    public String portSets;

    public String isp;

    public Long number;

    public String geoIp;

    public String desc;

    public Long foundTime;
    //首次发现时间
    public Date foundTimeDate;

    public Long updateTime;
    //最近发现时间
    public Date updateTimeDate;

    public String version;

    public String dataSource;

    public String tag;

    public Set<Integer> addPortSets(Integer ipPort) {
        if (this.portSet == null) {
            portSet = new HashSet<>();
        }
        this.portSet.add(ipPort);
        return this.portSet;
    }

    public Map<String, Long> addDataSourceMap(String dataSource) {
        if (this.dataSourceMap == null) {
            this.dataSourceMap = new HashMap<>();
            this.dataSourceMap.put(dataSource, 1L);
        } else {
            this.dataSourceMap.put(dataSource, this.dataSourceMap.getOrDefault(dataSource, 0L) + 1);
        }
        return this.dataSourceMap;
    }

    public void addAllPortSets(Set<Integer> portSet) {
        this.portSet.addAll(portSet);
    }

    public void addAllDataSourceMap(Map<String, Long> paramMap) {
        paramMap.forEach((key, value) -> this.dataSourceMap.merge(key, value, Long::sum));
    }
}
