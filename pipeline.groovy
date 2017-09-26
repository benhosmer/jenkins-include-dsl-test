#!groovy

String configfile = readFileFromWorkspace("venice.json")

def slurper = new groovy.json.JsonSlurper()
def veniceprojects = slurper.parseText(configfile)
def gitprefix = 'https://github.com/venicegeo/'

for (project in veniceprojects.projects) {
  folder("venice/${project.foldername}") {
    displayName("${project.foldername} pipelines")
  }
  for (repo in project.repos) {
    pipelineJob("venice/${project.foldername}/${repo.name}-pipeline") {
      description("${repo.name} pipeline")
      triggers {
        gitHubPushTrigger()
      }
      environmentVariables {
        env('THREADFIX_ID', "${repo.threadfixId}")
      }
      definition {
        cpsScm {
          scm {
            git {
              remote {
                url("${gitprefix}${repo.name}")
                branch("*/master")
              }
            }
          }
        }
      }
      parameters {
        for(param in project.jobparams) {
          "${param.type}"("\"${param.name}\"", "\"${param.defaultvalue}\"", "\"${param.description}\"")
        }
        for(credparam in project.credparams) {
          credentialsParam("\"${credparam.name}\"") {
            defaultValue("\"${credparam.defaultvalue}\"")
            description("\"${credparam.description}\"")
          }
        }
      }
    }
  }
}
