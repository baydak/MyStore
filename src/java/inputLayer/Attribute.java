package inputLayer;

import java.util.HashMap;
import java.util.Map;

public class Attribute {
    private Map<String, Integer> map;
    String addParameterMsg;
            
    public Attribute() {
        map = new HashMap<>();
    }
    
    public Map<String, Integer> getMap() {
        return this.map;
    }
    
    public boolean isThisANewParameter(Parameter tmpPar) {
        if (map.containsKey(tmpPar.getName())) {
            addParameterMsg = "modify an existing parameter";
            return false;
        } else {
            addParameterMsg = "adding a new parameter";
            return true;
        }    
    }

    public void addParameter(Parameter tmpPar) {
            map.put(tmpPar.getName(), tmpPar.getValue());
    }
}
    

