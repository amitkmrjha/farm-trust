package com.lightbend.farmtrust.farmitem.domain.action;

import com.akkaserverless.javasdk.Reply;
import com.akkaserverless.javasdk.ServiceCallRef;
import com.akkaserverless.javasdk.action.Action;
import com.akkaserverless.javasdk.action.ActionContext;
import com.akkaserverless.javasdk.action.Handler;
import com.akkaserverless.javasdk.reply.ForwardReply;
import com.google.protobuf.Any;
import com.google.protobuf.Empty;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lightbend.farmtrust.TopicMessage;
import com.lightbend.farmtrust.farmitem.FarmItemApi;
import com.lightbend.farmtrust.farmitem.domain.FarmItemDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Action
public class FarmItemTopicSubscribeAction {
    private static final Logger LOG = LoggerFactory.getLogger(FarmItemTopicSubscribeAction.class);
    private final String forwardTo = "com.lightbend.farmtrust.farmitem.FarmItemService";
    @Handler
    public Reply<Empty> subscribeFarmLandEvent(Any event, ActionContext ctx) {
        try {
            TopicMessage.Message message = event.unpack(TopicMessage.Message.class);
            LOG.debug("Subscribed catchOthers: '{}' event  with id '{}' from topic.",message.getOperation(), message.getFarmLandId());
            String farmLandId = message.getFarmLandId();
            int cycleNumber = message.getCycleNumber();
            String cropName = message.getCropName();
            String farmerName = message.getFarmerName();
            String itemId =    farmLandId + "-" + cycleNumber;
            List<String> logFromFarm = message.getFarmLogList();
            String.join("\n", logFromFarm);

            FarmItemApi.CreateItemMessage createMessage =
                    FarmItemApi.CreateItemMessage.newBuilder()
                            .setItemId(itemId)
                            .setCycleNumber(cycleNumber)
                            .setFarmLandId(farmLandId)
                            .setCropName(cropName)
                            .setFarmerName(farmerName)
                            .setLogFromFarm(String.join("\n", logFromFarm))
                            .build();
            ServiceCallRef<FarmItemApi.CreateItemMessage> call =
                    ctx.serviceCallFactory().lookup(forwardTo, "CreateItem", FarmItemApi.CreateItemMessage.class);
            return Reply.forward(call.createCall(createMessage));
        } catch (InvalidProtocolBufferException e) {
            LOG.error("catchOthers had exception : ",e);
        }
        return Reply.message(Empty.getDefaultInstance());
    }

}
