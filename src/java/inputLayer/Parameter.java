package inputLayer;

public class Parameter {
    private String parameterName;
    private int parameterValue;
    
    public Parameter() {
        parameterName = "default";
        parameterValue = -1;
    }
    public Parameter(String strName, int intValue) {
        parameterName = strName;
        parameterValue = intValue;
    }
    
    public Parameter(String strName, String strValue) {
        parameterName = strName;
        parameterValue = Integer.parseInt(strValue);
    }
     
    protected static boolean nameIsCorrect(String par) {
        boolean result = false;
        if (!(par.trim().isEmpty()) && par.trim().length() < 256) {
            result = true;
        } 
        return result;
    }
    
    protected static boolean valueIsCorrect(String par) {
        boolean result = true;
        try {
            Integer.parseInt(par);
        } catch (NumberFormatException e) {
            result = false;
        } 
        return result;
    }
    
    protected String getName() {
        return this.parameterName;
    }
    
    protected int getValue() {
        return this.parameterValue;
    }
     
    protected void setName(String par) {
        this.parameterName = par.trim();
   }
       
    protected void setValue(int par) {
        this.parameterValue = par;
    }
    
    protected void setValue(String par) {
        this.parameterValue = Integer.parseInt(par);
    }
    
    protected static int strToInt(String valStr) {
        return Integer.parseInt(valStr);
    }
    
    protected String convertedString(Parameter par) {
        return ("<li>" + par.getName() + " - " + par.getValue() + "</li>\n");
    }
    
    protected static String convertedString(String parameter, Integer value) {
        return ("<li>" + parameter + " - " + value + "</li>");
    }
}

