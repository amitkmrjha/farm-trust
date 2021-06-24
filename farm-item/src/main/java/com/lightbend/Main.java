package com.lightbend;

import com.akkaserverless.javasdk.AkkaServerless;
import com.lightbend.farmtrust.farmitem.action.FarmItemEventSubscribeTopic;
import com.lightbend.farmtrust.farmitem.action.FarmItemPublishTopic;
import com.lightbend.farmtrust.farmitem.domain.FarmItemDomain;
import com.lightbend.farmtrust.farmitem.domain.action.FarmItemTopicPublishAction;
import com.lightbend.farmtrust.farmitem.domain.action.FarmItemTopicSubscribeAction;
import com.lightbend.farmtrust.farmitem.view.FarmItemViewModel;
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
                    .registerAction(
                            FarmItemTopicPublishAction.class,
                            FarmItemPublishTopic.getDescriptor()
                                    .findServiceByName("FarmItemPublishService"))
                    .registerView(
                            FarmItemViewModel.getDescriptor().findServiceByName("FarmItemByFarmLand"),
                            "FarmItemByFarmLand",
                            FarmItemDomain.getDescriptor())
                    .registerView(
                            FarmItemViewModel.getDescriptor().findServiceByName("FarmItemByFarmer"),
                            "FarmItemByFarmer",
                            FarmItemDomain.getDescriptor())
                    .registerView(
                            FarmItemViewModel.getDescriptor().findServiceByName("FarmItemByCrop"),
                            "FarmItemByCrop",
                            FarmItemDomain.getDescriptor())
                    .registerView(
                            FarmItemViewModel.getDescriptor().findServiceByName("FarmItemBySoldUser"),
                            "FarmItemBySoldUser",
                            FarmItemDomain.getDescriptor());


    public static void main(String[] args) throws Exception {
        LOG.info("starting the Akka Serverless service");
        SERVICE.start().toCompletableFuture().get();
    }
}