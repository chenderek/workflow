package com.liveramp.recruitment.workflow.interfaces;

import com.liveramp.recruitment.workflow.application.WorkflowApplicationService;
import com.liveramp.recruitment.workflow.domain.entity.Task;
import com.liveramp.recruitment.workflow.domain.entity.WorkNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by derche on 2022/4/21.
 */
@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    @Autowired
    WorkflowApplicationService workflowApplicationService;

    @PostMapping("/save")
    @ResponseBody
    public Object saveTask(@RequestBody Task task){
        return workflowApplicationService.saveTask(task);
    }

    @PostMapping("{taskId}/workNode/add")
    @ResponseBody
    public Object addWorkNode(@PathVariable("taskId")String taskId, @RequestBody WorkNode workNode){

        return workflowApplicationService.addWorkNode(taskId, workNode);
    }

    @GetMapping("{taskId}/process")
    public String process(@PathVariable("taskId")String taskId){
        workflowApplicationService.process(taskId);
        return "running";
    }

    @GetMapping("{taskId}/rerun")
    public String reprocess(@PathVariable("taskId")String taskId){
        workflowApplicationService.reprocess(taskId);
        return "running";
    }

    @GetMapping("/testProcess")
    public String testProcess(){
        workflowApplicationService.process();
        return "running";
    }



}
