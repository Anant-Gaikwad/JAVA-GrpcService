
package com.psa.grpc.client;

import grpc.ConnectorGrpc;
import grpc.ConnectorGrpc.ConnectorBlockingStub;
import grpc.ConnectorOuterClass.ActionRequest;
import grpc.ConnectorOuterClass.ActionResponse;
import grpc.ConnectorOuterClass.ActionsRequest;
import grpc.ConnectorOuterClass.ActionsResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GRPCClient {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("client programm started........");
		
	//	NettyChannelBuilder.forAddress("ec2-35-154-146-56.ap-south-1.compute.amazonaws.com", 5001).build();
		ManagedChannel channel = ManagedChannelBuilder.forAddress("ec2-35-154-146-56.ap-south-1.compute.amazonaws.com", 5001). usePlaintext().build();
		//ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5001). usePlaintext().build();

		System.out.println("ManagedChannel client built ........");

		ConnectorBlockingStub connectorStub = ConnectorGrpc.newBlockingStub(channel);
		System.out.println("ConnectorBlockingStub created ........");

		ActionRequest.Builder request = ActionRequest.newBuilder();
		request.getActionBuilder().setDisplayName("Concat Strings");
		request.getMutableParams().put("string1", "Hello");
		request.getMutableParams().put("string2", "World");
		System.out.println(" request :::::  " + request);
		
		
		ActionsRequest.Builder request2 = ActionsRequest.newBuilder();
		ActionsResponse response2 =connectorStub.actions(request2.build());
		System.out.println(" ActionsResponse :::::  "+ response2);


		ActionResponse response = connectorStub.performAction(request.build());
		System.out.println(" response :::::  " + response);
	}

}
