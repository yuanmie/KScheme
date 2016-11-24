import javafx.beans.binding.ObjectBinding;

import java.util.HashMap;
import java.util.Map;

public class Env {
    private Map<String, Object> environment;
    public Env parent;

    public Env(){
        environment = new HashMap<String, Object>();
    }

    public Object lookup(String identifier){
        if(identifier == null && identifier.equals("")) return null;
        Object object = environment.get(identifier);
        if(object == null && parent != null) return parent.lookup(identifier);
        return object;
    }

    public void install(String identifier, Object object){
       if(parent != null) parent.install(identifier, object);
        else environment.put(identifier, object);
    }

    public void set(String identifier, Object object){
        if(environment.get(identifier) != null)
            environment.put(identifier, object);
        else if(parent != null)
            parent.install(identifier,object);
        else
            throw new RuntimeException("assgin to a not exist value");
    }



    public void installPrimitive() {
        install("a", 1);
    }
}
