pipeline{
  agent any
  
  tools {
      gradle 'gradle'
  }
  
  stages{
    stage('Ready'){
      steps{
        sh "echo 'Ready'"
        git branch: 'dev',
          credentialsId: 'sokdak_hook',
          url: 'https://github.com/woowacourse-teams/2022-sokdak'
      }
    }

//     stage('Test'){
//       steps{
//         sh "echo 'Test'"
//         dir('backend/sokdak') {
//           sh './gradlew clean test'
//         }
//       }
//     }

    stage('Build'){
      steps{
        sh "echo 'Build Jar'"
        dir('backend/sokdak') {
          sh './gradlew bootJar'
        }
      }
    }
    
    stage('Deploy'){
      steps{
        script{
          withCredentials([sshUserPrivateKey(credentialsId: "pem-key", keyFileVariable: 'my_private_key_file')]) {
            def remote = [:]
            remote.name = "pem-key"
            remote.host = "${env.DEV_BACK_IP}"
            remote.user = "ubuntu"
            remote.allowAnyHosts = true
            remote.identityFile = my_private_key_file

            sh "echo 'Deploy AWS'"
            dir('backend/sokdak/build/libs'){
                sh "scp -o StrictHostKeyChecking=no -i ${my_private_key_file} *.jar ubuntu@${env.DEV_BACK_IP}:/home/ubuntu/sokdak"
            }
            sh "ssh -o StrictHostKeyChecking=no -i ${my_private_key_file} ubuntu@${env.DEV_BACK_IP} 'cd sokdak && ./deploy.sh'"
            sh "echo 'Spring Boot Running'"
          } 
        }
      }
    }
  }
    
      
  post {
    success {
        slackSend (channel: 'jenkins', color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
    }
    failure {
        slackSend (channel: 'jenkins', color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
    }
  }
}
