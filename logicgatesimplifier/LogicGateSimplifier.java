package logicgatesimplifier;

/** 
 * @author Vilash
 */
class LogicGateSimplifier
{
    public static void main(String[] args) {        
        String E = "(A AND B) OR (C AND D) OR (E AND F) OR (A AND B) OR D";
        LogicExpression LE = new LogicExpression();
        
        if(LE.setExpression(E) == false)
            System.out.println("Wrong expression. (operator misplace/variables greater than 8");
        
        LE.processCombination();
        
        LE.pairKMap();        
        LE.simplifyExpression();        
        System.out.println("OUtput: " + LE.printPairs());        
        
        //System.out.println("User expression: " + LE.getRoughExpression());
        //System.out.println("Program expression: " + LE.getExpression());        
    }
}
