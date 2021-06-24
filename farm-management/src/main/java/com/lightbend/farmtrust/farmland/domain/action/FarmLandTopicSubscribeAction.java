package com.lightbend.farmtrust.farmland.domain.action;

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
import com.lightbend.farmtrust.common.topic.FarmItemTopic;
import com.lightbend.farmtrust.common.topic.FarmLandTopic;
import com.lightbend.farmtrust.farmland.FarmLandApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Action
public class FarmLandTopicSubscribeAction {
    private static final Logger LOG = LoggerFactory.getLogger(FarmLandTopicSubscribeAction.class);
    private final String forwardTo = "com.lightbend.farmtrust.farmland.FarmlandService";
    @Handler
    public Reply<Empty> subscribeFarmItemEvent(Any event, ActionContext ctx) {
        try {
            FarmItemTopic.LandRatingMessage message = event.unpack(FarmItemTopic.LandRatingMessage.class);
            if(!message.getFarmLandId().isEmpty() && message.getRating() >0){
                LOG.info("Subscribing rating for land : '{}' '{}' to topic.",message.getFarmLandId(),message.getRating());
                LOG.debug("Invoking AddLandRating for '{}' land ",message.getFarmLandId());
                FarmLandApi.AddLandRatingMessage addMsg =  FarmLandApi.AddLandRatingMessage.newBuilder()
                        .setFarmLandId(message.getFarmLandId())
                        .setRating(message.getRating())
                        .build();
                ServiceCallRef<FarmLandApi.AddLandRatingMessage> call =
                        ctx.serviceCallFactory().lookup(forwardTo, "AddLandRating", FarmLandApi.AddLandRatingMessage.class);
                return Reply.forward(call.createCall(addMsg));
            }else{
                LOG.debug("subscribeFarmItemEvent recieved empty messge");
            }
        } catch (InvalidProtocolBufferException e) {
            LOG.error("subscribeFarmItemEvent had exception : ",e);
        }
        return Reply.message(Empty.getDefaultInstance());
    }
}
