package com.liveramp.recruitment.workflow.domain.entity.work;

/**
 * Default implementation of {@link WorkReport}.
 *
 */
public class DefaultWorkReport implements WorkReport {

    private WorkStatus status;
    private WorkContext workContext;
    private Throwable error;

    /**
     * Create a new {@link DefaultWorkReport}.
     *
     * @param status of work
     */
    public DefaultWorkReport(WorkStatus status, WorkContext workContext) {
        this.status = status;
        this.workContext = workContext;
    }

    /**
     * Create a new {@link DefaultWorkReport}.
     *
     * @param status of work
     * @param error if any
     */
    public DefaultWorkReport(WorkStatus status, WorkContext workContext, Throwable error) {
        this(status, workContext);
        this.error = error;
    }

    public WorkStatus getStatus() {
        return status;
    }

    public Throwable getError() {
        return error;
    }

    @Override
    public WorkContext getWorkContext() {
        return workContext;
    }

    @Override
    public String toString() {
        return "DefaultWorkReport {" +
                "status=" + status +
                ", jobContext=" + workContext +
                ", error=" + (error == null ? "''" : error) +
                '}';
    }
}
