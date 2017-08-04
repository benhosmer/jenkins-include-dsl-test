#!groovy

package lib

class Beachfront {

  folder("venice/beachfront") {
    displayName("beachfront")
  }

  // BF Projects
  def bfprojects = [
    [
      name: 'bf_TidePrediction',
      threadfixId: '67'
    ],[
      name: 'bf-ui',
      threadfixId: '63'
    ],[
      name: 'bf-swagger',
      threadfixId: '68'
    ],[
      name: 'bf-api',
      threadfixId: '57'
    ],[
      name: 'pzsvc-ndwi-py'
    ],[
      name: 'bf-geojson-geopkg-converter',
      threadfixId: '117'
    ],[
      name: 'bf-ia-broker',
      threadfixId: '116'
    ],[
      name: 'bfalg-shape'
    ],[
      name: 'pzsvc-exec'
    ],[
      name: 'venicegeo-conda-recipes',
      childjobs: ['pzsvc-exec-pipeline']
    ]
  ]


  for(i in bfprojects) {
    pipelineJob("venice/beachfront/${i.name}-pipeline") {
      description("Beachfront pipeline")
      triggers {
        gitHubPushTrigger()
        if(i.childjobs) {
          for(job in i.childjobs) {
            upstream(job, 'SUCCESS')
          }
        }
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
        stringParam("ARTIFACT_STORAGE_DEPLOY_URL", "https://nexus.devops.geointservices.io/content/repositories/Piazza/", "Project artifact storage location for maven and others.")
        stringParam("SONAR_URL", "https://sonar.geointservices.io", "URL to upload data to sonar.")
        stringParam("IONCHANNEL_ENDPOINT_URL", "https://api.ionchannel.io/", "URL to connect to ionchannel.")
        stringParam("GIT_URL", "https://github.com/venicegeo/${i.name}.git", "Git URL")
        stringParam("GIT_BRANCH", "master", "Default git branch")
        stringParam("PHASE_ONE_PCF_SPACE", "int", "Phase one Cloudfoundry space")
        stringParam("PHASE_ONE_PCF_DOMAIN", "int.geointservices.io", "Phase one Cloudfoundry domain")
        stringParam("PHASE_TWO_PCF_SPACE", "stage", "Phase two Cloudfoundry space")
        stringParam("PHASE_TWO_PCF_DOMAIN", "stage.geointservices.io", "Phase two Cloudfoundry domain")
        stringParam("PCF_API_ENDPOINT", "api.devops.geointservices.io", "Cloudfoundry API endpoint")
        stringParam("PCF_ORG", "piazza", "PCF Organization")
        stringParam("THREADFIX_URL", "https://threadfix.devops.geointservices.io", "URL to upload data to threadfix.")
        if (i.threadfixId != null) {
         stringParam("THREADFIX_ID", "${i.threadfixId}", "Threadfix app id")
        }
        stringParam("SSPF_PACKAGE", "https://github.com/venicegeo/sspf/archive/master.zip", "Security Scan Pass/Fail archive package")
        stringParam("INTEGRATION_GIT_URL", "https://github.com/venicegeo/bftest-integration.git", "Integration Tests Git URL")
        stringParam("INTEGRATION_GIT_BRANCH", "master", "Default integration tests git branch")
        stringParam("JAVA_BUILDPACK_NAME", "java_buildpack", "Name for the Java Buildpack")
        stringParam("PYTHON_BUILDPACK_NAME", "python_buildpack_v1_5_18", "Name for the Python Buildpack")
        booleanParam("SKIP_INTEGRATION_TESTS", false, "Skipping postman tests")
        booleanParam("DEPLOY_PHASE_TWO", true, "Perform two phase CF deployment")
        booleanParam("SECENV", false, "Enable security banner and configurations")
        credentialsParam("CONSENT_BANNER_TEXT") {
          defaultValue("824eee31-0408-49e2-9a7e-70b59297b1b9")
          description("Consent banner text")
        }
        credentialsParam("SONAR_TOKEN") {
          defaultValue("sonar-publish-token")
          description("Sonar Upload Token")
        }
        credentialsParam("THREADFIX_API_KEY") {
          defaultValue("BF_THREADFIX_API_KEY")
          description("Beachfront's Threadfix API Key")
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
        credentialsParam("BEACHFRONT_PIAZZA_AUTH"){
          defaultValue("Bf-Api-GeoAxis-PKI-Credentials")
          description("Beachfront's Piazza access key")
        }
        stringParam("GEOAXIS_DOMAIN", "gxisaccess.gxaccess.com", "Geoaxis URL")
        stringParam("PIAZZA_URL", "geointservices.io", "Piazza's URL without prefixes, which allows for the changing of spaces. Ex: piazza.{SPACE}.{PIAZZA_URL}")
        credentialsParam("GEOAXIS_CLIENT_ID") {
          defaultValue("b81d7d20-3576-4f02-ac90-4e6fd5a9d453")
        }
        credentialsParam("GEOAXIS_SECRET") {
          defaultValue("e83dfc65-4462-4a80-a04d-57ab8da20ebd")
        }
        credentialsParam("SAUCELAB_ACCESS") {
          defaultValue("1ba84f72-0a02-45e2-8869-cfa62df01251")
        }
        credentialsParam("PL_API_KEY") {
          defaultValue("7a64953f-283a-4a28-824f-4e96760574e8")
        }
        credentialsParam("GX_TEST_USER") {
          defaultValue("gx_test_account")
        }
        if(i.requiresJksCreds) {
          credentialsParam("JKS_FILE") {
            defaultValue("ca8591a7-fc1f-4b6d-808e-c9944c9bf4f8")
            description("Java Key Store")
          }
          stringParam("JKS_PASSPHRASE", "ff7148c6-2855-4f3d-bd2e-3aa296b09d98", "Java Key Store Passphrase")
          stringParam("PZ_PASSPHRASE", "da3092c4-d13d-4078-ab91-a630c61547aa", "PZ Passphrase")
        }
      }
    }
  }

  //Beachfront health-test job
  def bfhealthprojects = ['bftest-integration']
  for(i in bfhealthprojects) {
    pipelineJob("venice/beachfront/${i}-pipeline") {
      description("Beachfront pipeline")
      triggers {
        cron('H 8 * * *')
      }
      definition {
        cpsScm {
          scm {
            git {
              remote {
                url("${gitprefix}${i}")
                branch("*/master")
              }
            }
          }
       }
     }
   }
  }

}
