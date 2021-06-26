import enigma.console.TextAttributes;
import enigma.core.Enigma;

import java.io.*;
import java.util.Scanner;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Screen {
    public enigma.console.Console cn = Enigma.getConsole("CENG Editor", 100,30, 25); //Creating console object
    public enigma.console.TextWindow cnt = cn.getTextWindow();
    public static TextAttributes att0 = new TextAttributes(Color.white, Color.black);  //foreground, background color
    public static TextAttributes att1 = new TextAttributes(Color.green, Color.black);
    public static TextAttributes att2 = new TextAttributes(Color.cyan, Color.black);
    public static TextAttributes att3 = new TextAttributes(Color.black, Color.white);
    public KeyListener klis;
    DoublyMultiLinkedList DMLL = new  DoublyMultiLinkedList();
    DoubleLinkedList DLLselection = new  DoubleLinkedList();
    int[] selectionStart = {-1,-1};
    int[] selectionEnd = {-1,-1};
    boolean pasteMode = false;
    int pasteIndex=-1;
    String toBeReplaced;
    String wordToReplace;

    // ------ Standard variables for mouse and keyboard ------
    public int keypr;   // key pressed?
    public int rkey;    // key   (for press/release)
    public int rkeymod;      // key modifiers
    public int capslock=0;   // 0:off    1:on
    // ----------------------------------------------------


    public void resetSelection() { //Resets selection
        selectionStart[0]= -1;
        selectionStart[1]= -1;
        selectionEnd[0]= -1;
        selectionEnd[1]= -1;
    }

    public void printFButton(int x, int y, String s1, String s2) { //Prints names and functions of F buttons
        cn.getTextWindow().setCursorPosition(x, y);
        System.out.println(s1 + ": " + s2);
    }

    public void printRow(int y) { //Prints edges (row) of the screen
        cnt.setCursorPosition(0, y);
        System.out.print("+");
        for (int i = 1; i < 61 ; i++) {
            if (i % 5 == 0) {
                cnt.setCursorPosition(i, y);
                System.out.print("+");
            }
            else {
                cnt.setCursorPosition(i, y);
                System.out.print("-");
            }

        }
        cnt.setCursorPosition(61, y);
        System.out.print("+");
    }

    public void printColumn(int x) { //Prints edges (column) of the screen
        for (int i = 1; i < 21 ; i++) {
            if (i % 5 == 0) {
                cnt.setCursorPosition(x, i);
                System.out.print("+");
            }
            else {
                cnt.setCursorPosition(x, i);
                System.out.print("|");
            }
        }
    }
    public void displayRow(DoublyMultiLinkedList DMLL) {  //Overwrites the text
        int x = 1;
        int y = 1;

        for (int i = 0; i < 20; i++) {
            cnt.setCursorPosition(x, y);
            System.out.print("                                                            ");
            y++;
        }

        y = 1;
        RowNode temp = DMLL.getHead();
        while (temp != null) {
            CharNode temp2 = temp.getRight();
            x = 1;
            while (temp2 != null) {
                cnt.setCursorPosition(x, y);
                System.out.print(temp2.getCharacter());
                temp2 = temp2.getNext();
                x++;
            }
            temp = temp.getDown();
            y++;
        }
    }

    public void loadText() throws FileNotFoundException { //Loads the text from the specified path
        int y = 1;
        int counter = 1;

        File file = new File("text.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            if (y != 1 || DMLL.sizeOfSelectedRow(y) != 0)
                y++;
            if (!DMLL.rowCheck(y)) {
                DMLL.addRow(y - 1);
                counter = 1;
            }

            String line = scanner.nextLine();
            String[] split = line.split(" ");


            for (int i = 0; i < split.length; i++) {
                int counterTemp = counter;
                for (int j = 0; j < split[i].length(); j++) {
                    if (counter == 61 || (counterTemp - 1 + split[i].length() > 60)) {
                        y++;
                        DMLL.addRow(y - 1);
                        counter = 1;
                        counterTemp = 1;
                    }
                    DMLL.addCharacter(y, counter, split[i].substring(j, j+1));
                    counter++;
                }

                if (counter != 61) {
                    DMLL.addCharacter(y, counter, " ");
                    counter++;
                }
            }
        }
        scanner.close();
    }

    public void saveText() throws IOException {  //Saves the text to the specified path
        File file = new File("text.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        RowNode temp = DMLL.getHead();
        while (temp != null) {
            CharNode temp2 = temp.getRight();
            while (temp2 != null) {
                writer.write(temp2.getCharacter());
                temp2 = temp2.getNext();
            }
            temp = temp.getDown();
            if (temp != null)
                writer.write("\n");
        }
        writer.close();
    }

    public void find(String dataToFind, DoublyMultiLinkedList DMLL) { //Finds wanted pattern from text
        if (dataToFind.length() != 0) {
            RowNode temp = DMLL.getHead();
            int row = 1;
            int column = 1;

            String[] chars = new String[dataToFind.length()];
            String[] realChars = new String[dataToFind.length()];
            cn.setTextAttributes(att3);
            for (int i = 0; i < dataToFind.length(); i++) {
                chars[i] = dataToFind.substring(i, i + 1);
            }
            int index = 0;
            while (temp != null) {
                CharNode temp2 = temp.getRight();
                while (temp2 != null) {
                    if (dataToFind.length() == 1 && temp2.getCharacter().equalsIgnoreCase(dataToFind)) {
                        cnt.setCursorPosition(column, row);
                        System.out.print(temp2.getCharacter());
                    }
                    else if (temp2.getCharacter().equalsIgnoreCase(chars[index])){
                        realChars[index] = temp2.getCharacter();
                        index++;
                        if (index == dataToFind.length()) {
                            for (int i = 0; i < dataToFind.length(); i++) {
                                cnt.setCursorPosition(column - dataToFind.length() + 1 + i, row);
                                System.out.print(realChars[i]);
                            }
                            index = 0;
                        }
                    }
                    else
                        index = 0;
                    temp2 = temp2.getNext();
                    if (column == 60)
                        column = 1;
                    else
                        column++;
                }
                temp = temp.getDown();
                row++;
                column = 1;
            }
            cn.setTextAttributes(att0);
        }
    }

    public void replace(String toBeReplaced, String wordToReplace, DoublyMultiLinkedList DMLL) {  //Replaces the desired pattern with another
        if (toBeReplaced.length() != 0) {
            RowNode temp = DMLL.getHead();
            int row = 1;
            int column = 1;
            int str1Length = toBeReplaced.length();
            int str2Length = wordToReplace.length();

            String[] chars1 = new String[str1Length];
            String[] chars2 = new String[str2Length];
            cn.setTextAttributes(att3);
            for (int i = 0; i < str1Length; i++) {
                chars1[i] = toBeReplaced.substring(i, i + 1);
            }
            for (int i = 0; i < str2Length; i++) {
                chars2[i] = wordToReplace.substring(i, i + 1);
            }
            int index = 0;
            while (temp != null) {
                CharNode temp2 = temp.getRight();
                while (temp2 != null) {
                    if (str1Length == 1 && str2Length == 1 && temp2.getCharacter().equalsIgnoreCase(toBeReplaced)) {
                        DMLL.returnCharNode(row, column).setCharacter(wordToReplace);
                        cnt.setCursorPosition(column, row);
                        return;
                    }
                    else if (temp2.getCharacter().equalsIgnoreCase(chars1[index])){
                        index++;
                        if (index == str1Length) {
                            if (str1Length == str2Length) {
                                for (int i = 0; i < str1Length; i++) {
                                    DMLL.returnCharNode(row, column - str1Length + 1 + i).setCharacter(chars2[i]);
                                    cnt.setCursorPosition(column - str1Length + 1 + i, row);
                                }
                            }
                            else if (str1Length > str2Length) {
                                for (int i = 0; i < str1Length - str2Length; i++) {
                                    DMLL.deleteChar(row, column - (str1Length - str2Length) + 1 + i);
                                    column--;
                                }
                                cn.setTextAttributes(att0);
                                displayRow(DMLL);
                                cn.setTextAttributes(att3);
                                for (int i = 0; i < str2Length; i++) {
                                    DMLL.returnCharNode(row, column - str2Length + 1 + i).setCharacter(chars2[i]);
                                    cnt.setCursorPosition(column - str2Length + 1 + i, row);
                                    System.out.print(chars2[i]);
                                }
                            }
                            else {
                                for (int i = 0; i < str1Length; i++) {
                                    DMLL.returnCharNode(row, column - str1Length + 1 + i).setCharacter(chars2[i]);
                                    cnt.setCursorPosition(column - str1Length + 1 + i, row);
                                }
                                for (int i = str1Length; i < str2Length; i++) {
                                    if (DMLL.sizeOfSelectedRow(row) == 60) {
                                        if (DMLL.returnCharNode(row, 60).getCharacter().equals(" "))
                                            DMLL.deleteChar(row, 60);
                                        else
                                            DMLL.headOfTheWord(row, column + 1 + (i - str1Length));
                                    }
                                    DMLL.addCharacter(row, column + 1 + (i - str1Length), chars2[i]);
                                }
                                cn.setTextAttributes(att0);
                                displayRow(DMLL);
                                cn.setTextAttributes(att3);
                            }
                            return;
                        }
                    }
                    else
                        index = 0;
                    temp2 = temp2.getNext();
                    if (column == 60)
                        column = 1;
                    else
                        column++;
                }
                temp = temp.getDown();
                row++;
                column = 1;
            }
            cn.setTextAttributes(att0);
        }
    }

    public void feedback(String feedback, TextAttributes att) throws InterruptedException { //Prints warning messages on the screen
        cn.setTextAttributes(att);
        cnt.setCursorPosition(63, 18);
        System.out.print(feedback);
        Thread.sleep(2000);
        cnt.setCursorPosition(63, 18);
        System.out.print("                           ");
        cn.setTextAttributes(att0);
    }

    Screen() throws Exception {

        klis=new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {
                if(keypr==0) {
                    keypr=1;
                    rkey=e.getKeyCode();
                    rkeymod=e.getModifiersEx();
                    if(rkey == KeyEvent.VK_CAPS_LOCK) {
                        if(capslock==0) capslock=1;
                        else capslock=0;
                    }
                }
            }
            public void keyReleased(KeyEvent e) {}
        };
        cn.getTextWindow().addKeyListener(klis);
        // ----------------------------------------------------

        int curtype;
        curtype=cnt.getCursorType();   // default:2 (invisible)       0-1:visible
        cnt.setCursorType(1);
        cn.setTextAttributes(att0);


        printRow(0);
        printColumn(0);
        printRow(21);
        printColumn(61);


        printFButton(63, 1, "F1", "Selection start");
        printFButton(63, 2, "F2", "Selection end");
        printFButton(63, 3, "F3", "Cut");
        printFButton(63, 4, "F4", "Copy");
        printFButton(63, 5, "F5", "Paste");
        printFButton(63, 6, "F6", "Find");
        printFButton(63, 7, "F7", "Replace");
        printFButton(63, 8, "F8", "Next");
        printFButton(63, 9, "F9", "Align left");
        printFButton(63, 10, "F10", "Justify");
        printFButton(63, 11, "F11", "Load");
        printFButton(63, 12, "F12", "Save");
        printFButton(63, 14, "Mode", "Insert");
        printFButton(63, 16, "Alignment", "Align Left");

        DMLL.addRow(0);

        int row = 1;
        int column = 1;

        displayRow(DMLL);
        int px=column,py=row;
        cnt.setCursorPosition(px, py);
        boolean wasLoaded = false;
        boolean insert = true;
        boolean justify = false;


        while(true) {

            if(keypr==1 || pasteMode) { // if keyboard button pressed or pasteMode is active
                if (rkey==KeyEvent.VK_INSERT) {
                    insert = !insert;
                }

                if(rkey==KeyEvent.VK_LEFT) {
                    int size = DMLL.sizeOfSelectedRow(py - 1);
                    if (size != 0 && px == 1 && py > 1) {
                        py--;
                        px = size + 1;
                    }
                    else if (px > 1){
                        px--;
                    }
                    else if (DMLL.rowCheck(py - 1) && py > 1) {
                        py--;
                        px = 1;
                    }

                }
                if(rkey==KeyEvent.VK_RIGHT && px < 62) {
                    int size = DMLL.sizeOfSelectedRow(py + 1);
                    if ((size != 0 && px == 61 || DMLL.returnData(py, px) == null) && (DMLL.returnData(py + 1, 1) != null || DMLL.rowCheck(py + 1))) {
                        py++;
                        px = 1;
                    }
                    else if (DMLL.returnData(py, px) != null)
                        px++;
                }
                if(rkey==KeyEvent.VK_UP && py > 1) {
                    int size = DMLL.sizeOfSelectedRow(py - 1);
                    if (DMLL.returnData(py - 1, px - 1) == null && size != 0) {
                        py--;
                        px = size + 1;
                    }
                    else if (DMLL.returnData(py - 1, px - 1) != null)
                        py--;
                    else if (DMLL.rowCheck(py - 1)) {
                        py--;
                        px = 1;
                    }

                }
                if(rkey==KeyEvent.VK_DOWN && py < 20) {
                    int size = DMLL.sizeOfSelectedRow(py + 1);
                    if (DMLL.returnData(py + 1, px - 1) != null)
                        py++;
                    else if (size != 0) {
                        py++;
                        px = size + 1;
                    }
                    else if (DMLL.rowCheck(py + 1)) {
                        py++;
                        px = 1;
                    }
                }

                char rckey=(char)rkey;

                if(pasteMode) {
                    rckey=Character.toUpperCase(DLLselection.getFromIndex(pasteIndex));
                    if (DLLselection.getFromIndex(pasteIndex) == rckey) capslock = 1;
                    else capslock = 0;
                    if(rckey == ' ') rkey = KeyEvent.VK_SPACE;
                    else rkey = rckey;
                }
                else rckey=(char)rkey;
                boolean sixtyOneCheck = false;

                if (!(DMLL.sizeOfSelectedRow(py) == 60 && DMLL.sizeRow() == 20) && (((char)rckey >= 41 && (char)rckey < 112) || (char)rckey == 32)) {
                    if (px == 61 || DMLL.sizeOfSelectedRow(py) == 60) {
                        if (px == 61)
                            sixtyOneCheck = true;
                        if (px != 61 && DMLL.returnData(py, 60).equals(" "))
                            DMLL.deleteChar(py, 60);
                        else if (py != 20 && DMLL.sizeRow() != 20) {

                            int pxTemp = px;

                            if (px < 61) {
                                if ((DMLL.sizeOfSelectedRow(py) == 60 && !DMLL.rowCheck(py + 1)) || (DMLL.rowCheck(py + 1) && DMLL.sizeOfSelectedRow(py + 1) == 60))
                                    DMLL.addRow(py);

                                //Moves the word to the bottom line so as not to split it
                                if (!DMLL.isFullOfLetters(py) && !DMLL.returnData(py, 60).equals(" ")) {
                                    //Returns the index of the beginning of the trailing word.
                                    int headIndex = DMLL.returnTheWordHeadIndex(py);
                                    if (px < headIndex) {
                                        DMLL.headOfTheWord(py, px);
                                        //px++;
                                    }
                                    else {
                                        px = DMLL.headOfTheWord(py, px);
                                        py++;
                                    }
                                    cnt.setCursorPosition(px, py);
                                }
                                //When the line is full, the cursor moves down if not at the end.
                                if (DMLL.isFullOfLetters(py))
                                    DMLL.fromLastToHead(py);

                            }
                            else if (px == 61) {
                                px = 1;
                                DMLL.addRow(py);
                                py++;
                            }
                            //Moves the word to the bottom line so as not to split it
                            if (pxTemp == 61 && !DMLL.returnData(py - 1, 60).equals(" ") && !DMLL.isFullOfLetters(py - 1)) {
                                px = DMLL.headOfTheWord(py - 1, pxTemp);
                                cnt.setCursorPosition(px, py);
                            }
                            cnt.setCursorPosition(px, py);
                        }

                    }
                    if (px < 61) {
                        if(rckey>='0' && rckey<='9') {
                            System.out.print(rckey);
                            if (insert || DMLL.returnData(py, px) != null || sixtyOneCheck)
                                DMLL.addCharacter(py, px, String.valueOf(rckey));
                            else
                                DMLL.returnCharNode(py, px).setCharacter(String.valueOf(rckey));
                            px++;
                        }
                        if(rckey>='A' && rckey<='Z') {


                            if(((rkeymod & KeyEvent.SHIFT_DOWN_MASK) > 0) || capslock==1) {
                                System.out.print(rckey);
                                if (insert || DMLL.returnData(py, px) == null || sixtyOneCheck)
                                    DMLL.addCharacter(py, px, String.valueOf(rckey));
                                else
                                    DMLL.returnCharNode(py, px).setCharacter(String.valueOf(rckey));
                            }

                            else {
                                System.out.print((char)(rckey+32));
                                if (insert || DMLL.returnData(py, px) == null || sixtyOneCheck)
                                    DMLL.addCharacter(py, px, String.valueOf((char)(rckey+32)));
                                else
                                    DMLL.returnCharNode(py, px).setCharacter(String.valueOf((char)(rckey+32)));
                            }
                            px++;
                        }

                        if((rkeymod & KeyEvent.SHIFT_DOWN_MASK) == 0) {
                            if(rckey=='.' || rckey==',' || rckey=='-') {
                                System.out.print(rckey);
                                if (insert || DMLL.returnData(py, px) == null || sixtyOneCheck)
                                    DMLL.addCharacter(py, px, String.valueOf(rckey));
                                else
                                    DMLL.returnCharNode(py, px).setCharacter(String.valueOf(rckey));
                                px++;
                            }

                        }
                        else {
                            if(rckey=='.') {
                                System.out.print(':');
                                rckey = ':';
                                if (insert || DMLL.returnData(py, px) == null || sixtyOneCheck)
                                    DMLL.addCharacter(py, px, String.valueOf(rckey));
                                else
                                    DMLL.returnCharNode(py, px).setCharacter(String.valueOf(rckey));
                                px++;
                            }
                            if(rckey==',') {
                                rckey = ';';
                                System.out.print(';');
                                if (insert || DMLL.returnData(py, px) == null || sixtyOneCheck)
                                    DMLL.addCharacter(py, px, String.valueOf(rckey));
                                else
                                    DMLL.returnCharNode(py, px).setCharacter(String.valueOf(rckey));
                                px++;
                            }
                        }
                    }
                }

                if (rkey==KeyEvent.VK_SPACE) {
                    if (!(DMLL.sizeOfSelectedRow(py) == 60 && DMLL.sizeRow() == 20) && px < 61) {
                        System.out.print(rckey);
                        if (insert || DMLL.returnData(py, px) != null || sixtyOneCheck)
                            DMLL.addCharacter(py, px, " ");
                        else
                            DMLL.returnCharNode(py, px).setCharacter(" ");
                        px++;
                    }
                }

                if (rkey==KeyEvent.VK_ENTER) {
                    if (DMLL.returnData(py, px) != null && DMLL.sizeRow() != 20) {
                        py++;
                        DMLL.addRow(py - 1);
                        int size = DMLL.sizeOfSelectedRow(py - 1);
                        DMLL.deleteFromTheEnd(py - 1, size - px + 1);
                        px = 1;
                        cnt.setCursorPosition(30, 25);
                    }
                    else if (py != 20 && DMLL.sizeRow() != 20) {
                        py++;
                        DMLL.addRow(py - 1);
                        px = 1;
                        cnt.setCursorPosition(px, py);
                    }

                }
                if (rkey==KeyEvent.VK_BACK_SPACE) {

                    if (px != 1) {
                        px--;
                        DMLL.deleteChar(py, px);
                        DMLL.moveUpTheWholeWord(py, px);
                    }
                    else if (py != 1) {
                        int sizeUp = DMLL.sizeOfSelectedRow(py - 1);
                        if (DMLL.sizeOfSelectedRow(py - 1) != 0) {
                            px = sizeUp + 1;
                        }
                        if (DMLL.sizeOfSelectedRow(py) == 0)
                            DMLL.deleteRow(py);
                        DMLL.copyTheRow(py, DMLL.sizeOfSelectedRow(py));
                        py--;
                    }
                }



                if (rkey==KeyEvent.VK_HOME) //top of page
                    px = 1;
                if (rkey==KeyEvent.VK_END) //end of page
                    px = DMLL.sizeOfSelectedRow(py) + 1;
                if (rkey==KeyEvent.VK_DELETE) { //Delete
                    if (DMLL.returnData(py, px) != null)
                        DMLL.deleteChar(py, px);
                }
                if (rkey==KeyEvent.VK_PAGE_UP) {
                    px = 1;
                    py = 1;
                }
                if (rkey==KeyEvent.VK_PAGE_DOWN) {
                    py = DMLL.sizeRow();
                    px = DMLL.sizeOfSelectedRow(py) + 1;
                }

                if(rkey==KeyEvent.VK_F1) { //Select Start
                    selectionStart[0]= py;
                    selectionStart[1]= px;
                }
                if(rkey==KeyEvent.VK_F2) { //Select end
                    selectionEnd[0]= py;
                    selectionEnd[1]= px;
                }
                if(rkey==KeyEvent.VK_F3) { //Cut
                    if(!(selectionStart[0] == -1 || selectionStart[1] == -1 || selectionEnd[0] == -1 || selectionEnd[1] == -1)) {
                        DLLselection = DMLL.cut(selectionStart, selectionEnd);
                        px = selectionStart[1];
                        py = selectionStart[0];
                        resetSelection();
                    }
                }
                if(rkey==KeyEvent.VK_F4) {//Copy
                    if(!(selectionStart[0] == -1 || selectionStart[1] == -1 || selectionEnd[0] == -1 || selectionEnd[1] == -1)) {
                        DLLselection = DMLL.copy(selectionStart, selectionEnd);
                        resetSelection();
                    }
                }
                if(rkey==KeyEvent.VK_F5 && DLLselection.size() != 0) { //Paste
                    pasteMode =true;
                }
                if(rkey==KeyEvent.VK_F6) { //Find
                    Scanner sc = new Scanner(System.in);
                    while (true) {
                        cn.setTextAttributes(att0);
                        cnt.setCursorPosition(0, 23);
                        System.out.print("(Type 'esc' to exit find mode)");
                        cnt.setCursorPosition(1, 22);
                        System.out.print("                                                            ");
                        cnt.setCursorPosition(1, 22);
                        System.out.print("Find: ");
                        String str = sc.nextLine();
                        if (str.equalsIgnoreCase("ESC"))
                            break;
                        displayRow(DMLL);
                        find(str, DMLL);
                    }
                    cn.setTextAttributes(att0);
                    cnt.setCursorPosition(0, 23);
                    System.out.print("                              ");
                    cnt.setCursorPosition(1, 22);
                    System.out.print("                                                            ");
                }
                if(rkey==KeyEvent.VK_F7) { //Replace
                    Scanner sc = new Scanner(System.in);
                    cn.setTextAttributes(att0);
                    cnt.setCursorPosition(1, 22);
                    System.out.print("                                  ");
                    cnt.setCursorPosition(1, 22);
                    System.out.print("To Be Replaced: ");
                    toBeReplaced = sc.nextLine();
                    cnt.setCursorPosition(1, 22);
                    System.out.print("                                      ");
                    if (toBeReplaced.equalsIgnoreCase("ESC")) {
                        keypr = 0;
                        continue;
                    }
                    cnt.setCursorPosition(1, 22);
                    System.out.print("Word To Replace: ");
                    wordToReplace = sc.nextLine();
                    cnt.setCursorPosition(1, 22);
                    System.out.print("                                      ");
                    if (wordToReplace.equalsIgnoreCase("ESC")) {
                        keypr = 0;
                        continue;
                    }
                    replace(toBeReplaced,wordToReplace,DMLL);
                    Thread.sleep(100);
                    cn.setTextAttributes(att0);
                }
                if(rkey==KeyEvent.VK_F8 && wordToReplace!= null) { //Next
                    replace(toBeReplaced,wordToReplace,DMLL);
                    Thread.sleep(100);
                    cn.setTextAttributes(att0);
                }
                if(rkey==KeyEvent.VK_F9) {//Align left
                    if (justify) {
                        justify = false;
                        printFButton(63, 16, "Alignment", "          ");
                        printFButton(63, 16, "Alignment", "Align Left");
                    }
                    DMLL.alignLeft();
                    displayRow(DMLL);
                }
                if(rkey==KeyEvent.VK_F10) { //Justify
                    if (!justify) {
                        justify = true;
                        printFButton(63, 16, "Alignment", "          ");
                        printFButton(63, 16, "Alignment", "Justify");
                    }
                    DMLL.justify();
                    displayRow(DMLL);
                }
                if(rkey==KeyEvent.VK_F11) { //Loads from file
                    String feedback;
                    if (!wasLoaded) {
                        loadText();
                        displayRow(DMLL);
                        wasLoaded = true;
                        feedback = "The file has loaded";
                    }
                    else
                        feedback = "The file has already loaded";
                    feedback(feedback, att2);
                }

                if(rkey==KeyEvent.VK_F12) { //Saves to file
                    saveText();
                    String feedback = "The file has saved";
                    feedback(feedback, att1);
                }
                if(pasteMode) {
                    if(DLLselection.size() == pasteIndex+1) {
                        pasteMode = false;
                        pasteIndex=-1;
                        capslock=0;
                    }
                    else pasteIndex++;
                }



                cnt.setCursorPosition(0, 23);
                System.out.print("                                                            ");
                cnt.setCursorPosition(0, 23);
                cnt.setCursorPosition(66, 23);
                System.out.print("                             ");
                cnt.setCursorPosition(66, 23);
                cnt.setCursorPosition(66, 25);
                displayRow(DMLL);

                if (insert) {
                    printFButton(63, 14, "Mode", "         ");
                    printFButton(63, 14, "Mode", "Insert");
                }
                else {
                    printFButton(63, 14, "Mode", "Overwrite");
                }
                cnt.setCursorType(1);

                cnt.setCursorPosition(px, py);

                keypr=0;
            }

            if(!pasteMode) Thread.sleep(20);
        }
    }
}