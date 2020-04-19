# synergy-normaliser

A Leiningen template for Synergy Event Normalisers.

## Usage

lein new synergy-normaliser <project-name>

Generates a new project for the Synergy Normaliser style to 
transform incoming events into Synergy standards. Update the core.clj namespace
with application specific logic. Creates an uberjar called:

synergy-normaliser-<project-name>.jar

Scripts deployFunction.sh and updateFunction.sh can be used
to deploy/update Lambda on AWS

Can support incoming events from:

- SNS
- SQS
- S3
- API Gateway
- Cloudwatch timed events

## License

Copyright © 2020 Hackthorn Imagineering Ltd

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

