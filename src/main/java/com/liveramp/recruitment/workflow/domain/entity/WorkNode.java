package com.liveramp.recruitment.workflow.domain.entity;

import com.liveramp.recruitment.workflow.domain.entity.work.WorkConfiguration;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by derche on 2022/4/21.
 */
@Data
public class WorkNode extends WorkConfiguration {
    private String workId;
    //default SEQUENTIAL
    private WorkExecutionModelEnum workExecutionModel = WorkExecutionModelEnum.SEQUENTIAL;
    //default ALL
    private WorkNodeFinishTypeEnum workNodeFinishTypeEnum = WorkNodeFinishTypeEnum.ALL;

    private WorkNodeJobType workNodeJobType;

    private List<WorkNode> subWorkNodeList = new ArrayList<>();

}
