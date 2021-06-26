public class DoubleLinkedList {

    private CharNode head;
    private CharNode tail;

    public DoubleLinkedList() {

        head = null;
        tail = null;
    }

    public void addEnd(String data) {

        if(head == null && tail == null) {

            CharNode newnode = new CharNode(data);
            head = newnode;
            tail = newnode;

        }
        else {

            CharNode newnode = new CharNode(data);
            newnode.setPrevious(tail);
            tail.setNext(newnode);
            tail = newnode;

        }

    }

    public char getFromIndex(int i) {
        if(head == null) {
            System.out.println("LinkedList is empty");
            return ' ';
        }
        else {
            CharNode temp = head;
            for(int k = 0; k<i; k++) {
                temp = temp.getNext();
            }
            return temp.getCharacter().charAt(0);
        }
    }

    public int size() {
        int count =0;
        CharNode temp = head;
        while(temp!=null) {
            count++;
            temp=temp.getNext();
        }
        return count;
    }
    public void clearDLL() {
        tail=null;
        head=null;
    }

}
