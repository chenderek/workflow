package com.liveramp.recruitment.workflow;

import com.liveramp.recruitment.workflow.application.WorkflowApplicationService;
import com.liveramp.recruitment.workflow.domain.entity.*;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkContext;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkReport;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkStatus;
import com.liveramp.recruitment.workflow.domain.entity.workflow.WorkFlow;
import com.liveramp.recruitment.workflow.infrastructure.util.WorkFlowEngineBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= WorkflowApplication.class)
public class WorkflowApplicationTests {


	@Test
    public void test(){
        List<WorkNode> workNodeList = new ArrayList<>();
        WorkNode workNode1 = new WorkNode();
        workNode1.setWorkId("1");
        workNode1.setWorkNodeJobType(WorkNodeJobType.WorkNodeJob1);
        workNodeList.add(workNode1);

        WorkNode workNode2 = new WorkNode();
        workNode2.setWorkId("2");
        workNode2.setWorkNodeJobType(WorkNodeJobType.WorkNodeJob2);
        workNodeList.add(workNode2);

        WorkNode workNode3 = new WorkNode();
        workNode3.setWorkId("3 PARALLEL");
        workNode3.setWorkExecutionModel(WorkExecutionModelEnum.PARALLEL);
        //并行 至少有一个成功
        workNode3.setWorkNodeFinishTypeEnum(WorkNodeFinishTypeEnum.ATLEASTONE);

        List<WorkNode> subWorkNodeList = new ArrayList<>();
        WorkNode workNode31 = new WorkNode();
        workNode31.setWorkId("3(1)");
        workNode31.setWorkNodeJobType(WorkNodeJobType.WorkNodeJob31);
        WorkNode workNode32 = new WorkNode();
        workNode32.setWorkId("3(2)");
        workNode32.setWorkNodeJobType(WorkNodeJobType.WorkNodeJob32);
        subWorkNodeList.add(workNode31);
        subWorkNodeList.add(workNode32);
        workNode3.setSubWorkNodeList(subWorkNodeList);
        workNodeList.add(workNode3);

        WorkNode workNode4 = new WorkNode();
        workNode4.setWorkId("4");
        workNode4.setWorkNodeJobType(WorkNodeJobType.WorkNodeJob4);
        workNodeList.add(workNode4);

        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setWorkExecutionModel(WorkExecutionModelEnum.SEQUENTIAL);
        task.setWorkNodeList(workNodeList);


        System.out.println("start");
        WorkFlow workFlow = WorkFlowEngineBuilder.build(task);
        WorkContext context = new WorkContext();
        context.setFlowId(task.getId());

        WorkReport workReport = workFlow.call(context);
        WorkStatus workStatus =  workReport.getStatus();

        if(WorkStatus.COMPLETED.name().equalsIgnoreCase(workStatus.name())){
            System.out.println("COMPLETED");
        }else{
            System.out.println("FAILED");
        }
    }

}
