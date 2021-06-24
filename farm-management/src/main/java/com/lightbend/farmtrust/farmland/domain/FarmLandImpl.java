package com.lightbend.farmtrust.farmland.domain;

import com.akkaserverless.javasdk.EntityId;
import com.akkaserverless.javasdk.eventsourcedentity.*;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.lightbend.farmtrust.common.CommonMessage;
import com.lightbend.farmtrust.farmland.FarmLandApi;
import com.lightbend.farmtrust.farmland.domain.query.FarmLandViewTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/** An event sourced entity. */
@EventSourcedEntity(entityType = "farmland")
public class FarmLandImpl extends FarmLandInterface {

    private static final Logger LOG = LoggerFactory.getLogger(FarmLandImpl.class);

    enum FarmLandStatus {
        UNDER_FARMING,
        IDLE
    }
    @SuppressWarnings("unused")
    private final String entityId;

    private FarmLandDomain.FarmLandState farmLandState =
            FarmLandDomain.FarmLandState.newBuilder()
                    .setCycleNumber(0)
                    .setStatus(com.lightbend.farmtrust.farmland.domain.FarmLandImpl.FarmLandStatus.IDLE.name())
                    .setNumberOfRating(0)
                    .build();
    public FarmLandImpl(@EntityId String entityId) {
        this.entityId = entityId;
    }

    @Override
    public FarmLandDomain.FarmLandState snapshot() {
        return this.farmLandState;
    }

    @Override
    public void handleSnapshot(FarmLandDomain.FarmLandState snapshot) {
        this.farmLandState = snapshot;
    }

    @Override
    protected Empty startCropSeason(FarmLandApi.StartCropSeasonMessage command, CommandContext ctx) {
        if(!farmLandState.getStatus().equals(FarmLandStatus.IDLE.name())){
            throw ctx.fail(logStateInfo());
        }
        int newCycleNumber = this.farmLandState.getCycleNumber() +1;
        FarmLandStatus newStatus = FarmLandStatus.UNDER_FARMING;

        CommonMessage.FarmLandLog farmLandEventLog =
                CommonMessage.FarmLandLog.newBuilder()
                        .setTimestamp(getCurrentSystemTimeStamp())
                        .setFarmEventName("CropSeasonStarted")
                        .build();

        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder().mergeFrom(this.farmLandState)
                        .setFarmLandId(command.getFarmLandId())
                        .setCycleNumber(newCycleNumber)
                        .setFarmerName(command.getFarmerName())
                        .setCropName(command.getCropName())
                        .addFarmLandLog(farmLandEventLog)
                        .setStatus(newStatus.name())
                        .build();


        ctx.emit(
                FarmLandDomain.CropSeasonStarted.newBuilder()
                        .setFarmLandState(newState)
                        .build()
        );
        return Empty.getDefaultInstance();
    }

    @Override
    public void cropSeasonStarted(FarmLandDomain.CropSeasonStarted event) {
        this.farmLandState = event.getFarmLandState();
    }

    @Override
    protected Empty soilPreparation(FarmLandApi.SoilPreparationMessage command, CommandContext ctx) {
        if(!farmLandState.getStatus().equals(FarmLandImpl.FarmLandStatus.UNDER_FARMING.name())){
            throw ctx.fail(logStateInfo());
        }
        CommonMessage.FarmLandLog farmLandEventLog =
                CommonMessage.FarmLandLog.newBuilder()
                        .setTimestamp(getCurrentSystemTimeStamp())
                        .setFarmEventName("SoilPreparationStarted")
                        .setLogName("weather_info")
                        .setLogInfo(command.getWeatherInfo())
                        .build();

        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .addFarmLandLog(farmLandEventLog).build();

        ctx.emit(
                FarmLandDomain.SoilPreparationStarted.newBuilder()
                        .setFarmLandState(newState)
                        .build()
        );
        return Empty.getDefaultInstance();
    }

    @Override
    public void soilPreparationStarted(FarmLandDomain.SoilPreparationStarted event) {
        this.farmLandState = event.getFarmLandState();
    }


    @Override
    protected Empty seeding(FarmLandApi.SeedingMessage command, CommandContext ctx) {
        if(!farmLandState.getStatus().equals(FarmLandImpl.FarmLandStatus.UNDER_FARMING.name())){
            throw ctx.fail(logStateInfo());
        }

        CommonMessage.FarmLandLog farmLandEventLog =
                CommonMessage.FarmLandLog.newBuilder()
                        .setTimestamp(getCurrentSystemTimeStamp())
                        .setFarmEventName("SeedingStarted")
                        .setLogName("seed_info")
                        .setLogInfo(command.getSeedInfo())
                        .build();
        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .addFarmLandLog(farmLandEventLog).build();
        ctx.emit(
                FarmLandDomain.SeedingStarted.newBuilder()
                        .setFarmLandState(newState)
                        .build()
        );
        return Empty.getDefaultInstance();
    }


