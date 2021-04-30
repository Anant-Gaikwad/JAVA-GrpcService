package com.psa.grpc.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import grpc.ConnectorOuterClass.Action;
import grpc.ConnectorOuterClass.ActionRequest;
import grpc.ConnectorOuterClass.ActionResponse;
import com.psa.grpc.service.WorkflowAction;
import com.psa.grpc.utils.Constants;

@Service
public class SayHello implements WorkflowAction {
	private static final Logger logger = LoggerFactory.getLogger(SayHello.class);
	@Override
	public synchronized Action registerAction() {
		logger.info("inside register Action");		
		
		Action.Builder sayHello = Action.newBuilder();
		sayHello.setDisplayName(Constants.SAY_HELLO).setDescription("Hello User Test Action"); // getInputsList().add(inputField.build());
		sayHello.addInputsBuilder().setDisplayName("Full Name").setKey("full_name").setDescription("Users Full Name")
				.setType("text");
		sayHello.addOutputsBuilder().setDisplayName("Salutation").setKey("salutation")
				.setDescription("User-specific salutation").setType("text");

		return sayHello.build();
	}

	@SuppressWarnings("deprecation")
	@Override
	public synchronized ActionResponse performAction(ActionRequest request) {
		logger.info("inside Server performAction with request  +"+ request);
		ActionResponse.Builder response = ActionResponse.newBuilder();
		response.putOutputs("salutation", "Hello there " + request.getParams().get("full_name").toString());
		return response.build();
	}

}
