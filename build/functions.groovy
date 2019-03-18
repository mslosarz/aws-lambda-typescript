// Environment name will be taken from the ROOT folder
def env() {
  return env.JOB_NAME.tokenize('/').get(0).toLowerCase()
}

def validateTemplates() {
  def templates = findFiles(glob: 'cfn/**')
  for (template in templates) {
    cfnValidate(file: template.getPath())
  }
}

def updateDeploymentBucket() {
  def envName = env()
  cfnUpdate(stack: "${envName}-deployment-bucket",
    file: 'cfn/deployment/s3bucket.cfn.yaml',
    params: ['BucketName': envName],
    timeoutInMinutes: 10,
    tags: ['Environment=' + envName],
    pollInterval: 10000)
}

def getDeploymentBucketName() {
  return cfnDescribe(stack: "${env()}-deployment-bucket").BucketName
}

def getDeploymentPath() {
  return "artifacts/${env()}/${env.JOB_BASE_NAME}/${env.BUILD_NUMBER}"
}

def uploadTemplates() {
  s3Upload(file: 'cfn', bucket: getDeploymentBucketName(), path: "${getDeploymentPath()}/cfn")
}

def uploadLambdaCode() {
  s3Upload(file: 'dist/lambda.zip', bucket: getDeploymentBucketName(), path: "${getDeploymentPath()}/lambda.zip")
}

def updateLambda() {
  def envName = env()
  cfnUpdate(stack: "${envName}-ts-lambda",
    file: 'cfn/lambda/lambda.cfn.yaml',
    params: [
      'Environment'     : envName,
      'DeploymentBucket': getDeploymentBucketName(),
      'LambdaZipPath'   : "${getDeploymentPath()}/lambda.zip"
    ],
    timeoutInMinutes: 10,
    tags: ['Environment=' + envName],
    pollInterval: 10000)
}

def getLambdaName() {
  return cfnDescribe(stack: "${env()}-ts-lambda").TypescriptLambda
}

def callAWSLambda() {
  def result = invokeLambda(
    functionName: getLambdaName(),
    payloadAsString: '{}',
    returnValueAsString: true
  )
  echo "-------------------------------------------------"
  echo "Execution Result:"
  echo result
  echo "-------------------------------------------------"
}

return this
