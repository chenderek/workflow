package com.liveramp.recruitment.workflow.domain.entity;

import lombok.Data;

import java.util.List;

/**
 * Created by derche on 2022/4/21.
 */
@Data
public class Task {
    private String id;
    private String name;
    private WorkExecutionModelEnum workExecutionModel;

    private List<WorkNode> workNodeList;
}
