import java.util.List;


public class AST {
    String op;
    String name;
    Symbol value;
    List<AST> args;
    List<AST> seq;
    AST left;
    AST right;

    public static AST leftValue(String name){
        AST ast = new AST();
        ast.op = "lvalue";
        ast.name = name;
        return ast;
    }

    public static AST constAST(String op, Symbol value){
        AST ast = new AST();
        ast.op = "const";
        ast.value = value;
        return ast;
    }

}
