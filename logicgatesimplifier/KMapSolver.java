package logicgatesimplifier;
import java.util.Arrays;

class KMapSolver extends LogicGate
{
    /**Total number of variable can be simplified in boolean expression
     using KMap. */


    /*Value for perticuler cell in KMAP 2D array*/
    private class KMap {
        private String minterm, maxterm;
        private boolean flag;
        private boolean paired;					//is paired with any other cells

        KMap() {
            minterm = maxterm = "";
            flag = paired = false;
        }

        private void setFlag(boolean flg) { flag = flg; }
        private void setMinterm(String mTerm) { minterm = mTerm; }
        private void setMaxterm(String mTerm) { maxterm = mTerm; }
        private void setPaired(boolean flg) { paired = flg; }

        private boolean getFlag() { return flag; }
        private String getMinterm() { return minterm; }
        private String getMaxterm() { return maxterm; }
        private boolean getPaired() { return paired; }

        private boolean isPrimeImplicants(int row, int col) {
                return !(hasRightPair(row, col) | hasLeftPair(row, col) | hasTopPair(row, col) | hasBottomPair(row, col));
        }
    }


    private int y_terms;										//KMap rows
    private int x_terms;										//KMap columns

    private int change_counter;
    private int remainPairedCount;

    /*For component matching*/
    /*These arrays are used to get row and column numbers of 4 neighbours
    of a given cell*/
                                //  N, S, W, E
    static final int rowNbr[] = {-1, 1, 0, 0};
    static final int colNbr[] = {0,  0, -1, 1};
    int pairtyCount;											//total connected components found

    /*
    Description about x and y terms
            \y = 4
        x = 2 +----------+
              |0| 2| 6| 4|
              +----------+
              |1| 3| 7| 5|
              +----------+
    */

    private KMap myMap[][];

    public KMapSolver() {
         change_counter = remainPairedCount = 0;
    }

    public void setResult(boolean res[]) {
        result = res.clone();
    }

    private void setRemainPairedCount(int cnt) { remainPairedCount = cnt; }
    private int getRemainPairedCount() { return remainPairedCount; }

    public void pairKMap() {
        int row, col;

        initlizeTerms();
        setKMap();

        /* Printing KMap
        for(row = 0; row < x_terms; row++) {
            for(col = 0; col < y_terms; col++)
                System.out.print(myMap[row][col].getFlag() + " ");
            System.out.println("");
        }*/

        //Find prime implicants
        for(row = 0; row < x_terms; row++) {
            for(col = 0; col < y_terms; col++) {
                if(myMap[row][col].getFlag() == true && myMap[row][col].getPaired() == false &&
                    myMap[row][col].isPrimeImplicants(row, col) == true) {
                    mintermPairs[pairs_counter++] = myMap[row][col].getMinterm();
                    maxtermPairs[pairs_counter-1] = myMap[row][col].getMaxterm();
                    pairsChange[pairs_counter-1] = ++change_counter;
                    myMap[row][col].setPaired(true);
                }
            }
        }

        /*Find connected components*/
        getConnectedComponents();						//find MyMap connected components

        /* Print pairs
        for(int i = 0; i < pairs_counter; i++) {
            System.out.println(mintermPairs[i] + " " + maxtermPairs[i] + " -> " + pairsChange[i]);
        }*/
    }

    private void initlizeTerms() {
        x_terms = (int)(getVarcount()/2);
        y_terms = getVarcount() - x_terms;
        x_terms = 2 << (x_terms-1) ;
        y_terms = 2 << (y_terms-1);

        myMap = new KMap[x_terms][y_terms];
        for(int i = 0; i < x_terms; i++) {
            for(int j = 0; j < y_terms; j++) {
                    myMap[i][j] = new KMap();
            }
        }
    }

