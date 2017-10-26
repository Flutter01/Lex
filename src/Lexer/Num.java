package Lexer;

public class Num extends Token{
    private int value;
    public Num(int id,int value){
        super(id);
        this.value=value;
    }
    public int getValue() {
        return value;
    }

}