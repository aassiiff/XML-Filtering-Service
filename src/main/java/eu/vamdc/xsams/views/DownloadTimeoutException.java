package eu.vamdc.xsams.views;

/**
 *
 * @author Guy Rixon
 */
public class DownloadTimeoutException extends DownloadException {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 4127506841012074443L;

public DownloadTimeoutException(String message) {
    super(message);
  }
  
  public DownloadTimeoutException(String message, Throwable t) {
    super(message, t);
  }
  
}
