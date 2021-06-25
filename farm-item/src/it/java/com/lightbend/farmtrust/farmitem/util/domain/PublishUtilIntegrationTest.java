package com.lightbend.farmtrust.farmitem.util.domain;

import com.lightbend.Main;
import com.lightbend.farmtrust.farmitem.util.PublishUtilServiceClient;
import com.akkaserverless.javasdk.testkit.junit.AkkaServerlessTestkitResource;
import org.junit.ClassRule;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.*;

// Example of an integration test calling our service via the Akka Serverless proxy
// Run all test classes ending with "IntegrationTest" using `mvn verify -Pit`
public class PublishUtilIntegrationTest {
    
    /**
     * The test kit starts both the service container and the Akka Serverless proxy.
     */
    @ClassRule
    public static final AkkaServerlessTestkitResource testkit = new AkkaServerlessTestkitResource(Main.SERVICE);
    
    /**
     * Use the generated gRPC client to call the service through the Akka Serverless proxy.
     */
    private final PublishUtilServiceClient client;
    
    public PublishUtilIntegrationTest() {
        client = PublishUtilServiceClient.create(testkit.getGrpcClientSettings(), testkit.getActorSystem());
    }
    
    @Test
    public void publishRatingOnNonExistingEntity() throws Exception {
        // TODO: set fields in command, and provide assertions to match replies
        // client.publishRating(PublishUtilApi.PublishRatingMessage.newBuilder().build())
        //         .toCompletableFuture().get(2, SECONDS);
    }
}