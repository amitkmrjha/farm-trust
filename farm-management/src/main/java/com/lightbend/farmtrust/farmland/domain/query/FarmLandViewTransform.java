package com.lightbend.farmtrust.farmland.domain.query;
import com.akkaserverless.javasdk.view.UpdateHandler;
import com.akkaserverless.javasdk.view.View;
import com.lightbend.farmtrust.farmland.domain.FarmLandDomain;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@View
public class FarmLandViewTransform {
    private static final Logger LOG = LoggerFactory.getLogger(FarmLandViewTransform.class);

    @UpdateHandler
    public FarmLandDomain.FarmLandState processCropSeasonStarted(
            FarmLandDomain.CropSeasonStarted event, Optional<FarmLandDomain.FarmLandState> state) {

        if (state.isPresent()) {
            logEvent(event.getClass().getSimpleName(),state.get());
            return state.get();
        } else {
            logEvent(event.getClass().getSimpleName(),event.getFarmLandState());
            return event.getFarmLandState();
        }
    }

    @UpdateHandler
    public FarmLandDomain.FarmLandState processSoilPreparationStarted(
            FarmLandDomain.SoilPreparationStarted event, FarmLandDomain.FarmLandState state) {
        logEvent(event.getClass().getSimpleName(),event.getFarmLandState());
        return event.getFarmLandState();
    }

    @UpdateHandler
    public FarmLandDomain.FarmLandState processSeedingStarted(
            FarmLandDomain.SeedingStarted event, FarmLandDomain.FarmLandState state) {
        logEvent(event.getClass().getSimpleName(),event.getFarmLandState());
        return event.getFarmLandState();
    }

    @UpdateHandler
    public FarmLandDomain.FarmLandState processPlantingStarted(
            FarmLandDomain.PlantingStarted event, FarmLandDomain.FarmLandState state) {
        logEvent(event.getClass().getSimpleName(),event.getFarmLandState());
        return event.getFarmLandState();
    }


    @UpdateHandler
    public FarmLandDomain.FarmLandState processCaringStarted(
            FarmLandDomain.CaringStarted event, FarmLandDomain.FarmLandState state) {
        logEvent(event.getClass().getSimpleName(),event.getFarmLandState());
        return event.getFarmLandState();
    }

    @UpdateHandler
    public FarmLandDomain.FarmLandState processHealthCheckUpStarted(
            FarmLandDomain.HealthCheckUpStarted event, FarmLandDomain.FarmLandState state) {
        logEvent(event.getClass().getSimpleName(),event.getFarmLandState());
        return event.getFarmLandState();
    }


    @UpdateHandler
    public FarmLandDomain.FarmLandState processPestAndDiseaseControlStarted(
            FarmLandDomain.PestAndDiseaseControlStarted event, FarmLandDomain.FarmLandState state) {
        logEvent(event.getClass().getSimpleName(),event.getFarmLandState());
        return event.getFarmLandState();
    }

    @UpdateHandler
    public FarmLandDomain.FarmLandState processHarvestStarted(
            FarmLandDomain.HarvestStarted event, FarmLandDomain.FarmLandState state) {
        logEvent(event.getClass().getSimpleName(),event.getFarmLandState());
        return event.getFarmLandState();
    }

    @UpdateHandler
    public FarmLandDomain.FarmLandState processCropSeasonFinished(
            FarmLandDomain.CropSeasonFinished event, FarmLandDomain.FarmLandState state) {
        logEvent(event.getClass().getSimpleName(),event.getFarmLandState());
        return event.getFarmLandState();
    }

    @UpdateHandler
    public FarmLandDomain.FarmLandState processRatingAdded(
            FarmLandDomain.RatingAdded event, FarmLandDomain.FarmLandState state) {
        logEvent(event.getClass().getSimpleName(),event.getFarmLandState());
        return event.getFarmLandState();
    }

    private void logEvent(String eventName, FarmLandDomain.FarmLandState state ) {
        LOG.debug("processing event ["+ eventName + "] entity id  = ["+ state.getFarmLandId()+"].");
    }

}
