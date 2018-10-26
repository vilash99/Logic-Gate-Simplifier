package logicgatesimplifier;

/**Entering and validating boolean expression
 * User enter rough expression and this class will convert it in machin 
 * readable form and convert it in postfix notation for further calculation. */
class LogicExpression extends KMapSolver
{
    /**User entered boolean expression. */
    private String roughExp;
    
    /** program compatible expression with appropriate boolean symbols. */
    private String myExp;

    public String getExpression() { return myExp; }    
    public String getRoughExpression() { return roughExp; }
    
    public boolean setExpression(String exp) {
        roughExp = exp;
        myExp = exp.trim();
        myExp = myExp.toUpperCase();
        
        return isValidExpression();        
    }

    private boolean isValidExpression() {
        String tmp = myExp;
        
        /* Convert all logic gates with it's equivalent operator. */
        replaceGates();

        if(tmp.equals(myExp))	//no logic gates found
            return false;

        /*Get all variables*/
        int i;
        boolean prevchar = false;

        for(i = 0; i < myExp.length(); i++) {
            if(Character.isLetter(myExp.charAt(i))) {
                if(!prevchar) {
                    if(!insertVariable(myExp.charAt(i)))    /*variable already exists*/
                        return (false);
                    prevchar = true;
                }
                else return (false);
            }
            else prevchar = false;
        }


        /*Remove spaces and add bracket at beginning*/
        tmp = "";
        for(i = 0; i < myExp.length(); i++) {
            if(myExp.charAt(i) != ' ')
                tmp += myExp.charAt(i);
        }
        myExp = "(" + tmp +")";

        /*** All errors conditions has been checked; Convert it in postfix notation. */        
        convertPostfix();        
        return true;
    }

    
    /*Replace all gates with it's equivalent operator*/
    private void replaceGates()	{        
        for(int i = LOGICGATES.GATESCOUNT-1; i >= 0; i--)
            myExp = replaceWord(myExp, LOGICGATES.MYGATES[i], LOGICGATES.MYGATESOP[i]);
    }


    private String replaceWord(String trg, String src, String by)
    {
        int i, k;
        String tmp = "";

        for(i = 0; i < trg.length(); i++) {
            if(trg.charAt(i) == src.charAt(0)) { /** First character matched. */
                
                /** Match remaining characters. */
                for(k = 1; k < src.length() && k+i < trg.length(); k++)
                    if(trg.charAt(k+i) != src.charAt(k)) break;
                
                if(k < src.length())    /** Not found. */
                    tmp += trg.charAt(i);
                else {
                    tmp += by;
                    i += src.length()-1;
                }
            }
            else tmp += trg.charAt(i);
        }
        return tmp;
    }
    
    private boolean convertPostfix() {
        /*TODO: Saturday, December 07, 2013: Vilash
        1. Add bracket for not gate e.g. A+B*(!A)+(!B)
        2. Proper bracket checking
        3. Operator checking e.g. a+*b, and **, exception for ~
        */

        Stack tmpStack = new Stack();
        String tmpPostfixExp = "";        
        String tmpChar;

        for(int i = 0; i < myExp.length(); i++) {
            /**Except variable found. */
            if(!Character.isDigit(myExp.charAt(i))) {
                if(myExp.charAt(i) == '(')
                    tmpStack.push('(');
                else if(myExp.charAt(i) == ')') {
                    /** Check for complete bracket expression. */
                    while(tmpStack.top() != '(') {   
                        tmpChar = tmpStack.top() + "";
                        tmpPostfixExp += tmpChar;
                        tmpStack.pop();
                    }
                    /** Remove ) from stack. */
                    tmpStack.pop();
                }
                else if(is_operator(myExp.charAt(i))) {
                    if(!tmpStack.empty() && is_operator(tmpStack.top()))  {
                        tmpChar = tmpStack.top() + "";
                        tmpStack.pop();
                        tmpPostfixExp += tmpChar;
                        tmpStack.push(myExp.charAt(i));
                    }
                    else tmpStack.push(myExp.charAt(i));
                }
                else {
                    tmpChar = myExp.charAt(i) + "";
                    tmpPostfixExp += tmpChar;
                }
            }            
            /** Push variables in stack. */
            else 
                tmpPostfixExp += myExp.charAt(i);
        }
        
        setPostfixExpression(tmpPostfixExp);        
        return true;
    }

    private boolean is_operator(char ch) {
        String tmp = ch + "";       //Convert in type String     
        for(int i = 0; i < LOGICGATES.GATESCOUNT; i++) {
            if(tmp.equals(LOGICGATES.MYGATESOP[i]))
                return true;
        }
        return false;
    }
}