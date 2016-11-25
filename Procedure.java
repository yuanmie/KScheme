import java.util.List;

public class Procedure {
    public Env env;
     Object returnValue;
     List<Symbol> args;
     List<AST> body;
    public boolean isListArgs;
    public boolean isPairArgs;
}
