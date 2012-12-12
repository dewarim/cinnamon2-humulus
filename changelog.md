# Cinnamon-Humulus changelog

##0.7.0

* Removed jtds-jar from libs directory: 
    database drivers should be provided by the application using this plugin
* Moved to new git repository. 

##0.6.9

* Updated Cinnamon base libraries.
* Improved BuildConfig: use maven to pull in dependencies instead of dropping them into the lib folder.
  (with the exception of the new jtds-1.2.6.jar, which seems not to be available otherwise).  

##0.6.8

* Upgrade to Grails 2.1.1

##0.6.7

* updated Cinnamon base libraries.
* Upgrade to Grails 2.0.3

##0.5.57

* New version of entitylib (ZippedFolder included)
* Updated spring-security-core plugin to 1.2.7.2
* Upgrade to Grails 2.0.1

##0.5.53

* New version of Safran lib to fix encoding issue when uploading files.

##0.5.40

* database-config is now read from "${System.env.CINNAMON_HOME_DIR}/database-config.groovy"
  instead of $appName-config.groovy. Please update your Illicium and Dandelion configuration accordingly.

##0.4.7

* Fixed display / i18n-problem with missing login error message
* Fixed problem with HashMaker not catching an illegal argument exception
  (in case someone uses passwords with invalid salt)

##0.4.6

* Updated Spring-Security-Plugin to 1.2.4

##0.4.5

* Updated entitylib.

##0.4.4

* Fixed bug in User.equals().
* Added database connection parameters to Environment.
* Updated Lucene to 3.3.0.

##0.4.3

* Added repositoryService.getObjects().
* Added repositoryService.getFolders().