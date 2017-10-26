package Transation;

import java.util.*;

/**
 * Created by asus1 on 2017/10/26.
 */
public class minimizeDFA {
    /**
     * 分割
     * @return
     */
    public ArrayList<ArrayList<Integer>> Division(ArrayList<ArrayList<Integer>> state_division,ArrayList<Integer> current,Map<Integer, ArrayList<Integer>> map,int operand){
        //得到的转换状态在state_division中的位置
        ArrayList<Integer> position = new ArrayList<>();
        ArrayList<ArrayList<Integer>> current_division = new ArrayList<>();
        //处理没有指向下一节点的情况
        ArrayList<Integer> empty = new ArrayList<>();

        for(int i=0;i<current.size();i++){
            int next = map.get(current.get(i)).get(operand);
            for(int m=0;m<state_division.size();m++){ //循环检查当前结果集
                ArrayList<Integer> temp = state_division.get(m);
                //没有指向下一个节点的边
                if(next==-1){
                    empty.add(current.get(i));
                } else if(temp.contains(next)){
                    if(position.contains(m)){
                        current_division.get(position.indexOf(m)).add(current.get(i));
                    }else{
                        position.add(m);
                        ArrayList<Integer> new_list = new ArrayList<>();
                        new_list.add(current.get(i));
                        current_division.add(new_list);
                    }
                    break;
                }
            }
        }
        //用current_division的内容取代state_division中的current
        state_division.remove(current);
        if(empty.size()!=0){
            state_division.add(empty);
        }
        for(int i=0;i<current_division.size();i++){
            state_division.add(current_division.get(i));
        }
        return state_division;
    }

    /**
     * 判断一个节点是否可以被替代，是则返回-1，否则返回替代其的节点值
     * @param node
     * @param state_division
     * @return
     */
    public int IsReplaceable(int node,ArrayList<ArrayList<Integer>> state_division){
        for(int i=0;i<state_division.size();i++){
            ArrayList<Integer> current = state_division.get(i);
            if(current.contains(node)){
                //选择列表第一个代替该节点
                if(current.get(0)==node){
                    return -1;
                }else{
                    return current.get(0);
                }
            }
        }
        return -2;
    }

    public State getDFAState(ArrayList<State> states,int num){
        for(int i=0;i<states.size();i++){
            if(states.get(i).getStateNo()==num){
                return states.get(i);
            }
        }
        return null;
    }
    /**
     * 得到总的分割结果
     * @param dfa
     * @param input
     * @return
     */
    public DFA Main(DFA dfa, ArrayList <Character> input){
        ArrayList<Integer> terminalStates = new ArrayList();
        ArrayList<Integer> initialStates = new ArrayList();
        ArrayList<Set<State>> list = new ArrayList<>();//原始DFA里每个DFA含有的NFA状态
        LinkedList<State> states = dfa.getDFA();
        Map<Integer, ArrayList<Integer>> map = new HashMap<>();//key是起始状态 value是后继状态(NFA转DFA的表)
        State containStates;
        for(int i=0;i<states.size();i++) {
            if(states.get(i).isFinalSate()==true) {
                terminalStates.add(states.get(i).getStateNo());
            } else {
                initialStates.add(states.get(i).getStateNo());
            }
            list.add(states.get(i).getStates());
            containStates = states.get(i);
            ArrayList<Integer> stateNos = new ArrayList<>();
            for(int j=0;j<containStates.getNextStateSet().size();j++) {
                int a;
                if(containStates.getNextStateSet().get(j)==0&&containStates.getStateNo()!=0) a=-1;
                else a=containStates.getNextStateSet().get(j);
                stateNos.add(a);
            }
            map.put(containStates.getStateNo(),stateNos);
        }
        ArrayList<ArrayList<Integer>> state_division = new ArrayList<>();
        state_division.add(terminalStates);
        state_division.add(initialStates);
        ArrayList<ArrayList<Integer>> temp = state_division;
        int k=0;
        while(k==0||temp.size()!=state_division.size()){
            k=1;
            state_division = temp;
            for(int j=0;j<input.size();j++){
                for(int i=0;i<state_division.size();i++){
                    ArrayList<Integer> current = state_division.get(i);
                    temp = Division(state_division,current,map,j);
                }
            }
        }
        ArrayList<State> state = new ArrayList<>();
        for(int i=0;i<state_division.size();i++){
            int num = state_division.get(i).get(0);
            state.add(new State(list.get(num),num));
        }

        for(int i=0;i<state_division.size();i++){
            int num = state_division.get(i).get(0);
            State dfaState = getDFAState(state, num);
            for(int n=0;n<input.size();n++){
                int node = map.get(num).get(n);
                if(node!=-1){
                    if(IsReplaceable(node,state_division)==-1){
                        State temp_state = getDFAState(state, node);
                        dfaState.setNextState(input.get(n),temp_state);
                    }else{
                        State temp_state = getDFAState(state, IsReplaceable(node,state_division));
                        dfaState.setNextState(input.get(n),temp_state);
                    }
                }
            }
        }
        State start = getDFAState(state,0);
        ArrayList<State> end = new ArrayList<>();
        for(int i=0;i<terminalStates.size();i++){
            if(IsReplaceable(terminalStates.get(i),state_division)==-1){
                end.add(getDFAState(state,terminalStates.get(i)));
            }
        }
        DFA dfa0 = new DFA(start,end,state);
        return dfa0;
    }

}
