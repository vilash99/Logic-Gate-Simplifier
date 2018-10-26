package logicgatesimplifier;
// This class defines an character stack that can hold 10000 values.
public class Stack
{
    public static final int MAX = 10000;	
    private char stck[];
    private int tos;

    // Initialize top-of-stack
    public Stack() {
        tos = -1;
        stck = new char[MAX];
    }

    // Push an item onto the stack
    public void push(char item) {
        if(tos != MAX) 
            stck[++tos] = item;        
    }

    // Pop an item from the stack
    public void pop() {
        if(tos >= 0)            
            tos--;
    }
    public char top() {
        if(tos < 0)
            return ' ';
        else return stck[tos];
    }

    public boolean empty() {
        if(tos < 0)
            return true;
        return false;
    }
}