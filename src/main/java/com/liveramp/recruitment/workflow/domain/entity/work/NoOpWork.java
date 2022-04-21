package com.liveramp.recruitment.workflow.domain.entity.work;

import java.util.UUID;

/**
 * No operation work.
 *
 */
public class NoOpWork implements Work {

    @Override
    public String getName() {
        return UUID.randomUUID().toString();
    }

    @Override
    public WorkReport call(WorkContext workContext) {
        return new DefaultWorkReport(WorkStatus.COMPLETED, workContext);
    }

    @Override
    public void putConfiguration(String flowId, WorkConfiguration workConfiguration) {

    }

    @Override
    public WorkConfiguration getConfiguration(String flowId) {
        return null;
    }

    @Override
    public void removeConfiguration(String flowId) {

    }

    @Override
    public WorkConfiguration getWorkConfiguration(WorkContext workContext) {
        return null;
    }

}
