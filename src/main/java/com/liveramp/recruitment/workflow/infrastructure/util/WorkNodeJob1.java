package com.liveramp.recruitment.workflow.infrastructure.util;

import com.liveramp.recruitment.workflow.domain.entity.WorkNode;
import com.liveramp.recruitment.workflow.domain.entity.work.DefaultWorkReport;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkContext;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkReport;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkStatus;
import com.liveramp.recruitment.workflow.domain.entity.workflow.AbstractWorkFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by derche on 2022/4/22.
 */
@Component("WorkNodeJob1")
public class WorkNodeJob1 extends AbstractWorkFlow {

    @Autowired
    WorkNodeJobService workNodeJobService;

    @Override
    public WorkReport call(WorkContext workContext) {
        WorkReport workReport = null;

        try {
            //TODO

            String taskId = workContext.getFlowId();
            WorkNode workNode = (WorkNode)this.getConfiguration(taskId);
            System.out.println("running job1 on workNode: " + workNode.getWorkId());
            workNodeJobService.execute();
        } catch (Exception e) {
            workReport = new DefaultWorkReport(WorkStatus.FAILED, workContext, e);
            return workReport;
        }

        workReport = new DefaultWorkReport(WorkStatus.COMPLETED, workContext);

        workNodeJobService.saveLog(workContext.getFlowId(), workReport);

        return workReport;
    }
}
