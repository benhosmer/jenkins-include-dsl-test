#!groovy
// This is the initial seed job for venicegeo which creates all of ther
// other pipeline jobs. Each repo must contain a JenkinsFile
// that denotes the steps to take when building.


folder("${foldername}piazza") {
  displayName("piazza")
}

def gitprefix = 'https://github.com/venicegeo/'

// PZ Projects
def pzprojects = [
  [
    name: 'pz-access',
    threadfixId: '115'
  ],[
    name: 'pz-docs',
    threadfixId: '115'
  ],[
    name: 'pz-gateway',
    threadfixId: '115'
  ],[
    name: 'pz-gocommon',
    threadfixId: '115'
  ],[
    name: 'pz-idam',
    threadfixId: '115',
    requiresJksCreds: true
  ],[
    name: 'pz-ingest',
    threadfixId: '115'
  ],[
    name: 'pz-jobcommon',
    threadfixId: '115'
  ],[
    name: 'pz-jobmanager',
    threadfixId: '115'
  ],[
    name: 'pz-logger',
    threadfixId: '115'
  ],[
    name: 'pz-search-metadata-ingest',
    threadfixId: '115'
  ],[
    name: 'pz-search-query',
    threadfixId: '115'
  ],[
    name: 'pz-servicecontroller',
    threadfixId: '115'
  ],[
    name: 'pz-sak',
    threadfixId: '115'
  ],[
    name: 'pz-swagger',
    threadfixId: '115'
  ],[
    name: 'pz-workflow',
    threadfixId: '115'
  ],[
    name: 'pzsvc-hello',
    threadfixId: '115'
  ],[
    name: 'pzsvc-preview-generator',
    threadfixId: '115'
  ],[
    name: 'pztest-integration',
    threadfixId: '115'
  ],[
    name: 'pz-release',
    threadfixId: '115',
    requiresTagging: true
  ]
]

for(i in pzprojects) {
  pipelineJob("venice/piazza/${i.name}-pipeline") {
    description("Piazza pipeline")
    triggers {
      gitHubPushTrigger()
    }
    environmentVariables {
      env('THREADFIX_ID', i.threadfixId)
    }
    definition {
      cpsScm {
        scm {
          git {
            remote {
              url("${gitprefix}${i.name}")
              branch("*/master")
            }
          }
        }
      }
    }
    parameters {
      //stringParam("ARTIFACT_STORAGE_URL", "https://nexus.devops.geointservices.io/content/repositories/Piazza-Group/", "Artifact storage location for external maven dependencies.")
      stringParam("ARTIFACT_STORAGE_DEPLOY_URL", "https://nexus.devops.geointservices.io/content/repositories/Piazza/", "Project artifact storage location for maven and others.")
      stringParam("THREADFIX_URL", "https://threadfix.devops.geointservices.io", "URL to upload data to threadfix.")
      stringParam("SONAR_URL", "https://sonar.geointservices.io", "URL to upload data to sonar.")
      stringParam("IONCHANNEL_ENDPOINT_URL", "https://api.ionchannel.io/", "URL to connect to ionchannel.")
      stringParam("GIT_URL", "https://github.com/venicegeo/${i.name}.git", "Git URL")
      stringParam("GIT_BRANCH", "master", "Default git branch")
      stringParam("PHASE_ONE_PCF_SPACE", "int", "Phase one Cloudfoundry space")
      stringParam("PHASE_ONE_PCF_DOMAIN", "int.geointservices.io", "Phase one Cloudfoundry domain")
      stringParam("PHASE_TWO_PCF_SPACE", "stage", "Phase two Cloudfoundry space")
      stringParam("PHASE_TWO_PCF_DOMAIN", "stage.geointservices.io", "Phase two Cloudfoundry domain")
      stringParam("PCF_API_ENDPOINT", "api.devops.geointservices.io", "Cloudfoundry API endpoint")
      stringParam("JAVA_BUILDPACK_NAME", "java_buildpack", "Name for the Java Buildpack")
      stringParam("PCF_ORG", "piazza", "PCF Organization")
      stringParam("THREADFIX_ID", "${i.threadfixId}", "Threadfix app id")
      stringParam("SSPF_PACKAGE", "https://github.com/venicegeo/sspf/archive/master.zip", "Security Scan Pass/Fail archive package")
      stringParam("INTEGRATION_GIT_URL", "git@gitlab.devops.geointservices.io:venicegeo/pztest-integration-source.git", "Integration Tests Git URL")
      stringParam("INTEGRATION_GIT_BRANCH", "master", "Default integration tests git branch")
      stringParam("INTEGRATION_GITLAB_CREDS", "gitlab-piazza-jenkins", "credentials for integration test repo in gitlab")
      booleanParam("SKIP_INTEGRATION_TESTS", false, "Skipping postman tests")
      booleanParam("DEPLOY_PHASE_TWO", true, "Perform two phase CF deployment")
      booleanParam("SECENV", false, "Enable security banner and configurations")
      booleanParam("USE_MONGO_SSL", false, "Determines if MongoSSL should be enabled")
      if (i.requiresTagging) {
        booleanParam("TAG_AND_RELEASE", false, "Tag and release all repos to bump versions")
      }
      credentialsParam("THREADFIX_API_KEY") {
        defaultValue("PZ_THREADFIX_API_KEY")
        description("Piazza's Threadfix API Key")
      }
      credentialsParam("SONAR_TOKEN") {
        defaultValue("sonar-publish-token")
        description("Sonar Upload Token")
      }
      credentialsParam("IONCHANNEL_SECRET_KEY") {
        defaultValue("venice_ionchannel_key")
        description("IonChannel Credentials")
      }
      credentialsParam("PCF_CREDS") {
        defaultValue("ldap_baxtersh")
        description("Cloud Foundry Credentials")
      }
      credentialsParam("POSTMAN_SECRET_FILE") {
        defaultValue("579f8660-01e6-4feb-8764-ec132432ebb1")
        description("Environment file containing credentials for Postman")
      }
      credentialsParam("ARTIFACT_STORAGE_CREDS") {
        defaultValue("nexus-deployment")
        description("Nexus Repository Credentials")
      }
      if (i.requiresJksCreds) {
        credentialsParam("JKS_FILE") {
          defaultValue("ca8591a7-fc1f-4b6d-808e-c9944c9bf4f8")
          description("Java Key Store")
        }
        stringParam("JKS_PASSPHRASE", "ff7148c6-2855-4f3d-bd2e-3aa296b09d98", "Java Key Store Passphrase")
        stringParam("PZ_PASSPHRASE", "da3092c4-d13d-4078-ab91-a630c61547aa", "PZ Passphrase")
      }
    }
    environmentVariables {
      env("ARTIFACT_STORAGE_URL", "https://nexus.devops.geointservices.io/content/repositories/Piazza-Group/")
    }
  }
}

