import java.util.HashMap;
import java.util.Map;


public class ConstantPool {
    public static Map<String, Symbol> constantPool = new HashMap<String, Symbol>();

    public static Symbol lookup(String key){
        if(key == null) return null;
        return constantPool.get(key);
    }

    public static void install(String key, Symbol object){
        constantPool.put(key, object);
    }
}
