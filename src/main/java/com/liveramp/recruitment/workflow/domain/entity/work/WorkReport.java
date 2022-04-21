package com.liveramp.recruitment.workflow.domain.entity.work;

/**
 * Execution report of a unit of work.
 *
 */
public interface WorkReport {

    WorkStatus getStatus();

    Throwable getError();

    WorkContext getWorkContext();

}
