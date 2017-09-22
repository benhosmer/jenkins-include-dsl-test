#!groovy

import groovy.io.FileType

def foldername = 'hosmer-snowflakes/yorkshire'
def root = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
evaluate(new File("${root}/beachfront.properties"))
evaluate(new File("${root}/piazza.properties"))

def datas = readYaml file: 'my.yml'
println datas

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
