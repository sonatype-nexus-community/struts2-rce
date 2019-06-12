def pom

pipeline 
{
    agent any
    tools {
        maven 'maven'
        jdk 'jdk8'
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/Iletee/webgoat-test.git'
            }
        }
        stage('Maven Build') {
            steps {
                sh '''
                    cd webgoat
                    mvn versions:set -DnewVersion=1.0
                    mvn clean install
                    sleep 3
                '''
            }
        }
        stage('Read Pom') {
            steps {
                script {
                    pom = readMavenPom file: 'webgoat/pom.xml'
                }
            }
        }
        stage('Publish Artifact and Tag Creation') {
            parallel {
                stage('Publish Snapshot to Release') {
                    steps {
                        nexusPublisher nexusInstanceId: 'nxrm3', \
                            nexusRepositoryId: 'maven-releases', \
                            packages: [[$class: 'MavenPackage', \
                            mavenAssetList: [[classifier: '', extension: '', filePath: "webgoat/target/${pom.artifactId}-${pom.version}.${pom.packaging}"]], \
                            mavenCoordinate: [artifactId: "${pom.artifactId}", groupId: "${pom.groupId}", packaging: "${pom.packaging}", version: "${pom.version}.${env.BUILD_NUMBER}"]]]
                    }
                }
                stage('Create Tags') {
                    steps {
                        script {
                            try {
                                createTag nexusInstanceId: 'nxrm3', tagName: 'passed-dev-scan'
                                createTag nexusInstanceId: 'nxrm3', tagName: 'passed-test-scan'
                                createTag nexusInstanceId: 'nxrm3', tagName: 'passed-prod-scan'
                            }
                            catch (Exception e) { //com.sonatype.nexus.api.exception.RepositoryManagerException e) {
                                echo e.toString()
                            }
                        }
                    }
                }
            }
        }
        stage('Scanning, Staging, and Tagging') {
            parallel {
                stage('Dev') {
                    stages {
                        stage('IQ Scan Dev') {
                            steps {
                                nexusPolicyEvaluation(iqApplication: 'webgoat', iqStage: 'build')
                            }
                        }
                        stage('Associate Dev Tag') {
                            steps {
                                associateTag nexusInstanceId: 'nxrm3', tagName: 'passed-dev-scan', \
                                search: [[key: 'repository', value: 'maven-releases'], [key: 'version', value: "${pom.version}.${env.BUILD_NUMBER}"]]
                            }
                        }
                    }
                }
                stage('Test') {
                    stages {
                        stage('IQ Scan Test') {
                            steps {
                                nexusPolicyEvaluation(iqApplication: 'webgoat', iqStage: 'stage-release')
                            }
                        }
                        stage('Associate Test Tag') {
                            steps {
                                associateTag nexusInstanceId: 'nxrm3', tagName: 'passed-test-scan', \
                                search: [[key: 'repository', value: 'maven-releases'], [key: 'version', value: "${pom.version}.${env.BUILD_NUMBER}"]]
                            }
                        }
                    }
                }
                stage('Prod') {
                    stages {
                        stage('IQ Scan Prod') {
                            steps {
                                nexusPolicyEvaluation(iqApplication: 'webgoat', iqStage: 'release')
                            }
                        }
                        stage('Associate Prod Tag') {
                            steps {
                                associateTag nexusInstanceId: 'nxrm3', tagName: 'passed-prod-scan', \
                                search: [[key: 'repository', value: 'maven-releases'], [key: 'version', value: "${pom.version}.${env.BUILD_NUMBER}"]]
                            }
                        }
                    }
                }
            }
        }
    }
    post ('Promotion') {
        always {
            moveComponents destination: 'maven-dev', nexusInstanceId: 'nxrm3', tagName: 'passed-dev-scan', repositoryName: 'maven-releases'
            echo 'Successfully promoted component'
        }
    }
}
