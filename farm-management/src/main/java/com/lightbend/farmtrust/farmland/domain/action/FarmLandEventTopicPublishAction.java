package com.lightbend.farmtrust.farmland.domain.action;

import com.akkaserverless.javasdk.action.Action;
import com.akkaserverless.javasdk.action.Handler;
import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import com.lightbend.farmtrust.farmland.domain.FarmLandDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Action
public class FarmLandEventTopicPublishAction {
    private static final Logger LOG = LoggerFactory.getLogger(FarmLandEventTopicPublishAction.class);

    @Handler
    FarmLandDomain.CropSeasonFinished publishCropSeasonFinished(FarmLandDomain.CropSeasonFinished event) {
        LOG.info("Publishing: '{}' event  with id '{}' to topic.",event.getClass().getSimpleName(), event.getFarmLandState().getFarmLandId());
        return event;
    }

    @Handler
    public Empty catchOthers(Any event) {
        LOG.info("Publishing: '{}' event  with id '{}' to topic.",event.getClass().getSimpleName(), "");
        return Empty.getDefaultInstance();
    }

}
