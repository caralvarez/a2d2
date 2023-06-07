package io.elimu.serviceapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AWSConfig {
	@Value("${amazon.aws.region:us-west-2}")
    private String amazonAWSRegion;
	
	@Bean
	public SqsClient sqsClient() {
		return SqsClient.builder()
				.credentialsProvider(DefaultCredentialsProvider.create())
				.region(Region.of(amazonAWSRegion))
				.httpClientBuilder(UrlConnectionHttpClient.builder())
				.build();
	}
}