    public void setKMap()
    {
        if(getVarcount() == 2) {
            int tmp[][] = {{0, 2}, {1, 3}};
            assignArray(tmp);
        }
        else if(getVarcount() == 3) {
            int tmp[][] = {{0, 2, 6, 4}, {1, 3, 7, 5}};
            assignArray(tmp);
        }
        else if(getVarcount() == 4) {
            int tmp[][] = {{0, 4, 12, 8}, {1, 5, 13, 9}, {3, 7, 15, 11}, {2, 6, 14, 10}};
            assignArray(tmp);
        }
        else if(getVarcount() == 5) {
            int tmp[][] = {{0, 4, 12, 8, 16, 20, 28, 24}, {1, 5, 13, 9, 17, 21, 29, 25}, {3, 7, 15, 11, 19, 23, 31, 27},
                    {2, 6, 14, 10, 18, 22, 30, 26}};
            assignArray(tmp);
        }
        else if(getVarcount() == 6) {
            int tmp[][] = {{0, 8, 24, 16, 32, 40, 56, 48}, {1, 9, 25, 17, 33, 41, 57, 49}, {3, 11, 27, 19, 35, 43, 59, 51},
                    {2, 10, 26, 18, 34, 42, 58, 50}, {4, 12, 28, 20, 36, 44, 60, 52}, {5, 13, 29, 21, 37, 45, 61, 53},
                    {7, 15, 31, 23, 39, 47, 63, 55}, {6, 14, 30, 22, 38, 46, 62, 54}};
            assignArray(tmp);
        }
        else if(getVarcount() == 7) {
            int tmp[][] = {{0, 8, 24, 16, 32, 40, 56, 48, 64, 72, 88, 80, 96, 104, 120, 112},
                    {1, 9, 25, 17, 33, 41, 57, 49, 65, 73, 89, 81, 97, 105, 121, 113},
                    {3, 11, 27, 19, 35, 43, 59, 51, 67, 75, 91, 83, 99, 107, 123, 115},
                    {2, 10, 26, 18, 34, 42, 58, 50, 66, 74, 90, 82, 98, 106, 122, 114},
                    {4, 12, 28, 20, 36, 44, 60, 52, 68, 76, 92, 84, 100, 108, 124, 116},
                    {5, 13, 29, 21, 37, 45, 61, 53, 69, 77, 93, 85, 101, 109, 125, 117},
                    {7, 15, 31, 23, 39, 47, 63, 55, 71, 79, 95, 87, 103, 111, 127, 119},
                    {6, 14, 30, 22, 38, 46, 62, 54, 70, 78, 94, 86, 102, 110, 126, 118}};
            assignArray(tmp);
        }
        else if(getVarcount() == 8) {
            int tmp[][] = {{0, 16, 48, 32, 64, 80, 112, 96, 128, 144, 176, 160, 192, 208, 240, 224},
                    {1, 17, 49, 33, 65, 81, 113, 97, 129, 145, 177, 161, 193, 209, 241, 225},
                    {3, 19, 51, 35, 67, 83, 115, 99, 131, 147, 179, 163, 195, 211, 243, 227},
                    {2, 18, 50, 34, 66, 82, 114, 98, 130, 146, 178, 162, 194, 210, 242, 226},
                    {4, 20, 52, 36, 68, 84, 116, 100, 132, 148, 180, 164, 196, 212, 244, 228},
                    {5, 21, 53, 37, 69, 85, 117, 101, 133, 149, 181, 165, 197, 213, 245, 229},
                    {7, 23, 55, 39, 71, 87, 119, 103, 135, 151, 183, 167, 199, 215, 247, 231},
                    {6, 22, 54, 38, 70, 86, 118, 102, 134, 150, 182, 166, 198, 214, 246, 230},
                    {8, 24, 56, 40, 72, 88, 120, 104, 136, 152, 184, 168, 200, 216, 248, 232},
                    {9, 25, 57, 41, 73, 89, 121, 105, 137, 153, 185, 169, 201, 217, 249, 233},
                    {11, 27, 59, 43, 75, 91, 123, 107, 139, 155, 187, 171, 203, 219, 251, 235},
                    {10, 26, 58, 42, 74, 90, 122, 106, 138, 154, 186, 170, 202, 218, 250, 234},
                    {12, 28, 60, 44, 76, 92, 124, 108, 140, 156, 188, 172, 204, 220, 252, 236},
                    {13, 29, 61, 45, 77, 93, 125, 109, 141, 157, 189, 173, 205, 221, 253, 237},
                    {15, 31, 63, 47, 79, 95, 127, 111, 143, 159, 191, 175, 207, 223, 255, 239},
                    {14, 30, 62, 46, 78, 94, 126, 110, 142, 158, 190, 174, 206, 222, 254, 238}};
            assignArray(tmp);
        }
    }


