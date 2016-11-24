import java.util.ArrayList;
import java.util.List;

public class Parser {
    public Token token;

    public Parser(Token token){
        this.token = token;
    }


    public AST parse() {
        AST ast = null;
        while(!token.eof()){
            ast = parse_form();
        }
        return ast;
    }

    private AST parse_form() {
        AST ast = null;
        String t = token.peekToken();
        if(First.contains("expression", t) && First.contains("definition", t)){
                token.nextToken();
                t = token.peekToken();
            if(t.equals("define")){
                ast = parse_define();
            }else{
                ast = parse_expression();
            }
        }
        else if(First.contains("definition", t)){
            ast = parse_define();
        }else{
            ast = parse_expression();
        }
        return ast;
    }

    private AST parse_expression() {
        AST ast = new AST();
        //only process variable
        String t = token.nextToken();
        boolean isProcedure = false;
        if(t.equals("(")) {t = token.nextToken(); isProcedure = true;};


        if(token.type.equals("identifier") && !isProcedure){
            ast.op = "indir";
            ast.name = t;
        }
        else if(token.type.equals("String")){

        }else if(token.type.equals("number")){
            Symbol symbol = ConstantPool.lookup(t);
            if(symbol == null){
                Type type = new Type();
                symbol = new Symbol();
                if(token.numberType.equals("int")){
                    type.type = "int";
                    symbol.type = type;
                    symbol.value = Integer.parseInt(t);
                    ConstantPool.install(t, symbol);
                }else if(token.numberType.equals("double")){
                    type.type = "double";
                    symbol.type = type;
                    symbol.value = Double.parseDouble(t);
                    ConstantPool.install(t, symbol);
                }
            }

            ast = AST.constAST("const", symbol);
        }else if(token.type.equals("if")){
            ast.op = "if";
            ast.left = parse_expression();
            ast.right = new AST();
            ast.right.op = "cmp";
            ast.right.left = parse_expression();

            if(!token.peekToken().equals(")")){
                ast.right.right = parse_expression();
                expect(")");
            }else{
                expect(")");
            }
        }else if(token.type.equals("true")){
            Type type = new Type();
            Symbol symbol = new Symbol();
            type.type = "true";
            symbol.type = type;
            symbol.value = Boolean.TRUE;
            ConstantPool.install(t, symbol);
            ast = AST.constAST("const", symbol);
        }else if(token.type.equals("false")){
            Type type = new Type();
            Symbol symbol = new Symbol();
            type.type = "false";
            symbol.type = type;
            symbol.value = Boolean.FALSE;
            ConstantPool.install(t, symbol);
            ast = AST.constAST("const", symbol);
        }else if(token.type.equals("quote")){
            ast.op = "quote";
            ast.value = parse_datum();
            expect(")");
        }else if(token.type.equals("set!")){
            ast.op = "set!";
            ast.left = AST.leftValue(token.nextToken());
            ast.right = parse_expression();
            expect(")");
        }else if(t.equals("lambda")){
            Symbol s = new Symbol();
            Type type = new Type("procedure");
            s.type = type;
            ast.op = "lambda";
            Procedure p = new Procedure();
            p.args = parse_formal();
            p.body = parse_body();
            s.value = p;
            ast.value = s;
            expect(")");
        }else if(t.equals("begin")){
            ast.op = "begin";
            List<AST> seq = new ArrayList<AST>();
            while(!peekExpect(")")){
                seq.add(parse_expression());
            }
            ast.seq = seq;
        }
        else{
            ast.op = "call";
            String procedure = t;
            List<AST> actualArgs = new ArrayList<AST>();
            while(!peekExpect(")")){
                actualArgs.add(parse_expression());
            }
            ast.name =procedure;
            ast.args = actualArgs;
        }
        return ast;
    }

    private AST parse_body() {
        return parse_expression();
    }

    private List<Symbol> parse_formal() {
        List<Symbol> args = new ArrayList<Symbol>();
        expect("(");
        while(!peekExpect(")")){
            args.add(parse_variable());
        }
        return args;
    }

    private Symbol parse_variable() {
        Symbol s = new Symbol();
        String t = token.nextToken();
        Type type = new Type();
        type.type = token.type;
        s.name = t;
        s.value = t;
        s.type = type;
        return s;
    }

    private Symbol parse_datum() {
        Symbol symbol = new Symbol();
        Type type = new Type();
        String t = token.nextToken();
        if(token.type.equals("identifier")){
            symbol.value = t;
            type.type = "identifier";
            symbol.type = type;

        }
        return symbol;
    }

    public AST parse_define(){
        String t = token.nextToken();
        AST ast = new AST();
        if(t.equals("define")){
            ast.op = "define";
            ast.left = AST.leftValue(token.nextToken());
            ast.right = parse_expression();
            expect(")");
        }
        return ast;
    }

    private boolean expect(String s) {
        if(!s.equals(token.nextToken())){
            throw new RuntimeException("expect " + s);
        }
        return true;
    }

    private boolean peekExpect(String s){
        String t = token.peekToken();
        if(t.equals(s)) {
            token.nextToken();
            return true;
        }else {
            return false;
        }
    }



}
