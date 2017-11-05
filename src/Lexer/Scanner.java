package Lexer;

import Transation.*;

import java.io.*;
import java.util.*;

/**
 * Created by asus1 on 2017/11/5.
 */
public class Scanner {
    private static ArrayList<Character> input = new ArrayList<>();// 存储输入的字符数组
    static ArrayList<Token> tokens = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        RETransation transation = new RETransation();
        Map<String, List<String>> reMap = readREs();
        List<DFA> NUMList = new ArrayList<>();
        List<DFA> KEYList = new ArrayList<>();
        List<DFA> WORDList = new ArrayList<>();
        List<DFA> OPERATORList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : reMap.entrySet()) {
            if(entry.getKey().endsWith("NUM")) {
                for(String s:entry.getValue()) {
                    NUMList.add(transation.REtoDFA0(s));
                }
            } else if (entry.getKey().endsWith("KEYWORD")){
                for(String s:entry.getValue()) {
                    KEYList.add(transation.REtoDFA0(s));
                }
            } else if (entry.getKey().endsWith("WORD")) {
                for(String s:entry.getValue()) {
                    WORDList.add(transation.REtoDFA0(s));
                }
            } else if (entry.getKey().equals("OPERATOR")) {
                for(String s:entry.getValue()) {
                    OPERATORList.add(transation.REtoDFA0(s));
                }
            }
        }
        getInput();

        int currentPoint = 0;
        while(input.get(currentPoint)!='$') {
            int temp = currentPoint;
            if (Character.isLetter(input.get(currentPoint))) {
                currentPoint = matchKeyWord(KEYList, currentPoint);
                if (currentPoint == temp) {
                    currentPoint = matchID(WORDList, currentPoint);
                }
            }else if(Character.isDigit((input.get(currentPoint)))) {
                currentPoint = matchNUM(NUMList,currentPoint);
            }else if((input.get(currentPoint).equals('@')||input.get(currentPoint).equals('\n'))){
                currentPoint = currentPoint + 1;
            }else if(input.get(currentPoint)==';'||input.get(currentPoint)=='('||input.get(currentPoint)==')'
            ||input.get(currentPoint)=='}'||input.get(currentPoint)=='{') {
                tokens.add(new Token("SPECIAL",input.get(currentPoint).toString()));
                currentPoint = currentPoint + 1;
            }else{
                currentPoint = matchOPERATOR(OPERATORList,currentPoint);
            }
        }
        for(Token t:tokens) {
            System.out.println(t.toString());
            if(t.getType().equals("Identifier ")) {
                FileOutputStream fs2 = new FileOutputStream(new File("txtFile/symbol.txt"), true); //在该文件的末尾添加内容
                String in = t.getStr()+"\r\n";
                fs2.write(in.getBytes());
                fs2.flush();   //清空缓存里的数据，并通知底层去进行实际的写操作
                fs2.close();
            }
        }
    }


    private static int matchKeyWord(List<DFA> KEYList, int start) {
        int point = start;
        for (DFA dfa : KEYList) {
            ArrayList<Map<Character, Integer>> list = DFAtoTable(dfa);
            ArrayList<Character> str = new ArrayList<>();//保存处理过的字符
            Map<Character, Integer> map = list.get(0); //获取第一个状态

            if(map.get(input.get(start))==null) {
                point = start;
                continue;
            }
            for (int p = start; p < 200; ) {
                if (map.get(input.get(p)) != null) { //如果这个状态属于字符有当前字符 到下一个状态
                        int nextState = map.get(input.get(p));
                        str.add(input.get(p));
                        if (map.get('e').equals(-1)) {  //表明当前状态是终态
                            if (map.get(input.get(p + 1)) == null) { //如果该终态有下一个字符的后继 则继续 没有则判断下一字符属性
                                if (input.get(p + 1) == '\n' || input.get(p + 1) == '@' || input.get(p + 1) == '{'
                                        || input.get(p + 1) == '}' || input.get(p + 1) == '(' || input.get(p + 1) == ')') { //下一字符为换行或@ 则以上字符匹配为关键字 保存Token
                                    String info = "";
                                    for (char c : str) {
                                        info = info + c;
                                    }
                                    tokens.add(new Token("KeyWord", info));
                                    point = p + 1;
                                    str.clear();
                                    break;
                                } else if (Character.isDigit(input.get(p + 1))) { //如果下一个字符是数字 那么说明这不属于关键字 可能属于标识符 应去判断是否属于标识符
                                    //判断是否符合标识符定义
                                    point = start;
                                    break;
                                } else {
                                    tokens.add(new Token("词法错误1"));
                                    point = p + 1;
                                    break;
                                }
                            }
                        }
                        //状态不是终态 继续判断
                        p = p + 1;
                        map = list.get(nextState);
                        if (map != null) { //如果有下一个状态
                            if (map.get('e').equals(-1)) {  //如果这个状态是终态
                                if (map.get(input.get(p)) == null) { //如果该终态有这个字符的后继 则继续 没有则判断下一字符属性
                                    if (input.get(p) == '\n' || input.get(p) == '@' || input.get(p) == '{'
                                            || input.get(p) == '}' || input.get(p) == '(' || input.get(p) == ')') { //字符为换行 则以上字符匹配为关键字 保存Token
                                        String info = "";
                                        for (char c : str) {
                                            info = info + c;
                                        }
                                        tokens.add(new Token("KeyWord", info));
                                        str.clear();
                                        point = p;
                                        break;
                                    } else if (Character.isDigit(input.get(p))) { //如果这个字符是数字 那么说明这不属于关键字 可能属于标识符 应去判断是否属于标识符
                                        //判断是否符合标识符定义
                                        point = start;
                                        break;
                                    } else {
                                        tokens.add(new Token("词法错误2"));
                                        point = p;
                                        break;
                                    }
                                }

                            }
                        }

                    } else {
                        break;
                    }
                }
            }
            return point;
        }

    private static int matchID(List<DFA> WORDList, int start) {
        int point = start;
        boolean isHave = false; //这里考虑了错误问题 已经确定不是关键字 但在ID中未被定义
        for(DFA dfa0:WORDList) {
            ArrayList<Map<Character, Integer>> list = DFAtoTable(dfa0);
            Map<Character, Integer> map = list.get(0); //获取第一个状态
            if(map.get(input.get(start))!=null) {
                isHave = true;
                break;
            }
        }
        if(isHave==false) {  //说明没有一个定义的RE符合它
            tokens.add(new Token("未被定义"));
            point = point+1;
            return point;
        }
        for(DFA dfa:WORDList) {
            ArrayList<Map<Character, Integer>> list = DFAtoTable(dfa);
            ArrayList<Character> str = new ArrayList<>();//保存处理过的字符
            Map<Character, Integer> map = list.get(0); //获取第一个状态

            for(int p=start; p<200;) {
                if (map.get(input.get(p))!=null) { //如果这个状态属于字符有当前字符 到下一个状态
                    int nextState = map.get(input.get(p));
                    str.add(input.get(p));
                    if (map.get('e').equals(-1)) {  //表明当前状态是终态
                        if (map.get(input.get(p + 1)) == null) { //如果该终态有下一个字符的后继 则继续 没有则判断下一字符属性
                            if (input.get(p + 1) == '\n'||input.get(p+1) == '@'||input.get(p+1)=='{'
                                    ||input.get(p+1)=='}'||input.get(p+1)=='('||input.get(p+1)==')'
                                    ||input.get(p+1)=='>'||input.get(p+1)=='<'
                                    ||input.get(p+1)=='=') { //下一字符为换行或@ 则以上字符匹配为关键字 保存Token
                                String info = "";
                                for(char ch:str) {
                                    info = info + ch;
                                }
                                tokens.add(new Token("Identifier ", info));
                                point = p+1;
                                str.clear();
                                break;
                            } else {
                                tokens.add(new Token("词法错误3"));
                                point = p+1;
                                break;
                            }
                        }
                    }
                    //状态不是终态 继续判断
                    p = p + 1;
                    map = list.get(nextState);
                    if(map!=null) { //如果有下一个状态
                        if (map.get('e').equals(-1)) {  //如果下一个状态是终态
                            if (map.get(input.get(p)) == null) { //如果该终态有下一个字符的后继 则继续 没有则判断下一字符属性
                                if (input.get(p) == '\n'||input.get(p) == '@'||input.get(p)=='{'
                                        ||input.get(p)=='}'||input.get(p)=='('||input.get(p)==')'
                                        ||input.get(p)=='>'||input.get(p)=='<'
                                        ||input.get(p)=='='||input.get(p)==';') { //下一字符为换行 则以上字符匹配为关键字 保存Token
                                    String info = "";
                                    for(char c:str) {
                                        info = info+c;
                                    }
                                    tokens.add(new Token("Identifier ", info));
                                    str.clear();
                                    point = p;
                                    break;
                                }else {
                                    tokens.add(new Token("词法错误4"));
                                    point = p;
                                    break;
                                }
                            }

                        }
                    }

                } else {
                    break; // 不属于该DFA
                }
            }
        }
        return  point;
    }

    private static int matchNUM(List<DFA> NUMList, int start) {
        int point = 0;
        for(DFA dfa:NUMList) {
            ArrayList<Map<Character, Integer>> list = DFAtoTable(dfa);
            ArrayList<Character> str = new ArrayList<>();//保存处理过的字符
            Map<Character, Integer> map = list.get(0); //获取第一个状态
            for(int p=start; p<200;) {
                if (map.get(input.get(p))!=null) { //如果这个状态属于字符有当前字符 到下一个状态
                    int nextState = map.get(input.get(p));
                    str.add(input.get(p));
                    if (map.get('e').equals(-1)) {  //表明当前状态是终态
                        if (map.get(input.get(p + 1)) == null) { //如果该终态有下一个字符的后继 则继续 没有则判断下一字符属性
                            if (input.get(p + 1) == '\n'||input.get(p+1) == '@'||input.get(p+1)=='('||input.get(p+1)==')'
                                    ||input.get(p+1)==';') { //下一字符为换行或@ 则以上字符匹配为关键字 保存Token
                                String info = "";
                                for(char ch:str) {
                                    info = info + ch;
                                }
                                tokens.add(new Token("NUM", info));
                                point = p+1;
                                str.clear();
                                break;
                            } else {
                                tokens.add(new Token("词法错误5"));
                                point = p+1;
                                break;
                            }
                        }
                    }
                    //状态不是终态 继续判断
                    p = p + 1;
                    map = list.get(nextState);
                    if(map!=null) { //如果有下一个状态
                        if (map.get('e').equals(-1)) {  //如果下一个状态是终态
                            if (map.get(input.get(p)) == null) { //如果该终态有下一个字符的后继 则继续 没有则判断下一字符属性
                                if (input.get(p) == '\n'||input.get(p) == '@'||input.get(p)=='('||input.get(p)==')'
                                        ||input.get(p)==';') { //下一字符为换行 则以上字符匹配为关键字 保存Token
                                    String info = "";
                                    for(char c:str) {
                                        info = info+c;
                                    }
                                    tokens.add(new Token("NUM", info));
                                    str.clear();
                                    point = p;
                                    break;
                                }else {
                                    tokens.add(new Token("词法错误6"));
                                    point = p;
                                    break;
                                }
                            }

                        }
                    }

                } else {
                    break; // 不属于该DFA
                }
            }
        }
        return  point;
    }

    private static int  matchOPERATOR(List<DFA> OPERATORList, int start) {
        int point = start;
        for(DFA dfa:OPERATORList) {
            ArrayList<Map<Character, Integer>> list = DFAtoTable(dfa);
            ArrayList<Character> str = new ArrayList<>();//保存处理过的字符
            Map<Character, Integer> map = list.get(0); //获取第一个状态
            for(int p=start; p<200;) {
                if (map.get(input.get(p))!=null) { //如果这个状态属于字符有当前字符 到下一个状态
                    int nextState = map.get(input.get(p));
                    str.add(input.get(p));
                    if (map.get('e').equals(-1)) {  //表明当前状态是终态
                        if (map.get(input.get(p + 1)) == null) { //如果该终态有下一个字符的后继 则继续 没有则判断下一字符属性
                            if (input.get(p + 1) == '\n'||input.get(p+1) == '@'||input.get(p+1)=='('
                                    ||Character.isDigit(input.get(p + 1))||Character.isLetter(input.get(p + 1))) { //下一字符为换行或@ 则以上字符匹配为关键字 保存Token
                                String info = "";
                                for(char ch:str) {
                                    info = info + ch;
                                }
                                tokens.add(new Token("OPERATOR", info));
                                point = p+1;
                                str.clear();
                                break;
                            } else {
                                tokens.add(new Token("词法错误7"));
                                point = p+1;
                                break;
                            }
                        }
                    }
                    //状态不是终态 继续判断
                    p = p + 1;
                    map = list.get(nextState);
                    if(map!=null) { //如果有下一个状态
                        if (map.get('e').equals(-1)) {  //如果下一个状态是终态
                            if (map.get(input.get(p)) == null) { //如果该终态有下一个字符的后继 则继续 没有则判断下一字符属性
                                if (input.get(p) == '\n'||input.get(p) == '@'||input.get(p)=='('
                                        ||Character.isDigit(input.get(p))||Character.isLetter(input.get(p))) { //下一字符为换行 则以上字符匹配为关键字 保存Token
                                    String info = "";
                                    for(char c:str) {
                                        info = info+c;
                                    }
                                    tokens.add(new Token("OPERATOR", info));
                                    str.clear();
                                    point = p;
                                    break;
                                }else {
                                    tokens.add(new Token("词法错误8"));
                                    point = p;
                                    break;
                                }
                            }

                        }
                    }

                } else {
                    break; // 不属于该DFA
                }
            }
        }
        return  point;
    }

    private static Map<String, List<String>> readREs() {
        Map<String, List<String>> reMap = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream("txtFile/RE.txt"), "utf-8"));
            String temp = null;
            while ((temp = br.readLine()) != null) {
                String[] reType = temp.split("@");
                if(reMap.get(reType[1])==null ){
                    List<String> reInfo = new ArrayList<>();
                    reInfo.add(reType[0]);
                    reMap.put(reType[1],reInfo);
                } else {
                    reMap.get(reType[1]).add(reType[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reMap;
    }

    private static void getInput() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File("txtFile/program.txt"))));
        String line = null;
        char[] temp = null;
        while ((line = br.readLine()) != null) {
            temp = line.toCharArray();
            for (int i = 0; i < temp.length; i++) {
                if (temp[i]=='\t')
                    continue;
                if(temp[i] == ' ') {
                    input.add('@');
                    continue;
                }
                input.add(temp[i]);
            }
            input.add('\n');
        }
        input.add('$');
        br.close();
    }


    private static ArrayList<Map<Character, Integer>> DFAtoTable( DFA dfa) {
        ArrayList<Map<Character, Integer>> transationTable = new ArrayList<Map<Character, Integer>>();
        LinkedList<State> states = dfa.getDFA(); //这个DFA包含的所有状态
        for(State s:states) {
            Map<Character, Integer> transationMap = new HashMap<Character, Integer>();
            Map<Character,ArrayList<State>> map = s.getNextState();
            for(Map.Entry<Character, ArrayList<State>> entry : map.entrySet()){
                transationMap.put(entry.getKey(),entry.getValue().get(0).getStateNo());
            }
            if(s.isFinalSate()) transationMap.put('e',-1);
            else transationMap.put('e',0);
            transationTable.add(s.getStateNo(),transationMap);
        }
        return  transationTable;
    }
}