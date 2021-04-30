package com.psa.grpc.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import grpc.ConnectorOuterClass.Action;
import grpc.ConnectorOuterClass.ActionRequest;
import grpc.ConnectorOuterClass.ActionResponse;
import grpc.ConnectorOuterClass.ActionResponse.Status;
import grpc.ConnectorOuterClass.Field;
import com.psa.grpc.service.WorkflowAction;
import com.psa.grpc.utils.Constants.ActionTriggerProperties;
import com.psa.grpc.utils.RestServiceUtils;
import com.psa.ml.responses.Organization;
import com.psa.ml.responses.OrganizationMembership;
import com.psa.ml.responses.OrganizationMembershipResp;
import com.psa.ml.responses.OrganizationsResponse;
@Service
public class ValidateParticipants implements WorkflowAction {

	private static final Logger logger = LoggerFactory.getLogger(ValidateParticipants.class);

	@Override
	public Action registerAction() {
		Field.Builder userInput = Field.newBuilder();
		userInput.setDisplayName("User Id").setKey("user_id").setDescription("User Id").setType("text");
		
		Field.Builder workspaceInput = Field.newBuilder();
		workspaceInput.setDisplayName("Workspace Id").setKey("workspace_id").setDescription("Workspace Id").setType("text");
		
		Field.Builder participantInput = Field.newBuilder();
		participantInput.setDisplayName("Participation Id").setKey("participation_id").setDescription("Participation Id").setType("text");
		
		Field.Builder participantRoleInput = Field.newBuilder();
		participantRoleInput.setDisplayName("Particpation Role").setKey("participation_role").setDescription("Particpation Role wether it is avaya user or customer").setType("text");

		Field.Builder outputField = Field.newBuilder();
		outputField.setDisplayName("Result").setKey("result").setDescription("Result").setType("text");
		
		
        Action.Builder validateParticipantsAction = Action.newBuilder();
        validateParticipantsAction.setDisplayName(ActionTriggerProperties.DISPLAY_VALIDATE_AND_REMOVE_PARTICIPANTS).setDescription(ActionTriggerProperties.DESC_VALIDATE_AND_REMOVE_PARTICIPANTS);
        
        validateParticipantsAction.addInputs(workspaceInput.build());
        validateParticipantsAction.addInputs(userInput.build());
        validateParticipantsAction.addInputs(participantRoleInput.build());
        validateParticipantsAction.addInputs(participantInput.build());

        validateParticipantsAction.addOutputs(outputField.build());
        
        System.out.println("Register validate and Remove workspace participation from Server :" + validateParticipantsAction.build());
		return validateParticipantsAction.build();
	}
	
	@Autowired RestServiceUtils restUtils;


	private List<Organization> getCommonOrgs(List<Organization> orgDeptList,List<OrganizationMembership> workspaceOrgList, List<OrganizationMembership> userOrgList) {

		logger.info("inside user org and projects org validation");
		Map<String, Organization> workspaceOrgMap = new HashMap<>();
		Map<String, Organization> userOrgMap = new HashMap<>();
		for (OrganizationMembership workspaceOrg : workspaceOrgList) {
			// check current workspace org having any child org fetch all childs + current org
			Integer workspaceDepartmentId = Integer.parseInt(workspaceOrg.getDepartmentId());
			List<Organization> workspaceAllOrgs = orgDeptList.stream().filter(x -> x.getAncestorIds().contains(workspaceDepartmentId)|| x.getId().equalsIgnoreCase(workspaceOrg.getDepartmentId())).collect(Collectors.toList());
			workspaceAllOrgs.stream().forEach(x-> {  
				if (!workspaceOrgMap.containsKey(x.getId())) {
					workspaceOrgMap.put(x.getId(), x);
				}
			});
		}
		
		for (OrganizationMembership userOrg : userOrgList) {
			// check current workspace org having any child org fetch all childs + current org
			Integer userDepartmentId = Integer.parseInt(userOrg.getDepartmentId());
			List<Organization> userAllOrgs = orgDeptList.stream().filter(x -> x.getAncestorIds().contains(userDepartmentId)|| x.getId().equalsIgnoreCase(userOrg.getDepartmentId())).collect(Collectors.toList());
			userAllOrgs.stream().forEach(x-> {  
				if (!userOrgMap.containsKey(x.getId())) {
					userOrgMap.put(x.getId(), x);
				}
			});
		}
		
		logger.info("::::::::  Below are the workspace orgs :::::::::::::::::::::::::::::::::::");		
		workspaceOrgMap.entrySet().forEach(x-> logger.info(":--"+x));
		
		logger.info(":::::::: Below are the user orgs  :::::::::::::::::::::::::::::::::::");		
		userOrgMap.entrySet().forEach(x-> logger.info(":--"+x));
		
		List<Organization> workspaceOrgs =workspaceOrgMap.values().stream().collect(Collectors.toList());
		List<Organization> userOrgs =userOrgMap.values().stream().collect(Collectors.toList());
		List<Organization> commonOrgs = workspaceOrgs.stream().filter(x -> userOrgs.stream().anyMatch(y -> y.getId().equals(x.getId()))).collect(Collectors.toList());
		 logger.info("::::::::  Below Are common orgs between User and Workspace :::::::::::::::::::::::::::::::::::");
		 commonOrgs.forEach(x-> System.out.println(x));
		 
		return commonOrgs ;

	}

