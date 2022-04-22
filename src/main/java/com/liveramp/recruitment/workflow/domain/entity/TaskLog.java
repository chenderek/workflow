package com.liveramp.recruitment.workflow.domain.entity;

import com.liveramp.recruitment.workflow.domain.entity.work.WorkReport;
import lombok.Data;

import java.util.List;

/**
 * Created by derche on 2022/4/22.
 */
@Data
public class TaskLog {
    private String id;
    private String taskId;
    private List<WorkReport> workReportList;
}
