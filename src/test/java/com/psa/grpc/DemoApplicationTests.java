/*
 * package com.psa.grpc;
 * 
 * import org.junit.Test; import org.junit.runner.RunWith; import
 * org.springframework.boot.test.context.SpringBootTest; import
 * org.springframework.test.context.junit4.SpringRunner;
 * 
 * import com.grpc.psa.protoBuf.ConnectorOuterClass.ActionRequest; import
 * com.grpc.psa.protoBuf.ConnectorOuterClass.ActionResponse; import
 * com.psa.grpc.utils.Constants;
 * 
 * @RunWith(SpringRunner.class)
 * 
 * @SpringBootTest public class DemoApplicationTests {
 * 
 * 
 * @Test void testSayHello() {
 * 
 * 
 * ActionRequest.Builder request = ActionRequest.newBuilder();
 * request.getActionBuilder().setDisplayName(Constants.CONCAT_STRINGS);
 * request.getMutableParams().put("string1", "Anant");
 * request.getMutableParams().put("string2", "Gaikwad");
 * System.out.println(" request :::::  "+ request);
 * 
 * //ActionResponse response =connectorStub.performAction(request.build());
 * //System.out.printlnz" response :::::  "+ response); HelloWorldClient
 * helloWorldClient = new HelloWorldClient();
 * assertThat(helloWorldClient.sayHello("John", "Doe"))
 * .isEqualTo("Hello John Doe!"); }
 * 
 * }
 */