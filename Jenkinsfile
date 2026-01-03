pipeline {
    // Utiliser un agent Docker avec Maven pour éviter les installations locales
    agent {
        docker {
            image 'maven:3.8-openjdk-11'
            args '-v $HOME/.m2:/root/.m2'
        }
    }
    
    environment {
        SONAR_HOST_URL = 'http://sonarqube:9000'
        PROJECT_NAME = 'finance-refactoring'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Récupération du code source...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo 'Compilation du projet...'
                sh 'mvn clean compile -B'
            }
        }
        
        stage('Unit Tests') {
            steps {
                echo 'Exécution des tests unitaires...'
                sh 'mvn test -B'
            }
            post {
                always {
                    // Publication des rapports de tests JUnit
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                    
                    // Publication du rapport de couverture JaCoCo
                    jacoco(
                        execPattern: 'target/jacoco.exec',
                        classPattern: 'target/classes',
                        sourcePattern: 'src/main/java',
                        exclusionPattern: 'src/test*'
                    )
                }
            }
        }
        
        stage('Code Quality Analysis') {
            steps {
                echo 'Analyse de qualité avec SonarQube...'
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar -Dsonar.projectKey=${PROJECT_NAME} -Dsonar.host.url=${SONAR_HOST_URL} -B'
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                echo 'Vérification du Quality Gate SonarQube...'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        
        stage('Package') {
            steps {
                echo 'Création du package JAR...'
                sh 'mvn package -DskipTests -B'
            }
            post {
                success {
                    // Archivage de l'artefact JAR
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
    }
    
    post {
        always {
            echo 'Nettoyage et notifications...'
            cleanWs()
        }
        success {
            echo 'Build réussi!'
            emailext (
                attachLog: true,
                subject: "✅ Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                to: 'professor@university.edu',
                body: """
                    <h2>Build Réussi</h2>
                    <p><strong>Projet:</strong> ${env.JOB_NAME}</p>
                    <p><strong>Build:</strong> #${env.BUILD_NUMBER}</p>
                    <p><strong>URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                    <p><strong>Durée:</strong> ${currentBuild.durationString}</p>
                    <hr>
                    <p>Voir les détails dans Jenkins.</p>
                """,
                mimeType: 'text/html'
            )
        }
        failure {
            echo 'Build échoué!'
            emailext (
                attachLog: true,
                subject: "❌ Build FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                to: 'professor@university.edu',
                body: """
                    <h2>Build Échoué</h2>
                    <p><strong>Projet:</strong> ${env.JOB_NAME}</p>
                    <p><strong>Build:</strong> #${env.BUILD_NUMBER}</p>
                    <p><strong>URL:</strong> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                    <p><strong>Durée:</strong> ${currentBuild.durationString}</p>
                    <hr>
                    <p>Veuillez consulter les logs pour plus de détails.</p>
                """,
                mimeType: 'text/html'
            )
        }
        unstable {
            echo 'Build instable - vérifier les tests!'
        }
    }
}

