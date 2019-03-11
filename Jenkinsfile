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
          functions.validateTemplate()
          functions.updateDeploymentBucket()
          functions.uploadTemplates()
        }
      }
    }

    stage('Test and build deployment package') {
      steps {
        script {
          sh 'node -v'
          sh 'npm run build'
        }
      }
    }

    stage('Copy deployment package to S3 deployment bucket') {
      steps {
        script {
          functions.uploadLambdaCode()
        }
      }
    }

    stage('Test lambda') {
      steps {
        echo 'Deploying....'
      }
    }
  }
}
