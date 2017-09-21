#!groovy

import groovy.io.FileType
import static Piazza.pzprojects
import static Piazza.pzparams
import static Piazza.pzcredsparams

println pzparams
println pzprojects
println pzcredsparams
def foldername = 'yorkshire'
def root = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
//evaluate(new File("${root}/beachfront.properties"))
//evaluate(new File("${root}/piazza.properties"))

folder("${foldername}/piazza") {
  displayName("piazza")
}

def gitprefix = 'https://github.com/venicegeo/'

for(project in pzprojects) {
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
      for(param in pzparams) {
        "${param.type}"("${param.name}", "${param.defaultVaue}", "${param.description}")
      }
      for(credsparam in pzcredsparams) {
        "${credsparam.type}"("${credsparam.name}") {
          defaultValue("${credsparam.defaultValue}")
          description("${credsparam.description}")
        }
      }
    }
  }
}
