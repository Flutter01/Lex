package Transation;

import java.util.ArrayList;
import java.util.LinkedList;

public class DFA {
    private LinkedList<State> dfa = new LinkedList<>();
    private State startState;
    private ArrayList<State> endState = new ArrayList<>();

    /**基本DFA的构造函数*/
    public DFA () {
        this.setDFA(new LinkedList<State> ());
        this.getDFA().clear();
    }

    /**最小化DFA的构造函数*/
    public DFA(State start, ArrayList<State> end, ArrayList<State> dfa) {
        this.dfa.addAll(dfa);
        this.startState = start;
        this.endState = end;
    }

    public String toString(){
        String s = null;
        s = String.valueOf(this.dfa.size());
        return s;
    }

    /**获得DFA包含的所有状态*/
    public LinkedList<State> getDFA() {
        return dfa;
    }

    /**添加状态*/
    public void setDFA(LinkedList<State> dfa) {
        this.dfa = dfa;
    }

}

