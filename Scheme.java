import java.util.List;
import java.util.Scanner;

public class Scheme {
    private Env global;
    private Token token;
    Parser parser;
    public Scheme(){
        global = new Env();
        global.installPrimitive();
    }

    public static void main(String args[]){

        repl();
    }

    private static void repl() {
        Scheme scheme = new Scheme();
        String text;

        Scanner input = new Scanner(System.in);
        AST ast ;
        Object o ;
        Token token = new Token();
        text = "(define a (lambda (x y) (if #t x y)))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(define result (a 199 2))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(begin (set! a 2) (if #t a))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(define (z x) (if #t x))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(define a (lambda (abc) (abc 5)))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(a z)";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);
        while(true){
            System.out.print("KScheme>");
             text = input.nextLine();
            if(text.equals("exit")){break;}
            token.setText(text);
            scheme.setToken(token);
             ast = scheme.parser.parse();
             o = scheme.eval(ast, scheme.global);
            scheme.display(o);
        }

    }

    private Object eval(AST ast, Env env) {
        if(ast == null) return null;
        if(ast.op.equals("lvalue")){
            return ast.name;
        }

        if(ast.op.equals("indir")){
            return env.lookup(ast.name);
        }
        if(ast.op.equals("const")){
            Symbol constant = ast.value;
            return constant;
        }
        if(ast.op.equals("define")){
            String lvalue = (String)eval(ast.left, env);
            Object rvalue = eval(ast.right, env);
            env.install(lvalue,rvalue);
            return rvalue;
        }
        if(ast.op.equals("if")){
            Symbol cmp = (Symbol) eval(ast.left, env);
            if("#t".equals(cmp.toString())){
                return eval(ast.right.left,env);
            }else{
                return eval(ast.right.right, env);
            }
        }
        if(ast.op.equals("quote")){
            return ast.value;
        }
        if(ast.op.equals("set!")){
            String lvalue = (String)eval(ast.left, env);
            Object rvalue = eval(ast.right, env);
            //需要检查变量是否已经存在，先不管
            env.set(lvalue,rvalue);
            return rvalue;
        }if(ast.op.equals("lambda")){
            return ast.value;
        }
        if(ast.op.equals("begin")){
            List<AST> seq = ast.seq;
            Object o = null;
            for(AST a : seq){
                o = eval(a, env);
            }
            return o;
        }
        if(ast.op.equals("call")){
            Symbol symbol = (Symbol)env.lookup(ast.name);
            Procedure procedure = (Procedure)symbol.value;
            //build env
            Env procedureEnv = new Env();
            procedure.env = procedureEnv;
            procedure.env.parent = env;
            List<Symbol> formArgs = procedure.args;
            List<AST> actualArgs = ast.args;
            int size = formArgs.size();
           for(int index = 0; index < formArgs.size(); index ++){
                env.install_local(formArgs.get(index).name, eval(actualArgs.get(index), env));
           }
           return eval(procedure.body, procedure.env);
        }
        return null;
    }

    public void setToken(Token token) {
        this.token = token;
        parser = new Parser(token);
    }

    public void display(Object o){
        if(o == null)  {
            System.out.println("");
            return;
        }
        Symbol symbol = (Symbol)o;
        System.out.println(symbol);
    }
}
