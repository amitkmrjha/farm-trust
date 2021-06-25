package com.lightbend.farmtrust.farmitem.domain;

import com.akkaserverless.javasdk.Effect;
import com.akkaserverless.javasdk.EntityId;
import com.akkaserverless.javasdk.Reply;
import com.akkaserverless.javasdk.ServiceCallRef;
import com.akkaserverless.javasdk.valueentity.*;
import com.google.protobuf.Empty;
import com.lightbend.farmtrust.farmitem.FarmItemApi;
import com.lightbend.farmtrust.farmitem.domain.action.FarmItemTopicPublishAction;
import com.lightbend.farmtrust.farmitem.util.PublishUtilApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

//** A value entity. */
@ValueEntity(entityType = "farmitem")
public class FarmItemImpl extends FarmItemInterface {

    private static final Logger LOG = LoggerFactory.getLogger(FarmItemImpl.class);

    enum FarmItemStatus {
        AVAILABLE,
        SOLD
    }

    @SuppressWarnings("unused")
    private final String entityId;

    public FarmItemImpl(@EntityId String entityId) {
        this.entityId = entityId;
    }

    @Override
    protected Empty createItem(FarmItemApi.CreateItemMessage command, CommandContext<FarmItemDomain.FarmItemState> ctx) {
        Optional<FarmItemDomain.FarmItemState> stateOption = ctx.getState();
        if(stateOption.isPresent()){
            throw ctx.fail(logStateInfo(stateOption.get()));
        }

        FarmItemDomain.FarmItemState newState =
                FarmItemDomain.FarmItemState.newBuilder()
                        .setItemId(this.entityId)
                        .setFarmLandId(command.getFarmLandId())
                        .setCycleNumber(command.getCycleNumber())
                        .setFarmerName(command.getFarmerName())
                        .setCropName(command.getCropName())
                        .addAllFarmLandLog(command.getFarmLandLogList())
                        .setItemStatus(FarmItemStatus.AVAILABLE.name())
                        .setUnitItem(command.getUnitItem())
                        .build();
        ctx.updateState(newState);
        return Empty.getDefaultInstance();
    }

    @Override
    protected FarmItemApi.ItemInfo buyItem(FarmItemApi.BuyItemMessage command, CommandContext<FarmItemDomain.FarmItemState> ctx) {
        Optional<FarmItemDomain.FarmItemState> stateOption = ctx.getState();
        if(!stateOption.isPresent()){
            throw ctx.fail(logStateInfo(FarmItemDomain.FarmItemState.newBuilder().build()));
        }

        FarmItemDomain.FarmItemState state  = stateOption.get();
        if(state.getItemStatus().equals(FarmItemStatus.SOLD.name())){
            throw ctx.fail(logStateInfo(state));
        }
        FarmItemDomain.FarmItemState newState =
                state.toBuilder()
                        .setItemStatus(FarmItemStatus.SOLD.name())
                        .setBoughtByUser(command.getUserName())
                        .build();
        ctx.updateState(newState);
        return convertToApi(newState);
    }

    @Override
    public Reply<Empty> rateItemWithReply(FarmItemApi.RateItemMessage command, CommandContext<FarmItemDomain.FarmItemState> ctx) {
        final String forwardTo = "com.lightbend.farmtrust.farmitem.util.PublishUtilService";
        ServiceCallRef<PublishUtilApi.PublishRatingMessage> publishRatingCallRef =
                ctx.serviceCallFactory().lookup(forwardTo, "PublishRating", PublishUtilApi.PublishRatingMessage.class);

        Optional<FarmItemDomain.FarmItemState> stateOption = ctx.getState();
        if(!stateOption.isPresent()){
            throw ctx.fail(logStateInfo(FarmItemDomain.FarmItemState.newBuilder().build()));
        }
        FarmItemDomain.FarmItemState state  = stateOption.get();
        if(!state.getItemStatus().equals(FarmItemStatus.SOLD.name())){
            throw ctx.fail(logStateInfo(state));
        }

        FarmItemDomain.FarmItemState newState =
                state.toBuilder()
                        .setUserRating(command.getRating())
                        .build();

        PublishUtilApi.PublishRatingMessage pubMsg =
                PublishUtilApi.PublishRatingMessage.newBuilder()
                        .setPublishUtilId(FarmItemTopicPublishAction.defaultPublishUtilEntityId)
                        .setFarmLandId(state.getFarmLandId())
                        .setRating(command.getRating())
                        .build();

        ctx.updateState(newState);
        return Reply.message(Empty.getDefaultInstance()).addEffects(Effect.of(publishRatingCallRef.createCall(pubMsg)));
    }



    @Override
    protected FarmItemApi.ItemInfo getItem(FarmItemApi.GetItemMessage command, CommandContext<FarmItemDomain.FarmItemState> ctx) {
        FarmItemDomain.FarmItemState current = ctx.getState()
                .orElseGet(() -> FarmItemDomain.FarmItemState.newBuilder().build());
        return  convertToApi(current);
    }

    private FarmItemApi.ItemInfo convertToApi(FarmItemDomain.FarmItemState state ) {

        return FarmItemApi.ItemInfo.newBuilder()
                .setItemId(state.getItemId())
                .setFarmLandId(state.getFarmLandId())
                .setCycleNumber(state.getCycleNumber())
                .setFarmerName(state.getFarmerName())
                .setCropName(state.getCropName())
                .addAllFarmLandLog(state.getFarmLandLogList())
                .setItemStatus(state.getItemStatus())
                .setBoughtByUser(state.getBoughtByUser())
                .setUserRating(state.getUserRating())
                .setUnitItem(state.getUnitItem())
                .build();
    }

    private String logStateInfo(FarmItemDomain.FarmItemState state) {
        return "Status of farm item " + this.entityId+ "  ["+state.getItemStatus()+"].";
    }

}