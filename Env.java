import java.util.HashMap;
import java.util.Map;

public class Env extends SchemeUtil{
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

    public void install_local(String identifier, Object object){
        environment.put(identifier, object);
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
        for(String name : primitive){
            Symbol s = new Symbol();
            s.type = new Type("primitive");
            s.name = name;
            Procedure p = new Procedure();
            p.isListArgs = true;
            p.isPairArgs = false;
            s.value = p;
            install(name, s);
        }
    }
}
