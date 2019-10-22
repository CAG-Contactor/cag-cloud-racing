## Bygg & deploy

```bash
 mvn clean package
 aws cloudformation package --template-file sam-template.yaml --output-template-file sam-template-output.yaml --s3-bucket se.caglabs.cloudracing2
 aws cloudformation deploy --template-file sam-template-output.yaml --stack-name cag-cloud-racing-stack --capabilities CAPABILITY_IAM
```
