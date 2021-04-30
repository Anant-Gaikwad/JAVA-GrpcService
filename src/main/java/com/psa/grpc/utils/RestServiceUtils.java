package com.psa.grpc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.psa.grpc.response.WorkspaceParticipations;
import com.psa.ml.responses.OrganizationMembershipResp;
import com.psa.ml.responses.OrganizationsResponse;
import com.psa.ml.responses.ParticipantsData;
import com.psa.ml.responses.Participations;
import com.psa.ml.responses.WorkSpaceDetails;
import com.psa.ml.responses.WorkSpaceDetailsResponse;

@Component
public class RestServiceUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(RestServiceUtils.class);
	
	
	@Value("${mavenlink.url.token}")  
	private String mavenlinkToken;
	
	@Value("${mavenlink.url.participant}")
	private String workspaceParticipantUrl;
	
	@Value("${mavenlink.url.workspaceDetails}")
	private String workspaceDetailsUrl;
	
	@Value("${mavenlink.url.orgMembership}")
	private String orgMembership;
	
	@Value("${mavenlink.url.orgMaster}")
	private String orgMaster;
	
	@Autowired RestTemplate restTemplateWithProxy;
	

	public synchronized ParticipantsData getParticipations(Map<String, Object> uriVariables) throws Exception {
		Thread.sleep(1000);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
     	headers.setBearerAuth(mavenlinkToken);
		
		
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(workspaceParticipantUrl);
		for (Map.Entry<String, Object> entry : uriVariables.entrySet()) {
			builder.queryParam(entry.getKey(), entry.getValue());
		}
		ResponseEntity<ParticipantsData> response = restTemplateWithProxy.exchange(builder.build().toString(), HttpMethod.GET, entity, ParticipantsData.class);
		ParticipantsData responseJson = response.getBody();
		logger.info("getParticipations Json: " + response);
		return responseJson;
	}
	
	public synchronized String deleteParticipations(String participationId) throws Exception {
		Thread.sleep(1000);


		
		HttpEntity<String> entity = new HttpEntity<>(getAuthenticatedHttpHeaders(Constants.MAVEN_LINK));
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(workspaceParticipantUrl);
		builder.pathSegment(participationId);
		logger.info("delete Participation API : " + builder.build().toString());

		ResponseEntity<String> response = restTemplateWithProxy.exchange(builder.build().toString(), HttpMethod.DELETE, entity,String.class);
		String responseJson = response.getBody();
		logger.info("delete Participation response Json: " + response);
		return responseJson;
	}
	
	public  synchronized List<WorkSpaceDetails> getAllWorkspaceDetails() throws Exception{

		List<WorkSpaceDetails> workspaces = new ArrayList<>();
		// Get workspace details
		WorkSpaceDetailsResponse responseJson = fecthWorkspaceList("1");
		int workspaceCnt = responseJson.getCount();
		int iterationRequired = workspaceCnt / 200;
		logger.info("iterationRequired "+iterationRequired);

		if (responseJson !=null && responseJson.getWorkspaces() !=null) {
			workspaces.addAll(responseJson.getWorkspaces().values());
			logger.info("added into workspaces "+workspaces);
		}
		if (iterationRequired > 0) {
			for (int i = 1; i <= iterationRequired; i++) {
				responseJson = fecthWorkspaceList(String.valueOf(i + 1));
				if (responseJson !=null && responseJson.getWorkspaces() !=null) {
					workspaces.addAll(responseJson.getWorkspaces().values());
				}
			}
		}
		logger.info("Final workspaces  "+workspaces);

		return workspaces;
	}	
	
	public synchronized WorkSpaceDetailsResponse fecthWorkspaceList(String pageNumber) throws Exception {		
		HttpEntity<Object>requestObj = new HttpEntity<>(getAuthenticatedHttpHeaders(Constants.MAVEN_LINK));
		logger.info("fecthWorkspaceList pageNumber"+pageNumber);
		logger.info("fecthWorkspaceList workspaceDetailsUrl"+workspaceDetailsUrl);

		ResponseEntity<WorkSpaceDetailsResponse> mlObjectWorkspace = restTemplateWithProxy.exchange(workspaceDetailsUrl.replace("{0}",pageNumber), HttpMethod.GET,requestObj,WorkSpaceDetailsResponse.class);
		WorkSpaceDetailsResponse responseJson = mlObjectWorkspace.getBody();
		logger.info("WorkSpaceDetailsResponse responseJson "+responseJson);

		return responseJson;
	}

	
	public synchronized HttpHeaders getAuthenticatedHttpHeaders(String target) {
		HttpHeaders headers = new HttpHeaders();
		String jwttoken = null;
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (target != null && target.equalsIgnoreCase(Constants.MAVEN_LINK)) {
			jwttoken =   mavenlinkToken;
			headers.add("Authorization", "bearer " + jwttoken);
		} 
		return headers;
	}
