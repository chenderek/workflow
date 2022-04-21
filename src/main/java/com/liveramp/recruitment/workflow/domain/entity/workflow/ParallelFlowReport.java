package com.liveramp.recruitment.workflow.domain.entity.workflow;


import com.liveramp.recruitment.workflow.domain.entity.work.WorkContext;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkReport;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Aggregate report of the partial reports of work units executed in a parallel flow.
 *
 */
public class ParallelFlowReport implements WorkReport {

    private List<WorkReport> reports;

    /**
     * Create a new {@link ParallelFlowReport}.
     */
    public ParallelFlowReport() {
        this(new ArrayList<>());
    }

    /**
     * Create a new {@link ParallelFlowReport}.
     * 
     * @param reports of works executed in parallel
     */
    public ParallelFlowReport(List<WorkReport> reports) {
        this.reports = reports;
    }

    /**
     * Get partial reports.
     *
     * @return partial reports
     */
    public List<WorkReport> getReports() {
        return reports;
    }

    void add(WorkReport workReport) {
        reports.add(workReport);
    }

    void addAll(List<WorkReport> workReports) {
        reports.addAll(workReports);
    }

    /**
     * Return the status of the parallel flow.
     *
     * The status of a parallel flow is defined as follows:
     *
     * <ul>
     *     <li>{@link WorkStatus#COMPLETED}: If all work units have successfully completed</li>
     *     <li>{@link WorkStatus#FAILED}: If one of the work units has failed</li>
     * </ul>
     * @return workflow status
     */
    @Override
    public WorkStatus getStatus() {
        for (WorkReport report : reports) {
            if (report.getStatus().equals(WorkStatus.FAILED)) {
                return WorkStatus.FAILED;
            }
        }
        return WorkStatus.COMPLETED;
    }

    /**
     * Return the first error of partial reports.
     *
     * @return the first error of partial reports.
     */
    @Override
    public Throwable getError() {
        for (WorkReport report : reports) {
            Throwable error = report.getError();
            if (error != null) {
                return error;
            }
        }
        return null;
    }

    /**
     * The parallel flow jobContext is the union of all partial contexts. In a parallel
     * flow, each work unit should have its own unique keys to avoid key overriding
     * when merging partial contexts.
     * 
     * @return the union of all partial contexts
     */
    @Override
    public WorkContext getWorkContext() {
        WorkContext workContext = new WorkContext();
        for (WorkReport report : reports) {
            WorkContext partialContext = report.getWorkContext();
            for (Map.Entry<String, String> entry : partialContext.getOutputContext().entrySet()) {
                workContext.getOutputContext().put(entry.getKey(), entry.getValue());
            }
        }
        return workContext;
    }
}
