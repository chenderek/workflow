package com.liveramp.recruitment.workflow.domain.entity.workflow;


import com.liveramp.recruitment.workflow.domain.entity.work.Work;

/**
 * Interface to define a flow of work units. A workflow is also a work, this is
 * what makes workflows composable.
 *
 */
public interface WorkFlow extends Work {
}
