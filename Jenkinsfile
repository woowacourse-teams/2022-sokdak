pipeline{
  agent any
  tools {
      gradle 'gradle'
  }
  stages{
    stage('Ready'){
      steps{
        sh "echo 'Ready~'"
        git branch: 'feature/CICD',
          credentialsId: 'sokdak_hook',
          url: 'https://github.com/woowacourse-teams/2022-sokdak'

        dir('frontend'){
          sh 'npm i'
        }
      }
    }

    stage('Test'){
      steps{
        dir('frontend'){
          sh 'chmod 777 node_modules'
          sh 'npm run test'
        }
      }
    }

    stage('Build'){
      steps{
        dir('frontend'){
          sh 'npm run build-dev'
        }
      }
    }

    stage('Deploy'){
      steps{
        script{
          withCredentials([sshUserPrivateKey(credentialsId: "front-key", keyFileVariable: 'front_key_file')]) {
            def remote = [:]
            remote.name = "pem-key"
            remote.host = "${env.DEV_FRONT_IP}"
            remote.user = "ubuntu"
            remote.allowAnyHosts = true
            remote.identityFile = front_key_file

            dir('frontend'){
                sh "scp -o StrictHostKeyChecking=no -i ${front_key_file} -r dist ubuntu@${env.DEV_FRONT_IP}:/home/ubuntu"
            }
            sh "ssh -o StrictHostKeyChecking=no -i ${front_key_file} ubuntu@${env.DEV_FRONT_IP} 'sudo service nginx restart'"
          }
        }
      }
    }
  }
  post {
    success {
        slackSend (channel: 'jenkins', color: '#00FF00', message: "FE_SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
    }
    failure {
        slackSend (channel: 'jenkins', color: '#FF0000', message: "FE_FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
    }
  }
}