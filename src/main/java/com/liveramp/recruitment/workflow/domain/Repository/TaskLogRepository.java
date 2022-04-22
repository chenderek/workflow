package com.liveramp.recruitment.workflow.domain.Repository;

import com.liveramp.recruitment.workflow.domain.entity.TaskLog;

/**
 * Created by derche on 2022/4/22.
 */
public interface TaskLogRepository {
    public TaskLog save(TaskLog taskLog);
    public TaskLog findOneByTaskId(String taskId);
}
