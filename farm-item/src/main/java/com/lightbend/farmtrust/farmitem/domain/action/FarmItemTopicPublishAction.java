package com.lightbend.farmtrust.farmitem.domain.action;

import com.akkaserverless.javasdk.action.Action;
import com.akkaserverless.javasdk.action.Handler;
import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import com.lightbend.farmtrust.common.topic.FarmItemTopic;
import com.lightbend.farmtrust.common.topic.FarmLandTopic;
import com.lightbend.farmtrust.farmitem.domain.FarmItemDomain;
import com.lightbend.farmtrust.farmitem.domain.FarmItemImpl;
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

    @Handler
    FarmItemTopic.LandRatingMessage publishLandRating(FarmItemDomain.FarmItemState state) {
        String farmLandId = state.getFarmLandId();
        String status = state.getItemStatus();
        Double rating = state.getUserRating();
        if(status.equals("SOLD") && rating != null){
            LOG.info("Publishing rating for land : '{}' '{}' to topic.",state.getFarmLandId(),rating);
            return FarmItemTopic.LandRatingMessage.newBuilder()
                    .setFarmLandId(farmLandId)
                    .setRating(rating)
                    .build();
        }
        LOG.info("Publishing rating for land : Empty");
        return FarmItemTopic.LandRatingMessage.getDefaultInstance();
    }

    @Handler
    public Empty catchOthers(Any event) {
        LOG.info("Publishing rating for land : catchOthers");
        return Empty.getDefaultInstance();
    }
}
