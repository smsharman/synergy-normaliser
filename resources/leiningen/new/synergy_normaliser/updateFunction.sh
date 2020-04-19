#!/usr/bin/env bash
aws lambda update-function-code \
    --region eu-west-1 \
    --function-name synergy-normaliser-{{name}} \
    --cli-connect-timeout 6000 \
    --zip-file fileb://$(pwd)/../target/synergy-normaliser-{{name}}.jar