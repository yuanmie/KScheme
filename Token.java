
public class Token {
    private String text;
     String type;
     String numberType;
    private int currIndex = 0;
    private int text_length;
    private char[] charArray;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.currIndex = 0;
        this.text_length = text.length();
        this.charArray = text.toCharArray();
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCurrIndex() {
        return currIndex;
    }

    public void setCurrIndex(int currIndex) {
        this.currIndex = currIndex;
    }

    public int getText_length() {
        return text_length;
    }

    public void setText_length(int text_length) {
        this.text_length = text_length;
    }

    public Token() {
        this.currIndex = 0;
        this.text_length = 0;
    }

    public Token(String text) {
        this.text = text;
        this.text_length = text.length();
        this.currIndex = 0;
        this.charArray = text.toCharArray();
    }

    public String nextToken() {
        if (eof()) return "";
        while(charArray[currIndex] == ' ') ++currIndex;
        char ch = charArray[currIndex];
        String sign = "";
        int oldIndex = 0;
        switch (ch) {
            case '#': if(charArray[currIndex+1] == 'f'){
                currIndex += 2;
                this.type = "false";
                return "#f";
            }else if(charArray[currIndex+1] == 't'){
                currIndex += 2;
                this.type = "true";
                return "#t";
            }else if(charArray[currIndex+1] == '\\' && isAlpha(charArray[currIndex+2])){
                currIndex += 3;
                this.type = "char";
                return "'" + charArray[currIndex-1] + "'";
             }
                break;
            case '{':
                this.type = "object";
                ++currIndex;
                return ch + "";
            case '}':
            case ',':
            case ':':
            case '(':case')':
            case '[':
                this.type = "array";
            case ']':
            case '+':
            case '/':
            case '*':
            case '=':
                this.type = "identifier";
                ++currIndex;
                return ch + "";
            //string token
            case '.':if(charArray[currIndex+1] == '.' && charArray[currIndex+2] == '.') {
                    currIndex += 3;
                    return "...";
                }else{
                    ++currIndex;
                    return ".";
            }
            case '"':
                oldIndex = currIndex;
                while (!eof() && charArray[++currIndex] != '"') {
                    ;
                }
                ++currIndex;
                this.type = "string";
                return text.substring(oldIndex, currIndex);

            case '-':
                sign = "-";
                ++currIndex;
            default:
                oldIndex = currIndex;
                ch = charArray[currIndex];
                if (ch >= '0' && ch <= '9') {
                    this.type = "number";
                    this.numberType = "int";
                    currIndex++;
                    if (ch == '0') {

                    } else {
                        while (!eof() && isDigit(charArray[currIndex])) {
                            currIndex++;
                        }
                    }
                    if (!eof() && charArray[currIndex] == '.') {
                        this.numberType = "double";
                        ++currIndex;
                        while (!eof() && isDigit(charArray[currIndex])) {
                            currIndex++;
                        }

                        if (!eof() && (charArray[currIndex] == 'e' || charArray[currIndex] == 'E')) {
                            ++currIndex;
                            if (!eof() && (charArray[currIndex] == '+' || charArray[currIndex] == '-')) {
                                ++currIndex;
                            }
                            while (!eof() && isDigit(charArray[currIndex])) {
                                currIndex++;
                            }
                        }
                    }
                    return sign + text.substring(oldIndex, currIndex);
                } else if(ch == '_' || isAlpha(ch)){
                    switch (ch){
                        case 'i': if(charArray[currIndex+1] == 'f'){
                            currIndex += 2;
                            this.type = "if";
                            return "if";
                        }
                        case 't':
                            if (charArray[currIndex + 1] == 'r' && charArray[currIndex + 2] == 'u' && charArray[currIndex + 3] == 'e') {
                                currIndex += 4;
                                this.type = "true";
                                return "true";
                            }

                        case 'c':
                            if (charArray[currIndex + 1] == 'o' && charArray[currIndex + 2] == 'n' && charArray[currIndex + 3] == 's') {
                                currIndex += 4;
                                this.type = "cons";
                                return "cons";
                            }else  if (charArray[currIndex + 1] == 'o' && charArray[currIndex + 2] == 'n' && charArray[currIndex + 3] == 'd') {
                                currIndex += 4;
                                this.type = "cond";
                                return "cond";
                            }

                        case 'w':
                            if (charArray[currIndex + 1] == 'h' && charArray[currIndex + 2] == 'e' && charArray[currIndex + 3] == 'n') {
                                currIndex += 4;
                                this.type = "when";
                                return "when";
                            }

                        case 'u':
                            if (charArray[currIndex + 1] == 'n' && charArray[currIndex + 2] == 'l' && charArray[currIndex + 3] == 'e'
                                    && charArray[currIndex+4] == 's' && charArray[currIndex+5] == 's') {
                                currIndex += 6;
                                this.type = "unless";
                                return "unless";
                            }

                        case 'f':
                            if (charArray[currIndex + 1] == 'a' && charArray[currIndex + 2] == 'l' && charArray[currIndex + 3] == 's' && charArray[currIndex + 4] == 'e') {
                                currIndex += 5;
                                this.type = "false";
                                return "false";
                            }
                        case 'a':
                            if (currIndex + 4 < text_length && charArray[currIndex + 1] == 'p' && charArray[currIndex + 2] == 'p' && charArray[currIndex + 3] == 'l' && charArray[currIndex + 4] == 'y') {
                                currIndex += 5;
                                this.type = "apply";
                                return "apply";
                            }
                        case 'b':
                            if (currIndex + 4 < text_length && charArray[currIndex + 1] == 'e' && charArray[currIndex + 2] == 'g' && charArray[currIndex + 3] == 'i' && charArray[currIndex + 4] == 'n') {
                                currIndex += 5;
                                this.type = "begin";
                                return "begin";
                            }
                        case 'q':
                            if (currIndex + 4 < text_length && charArray[currIndex + 1] == 'u' && charArray[currIndex + 2] == 'o' && charArray[currIndex + 3] == 't' && charArray[currIndex + 4] == 'e') {
                                currIndex += 5;
                                this.type = "quote";
                                return "quote";
                            }
                        case 'n':
                            if (currIndex + 3 < text_length && charArray[currIndex + 1] == 'u' && charArray[currIndex + 2] == 'l' && charArray[currIndex + 3] == 'l') {
                                currIndex += 4;
                                this.type = "null";
                                return "null";
                            }
                        case 'd':
                            if(currIndex + 1 < text_length && charArray[currIndex + 1] == 'o') {
                                currIndex += 2;
                                this.type = "do";
                                return "do";
                            }
                            else if (currIndex + 5 < text_length && charArray[currIndex + 1] == 'e' && charArray[currIndex + 2] == 'f' && charArray[currIndex + 3] == 'i' && charArray[currIndex + 4] == 'n' && charArray[currIndex + 5] == 'e') {
                                currIndex += 6;
                                this.type = "define";
                                return "define";
                            }
                        case 'l':
                            if (currIndex + 5 < text_length && charArray[currIndex + 1] == 'a' && charArray[currIndex + 2] == 'm' && charArray[currIndex + 3] == 'b' && charArray[currIndex + 4] == 'd' && charArray[currIndex + 5] == 'a') {
                                currIndex += 6;
                                this.type = "lambda";
                                return "lambda";
                            }else if (currIndex + 3 < text_length && charArray[currIndex + 1] == 'i' && charArray[currIndex + 2] == 's' && charArray[currIndex + 3] == 't') {
                                currIndex += 4;
                                this.type = "list";
                                return "list";
                            }else if (currIndex + 5 < text_length && charArray[currIndex + 1] == 'e' && charArray[currIndex + 2] == 't' && charArray[currIndex + 3] == 'r' && charArray[currIndex + 4] == 'e' && charArray[currIndex + 5] == 'c') {
                                currIndex += 6;
                                this.type = "letrec";
                                return "letrec";
                            }
                            else if(currIndex + 3 < text_length && charArray[currIndex + 1] == 'e' && charArray[currIndex + 2] == 't' && charArray[currIndex + 3] == '*'){
                                currIndex += 4;
                                this.type = "let*";
                                return "let*";
                            }else if(currIndex + 2 < text_length && charArray[currIndex + 1] == 'e' && charArray[currIndex + 2] == 't'){
                                currIndex += 3;
                                this.type = "let";
                                return "let";
                            }
                        case 's':
                            if (currIndex + 3 < text_length && charArray[currIndex + 1] == 'e' && charArray[currIndex + 2] == 't' && charArray[currIndex + 3] == '!') {
                                currIndex += 4;
                                this.type = "set!";
                                return "set!";
                            }
                    }
                    oldIndex = currIndex++;
                    while(!eof() && (isDigit(charArray[currIndex]) || isAlpha(charArray[currIndex]) || isExpandChar(charArray[currIndex]))){
                        currIndex++;
                    }
                    this.type = "identifier";
                    return text.substring(oldIndex,currIndex);
                }
                else {
                    if(sign.equals("-")){
                        ++currIndex;
                        sign = "";
                        return "-";

                    }
                    throw new RuntimeException("lex error!");
                }

        }
        return "";
    }

    public Boolean eof() {
        return this.currIndex >= this.text_length;
    }

    public boolean isDigit(int c) {
        return c >= '0' && c <= '9';
    }
    public boolean isAlpha(int c) { return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');}
    public boolean isExpandChar(int c){
        String expandChar = "!$%&*+-./:<=>?@^_~";
        return expandChar.indexOf(c) >= 0;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }
    
    public String peekToken(){
    	int oldIndex = currIndex;
    	String result = nextToken();
    	currIndex = oldIndex;
    	return result;
    }
}
