package RE2DFA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class NFA {
    private LinkedList<State> nfa;
    private State startState;
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

    public static NFA mergeNFA(NFA nfa2, NFA nfa1) {
/*        // Create states for union operation
        State start = new State (stateID++);
        State end	= new State (stateID++);

        // Set transition to the begin of each subNFA with empty string
        start.addTransition(nfa1.getNfa().getFirst(), 'e');
        start.addTransition(nfa2.getNfa().getFirst(), 'e');

        // Set transition to the end of each subNfa with empty string
        nfa1.getNfa().getLast().addTransition(end, 'e');
        nfa2.getNfa().getLast().addTransition(end, 'e');

        // Add start to the end of each nfa
        nfa1.getNfa().addFirst(start);
        nfa2.getNfa().addLast(end);

        // Add all states in nfa2 to the end of nfa1
        // in order
        for (State s : nfa2.getNfa()) {
            nfa1.getNfa().addLast(s);
        }
        // Put NFA back to stack
        stackNfa.push(nfa1);*/
        return null;
    }

    public static NFA ringNFA(NFA nfa) {
        return null;
    }

    public void addState(State state) {
        this.nfa.add(state);
    }

    ;

    public void addStartState(State state) {
        this.startState = state;
    }

    public void addEndState(State state) {
        this.endState.add(state);
    }

}