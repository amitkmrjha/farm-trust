package com.lightbend.farmtrust.farmland.domain;

import com.akkaserverless.javasdk.eventsourcedentity.CommandContext;
import com.google.protobuf.Empty;
import com.lightbend.farmtrust.farmland.FarmLandApi;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.assertThrows;

public class FarmLandTest {
    private String entityId = "entityId1";
    private FarmLandImpl entity;
    private CommandContext context = Mockito.mock(CommandContext.class);
    
    private class MockedContextFailure extends RuntimeException {};
    
    @Test
    public void startCropSeasonTest() {
        entity = new FarmLandImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `StartCropSeason` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.startCropSeasonWithReply(FarmLandApi.StartCropSeasonMessage.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void soilPreparationTest() {
        entity = new FarmLandImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `SoilPreparation` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.soilPreparationWithReply(FarmLandApi.SoilPreparationMessage.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void seedingTest() {
        entity = new FarmLandImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `Seeding` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.seedingWithReply(FarmLandApi.SeedingMessage.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void plantingTest() {
        entity = new FarmLandImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `Planting` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.plantingWithReply(FarmLandApi.PlantingMessage.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void caringTest() {
        entity = new FarmLandImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `Caring` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.caringWithReply(FarmLandApi.CaringMessage.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void healthCheckUpTest() {
        entity = new FarmLandImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `HealthCheckUp` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.healthCheckUpWithReply(FarmLandApi.HealthCheckUpMessage.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void pestAndDiseaseControlTest() {
        entity = new FarmLandImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `PestAndDiseaseControl` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.pestAndDiseaseControlWithReply(FarmLandApi.PestAndDiseaseControlMessage.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void harvestTest() {
        entity = new FarmLandImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `Harvest` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.harvestWithReply(FarmLandApi.HarvestMessage.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void finishCropSeasonTest() {
        entity = new FarmLandImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `FinishCropSeason` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.finishCropSeasonWithReply(FarmLandApi.FinishCropSeasonMessage.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void addLandRatingTest() {
        entity = new FarmLandImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `AddLandRating` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.addLandRatingWithReply(FarmLandApi.AddLandRatingMessage.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
    
    @Test
    public void getFarmLandTest() {
        entity = new FarmLandImpl(entityId);
        
        Mockito.when(context.fail("The command handler for `GetFarmLand` is not implemented, yet"))
            .thenReturn(new MockedContextFailure());
        
        // TODO: set fields in command, and update assertions to match implementation
        assertThrows(MockedContextFailure.class, () -> {
            entity.getFarmLandWithReply(FarmLandApi.GetFarmLandMessage.newBuilder().build(), context);
        });
        
        // TODO: if you wish to verify events:
        //    Mockito.verify(context).emit(event);
    }
}