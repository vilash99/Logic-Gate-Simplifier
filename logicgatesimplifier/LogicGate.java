package logicgatesimplifier;


public class LogicGate
{
    public static final byte MAX_VARIABLE = 4;
    /** Postfix notation of user expression; it is easy to do calculation in
     postfix notation rather in infix. */
    protected String postfixExp;

    /** Unique variables in boolean expression. */
    protected char variable[];

    /** Unique variables count in boolean expression. */
    protected int varcount;

    /** (terms = 2^varcount)
    * Number of combinations in Truth Table. */
    protected int terms;

    /** Result after procession all combination in Truth Table. */
    protected boolean result[];

    /** Result counter. */
    protected int result_cntr;

    /** SOP(Sum of Product) form by kMap. */
    protected String mintermPairs[];

    /** POS(Product of Sum) form of kMap. */
    protected String maxtermPairs[];

     /** Store pairs change counter. Value differ if new pairs start. */
    protected int pairsChange[];

    /** Unique pairs counter. */
    protected int pairs_counter;


    /** Min terms Pairs after simplifying and minimizing. */
    protected String simplifiedMintermPairs[];

    /** Max terms Pairs after simplifying and minimizing counter. */
    protected int min_simpli_pairs_cntr;

    /** Min terms Pairs after simplifying and minimizing. */
    //private String simplifiedMaxtermPairs[];

    /** Max terms Pairs after simplifying and minimizing. */
    //private int max_simpli_pairs_cntr;

    LogicGate() {
        //Maximum 8 variable is allowed
        postfixExp = "";
        variable = new char[MAX_VARIABLE];
        result = new boolean[500];
        varcount = pairs_counter = result_cntr = 0;
        mintermPairs = new String[500];
        maxtermPairs = new String[500];
        simplifiedMintermPairs = new String[500];
        pairsChange = new int[500];
    }


    public void setPostfixExpression(String op) {
        postfixExp = op;
        terms = (int)Math.pow(2, varcount);
    }
    //public void setResult(boolean op[])      {results = op.clone();}
    public void setMintermPairs(String op[]) {mintermPairs = op.clone();}
    public void setMaxtermPairs(String op[]) {maxtermPairs = op.clone();}
    public void setPairsChange(int op[])     {pairsChange = op.clone();}
    public void setPairsCounter(int op)      {pairs_counter = op;}
    public void setSimplifiedMintermPairs(String op[]) {simplifiedMintermPairs = op.clone();}
    //public void setSimplifiedMaxtermPairs(String op[]) {simplifiedMaxtermPairs = op.clone();}
    public void setMinSimplifiedPairsCounter(int op) {min_simpli_pairs_cntr = op;}
    //public void setMaxSimplifiedPairsCounter(int op) {max_simpli_pairs_cntr = op;}

    public String printPairs() {
        
//        System.out.println(postfixExp);
//
//        System.out.println("Truth value:");
//        for(int i = 0; i < result_cntr; i++) {
//            System.out.println(result[i] + " ");
//	}
//
//        System.out.println("\nPairs: ");
//        for(int i = 0; i < pairs_counter; i++) {
//            System.out.println(mintermPairs[i] + " " + maxtermPairs[i] + " -> " + pairsChange[i]);
//	}
        
        String simplified = "";
//        System.out.println("\nSimplified Expression: ");
        for(int i = 0; i < min_simpli_pairs_cntr; i++)
            simplified += simplifiedMintermPairs[i] + " + ";        
        return simplified;
    }

    //public String getPostfixExp(){return postfixExp;}
    //public char[] getVariable(){return variable;}
    public int getVarcount(){return varcount;}
    public int getTerms() {return terms;}
    public boolean[] getResults() {return result;}
    public int getResultsCounter() {return result_cntr;}
    //public String[] getMintermPairs() {return mintermPairs;}
    //public String[] getMaxtermPairs() {return maxtermPairs;}
    //public int[] getPairsChange() {return pairsChange;}
    public int getPairsCounter() {return pairs_counter;}
    //public String[] getSimplifiedMintermPairs() {return simplifiedMintermPairs;}
    //public int getMinSimplifiedPairsCounter() {return min_simpli_pairs_cntr;}
    //public String[] getSimplifiedMaxtermPairs() {return simplifiedMaxtermPairs;}
    //public int getMaxSimplifiedPairsCounter() {return max_simpli_pairs_cntr;}


