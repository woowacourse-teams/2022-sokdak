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

    stage('Test'){
      steps{
        sh "echo 'Test'"
        dir('backend/sokdak') {
          sh './gradlew clean test'
        }
      }
    }

    stage('Build'){
      steps{
        sh "echo 'Build Jar'"
        dir('backend/sokdak') {
          sh './gradlew bootJar'
        }
      }
    }
    
    stage('Deploy'){
      withCredentials([sshUserPrivateKey(credentialsId: "sokdak-pem", keyFileVariable: 'my_private_key_file')]) {
        def remote = [:]
        remote.name = "sokdak-pem"
        remote.host = "192.168.1.196"
        remote.user = "ubuntu"
        remote.allowAnyHosts = true
        remote.identityFile = my_private_key_file
                
        sh "echo 'Deploy AWS'"
        dir('backend/sokdak/build/libs'){
            sh 'scp -o StrictHostKeyChecking=no -i ${my_private_key_file} *.jar ubuntu@192.168.1.196:/home/ubuntu/sokdak'
        }
        sh 'ssh -o StrictHostKeyChecking=no -i ${my_private_key_file} ubuntu@192.168.1.196 "cd sokdak && ls && ./deploy.sh"'
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
