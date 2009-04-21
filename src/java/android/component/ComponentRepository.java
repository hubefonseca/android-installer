package android.component;

import java.util.HashMap;

/**
 *
 * @author Hubert
 */
public class ComponentRepository {

    // Stores the component`s name and path to apk
    private HashMap<String, String> components;
    
    public ComponentRepository() {
        components = new HashMap<String, String>();
        components.put("thesis.mobilis.examples.pingpong.PongComponent", "thesis.apk");
        components.put("thesis.mobilis.examples.pingpong.PingComponent", "thesis.apk");
    }
    
    public String getApkName(String componentName) {
        return components.get("componentName");
    }
    
}
