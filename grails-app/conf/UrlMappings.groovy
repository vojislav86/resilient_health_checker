class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')

        /**
         * REST url mappings
         */

        "/health/check" (controller: 'healthCheck', parseRequest: true){
            action = [GET: 'healthStatus']
        }
	}
}
