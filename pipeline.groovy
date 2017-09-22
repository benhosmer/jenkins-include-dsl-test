#!groovy

import groovy.io.FileType

def foldername = 'hosmer-snowflakes/yorkshire'
def root = hudson.model.Executor.currentExecutor().getCurrentWorkspace()

String props = readFileFromWorkspace("piazza.properties")
configs = new ConfigSlurper().parse( props )
println configs
