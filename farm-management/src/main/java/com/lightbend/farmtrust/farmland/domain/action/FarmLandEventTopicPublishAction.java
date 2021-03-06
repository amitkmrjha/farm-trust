package com.lightbend.farmtrust.farmland.domain.action;

import com.akkaserverless.javasdk.action.Action;
import com.akkaserverless.javasdk.action.Handler;
import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.lightbend.farmtrust.common.topic.FarmLandTopic;
import com.lightbend.farmtrust.farmland.action.FarmLandEventPublishTopic;
import com.lightbend.farmtrust.farmland.domain.FarmLandDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Action
public class FarmLandEventTopicPublishAction {
    private static final Logger LOG = LoggerFactory.getLogger(FarmLandEventTopicPublishAction.class);

    @Handler
    FarmLandTopic.FarmLandMessage publishHarvestStarted(FarmLandDomain.HarvestStarted event) {
        LOG.info("Publishing: '{}' event  with id '{}'  cycle '{}' to topic.",
                event.getClass().getSimpleName(),
                event.getFarmLandState().getFarmLandId(),
                event.getFarmLandState().getCycleNumber()
        );
        return convert(event.getClass().getSimpleName(),event.getFarmLandState());
    }

    @Handler
    public Empty catchOthers(Any event) {
        LOG.info("Publishing: '{}' event  with farm_id '{}' to topic.",event.getClass().getSimpleName(), "");
        return Empty.getDefaultInstance();
    }

    private FarmLandTopic.FarmLandMessage convert(String eventType ,FarmLandDomain.FarmLandState state ) {
        return  FarmLandTopic.FarmLandMessage.newBuilder()
                .setOperation(eventType)
                .setFarmLandId(state.getFarmLandId())
                .setCycleNumber(state.getCycleNumber())
                .setFarmerName(state.getFarmerName())
                .setCropName(state.getCropName())
                .setUnitItem(state.getUnitItem())
                .setQuantity(state.getQuantity())
                .setStatus(state.getStatus())
                .addAllFarmLandLog(state.getFarmLandLogList())
                .build();
    }

}
