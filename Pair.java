
public class Pair {
    private Object first;
    private Object rest;

    public Pair(){

    }

    public Pair(Object first, Object rest){
        this.first = first;
        this.first = rest;
    }

    public Object car(Pair p){
        return p.first;
    }

    public Object cdr(Pair p){
        return p.rest;
    }
}
