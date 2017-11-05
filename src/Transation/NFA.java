package Transation;

import java.util.LinkedList;

public class NFA {
    private LinkedList<State> nfa;

    public NFA() {
        this.setNFA(new LinkedList<State>());
        this.getNFA().clear();
    }

    public LinkedList<State> getNFA() {
        return nfa;
    }

    /**填入NFA状态*/
    public void setNFA(LinkedList<State> nfa) {
        this.nfa = nfa;
    }

    /**添加状态*/
    public void addState(State state) {
        this.nfa.add(state);
    }

}