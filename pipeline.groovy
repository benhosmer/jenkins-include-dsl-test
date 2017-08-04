#!groovy

job('Beachfront Seed') {
    triggers {
      upstream('example-1', 'UNSTBLE')
    }
    steps {
        dsl {
            external('jobs/example-1/Beachfront.groovy')
            removeAction('DISABLE')
        }
    }
}

job('Piazza Seed') {
    triggers {
      upstream('example-1', 'UNSTABLE')
    }
    steps {
        dsl {
            external('jobs/example-1/Piazza.groovy')
            removeAction('DISABLE')
        }
    }
}

