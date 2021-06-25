package com.lightbend.farmtrust.farmitem.domain.action;

import com.akkaserverless.javasdk.action.Action;
import com.akkaserverless.javasdk.action.Handler;
import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.lightbend.farmtrust.common.topic.FarmItemTopic;
import com.lightbend.farmtrust.common.topic.FarmLandTopic;
import com.lightbend.farmtrust.farmitem.domain.FarmItemDomain;
import com.lightbend.farmtrust.farmitem.util.domain.FarmUtilDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Action
public class FarmItemTopicPublishAction {

    private static final Logger LOG = LoggerFactory.getLogger(FarmItemTopicPublishAction.class);
    public static final String defaultPublishUtilEntityId = "default-publishutil-entity-id-xxxxx";

    @Handler
    FarmItemTopic.LandRatingMessage publishLandRating(FarmUtilDomain.PublishRatingAdded event) {
        String farmLandId = event.getFarmLandId();
        Double rating = event.getRating();
        LOG.debug("Publishing rating for land : '{}' '{}' to topic.",farmLandId,rating);
        return FarmItemTopic.LandRatingMessage.newBuilder()
                .setFarmLandId(farmLandId)
                .setRating(rating)
                .build();
    }

    @Handler
    public Empty catchOthers(Any event) {
        LOG.debug("Publishing rating for land : catchOthers");
        return Empty.getDefaultInstance();
    }
}
