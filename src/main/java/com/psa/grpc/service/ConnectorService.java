package com.psa.grpc.service;

import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import grpc.ConnectorGrpc.ConnectorImplBase;
import grpc.ConnectorOuterClass.ActionRequest;
import grpc.ConnectorOuterClass.ActionResponse;
import grpc.ConnectorOuterClass.ActionsRequest;
import grpc.ConnectorOuterClass.ActionsResponse;
import grpc.ConnectorOuterClass.TriggerRequest;
import grpc.ConnectorOuterClass.TriggerResponse;
import grpc.ConnectorOuterClass.TriggersRequest;
import grpc.ConnectorOuterClass.TriggersResponse;
import com.psa.grpc.actions.ConcatTwoStrings;
import com.psa.grpc.actions.SayHello;
import com.psa.grpc.actions.ValidateParticipants;
import com.psa.grpc.triggers.WorkspaceParticipants;
import com.psa.grpc.utils.Constants;
import com.psa.grpc.utils.Constants.ActionTriggerProperties;

import io.grpc.stub.StreamObserver;
@GRpcService
public class ConnectorService  extends ConnectorImplBase{

	private static final Logger logger = LoggerFactory.getLogger(ConnectorService.class);

	@Autowired  WorkspaceParticipants workspaceParticipants;
    @Autowired ValidateParticipants participants;
	
	@Override
	public void triggers(TriggersRequest request, StreamObserver<TriggersResponse> responseObserver) {
		TriggersResponse.Builder  triggerResponse =  TriggersResponse.newBuilder();	
		//If created new triggers then add it into response as below
		triggerResponse.addTriggers(workspaceParticipants.registerTrigger());
		
		responseObserver.onNext(triggerResponse.build());
		responseObserver.onCompleted();
		
	}
	@Override
	public void actions(ActionsRequest request, StreamObserver<ActionsResponse> responseObserver) {

		ActionsResponse.Builder actionsResponse = ActionsResponse.newBuilder();
		//If created new action then add it into response as below
		actionsResponse.addActions(new SayHello().registerAction());
		actionsResponse.addActions(new ConcatTwoStrings().registerAction());
		actionsResponse.addActions(new ValidateParticipants().registerAction());
		
		responseObserver.onNext(actionsResponse.build());
		responseObserver.onCompleted();
	}
	
	@Override
	public void performAction(ActionRequest request, StreamObserver<ActionResponse> responseObserver) {
		
		ActionResponse response = ActionResponse.newBuilder().build();
		
		
		switch (request.getAction().getDisplayName().toString()) {
		case Constants.SAY_HELLO:
			response = new SayHello().performAction(request);
			break;
		case Constants.CONCAT_STRINGS:
			response = new ConcatTwoStrings().performAction(request);
			break;
		case Constants.ActionTriggerProperties.DISPLAY_VALIDATE_AND_REMOVE_PARTICIPANTS:
			response = participants.performAction(request);
			break;

		default:
			break;
		}
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	@Override
	public void performTrigger(TriggerRequest request, StreamObserver<TriggerResponse> responseObserver) {
        logger.info("trigger type: " + request.getTrigger().getType());
        logger.info("trigger workspaceParticipants: " + workspaceParticipants);

        TriggerResponse response =TriggerResponse.newBuilder().build();
        
        switch (request.getTrigger().getType().toString()) {
		case ActionTriggerProperties.TYPE_WOEKSPACE_PARTICIPANTS:
			response = workspaceParticipants.performTrigger(request);
			break;

		default:
			break;
		}
		
		responseObserver.onNext(response);
		responseObserver.onCompleted();

	}
	
}
