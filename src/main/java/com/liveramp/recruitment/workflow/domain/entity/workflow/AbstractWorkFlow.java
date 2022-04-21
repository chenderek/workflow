package com.liveramp.recruitment.workflow.domain.entity.workflow;



import com.liveramp.recruitment.workflow.domain.entity.work.WorkConfiguration;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractWorkFlow implements WorkFlow {

    private Map<String, WorkConfiguration> workConfigurationMap = new ConcurrentHashMap<>();


    private String name;

    public AbstractWorkFlow() {
    }

    AbstractWorkFlow(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void putConfiguration(String flowId, WorkConfiguration workConfiguration) {
        workConfigurationMap.put(flowId, workConfiguration);
    }

    public WorkConfiguration getConfiguration(String flowId) {
        return workConfigurationMap.get(flowId);
    }

    public void removeConfiguration(String flowId){
        workConfigurationMap.remove(flowId);
    }

    public WorkConfiguration getWorkConfiguration(WorkContext workContext) {
        if(this.workConfigurationMap != null) {
            return this.workConfigurationMap.get(workContext.getFlowId());
        }
        return null;
    }

}
