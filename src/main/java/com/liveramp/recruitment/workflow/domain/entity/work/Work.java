package com.liveramp.recruitment.workflow.domain.entity.work;

import org.pf4j.ExtensionPoint;

import java.util.UUID;

/**
 * This interface represents a unit of work. Implementations of this interface must:
 * 
 * <ul>
 *     <li>catch any checked or unchecked exceptions and return a  WorkReport
 *     instance with a status of  WorkStatus#FAILED and a reference to the exception</li>
 *     <li>make sure the work in finished in a finite amount of time</li>
 * </ul>
 *
 * Work name must be unique within a workflow definition.
 *
 */
public interface Work extends ExtensionPoint {

    /**
     * The name of the unit of work. The name must be unique within a workflow definition.
     * 
     * @return name of the unit of work.
     */
    default String getName() {
        return UUID.randomUUID().toString();
    }

    /**
     * Execute the unit of work and return its report. Implementations are required
     * to catch any checked or unchecked exceptions and return a WorkReport instance
     * with a status of WorkStatus#FAILED and a reference to the exception.
     * 
     * @param workContext jobContext in which this unit of work is being executed
     * @return the execution report
     */
    WorkReport call(WorkContext workContext);

    void putConfiguration(String flowId, WorkConfiguration workConfiguration);

    WorkConfiguration getConfiguration(String flowId);

    void removeConfiguration(String flowId);

    WorkConfiguration getWorkConfiguration(WorkContext workContext);
}