    @Override
    public void seedingStarted(FarmLandDomain.SeedingStarted event) {
        this.farmLandState = event.getFarmLandState();
    }

    @Override
    protected Empty planting(FarmLandApi.PlantingMessage command, CommandContext ctx) {
        if(!farmLandState.getStatus().equals(FarmLandImpl.FarmLandStatus.UNDER_FARMING.name())){
            throw ctx.fail(logStateInfo());
        }

        CommonMessage.FarmLandLog farmLandEventLog =
                CommonMessage.FarmLandLog.newBuilder()
                        .setTimestamp(getCurrentSystemTimeStamp())
                        .setFarmEventName("PlantingStarted")
                        .setLogName("plant_info")
                        .setLogInfo(command.getPlantInfo()).build();
        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .addFarmLandLog(farmLandEventLog).build();
        ctx.emit(
                FarmLandDomain.PlantingStarted.newBuilder()
                        .setFarmLandState(newState)
                        .build()
        );
        return Empty.getDefaultInstance();
    }

    @Override
    public void plantingStarted(FarmLandDomain.PlantingStarted event) {
        this.farmLandState = event.getFarmLandState();
    }

    @Override
    protected Empty caring(FarmLandApi.CaringMessage command, CommandContext ctx) {
        if(!farmLandState.getStatus().equals(FarmLandImpl.FarmLandStatus.UNDER_FARMING.name())){
            throw ctx.fail(logStateInfo());
        }

        HashMap<String, String> eventLogMap = new HashMap<String, String>();
        eventLogMap.put("fertilization_info",command.getFertilizationInfo());
        eventLogMap.put("watering_info",command.getWateringInfo());
        eventLogMap.put("weeding_info",command.getWeedingInfo());
        eventLogMap.put("manure_info",command.getManureInfo());
        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .addAllFarmLandLog(
                                eventLogMap.entrySet().stream().map(entryS ->
                                        CommonMessage.FarmLandLog.newBuilder()
                                                .setTimestamp(getCurrentSystemTimeStamp())
                                                .setFarmEventName("CaringStarted")
                                                .setLogName(entryS.getKey())
                                                .setLogInfo(entryS.getValue())
                                                .build()
                                ).collect(Collectors.toSet())
                        ).build();
        ctx.emit(
                FarmLandDomain.CaringStarted.newBuilder()
                        .setFarmLandState(newState)
                        .build());
        return Empty.getDefaultInstance();
    }

    @Override
    public void caringStarted(FarmLandDomain.CaringStarted event) {
        this.farmLandState = event.getFarmLandState();
    }

    @Override
    protected Empty healthCheckUp(FarmLandApi.HealthCheckUpMessage command, CommandContext ctx) {
        if(!farmLandState.getStatus().equals(FarmLandImpl.FarmLandStatus.UNDER_FARMING.name())){
            throw ctx.fail(logStateInfo());
        }

        CommonMessage.FarmLandLog farmLandEventLog =
                CommonMessage.FarmLandLog.newBuilder()
                        .setTimestamp(getCurrentSystemTimeStamp())
                        .setFarmEventName("HealthCheckUpStarted")
                        .setLogName("health_check_report")
                        .setLogInfo(command.getHealthCheckReport())
                        .build();
        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .addFarmLandLog(farmLandEventLog).build();

        ctx.emit(
                FarmLandDomain.HealthCheckUpStarted.newBuilder()
                        .setFarmLandState(newState)
                        .build());
        return Empty.getDefaultInstance();
    }

    @Override
    public void healthCheckUpStarted(FarmLandDomain.HealthCheckUpStarted event) {
        this.farmLandState = event.getFarmLandState();
    }

    @Override
    protected Empty pestAndDiseaseControl(FarmLandApi.PestAndDiseaseControlMessage command, CommandContext ctx) {
        if(!farmLandState.getStatus().equals(FarmLandImpl.FarmLandStatus.UNDER_FARMING.name())){
            throw ctx.fail(logStateInfo());
        }
        CommonMessage.FarmLandLog farmLandEventLog =
                CommonMessage.FarmLandLog.newBuilder()
                        .setTimestamp(getCurrentSystemTimeStamp())
                        .setFarmEventName("PestAndDiseaseControlStarted")
                        .setLogName("action_report")
                        .setLogInfo(command.getActionReport())
                        .build();
        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .addFarmLandLog(farmLandEventLog).build();

        ctx.emit(
                FarmLandDomain.PestAndDiseaseControlStarted.newBuilder()
                        .setFarmLandState(newState)
                        .build());
        return Empty.getDefaultInstance();
    }

    @Override
    public void pestAndDiseaseControlStarted(FarmLandDomain.PestAndDiseaseControlStarted event) {
        this.farmLandState = event.getFarmLandState();
    }

