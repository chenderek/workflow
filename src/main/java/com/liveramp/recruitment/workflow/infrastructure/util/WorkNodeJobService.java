package com.liveramp.recruitment.workflow.infrastructure.util;

import com.liveramp.recruitment.workflow.domain.Repository.TaskLogRepository;
import com.liveramp.recruitment.workflow.domain.entity.TaskLog;
import com.liveramp.recruitment.workflow.domain.entity.work.WorkReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by derche on 2022/4/22.
 */
@Service
public class WorkNodeJobService {

    @Autowired
    TaskLogRepository taskLogRepository;

    public TaskLog saveLog(String taskId, WorkReport workReport){
        TaskLog taskLog = taskLogRepository.findOneByTaskId(taskId);
        if(taskLog == null){
            taskLog = new TaskLog();
            taskLog.setId(UUID.randomUUID().toString());
            taskLog.setTaskId(taskId);
        }

        List<WorkReport> workReportList = CollectionUtils.isEmpty(taskLog.getWorkReportList())? new ArrayList<>() : taskLog.getWorkReportList();
        workReportList.add(workReport);
        taskLog.setWorkReportList(workReportList);

        return taskLogRepository.save(taskLog);
    }

    public void execute(){

    }
}
