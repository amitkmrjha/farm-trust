package com.lightbend;

import com.akkaserverless.javasdk.AkkaServerless;
import com.lightbend.farmtrust.farmitem.action.FarmItemEventSubscribeTopic;
import com.lightbend.farmtrust.farmitem.api.FarmItemTopic;
import com.lightbend.farmtrust.farmitem.domain.action.FarmItemTopicSubscribeAction;
import com.lightbend.farmtrust.farmitem.domain.api.FarmItemTopicAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.lightbend.MainComponentRegistrations.withGeneratedComponentsAdded;

public final class Main {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static final AkkaServerless SERVICE =
            withGeneratedComponentsAdded(new AkkaServerless())
                    .registerAction(
                            FarmItemTopicSubscribeAction.class,
                            FarmItemEventSubscribeTopic.getDescriptor()
                                    .findServiceByName("FarmItemEventSubscriberService"))
                    /*.registerAction(
                    FarmItemTopicAction.class,
                    FarmItemTopic.getDescriptor().findServiceByName("FarmItemTopicService"))*/;

    public static void main(String[] args) throws Exception {
        LOG.info("starting the Akka Serverless service");
        SERVICE.start().toCompletableFuture().get();
    }
}