pipeline {
    agent any

    tools {
        maven "M3"
        jdk "jdk-17"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/othman-15/workshop2-groupe4-othman-akram-jabran-mohammed-.git'
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify'
            }
        }
           /* =========================
           3️⃣ OWASP Dependency-Check
           ========================= */
        stage('OWASP Dependency Check') {
            steps {
                dependencyCheck additionalArguments: '''
                    --scan .
                    --format XML
                    --out dependency-check-report
                ''', odcInstallation: 'OWASP-Dependency-Check'
            }
        }

        /* =========================
           4️⃣ Publication du rapport OWASP
           ========================= */
        stage('Publish OWASP Report') {
            steps {
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }

        /* =========================
           5️⃣ Security Gate – Blocage si CRITICAL
           ========================= */
        stage('Dependency Security Gate') {
            steps {
                script {
                    def report = readFile 'dependency-check-report/dependency-check-report.xml'
                    if (report.contains('<severity>CRITICAL</severity>')) {
                        error '❌ Build FAILED : CRITICAL vulnerabilities detected!'
                    } else {
                        echo '✅ No CRITICAL vulnerabilities found'
                    }
                }
            }
        }


        stage('SonarQube Analysis') {
            environment {
                scannerHome = tool 'Sonar-Scanner-4'
            }
            steps {
                withSonarQubeEnv('SonarQube_ser') {
                    sh """
                        ${scannerHome}/bin/sonar-scanner \
                        -Dsonar.projectKey=springboot-app \
                        -Dsonar.projectName=springboot-app \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.tests=src/test/java \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.junit.reportsPath=target/surefire-reports \
                        -Dsonar.java.coveragePlugin=jacoco \
                        -Dsonar.jacoco.reportPaths=target/jacoco.exec
                    """
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    timeout(time: 5, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }
       
    }
}
