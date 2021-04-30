package com.psa.ml.responses;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

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
public class OrganizationsResponse {

	private Map<String, Organization> organizations;
	private int count;
}
