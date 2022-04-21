package com.liveramp.recruitment.workflow.domain.Repository;

import com.liveramp.recruitment.workflow.domain.entity.Task;

/**
 * Created by derche on 2022/4/21.
 */
public interface TaskRepository {
    Task save(Task task);
    Task update(Task task);
    Task findOneById(String taskId);
}
