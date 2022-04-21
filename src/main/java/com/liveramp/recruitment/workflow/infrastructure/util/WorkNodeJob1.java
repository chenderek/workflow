package com.liveramp.recruitment.workflow.infrastructure.util;

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

//    @Autowired


    @Override
    public WorkReport call(WorkContext workContext) {
        WorkReport workReport = null;

        try {
            //TODO
            System.out.println("running WorkNodeJob1");
        } catch (Exception e) {
            workReport = new DefaultWorkReport(WorkStatus.FAILED, workContext, e);
            return workReport;
        }

        workReport = new DefaultWorkReport(WorkStatus.COMPLETED, workContext);

        return workReport;
    }
}