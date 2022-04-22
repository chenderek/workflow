package com.liveramp.recruitment.workflow.application;

import com.liveramp.recruitment.workflow.domain.Repository.TaskLogRepository;
import com.liveramp.recruitment.workflow.domain.Repository.TaskRepository;
import com.liveramp.recruitment.workflow.domain.entity.*;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkContext;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkReport;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkStatus;
import com.liveramp.recruitment.workflow.domain.entity.workflow.WorkFlow;
import com.liveramp.recruitment.workflow.infrastructure.util.WorkFlowEngineBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by derche on 2022/4/21.
 */
@Service
public class WorkflowApplicationService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskLogRepository taskLogRepository;

    public Task saveTask(Task task){
        if(task != null) {
            if(!StringUtils.hasText(task.getId())){
                task.setId(UUID.randomUUID().toString());
            }

            return taskRepository.save(task);
        }else{
            return null;
        }

    }

    public Task addWorkNode(String taskId, WorkNode workNode) {
        Task task = taskRepository.findOneById(taskId);
        if(task != null){
            List<WorkNode> workNodeList = CollectionUtils.isEmpty(task.getWorkNodeList())? new ArrayList<>(): task.getWorkNodeList();
            workNodeList.add(workNode);
            task.setWorkNodeList(workNodeList);

            return taskRepository.save(task);
        }else{
            return null;
        }

    }

    public TaskLog getLog(String taskId){
        if(StringUtils.hasText(taskId)){
            TaskLog taskLog = taskLogRepository.findOneByTaskId(taskId);
            return taskLog;
        }
        return null;
    }

    @Async
    public void reprocess(String taskId){
        WorkFlow workFlow = WorkFlowEngineBuilder.get(taskId);
        if(null == workFlow){
            Task task = taskRepository.findOneById(taskId);
            if(task!= null) {
                workFlow = WorkFlowEngineBuilder.build(task);
            }
        }
        if(workFlow != null){
            WorkContext context = new WorkContext();
            context.setFlowId(taskId);

            WorkReport workReport = workFlow.call(context);
            WorkStatus workStatus =  workReport.getStatus();

            if(WorkStatus.COMPLETED.name().equalsIgnoreCase(workStatus.name())){
                System.out.println("COMPLETED");
            }else{
                System.out.println("FAILED");
            }
        }

    }

    @Async
    public void process(String taskId){
        Task task = taskRepository.findOneById(taskId);
        if(task != null){
            WorkFlow workFlow = WorkFlowEngineBuilder.build(task);
            WorkContext context = new WorkContext();
            context.setFlowId(taskId);

            WorkReport workReport = workFlow.call(context);
            WorkStatus workStatus =  workReport.getStatus();

            if(WorkStatus.COMPLETED.name().equalsIgnoreCase(workStatus.name())){
                System.out.println("COMPLETED");
            }else{
                System.out.println("FAILED");
            }
        }

    }

    @Async
    public void process() {
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
        //并行 至少又一个成功
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
        workNode4.setWorkNodeJobType(WorkNodeJobType.WorkNodeJob1);
        workNodeList.add(workNode4);

        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setWorkExecutionModel(WorkExecutionModelEnum.SEQUENTIAL);
        task.setWorkNodeList(workNodeList);

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
