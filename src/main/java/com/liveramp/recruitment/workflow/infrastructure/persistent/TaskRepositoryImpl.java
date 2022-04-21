package com.liveramp.recruitment.workflow.infrastructure.persistent;

import com.liveramp.recruitment.workflow.domain.Repository.TaskRepository;
import com.liveramp.recruitment.workflow.domain.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by derche on 2022/4/21.
 */
@Repository
public class TaskRepositoryImpl implements TaskRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    private static final String TASK_COLLECTION_NAME = "task";
    private static final String TASK_ID = "id";

    @Override
    public Task save(Task task) {
//        Query query = new Query();
//        Criteria criteria = new Criteria();
//        criteria.and(TASK_ID).is(task.getId());
//        query.addCriteria(criteria);
//        mongoTemplate.findAllAndRemove(query, TASK_COLLECTION_NAME);
        Task result = mongoTemplate.save(task, TASK_COLLECTION_NAME);
        return result;
    }

    @Override
    public Task update(Task task) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and(TASK_ID).is(task.getId());
        query.addCriteria(criteria);
        mongoTemplate.findAllAndRemove(query, TASK_COLLECTION_NAME);
        Task result = mongoTemplate.save(task, TASK_COLLECTION_NAME);
        return result;
    }

    @Override
    public Task findOneById(String taskId) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and(TASK_ID).is(taskId);
        query.addCriteria(criteria);
        Task result = mongoTemplate.findOne(query, Task.class, TASK_COLLECTION_NAME);
        return result;
    }
}
