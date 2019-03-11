/**
 * CREDENTIALS:
 *  - ADD Jenkins global credentials -> add PRIV_AWS_ACCESS (aws key / assigned to admin user)
 *  - ADD ssh key for Jenkins user (jenkins / github)
 * CREATE FOLDER (name of folder will be used as an environment eg dev/test/prod)
 *  - inside FOLDER create pipeline project that will points to this Jenkinsfile
 */

def functions

pipeline {
  agent any

  options {
    timeout(time: 15, unit: "MINUTES")
    withAWS(region: "${params.AWS_REGION}")
    withCredentials([[$class           : "AmazonWebServicesCredentialsBinding",
                      credentialsId    : "${params.CREDENTIAL_ID}",
                      accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                      secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']])
  }

  parameters {
    choice(name: 'CREDENTIAL_ID', choices: 'PRIV_AWS_ACCESS', description: 'Supply name of AWS_KEY (Stored on Jenkins)')
    choice(name: 'AWS_REGION', choices: 'eu-west-1\neu-west-2', description: 'Pick up region where app should be deployed (Ireland / London)')
  }

  stages {
    stage('Load functions') {
      steps {
        script {
          functions = load(pwd() + '/build/functions.groovy')
        }
      }
    }

    stage('Setup s3 deployment bucket') {
      steps {
        script {
          functions.validateTemplates()
          functions.updateDeploymentBucket()
          functions.uploadTemplates()
        }
      }
    }

    stage('Test and build deployment package') {
      steps {
        script {
          // clean up old dist files
          sh 'rm -fr dist/*'
          sh 'node -v'
          sh 'npm run build'
          sh 'npm prune --production'
          sh 'zip -q -r dist/lambda.zip node_modules dist'
        }
      }
    }

    stage('Copy deployment package to S3 and update lambda') {
      steps {
        script {
          functions.uploadLambdaCode()
          functions.updateLambda()
        }
      }
    }

    stage('Test deployed lambda') {
      steps {
        script {
          functions.callAWSLambda()
        }
      }
    }
  }
}
