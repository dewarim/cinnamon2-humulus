import eu.hornerproject.humulus.RepositoryLoginFilter
import eu.hornerproject.humulus.CinnamonPasswordEncoder
import eu.hornerproject.humulus.CinnamonUserDetailsService
import eu.hornerproject.humulus.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.codehaus.groovy.grails.plugins.springsecurity.SecurityFilterPosition
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import eu.hornerproject.humulus.SwitchableDataSource2

class HumulusGrailsPlugin {

    def version = "0.7.3"
    def groupId = 'cinnamon2'
    def grailsVersion = "2.0.0 > *"
    def dependsOn = [springSecurityCore: '1.2.7.2 >*']
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Ingo Wiarda @ Horner GmbH"
    def authorEmail = "ingo.wiarda@texolution.eu"
    def title = "Humulus: essential things for Cinnamon applications"
    def description = '''\\
Essential classes and libraries you need to create a Grails based Cinnamon application.
This includes the modified security plugin for a switchable datasource as well as the Cinnamon base libraries:
CinnamonBase, EntityLib, Safran.mini, CinnamonUtils.
'''

    // URL to the plugin's documentation
    def documentation = "doc/humulus"

    def doWithWebDescriptor = { xml ->
        // CanDo Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        parentDataSource(DriverManagerDataSource) { bean ->
            bean.'abstract' = true;
            username = "sa"
        }

        Environment.list().each { env ->

            "${env.prefix}DataSource"(DriverManagerDataSource) { bean ->
                bean.parent = parentDataSource
                bean.scope = 'prototype'
                def port = env.port
                if (env.jdbcType.equals('hsqldb') && env.dbname.equals('mem')) {
                    // hsqldb does not use a host:port for its in-memory database.
                    url = "jdbc:hsqldb:mem:${env.dbname}"
                }
                else {
                    url = env.dbConnectionUrl
                }
                log.debug("url = '$url'")
                driverClassName = env.driverClassName
                if (env.username) {
                    username = env.username
                }
                if (env.password) {
                    password = env.password
                }
            }
        }

        def dataSources = [:]
        Environment.list().each {env ->
            dataSources[env.id] = ref(env.prefix + 'DataSource')
        }

        dataSource(SwitchableDataSource2) {
            targetDataSources = dataSources
        }

        userDetailsService(CinnamonUserDetailsService) {
            // looks like this service is not injected automatically:
            repositoryService = ref('repositoryService')
        }

        passwordEncoder(CinnamonPasswordEncoder)

        authenticationProvider(org.springframework.security.authentication.dao.DaoAuthenticationProvider) {
            userDetailsService = ref('userDetailsService')
        }

        authenticationManager(org.springframework.security.authentication.ProviderManager) {
            providers = ref('authenticationProvider')
        }

        repositoryLoginFilter(RepositoryLoginFilter) {
            authenticationManager = ref('authenticationManager')
        }

    }

    def doWithDynamicMethods = { ctx ->
        // CanDo: Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->

        SpringSecurityUtils.clientRegisterFilter(
                'repositoryLoginFilter', SecurityFilterPosition.PRE_AUTH_FILTER.getOrder() + 11)

    }

    def onChange = { event ->
        // CanDo: Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // CanDo: Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
