import java.util.Arrays;

public class SchemeUtil {
    public String[] primitive = {
            "+", "-", "*", "/","=",">","<","apply"
    };

    public Boolean isPrimitive(String name){
        return Arrays.asList(primitive).contains(name);
    }
}
