package com.liveramp.recruitment.workflow.domain.entity.work;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class WorkConfiguration {

    private Map<String, String> configurationMap = new ConcurrentHashMap<>();

}
