package com.lightbend.farmtrust.farmitem.util.domain;

import com.akkaserverless.javasdk.eventsourcedentity.CommandContext;
import com.google.protobuf.Empty;
import com.lightbend.farmtrust.farmitem.util.PublishUtilApi;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.assertThrows;

public class PublishUtilTest {
    private String entityId = "entityId1";
    private PublishUtilImpl entity;
    private CommandContext context = Mockito.mock(CommandContext.class);
    
    private class MockedContextFailure extends RuntimeException {};
    
    @Test
    public void publishRatingTest() {
        entity = new PublishUtilImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `PublishRating` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.publishRatingWithReply(PublishUtilApi.PublishRatingMessage.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
}