package RE2DFA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class State {

    private int stateNo;//状态号
    private boolean isFinalSate = false;//判断状态是否是终态
    private Map<Character, ArrayList<State>> nextState = new HashMap<Character, ArrayList<State>>();

    public State(int stateNo){
        this.stateNo = stateNo;
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

    public Map<Character, ArrayList<State>> getNextState() {
        return this.nextState;
    }

    public void setNextState(Map<Character, ArrayList<State>> nextState) {
        this.nextState = nextState;
    }
}
