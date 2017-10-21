package RE2DFA;

import java.util.LinkedList;

public class NFA {
    private LinkedList<State> nfa;

    public NFA () {
        this.setNFA(new LinkedList<State> ());
        this.getNFA().clear();
    }

    public LinkedList<State> getNFA() {
        return nfa;
    }

    public void setNFA(LinkedList<State> nfa) {
        this.nfa = nfa;
    }

    public static NFA mergeNFA(NFA nfa2, NFA nfa1) {
        return null;
    }

    public static NFA connectNFA(NFA nfa2, NFA nfa1) {
        return null;
    }

    public static NFA ringNFA(NFA nfa) {
        return null;
    }

    public void addState(State state){};

    public void addStartState(State state){};

    public void addEndState(State state){};

    public void addEdge(Edge edge){};
}
