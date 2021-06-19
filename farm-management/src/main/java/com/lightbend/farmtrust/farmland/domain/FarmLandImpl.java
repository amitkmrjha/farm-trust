package com.lightbend.farmtrust.farmland.domain;

import com.akkaserverless.javasdk.EntityId;
import com.akkaserverless.javasdk.eventsourcedentity.*;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.lightbend.farmtrust.farmland.FarmLandApi;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

/** An event sourced entity. */
@EventSourcedEntity(entityType = "farmland")
public class FarmLandImpl extends FarmLandInterface {

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

        FarmLandDomain.FarmLandEventLog farmLandEventLog =
                FarmLandDomain.FarmLandEventLog.newBuilder()
                        .setFarmEventName("CropSeasonStarted")
                        .setLog(
                                FarmLandDomain.FarmLandLog.newBuilder()
                                        .setTimestamp(getCurrentSystemTimeStamp())
                                        .build()
                        ).build();

        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder().mergeFrom(this.farmLandState)
                        .setFarmLandId(command.getFarmLandId())
                        .setCycleNumber(newCycleNumber)
                        .setFarmerName(command.getFarmerName())
                        .setCropName(command.getCropName())
                        .addFarmLandEventLog(farmLandEventLog)
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
        FarmLandDomain.FarmLandEventLog farmLandEventLog =
                FarmLandDomain.FarmLandEventLog.newBuilder()
                        .setFarmEventName("SoilPreparationStarted")
                        .setLog(
                                FarmLandDomain.FarmLandLog.newBuilder()
                                        .setLogName("weather_info")
                                        .setLogInfo(command.getWeatherInfo())
                                        .setTimestamp(getCurrentSystemTimeStamp())
                                        .build()
                        ).build();

        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .addFarmLandEventLog(farmLandEventLog).build();

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

        FarmLandDomain.FarmLandEventLog farmLandEventLog =
                FarmLandDomain.FarmLandEventLog.newBuilder()
                        .setFarmEventName("SeedingStarted")
                        .setLog(
                                FarmLandDomain.FarmLandLog.newBuilder()
                                        .setLogName("seed_info")
                                        .setLogInfo(command.getSeedInfo())
                                        .setTimestamp(getCurrentSystemTimeStamp())
                                        .build()
                        ).build();
        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .addFarmLandEventLog(farmLandEventLog).build();
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

        FarmLandDomain.FarmLandEventLog farmLandEventLog =
                FarmLandDomain.FarmLandEventLog.newBuilder()
                        .setFarmEventName("PlantingStarted")
                        .setLog(
                                FarmLandDomain.FarmLandLog.newBuilder()
                                        .setLogName("plant_info")
                                        .setLogInfo(command.getPlantInfo())
                                        .setTimestamp(getCurrentSystemTimeStamp())
                                        .build()
                        ).build();
        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .addFarmLandEventLog(farmLandEventLog).build();
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
                        .addAllFarmLandEventLog(
                                eventLogMap.entrySet().stream().map(entryS ->
                                        FarmLandDomain.FarmLandEventLog.newBuilder()
                                                .setFarmEventName("CaringStarted")
                                                .setLog(
                                                        FarmLandDomain.FarmLandLog.newBuilder()
                                                                .setLogName(entryS.getKey())
                                                                .setLogInfo(entryS.getValue())
                                                                .setTimestamp(getCurrentSystemTimeStamp())
                                                                .build()
                                                ).build()
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

        FarmLandDomain.FarmLandEventLog farmLandEventLog =
                FarmLandDomain.FarmLandEventLog.newBuilder()
                        .setFarmEventName("HealthCheckUpStarted")
                        .setLog(
                                FarmLandDomain.FarmLandLog.newBuilder()
                                        .setLogName("health_check_report")
                                        .setLogInfo(command.getHealthCheckReport())
                                        .setTimestamp(getCurrentSystemTimeStamp())
                                        .build()
                        ).build();
        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .addFarmLandEventLog(farmLandEventLog).build();

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
        FarmLandDomain.FarmLandEventLog farmLandEventLog =
                FarmLandDomain.FarmLandEventLog.newBuilder()
                        .setFarmEventName("PestAndDiseaseControlStarted")
                        .setLog(
                                FarmLandDomain.FarmLandLog.newBuilder()
                                        .setLogName("action_report")
                                        .setLogInfo(command.getActionReport())
                                        .setTimestamp(getCurrentSystemTimeStamp())
                                        .build()
                        ).build();
        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .addFarmLandEventLog(farmLandEventLog).build();

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

        FarmLandDomain.FarmLandEventLog farmLandEventLog =
                FarmLandDomain.FarmLandEventLog.newBuilder()
                        .setFarmEventName("HarvestStarted")
                        .setLog(
                                FarmLandDomain.FarmLandLog.newBuilder()
                                        .setTimestamp(getCurrentSystemTimeStamp())
                                        .build()
                        ).build();

        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(this.farmLandState)
                        .setUnitItem(command.getUnitItem())
                        .setQuantity(command.getQuantity())
                        .addFarmLandEventLog(farmLandEventLog).build();

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
        FarmLandDomain.FarmLandEventLog farmLandEventLog =
                FarmLandDomain.FarmLandEventLog.newBuilder()
                        .setFarmEventName("CropSeasonFinished")
                        .setLog(
                                FarmLandDomain.FarmLandLog.newBuilder()
                                        .setTimestamp(getCurrentSystemTimeStamp())
                                        .build()
                        ).build();

        FarmLandDomain.FarmLandState newState =
                FarmLandDomain.FarmLandState.newBuilder()
                        .mergeFrom(FarmLandDomain.FarmLandState.getDefaultInstance())
                        .setCycleNumber(this.farmLandState.getCycleNumber())
                        .setStatus(FarmLandImpl.FarmLandStatus.IDLE.name())
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
    protected FarmLandApi.CurrentFarmLand getFarmLand(FarmLandApi.GetFarmLandMessage command, CommandContext ctx) {
        return convert(this.farmLandState);
    }

    private FarmLandApi.CurrentFarmLand convert(FarmLandDomain.FarmLandState farmState) {
        Set<FarmLandApi.FarmingProcessLog> farmingProcessLog =
                farmState.getFarmLandEventLogList().stream().map(farmLandEventLog ->
                        FarmLandApi.FarmingProcessLog.newBuilder()
                                .setFarmEventName(farmLandEventLog.getFarmEventName())
                                .setLog(
                                        FarmLandApi.ProcessLog.newBuilder()
                                                .setLogName(farmLandEventLog.getLog().getLogName())
                                                .setLogInfo(farmLandEventLog.getLog().getLogInfo())
                                                .setTimestamp(farmLandEventLog.getLog().getTimestamp())
                                                .build()
                                )
                                .build()
                ).collect(Collectors.toSet());

        return FarmLandApi.CurrentFarmLand.newBuilder()
                .setFarmLandId(farmState.getFarmLandId())
                .setCycleNumber(farmState.getCycleNumber())
                .setFarmerName(farmState.getFarmerName())
                .setCropName(farmState.getCropName())
                .addAllFarmingProcessLog(farmingProcessLog)
                .setUnitItem(farmState.getUnitItem())
                .setQuantity(farmState.getQuantity())
                .setFarmStatus(farmState.getStatus())
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
}