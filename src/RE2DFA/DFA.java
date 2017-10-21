package RE2DFA;

import java.util.LinkedList;

public class DFA {
    private LinkedList<State> dfa;

    public DFA () {
        this.setDFA(new LinkedList<State> ());
        this.getDFA().clear();
    }

    public LinkedList<State> getDFA() {
        return dfa;
    }

    public void setDFA(LinkedList<State> dfa) {
        this.dfa = dfa;
    }
}

