import java.util.List;

public class Symbol {
    String name;
    Type type;
    Object value;

    public String toString(){
        if(type.type.equals("int")){
            return String.valueOf((Integer)value);
        }else if(type.type.equals("double")){
            return String.valueOf((Double)value);
        }else if(type.type.equals("true")){
            return "#t";
        }else if(type.type.equals("false")){
            return "#f";
        }else if(type.type.equals("identifier")){
            return value.toString();
        }else if (type.type.equals("procedure")){
            Procedure p = (Procedure)value;
            StringBuffer s = new StringBuffer();
            s.append("procedure : anonymous\n");
            s.append("args: " + p.args.toString());
            return s.toString();
        }else if(type.type.equals("pair")){
            Pair p = (Pair)value;
            return "(" + p.first.toString() + " . " + p.rest.toString() + ")";
        }else if(type.type.equals("list")){
            List<Symbol> list = (List<Symbol>)value;
            return list.toString();
        }else if(type.type.equals("primitive")){
            return "#<primitive>"+name;
        } else{
            return value.toString();
        }
    }
}
