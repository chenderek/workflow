package com.liveramp.recruitment.workflow.domain.entity.workflow;



import com.liveramp.recruitment.workflow.domain.entity.work.Work;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkContext;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkReport;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * A sequential flow executes a set of work units in sequence.
 *
 * If a init of work fails, next work units in the pipeline will be skipped.
 *
 */
public class SequentialFlow extends AbstractWorkFlow {

    private static final Logger LOGGER = Logger.getLogger(SequentialFlow.class.getName());

    private List<Work> works = new ArrayList<>();

    SequentialFlow(String name, List<Work> works) {
        super(name);
        this.works.addAll(works);
    }

    /**
     * {@inheritDoc}
     */
    public WorkReport call(WorkContext workContext) {
        WorkReport workReport = null;
        for (Work work : works) {
            workReport = work.call(workContext);
            if (workReport != null && WorkStatus.FAILED.equals(workReport.getStatus())) {
                LOGGER.log(Level.INFO, "Work unit ''{0}'' has failed, skipping subsequent work units", work.getName());
                break;
            }
        }
        return workReport;
    }

    public static class Builder {

        private String name;
        private List<Work> works;

        private Builder() {
            this.name = UUID.randomUUID().toString();
            this.works = new ArrayList<>();
        }

        public static SequentialFlow.Builder aNewSequentialFlow() {
            return new SequentialFlow.Builder();
        }

        public SequentialFlow.Builder named(String name) {
            this.name = name;
            return this;
        }

        public SequentialFlow.Builder execute(Work work) {
            this.works.add(work);
            return this;
        }

        public SequentialFlow.Builder then(Work work) {
            this.works.add(work);
            return this;
        }

        public SequentialFlow build() {
            return new SequentialFlow(name, works);
        }
    }
}
