#!/bin/bash

. build-support/build-common.sh

usage() {
  echo "Usage: $0 DEPLOY_CLUSTER"
}

check_volume() {
  if docker volume &> /dev/null; then
    if docker volume ls |awk "\$2 == \"$1\"{++m} END {exit (m>0?0:1)}"; then
      return
    else
      echo "Missing required volume $1"
      exit 1
    fi
  else
    echo "WARN: 'docker volume' not supported so we'll have to trust $1 exists"
  fi
}

if [[ $# < 1 ]]; then
  usage
  exit 1
fi
export DEPLOY_CLUSTER=$1

if [[ -z $CIRCLE_TAG ]]; then
  if [[ $CIRCLE_BRANCH == "master" ]]; then
    export MCCY_TAG=latest
  else
    export MCCY_TAG=$CIRCLE_BRANCH
  fi
else
  export MCCY_TAG=$CIRCLE_TAG
fi

export BRANCH=${CIRCLE_BRANCH:-tag}

check_var CARINA_USERNAME
check_var CARINA_APIKEY
check_var MCCY_PASSWORD
check_var LETSENCRYPT_EMAIL
check_var LETSENCRYPT_DOMAIN
check_var MCCY_TAG
resolve_vars

set -e

#### SETUP credentials

mkdir -p $HOME/carina
docker pull itzg/carina-cli
docker run --name $$ -e CARINA_USERNAME -e CARINA_APIKEY itzg/carina-cli credentials $DEPLOY_CLUSTER
docker cp $$:/carina/clusters/$CARINA_USERNAME/$DEPLOY_CLUSTER/. $HOME/carina
if [[ $CIRCLECI != true ]]; then
  docker rm $$ > /dev/null
fi
. $HOME/carina/docker.env
docker info

export DOCKER_HOST_URI=${DOCKER_HOST/tcp:/https:}

#### DEPLOY

COMPOSE_FILE=docker-compose-carina.yml

export COMPOSE_PROJECT_NAME="${BRANCH}"
docker-compose -f $COMPOSE_FILE pull
docker-compose --verbose -f $COMPOSE_FILE up -d

echo "
READY for use on the cluster $DEPLOY_CLUSTER
"

if [[ -v CIRCLE_ARTIFACTS ]]; then
  cat <<EOF > $CIRCLE_ARTIFACTS/results.html
<html><body>
Ready for use on the cluster $DEPLOY_CLUSTER
</body></html>
EOF

fi