@Autowired Environment env;
	public synchronized List<WorkspaceParticipations> getAllWorkspaceParticipations(String lastPolledDate) throws Exception {
		
		List<WorkspaceParticipations>  responseList= new ArrayList<>();
		
		List<WorkSpaceDetails> workspaces=getAllWorkspaceDetails();
		for (WorkSpaceDetails workSpaceDetails : workspaces) {
			
			Map<String, Object> uriVariables = new HashMap<>();
			uriVariables.put("workspace_id", workSpaceDetails.getId());
			uriVariables.put("per_page", 200);
			uriVariables.put("created_after", lastPolledDate);
		    logger.info("uriVariables"+ uriVariables);
			ParticipantsData participationResp =getParticipations(uriVariables);
			if (participationResp !=null && participationResp.getParticipations() !=null) {
				for (Entry<Integer, Participations> entry : participationResp.getParticipations() .entrySet()) {
					Participations participations =entry.getValue();
					if (env.getProperty("mavenlink.integration.userId").equalsIgnoreCase(participations.getUserId())) {
						logger.info("This participant user is integration user so we have to skip this user");
						continue;
					}
					WorkspaceParticipations respone= new WorkspaceParticipations();
					respone.setWorkspaceId(workSpaceDetails.getId());
					respone.setParticipationId(participations.getId());
					respone.setUserId(participations.getUserId());
					respone.setParticipationRole(participations.getRole());
					responseList.add(respone);
				}
			}
		}
		return responseList;
		
	}

	public synchronized OrganizationsResponse getOrgnizationMst() throws Exception {

		Thread.sleep(1000);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
     	headers.setBearerAuth(mavenlinkToken);
		
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orgMaster);

		ResponseEntity<OrganizationsResponse> response = restTemplateWithProxy.exchange(builder.build().toString(), HttpMethod.GET, entity, OrganizationsResponse.class);
		OrganizationsResponse responseJson = response.getBody();
		logger.info("getOrgnizationMst Json: " + response);
		return responseJson;

	}
	
	public synchronized OrganizationMembershipResp getOrgnizationMemeberships(Map<String, Object> uriVariables) throws Exception {

		Thread.sleep(1000);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
     	headers.setBearerAuth(mavenlinkToken);
		
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(orgMembership);
		for (Map.Entry<String, Object> entry : uriVariables.entrySet()) {
			builder.queryParam(entry.getKey(), entry.getValue());
		}
		logger.info("getOrgnizationMst request Builder: " + builder.build().toString());

		ResponseEntity<OrganizationMembershipResp> response = restTemplateWithProxy.exchange(builder.build().toString(), HttpMethod.GET, entity, OrganizationMembershipResp.class);
		OrganizationMembershipResp responseJson = response.getBody();
		logger.info("OrganizationMemberships Json: " + response);
		return responseJson;

	}

	
	
}
