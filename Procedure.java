import java.util.List;

public class Procedure {
    public Env env;
     Object returnValue;
     List<Symbol> args;
     AST body;
    public boolean isListArgs;
    public boolean isPairArgs;
}
