package com.lightbend.farmtrust.farmitem.domain;

import com.akkaserverless.javasdk.valueentity.CommandContext;
import com.google.protobuf.Empty;
import com.lightbend.farmtrust.farmitem.FarmItemApi;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.assertThrows;

public class FarmItemTest {
    private String entityId = "entityId1";
    private FarmItemImpl entity;
    private CommandContext<FarmItemDomain.FarmItemState> context = Mockito.mock(CommandContext.class);
    
    private class MockedContextFailure extends RuntimeException {};

    
    @Test
    public void createItemTest() {
        entity = new FarmItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `CreateItem` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.createItemWithReply(FarmItemApi.CreateItemMessage.newBuilder().build(), context);
        });
    }
    
    @Test
    public void buyItemTest() {
        entity = new FarmItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `BuyItem` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.buyItemWithReply(FarmItemApi.BuyItemMessage.newBuilder().build(), context);
        });
    }
    
    @Test
    public void rateItemTest() {
        entity = new FarmItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `RateItem` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.rateItemWithReply(FarmItemApi.RateItemMessage.newBuilder().build(), context);
        });
    }
    
    @Test
    public void getItemTest() {
        entity = new FarmItemImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `GetItem` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.getItemWithReply(FarmItemApi.GetItemMessage.newBuilder().build(), context);
        });
    }
}