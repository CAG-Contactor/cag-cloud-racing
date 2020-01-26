#!/bin/bash

# exit if any command fails
set -e

usage() {
  echo "Usage: $0 [-p <profile name>] <stack name suffix>"
  echo "  -p: specify AWS profile"
   1>&2; exit 1;
  }

deploy=0

while getopts "dp:" o; do
    case "${o}" in
        p)
            aws_profile="--profile ${OPTARG}"
            ;;
        *)
            usage
            ;;
    esac
done
shift $((OPTIND-1))
suffix=$1

if [ -z "$suffix" ]; then
    usage
fi

stack_name="cag-cloud-racing-stack-$suffix"
echo "Deploying stack: $stack_name"
echo "Profile: $aws_profile"

# only deploy prod and test to S3
if [ "$suffix" == "prod" ] || [ "$suffix" == "test" ]; then
  echo "Building and deploying frontends..."
  if [ "$suffix" == "test" ]; then
    s3_prefix="test."
  else
    s3_prefix=""
  fi
  echo "s3_prefix=$s3_prefix"

  cd frontend/contestant-ui
  npm run build:$suffix
  echo "aws $aws_profile s3 cp build s3://${s3_prefix}jfokus.caglabs.se --recursive --acl public-read"
  aws $aws_profile s3 cp build s3://${s3_prefix}jfokus.caglabs.se --recursive --acl public-read
  cd -

  cd frontend/admin-ui
  npm run build:$suffix
  echo "aws $aws_profile s3 cp dist/admin-ui/ s3://${s3_prefix}admin.jfokus.caglabs.se --recursive --acl public-read"
  aws $aws_profile s3 cp dist/admin-ui/ s3://${s3_prefix}admin.jfokus.caglabs.se --recursive --acl public-read
  cd -

  cd frontend/race-leaderboard-ui
  npm run build:$suffix
  echo "aws $aws_profile s3 cp build s3://${s3_prefix}leaderboard.jfokus.caglabs.se --recursive --acl public-read"
  aws $aws_profile s3 cp build s3://${s3_prefix}leaderboard.jfokus.caglabs.se --recursive --acl public-read
  cd -
fi

#echo "Packaging backend stack..."
#aws cloudformation package --template-file sam-template.yaml --output-template-file sam-template-output.yaml --s3-bucket se.caglabs.cloudracing2 $aws_profile

#echo "Deploying backend stack..."
#aws cloudformation deploy --template-file sam-template-output.yaml --stack-name $stack_name --capabilities CAPABILITY_NAMED_IAM  --parameter-overrides StageName=$suffix $aws_profile

