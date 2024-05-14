pipeline {
    agent any
    tools {
        jdk 'Java 17'
    }
    options {
        buildDiscarder(logRotator(artifactNumToKeepStr: '20'))
    }
    stages {
        stage ('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage ('Javadocs') {
            when {
                branch "master"
            }

            steps {
                sh './gradlew javadoc'
                step([$class: 'JavadocArchiver', javadocDir: 'protocol/build/docs/javadoc', keepAll: false])
            }
        }
    }
}
