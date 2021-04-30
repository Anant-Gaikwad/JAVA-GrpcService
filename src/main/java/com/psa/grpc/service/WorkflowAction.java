package com.psa.grpc.service;

import java.io.PrintWriter;
import java.io.StringWriter;

import grpc.ConnectorOuterClass.Action;
import grpc.ConnectorOuterClass.ActionRequest;
import grpc.ConnectorOuterClass.ActionResponse;

public interface WorkflowAction {

	 public Action registerAction();
     public ActionResponse performAction(ActionRequest request);
     
 	default String getExceptionMessage(Throwable e) {
 		StringBuilder builder = new StringBuilder();
 		StringWriter errors = new StringWriter();
 		e.printStackTrace(new PrintWriter(errors));
 		builder.append(errors.toString());
 		String errMsg = builder.toString();
 		return errMsg.length() < 4000 ? errMsg : errMsg.substring(0, 3999);
 	}
}
