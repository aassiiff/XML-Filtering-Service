package uk.ac.cam.ioa.vamdc.consumer.service.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ViewServlet
 */
@WebServlet("/viewServlet")
public class ViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
    private String filePath = "/opt/jboss/VAMDCData/csv/";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        //PrintWriter out = response.getWriter();
        try {
            System.out.println("doGet(HttpServletRequest request, HttpServletResponse response)");
            // Get requested file by path info.
            String requestedFile = request.getPathInfo();
            requestedFile = request.getParameter("fileName");

            System.out.println("requestedFile: " + requestedFile);

            // Check if file is actually supplied to the request URI.
            if (requestedFile == null) {
                // Do your thing if the file is not supplied to the request URI.
                // Throw an exception, or send 404, or show default/warning page, or just ignore it.
                response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                return;
            }
            // Decode the file name (might contain spaces and on) and prepare file object.
            File file = new File(filePath, URLDecoder.decode(requestedFile, "UTF-8"));

            System.out.println("file: " + file.getAbsolutePath());

            // Check if file actually exists in filesystem.
            if (!file.exists()) {
                System.out.println("file not exists: " + file.getAbsolutePath());
                // Do your thing if the file appears to be non-existing.
                // Throw an exception, or send 404, or show default/warning page, or just ignore it.
                response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
                return;
            }
            // Get content type by filename.
            String contentType = "text/html";

            // Init servlet response.
            response.reset();
            response.setBufferSize(DEFAULT_BUFFER_SIZE);
            response.setContentType(contentType);
            response.setHeader("Content-Length", String.valueOf(file.length()));
            
            // This was used for CSV as sent as an attachment
            //response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            // Prepare streams.
            BufferedInputStream input = null;
            BufferedOutputStream output = null;

            try {
                // Open streams.
                input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
                output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

                // Write file contents to response.
                byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
                int length;
                while ((length = input.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Gently close streams.
                close(output);
                close(input);
            }
        } finally {
            //out.close();
        }
    }
	
	private static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                // Do your thing with the exception. Print it, log it or mail it.
                e.printStackTrace();
            }
        }
    }

}
