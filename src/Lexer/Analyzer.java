package Lexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by asus1 on 2017/10/26.
 */
public class Analyzer {

    public static final int NUM=256;
    public static final int ID=257;
    public static final int TRUE=258;
    public static final int FALSE=259;

    private final int STATE_INIT=0;
    private final int STATE_NUM=1;
    private final int STATE_STRING=2;
    private final int STATE_NULL=3;
    private List<HashMap<Integer,Integer>> states=new ArrayList<HashMap<Integer,Integer>>();

    Hashtable words=new Hashtable();

    public int getIndex(char c){
        if(Character.isDigit(c)){
            return STATE_NUM;
        }else if(Character.isAlphabetic(c)){
            return STATE_STRING;
        }
        return STATE_NULL;
    }
    public Analyzer(){
        words.put("true", new Word(TRUE, "true"));
        words.put("true", new Word(TRUE, "true"));

        HashMap init_map=new HashMap();
        init_map.put(STATE_NUM, STATE_NUM);
        init_map.put(STATE_STRING, STATE_STRING);
        init_map.put(STATE_NULL, STATE_NULL);
        states.add(init_map);

        HashMap num_map=new HashMap();
        num_map.put(STATE_NUM, STATE_NUM);
        num_map.put(STATE_STRING, STATE_NULL);
        num_map.put(STATE_NULL, STATE_NULL);
        states.add(num_map);

        HashMap string_map=new HashMap();
        string_map.put(STATE_NUM, STATE_NULL);
        string_map.put(STATE_STRING, STATE_STRING);
        string_map.put(STATE_NULL, STATE_NULL);
        states.add(string_map);

        HashMap null_map=new HashMap();
        null_map.put(STATE_NUM, STATE_NULL);
        null_map.put(STATE_STRING,STATE_NULL);
        null_map.put(STATE_NULL, STATE_NULL);
        states.add(null_map);




    }
    public Token scan() throws IOException{
        int currentState=STATE_INIT;
        int lastState=currentState;
        String word="";
        char c=(char) System.in.read();
        while(true){

            lastState=currentState;

            currentState=(int) states.get(currentState)
                    .get(getIndex(c));
            if(currentState==STATE_NULL){
                if(lastState==STATE_NUM){
                    Num num=new Num(NUM, Integer.parseInt(word));
                    return num;
                }else if(lastState==STATE_STRING){
                    Word word3=(Word) words.get(word);
                    if(word3!=null){
                        return word3;
                    }
                    Word word2=new Word( ID,word);
                    return word2;
                }
                return new Token(0);
            }else{
                word+=c;
                c=(char) System.in.read();
            }


        }


    }
    public static void main(String[] args) throws IOException {
        Analyzer lexer=new Analyzer();
        while(true){
            Token t=lexer.scan();
            switch (t.getId()) {
                case Analyzer.NUM:
                    System.out.println("识别到数字:"+((Num) t).getValue());
                    break;
                case Analyzer.ID:
                    System.out.println("识别到字符："+((Word) t).elem);
                    break;
                case Analyzer.TRUE:
                    System.out.println("识别到布尔值："+((Word) t).elem);
                    break;
                case Analyzer.FALSE:
                    System.out.println("识别到布尔值:"+((Word) t).elem);
                    break;

                default:

                    break;
            }

        }
    }
}