    /** add unique new variable form expression. */
    public boolean insertVariable(char ch) {
        int i;

        /*Check for previous inserted variable*/
        for(i = 0; i < varcount; i++) {
            if(variable[i] == ch) {
                if(varcount >= MAX_VARIABLE)
                    return (false);
                else break;
            }
        }

        if(i == varcount )
            if(varcount < MAX_VARIABLE)
                variable[varcount++] = ch;
            else return (false);
        return (true);
    }


    public void processCombination() {
        char bits[] = new char[10];

        for(char a = '0'; a <= '1' && varcount >= 1; a++) {
            bits[0] = a;
            if(varcount == 1) {
                solveExpression(bits);
                continue;
            }
            for(char b = '0'; b <= '1' && varcount >= 2; b++) {
                bits[1] = b;
                if(varcount == 2) {
                    solveExpression(bits);
                    continue;
                }
                for(char c = '0'; c <= '1' && varcount >= 3; c++) {
                    bits[2] = c;
                    if(varcount == 3) {
                        solveExpression(bits);
                        continue;
                    }
                    for(char d = '0'; d <= '1' && varcount >= 4; d++) {
                        bits[3] = d;
                        if(varcount == 4) {
                            solveExpression(bits);
                            continue;
                        }
                        for(char e = '0'; e <= '1'  && varcount >= 5; e++) {
                            bits[4] = e;
                            if(varcount == 5) {
                                solveExpression(bits);
                                continue;
                            }
                            for(char f = '0'; f <= '1' && varcount >= 6; f++) {
                                bits[5] = f;
                                if(varcount == 6) {
                                    solveExpression(bits);
                                    continue;
                                }

                                for(char g = '0'; g <= '1' && varcount >= 7; g++) {
                                    bits[6] = g;
                                    if(varcount == 7) {
                                        solveExpression(bits);
                                        continue;
                                    }
                                    for(char h = '0'; h <= '1' && varcount >= 8; h++) {
                                        bits[7] = h;
                                        if(varcount == 8) {
                                            solveExpression(bits);
                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private void solveExpression(char bits[]) {
        boolean resArray[] = new boolean[100];
        int res_index = 0;
        int var_index, i;


        for(i = 0; i < postfixExp.length(); i++) {
            if(is_operator(postfixExp.charAt(i)) == false) {	//variable found
                var_index = getVariableIndex(postfixExp.charAt(i));
                if(bits[var_index] == '1')
                        resArray[res_index++] = true;
                else resArray[res_index++] = false;
            }
            else { //operator found
                if(postfixExp.charAt(i) == '!') {
                        resArray[res_index-1] = NOT(resArray[res_index-1]) ;
                        continue;
                }
                else if(postfixExp.charAt(i) == '*')
                    resArray[res_index-2] = AND(resArray[res_index-2], resArray[res_index-1]);
                else if(postfixExp.charAt(i) == '+')
                    resArray[res_index-2] = OR(resArray[res_index-2], resArray[res_index-1]);
                else if(postfixExp.charAt(i) == '#')
                    resArray[res_index-2] = NOR(resArray[res_index-2], resArray[res_index-1]);
                else if(postfixExp.charAt(i) == '@')
                    resArray[res_index-2] = NAND(resArray[res_index-2], resArray[res_index-1]);
                else if(postfixExp.charAt(i) == '$')
                    resArray[res_index-2] = XOR(resArray[res_index-2], resArray[res_index-1]);
                else if(postfixExp.charAt(i) == '%')
                    resArray[res_index-2] = XNOR(resArray[res_index-2], resArray[res_index-1]);

                res_index--;	//move back to array
            }
        }
        result[result_cntr++] = resArray[res_index-1];
    }


    private boolean is_operator(char ch) {
        String tmp = ch + "";
        int i;
        for(i = 0; i < LOGICGATES.GATESCOUNT; i++) {
            if(tmp.equals(LOGICGATES.MYGATESOP[i]))
                    return true;
        }
        return false;
    }

    private int getVariableIndex(char ch) {
        for(int i = 0; i < variable.length; i++) {
            if(variable[i] == ch)
                return i;
        }
        return 0;			//TODO: could it be happend?
    }

    /*Logic gate operator*/
    private boolean AND(boolean a, boolean b) {
        return (a&b);
    }

    private boolean OR(boolean a, boolean b) {
        return (a|b);
    }

    private boolean NOT(boolean a) {
        return (!a);
    }

    private boolean NAND(boolean a, boolean b) {
        return !(a&b);
    }

    private boolean NOR(boolean a, boolean b) {
        return !(a|b);
    }

    private boolean XOR(boolean a, boolean b) {
        return (a^b);
    }

    private boolean XNOR(boolean a, boolean b) {
        return !(a^b);
    }


    /**Simplify min term pairs. */
    public void simplifyExpression() {        
        
        if(getPairsCounter() == 1) {
            simplifiedMintermPairs[min_simpli_pairs_cntr++] = mintermPairs[0];
            return;
        }
            
        int i = 0;
        int prev_change;                        //previous pair change counter

        String tmpPair[] = new String[20];

        int prev_change_index = i;
        prev_change = pairsChange[i];
        int tmpPair_cntr = 0;

        tmpPair[tmpPair_cntr++] = mintermPairs[i];

        for(i = 1; i < pairs_counter; i++) {
            if(prev_change != pairsChange[i]) {
                if(prev_change_index == i-1) {       //only one change (prime implicants)
                    /*It is guaranteed that all prime implicants will come first*/
                    simplifiedMintermPairs[min_simpli_pairs_cntr++] = tmpPair[0];
                    tmpPair_cntr = 0;
                    prev_change = pairsChange[i];
                    prev_change_index = i;
                }
                else {
                    simplifyExpression(tmpPair, 0, tmpPair_cntr-1);

                    //it is guaranteed that all pairs should simplified in one expression
                    simplifiedMintermPairs[min_simpli_pairs_cntr++] = tmpPair[0];
                    prev_change = pairsChange[i];
                    prev_change_index = i;
                    tmpPair_cntr = 0;
                }
            }
            tmpPair[tmpPair_cntr++] = mintermPairs[i];
        }

        //remaining pairs
        simplifyExpression(tmpPair, 0, tmpPair_cntr-1);
        simplifiedMintermPairs[min_simpli_pairs_cntr++] = tmpPair[0];


        //further simplified
        String tmpResult;
        boolean changeFound = true;
        int j, k;
        while(changeFound != false) {
            changeFound = false;
            for(i = 0; i < min_simpli_pairs_cntr; i++) {
                for(j = i+1; j < min_simpli_pairs_cntr; j++) {
                    tmpResult = simplifies(simplifiedMintermPairs[i], simplifiedMintermPairs[j]);
                    if(!tmpResult.equals("null")) {
                        simplifiedMintermPairs[i] = tmpResult;
                        changeFound = true;
                        //delete processed pair
                        for(k = j; k < min_simpli_pairs_cntr-1; k++)
                            simplifiedMintermPairs[k] = simplifiedMintermPairs[k+1];
                        min_simpli_pairs_cntr--;
                        j--;    //go back, new pair is added
                    }
                }
            }
        }
    }


    private void simplifyExpression(String tmpPair[], int startIndex, int endIndex) {
        int i, j;
        while(startIndex <= endIndex) {
            for(i = startIndex, j = 0; i <= Integer.valueOf(endIndex/2); i++, j+=2) {
                tmpPair[i] = simplifies(tmpPair[j], tmpPair[j+1]);
            }
            endIndex = (endIndex/2)-1;
        }
    }

    private String simplifies(String exp1, String exp2)
    {
        String myResult = "";
        int i, j, changeIndex, len1, len2;
        try {
            len1 = exp1.length();
            len2 = exp2.length();
        } catch (NullPointerException e) {
            return "null";
        }
        int max_len = Math.max(len1, len2);

        if(Math.abs(len1-len2) != 1) return "null";

        changeIndex = -1;            //is any change found

        for(i = 0, j = 0; i < max_len && j < max_len; i++, j++) {
            if(exp1.charAt(i) != exp2.charAt(j) && changeIndex == -1) {
                if(exp1.charAt(i) == '!') {
                    changeIndex = i;
                    i++;
                }
                else if(exp2.charAt(j) == '!') {
                    changeIndex = j;
                    j++;
                }
                else  return "null";
            }
            else if(exp1.charAt(i) != exp2.charAt(j)) return "null";

            myResult += exp1.charAt(i);
        }

        //add remaining character
        if(i < len1)
            myResult += exp1.charAt(i);
        else if(j < len2)
            myResult += exp2.charAt(j);

        //successfully checked all variables
        //remove variable in which change occurred
        max_len = myResult.length();

        if(changeIndex == max_len-1)
            myResult = myResult.substring(0, changeIndex-1);
        else if(changeIndex == 0)
            myResult = myResult.substring(changeIndex+2, max_len);
        else //middle
            myResult = myResult.substring(0, changeIndex-1) + myResult.substring(changeIndex+1, max_len);
        return myResult;
    }
}
