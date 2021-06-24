package com.lightbend.farmtrust;

import com.akkaserverless.javasdk.AkkaServerless;
import com.lightbend.farmtrust.farmland.action.FarmLandEventPublishTopic;
import com.lightbend.farmtrust.farmland.action.FarmLandSubscribeTopic;
import com.lightbend.farmtrust.farmland.domain.FarmLandDomain;
import com.lightbend.farmtrust.farmland.domain.action.FarmLandEventTopicPublishAction;
import com.lightbend.farmtrust.farmland.domain.action.FarmLandTopicSubscribeAction;
import com.lightbend.farmtrust.farmland.domain.query.FarmLandViewTransform;
import com.lightbend.farmtrust.farmland.view.FarmLandViewByCropModel;
import com.lightbend.farmtrust.farmland.view.FarmLandViewByFarmerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.lightbend.farmtrust.MainComponentRegistrations.withGeneratedComponentsAdded;

public final class Main {
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static final AkkaServerless SERVICE =
            withGeneratedComponentsAdded(new AkkaServerless())
                    .registerView(
                            FarmLandViewTransform.class,
                            FarmLandViewByFarmerModel.getDescriptor().findServiceByName("FarmLandByFarmerIdView"),
                            "FarmLandByFarmerIdView",
                            FarmLandDomain.getDescriptor()
                    ).registerView(
                            FarmLandViewTransform.class,
                            FarmLandViewByCropModel.getDescriptor().findServiceByName("FarmLandByCropNameView"),
                            "FarmLandByCropNameView",
                            FarmLandDomain.getDescriptor()
                   ).registerAction(
                            FarmLandEventTopicPublishAction.class,
                            FarmLandEventPublishTopic.getDescriptor()
                            .findServiceByName("EventToTopicPublisherService")
                   ).registerAction(
                    FarmLandTopicSubscribeAction.class,
                    FarmLandSubscribeTopic.getDescriptor()
                                .findServiceByName("FarmLandSubscribeService"));

    
    public static void main(String[] args) throws Exception {
        LOG.info("starting the Akka Serverless service");
        SERVICE.start().toCompletableFuture().get();
    }
}