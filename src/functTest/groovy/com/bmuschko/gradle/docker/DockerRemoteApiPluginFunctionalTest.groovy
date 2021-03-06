package com.bmuschko.gradle.docker

import static com.bmuschko.gradle.docker.fixtures.DockerConventionPluginFixture.groovySettingsFile

class DockerRemoteApiPluginFunctionalTest extends AbstractGroovyDslFunctionalTest {

    public static final String DEFAULT_USERNAME = 'Jon Doe'
    public static final String DEFAULT_PASSWORD = 'pwd'
    public static final String CUSTOM_USERNAME = 'Sally Wash'
    public static final String CUSTOM_PASSWORD = 'secret'

    def setup() {
        settingsFile << groovySettingsFile()
    }

    def "can convert credentials into PasswordCredentials type and retrieve values"() {
        given:
        buildFile << registryCredentials()
        buildFile << """
            task convert {
                doLast {
                    def passwordCredentials = docker.registryCredentials.asPasswordCredentials()
                    assert passwordCredentials instanceof org.gradle.api.credentials.PasswordCredentials
                    assert passwordCredentials.username == '$DEFAULT_USERNAME'
                    assert passwordCredentials.password == '$DEFAULT_PASSWORD'
                }
            }
        """

        expect:
        build('convert')
    }

    def "can convert credentials into PasswordCredentials type and change values"() {
        given:
        buildFile << registryCredentials()
        buildFile << """
            task convert {
                doLast {
                    def passwordCredentials = docker.registryCredentials.asPasswordCredentials()
                    assert passwordCredentials instanceof org.gradle.api.credentials.PasswordCredentials
                    passwordCredentials.username = '$CUSTOM_USERNAME'
                    passwordCredentials.password = '$CUSTOM_PASSWORD'
                    assert passwordCredentials.username == '$CUSTOM_USERNAME'
                    assert passwordCredentials.password == '$CUSTOM_PASSWORD'
                    assert docker.registryCredentials.username.get() == '$CUSTOM_USERNAME'
                    assert docker.registryCredentials.password.get() == '$CUSTOM_PASSWORD'
                }
            }
        """

        expect:
        build('convert')
    }

    static String registryCredentials() {
        """
            docker {
                registryCredentials {
                    username = '$DEFAULT_USERNAME'
                    password = '$DEFAULT_PASSWORD'
                }
            }
        """
    }
}
