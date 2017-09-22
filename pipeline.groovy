#!groovy

import groovy.io.FileType

def foldername = 'hosmer-snowflakes/yorkshire'
def root = hudson.model.Executor.currentExecutor().getCurrentWorkspace()

Properties properties = new Properties()
File propertiesFile = new File('./piazza.properties')
propertiesFile.withInputStream {
    properties.load(it)
}

println propertiesFile