    /**Assign array to KMap. */
    private void assignArray(int tmp[][]) {
        int i, j;
        String tmpBinary, tmpMinterm, tmpMaxterm;
        for(i = 0; i < x_terms; i++) {
            for(j = 0; j < y_terms; j++) {
                tmpBinary = toBinaryChar(tmp[i][j], 2);		//convert decimal to binary

                tmpMinterm = tmpMaxterm = "";
                int k = 0;

                if(getVarcount() > 1) {
                    if(tmpBinary.charAt(k) == '0') {
                        tmpMinterm = "!" + variable[k];
                        tmpMaxterm = variable[k]+"";
                    }
                    else  {
                        tmpMinterm = variable[k] +"";
                        tmpMaxterm = "!" + variable[k];
                    }
                }

                for(k = 1; k < getVarcount(); k++) {
                    if(tmpBinary.charAt(k) == '0') {
                        tmpMinterm += "*!" + variable[k];
                        tmpMaxterm += "+" + variable[k];
                    }
                    else  {
                        tmpMinterm += "*" + variable[k];
                        tmpMaxterm += "+!" + variable[k];
                    }
                }

                myMap[i][j].setMinterm(tmpMinterm);
                myMap[i][j].setMaxterm(tmpMaxterm);

                myMap[i][j].setFlag(result[tmp[i][j]]);
            }
        }
    }

    private String toBinaryChar(int num, int base) {
        String tmp = "";
        while(num >= base) {
            tmp = (num % base) + tmp;
            num /= base;
        }
        if(num != 0) tmp = num + tmp;

        if(tmp.equals("")) tmp="0";

        int tmplen = tmp.length();
        if(tmplen < varcount) {
            for(int i = 0; i < (varcount - tmplen); i++)
                tmp = "0" + tmp;
        }
        return tmp;
    }


    private void getConnectedComponents() {
        // Make a bool array to mark visited cells.
        // Initially all cells are unvisited
        boolean visited[][] = new boolean[x_terms][y_terms];
        boolean components[][] = new boolean[x_terms][y_terms];		//save current connected component (all values false)
        int row, col, i;											//counter


        // Initialize count as 0 and traverse through the all cells of
        // given matrix
        for(row = 0; row < x_terms; row++) {
            for(col = 0; col < y_terms; col++) {
                if(myMap[row][col].getFlag() == true && visited[row][col] == false) { // If a cell with value 1 is not visited
                    for(i = 0; i < x_terms; i++)
                        Arrays.fill(components[i], false);	/*Initlize components array to false*/

                    pairtyCount = 0;
                    DFS(row, col, visited, components);


                        //Print matrix
//
//			System.out.println("Current component Matrix: " + pairtyCount);
//			for(int r = 0; r < x_terms; r++) {
//                          for(int c = 0; c < y_terms; c++)
//                          System.out.print(components[r][c]+ " ");
//                          System.out.println("");
//			}

                    if(pairtyCount > 1)		//1 pair (prime implicants) is already processed
                        pairConnectedComponent(components, pairtyCount);
                }
            }
        }
    }


    private void DFS(int row, int col, boolean visited[][], boolean components[][]) {
        int tmp_row, tmp_col;

        //mark this cell is visited
        visited[row][col] = true;

        components[row][col] = myMap[row][col].getFlag();
        pairtyCount++;

        //recur all connected neighbours
        for(int k = 0; k < 4; k++) {
            tmp_row = row+rowNbr[k];
            tmp_col = col+colNbr[k];

            //Make row and column circular
            if(tmp_row < 0)
                tmp_row = x_terms-1;
            else if(tmp_row >= x_terms)
                tmp_row = 0;
            if(tmp_col < 0)
                tmp_col = y_terms-1;
            else if(tmp_col >= y_terms)
                tmp_col = 0;

            if(isSafe(tmp_row, tmp_col, visited) == true)
                DFS(tmp_row, tmp_col, visited, components);
        }
    }


