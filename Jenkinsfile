pipeline{
  agent any
  tools {
      gradle ‘gradle’
  }
  
  stages{
    stage(‘Bulid’){
      steps{
        dir(‘frontend’){
            sh ‘npm i’
            sh ‘npm run build’
        }
      }
    }
    
    stage(‘Deploy’){
      steps{
        script{
          withCredentials([sshUserPrivateKey(credentialsId: “front-key”, keyFileVariable: ‘front_key_file’)]) {
            def remote = [:]
            remote.name = “front-key”
            remote.host = “${env.DEV_FRONT_IP}”
            remote.user = “ubuntu”
            remote.allowAnyHosts = true
            remote.identityFile = front_key_file

            dir(‘frontend’){
              sh “scp -o StrictHostKeyChecking=no -i ${front_key_file} -r dist ubuntu@${env.DEV_FRONT_IP}:/home/ubuntu”
            }
            sh “ssh -o StrictHostKeyChecking=no -i ${front_key_file} ubuntu@${env.DEV_FRONT_IP} ‘sudo service nginx restart’”
            sh “echo ‘NGINX START’”
          }
        }
      }
    }
  }
  
  post {
    success {
        slackSend (channel: ‘jenkins’, color: ‘#00FF00’, message: “SUCCESSFUL_FE: Job ‘${env.JOB_NAME} [${env.BUILD_NUMBER}]’ (${env.BUILD_URL})“)
    }
    failure {
        slackSend (channel: ‘jenkins’, color: ‘#FF0000’, message: “FAILED_FE: Job ‘${env.JOB_NAME} [${env.BUILD_NUMBER}]’ (${env.BUILD_URL})“)
    }
  }
}