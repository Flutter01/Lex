package Lexer;

/**
 * Created by asus1 on 2017/10/24.
 */
/*public class Token {
    private int code;
    private String str;
    private String error;

    public Token(int c,String s){
        this.code = c;
        this.str = s;
        this.error = null;
    }

    public Token(String error){
        this.error = error;
    }

    public String toString(){
        if(this.error != null)
            return "Error:" + this.error;
        return "<" + this.code + "," + this.str + ">";
    }
}*/
public class Token {
    private int id;
    public Token(int id){
        this.id=id;
    }
    public int getId() {
        return id;
    }


}