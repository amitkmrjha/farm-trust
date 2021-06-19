package com.lightbend.farmtrust.farmitem.domain.action;

import com.akkaserverless.javasdk.action.Action;
import com.akkaserverless.javasdk.action.Handler;
import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import com.lightbend.farmtrust.farmitem.domain.FarmItemDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Action
public class FarmItemTopicSubscribeAction {
    private static final Logger LOG = LoggerFactory.getLogger(FarmItemTopicSubscribeAction.class);

    @Handler
    public Empty subscribeHarvestStarted(FarmItemDomain.HarvestStarted event) {
        LOG.info("Subscribed: '{}' event  with id '{}' from topic.",event.getClass().getSimpleName(), event.getFarmLandState().getFarmLandId());
        return Empty.getDefaultInstance();
    }

    @Handler
    public Empty catchOthers(Any event) {
        LOG.info("Subscribed: '{}' event  with id '{}' from topic.",event.getClass().getSimpleName(), "");
        return Empty.getDefaultInstance();
    }

}