    private boolean isSafe(int row, int col, boolean visited[][]) {
        return (myMap[row][col].getFlag()== true && visited[row][col] == false);
    }


    private void pairConnectedComponent(boolean components[][], int myPairtyCount) {
            int col, row, pairIn = 0;
            String myPair[];

            setRemainPairedCount(myPairtyCount);
            if(myPairtyCount >= 16) pairIn = 16;
            else if(myPairtyCount >= 8) pairIn = 8;
            else if(myPairtyCount >= 4) pairIn = 4;
            else if(myPairtyCount >= 2) pairIn = 2;
            else if(myPairtyCount == 1) pairIn = 1;

            while(getRemainPairedCount() > 0 && pairIn > 0) {
                for(row = 0; row < x_terms && myPairtyCount > 0; row++) {
                    for(col = 0; col < y_terms && myPairtyCount > 0; col++) {
                        if(components[row][col] == true && myMap[row][col].getPaired() == false) {
                                if(pairIn == 16) {
                                    myPair = pairOf_16(row, col);
                                    if(myPair[0] != null && myPair[0].equals("empty") == false) {
                                        //Incriment pair counter
                                        ++change_counter;
                                        for(int i = 0; myPair[i] != null && !myPair[i].equals("empty") ; i++) {
                                            mintermPairs[pairs_counter++] = myPair[i].substring(0, myPair[i].indexOf('?'));
                                            maxtermPairs[pairs_counter-1] = myPair[i].substring(myPair[i].indexOf('?')+1, myPair[i].length());
                                            pairsChange[pairs_counter-1] = change_counter;
                                        }
                                    }
                                }
                                else if(pairIn == 8) {
                                    myPair = pairOf_8(row, col);
                                    if(myPair[0] != null && myPair[0].equals("empty") == false) {
                                        //System.out.println("8 Pairs");
                                        ++change_counter;

                                        for(int i = 0; myPair[i] != null && !myPair[i].equals("empty") ; i++) {
                                            mintermPairs[pairs_counter++] = myPair[i].substring(0, myPair[i].indexOf('?'));
                                            maxtermPairs[pairs_counter-1] = myPair[i].substring(myPair[i].indexOf('?')+1, myPair[i].length());
                                            pairsChange[pairs_counter-1] = change_counter;
                                        }
                                    }
                                }
                                else if(pairIn == 4) {
                                    myPair = pairOf_4(row, col);
                                    if(myPair[0] != null && myPair[0].equals("empty") == false) {
                                        //System.out.println("4 Pairs");
                                        ++change_counter;

                                        for(int i = 0; myPair[i] != null && !myPair[i].equals("empty") ; i++) {
                                        //for(int i = 0; i < 4; i++) {
                                            mintermPairs[pairs_counter++] = myPair[i].substring(0, myPair[i].indexOf('?'));
                                            maxtermPairs[pairs_counter-1] = myPair[i].substring(myPair[i].indexOf('?')+1, myPair[i].length());
                                            pairsChange[pairs_counter-1] = change_counter;
                                        }
                                    }
                                }
                                else if(pairIn == 2) {
                                    myPair = pairOf_2(row, col);
                                    if(myPair[0] != null && myPair[0].equals("empty") == false) {
                                        //System.out.println("2 Pairs");
                                        ++change_counter;

                                        for(int i = 0; myPair[i] != null && !myPair[i].equals("empty") ; i++) {
                                                mintermPairs[pairs_counter++] = myPair[i].substring(0, myPair[i].indexOf('?'));
                                                maxtermPairs[pairs_counter-1] = myPair[i].substring(myPair[i].indexOf('?')+1, myPair[i].length());
                                                pairsChange[pairs_counter-1] = change_counter;
                                        }
                                    }
                                }
                                else if(pairIn == 1) {  //Pair with already paired cell
                                    myPair = pairOf_1(row, col);
                                    if(myPair[0] != null && myPair[0].equals("empty") == false) {
                                        //System.out.println("1 Pairs");
                                        ++change_counter;

                                        for(int i = 0; myPair[i] != null && !myPair[i].equals("empty") ; i++) {
                                                mintermPairs[pairs_counter++] = myPair[i].substring(0, myPair[i].indexOf('?'));
                                                maxtermPairs[pairs_counter-1] = myPair[i].substring(myPair[i].indexOf('?')+1, myPair[i].length());
                                                pairsChange[pairs_counter-1] = change_counter;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    pairIn >>= 1;
                    /*special condtion when another paired cells had paired with anathor paired cell in which any cell didnot
                    paired, then it did not minimize, so this conditon occured
                    ex.

                            \y = 4
                        x = 2 +---------------+
                            |0  | 0 |(1)| 0 |
                            +---------------+
                            |0  | 0 |(1)| 0 |
                            +---------------+
                            |(1)|(1)|(1)|[1]|		//square bracketed could not minimize, due to back values
                            +---------------+
                            |0  | 0 |(1)| 0 |
                            +---------------+
                    */
            }
    }


    private boolean getCellFlag(int row, int col, boolean ignoreVisit)    //noVisit = true for do not check visited
    {
        if(ignoreVisit) {
            if(myMap[row][col].getFlag()) return true;
        }
        else {
            if(myMap[row][col].getFlag() == true && myMap[row][col].getPaired() == false)
                return true;
        }
        return false;
    }

    private void setVisited(int row, int col) {
        //Check if already visited
        //if not then visit it and decrement counter;
        if(!myMap[row][col].getPaired()) {
            myMap[row][col].setPaired(true);
            setRemainPairedCount(getRemainPairedCount()-1);
        }
    }


    /*Make circuler 2D array*/
    private int absRow(int row) {
        if(row >= x_terms)
            row = Math.abs(x_terms-row);
        else if(row < 0) row = Math.abs(row);
        return row;
    }

    private int absColumn(int col) {
        if(col >= y_terms)
            col = Math.abs(col-y_terms);
        else if(col < 0) col = Math.abs(col);
        return col;
    }


    private String[] pairFinder(int row, int col, int maxColumn, int maxRow, boolean ignorePaired) {
        int i, j;
        boolean myFlag = true;
        String tmpPair[] = new String[20];		//Maximum pairs
        int tmpPairCntr = 0;

        //row 0, col = 2, maxColumn = 1, maxRow = 4
        for(i = 0, j = 0; i < maxColumn && j < maxRow; i++) {
            myFlag &= getCellFlag(absRow(row+j), absColumn(col+i), ignorePaired);
            if(myFlag == false) break;
            if(i == maxColumn-1) {j++; i = -1;}
        }

        if(myFlag == true) {    //found pair
            for(i = 0, j = 0; i < maxColumn && j < maxRow; i++) {
                setVisited(absRow(row+j), absColumn(col+i));
                tmpPair[tmpPairCntr++] = myMap[absRow(row+j)][absColumn(col+i)].getMinterm() + "?" +
                                                                    myMap[absRow(row+j)][absColumn(col+i)].getMaxterm();
                if(i == maxColumn-1) {j++; i = -1;}
            }
            return tmpPair;
        }

        tmpPair[0] = "empty";
        return tmpPair;
    }

    /*Pair with already paired cell*/
    private String[] pairOf_1(int row, int col)
    {
        String tmpPair[] = new String[16];
        Arrays.fill(tmpPair, "empty");

        setVisited(row, col);

        //set minterm and maxterm together seperete with ? symbol
        tmpPair[0] = myMap[row][col].getMinterm()  + "?" + myMap[row][col].getMaxterm();

        //right
        if(getCellFlag(row, absColumn(col+1), true) == true) {
            setVisited(row, absColumn(col+1));
            tmpPair[1] = myMap[row][absColumn(col+1)].getMinterm() + "?" + myMap[row][absColumn(col+1)].getMaxterm();
            return tmpPair;
        }
        //top
        else if(getCellFlag(absRow(row+1), col, true) == true) {
            setVisited(absRow(row+1), col);
            tmpPair[1] = myMap[absRow(row+1)][col].getMinterm() + "?" + myMap[absRow(row+1)][col].getMaxterm();
            return tmpPair;
        }
        //left
        else if(getCellFlag(row, absColumn(col-1), true) == true) {
            setVisited(row, absColumn(col-1));
            tmpPair[1] = myMap[row][absColumn(col-1)].getMinterm() + "?" + myMap[row][absColumn(col-1)].getMaxterm();
            return tmpPair;
        }

        //bottom (definetly found)
        setVisited(absRow(row-1), col);
        tmpPair[1] = myMap[absRow(row-1)][col].getMinterm() + "?" + myMap[absRow(row-1)][col].getMaxterm();
        return tmpPair;
    }

    private String[] pairOf_2(int row, int col)
    {
            String tmpPair[] = new String[16];
            String secondPair[] = new String[16];
            Arrays.fill(tmpPair, "empty");
            Arrays.fill(secondPair, "empty");

            //horizontal
            if(y_terms >= 2)
                    tmpPair = pairFinder(row, col, 2, 1, false);	//paired cells will not be ignored

            if(!tmpPair[0].equals("empty")) {
                    /*find another pair which is paired with other but can also be paired
                        with current pair*/

                    //true, ignore paired, go 2 step back to pair of 2

                    //look backward
                    if(y_terms > 2)
                            secondPair = pairFinder(row, absColumn(col-2), 2, 1, true);

                    //look forward horizontal
                    if(secondPair[0].equals("empty") && y_terms > 2)
                            secondPair = pairFinder(row, absColumn(col+2), 2, 1, true);

                    //look upword horizontal
                    if(secondPair[0].equals("empty"))
                            secondPair = pairFinder(absRow(row-1), col, 2, 1, true);

                    //look down horizontal
                    if(secondPair[0].equals("empty"))
                            secondPair = pairFinder(absRow(row+1), col, 2, 1, true);

                    tmpPair[2] = secondPair[0];						//first value is default value or null

                    if(!secondPair[0].equals("empty"))  //Make pair of 4 (quad)
                            tmpPair[3] = secondPair[1];

                    return tmpPair;
            }


            //vertical
            if(x_terms >= 2)
                    tmpPair = pairFinder(row, col, 1, 2, false);

            if(!tmpPair[0].equals("empty")) {
                    //look up
                    if(x_terms > 2)
                            secondPair = pairFinder(absRow(row-2), col, 1, 2, true);

                    //look backword
                    if(secondPair[0].equals("empty"))
                            secondPair = pairFinder(row, absColumn(col-1), 1, 2, true); //vertical

                    tmpPair[2] = secondPair[0];						//first value is default value or null

                    if(!secondPair[0].equals("empty"))  //Make pair of 4 (quad)
                            tmpPair[3] = secondPair[1];
            }
            return tmpPair;
    }

    private String[] pairOf_4(int row, int col)
    {
            String tmpPair[] = new String[16];
            String secondPair[] = new String[16];
            Arrays.fill(tmpPair, "empty");
            Arrays.fill(secondPair, "empty");

            //horizontal
            if(y_terms >= 4)
                    tmpPair = pairFinder(row, col, 4, 1, false);

            if(!tmpPair[0].equals("empty")) {
                    //look backward horizontal
                    if(y_terms > 4)
                            secondPair = pairFinder(row, absColumn(col-4), 4, 1, true);

                    //look forward horizontal
                    if(secondPair[0].equals("empty") && y_terms > 4)
                            secondPair = pairFinder(row, absColumn(col+4), 4, 1, true);

                    if(secondPair[0].equals("empty"))
                            secondPair = pairFinder(absRow(row-1), col, 4, 1, true);    //vertical

                    //look down horizontal
                    if(secondPair[0].equals("empty"))
                            secondPair = pairFinder(absRow(row+1), col, 4, 1, true);

                    tmpPair[4] = secondPair[0];						//first value is default value or null

                    if(!secondPair[0].equals("empty"))  {//Make pair of 8 (octate)
                            int i, j;
                            for(i = 1, j = 5; i < 4; i++, j++)
                                    tmpPair[j] = secondPair[i]; //remaing bunch
                    }
                    return tmpPair;
            }

            //vertical
            if(x_terms >= 4) {
                    tmpPair = pairFinder(row, col, 1, 4, false);
                    if(!tmpPair[0].equals("empty")) {
                            //look up vertical
                            if(x_terms > 4)
                                    secondPair = pairFinder(absRow(row-4), col, 1, 4, true);

                                    //look backword
                            if(secondPair[0].equals("empty"))
                                    secondPair = pairFinder(row, absColumn(col-1), 1, 4, true); //vertical

                            tmpPair[4] = secondPair[0];						//first value is default value or null

                            if(!secondPair[0].equals("empty"))  {//Make pair of 8 (octate)
                                    int i, j;
                                    for(i = 1, j = 5; i < 4; i++, j++)
                                            tmpPair[j] = secondPair[i]; //remaing bunch
                }
                return tmpPair;
            }
            }

            //2 horizontal, 2 vertical each
            tmpPair = pairFinder(row, col, 2, 2, false);
            if(!tmpPair[0].equals("empty")) {
                    //find same in backword
                    if(x_terms > 2)
                            secondPair = pairFinder(row, absColumn(col-2), 2, 2, true);

                    if(secondPair[0].equals("empty") && y_terms > 4)
                            //find same in up
                            secondPair = pairFinder(absRow(row-2), col, 2, 2, true);

                    tmpPair[4] = secondPair[0];						//first value is default value or null

                    if(!secondPair[0].equals("empty"))  {//Make pair of 16
                            int i, j;
                            for(i = 1, j = 5; i < 4; i++, j++)
                                    tmpPair[j] = secondPair[i]; //remaing bunch
                    }
            }

            return tmpPair;		//null condition will be check in value catcher
    }


    /*Pair of 8 can be make pair of 16*/
    private String[] pairOf_8(int row, int col)
    {
            String tmpPair[] = new String[16];
            String secondPair[] = new String[16];
            Arrays.fill(tmpPair, "empty");
            Arrays.fill(secondPair, "empty");

            //horizontal
            if(y_terms >= 8)
                    tmpPair = pairFinder(row, col, 8, 1, false);

            if(!tmpPair[0].equals("empty")) {		//Pair found
                    //look backward
                    if(y_terms > 8)	//16 pair can be made with 8 only if there are columns greater than 8
                            secondPair = pairFinder(row, absColumn(col-8), 8, 1, true);

                    if(secondPair[0].equals("empty") && y_terms > 8)	//16 pair can be made with 8 only if there are columns greater than 8
                            secondPair = pairFinder(row, absColumn(col+8), 8, 1, true);

                    if(secondPair[0].equals("empty"))	//did not found pair in upword horizontal row
                            secondPair = pairFinder(absRow(row-1), col, 8, 1, true);    //vertical

                    if(secondPair[0].equals("empty"))	//did not found pair in upword horizontal row
                            secondPair = pairFinder(absRow(row+1), col, 8, 1, true);    //vertical

                    tmpPair[8] = secondPair[0];						//first value is default value or null

                    if(!secondPair[0].equals("empty"))  {//Make pair of 16
                            int i, j;
                            for(i = 1, j = 9; i < 8; i++, j++)
                                    tmpPair[j] = secondPair[i]; //remaing bunch
                    }
                    return tmpPair;
            }

            //vertical
            if(x_terms >= 8) {
                    tmpPair = pairFinder(row, col, 1, 8, false);

                    //vertical pair found, look for pair of 16
                    if(!tmpPair[0].equals("empty")) {
                            //look up in vertical
                            if(x_terms > 8)
                                    secondPair = pairFinder(absRow(row-8), col, 1, 8, true);

                            //look backword
                            if(secondPair[0].equals("empty"))
                                    secondPair = pairFinder(row, absColumn(col-1), 1, 8, true); //vertical

                            tmpPair[8] = secondPair[0];						//first value is default value or null

                            if(!secondPair[0].equals("empty"))  {//Make pair of 16
                                    int i, j;
                                    for(i = 1, j = 9; i < 8; i++, j++)
                                            tmpPair[j] = secondPair[i]; //remaing bunch
                            }
                            return tmpPair;
                        }
            }

            //4 right and 2 down 4 right
            tmpPair = pairFinder(row, col, 4, 2, false);
            if(!tmpPair[0].equals("empty")) {
                    //find same in backword
                    if(x_terms > 4)
                            secondPair = pairFinder(absRow(row-4), col, 4, 2, true);

                    if(secondPair[0].equals("empty") && y_terms > 4)
                            //find same in up                            
                            secondPair = pairFinder(row, absColumn(col-4), 4, 2, true);

                    tmpPair[8] = secondPair[0];						//first value is default value or null

                    if(!secondPair[0].equals("empty"))  {//Make pair of 16
                            int i, j;
                            for(i = 1, j = 9; i < 8; i++, j++)
                                    tmpPair[j] = secondPair[i]; //remaing bunch
                    }

                    return tmpPair;
            }


        //2 right and 4 down 2 right
        tmpPair = pairFinder(row, col, 2, 4, false);
        if(!tmpPair[0].equals("empty")) {
            //find same in backword
            if(x_terms > 4)
                 secondPair = pairFinder(row, absColumn(col+2), 2, 4, true);
                            
            if(secondPair[0].equals("empty") && y_terms > 4)
                //find same in up
                            
                secondPair = pairFinder(row, absColumn(col-2), 2, 4, true);

                tmpPair[8] = secondPair[0];						//first value is default value or null

                if(!secondPair[0].equals("empty"))  {//Make pair of 16
                    int i, j;
                    for(i = 1, j = 9; i < 8; i++, j++)
                        tmpPair[j] = secondPair[i]; //remaing bunch
                    }
            }

            return tmpPair;
    }


    //pair of 16 can be make 8 y_terms
    private String[] pairOf_16(int row, int col)
    {
        String tmpPair[] = new String[16];
            Arrays.fill(tmpPair, "empty");

        //horizontal
        if(y_terms >= 16)
                    tmpPair = pairFinder(row, col, 16, 1, false);

            if(!tmpPair[0].equals("empty"))
                    return tmpPair;

            //vertical
            if(x_terms >= 16) {
                    tmpPair = pairFinder(row, col, 1, 16, false);
                    if(!tmpPair[0].equals("empty"))
                            return tmpPair;
            }


        //Horizontal 8 and vertical 2
        tmpPair = pairFinder(row, col, 8, 2, false);
            if(!tmpPair[0].equals("empty"))
                    return tmpPair;

            //Horizontal 4 and vertical 4
            tmpPair = pairFinder(row, col, 4, 4, false);
            if(!tmpPair[0].equals("empty"))
                    return tmpPair;

        //Horizontal 2 and vertical 8
        tmpPair = pairFinder(row, col, 2, 8, false);
            return tmpPair;
    }


    private boolean hasRightPair(int x, int y) {
        if(y == y_terms-1) y = 0;
        else y++;
        if(myMap[x][y].getFlag() == true) return true;
        return false;
    }

    private boolean hasLeftPair(int x, int y) {
        if(y == 0) y = y_terms-1;
        else y--;
        if(myMap[x][y].getFlag() == true) return true;
        return false;
    }

    private boolean hasTopPair(int x, int y) {
        if(x == 0) x = x_terms-1;
        else x--;
        if(myMap[x][y].getFlag() == true) return true;
        return false;
    }

    private boolean hasBottomPair(int x, int y) {
        if(x == x_terms-1) x = 0;
        else x++;
        if(myMap[x][y].getFlag() == true) return true;
        return false;
    }
}