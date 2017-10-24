package RE2DFA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class State {

    private int stateNo;//状态号
    private boolean isFinalSate = false;//判断状态是否是终态
    private Set <State> states;
    private Map<Character, ArrayList<State>> nextState = new HashMap<Character, ArrayList<State>>();//这里直接初始化了

    public State(int stateNo){
        this.stateNo = stateNo;
    }
    // This constructor is used for DFA
    public State(Set<State> states, int stateNo) {
        this.states = states;
        this.stateNo = stateNo;

        // find if there is final state in this set of states
        for (State s : states) {
            if (s.isFinalSate()) {
                this.setFinalSate(true);
                break;
            }
        }
    }

    public int getStateNo() {
        return this.stateNo;
    }

    public void setStateNo(int stateNo){
        this.stateNo = stateNo;
    }

    public boolean isFinalSate(){
        return  this.isFinalSate;
    }

    public void setFinalSate(boolean isFinalSate){
        this.isFinalSate = isFinalSate;
    }


    public void setNextState(Character production, State next) {
        //这里没有写遍历检验是否存在
        ArrayList <State> list = this.nextState.get(production);

        if (list == null) {
            list = new ArrayList<State> ();
            this.nextState.put(production, list);
        }
        list.add(next);
    }
    public ArrayList<State> getAllProductions(char c) {
        if (this.nextState.get(c) == null)	{	return new ArrayList<State> ();	}
        else 								{	return this.nextState.get(c);	}
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }
    public Set<State> getStates() {
        return states;
    }
}
