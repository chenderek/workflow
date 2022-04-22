package com.liveramp.recruitment.workflow.infrastructure.persistent;

import com.liveramp.recruitment.workflow.domain.Repository.TaskLogRepository;
import com.liveramp.recruitment.workflow.domain.entity.TaskLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by derche on 2022/4/22.
 */
@Repository
public class TaskLogRepositoryImpl implements TaskLogRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    private static final String TASK_LOG_COLLECTION_NAME = "taskLog";
    private static final String TASK_LOG_ID = "id";
    private static final String TASK_ID = "taskId";

    @Override
    public TaskLog save(TaskLog taskLog) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and(TASK_ID).is(taskLog.getTaskId());
        query.addCriteria(criteria);
        mongoTemplate.findAllAndRemove(query, TASK_LOG_COLLECTION_NAME);
        TaskLog result = mongoTemplate.save(taskLog, TASK_LOG_COLLECTION_NAME);
        return result;
    }

    @Override
    public TaskLog findOneByTaskId(String taskId) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and(TASK_ID).is(taskId);
        query.addCriteria(criteria);

        TaskLog result = mongoTemplate.findOne(query, TaskLog.class, TASK_LOG_COLLECTION_NAME);
        return result;
    }
}
