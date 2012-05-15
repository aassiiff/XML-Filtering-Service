package uk.ac.cam.ioa.vamdc.consumer.service.filtering.interceptors;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import uk.ac.cam.ioa.vamdc.consumer.service.filtering.qualifier.UploadInterceptor;


@UploadInterceptor
@Interceptor
public class FileUploadInterceptorImp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileUploadInterceptorImp() {
		
	}
	
	@AroundInvoke
    public Object logMethodEntry(InvocationContext invocationContext)
            throws Exception {
        System.out.println("FileUploadInterceptorImp: Entering method: "
                + invocationContext.getMethod().getName() + " in class "
                + invocationContext.getMethod().getDeclaringClass().getName());

        return invocationContext.proceed();
    }
	
	

}
