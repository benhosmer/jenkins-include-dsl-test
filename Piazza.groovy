#!groovy

class Piazza {
  static pzprojects = [
    [
      name: 'pz-access',
      threadfixId: '115'
    ],[
      name: 'pz-docs',
      threadfixId: '115'
    ]
  ]

  static pzcredsparams = [
    [
      type: 'credentialsParam',
      name: 'ARTIFACT_READ_ONLY',
      defaultValue: 'NOUSERPASS',
      description: 'No user creds'
   ]
  ]
  static pzparams = [
    [
      type: 'stringParam',
      name: 'ARTIFACT_STORAGE_URL',
      defaultValue: 'https://nexus.devops.geointservices.io',
      description: 'Nexus url'
    ]
  ]
}

