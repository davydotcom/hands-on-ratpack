import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static ratpack.groovy.Groovy.ratpack
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import asset.pipeline.ratpack.AssetPipelineModule
import asset.pipeline.ratpack.AssetPipelineHandler

final Logger log = LoggerFactory.getLogger(Ratpack)

ratpack {
	bindings {
		module(new AssetPipelineModule())	    
	}
    handlers {
    	all() { ctx ->
    		log.info "${ctx.request.method.name} /${ctx.request.path}"
    		ctx.next()
    	}
    	prefix("user") {
    		prefix(":username") {
    			all() { ctx ->
    				String username = ctx.getAllPathTokens().get("username")
    				if(username.startsWith('f')) {
    					log.warn("Warning, request for $username")
    				}
    				ctx.next()
    			}
    			get("tweets") { ctx ->
		    		ctx.render "user/${ctx.getAllPathTokens().get("username")}/tweets"
		    	}

		    	get("friends") { ctx ->
		    		ctx.render "user/${ctx.getAllPathTokens().get("username")}/friends"
		    	}
		    	
		    	all() { ctx ->
		    		ctx.render "user/${ctx.getAllPathTokens().get("username")}"
		    	}
    		}

    		all() {
    			byMethod {
	    			get {
		    			render "user"		
	    			}
	    			post {
	    				render "user"
	    			}
	    		}
    		}
    	}

    	post("api/ws") { ctx ->
    		String soapAction = request.headers.get('SOAPAction')
    		ctx.render "api/ws - $soapAction"
    	}

    	get() { ctx ->
    		ctx.render "Hello Greach!"
    	}

    	all(AssetPipelineHandler)
	
    }
}
