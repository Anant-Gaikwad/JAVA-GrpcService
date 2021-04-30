package com.psa.grpc.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import grpc.ConnectorOuterClass.Action;
import grpc.ConnectorOuterClass.ActionRequest;
import grpc.ConnectorOuterClass.ActionResponse;
import grpc.ConnectorOuterClass.Field;
import com.psa.grpc.service.WorkflowAction;
import com.psa.grpc.utils.Constants;

@Service
public class ConcatTwoStrings  implements WorkflowAction {
	private static final Logger logger = LoggerFactory.getLogger(ConcatTwoStrings.class);

	@Override
	public Action registerAction() {
		Field.Builder inputFirstStringField = Field.newBuilder();
		inputFirstStringField.setDisplayName("string 1").setKey("string1").setDescription("String 1").setType("text");
		
		Field.Builder inputSecondStringField = Field.newBuilder();
		inputSecondStringField.setDisplayName("string 2").setKey("string2").setDescription("String 2").setType("text");


		Field.Builder outputConactedStringField = Field.newBuilder();
		outputConactedStringField.setDisplayName("Concatenated String").setKey("concatenatedString").setDescription("Concatenated String").setType("text");
		
		
        Action.Builder concatenateStringsAction = Action.newBuilder();
        concatenateStringsAction.setDisplayName(Constants.CONCAT_STRINGS).setDescription("Concatenates two strings together");
        
        concatenateStringsAction.addInputs(inputFirstStringField.build());
        concatenateStringsAction.addInputs(inputSecondStringField.build());
        concatenateStringsAction.addOutputs(outputConactedStringField.build());
        
       logger.info("Register Response from Server :" + concatenateStringsAction.build());
		return concatenateStringsAction.build();


	}

	@Override
	public ActionResponse performAction(ActionRequest request) {
		logger.info("inside Server performAction with request  +"+ request);
		ActionResponse.Builder response = ActionResponse.newBuilder();
		response.putOutputs("concatenatedString", request.getParams().get("string1").toString().concat(request.getParams().get("string2")));
		return response.build();
	}

}
