package com.psa.ml.responses;

import java.util.List;

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
public class Organization {
	
	private String id;
	private String name;
	private String type;
	@JsonProperty("parent_id")
	private String parentId;
	@JsonProperty("ancestor_ids")
	private List<Integer> ancestorIds;
	
}
