# farm-management


## Designing

While designing your service it is useful to read [designing services](https://developer.lightbend.com/docs/akka-serverless/designing/index.html)


## Developing

This project has a bare-bones skeleton service ready to go, but in order to adapt and
extend it it may be useful to read up on [developing services](https://developer.lightbend.com/docs/akka-serverless/developing/index.html)
and in particular the [Java section](https://developer.lightbend.com/docs/akka-serverless/java-services/index.html)


## Building

To build, at a minimum you need to generate and process sources, particularly when using an IDE.
A convenience is compile your project:

```
mvn compile
```


## Running Locally

In order to run your application locally, you must run the Akka Serverless proxy. The included `docker-compose` file contains the configuration required to run the proxy for a locally running application.
It also contains the configuration to start a local Google Pub/Sub emulator that the Akka Serverless proxy will connect to.
To start the proxy, run the following command from this directory:


```
docker-compose up
```


> On Linux this requires Docker 20.10 or later (https://github.com/moby/moby/pull/40007),
> or for a `USER_FUNCTION_HOST` environment variable to be set manually.

```
docker-compose -f docker-compose.yml -f docker-compose.linux.yml up
```

To start the application locally, the `exec-maven-plugin` is used. Use the following command:

```
mvn compile exec:java
```

With both the proxy and your application running, any defined endpoints should be available at `http://localhost:9000`. In addition to the defined gRPC interface, each method has a corresponding HTTP endpoint. Unless configured otherwise (see [Transcoding HTTP](https://docs.lbcs.dev/js-services/proto.html#_transcoding_http)), this endpoint accepts POST requests at the path `/[package].[entity name]/[method]`. For example, using `curl`:

```
> curl -XPOST -H "Content-Type: application/json" localhost:9000/com.lightbend.farmtrust.CounterService/GetCurrentCounter -d '{"counterId": "foo"}'
The command handler for `GetCurrentCounter` is not implemented, yet
```

For example, given [`grpcurl`](https://github.com/fullstorydev/grpcurl):

```shell
> curl -XPOST -H "Content-Type: application/json" -d '{"farm_land_id": "FarmLandinbangalore1234","crop_name": "mango","farmer_name": "Farmer123"}' localhost:9000/com.lightbend.farmtrust.farmland.FarmlandService/StartCropSeason

curl -XPOST -H "Content-Type: application/json" -d '{"farm_land_id": "FarmLandinbangalore1234","seed_info": "best organic seed were selected"}' localhost:9000/com.lightbend.farmtrust.farmland.FarmlandService/Seeding

curl -XPOST  -H "Content-Type: application/json" -d '{"farm_land_id": "FarmLandinbangalore1234"}' localhost:9000/com.lightbend.farmtrust.farmland.FarmlandService/FinishCropSeason


curl -XPOST  -H "Content-Type: application/json" -d '{"farm_land_id": "FarmLandinbangalore1234"}' localhost:9000/com.lightbend.farmtrust.farmland.FarmlandService/GetFarmLand

curl -XPOST -H "Content-Type: application/json" -d '{"farmer_name": "Farmer123"}' localhost:9000/com.lightbend.farmtrust.farmland.view.FarmLandByFarmerIdView/GetFarmLandByFarmerId


grpcurl \
  -d '{"farmer_name": "Farmer123"}' \
  -plaintext localhost:9000 \
  com.lightbend.farmtrust.farmland.view.FarmLandByFarmerIdView/GetFarmLandByFarmerId

  grpcurl \
  -d '{"crop_name": "mango"}' \
  -plaintext localhost:9000 \
  com.lightbend.farmtrust.farmland.view.FarmLandByCropNameView/GetFarmLandByCropName


curl -XPOST -H "Content-Type: application/json" -d '{"farm_land_id": "FarmLandinbangalore1236","crop_name": "mango","farmer_name": "Farmer123"}' localhost:9000/com.lightbend.farmtrust.FarmlandService/StartCropSeason

curl -XPOST -H "Content-Type: application/json" -d '{"farm_land_id": "FarmLandinbangalore1236","seed_info": "best organic seed were selected"}' localhost:9000/com.lightbend.farmtrust.FarmlandService/Seeding

curl -XPOST  -H "Content-Type: application/json" -d '{"farm_land_id": "FarmLandinbangalore1236"}' localhost:9000/com.lightbend.farmtrust.FarmlandService/FinishCropSeason


curl -XPOST  -H "Content-Type: application/json" -d '{"farm_land_id": "FarmLandinbangalore1236"}' localhost:9000/com.lightbend.farmtrust.FarmlandService/GetFarmLand


grpcurl -d '{"farm_land_id": "FarmLandinbangalore1234","crop_name": "mango","farmer_name": "Farmer123"}' -plaintext localhost:9000 com.lightbend.farmtrust.farmland.FarmlandService/StartCropSeason

grpcurl -d '{"farm_land_id": "FarmLandinbangalore1234","seed_info": "best organic seed were selected"}' -plaintext localhost:9000 com.lightbend.farmtrust.farmland.FarmlandService/Seeding

grpcurl -d '{"farm_land_id": "FarmLandinbangalore1234","unit_item": "box","quantity": 13}' -plaintext localhost:9000 com.lightbend.farmtrust.farmland.FarmlandService/Harvest

grpcurl -d '{"farm_land_id": "FarmLandinbangalore1234"}' -plaintext localhost:9000 com.lightbend.farmtrust.farmland.FarmlandService/FinishCropSeason


grpcurl -d '{"farm_land_id": "FarmLandinbangalore1234"}' -plaintext localhost:9000 com.lightbend.farmtrust.farmland.FarmlandService/GetFarmLand

grpcurl -d '{"farm_land_id": "FarmLandinbangalore1236","crop_name": "mango","farmer_name": "Farmer123"}' -plaintext localhost:9000 com.lightbend.farmtrust.farmland.FarmlandService/StartCropSeason

grpcurl -d '{"farm_land_id": "FarmLandinbangalore1236","seed_info": "best organic seed were selected"}' -plaintext localhost:9000 com.lightbend.farmtrust.farmland.FarmlandService/Seeding

grpcurl -d '{"farm_land_id": "FarmLandinbangalore1236"}' -plaintext localhost:9000 com.lightbend.farmtrust.farmland.FarmlandService/FinishCropSeason

grpcurl -d '{"farm_land_id": "FarmLandinbangalore1236"}' -plaintext localhost:9000 com.lightbend.farmtrust.farmland.FarmlandService/GetFarmLand

grpcurl -d '{"farmer_name": "Farmer123"}' -plaintext localhost:9000 com.lightbend.farmtrust.farmland.view.FarmLandByFarmerIdView/GetFarmLandByFarmerId

grpcurl -d '{"crop_name": "mango"}' -plaintext localhost:9000 com.lightbend.farmtrust.farmland.view.FarmLandByCropNameView/GetFarmLandByCropName


grpcurl \
-d '{"item_id": "mango1234","farm_land_id": "FarmLandinbangalore1234","crop_name": "mango","farmer_name": "Farmer123","logFromFarm": "Awesome organic farming"}' \
-plaintext localhost:9000 com.lightbend.farmtrust.farmitem.FarmItemService/CreateItem


grpcurl \
-d '{"item_id": "mango1234"}' \
-plaintext localhost:9000 com.lightbend.farmtrust.farmitem.FarmItemService/GetItem

grpcurl \
-d '{"item_id": "mango1234", "user_name": "amit123"}' \
-plaintext localhost:9000 com.lightbend.farmtrust.farmitem.FarmItemService/BuyItem

grpcurl \
-d '{"item_id": "mango1234", "rating": 3.5}' \
-plaintext localhost:9000 com.lightbend.farmtrust.farmitem.FarmItemService/RateItem



grpcurl \
-d '{"item_id": "FarmLandinbangalore1234-1-2"}' \
-plaintext localhost:9010 com.lightbend.farmtrust.farmitem.FarmItemService/GetItem


grpcurl \
-d '{"item_id": "FarmLandinbangalore1234-1-2","farm_land_id": "FarmLandinbangalore1234","crop_name": "mango","farmer_name": "Farmer123"}' \
-plaintext localhost:9010 com.lightbend.farmtrust.farmitem.FarmItemService/CreateItem

grpcurl \
-d '{"item_id": "FarmLandinbangalore1234-1-2", "user_name": "amit123"}' \
-plaintext localhost:9010 com.lightbend.farmtrust.farmitem.FarmItemService/BuyItem

grpcurl \
-d '{"item_id": "FarmLandinbangalore1234-1-2", "rating": 3.5}' \
-plaintext localhost:9010 com.lightbend.farmtrust.farmitem.FarmItemService/RateItem

grpcurl \
-d '{"farm_land_id": "FarmLandinbangalore1234", "itemStatus": "AVAILABLE"}' \
-plaintext localhost:9010 com.lightbend.farmtrust.farmitem.view.FarmItemByFarmLand/GetFarmItem

grpcurl \
-d '{"farmer_name": "Farmer123", "itemStatus": "AVAILABLE"}' \
-plaintext localhost:9010 com.lightbend.farmtrust.farmitem.view.FarmItemByFarmer/GetFarmItem

grpcurl \
-d '{"crop_name": "mango", "itemStatus": "AVAILABLE"}' \
-plaintext localhost:9010 com.lightbend.farmtrust.farmitem.view.FarmItemByCrop/GetFarmItem

grpcurl \
-d '{"boughtByUser": "amit123"}' \
-plaintext localhost:9010 com.lightbend.farmtrust.farmitem.view.FarmItemBySoldUser/GetFarmItem

```

> Note: The failure is to be expected if you have not yet provided an implementation of `GetCurrentCounter` in
> your entity.

```shell
mvn compile exec:java


gcloud projects list
gcloud config set project lbamitpubsub

gcloud iam service-accounts create akka-serverless-broker




gcloud iam service-accounts create lbamitpubsub-akkasls-broker



gcloud projects add-iam-policy-binding plenary-edition-288205 \
    --member "serviceAccount:lb-amitkumar-akkasls-broker@plenary-edition-288205.iam.gserviceaccount.com" \
    --role "roles/pubsub.editor"

lb-amitkumar-akkasls-broker@plenary-edition-288205.iam.gserviceaccount.com


akkasls project set broker --broker-service gcp-pubsub  --gcp-key-file lbamitpubsub-0273f7aba63a-Key.json


gcloud iam service-accounts keys create lb-amitkumar-akkasls-broker-keyfile.json \
    --iam-account lb-amitkumar-akkasls-broker@plenary-edition-288205.iam.gserviceaccount.com

gcloud beta emulators pubsub start --project=akkasls-pubsub-demo

mvn clean install -DskipTests
docker push amitjha12/farm-management:LATEST
docker push amitjha12/farm-item:LATEST

akkasls config set project farm-trust

akkasls services deploy farm-management amitjha12/farm-management:LATEST
akkasls services deploy farm-item amitjha12/farm-item:LATEST

akkasls svc expose farm-item --enable-cors
akkasls svc expose farm-management --enable-cors


git clone https://github.com/googleapis/python-pubsub.git
cd python-pubsub/samples/snippets
pip install -r requirements.txt
$(gcloud beta emulators pubsub env-init)
python3 publisher.py test create farm-land-events

python3 publisher.py test list 

python3 subscriber.py test create farm-land-events sub1
python3 subscriber.py test receive sub1
python3 publisher.py test publish farm-land-events


export PUBSUB_EMULATOR_HOST=localhost:8085
export PUBSUB_PROJECT_ID=test


curl http://localhost:8085

server.port=8090

-Dconfig.file=/home/demiourgos728/dev-mode-amit.conf


/home/demiourgos728/dev-mode-amit.conf


akkasls auth login --host api.akkaserverless.io:443
akkasls config set project farm-trust

PUBSUB_EMULATOR_HOST=localhost:8432


environment:
      PUBSUB_EMULATOR_HOST: http://${DOCKER_GATEWAY_HOST:-host.docker.internal}:8085
```


## Deploying

To deploy your service, install the `akkasls` CLI as documented in
[Setting up a local development environment](https://developer.lightbend.com/docs/akka-serverless/getting-started/set-up-development-env.html)
and configure a Docker Registry to upload your docker image to.

You will need to update the `akkasls.dockerImage` property in the `pom.xml` and refer to
[Configuring registries](https://developer.lightbend.com/docs/akka-serverless/deploying/registries.html)
for more information on how to make your docker image available to Akka Serverless.

Finally you can or use the [Akka Serverless Console](https://console.akkaserverless.com)
to create a project and then deploy your service into the project either by using `mvn deploy`,
through the `akkasls` CLI or via the web interface. When using `mvn deploy`, Maven will also
conveniently package and publish your docker image prior to deployment.
