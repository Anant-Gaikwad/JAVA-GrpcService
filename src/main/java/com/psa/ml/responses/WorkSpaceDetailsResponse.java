package com.psa.ml.responses;

import java.util.Map;

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
public class WorkSpaceDetailsResponse {

	private Map<Integer,WorkSpaceDetails> workspaces;
	private String errorMsg;
	private int count;


}
