import grails.converters.JSON
import grails.converters.XML
import healthchecker.HealthCheck

class BootStrap {

    def init = { servletContext ->

        registerHealthCheckMarshallers()

    }
    def destroy = {
    }

    private registerHealthCheckMarshallers(){

        def final healthCheckerConfig = 'healthChecker'

        JSON.createNamedConfig(healthCheckerConfig){

            it.registerObjectMarshaller(HealthCheck){ healthCheck ->
                [status: healthCheck.status,
                 message: healthCheck.message]
            }
        }

        XML.createNamedConfig(healthCheckerConfig){

            it.registerObjectMarshaller(HealthCheck){ healthCheck, xml ->

                xml.build {
                    status(healthCheck.status)
                    message(healthCheck.message)
                }
            }
        }


    }
}
