package healthchecker

import grails.converters.JSON
import grails.converters.XML
import grails.plugins.rest.client.RestBuilder
import grails.rest.RestfulController

class HealthCheckController extends RestfulController {

    static responseFormats = ['json', 'xml']
    private static final Map STATUS_SUCCESS_CODES = [200: 'OK', 201: 'CREATED', 202: 'ACCEPTED',
                                                     203: 'NON_AUTHORITATIVE_INFORMATION', 204: 'NO_CONTENT', 205: 'RESET_CONTENT',
                                                     206: 'PARTIAL_CONTENT', 207: 'MULTI_STATUS', 208: 'ALREADY_REPORTED', 226: 'IM_USED'];
    private static final String HEALTH_CHECKER_CONFIG = 'healthChecker'

    def healthStatus() {

        RestBuilder restBuilder = new RestBuilder()

        // Check database connection
        String url = grailsApplication.config.check.db.connection
        def jBillingResponse = restBuilder.get(url)
        def status = jBillingResponse.getStatus()
        if(!STATUS_SUCCESS_CODES.containsKey(status)){
            renderStatusAndMessage(status, message(code: 'database.connection.not.available.error.message'))
        }

        renderStatusAndMessage(status, message(code: 'health.check.success'))
    }

    private void renderStatusAndMessage(status, message){
        response.status = status
        withFormat {
            HealthCheck healthCheck = new HealthCheck(status: status, message: message)
            json {
                JSON.use(HEALTH_CHECKER_CONFIG){
                    render healthCheck as JSON
                }
            }
            xml {
                XML.use(HEALTH_CHECKER_CONFIG){
                    render healthCheck as XML
                }
            }
        }
    }
}
