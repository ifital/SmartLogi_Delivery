pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "latifi2001/smartlogi_delivery-app"
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Docker Build') {
            when {
                expression { false }
            }
            steps {
                echo "Docker Build désactivé"
            }
        }

        stage('Docker Push') {
            when {
                expression { false }
            }
            steps {
                echo "Docker Push désactivé"
            }
        }
    }

    post {
        success {
            echo "Pipeline exécuté sans Docker"
        }
        failure {
            echo "Échec du pipeline"
        }
        always {
            cleanWs()
        }
    }
}
