import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class First {
    public static String[] definition = {"(", "derived"};
    public static String[] expression = {"(", "derived","quote"};
    public static Map<String, List<String>> first = new HashMap<String, List<String>>();
    static{
        first.put("definition", Arrays.asList(definition));
        first.put("expression", Arrays.asList(expression));
    }

    static boolean contains(String nonTerminal, String token){
        return first.get(nonTerminal).contains(token);
    }
}
