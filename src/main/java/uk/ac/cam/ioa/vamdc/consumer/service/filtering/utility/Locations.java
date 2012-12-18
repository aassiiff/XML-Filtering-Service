package uk.ac.cam.ioa.vamdc.consumer.service.filtering.utility;

import javax.servlet.http.HttpServletRequest;

/**
*
* @author Guy Rixon
*/
public class Locations {
  
	/*
  public static String getRootLocation(HttpServletRequest request) {
    return String.format("http://%s:%d%s/",
                         request.getServerName(),
                         request.getLocalPort(),
                         request.getContextPath());
  }
  
  /**/
   public static String getRootLocation(HttpServletRequest request) {
    return String.format("http://%s:%d%s/",
                         "casx019-zone2.ast.cam.ac.uk",
                         80,
                         request.getContextPath());
  }
   
  
  public static String getServiceLocation(HttpServletRequest request) {
    return getRootLocation(request) + "service";
  }
  

  
  public static String getCapabilitiesLocation(HttpServletRequest request) {
    return getRootLocation(request) + "capabilities";
  }
  
  public static String getCapabilitiesCssLocation(HttpServletRequest request) {
    return getRootLocation(request) + "/Capabilities.xsl";
  }
  
}


