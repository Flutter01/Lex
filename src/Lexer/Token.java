package Lexer;

/**
 * Created by asus1 on 2017/10/24.
 */
public class Token {
    private String type;
    private String str;
    private String error;

    public Token(String t, String str){
        this.type = t;
        this.str = str;
        this.error = null;
    }

    public Token(String error){
        this.error = error;
    }

    /**构成<string,type>的可输出格式*/
    public String toString(){
        if(this.error != null) {
            return "Error:" + this.error;
        }
        return "<" + this.str + "," + this.type + ">";
    }

    /**获得token类型*/
    public String getType() {
        return type;
    }

    /**获得词法单元*/
    public String getStr() {
        return str;
    }
}
