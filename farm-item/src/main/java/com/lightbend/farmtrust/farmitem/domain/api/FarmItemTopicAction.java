package com.lightbend.farmtrust.farmitem.domain.api;
import com.akkaserverless.javasdk.Reply;
import com.akkaserverless.javasdk.ServiceCallRef;
import com.akkaserverless.javasdk.action.Action;
import com.akkaserverless.javasdk.action.ActionContext;
import com.akkaserverless.javasdk.action.Handler;
import com.google.protobuf.Empty;
import com.google.protobuf.Any;
import com.lightbend.farmtrust.farmitem.FarmItemApi;
import com.lightbend.farmtrust.farmitem.domain.FarmItemDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Action
public class FarmItemTopicAction {
    private static final Logger LOG = LoggerFactory.getLogger(FarmItemTopicAction.class);


    private final String forwardTo = "com.lightbend.farmtrust.farmitem.FarmItemService";

    /** Akka Serverless expects some CloudEvent metadata to determine the target protobuf type. */
    @Handler
    public Reply<Empty> protobufFromTopic(
            FarmItemDomain.HarvestStarted message, ActionContext ctx) {

        LOG.debug("TopicAction: '{}' event  with id '{}' from topic.",message.getClass().getSimpleName(), message.getFarmLandState().getFarmLandId());

        FarmItemDomain.FarmLandState farmLandState = message.getFarmLandState();

        String farmLandId = farmLandState.getFarmLandId();
        int cycleNumber = farmLandState.getCycleNumber();
        String cropName = farmLandState.getCropName();
        String farmerName = farmLandState.getFarmerName();
        String itemId =    farmLandId + "-" + cycleNumber;
        String logFromFarm = "TBD";

            FarmItemApi.CreateItemMessage createMessage =
                    FarmItemApi.CreateItemMessage.newBuilder()
                            .setItemId(itemId)
                            .setCycleNumber(cycleNumber)
                            .setFarmLandId(farmLandId)
                            .setCropName(cropName)
                            .setFarmerName(farmerName)
                            .setLogFromFarm(logFromFarm)
                            .build();
            ServiceCallRef<FarmItemApi.CreateItemMessage> call =
                    ctx.serviceCallFactory().lookup(forwardTo, "CreateItem", FarmItemApi.CreateItemMessage.class);
            return Reply.forward(call.createCall(createMessage));

    }

    @Handler
    public Reply<Empty> protobufFromTopicTest( Any message, ActionContext ctx) {
        LOG.debug("TopicAction Test: '{}' event  with id '{}' from topic.",message.getClass().getSimpleName(), message.toString());
        return Reply.message(Empty.getDefaultInstance());
    }

}
