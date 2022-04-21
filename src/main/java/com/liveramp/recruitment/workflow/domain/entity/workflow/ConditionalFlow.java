package com.liveramp.recruitment.workflow.domain.entity.workflow;



import com.liveramp.recruitment.workflow.domain.entity.work.*;

import java.util.UUID;

/**
 * A conditional flow is defined by 4 artifacts:
 *
 * <ul>
 *     <li>The work to execute first</li>
 *     <li>A predicate for the conditional logic</li>
 *     <li>The work to execute if the predicate is satisfied</li>
 *     <li>The work to execute if the predicate is not satisfied (optional)</li>
 * </ul>
 *
 * @see ConditionalFlow.Builder
 *
 */
public class ConditionalFlow extends AbstractWorkFlow {

    private Work toExecute, nextOnPredicateSuccess, nextOnPredicateFailure;
    private WorkReportPredicate predicate;

    ConditionalFlow(String name, Work toExecute, Work nextOnPredicateSuccess, Work nextOnPredicateFailure, WorkReportPredicate predicate) {
        super(name);
        this.toExecute = toExecute;
        this.nextOnPredicateSuccess = nextOnPredicateSuccess;
        this.nextOnPredicateFailure = nextOnPredicateFailure;
        this.predicate = predicate;
    }

    /**
     * {@inheritDoc}
     */
    public WorkReport call(WorkContext workContext) {
        WorkReport jobReport = toExecute.call(workContext);
        if (predicate.apply(jobReport)) {
            jobReport = nextOnPredicateSuccess.call(workContext);
        } else {
            if (nextOnPredicateFailure != null && !(nextOnPredicateFailure instanceof NoOpWork)) { // else is optional
                jobReport = nextOnPredicateFailure.call(workContext);
            }
        }
        return jobReport;
    }

    public static class Builder {

        private String name;
        private Work toExecute, nextOnPredicateSuccess, nextOnPredicateFailure;
        private WorkReportPredicate predicate;

        private Builder() {
            this.name = UUID.randomUUID().toString();
            this.toExecute = new NoOpWork();
            this.nextOnPredicateSuccess = new NoOpWork();
            this.nextOnPredicateFailure = new NoOpWork();
            this.predicate = WorkReportPredicate.ALWAYS_FALSE;
        }

        public static ConditionalFlow.Builder aNewConditionalFlow() {
            return new ConditionalFlow.Builder();
        }

        public ConditionalFlow.Builder named(String name) {
            this.name = name;
            return this;
        }

        public ConditionalFlow.Builder execute(Work work) {
            this.toExecute = work;
            return this;
        }

        public ConditionalFlow.Builder when(WorkReportPredicate predicate) {
            this.predicate = predicate;
            return this;
        }

        public ConditionalFlow.Builder then(Work work) {
            this.nextOnPredicateSuccess = work;
            return this;
        }

        public ConditionalFlow.Builder otherwise(Work work) {
            this.nextOnPredicateFailure = work;
            return this;
        }

        public ConditionalFlow build() {
            return new ConditionalFlow(name, toExecute, nextOnPredicateSuccess, nextOnPredicateFailure, predicate);
        }
    }
}
