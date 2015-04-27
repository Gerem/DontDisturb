package com.arkin.webservices.utils;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
@Provider
@ServerInterceptor
public class SecurityInterceptor implements PreProcessInterceptor{
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    private static final ServerResponse ACCESS_DENIED = new ServerResponse("Access denied for this resource", 401, new Headers<Object>());;
    private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Nobody can access this resource", 403, new Headers<Object>());;
    private static final ServerResponse SERVER_ERROR = new ServerResponse("INTERNAL SERVER ERROR", 500, new Headers<Object>());;
     
	@Override
	public ServerResponse preProcess(HttpRequest request, ResourceMethod methodInvoked) throws Failure, WebApplicationException {
		Method method = methodInvoked.getMethod();
		
		//Access allowed for all
        if(method.isAnnotationPresent(PermitAll.class))
            return null;
        
        //Access denied for all
        if(method.isAnnotationPresent(DenyAll.class))        
            return ACCESS_FORBIDDEN;
        
      //Get request headers
        final HttpHeaders headers = request.getHttpHeaders();
         
        //Fetch authorization header
        final List<String> authorization = headers.getRequestHeader(AUTHORIZATION_PROPERTY);
		
      //If no authorization information present; block access
//        if(authorization == null || authorization.isEmpty())        
//            return ACCESS_DENIED;
        
		return null;
	}

}
