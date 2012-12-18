<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"
        import="uk.ac.cam.ioa.vamdc.consumer.service.filtering.utility.Locations"%>
        
 <!-- 

<?xml-stylesheet type="text/xsl" href="<%=Locations.getCapabilitiesCssLocation(request) %>" ?>

 -->
<cap:capabilities
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:cap="http://www.ivoa.net/xml/VOSICapabilities/v1.0"
  xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0"
  xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0"
  xmlns:xc="http://www.vamdc.org/xml/XSAMS-consumer/v1.0"
  xsi:schemaLocation="
http://www.ivoa.net/xml/VOSICapabilities/v1.0 http://www.vamdc.org/downloads/xml/VOSI-capabilities-1.0.xsd
http://www.ivoa.net/xml/XSAMS-consumer/v1.0 http://www.ivoa.net/xml/XSAMS-consumer/v1.0
http://www.ivoa.net/xml/VOResource/v1.0 http://www.ivoa.net/xml/VOResource/v1.0
http://www.ivoa.net/xml/VODataService/v1.0 http://www.ivoa.net/xml/VODataService/v1.0">

  <capability standardID="ivo://vamdc/std/XSAMS-consumer" xsi:type="xc:XsamsConsumer">
    <interface xsi:type="vr:WebBrowser">
       <accessURL><%=Locations.getRootLocation(request)%></accessURL>
    </interface>
    <interface xsi:type="vs:ParamHTTP">
      <accessURL><%=Locations.getServiceLocation(request)%></accessURL>
      <resultType>text/html</resultType>
    </interface>
    <versionOfStandards>11.12</versionOfStandards>
    <versionOfSoftware>1.0-SNAPSHOT</versionOfSoftware>
    <numberOfInputs>5</numberOfInputs>
  </capability>
   
  <capability standardID="ivo://ivoa.net/std/VOSI#capabilities">
    <interface xsi:type="vs:ParamHTTP">
      <accessURL><%=Locations.getCapabilitiesLocation(request)%></accessURL>
      <resultType>application/xml</resultType>
    </interface>
  </capability>
        
</cap:capabilities>