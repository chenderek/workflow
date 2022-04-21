package com.liveramp.recruitment.workflow.domain.entity.workflow;


import com.liveramp.recruitment.workflow.domain.entity.WorkNodeFinishTypeEnum;
import com.liveramp.recruitment.workflow.domain.entity.work.Work;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkContext;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkReport;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * A parallel flow executes a set of work units in parallel. A {@link ParallelFlow}
 * requires a {@link ExecutorService} to run work units in parallel using multiple
 * threads.
 * 
 * <strong>It is the responsibility of the caller to manage the lifecycle of the
 * executor service.</strong>
 *
 * The status of a parallel flow execution is defined as:
 *
 *
 */
public class ParallelFlow extends AbstractWorkFlow {

    private List<Work> works = new ArrayList<>();
    private ParallelFlowExecutor workExecutor;
    private WorkNodeFinishTypeEnum workNodeFinishTypeEnum;

    ParallelFlow(String name, List<Work> works, ParallelFlowExecutor parallelFlowExecutor, WorkNodeFinishTypeEnum workNodeFinishTypeEnum) {
        super(name);
        this.works.addAll(works);
        this.workExecutor = parallelFlowExecutor;
        this.workNodeFinishTypeEnum = workNodeFinishTypeEnum;
    }

    /**
     * {@inheritDoc}
     */
    public ParallelFlowReport call(WorkContext workContext) {
        ParallelFlowReport workFlowReport = new ParallelFlowReport();
        List<WorkReport> workReports = workExecutor.executeInParallel(works, workContext);
        workFlowReport.addAll(workReports);
        if(workNodeFinishTypeEnum == null || WorkNodeFinishTypeEnum.ALL.name().equalsIgnoreCase(workNodeFinishTypeEnum.name())){
            //全部成功
            WorkStatus allStatus = workFlowReport.getAllStatus();
            workFlowReport.setStatus(allStatus);
        }else{
            //至少一个成功
            WorkStatus atLeastOneStatus = workFlowReport.getAtLeastOneStatus();
            workFlowReport.setStatus(atLeastOneStatus);
        }

        return workFlowReport;
    }

    public static class Builder {

        private String name;
        private List<Work> works;
        private ExecutorService executorService;
        private WorkNodeFinishTypeEnum workNodeFinishTypeEnum;

        private Builder(ExecutorService executorService, WorkNodeFinishTypeEnum workNodeFinishTypeEnum) {
            this.name = UUID.randomUUID().toString();
            this.works = new ArrayList<>();
            this.executorService = executorService;
            this.workNodeFinishTypeEnum = workNodeFinishTypeEnum;
        }

        /**
         *  Create a new {@link ParallelFlow} builder. A {@link ParallelFlow}
         *  requires a {@link ExecutorService} to run work units in parallel
         *  using multiple threads.
         *  
         *  <strong>It is the responsibility of the caller to manage the lifecycle
         *  of the executor service.</strong>
         *  
         * @param executorService to use to run work units in parallel
         * @return a new {@link ParallelFlow} builder
         */
        public static ParallelFlow.Builder aNewParallelFlow(ExecutorService executorService, WorkNodeFinishTypeEnum workNodeFinishTypeEnum) {
            return new ParallelFlow.Builder(executorService, workNodeFinishTypeEnum);
        }

        public ParallelFlow.Builder named(String name) {
            this.name = name;
            return this;
        }

        public ParallelFlow.Builder execute(Work... works) {
            this.works.addAll(Arrays.asList(works));
            return this;
        }

        public ParallelFlow build() {
            return new ParallelFlow(name, works, new ParallelFlowExecutor(executorService), workNodeFinishTypeEnum);
        }
    }
}
