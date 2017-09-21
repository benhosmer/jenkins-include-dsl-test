#!groovy

import groovy.io.FileType

println Piazza.pzparams
println Piazza.pzprojects
println Piazza.pzcredsparams
def foldername = 'hosmer-snowflakes/yorkshire'
def root = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
//evaluate(new File("${root}/beachfront.properties"))
//evaluate(new File("${root}/piazza.properties"))

folder("${foldername}/piazza") {
  displayName("piazza")
}

def gitprefix = 'https://github.com/venicegeo/'

for(project in Piazza.pzprojects) {
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
      for(param in Piazza.pzparams) {
        "${param.type}"("${param.name}", "${param.defaultVaue}", "${param.description}")
      }
      for(credsparam in Piazza.pzcredsparams) {
        "${credsparam.type}"("${credsparam.name}") {
          defaultValue("${credsparam.defaultValue}")
          description("${credsparam.description}")
        }
      }
    }
  }
}
