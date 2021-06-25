package com.lightbend.farmtrust.farmitem.util.domain;

import com.akkaserverless.javasdk.EntityId;
import com.akkaserverless.javasdk.eventsourcedentity.*;
import com.google.protobuf.Empty;
import com.lightbend.farmtrust.farmitem.domain.FarmItemImpl;
import com.lightbend.farmtrust.farmitem.util.PublishUtilApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** An event sourced entity. */
@EventSourcedEntity(entityType = "publishutil")
public class PublishUtilImpl extends PublishUtilInterface {
    private static final Logger LOG = LoggerFactory.getLogger(PublishUtilImpl.class);

    @SuppressWarnings("unused")
    private final String entityId;
    
    public PublishUtilImpl(@EntityId String entityId) {
        this.entityId = entityId;
    }
    
    @Override
    public FarmUtilDomain.PublishUtilState snapshot() {
        // TODO: produce state snapshot here
        return FarmUtilDomain.PublishUtilState.newBuilder().build();
    }
    
    @Override
    public void handleSnapshot(FarmUtilDomain.PublishUtilState snapshot) {
        // TODO: restore state from snapshot here
        
    }
    
    @Override
    protected Empty publishRating(PublishUtilApi.PublishRatingMessage command, CommandContext ctx) {
        ctx.emit(
                FarmUtilDomain.PublishRatingAdded.newBuilder()
                        .setPublishUtilId(command.getPublishUtilId())
                        .setFarmLandId(command.getFarmLandId())
                        .setRating(command.getRating())
                        .build()
        );
        return Empty.getDefaultInstance();
    }
    
    @Override
    public void publishRatingAdded(FarmUtilDomain.PublishRatingAdded event) {
    }
}