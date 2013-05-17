grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.servlet.version = "3.0"

grails.release.scm.enabled = false
grails.project.repos.default = "myRepo"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
        excludes "grails-plugin-logging"
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        mavenLocal(null)
//        mavenRepo name: 'myRepo'
        grailsRepo "http://grails.org/plugins"
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
        mavenRepo "http://mirrors.ibiblio.org/pub/mirrors/maven2/" // Apache repository
    }

    dependencies {

        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.13'
        runtime 'postgresql:postgresql:9.1-901.jdbc4'
        runtime([group: 'org.hibernate', name: 'hibernate-c3p0', version: '3.6.10.Final'])
        compile([group: 'org.hibernate', name: 'hibernate-entitymanager', version: '3.6.10.Final'])

        runtime('org.apache.lucene:lucene-core:3.6.1')
//        runtime('org.apache.lucene:lucene-xml-query-parser:3.6.1')
//        runtime('org.apache.lucene:lucene-queries:3.6.1')
        runtime 'dom4j:dom4j:1.6.1'
        runtime 'jaxen:jaxen:1.1.4'
        runtime 'commons-httpclient:commons-httpclient:3.1'
    }

    plugins {
        build (":tomcat:$grailsVersion",
               ":hibernate:$grailsVersion",
                ":release:2.2.1"){
            export = false
        }
        compile(':webxml:1.4.1')
        compile(':resources:1.1.6')
        compile(':spring-security-core:1.2.7.3')
    }
}
