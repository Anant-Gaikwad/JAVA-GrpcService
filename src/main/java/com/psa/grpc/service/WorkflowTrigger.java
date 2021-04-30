package com.psa.grpc.service;

import java.io.PrintWriter;
import java.io.StringWriter;

import grpc.ConnectorOuterClass.Trigger;
import grpc.ConnectorOuterClass.TriggerRequest;
import grpc.ConnectorOuterClass.TriggerResponse;

public interface WorkflowTrigger {

	  public abstract Trigger registerTrigger();
      public abstract TriggerResponse performTrigger(TriggerRequest request);
      


  	default String getExceptionMessage(Throwable e) {
  		StringBuilder builder = new StringBuilder();
  		StringWriter errors = new StringWriter();
  		e.printStackTrace(new PrintWriter(errors));
  		builder.append(errors.toString());
  		String errMsg = builder.toString();
  		return errMsg.length() < 4000 ? errMsg : errMsg.substring(0, 3999);
  	}
}
