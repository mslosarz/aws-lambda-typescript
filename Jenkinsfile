/**
 * docker run --name jenkins_local -d -v jenkins_home:/var/jenkins_home -p 9000:8080 -p 50000:50000 jenkins/jenkins:lts
 * PLUGINS:
 *  - Pipeline: AWS Steps
 *
 * CREDENTIALS:
 *  - ADD Jenkins global credentials -> add PRIV_AWS_ACCESS (aws key / assigned to admin user)
 *  - ADD ssh key for Jenkins user (jenkins / github)
  */


pipeline {
  agent any

  options {
    timeout(time: 15, unit: 'MINUTES')
    withAWS(region: "${params.AWS_REGION}")
  }

  parameters {
    string(name: 'CREDENTIAL_ID', defaultValue: 'PRIV_AWS_ACCESS', description: 'Supply name of AWS_KEY (Stored on Jenkins)')
    choice(name: 'AWS_REGION', choices: 'eu-west-1\neu-west-2', description: 'Pick up region where app should be deployed (Ireland / London)')
  }

  stages {
    stage('Setup S3 Deployment bucket') {
        script {
          def response = cfnValidate(file:'cfn/deployment/s3bucket.cfn.yaml')
          echo "template description: ${response.description}"
        }
    }
    stage('Test') {
      steps {
        echo 'Testing..'
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploying....'
      }
    }
  }
}
