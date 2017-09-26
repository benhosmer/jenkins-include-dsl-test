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
    pipelineJob("venice/${project.foldername}/${project.repos.name}-pipeline") {
      description("${project.repos.name} pipeline")
      triggers {
        gitHubPushTrigger()
      }
      environmentVariables {
        env('THREADFIX_ID', "${project.repos.threadfixId}")
      }
      definition {
        cpsScm {
          scm {
            git {
              remote {
                url("${gitprefix}${project.repos.name}")
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
  println credparam
          credentialsParam("\"${credparam.name}\"") {
            defaultValue("\"${credparam.defaultValue}\"")
            description("\"${credparam.description}\"")
          }
        }
      }
    }
  }
}
