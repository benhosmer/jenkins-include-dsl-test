#!groovy

import groovy.io.FileType

def foldername = 'hosmer-snowflakes/yorkshire'
def root = hudson.model.Executor.currentExecutor().getCurrentWorkspace()
//evaluate(new File("${root}/beachfront.properties"))
//evaluate(new File("${root}/piazza.properties"))

import org.yaml.snakeyaml.Yaml

Yaml parser = new Yaml()
List example = parser.load(("my.yaml" as File).text)

example.each{println it.subject}
