## Bygg & deploy för dev

```bash
 mvn clean package
 aws cloudformation package --template-file sam-template.yaml --output-template-file sam-template-output.yaml --s3-bucket se.caglabs.cloudracing2 --profile <profilnamn>
 aws cloudformation deploy --template-file sam-template-output.yaml --stack-name cag-cloud-racing-stack-dev --capabilities CAPABILITY_NAMED_IAM --profile <profilnamn>
```

## Bygg & deploy för prod

```bash
 mvn clean package
 aws cloudformation package --template-file sam-template.yaml --output-template-file sam-template-output.yaml --s3-bucket se.caglabs.cloudracing2 --profile <profilnamn>
 aws cloudformation deploy --template-file sam-template-output.yaml --stack-name cag-cloud-racing-stack-prod --capabilities CAPABILITY_NAMED_IAM --parameter-overrides StageName=prod --profile <profilnamn>
```

## Bygg & deploy med egen stack

```bash
 mvn clean package
 aws cloudformation package --template-file sam-template.yaml --output-template-file sam-template-output.yaml --s3-bucket se.caglabs.cloudracing2 --profile <profilnamn>
 aws cloudformation deploy --template-file sam-template-output.yaml --stack-name cag-cloud-racing-stack-<ditt signum> --capabilities CAPABILITY_NAMED_IAM --parameter-overrides StageName=<ditt signum> --profile <profilnamn>
```
