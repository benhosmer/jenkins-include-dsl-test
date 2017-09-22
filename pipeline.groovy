#!groovy

def foldername = 'hosmer-snowflakes/yorkshire'

String piazzaprops = readFileFromWorkspace("piazza.properties")
piazzaconfigs = new ConfigSlurper().parse( props )
println piazzaconfigs.pzprojects
