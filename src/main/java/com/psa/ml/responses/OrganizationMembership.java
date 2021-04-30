package com.psa.ml.responses;

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
public class OrganizationMembership {
	
	@JsonProperty("member_id")
	private String memberId;
	
	@JsonProperty("member_type")
	private String memberType;
	
	@JsonProperty("geography_id")
	private String geographyId;
	
	@JsonProperty("department_id")
	private String departmentId;
	
	private String id;

}
