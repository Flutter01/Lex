package Transation;


import java.util.*;

public class RE2DFA {

    private static int stateNo = 0;
    private static Set<State> set1 = new HashSet <State> ();
    private static Set<State> set2 = new HashSet <State> ();
    private static ArrayList<Character> input = new ArrayList<> ();

    public NFA produceNFA(String expr){
        stateNo=0;
        input.clear();
        //将输入表达式转成后缀表达式
        String infixExpr = addConnector(expr);
        String postfixExpr = infixToPostfix(infixExpr);
        Stack<NFA> middleNFAs = new Stack<NFA>();
        for(int i=0;i<postfixExpr.length();i++) {
            if(postfixExpr.charAt(i) == '|'){
                NFA nfa2 = middleNFAs.pop();
                NFA nfa1 = middleNFAs.pop();
                middleNFAs.push(mergeNFA(nfa2, nfa1));
                continue;
            }
            if(postfixExpr.charAt(i) == '.'){
                NFA nfa2 = middleNFAs.pop();
                NFA nfa1 = middleNFAs.pop();
                middleNFAs.push(connectNFA(nfa2, nfa1));
                continue;
            }
            if(postfixExpr.charAt(i)=='*'){
                NFA nfa = middleNFAs.pop();
                middleNFAs.push(ringNFA(nfa));
                continue;
            }
            //if是表达式就构造小的NFA并压栈
            if(!input.contains(postfixExpr.charAt(i)))
                input.add(postfixExpr.charAt(i));
            NFA newNFA = new NFA();  //假设只有一个a 按照托马孙算法
            State state1 = new State(stateNo);
            State state2 = new State(stateNo+1);
            state1.setNextState(postfixExpr.charAt(i), state2);
            state2.setFinalSate(true);
            stateNo = stateNo+2; //下一个状态的开始序号
            newNFA.addState(state1);
            newNFA.addState(state2);
            middleNFAs.push(newNFA);
        }
        NFA nfa = middleNFAs.pop();
        return nfa;

    }

