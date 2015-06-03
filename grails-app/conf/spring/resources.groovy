import grails.plugins.rest.client.RestBuilder

// Place your Spring DSL code here
beans = {

    rest(RestBuilder){ bean->
        bean.constructorArgs = [[connectTimeout: grailsApplication.config.connect.timeout as Integer,
                                 readTimeout: grailsApplication.config.read.timeout as Integer]]

    }

}
