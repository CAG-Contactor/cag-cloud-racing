#!/bin/bash

# exit if any command fails
set -e

usage() {
  echo "Usage: $0 [-d] [-p <profile name>] <stack name suffix>"
  echo "  -d: deploy stack"
  echo "  -p: specify AWS profile"
   1>&2; exit 1;
  }

deploy=0

while getopts "dp:" o; do
    case "${o}" in
        d)
            deploy=1
            ;;
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

echo "Packaging..."
aws cloudformation package --template-file sam-template.yaml --output-template-file sam-template-output.yaml --s3-bucket se.caglabs.cloudracing2 $aws_profile

if [ $deploy == 1 ]; then
  echo "Deploying..."
  aws cloudformation deploy --template-file sam-template-output.yaml --stack-name $stack_name --capabilities CAPABILITY_NAMED_IAM  --parameter-overrides StageName=$suffix $aws_profile
fi
