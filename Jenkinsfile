import groovy.json.*

node () {
   def policyEvaluation

   stage('Preparation') {
      checkout scm
   }
    stage('Build') {
      // Run the maven build
         sh "./mvnw -Dmaven.test.failure.ignore clean install"
   }

   stage('IQ Policy Check') {
        env.GIT_REPO_NAME = env.GIT_URL.replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
        policyEvaluation = nexusPolicyEvaluation failBuildOnNetworkError: false, iqScanPatterns: [[scanPattern: 'target/struts2-rest-showcase.war']], iqApplication: "${env.GIT_REPO_NAME}", iqStage: 'build', jobCredentialsId: ''
        sh "echo ${policyEvaluation.applicationCompositionReportUrl}"
   }

    stage('Create TAG') {
        createTag nexusInstanceId: 'nxrm3', tagAttributesJson: '''{
            "IQScan": "\${policyEvaluation.applicationCompositionReportUrl}",
            "JenkinsBuild": "\${BUILD_URL}"
          }''', tagName: "IQ-Policy-Evaluation_${currentBuild.number}"
    }

    stage('Publish') {
        nexusPublisher nexusInstanceId: 'nxrm3', nexusRepositoryId: 'maven-releases', packages: [[$class: 'MavenPackage', mavenAssetList: ["./target/struts2-rest-showcase.war"], mavenCoordinate: [artifactId: 'struts2-rest-showcase', groupId: 'org.apache.struts', packaging: 'war', version: '2.5.10']]], tagName: "IQ-Policy-Evaluation_${currentBuild.number}"
    }
}  

