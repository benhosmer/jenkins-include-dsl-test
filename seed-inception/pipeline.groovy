#!groovy

def foldername = 'yorkshire'

job('BeachfrontSeed') {
    scm {
      git("https://github.com/benhosmer/jenkins-include-dsl-test.git")
    }
    triggers {
      upstream('example-1', 'UNSTABLE')
    }
    steps {
        dsl {
            external('Piazza.groovy')
            removeAction('DISABLE')
        }
    }
}

job('PiazzaSeed') {
    scm {
      git("https://github.com/benhosmer/jenkins-include-dsl-test.git")
    }
    triggers {
      upstream('example-1', 'UNSTABLE')
    }
    steps {
        dsl {
            external('Beachfront.groovy')
            removeAction('DISABLE')
        }
    }
}

