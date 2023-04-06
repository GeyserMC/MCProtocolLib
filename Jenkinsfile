pipeline {
    agent any
    tools {
        maven 'Maven 3'
        jdk 'Java 8'
    }
    options {
        buildDiscarder(logRotator(artifactNumToKeepStr: '20'))
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage ('Javadocs') {
            when {
                branch "master"
            }

            steps {
                sh 'mvn javadoc:javadoc'
                step([$class: 'JavadocArchiver', javadocDir: 'target/site/apidocs', keepAll: false])
            }
        }
    }
}
