package android.serlvet;

import android.component.ComponentRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Hubert
 */
public class InstallApplication extends HttpServlet {

    private static final Logger logger = Logger.getLogger(InstallApplication.class);
    
    private static String pathPackages;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        pathPackages = config.getServletContext().getRealPath("/packages");
    }
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {

            String componentName = request.getParameter("componentName");
            
            ComponentRepository repo = new ComponentRepository();
            String apkName = repo.getApkName(componentName);
          
            logger.info("component: " + componentName);
            logger.info("apk: " + apkName);
            
            if (apkName != null) {
                URL url = new URL("http://localhost/" + apkName);
                URLConnection conn = url.openConnection();

                String fileName = pathPackages + apkName;

                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(fileName);

                int c = is.read();
                while (c > -1) {
                    fos.write(c);
                    c = is.read();
                }

                fos.flush();
                fos.close();

                File adbFile = new File("C:\\java\\android\\android-sdk-windows-1.0_r1\\tools", "adb.exe");
                logger.info("Found adb file: " + adbFile.getAbsolutePath());
                logger.info("Package file: " + fileName);

                try {
                    Process process = Runtime.getRuntime().exec("\"" + adbFile.getAbsolutePath() + "\" install " + "\"" + fileName + "\"");

                    int r = -1;

                    try {
                        r = process.waitFor();
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(InstallApplication.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String result = r + "";
                    out.write(result);

                } catch (IOException e) {
                    logger.info("There was a problem installing the application");
                    e.printStackTrace();
                }
                logger.info("Finished Installation.");

            } else {
                String result = "1";
                out.write(result);
            }
            
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Métodos HttpServlet. Clique no sinal de + à esquerda para editar o código.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
