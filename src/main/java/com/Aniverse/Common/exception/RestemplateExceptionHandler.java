package com.Aniverse.Common.exception;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class RestemplateExceptionHandler implements ResponseErrorHandler {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		// TODO Auto-generated method stub
		logger.info("exception test : " + response.getStatusCode()+ " : " + response.getBody());
		return false;
	}

	@Override
	public void handleError(ClientHttpResponse response) throws RuntimeException {
		// TODO Auto-generated method stub
		
	}

}
