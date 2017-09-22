#!groovy

def foldername = 'hosmer-snowflakes/yorkshire'

String piazzaprops = readFileFromWorkspace("piazza.properties")
piazzaconfigs = new ConfigSlurper().parse( piazzaprops )
println piazzaconfigs.pzprojects

folder("${foldername}/piazza") {
  displayName("piazza")
}

def gitprefix = 'https://github.com/venicegeo/'

for(project in piazzaconfigs.pzprojects) {
  pipelineJob("${foldername}/piazza/${project.name}-pipeline") {
    description("Piazza pipeline")
    triggers {
      gitHubPushTrigger()
    }
    environmentVariables {
      env('THREADFIX_ID', project.threadfixId)
    }
    definition {
      cpsScm {
        scm {
          git {
            remote {
              url("${gitprefix}${project.name}")
              branch("*/master")
            }
          }
        }
      }
    }
    parameters {
      for(param in piazzaconfigs.pzparams) {
        "${param.type}"("${param.name}", "${param.defaultVaue}", "${param.description}")
      }
      for(credsparam in piazzaconfigs.pzcredsparams) {
        "${credsparam.type}"("${credsparam.name}") {
          defaultValue("${credsparam.defaultValue}")
          description("${credsparam.description}")
        }
      }
    }
  }
}