    @Override
    protected Empty harvest(FarmLandApi.HarvestMessage command, CommandContext ctx) {
        if(!farmLandState.getStatus().equals(FarmLandImpl.FarmLandStatus.UNDER_FARMING.name())){
            throw ctx.fail(logStateInfo());
        }

        CommonMessage.FarmLandLog farmLandEventLog =
                CommonMessage.FarmLandLog.newBuilder()
                        .setTimestamp(getCurrentSystemTimeStamp())
                        .setFarmEventName("HarvestStarted")
                        .build();

        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .setUnitItem(command.getUnitItem())
                        .setQuantity(command.getQuantity())
                        .addFarmLandLog(farmLandEventLog).build();

        ctx.emit(
                FarmLandDomain.HarvestStarted.newBuilder()
                        .setFarmLandState(newState)
                        .build());
        return Empty.getDefaultInstance();
    }

    @Override
    public void harvestStarted(FarmLandDomain.HarvestStarted event) {
        this.farmLandState = event.getFarmLandState();
    }

    @Override
    protected Empty finishCropSeason(FarmLandApi.FinishCropSeasonMessage command, CommandContext ctx) {
        if(!farmLandState.getStatus().equals(FarmLandImpl.FarmLandStatus.UNDER_FARMING.name())){
            throw ctx.fail(logStateInfo());
        }
        CommonMessage.FarmLandLog farmLandEventLog =
                CommonMessage.FarmLandLog.newBuilder()
                        .setTimestamp(getCurrentSystemTimeStamp())
                        .setFarmEventName("CropSeasonFinished")
                        .build();

        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(FarmLandDomain.FarmLandState.getDefaultInstance())
                        .setCycleNumber(this.farmLandState.getCycleNumber())
                        .setStatus(FarmLandImpl.FarmLandStatus.IDLE.name())
                        .setRating(this.farmLandState.getRating())
                        .setNumberOfRating(this.farmLandState.getNumberOfRating())
                        .build();

        ctx.emit(
                FarmLandDomain.CropSeasonFinished.newBuilder()
                        .setFarmLandState(newState)
                        .build());
        return Empty.getDefaultInstance();
    }

    @Override
    public void cropSeasonFinished(FarmLandDomain.CropSeasonFinished event) {
        this.farmLandState = event.getFarmLandState();
    }

    @Override
    protected Empty addLandRating(FarmLandApi.AddLandRatingMessage command, CommandContext ctx) {
        Double currentRating = this.farmLandState.getRating();
        int numberOfRating = this.farmLandState.getNumberOfRating();

        Double newRating = approxRollingAverage(currentRating,numberOfRating,command.getRating());
        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .setRating(newRating)
                        .setNumberOfRating(numberOfRating+1)
                        .build();
        ctx.emit(
                FarmLandDomain.RatingAdded.newBuilder()
                        .setFarmLandState(newState)
                        .build());
        return Empty.getDefaultInstance();
    }

    @Override
    public void ratingAdded(FarmLandDomain.RatingAdded event) {
        this.farmLandState = event.getFarmLandState();
    }

    @Override
    protected FarmLandApi.CurrentFarmLand getFarmLand(FarmLandApi.GetFarmLandMessage command, CommandContext ctx) {
        return convert(this.farmLandState);
    }

    private FarmLandApi.CurrentFarmLand convert(FarmLandDomain.FarmLandState farmState) {

        return FarmLandApi.CurrentFarmLand.newBuilder()
                .setFarmLandId(farmState.getFarmLandId())
                .setCycleNumber(farmState.getCycleNumber())
                .setFarmerName(farmState.getFarmerName())
                .setCropName(farmState.getCropName())
                .addAllFarmLandLog(farmState.getFarmLandLogList())
                .setUnitItem(farmState.getUnitItem())
                .setQuantity(farmState.getQuantity())
                .setFarmStatus(farmState.getStatus())
                .setRating(farmState.getRating())
                .build();

    }

    private Timestamp getCurrentSystemTimeStamp() {
        long millis = System.currentTimeMillis();
        return Timestamp.newBuilder().setSeconds(millis / 1000)
                .setNanos((int) ((millis % 1000) * 1000000)).build();
    }

    private String logStateInfo() {
        return "Status of farm land " + this.entityId+ " cycle ["+this.farmLandState.getCycleNumber()+"] is "+this.farmLandState.getStatus()+".";
    }

    private double approxRollingAverage (double average,int currentCount, double newRating) {
        LOG.debug("average '{}' currentCount '{}' newRating '{}' ",average,currentCount,newRating);
        Double newSum = (average*currentCount) + newRating;
        LOG.debug("newSum '{}' ",newSum);
       Double  newAverage  = newSum/(currentCount+1);
        LOG.debug("newAverage '{}' ",newAverage);
       return newAverage;
    }
}