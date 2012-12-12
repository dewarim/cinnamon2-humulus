import org.apache.log4j.PropertyConfigurator

class BootStrap {

    def init = { servletContext ->
        log.debug("Init BootStrap")
        String logFilename = System.env.CINNAMON_HOME_DIR + "/humulus.log4j.properties"
        if (new File(logFilename).exists()) {
            PropertyConfigurator.configure(logFilename);
        }
    }

    def destroy = {

    }
}