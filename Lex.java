import java.io.*;

public class Lex {
    Reader in;
    private String text;
    private int index;
    private char[] charArray;
    private int text_length;

    public Lex(FileInputStream in){
        this.in = new InputStreamReader(in);
    }

    public Lex(Reader in){
        this.in = in;
    }

    public Lex(String text){
        this.text = text;
        this.index = 0;
        this.charArray = text.toCharArray();
        this.text_length = text.length();
    }

    public boolean eof(){
        return text == null || (index >= text_length);
    }


/*    public String nextToken() throws IOException {
        if(!in.ready()) return null;
        int ch;
        while((ch = in.read()) == ' ')
            ;

        switch(ch){
            case 'd'

        }
    }*/

}
