package com.psa.ml.responses;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@Builder
@AllArgsConstructor()
@NoArgsConstructor()
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationMembershipResp {
	
	private int count;
	@JsonProperty("organization_memberships")
	private Map<String, OrganizationMembership> organizationMemberships;

}
