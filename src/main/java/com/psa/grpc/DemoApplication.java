package com.psa.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan
public class DemoApplication {

	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Value("${mavenlink.url.token}")  
	private String mavenlinkToken;
		
	/*
	 * @Bean public void startGrpc() { try {
	 * logger.info("initializing startGrpc ::::::::::::::::::::::"+mavenlinkToken);
	 * 
	 * int port = 50051; Server server = ServerBuilder.forPort(port).addService(new
	 * ConnectorService()).build().start(); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */
	 
	@Bean(name = "restTemplateWithProxy")
	public RestTemplate restTemplateWithProxy() {
       logger.info("initializing restTemplateWithProxy ::::::::::::::::::::::");
		//logger.info("Profile is " + profile);
		//logger.info("env.getActiveProfiles() " + env.getActiveProfiles());
		//logger.info("env.getDefaultProfiles() " + env.getDefaultProfiles());

		RestTemplate restTemplate = new RestTemplate();

		/*
		 * if (env.getProperty("profiles.active.prod").equalsIgnoreCase(profile) ||
		 * env.getProperty("profiles.active.stag").equalsIgnoreCase(profile)) {
		 * 
		 * logger.info("inside Proxy Template settings:::::::::::::: " +
		 * env.getProperty("profiles.active.prod"));
		 * 
		 * SimpleClientHttpRequestFactory requestFactory = new
		 * SimpleClientHttpRequestFactory(); Proxy proxy = new Proxy(Proxy.Type.HTTP,new
		 * InetSocketAddress(avayaWebproxy, Integer.parseInt(avayaWebproxyPort)));
		 * requestFactory.setProxy(proxy);
		 * restTemplate.setRequestFactory(requestFactory); }
		 */
		return restTemplate;
	}

	@Bean(name = "restTemplate")
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
}
