package com.psa.grpc.triggers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import grpc.ConnectorOuterClass.Event;
import grpc.ConnectorOuterClass.Event.Builder;
import grpc.ConnectorOuterClass.Field;
import grpc.ConnectorOuterClass.Trigger;
import grpc.ConnectorOuterClass.TriggerRequest;
import grpc.ConnectorOuterClass.TriggerResponse;
import com.psa.grpc.response.WorkspaceParticipations;
import com.psa.grpc.service.WorkflowTrigger;
import com.psa.grpc.utils.Constants.ActionTriggerProperties;
import com.psa.grpc.utils.RestServiceUtils;

@Service
public class WorkspaceParticipants  implements WorkflowTrigger{
	
	
	private static final Logger logger = LoggerFactory.getLogger(WorkspaceParticipants.class);

	@Autowired RestServiceUtils restServiceUtils ;// = new RestServiceUtils();

	@Override
	public Trigger registerTrigger() {
		Trigger.Builder  tr= Trigger.newBuilder();
		
		tr.setDisplayName(ActionTriggerProperties.DISPLAY_WOEKSPACE_PARTICIPANTS)
				.setAppKey(ActionTriggerProperties.APP_KEY)
				.setDescription(ActionTriggerProperties.DESC_WOEKSPACE_PARTICIPANTS)
				.setType(ActionTriggerProperties.TYPE_WOEKSPACE_PARTICIPANTS);
		
		Field.Builder workspaceIdField = Field.newBuilder();
		workspaceIdField.setDisplayName("Workspace id").setKey("workspace_id").setDescription("Workspace id").setType("text");
		tr.addOutputs(workspaceIdField.build());

		Field.Builder participantIdField = Field.newBuilder();
		participantIdField.setDisplayName("Participations id").setKey("participation_id").setDescription("Participations id").setType("text");
		tr.addOutputs(participantIdField.build());

		Field.Builder userIdField = Field.newBuilder();
		userIdField.setDisplayName("User Id").setKey("user_id").setDescription("User id").setType("text");
		tr.addOutputs(userIdField.build());
		
		Field.Builder roleField = Field.newBuilder();
		roleField.setDisplayName("Particpation Role").setKey("participation_role").setDescription("Particpation Role wether it is avaya user or customer").setType("text");
		tr.addOutputs(roleField.build());

		
		return tr.build();
	}

	@Override
	public TriggerResponse performTrigger(TriggerRequest request) {

		TriggerResponse.Builder response =  TriggerResponse.newBuilder();
		logger.info("Trigger Performed with details "+ request.toString());
		String lastPolledDate = request.getParams().get("last_polled_at");
		logger.info("lastPolledDate " + lastPolledDate);
		try {
			List<Builder> evenlist = response.getEventsBuilderList();
			logger.info("evenlist " + evenlist);
			logger.info("restServiceUtils " + restServiceUtils);

			List<WorkspaceParticipations> workspaceParticipationResp = restServiceUtils.getAllWorkspaceParticipations(lastPolledDate);

			for (WorkspaceParticipations workspaceParticipations : workspaceParticipationResp) {
				ObjectMapper mapper = new ObjectMapper();
				Event.Builder event = Event.newBuilder();
				try {
					event.setPayload(mapper.writeValueAsString(workspaceParticipations));
				} catch (JsonProcessingException e) {
					logger.error("PSA_GRPC_EXCEPTION : while convert object to json workspaceParticipations"+workspaceParticipations+ e.getMessage());
				}
				response.addEvents(event.build());
			}
			logger.info("Final evenlist " + response.getEventsList());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PSA_GRPC_EXCEPTION : while perform Trigger"+ getExceptionMessage(e));
			response.clearEvents().build();
			response.setErrorMessage("Exception while get workspace Participations "+getExceptionMessage(e));
		}
		return response.build();
	}

}
