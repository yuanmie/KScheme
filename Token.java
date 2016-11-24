
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
            case 'i': if(charArray[currIndex+1] == 'f'){
                currIndex += 2;
                this.type = "if";
                return "if";
            }
                break;
            case '#': if(charArray[currIndex+1] == 'f'){
                currIndex += 2;
                this.type = "false";
                return "#f";
            }else if(charArray[currIndex+1] == 't'){
                currIndex += 2;
                this.type = "true";
                return "#t";
            }
                break;
            case 't':
                if (charArray[currIndex + 1] == 'r' && charArray[currIndex + 2] == 'u' && charArray[currIndex + 3] == 'e') {
                    currIndex += 4;
                    this.type = "true";
                    return "true";
                }

                break;
            case 'f':
                if (charArray[currIndex + 1] == 'a' && charArray[currIndex + 2] == 'l' && charArray[currIndex + 3] == 's' && charArray[currIndex + 4] == 'e') {
                    currIndex += 5;
                    this.type = "false";
                    return "false";
                }
                break;
            case 'b':
                if (charArray[currIndex + 1] == 'e' && charArray[currIndex + 2] == 'g' && charArray[currIndex + 3] == 'i' && charArray[currIndex + 4] == 'n') {
                    currIndex += 5;
                    this.type = "begin";
                    return "begin";
                }
                break;
            case 'q':
                if (charArray[currIndex + 1] == 'u' && charArray[currIndex + 2] == 'o' && charArray[currIndex + 3] == 't' && charArray[currIndex + 4] == 'e') {
                    currIndex += 5;
                    this.type = "quote";
                    return "quote";
                }
                break;
            case 'n':
                if (charArray[currIndex + 1] == 'u' && charArray[currIndex + 2] == 'l' && charArray[currIndex + 3] == 'l') {
                    currIndex += 4;
                    this.type = "null";
                    return "null";
                }
                break;
            case 'd':
                if (charArray[currIndex + 1] == 'e' && charArray[currIndex + 2] == 'f' && charArray[currIndex + 3] == 'i' && charArray[currIndex + 4] == 'n' && charArray[currIndex + 5] == 'e') {
                    currIndex += 6;
                    this.type = "define";
                    return "define";
                }
                break;
            case 'l':
                if (charArray[currIndex + 1] == 'a' && charArray[currIndex + 2] == 'm' && charArray[currIndex + 3] == 'b' && charArray[currIndex + 4] == 'd' && charArray[currIndex + 5] == 'a') {
                    currIndex += 6;
                    this.type = "lambda";
                    return "lambda";
                }
                break;
            case 's':
                if (charArray[currIndex + 1] == 'e' && charArray[currIndex + 2] == 't' && charArray[currIndex + 3] == '!') {
                    currIndex += 4;
                    this.type = "set!";
                    return "set!";
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
                ++currIndex;
                return ch + "";
            //string token
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
                    oldIndex = currIndex++;
                    while(!eof() && (isDigit(charArray[currIndex]) || isAlpha(charArray[currIndex]) || charArray[currIndex] == '_')){
                        currIndex++;
                    }
                    this.type = "identifier";
                    return text.substring(oldIndex,currIndex);
                }
                else {
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
