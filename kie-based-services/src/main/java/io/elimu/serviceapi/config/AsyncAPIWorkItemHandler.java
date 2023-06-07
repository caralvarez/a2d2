package io.elimu.serviceapi.config;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.runtime.process.WorkflowProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.GenericMessage;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.elimu.a2d2.genericmodel.ServiceRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

public class AsyncAPIWorkItemHandler implements WorkItemHandler {

	@Autowired
	SqsClient sqsClient;

	@Value("${sqs.queue.url}")
	private String queueUrl;

	private KieSession ksession;
	private static final Logger LOG = LoggerFactory.getLogger(ConfigAPIUtil.class);

	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;

	public AsyncAPIWorkItemHandler(KieSession ksession) {
		this.ksession = ksession;
	}

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		String appname = (String) workItem.getParameter("appName");
		String operation = (String) workItem.getParameter("operation");
		String tenant = (String) workItem.getParameter("tenant");
		String resourceType = (String) workItem.getParameter("resourceType");
		String body = (String) workItem.getParameter("body"); // TODO: This could be a variable not parameter.
		if (operation.equals("put")) {
			String resourceId = (String) workItem.getParameter("resourceId");
		}

		WorkflowProcessInstance pI = (WorkflowProcessInstance) ksession
				.getProcessInstance(workItem.getProcessInstanceId());

		Map<String, String> attributeMap = new HashMap<>();
		attributeMap.put("appname", appname);
		attributeMap.put("operation", operation);
		attributeMap.put("tenant", tenant);
		attributeMap.put("resourceType", resourceType);

		sendMessageToSQS(body, attributeMap);
		manager.completeWorkItem(workItem.getId(), workItem.getResults());
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

	}

	public String sendMessageToSQS(String body, Map<String, String> attributes) {
		Map<String, MessageAttributeValue> attributeMap = new HashMap<>();
		if (attributes != null) {
			for (String key : attributes.keySet()) {
				MessageAttributeValue valString = MessageAttributeValue.builder().stringValue(attributes.get(key))
						.dataType("String").build();
				attributeMap.put(key, valString);
			}
		}
		SendMessageRequest sendMessageRequest = SendMessageRequest.builder().messageBody(body).queueUrl(queueUrl)
				.messageAttributes(attributeMap).build();
		SendMessageResponse response = sqsClient.sendMessage(sendMessageRequest);
		return response.messageId();
	}
}