	@Override
	public ActionResponse performAction(ActionRequest request) {
		ActionResponse.Builder response = ActionResponse.newBuilder();
		String workspaceId =null;
		String userId =null;
		String participationId=null;
		String participationRole=null;
		try {
			
			workspaceId =request.getParamsMap().get("workspace_id");
			userId =  request.getParamsMap().get("user_id");
			participationId =request.getParamsMap().get("participation_id");
			participationRole =request.getParamsMap().get("participation_role");

			logger.info("restUtils obj:::::: "+restUtils);

			OrganizationsResponse orgMstResponse=restUtils.getOrgnizationMst();
			logger.info("orgMstResponse obj:::::: "+orgMstResponse);

			if (orgMstResponse != null && orgMstResponse.getOrganizations() != null) {

				List<Organization> orgMstList = orgMstResponse.getOrganizations().values().stream().filter(x->"department".equalsIgnoreCase(x.getType())) .collect(Collectors.toList());
				logger.info("orgMstList obj:::::: "+orgMstList);

				Organization fedralOrg = orgMstList.stream().filter(x -> "Federal".equalsIgnoreCase(x.getName())).findFirst().get();
				logger.info("fedralOrg obj:::::: "+fedralOrg);
				Map<String, Object>uriVariables2 = new HashMap<String, Object>();
				uriVariables2.put("member_type", "workspace");
				uriVariables2.put("member_id", workspaceId);
				OrganizationMembershipResp workspaceOrgResponse=restUtils.getOrgnizationMemeberships(uriVariables2);
				List<OrganizationMembership> workspaceOrgList=workspaceOrgResponse.getOrganizationMemberships().values().stream().collect(Collectors.toList());
				logger.info("workspaceOrgList obj:::::: "+workspaceOrgList);
				
				
				if ("buyer".equalsIgnoreCase(participationRole)) {
					
					Optional<OrganizationMembership> fedralOrgCheck=workspaceOrgList.stream().filter(x->fedralOrg.getId().equalsIgnoreCase(x.getDepartmentId())).findAny();
					if (fedralOrgCheck.isPresent()){
						//This is federal project and we are checking user is customer then remove customer users participants
						logger.info("Workspace found with federal org:::::: "+fedralOrgCheck.get());
						restUtils.deleteParticipations(participationId);					
						response.setStatus(Status.SUCCESS);
						response.putOutputs("result", "Removed Participant,because participants was customer and project  organization is Federal");
						return response.build();
						
					}
					
				}else {
					
					Map<String, Object>uriVariables = new HashMap<String, Object>();
					uriVariables.put("member_type", "user");
					uriVariables.put("member_id", userId);
					OrganizationMembershipResp userOrgResponse=restUtils.getOrgnizationMemeberships(uriVariables);
					List<OrganizationMembership> userOrgList=userOrgResponse.getOrganizationMemberships().values().stream().collect(Collectors.toList());
					logger.info("userOrgList obj:::::: "+userOrgList);
					
				   //check project org and user org are same or not, if it is different then remove that participants
				   List<Organization> commonOrgs=getCommonOrgs(orgMstList, workspaceOrgList, userOrgList);
				   if (commonOrgs ==null || commonOrgs.size() ==0) {
						logger.info("User and project having differnt orgs, need to remove participant here::::: "+participationId);
						
						restUtils.deleteParticipations(participationId);
						response.setStatus(Status.SUCCESS);
						response.putOutputs("result", "Removed Participant,because participants and projects organizations are differnet");

				   }else {
						logger.info("User and project sharing same orgs, no need to do any action. Common Orgs :"+ commonOrgs);
						response.setStatus(Status.SUCCESS);
						response.putOutputs("result", "No Action Required validParticipant");
				   }
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(Status.HALT);
			response.putOutputs("result", "Exception while validate and Reomve Participants "+getExceptionMessage(e) );
			logger.error("Exception while validate and remove user from workspace"+ getExceptionMessage(e));
		}
		return response.build();
	}

}
