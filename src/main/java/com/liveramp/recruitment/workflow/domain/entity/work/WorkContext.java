package com.liveramp.recruitment.workflow.domain.entity.work;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Work execution jobContext. This can be used to pass initial parameters to the
 * workflow and share data between work units.
 *
 *
 */
public class WorkContext {

	private String flowId;

	private Map<String, String> inputContext = new ConcurrentHashMap<>();

	private Map<String, String> outputContext = new ConcurrentHashMap<>();

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getFlowId() {
		return this.flowId;
	}

	public void putInputContext(Map<String, String> params) {
		inputContext.putAll(params);
	}

	public void putOutputContext(Map<String, String> params) {
		outputContext.putAll(params);
	}

	public Map<String, String> getInputContext() {
		return inputContext;
	}

	public Map<String, String> getOutputContext() {
		return outputContext;
	}
}
