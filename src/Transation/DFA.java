package Transation;

import java.util.ArrayList;
import java.util.LinkedList;

public class DFA {
    private LinkedList<State> dfa = new LinkedList<>();
    private State startState  ;
    private ArrayList<State> endState = new ArrayList<>();

    public DFA () {
        this.setDFA(new LinkedList<State> ());
        this.getDFA().clear();
    }

    public DFA(State start, ArrayList<State> end, ArrayList<State> dfa) {
        this.dfa.addAll(dfa);
        this.startState = start;
        this.endState = end;
    }

    public LinkedList<State> getDFA() {
        return dfa;
    }

    public void setDFA(LinkedList<State> dfa) {
        this.dfa = dfa;
    }

    public State getStartState() {
        return this.startState;
    }

    public ArrayList<State> getEndState() {
        return this.endState;
    }
}

