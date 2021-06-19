package com.lightbend.farmtrust.farmitem.domain;

import com.akkaserverless.javasdk.EntityId;
import com.akkaserverless.javasdk.valueentity.*;
import com.google.protobuf.Empty;
import com.lightbend.farmtrust.farmitem.FarmItemApi;
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
                        .setCropName(command.getCropName())
                        .setLogFromFarm(command.getLogFromFarm())
                        .setItemStatus(FarmItemStatus.AVAILABLE.name())
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
    protected Empty rateItem(FarmItemApi.RateItemMessage command, CommandContext<FarmItemDomain.FarmItemState> ctx) {
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
        ctx.updateState(newState);
        return Empty.getDefaultInstance();
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
                .setLogFromFarm(state.getLogFromFarm())
                .setItemStatus(state.getItemStatus())
                .setBoughtByUser(state.getBoughtByUser())
                .setUserRating(state.getUserRating())
                .build();
    }

    private String logStateInfo(FarmItemDomain.FarmItemState state) {
        return "Status of farm item " + this.entityId+ "  ["+state.getItemStatus()+"].";
    }

}