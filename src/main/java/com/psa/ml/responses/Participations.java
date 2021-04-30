package com.psa.ml.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "workspace_id", "role", "user_id", "can_invite", "permissions","id" })
public class Participations {

	@JsonProperty("workspace_id")
	public Integer workspaceId;
	@JsonProperty("role")
	public String role;
	@JsonProperty("user_id")
	public String userId;
	@JsonProperty("can_invite")
	public Boolean canInvite;
	@JsonProperty("permissions")
	public String permissions;
	@JsonProperty("id")
	public String id;

}