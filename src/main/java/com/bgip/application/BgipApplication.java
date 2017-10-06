package com.bgip.application;

import org.glassfish.jersey.server.ResourceConfig;

public class BgipApplication extends ResourceConfig{

	  public BgipApplication() {
		  System.out.println("application class exectuted");
	        
      packages("com.bgip.controller", "com.bgip.filter");
      
	        
	  }
	
}
