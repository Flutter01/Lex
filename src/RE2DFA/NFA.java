package RE2DFA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class NFA {
    private LinkedList<State> nfa;
    private LinkedList<State> startState = new LinkedList<State>();
    private LinkedList<State> endState = new LinkedList<State>();

    public NFA() {
        this.setNFA(new LinkedList<State>());
        this.getNFA().clear();
    }

    public LinkedList<State> getNFA() {
        return nfa;
    }

    public void setNFA(LinkedList<State> nfa) {
        this.nfa = nfa;
    }



    public void addState(State state) {
        this.nfa.add(state);
    }

    public void addStartState(State state) {
        this.startState.add(state);
    }

    public State getStartState() {
        return  this.startState.getLast();
    }

    public void addEndState(State state) {
        this.endState.add(state);
    }


}