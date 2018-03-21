package inputLayer;

import static inputLayer.Parameter.nameIsCorrect;
import static inputLayer.Parameter.strToInt;
import static inputLayer.Parameter.valueIsCorrect;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Registrator extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Registrator</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Registrator at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String newPar = request.getParameter("userParameter");
        String newVal = request.getParameter("userValue");
        String lowerLine = request.getParameter("userLowerLine");
        String upperLine = request.getParameter("userUpperLine");
        String equalName = request.getParameter("userEqualName");
        String equalValue = request.getParameter("userEqualValue");
                
        Parameter tmpParameter = null;
                
        Attribute attribute;
            Object obj = getServletContext()
                .getAttribute("attributes");
        if (obj != null && obj instanceof Attribute) {
            attribute = (Attribute) obj;
        } else {
            attribute = new Attribute();
            getServletContext().setAttribute("attributes", attribute);
        }
        
        if (request.getParameter("button1") != null) {
            if (!nameIsCorrect(newPar)) {
                try (PrintWriter out = response.getWriter()) {
                    out.println(formWebPattern("<h1>parameter name is not valid!</h1>" + formParameterAndValue()));
                }
            } else if (!valueIsCorrect(newVal)) {
                try (PrintWriter out = response.getWriter()) {
                    out.println(formWebPattern("<h1>parameter value is not valid!</h1>" + formParameterAndValue()));
                }
            } else {
                tmpParameter = new Parameter(newPar, newVal);
            }

            attribute.isThisANewParameter(tmpParameter);
            attribute.addParameter(tmpParameter);

            try (PrintWriter out = response.getWriter()) {
                out.println(formWebPattern("<h1>parameter entered correctly</h1>"
                        + "<p>Current parameter: " + tmpParameter.getName() + "</p>"
                        + "<p>Current value: " + tmpParameter.getValue() + "</p>"
                        + "<p>Message: " + attribute.addParameterMsg + "</p>"
                        + getCatalog(attribute)
                        + formParameterAndValue()
                        + formLowerAndUpperValue()
                        + formEqualParameterOrValue()));
            }
        } else if (request.getParameter("button2") != null) {
            try (PrintWriter out = response.getWriter()) {
                out.println(formWebPattern(getListRange(attribute, lowerLine, upperLine)));
            }
        } else if (request.getParameter("button3") != null) {
            try (PrintWriter out = response.getWriter()) {
                out.println(formWebPattern(getListEquality(attribute, equalName, equalValue)));
            }
        }
        
        
    }
    
    public String getListRange(Attribute attr, String valMin, String valMax) {
        if (valMin.trim().isEmpty() && valMax.trim().isEmpty()) {
            return (formWebPattern("<h3>No sample conditions specified</h3>\n")
                    + formParameterAndValue()
                    + formLowerAndUpperValue()
                    + formEqualParameterOrValue());
        } else if (!valueIsCorrect(valMin) || !valueIsCorrect(valMax)) {
            return ("<h3>enter numerical values for the borders</h3>\n"
                    + formParameterAndValue()
                    + formLowerAndUpperValue()
                    + formEqualParameterOrValue());
        } else if (strToInt(valMin) > strToInt(valMax)) {
            return ("<h3>The minimum range is greater than the maximum</h3>\n"
                    + "<p>min value entered: " + valMin + "</p>"
                    + "<p>max value entered: " + valMax + "</p>"
                    + formParameterAndValue()
                    + formLowerAndUpperValue()
                    + formEqualParameterOrValue());
        } else {
            int lower = strToInt(valMin);
            int upper = strToInt(valMax);
            StringBuilder sb = new StringBuilder();
            sb.append("<h2>Parameter list:</h2>\n<ol>\n");
            for (Map.Entry<String, Integer> m : attr.getMap().entrySet()) {
                if (m.getValue() >= lower && m.getValue() <= upper) {
                    sb.append("<li>").append(m.getKey())
                            .append(" --> ").append(m.getValue())
                            .append("</li>\n");
                }

            }
            sb.append("</ol>\n");
            sb.append(formParameterAndValue())
                    .append(formLowerAndUpperValue())
                    .append(formEqualParameterOrValue());
            return sb.toString();
        }
    }
    
    
    public String getListEquality(Attribute attr, String equalName, String equalValue) {
        if (!equalName.trim().isEmpty() || !equalValue.trim().isEmpty()) {
            if (!equalName.trim().isEmpty() && equalValue.trim().isEmpty()) {
                if (!nameIsCorrect(equalName)) {
                    return ("<h3>Error: enter correct name</h3>\n"
                            + formParameterAndValue()
                            + formLowerAndUpperValue()
                            + formEqualParameterOrValue());
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<h2>Equal parameter:</h2>\n");
                    for (Map.Entry<String, Integer> m : attr.getMap().entrySet()) {
                        if (m.getKey().equals(equalName)) {
                            sb.append("<li>").append(m.getKey())
                                    .append(" --> ").append(m.getValue())
                                    .append("</li>\n");
                        }

                    }
                    sb.append(formParameterAndValue())
                            .append(formLowerAndUpperValue())
                            .append(formEqualParameterOrValue());
                    return sb.toString();
                }
            } else if (equalName.trim().isEmpty() && !equalValue.trim().isEmpty()) {
                if (!valueIsCorrect(equalValue)) {
                    return ("<h3>Error: enter correct value</h3>\n"
                            + formParameterAndValue()
                            + formLowerAndUpperValue()
                            + formEqualParameterOrValue());
                } else {
                    int value = strToInt(equalValue);
                    StringBuilder sb = new StringBuilder();
                    sb.append("<h2>Equal parameter:</h2>\n");
                    for (Map.Entry<String, Integer> m : attr.getMap().entrySet()) {
                        if (m.getValue().equals(value)) {
                            sb.append("<li>").append(m.getKey())
                                    .append(" --> ").append(m.getValue())
                                    .append("</li>\n");
                        }

                    }
                    sb.append(formParameterAndValue())
                            .append(formLowerAndUpperValue())
                            .append(formEqualParameterOrValue());
                    return sb.toString();
                }
            } else if (!equalName.trim().isEmpty() && !equalValue.trim().isEmpty()) {
                if (!nameIsCorrect(equalName) || !valueIsCorrect(equalValue)) {
                    return ("<h3>Error: enter correct name/value</h3>\n"
                            + formParameterAndValue()
                            + formLowerAndUpperValue()
                            + formEqualParameterOrValue());
                } else {
                    String name = equalName;
                    int value = strToInt(equalValue);
                    StringBuilder sb = new StringBuilder();
                    sb.append("<h2>Equal parameter:</h2>\n");
                    for (Map.Entry<String, Integer> m : attr.getMap().entrySet()) {
                        if (m.getKey().equals(name) && m.getValue().equals(value)) {
                            sb.append("<li>").append(m.getKey())
                                    .append(" --> ").append(m.getValue())
                                    .append("</li>\n");
                        }

                    }
                    sb.append(formParameterAndValue())
                            .append(formLowerAndUpperValue())
                            .append(formEqualParameterOrValue());
                    return sb.toString();
                }
            }
        } else {

            return (formWebPattern("<h3>No sample conditions specified</h3>\n")
                    + formParameterAndValue()
                    + formLowerAndUpperValue()
                    + formEqualParameterOrValue());
        }
        return "Error";
    }
    
    private String formParameterAndValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("<form action=\"Registrator\" method=\"POST\">\n")
        .append("<h2>Enter the parameter</h2>")
        .append("<p>Parameter name:<input type=\"text\" name='userParameter' size='24'/>\n")
        .append("<p>Parameter value:<input type=\"text\" name='userValue' size='24'/>\n")     
        .append("<br>\n<input type='submit' name ='button1' value='button1'/>\n")
        .append("</p></form>\n");
      
        return sb.toString();
    }
    
    private String formLowerAndUpperValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("<form action=\"Registrator\" method=\"POST\">\n")
        .append("<h2>enter a range of values (optional)</h2>") 
        .append("<p>Min value:<input type=\"text\" name='userLowerLine' size='24'/>\n")
        .append("<p>Max value:<input type=\"text\" name='userUpperLine' size='24'/>\n")        
        .append("<br>\n<input type='submit' name ='button2' value='button2'/>\n")
        .append("</p></form>\n");
      
        return sb.toString();
    }
    
    private String formEqualParameterOrValue() {
        StringBuilder sb = new StringBuilder();
        sb.append("<form action=\"Registrator\" method=\"POST\">\n")
        .append("<h2>checking the equality of a name or value (optional)</h2>")
        .append("<p>Parameter name:<input type=\"text\" name='userEqualName' size='24'/>OR/AND\n")
        .append("<p>Parameter value:<input type=\"text\" name='userEqualValue' size='24'/>\n")     
        .append("<br>\n<input type='submit' name ='button3' value='button3'/>\n")
        .append("</p></form>\n");
      
        return sb.toString();
    }
    
    private String formWebPattern(String webContent) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>")
                .append("<html>")
                .append("<head>")
                .append("<title>Servlet</title>")
                .append("</head>")
                .append("<body>")
                .append(webContent)
                .append("</body>")
                .append("</html>");
        return sb.toString();
    }
    
    private String getCatalog(Attribute attr) {
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Parameter list:</h2>\n<ol>\n");
        for (Map.Entry<String, Integer> m : attr.getMap().entrySet()) {
            sb.append("<li>").append(m.getKey())
                    .append(" --> ").append(m.getValue())
                    .append("</li>\n");
        }
        sb.append("</ol>\n");
        return sb.toString();
    }  
    
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}