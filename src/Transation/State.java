package Transation;

import java.util.*;

public class State {
    private int stateNo;//状态号
    private boolean isFinalSate = false;//判断状态是否是终态
    private Set <State> states;
    private Map<Character, ArrayList<State>> nextState = new HashMap<Character, ArrayList<State>>();

    /**为NFA构造函数*/
    public State(int stateNo){
        this.stateNo = stateNo;
    }

    /**为DFA构造函数*/
    public State(Set<State> states, int stateNo) {
        this.states = states;
        this.stateNo = stateNo;
        for (State s : states) {
            if (s.isFinalSate()) {
                this.setFinalSate(true);
                break;
            }
        }
    }

    /**获得状态编号*/
    public int getStateNo() {
        return this.stateNo;
    }

    /**判断该状态是否是终态*/
    public boolean isFinalSate(){
        return  this.isFinalSate;
    }

    /**将状态设置为终态*/
    public void setFinalSate(boolean isFinalSate){
        this.isFinalSate = isFinalSate;
    }

    /**输入字符获得下一个状态*/
    public void setNextState(Character production, State next) {
        //这里没有写遍历检验是否存在
        ArrayList <State> list = this.nextState.get(production);

        if (list == null) {
            list = new ArrayList<State> ();
            this.nextState.put(production, list);
        }
        list.add(next);
    }

    /**获得当前状态的所有后继状态与输入字符构成的表*/
    public Map<Character, ArrayList<State>> getNextState(){
        return this.nextState;
    }

    /**获得当前状态在某字符下的所有后继状态*/
    public ArrayList<State> getAllProductions(char c) {
        if (this.nextState.get(c) == null)	{
            return new ArrayList<State> ();
        }
        else {
            return this.nextState.get(c);
        }
    }

    /**获得DFA中状态*/
    public Set<State> getStates() {
        return this.states;
    }

}
