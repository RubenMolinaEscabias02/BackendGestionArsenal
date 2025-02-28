package org.example.Dynamo;

import jdk.jfr.Name;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
@NoArgsConstructor
@Configuration
public class DynamoDBConfig {

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.EU_WEST_3)
                .endpointOverride(URI.create(System.getenv("SPRING_DB_URL")))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(System.getenv("SPRING_DB_ACCESS_KEY_ID"), System.getenv("SPRING_DB_SECRET_ACCESS_KEY"))
                ))
                .build();
    }
}