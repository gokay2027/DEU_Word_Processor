public class DoublyMultiLinkedList {
    RowNode head;

    //To delete node from the list.
    public void deleteChar(int rowNumber, int indexOfChar) {
        RowNode temp = head;
        int countRow = 1;
        while (countRow != rowNumber) {
            temp = temp.getDown();
            countRow++;
        }
        CharNode temp2 = temp.getRight();
        if(indexOfChar == 1) {
            temp.setRight(temp2.getNext());
            if (temp2.getNext() != null)
                temp2.getNext().setPrevious(null);
        }
        else {
            int countChar = 1;
            while (countChar != indexOfChar) {
                temp2 = temp2.getNext();
                countChar++;
            }
            if (temp2.getNext() == null) {
                temp2.getPrevious().setNext(null);
            }
            else {
                temp2.getPrevious().setNext(temp2.getNext());
                temp2.getNext().setPrevious(temp2.getPrevious());
            }
        }
    }

    //To implement enter. The rest of the characters goes down.
    public void deleteFromTheEnd(int py, int range) {
        int countRow = 1;
        int counter = 0;
        String[] chars = new String[range];
        RowNode temp = head;
        while (py != countRow) {
            temp = temp.getDown();
            countRow++;
        }
        CharNode temp2 = temp.getRight();
        while (temp2.getNext() != null) {
            temp2 = temp2.getNext();
        }
        while (counter != range) {
            if (counter == 0)
                chars[0] = temp2.getCharacter();
            else {
                chars[counter] = temp2.getCharacter();
            }

            temp2 = temp2.getPrevious();
            if (temp2 != null)
                temp2.setNext(null);
            else
                temp.setRight(null);
            counter++;
        }
        for (int i = 0; i < chars.length; i++) {
            addCharacter(py + 1, i + 1, chars[chars.length - 1 - i]);
        }
    }

    //To delete row
    public void deleteRow(int py) {
        RowNode temp = head;
        RowNode prev = null;
        int countRow = 1;
        while (countRow != py) {
            prev = temp;
            temp = temp.getDown();
            countRow++;
        }
        if (prev == null && temp.getDown() != null) {
            temp.getDown().setUp(null);
            head = temp.getDown();
            head.setUp(null);
        }
        else if (temp.getDown() == null && prev != null) {
            prev.setDown(null);
        }
        else if (prev != null && temp.getDown() != null){
            prev.setDown(temp.getDown());
            temp.getDown().setUp(prev);
        }
    }

    //To implement align left
    public void alignLeft() {
        RowNode temp = head;
        int countRow = 1;

        while (temp != null) {
            CharNode temp2 = temp.getRight();
            int countColumn = 1;
            while (temp2 != null) {
                if (temp2.getCharacter().equals(" ")) {
                    if (temp2.getNext() != null && temp2.getNext().getCharacter().equals(" ")) {
                        deleteChar(countRow, countColumn + 1);
                    }
                }
                temp2 = temp2.getNext();
                countColumn++;
            }
            temp = temp.getDown();
            countRow++;
        }
    }

    //Justify operation
    public void justify() {

        RowNode temp = head;
        int countRow = 1;
        while (temp != null) {
            if (sizeOfSelectedRow(countRow) != 60 && sizeOfSelectedRow(countRow) > 30) {
                CharNode temp2 = temp.getRight();
                while (temp2.getNext() != null) {
                    temp2 = temp2.getNext();
                }
                int countColumn = sizeOfSelectedRow(countRow);
                boolean flag = true;
                while (temp2.getPrevious() != null) {
                    if (temp2.getCharacter().equals(" ") && flag) {
                        deleteChar(countRow, countColumn);
                        countColumn--;
                    }
                    else
                        flag = false;
                    temp2 = temp2.getPrevious();
                }
                int emptySize = 60 - sizeOfSelectedRow(countRow);
                int emptyChar = 0;
                countColumn = 1;
                int[] spaces = new int[35];
                while (temp2 != null) {
                    if (temp2.getCharacter().equals(" ")) {
                        spaces[emptyChar] = countColumn;
                        emptyChar++;
                    }
                    countColumn++;
                    temp2 = temp2.getNext();
                }
                if (emptyChar != 0 && emptySize % emptyChar == 0) {
                    temp2 = temp.getRight();
                    countColumn = 1;
                    while (temp2 != null) {
                        if (temp2.getCharacter().equals(" ")) {
                            for (int i = 0; i < emptySize / emptyChar; i++) {
                                addCharacter(countRow, countColumn + i + 1, " ");
                                temp2 = temp2.getNext();
                            }
                            countColumn += emptySize / emptyChar;
                        }
                        temp2 = temp2.getNext();
                        countColumn++;
                    }
                }
                else {
                    int index = 0;
                    for (int i = 0; i < emptySize; i++) {
                        if (i > emptyChar)
                            index = 0;
                        addCharacter(countRow, spaces[index] + 1, " ");
                        for (int j = index + 1; j < emptyChar; j++) {
                            spaces[j]++;
                        }
                        index++;
                    }
                }
            }


            temp = temp.getDown();
            countRow++;
        }
    }

    //Used to prevent the word from being split in two when writing or in other situations
    public int headOfTheWord(int py, int px) {
        RowNode temp = head;
        int countRow = 1;
        while (countRow != py) {
            temp = temp.getDown();
            countRow++;
        }
        CharNode temp2 = temp.getRight();
        while (temp2.getNext() != null) {
            temp2 = temp2.getNext();
        }
        int countSize = 1;
        while (temp2.getPrevious() != null && !temp2.getPrevious().getCharacter().equals(" ")) {
            temp2 = temp2.getPrevious();
            countSize++;
        }
        int newPx = px - (60 - countSize);

        String[] chars = new String[countSize];

        temp2 = temp.getRight();
        while (temp2.getNext() != null) {
            temp2 = temp2.getNext();
        }
        countSize = 0;
        while (!temp2.getCharacter().equals(" ")) {
            chars[countSize] = temp2.getCharacter();
            if (temp2.getPrevious() == null)
                break;
            temp2 = temp2.getPrevious();
            countSize++;
        }
        temp2.setNext(null);
        if (countSize + 1 + sizeOfSelectedRow(py + 1) > 60)
            addRow(py);
        addCharacter(py + 1, 1, " ");
        for (int i = 0; i < chars.length ; i++) {
            addCharacter(py + 1, 1, chars[i]);
        }

        if (chars.length != 60)
            return newPx;
        else
            return 0;
    }

    //Keeps word's head part
    public void fromLastToHead(int py) {
        RowNode temp = head;
        int countRow = 1;
        while (countRow != py) {
            temp = temp.getDown();
            countRow++;
        }
        CharNode temp2 = temp.getRight();
        while (temp2.getNext() != null) {
            temp2 = temp2.getNext();
        }
        addCharacter(py + 1, 1, temp2.getCharacter());
        temp2.getPrevious().setNext(null);
    }

    //It is necessary to arrange px sometimes.
    public int returnTheWordHeadIndex(int py) {
        RowNode temp = head;
        int countRow = 1;
        while (countRow != py) {
            temp = temp.getDown();
            countRow++;
        }
        CharNode temp2 = temp.getRight();
        while (temp2.getNext() != null) {
            temp2 = temp2.getNext();
        }
        int index = 61;
        while (temp2.getPrevious() != null && !temp2.getPrevious().getCharacter().equals(" ")) {
            temp2 = temp2.getPrevious();
            index--;
        }
        return index;
    }

    //When backspace pressed if there is a enough space the word moves up.
    public void moveUpTheWholeWord(int py, int px) {
        if (rowCheck(py + 1) && sizeOfSelectedRow(py + 1) != 0) {
            CharNode temp = returnCharNode(py + 1, 1);
            int amount = 1;
            while (temp.getNext() != null && !temp.getNext().getCharacter().equals(" ")) {
                amount++;
                temp = temp.getNext();
                if (temp == null)
                    break;
            }

            String[] chars = new String[amount];
            int amountTemp = amount;

            if (temp != null && sizeOfSelectedRow(py) + amount < 61) {
                CharNode theNewFirstChar = temp.getNext();
                amount = 0;
                //temp = temp.getPrevious();
                while (temp != null && amountTemp != amount) {
                    chars[amount] = temp.getCharacter();
                    temp = temp.getPrevious();
                    amount++;
                }

                int counter = 1;
                RowNode headTemp = head;
                while (counter != py + 1) {
                    headTemp = headTemp.getDown();
                    counter++;
                }
                headTemp.setRight(theNewFirstChar);
                //CharNode lastCharOfTheLine = returnCharNode(py, sizeOfSelectedRow(py));
                for (int i = 0; i < chars.length; i++) {
                    addCharacter(py, sizeOfSelectedRow(py) + 1, chars[chars.length - 1  - i]);
                }
                if (sizeOfSelectedRow(py + 1) == 0)
                    deleteRow(py + 1);
            }
        }
    }

    //Checks if the whole row is full of letters.
    public boolean isFullOfLetters(int py) {
        RowNode temp = head;
        int countRow = 1;
        while (countRow != py) {
            temp = temp.getDown();
            countRow++;
        }
        CharNode temp2 = temp.getRight();;
        if (sizeOfSelectedRow(py) != 60)
            return false;
        else {
           while (temp2 != null) {
               if (temp2.getCharacter().equals(" "))
                   return false;
               temp2 = temp2.getNext();
           }
           return true;
        }
    }


    //Checks if the py th row exist.
    public boolean rowCheck(int py) {
        int countRow = 0;
        RowNode temp = head;
        while (temp != null) {
            temp = temp.getDown();
            countRow++;
        }
        return countRow >= py;
    }

    //Used to move up the row when pressed to backspace.
    public void copyTheRow(int py, int size) {
        String[] chars = new String[size];
        RowNode temp = head;
        int countRow = 1;
        while (countRow != py) {
            temp = temp.getDown();
            countRow++;
        }
        if (temp != null && temp.getRight() != null) {
            CharNode temp2 = temp.getRight();
            int countSize = 0;
            while (temp2 != null)  {
                chars[countSize] = temp2.getCharacter();
                temp2 = temp2.getNext();
                countSize++;
            }
            deleteRow(py);
            int sizeOfPrevRow = sizeOfSelectedRow(py - 1);
            int newRowCounter = 1;
            boolean isThere = false;
            for (int i = 0; i < chars.length; i++) {
                if (sizeOfSelectedRow(py - 1) >= 60) {
                    if (!isThere) {
                        isThere = true;
                        addRow(py - 1);
                    }
                    addCharacter(py, newRowCounter, chars[i]);
                    newRowCounter++;
                }
                else
                    addCharacter(py - 1, sizeOfPrevRow + 1 + i, chars[i]);
            }
        }
    }

    public CharNode returnCharNode(int py, int px) {
        RowNode temp = head;
        int countRow = 1;
        while (countRow != py) {
            temp = temp.getDown();
            countRow++;
        }
        CharNode temp2 = temp.getRight();
        int countColumn = 1;
        while (countColumn != px)  {
            temp2 = temp2.getNext();
            countColumn++;
        }
        return temp2;
    }

    public String returnData(int rowNumber, int indexOfChar) {
        int counter = 1;
        RowNode temp = head;
        if (temp == null)
            return null;
        while (rowNumber != counter) {
            if (temp == null)
                return null;
            temp = temp.getDown();
            counter++;
        }
        if (temp == null)
            return null;
        if (temp.getRight() != null) {
            CharNode temp2 = temp.getRight();
            if (temp2 == null) {
                return null;
            }
            else {
                for (int i = 0; i < indexOfChar - 1; i++) {
                    if (temp2 == null)
                        return null;
                    temp2 = temp2.getNext();
                }
                if (temp2 == null)
                    return null;
                else
                    return temp2.getCharacter();
            }
        }
        else return null;
    }

    public void addRow(int row) {
        if (head == null) {
            RowNode Row = new RowNode();
            head = Row;
            Row.setUp(null);
            Row.setDown(null);
        }
        else {
            int counter = 1;
            RowNode temp = head;
            RowNode prev = null;

            for (int i = 1; i < sizeRow() + 2; i++) {
                if (row + 1 == i) {
                    RowNode Row = new RowNode();
                    //add to last
                    if (temp == null) {
                        Row.setDown(null);
                        Row.setUp(prev);
                        prev.setDown(Row);
                    }
                    //add to first
                    else if (prev == null) {
                        Row.setDown(temp);
                        Row.setUp(null);
                        head = Row;
                        temp.setUp(Row);
                    }
                    //between
                    else {
                        Row.setDown(temp);
                        Row.setUp(prev);
                        prev.setDown(Row);
                        temp.setUp(Row);
                    }
                    break;
                }
                prev = temp;
                temp = temp.getDown();
            }
        }
    }

    public void addCharacter(int row, int column, String character) {
        if (head == null) {
            System.out.println("Add a row before character");
        }
        else {
            if (column > 60) {
                column = 1;
                row++;
            }

            int counter = 1;
            int countRow = 1;
            RowNode temp = head;
            while (temp != null) {
                if (row == countRow) {
                    CharNode temp2 = temp.getRight();
                    if (temp2 == null) {
                        CharNode ch = new CharNode(character);
                        temp.setRight(ch);
                        ch.setPrevious(null);
                        ch.setNext(null);
                        break;
                    }
                    CharNode prev = null;
                    while (column != counter) {
                        prev = temp2;
                        temp2 = temp2.getNext();
                        counter++;
                    }
                    CharNode ch = new CharNode(character);
                    if (temp2 != null)
                        temp2.setPrevious(ch);
                    ch.setPrevious(prev);
                    ch.setNext(temp2);
                    if (prev != null)
                        prev.setNext(ch);
                    else
                        temp.setRight(ch);
                    break;
                }
                temp = temp.getDown();
                countRow++;
            }
        }
    }



    public int sizeRow() {
        int count = 0;
        if (head == null) {
            System.out.println("Linked list is empty");
        }
        else {
            RowNode temp = head;
            while (temp != null) {
                count++;
                temp = temp.getDown();
            }
        }
        return count;
    }



    public int sizeOfSelectedRow(int rowNumber) {
        int count = 0;
        if (head == null) {
            System.out.println("Linked list is empty");
        }
        else {
            RowNode temp = head;
            int countRow = 1;
            while (temp != null && countRow != rowNumber) {
                temp = temp.getDown();
                countRow++;
            }
            if (temp != null) {
                CharNode temp2 = temp.getRight();
                while (temp2 != null) {
                    temp2 = temp2.getNext();
                    count++;
                }
            }
        }
        return count;
    }



    public void Display() {
        if (head == null) {
            System.out.println("Linked list is empty");
        }
        else {
            RowNode temp = head;
            int rowNum = 1;
            while (temp != null) {
                System.out.print(rowNum + " --> ");
                CharNode temp2 = temp.getRight();
                while (temp2 != null) {
                    System.out.print(temp2.getCharacter() + "");
                    temp2 = temp2.getNext();
                }
                temp = temp.getDown();
                rowNum++;
                System.out.println();
            }
        }
    }
    public DoubleLinkedList cut(int[]s, int[] e) {
        DoubleLinkedList DMLLselected = new DoubleLinkedList();
        RowNode temp = head;
        int countRow=1;
        while (countRow != s[0]) {
            temp = temp.getDown();
            countRow++;
        }
        CharNode temp2 = temp.getRight();
        int countCol=1;
        while (countCol != s[1]) {
            temp2 = temp2.getNext();
            countCol++;
        }
        boolean lastrow = e[0] == s[0];
        while(true) {
            if(lastrow) {
                while(countCol != e[1]) {
                    DMLLselected.addEnd(temp2.getCharacter());
                    temp2 = temp2.getNext();
                    deleteChar(countRow, 1);
                    countCol++;
                }
                break;
            }
            else {
                while(temp2 != null) {
                    DMLLselected.addEnd(temp2.getCharacter());
                    temp2 = temp2.getNext();
                    deleteChar(countRow, countCol);
                }
            }
            temp=temp.getDown();
            temp2 = temp.getRight();
            countRow++;
            countCol=1;
            if(countRow == e[0]) lastrow= true;
        }
        return DMLLselected;
    }
    public DoubleLinkedList copy(int[] s, int[] e) {
        if(s[0] == -1 || s[1] == -1 || e[0] == -1 || e[1] == -1) {
            return null;
        }
        DoubleLinkedList DMLLselected = new DoubleLinkedList();
        RowNode temp = head;
        int countRow=1;
        while (countRow != s[0]) {
            temp = temp.getDown();
            countRow++;
        }
        CharNode temp2 = temp.getRight();
        int countCol=1;
        while (countCol != s[1]) {
            temp2 = temp2.getNext();
            countCol++;
        }
        boolean lastrow = e[0]==s[0]? true:false;
        while(true) {
            if(lastrow) {
                countCol=1;
                while(countCol != e[1]) {
                    DMLLselected.addEnd(temp2.getCharacter());
                    temp2 = temp2.getNext();
                    countCol++;
                }
                break;
            }
            else {
                while(temp2 != null) {
                    DMLLselected.addEnd(temp2.getCharacter());
                    temp2 = temp2.getNext();
                }
            }
            temp=temp.getDown();
            temp2 = temp.getRight();
            countRow++;
            if(countRow == e[0]) lastrow= true;
        }
        return DMLLselected;
    }

    public RowNode getHead() {
        return head;
    }
}
