public class RowNode {
    private int RowNumber;
    private RowNode down;
    private RowNode up;
    private CharNode right;

    public RowNode() {
        down = null;
        up=null;
        right = null;
    }


    public RowNode getDown() {
        return down;
    }

    public void setDown(RowNode down) {
        this.down = down;
    }

    public void setUp(RowNode up) {
        this.up = up;
    }
    
    public CharNode getRight() {
        return right;
    }

    public void setRight(CharNode right) {
        this.right = right;
    }

}
