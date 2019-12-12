pipeline {
  agent any
  stages {
    stage('error') {
      steps {
        sh 'mvn clean package'
      }
    }

    stage('build') {
      steps {
        sh 'cp /root/.jenkins/workspace/iDoc_master/target/iDoc-1.0.war /opt/wildfly/standalone/deployments'
      }
    }

  }
}