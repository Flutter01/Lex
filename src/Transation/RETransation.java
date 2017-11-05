package Transation;

import java.util.LinkedList;

/**
 * Created by asus1 on 2017/11/5.
 */
public class RETransation {
    public DFA REtoDFA0(String re) {
        RE2DFA re2DFA = new RE2DFA();
        minimizeDFA min = new minimizeDFA();
        NFA nfa = re2DFA.produceNFA(re);
        DFA dfa = re2DFA.produceDFA(nfa);
        DFA dfa0 = min.Main(dfa, re2DFA.getInput());
        return dfa;
    }

}