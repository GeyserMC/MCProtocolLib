pipeline {
    agent any
    tools {
        maven "Maven 3"
        git "Default"
        jdk "jdk8"
    }
    options {
        buildDiscarder(logRotator(artifactNumToKeepStr: "5"))
    }
    stages {
        stage ("Build") {
            steps {
                sh "mvn clean package"
            }
            post {
                success {
                    junit "target/surefire-reports/**/*.xml"
                }
            }
        }

        stage ("Deploy") {
            steps {
                sh "mvn source:jar deploy -DskipTests"
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}
