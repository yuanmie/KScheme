import java.util.*;

public class Scheme {
    private Env global;
    private Token token;
    Parser parser;
    public String[] primitive = {
            "+", "-", "*", "/","=","apply"
    };
    public List<String> primitiveList;

    public Scheme(){
        global = new Env();
        global.installPrimitive();
        primitiveList = Arrays.asList(primitive);
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
        text = "(define zz (lambda (x y) (if #t x y)))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(define result (zz 199 2))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(begin (set! zz 2) (if #t zz))";
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

        text = "(define zz (lambda (zzbc) (zzbc 5)))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(zz z)";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(cons 1 2)";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(list 1 2 3 4)";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(define zz (lambda (x ...) (if #t x)))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(+ 1 2 3 4 5)";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(define zz (lambda (x ... . y) (if #f x y)))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(zz 12 34 (list 1 2))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(apply + 1 2 (list 3 44))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(define (xunc xxx) (begin (set! xxx 2) (if #t xxx)))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text = "(define xunc (lambda (xxx) (set! xxx 2) (if #t xxx)))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text =  "(when #t (set! zz 345) (if #t zz))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text =  "(unless #f (set! zz 345666) (if #t zz))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text =  "(cond (#f 1) (#t 2) (else 999))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text =  "(let ((x 134) (y 2) (z 3)) (+ x y z))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text =  "(let* ((x 134) (y x) (z y)) (+ x y z))";
        token.setText(text);
        scheme.setToken(token);
        ast = scheme.parser.parse();
        o = scheme.eval(ast, scheme.global);
        scheme.display(o);

        text =  "(= 1 2)";
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
            Symbol s = (Symbol)env.lookup(ast.name);
            if(s != null) s.name = ast.name;
            return s;
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
        if(ast.op.equals("cond")){
            List<AST> seq = ast.seq;
            Object o = null;
            for(AST a : seq){
                o = eval(a, env);
                if(o != null) return o;
            }
            return o;
        }
        if(ast.op.equals("when")) {
            Symbol cmp = (Symbol) eval(ast.left, env);
            if ("#t".equals(cmp.toString())) {
                return eval(ast.right, env);
            }
        }
        if(ast.op.equals("unless")) {
            Symbol cmp = (Symbol) eval(ast.left, env);
            if ("#f".equals(cmp.toString())) {
                return eval(ast.right, env);
            }
            return null;
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
        if(ast.op.equals("let") || ast.op.equals("let*")){
            String op = ast.op;
            List<AST> seq = ast.seq;
            AST last = seq.remove(seq.size()-1);
            Env e = new Env();
            e.parent = env;
            Object o = null;
            for(AST a : seq){
                String name = a.left.name;
                if(op.equals("let")){
                    o = eval(a.right, env);
                }else if(op.equals("let*")){
                    o = eval(a.right, e);
                }
                e.install_local(name, o);
            }
            o = eval(last, e);
            return o;
        }
        if(ast.op.equals("call")){
            if(primitiveList.contains(ast.name)){
                String pname = ast.name;
                List<AST> actualArgs = ast.args;
                List<Symbol> list = new ArrayList<Symbol>();
                for(AST a : actualArgs){
                    list.add((Symbol)eval(a, env));
                }
                int tmp = 0;
                if(pname.equals("+")){
                    for(int i = 0; i < list.size(); i++){
                        tmp += (Integer)list.get(i).value;
                    }
                }
                else if(pname.equals("-")){
                    tmp = (Integer)list.get(0).value;
                    for(int i = 1; i < list.size(); i++){
                        tmp -= (Integer)list.get(i).value;
                    }
                }
                else if(pname.equals("*")){
                    tmp = 1;
                    for(int i = 0; i < list.size(); i++){
                        tmp *= (Integer)list.get(i).value;
                    }
                }
                else if(pname.equals("/")){
                    tmp = (Integer)list.get(0).value;
                    for(int i = 1; i < list.size(); i++){
                        tmp /= (Integer)list.get(i).value;
                    }
                }else if(pname.equals("=")){
                    boolean result = false;
                    Symbol temp = list.get(0);
                    for(int i = 1; i < list.size(); i++){
                        result = (temp.toString().equals(list.get(i).toString()));
                        if(!result) break;
                    }
                    return ConstantPool.lookup((result ? "#t" : "#f"));
                }
                else if(pname.equals("apply")){
                    Symbol add = (Symbol)eval(actualArgs.remove(0), env);
                    AST last = actualArgs.remove(actualArgs.size()-1);
                    List<AST> lastList = last.seq;
                    actualArgs.addAll(lastList);
                    AST func = new AST();
                    func.op = "call";
                    func.name = (add != null) ?add.name : "+";
                    func.args = actualArgs;
                    Symbol s = (Symbol)eval(func, env);
                    return s;

                }

                Symbol s = new Symbol();
                s.type = new Type("int");
                s.value = tmp;
                return s;
            }else{
                Symbol symbol = (Symbol)env.lookup(ast.name);
                Procedure procedure = (Procedure)symbol.value;
                //build env
                Env procedureEnv = new Env();
                procedure.env = procedureEnv;
                procedure.env.parent = env;
                List<Symbol> formArgs = procedure.args;
                List<AST> actualArgs = ast.args;
                if(procedure.isPairArgs){
                    String argsListName = formArgs.get(0).name;
                    String argsPairName = formArgs.get(1).name; //pair args
                    Symbol s = new Symbol();
                    s.type = new Type("list");
                    List<Symbol> list = new ArrayList<Symbol>();
                    for(AST a : actualArgs){
                        list.add((Symbol)eval(a, env));
                    }
                    Symbol last = list.remove(list.size() - 1);
                    s.value = list;
                    procedure.env.install_local(argsListName, s);
                    procedure.env.install_local(argsPairName, last);
                }else if(procedure.isListArgs && !procedure.isPairArgs){
                    String argsListName = formArgs.get(0).name;
                    Symbol s = new Symbol();
                    s.type = new Type("list");
                    List<Symbol> list = new ArrayList<Symbol>();
                    for(AST a : actualArgs){
                        list.add((Symbol)eval(a, env));
                    }
                    s.value = list;
                    procedure.env.install_local(argsListName, s);
                }else{
                    int size = formArgs.size();
                    for(int index = 0; index < formArgs.size(); index ++){
                        procedure.env.install_local(formArgs.get(index).name, eval(actualArgs.get(index), env));
                    }
                }
                Object result = null;
                for(AST tree : procedure.body) result = eval(tree, procedure.env);
                return result;
            }

        }if(ast.op.equals("cons")){
            Symbol s = new Symbol();
            s.type = new Type("pair");
            Pair pair = new Pair();
            pair.first = eval(ast.left, env);
            pair.rest = eval(ast.right, env);
            s.value = pair;
            return s;
        }if(ast.op.equals("list")){
            Symbol s = new Symbol();
            s.type = new Type("list");
            List<AST> seq = ast.seq;
            List<Symbol> list = new ArrayList<Symbol>();
            for(AST a : seq){
                list.add((Symbol)eval(a, env));
            }
            s.value = list;
            return s;
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