    public DFA produceDFA(NFA nfa) {
        DFA dfa = new DFA ();
        stateNo = 0;

        LinkedList <State> unprocessed = new LinkedList<State>();

        set1 = new HashSet <State> ();
        set2 = new HashSet <State> ();

        set1.add(nfa.getNFA().getFirst());
        seekEpsilonClosure();
        State dfaStart = new State (set2, stateNo++);
        dfa.getDFA().addLast(dfaStart);
        unprocessed.addLast(dfaStart);

        while (!unprocessed.isEmpty()) {
            State state = unprocessed.removeLast();

            for (Character symbol : input) {
                set1 = new HashSet<State> ();
                set2 = new HashSet<State> ();
                moveStates (symbol, state.getStates(), set1);
                seekEpsilonClosure ();

                boolean found = false;
                State st = null;

                for (int i = 0 ; i < dfa.getDFA().size(); i++) {
                    st = dfa.getDFA().get(i);

                    if (st.getStates().containsAll(set2)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    State p = new State (set2, stateNo++);
                    unprocessed.addLast(p);
                    dfa.getDFA().addLast(p);
                    state.setNextState( symbol,p);

                } else {
                    state.setNextState( symbol,st);
                }
            }
        }

        return dfa;
    }

    private static void seekEpsilonClosure() {
        Stack <State> stack = new Stack <State> ();
        set2 = set1;

        for (State st : set1) { stack.push(st);	}
        while (!stack.isEmpty()) {
            State s = stack.pop();
            ArrayList <State> epsilonStates = s.getAllProductions ('ε');
            for (State p : epsilonStates) {
                if (!set2.contains(p)) {
                    set2.add(p);
                    stack.push(p);
                }
            }
        }
    }

    private static void moveStates(Character c, Set<State> states,	Set<State> set) {
        ArrayList <State> temp = new ArrayList<State> ();
        for (State s : states) {	temp.add(s);	}

        for (State s : temp) {
            ArrayList<State> allStates = s.getAllProductions(c);
            for (State p : allStates) {	set.add(p);	}
        }
    }

    /**
     * 当遇到'.'时，两个NFA首尾连接
     * @param nfa2
     * @param nfa1
     * @return
     */
    public NFA connectNFA(NFA nfa2, NFA nfa1) {
        State originalEnd = nfa1.getNFA().getLast();

        originalEnd.setFinalSate(false);
        originalEnd.setNextState('ε',nfa2.getNFA().getFirst());

        for (State s : nfa2.getNFA()) {	nfa1.getNFA().addLast(s); }
        return nfa1;
    }

    /**
     *
     * @param nfa2
     * @param nfa1
     * @return
     */
    public NFA mergeNFA(NFA nfa2, NFA nfa1) {
        State start = new State (stateNo++);
        State end	= new State (stateNo++);
        end.setFinalSate(true);

        start.setNextState('ε',nfa1.getNFA().getFirst());
        start.setNextState('ε',nfa2.getNFA().getFirst());

        nfa1.getNFA().getLast().setNextState('ε',end);
        nfa1.getNFA().getLast().setFinalSate(false);
        nfa2.getNFA().getLast().setNextState('ε',end);
        nfa2.getNFA().getLast().setFinalSate(false);

        nfa1.getNFA().addFirst(start);
        nfa2.getNFA().addLast(end);

        for (State s : nfa2.getNFA()) {
            nfa1.getNFA().addLast(s);
        }
        return nfa1;
    }

    public NFA ringNFA(NFA nfa) {
        // Create states for star operation
        State start = new State (stateNo++);
        State end	= new State (stateNo++);
        end.setFinalSate(true);

        // Add transition to start and end state
        start.setNextState('ε',end);
        start.setNextState('ε',nfa.getNFA().getFirst());

        nfa.getNFA().getLast().setNextState('ε',end);
        nfa.getNFA().getLast().setNextState('ε',nfa.getNFA().getFirst());
        nfa.getNFA().getLast().setFinalSate(false);

        nfa.getNFA().addFirst(start);
        nfa.getNFA().addLast(end);

        return nfa;
    }

    /**
     * 预处理中缀表达式，增加连接符"."
     * @param expr 输入的表达式
     * @return 经预处理过的表达式
     */
    public String addConnector(String expr) {
        // '(' '|' 之后无连接符   '*' ')' '|'之前无连接符
        String newExpr = new String("");
        for(int i=0; i<expr.length();i++){
            if(expr.charAt(i)=='('){
                newExpr = newExpr + (expr.charAt(i));
                continue;
            }
            else if(expr.charAt(i)=='|'){
                newExpr = newExpr + (expr.charAt(i));
                continue;
            }
            else{
                if(i==expr.length()-1 ||expr.charAt(i+1)=='|' || expr.charAt(i+1)=='*' || expr.charAt(i+1)==')'){
                    newExpr = newExpr + (expr.charAt(i));
                }else{
                    newExpr = newExpr + (expr.charAt(i))+".";
                }
                continue;
            }
        }
        return newExpr;
    }

    /**
     * 中缀表达式转后缀表达式
     * @param infixExpr 中缀表达式
     * @return postExpr 后缀表达式
     * 1）如果遇到操作数，我们就直接将其输出。
     * 2）如果遇到操作符，则我们将其放入到栈中，遇到左括号时我们也将其放入栈中。
     * 3）如果遇到一个右括号，则将栈元素弹出，将弹出的操作符输出直到遇到左括号为止。注意，左括号只弹出并不输出。
     * 4）如果遇到任何其他的操作符，如（ “*”，“（”）等，从栈中弹出元素直到遇到发现更低优先级的元素(或者栈为空)为止。
     * *  弹出完这些元素后，才将遇到的操作符压入到栈中。有一点需要注意，只有在遇到" ) "的情况下我们才弹出" ( "，其他情况我们都不会弹出" ( "。
     */
    public String infixToPostfix(String infixExpr) {
        String postExpr = new String("");
        Stack stack = new Stack();
        for(int i=0;i<infixExpr.length();i++) {
            if(infixExpr.charAt(i)=='('|infixExpr.charAt(i)=='*') {
                stack.push(infixExpr.charAt(i));
                continue;
            }
            if(infixExpr.charAt(i)=='.'|infixExpr.charAt(i)=='|'){
                if(stack.isEmpty()){
                    stack.push(infixExpr.charAt(i));
                    continue;
                }
                while(!comparePrior(infixExpr.charAt(i), stack.peek().toString().charAt(0))){
                    postExpr = postExpr + stack.pop();
                    if(stack.isEmpty()){
                        break;
                    }
                }
                stack.push(infixExpr.charAt(i));
                continue;
            }
            if(infixExpr.charAt(i)==')'){
                if(stack.isEmpty()){
                    continue;
                }
                while(!stack.peek().equals('(')){
                    postExpr = postExpr + stack.pop();
                }
                stack.pop();
                continue;
            }
            postExpr = postExpr + infixExpr.charAt(i);
            continue;
        }
        //最后将stack内元素一一输出
        while(!stack.isEmpty()){
            postExpr = postExpr + stack.pop();
        }
        return postExpr;
    }

    /**
     * <p>优先级比较<br>
     * @param operator1 比较值
     * @param operator2 被比较值
     * @return 小于等于返回false,大于返回true
     */
    public boolean comparePrior(char operator1, char operator2) {
        if(operator2 == '(') {
            return true;
        }
        if (operator1 == '*') {
            return true;
        }
        if (operator1 == '|' && operator2 == '.') {
            return true;
        }
        return false;
    }

    public ArrayList<Character> getInput() {
        return input;
    }

    public static void main(String[] args){
        String s = "=";

        RE2DFA re = new RE2DFA();
        minimizeDFA min = new minimizeDFA();
        NFA nfa = re.produceNFA("x1");
        System.out.println(re.produceDFA(nfa).getDFA().size());
    }
}
