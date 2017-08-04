#!groovy

job('Beachfront Seed') {
    steps {
        dsl {
            external('jobs/_includeseed/Beachfront.groovy')
            removeAction('DISABLE')
        }
    }
}

job('Piazza Seed') {
    steps {
        dsl {
            external('jobs/_includeseed/Piazza.groovy')
            removeAction('DISABLE')
        }
    }
}

