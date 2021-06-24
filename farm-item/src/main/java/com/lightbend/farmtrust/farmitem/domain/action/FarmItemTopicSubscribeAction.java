package com.lightbend.farmtrust.farmitem.domain.action;

import com.akkaserverless.javasdk.Effect;
import com.akkaserverless.javasdk.Reply;
import com.akkaserverless.javasdk.ServiceCall;
import com.akkaserverless.javasdk.ServiceCallRef;
import com.akkaserverless.javasdk.action.Action;
import com.akkaserverless.javasdk.action.ActionContext;
import com.akkaserverless.javasdk.action.Handler;
import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lightbend.farmtrust.common.topic.FarmLandTopic;
import com.lightbend.farmtrust.farmitem.FarmItemApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Action
public class FarmItemTopicSubscribeAction {
    private static final Logger LOG = LoggerFactory.getLogger(FarmItemTopicSubscribeAction.class);
    private final String forwardTo = "com.lightbend.farmtrust.farmitem.FarmItemService";
    @Handler
    public Reply<Empty> subscribeFarmLandEvent(Any event, ActionContext ctx) {
        try {
            FarmLandTopic.FarmLandMessage message = event.unpack(FarmLandTopic.FarmLandMessage.class);
            LOG.debug("subscribeFarmLandEvent : '{}' event  with id '{}'.",message.getOperation(), message.getFarmLandId());
            List<FarmItemApi.CreateItemMessage> toCreate = getCreateItem(message);
            LOG.debug("Invoking CreateItem for '{}' item ",toCreate.size());
            ServiceCallRef<FarmItemApi.CreateItemMessage> call =
                    ctx.serviceCallFactory().lookup(forwardTo, "CreateItem", FarmItemApi.CreateItemMessage.class);
            Set<Effect> effects = toCreate.stream().map(e ->
                    Effect.of(call.createCall(e))).collect(Collectors.toSet());
            return Reply.message(Empty.getDefaultInstance()).addEffects(effects);

        } catch (InvalidProtocolBufferException e) {
            LOG.error("subscribeFarmLandEvent had exception : ",e);
        }
        return Reply.message(Empty.getDefaultInstance());
    }

    private List<FarmItemApi.CreateItemMessage> getCreateItem(FarmLandTopic.FarmLandMessage farmLandMessage) {

        String farmLandId = farmLandMessage.getFarmLandId();
        int cycleNumber = farmLandMessage.getCycleNumber();
        String cropName = farmLandMessage.getCropName();
        String farmerName = farmLandMessage.getFarmerName();
        String itemId =    farmLandId + "-" + cycleNumber;

        return IntStream.range(1, farmLandMessage.getQuantity()).boxed().collect(Collectors.toSet()).stream().map( i ->
             FarmItemApi.CreateItemMessage.newBuilder()
                .setItemId(itemId+"-"+i)
                .setCycleNumber(cycleNumber)
                .setFarmLandId(farmLandId)
                .setCropName(cropName)
                .setFarmerName(farmerName)
                .addAllFarmLandLog(farmLandMessage.getFarmLandLogList())
                 .setUnitItem(farmLandMessage.getUnitItem()+"-"+i)
                .build()
        ).collect(Collectors.toList());
    }

}
