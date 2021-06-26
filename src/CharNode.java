public class CharNode {
    private String character;
    private CharNode next;
    private CharNode previous;

    public CharNode(String dataToAdd) {
        character = dataToAdd;
        next = null;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String itemName) {
        character = itemName;
    }

    public CharNode getNext() {
        return next;
    }

    public void setNext(CharNode next) {
        this.next = next;
    }
    
    public CharNode getPrevious() {
        return previous;
    }

    public void setPrevious(CharNode previous) {
        this.previous = previous;
    }
}
