package RE2DFA;

public class Edge {

    private char condition;//边产生的条件 包括ε
    private int startStateNo;//边开始的状态
    private  int endStateNo;//边结束的状态

    public Edge(char condition, int startStateNo, int endStateNo) {
        this.condition = condition ;
        this.startStateNo = startStateNo ;
        this.endStateNo = endStateNo ;
    }

    public char getCondition() {
        return condition;
    }

    public void setCondition(char condition) {
        this.condition = condition;
    }

    public int getStartStateNo() {
        return startStateNo;
    }

    public void setStartStateNo(int startStateNo) {
        this.startStateNo = startStateNo;
    }

    public int getEndStateNo() {
        return endStateNo;
    }

    public void setEndStateNo(int endStateNo) {
        this.endStateNo = endStateNo;
    }

}
