package RE2DFA;

import java.util.ArrayList;
import java.util.Map;

public class State {

    private int stateNo;//状态号
    private boolean isFinalSate = false;//判断状态是否是终态
    private Map<Character, ArrayList<State>> nextState;

    public State(int stateNo){
        this.stateNo = stateNo;
    }

    public int getStateNo() {
        return stateNo;
    }

    public void setStateNo(int stateNo){
        this.stateNo = stateNo;
    }

    public boolean isFinalSate(){
        return  isFinalSate;
    }

    public void setFinalSate(boolean isFinalSate){
        this.isFinalSate = isFinalSate;
    }

    public Map<Character, ArrayList<State>> getNextState() {
        return nextState;
    }

    public void setNextState(Map<Character, ArrayList<State>> nextState) {
        this.nextState = nextState;
    }
}
