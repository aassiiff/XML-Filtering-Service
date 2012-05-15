package uk.ac.cam.ioa.vamdc.consumer.service.filtering.utility;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@SuppressWarnings("unchecked")
public class BeanManagerUtility {

	public BeanManager getBeanManager()
    {
        try{
            InitialContext initialContext = new InitialContext();
            return (BeanManager) initialContext.lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            //log.error("Couldn't get BeanManager through JNDI");
            return null;
        }
    }
	
	@SuppressWarnings("rawtypes")
	//
	public Object getBeanByName(String name) // eg. name=availableCountryDao
    {
        BeanManager bm = getBeanManager();
        Bean bean = bm.getBeans(name).iterator().next();
        
		
		CreationalContext ctx = bm.createCreationalContext(bean); // could be inlined below
        Object o = bm.getReference(bean, bean.getClass(), ctx); // could be inlined with return
        return o;
    }
    
}
