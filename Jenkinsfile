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
        sh "echo '########### CLONE DEV ###########'"
      }
    }

    stage('BE-Test'){
      steps{
        sh "echo '########### BE TEST START ###########'"
        dir('backend/sokdak') {
          sh './gradlew clean test'
        }
        sh "echo '########### BE TEST SUCCESS ###########'"
      }
    }

    stage('BE-Build'){
      steps{
        sh "echo '########### BE BUILD START ###########'"
        dir('backend/sokdak') {
          sh './gradlew bootJar'
        }
        sh "echo '########### BE BUILD SUCCESS ###########'"
      }
    }

    stage('BE-Deploy'){
      steps{
        script{
          withCredentials([sshUserPrivateKey(credentialsId: "back-key", keyFileVariable: 'my_private_key_file')]) {
            sh "echo '########### BE DEPLOY START ###########'"
            dir('backend/sokdak/build/libs'){
                sh "scp -o StrictHostKeyChecking=no -i ${my_private_key_file} *.jar ubuntu@${env.DEV_BACK_IP}:/home/ubuntu/sokdak"
            }
            sh "ssh -o StrictHostKeyChecking=no -i ${my_private_key_file} ubuntu@${env.DEV_BACK_IP} 'cd sokdak && ./deploy.sh'"
            sh "echo '########### BE DEPLOY SUCCESS ###########'"
            sh "echo '########### BE COMPLETE ###########'"
          }
        }
      }
    }

    stage('FE-Install'){
      steps{
        sh "echo '########### FE MODULE INSTALL START ###########'"
        dir('frontend'){
          sh 'npm i'
        }
        sh "echo '########### FE MODULE INSTALL SUCCESS ###########'"
      }
    }

    stage('FE-Build'){
      steps{
        sh "echo '########### FE BUILD START ###########'"
        dir('frontend'){
          sh 'npm run build-dev'
        }
        sh "echo '########### FE BUILD SUCCESS ###########'"
      }
    }

    stage('FE-Deploy'){
      steps{
        script{
          withCredentials([sshUserPrivateKey(credentialsId: "front-key", keyFileVariable: 'front_key_file')]) {
            sh "echo '########### FE DEPLOY START ###########'"
            dir('frontend'){
                sh "scp -o StrictHostKeyChecking=no -i ${front_key_file} -r dist ubuntu@${env.DEV_FRONT_IP}:/home/ubuntu"
            }
            sh "ssh -o StrictHostKeyChecking=no -i ${front_key_file} ubuntu@${env.DEV_FRONT_IP} 'sudo service nginx restart'"
            sh "echo '########### FE DEPLOY SUCCESS ###########'"
            sh "echo '########### FE COMPLETE ###########'"
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

