package com.liveramp.recruitment.workflow.infrastructure.util;

import com.liveramp.recruitment.workflow.domain.entity.Task;
import com.liveramp.recruitment.workflow.domain.entity.WorkExecutionModelEnum;
import com.liveramp.recruitment.workflow.domain.entity.WorkNode;
import com.liveramp.recruitment.workflow.domain.entity.WorkNodeFinishTypeEnum;
import com.liveramp.recruitment.workflow.domain.entity.work.Work;
import com.liveramp.recruitment.workflow.domain.entity.workflow.ParallelFlow;
import com.liveramp.recruitment.workflow.domain.entity.workflow.SequentialFlow;
import com.liveramp.recruitment.workflow.domain.entity.workflow.WorkFlow;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class WorkFlowEngineBuilder implements ApplicationContextAware {

    private static Map<String, WorkFlow> workFlowMap = new ConcurrentHashMap<String, WorkFlow>();

    public static WorkFlow build(Task task) {

        if(task == null || CollectionUtils.isEmpty(task.getWorkNodeList())) {
            return null;
        }

        SequentialFlow.Builder sequentialFlowBuilder = SequentialFlow.Builder.aNewSequentialFlow();

        String flowId = task.getId();
        List<WorkNode> workNodeList = task.getWorkNodeList();
        for(WorkNode workNode : workNodeList) {
            Work work = getEngineWork(flowId, workNode);
            sequentialFlowBuilder.execute(work);
        }

        WorkFlow workflow = sequentialFlowBuilder.build();
        workFlowMap.put(task.getId(), workflow);
        return workflow;
    }

    public static WorkFlow destroy(String flowId) {
        WorkFlow workFlow = workFlowMap.get(flowId);
        workFlowMap.remove(flowId);
        return workFlow;
    }

    public static WorkFlow get(String flowId) {
        return workFlowMap.get(flowId);
    }

    private static Work getEngineWork(String taskId, WorkNode workNode) {
        List<WorkNode> subWorkNodeList = workNode.getSubWorkNodeList();

        Work work = null;
        if(!CollectionUtils.isEmpty(subWorkNodeList)) {
            if(workNode.getWorkExecutionModel() == WorkExecutionModelEnum.SEQUENTIAL) {
                work = getSequentialWork(taskId, subWorkNodeList);
            } else {
                WorkNodeFinishTypeEnum workNodeFinishType = workNode.getWorkNodeFinishTypeEnum();
                work = getParallelWork(taskId, subWorkNodeList, workNodeFinishType);
            }

        } else {
//            WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
            Object object = applicationContext.getBean(workNode.getWorkNodeJobType().name());
            //register bean from spring jobContext
            if(object != null && object instanceof Work) {
                work = (Work) object;
            } else {
                //register bean from pf4j
                //TODO
            }
            work.putConfiguration(taskId, workNode);
        }

        return work;
    }


    private static Work getSequentialWork(String flowId, List<WorkNode> workNodeList) {

        SequentialFlow.Builder sequentialFlowBuilder = SequentialFlow.Builder.aNewSequentialFlow();

        for(WorkNode subWorkNode : workNodeList) {
            sequentialFlowBuilder.execute(getEngineWork(flowId, subWorkNode));
        }

        return sequentialFlowBuilder.build();
    }

    private static Work getParallelWork(String taskId, List<WorkNode> workNodeList, WorkNodeFinishTypeEnum workNodeFinishTypeEnum) {

        ExecutorService executorService = Executors.newFixedThreadPool(workNodeList.size());
        ParallelFlow.Builder parallelFlowBuilder = ParallelFlow.Builder.aNewParallelFlow(executorService, workNodeFinishTypeEnum);

        for(WorkNode subWorkNode : workNodeList) {
            parallelFlowBuilder.execute(getEngineWork(taskId, subWorkNode));
        }

        return parallelFlowBuilder.build();
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(WorkFlowEngineBuilder.applicationContext == null) {
            WorkFlowEngineBuilder.applicationContext = applicationContext;
        }
    }
}
