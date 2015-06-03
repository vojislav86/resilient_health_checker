package healthchecker

import grails.converters.JSON
import grails.converters.XML
import grails.plugins.rest.client.RestBuilder
import grails.rest.RestfulController
import org.springframework.web.client.ResourceAccessException

class HealthCheckController extends RestfulController {

    static responseFormats = ['json', 'xml']
    private static final Map STATUS_SUCCESS_CODES = [200: 'OK', 201: 'CREATED', 202: 'ACCEPTED',
                                                     203: 'NON_AUTHORITATIVE_INFORMATION', 204: 'NO_CONTENT', 205: 'RESET_CONTENT',
                                                     206: 'PARTIAL_CONTENT', 207: 'MULTI_STATUS', 208: 'ALREADY_REPORTED', 226: 'IM_USED'];

    private static final Integer ERROR_CODE = Integer.valueOf(500)

    private static final String HEALTH_CHECKER_CONFIG = 'healthChecker'

    def rest

    def healthStatus() {
        // Check database connection
        String url = grailsApplication.config.check.db.connection
        log.info("Connecting with url: " + url)

        try{
            def infoMsg = message(code: 'health.check.success')
            def jBillingResponse = rest.get(url)
            def status = jBillingResponse.getStatus()
            log.info("URL response status: " + status)
            if(!STATUS_SUCCESS_CODES.containsKey(status)){
                infoMsg = message(code: 'database.connection.not.available.message')
            }
            log.info(infoMsg)
            renderStatusAndMessage(status, infoMsg)
        } catch (Exception e){
            def errorMsg
            if(e.cause instanceof SocketTimeoutException){
                errorMsg = message(code: 'connection.time.out.error.message')
            }
            else if(e.cause instanceof ConnectException){
                errorMsg = message(code: 'connection.refused.error.message')
            }
            else {
                errorMsg = message(code: 'generic.error.message')
            }
            log.info("URL response status: " + ERROR_CODE)
            log.error(errorMsg)
            renderStatusAndMessage(ERROR_CODE, errorMsg)
        }
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
