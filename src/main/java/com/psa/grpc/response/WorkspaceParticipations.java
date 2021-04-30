package com.psa.grpc.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class WorkspaceParticipations {
	
	
	@JsonProperty("workspace_id")
	private String workspaceId;
	
	@JsonProperty("user_id")
	private String userId;
	
	@JsonProperty("participation_id")
	private String participationId;
	
	@JsonProperty("participation_role")
	private String participationRole;
}
