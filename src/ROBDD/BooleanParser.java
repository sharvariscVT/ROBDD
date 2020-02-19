package ROBDD;

import java.util.ArrayList;
import java.util.List;

public class BooleanParser {
    int i;
    String s;

    public boolean parseBoolExpr(String expression) {
        if(expression.equals("") || expression.isEmpty()) return false;
        i = 0;
        s = expression;
        s = s.replaceAll("imp", "i");
        s = s.replaceAll("not", "!");
        s =  s.replaceAll("or", "|");
        s = s.replaceAll("and", "&");
        s = s.replaceAll("equiv", "e");

        return parse();
    }

    private boolean parse() {
        char op = s.charAt(i++);
        List<Boolean> bools = new ArrayList();

        while (i < s.length()) {
            char c = s.charAt(i++);

            if (c == '1' || c == '0')
                bools.add(c == '1');
            else if (c == '|' || c == '&' || c == '!' || c== 'i' || c=='e') {
                i--;
                bools.add(parse());
            }
            else if (c == ')')
                break;
        }
        return eval(bools, op);
    }

    private boolean eval(List<Boolean> bools, char op) {
        if (op == '!')
            return !bools.get(0);

        boolean result = false;
        boolean b1 = bools.get(0);
        boolean b2 = bools.get(1);

            if(op == '|')  result = b1 || b2 ;
            else if(op == '&')      result = (b1 && b2);
            else if(op == 'i')     {
                if(b2)   return true;
                else return !b1;
            }
            else if(op == 'e') {
                return b1 == b2;
            }

        return result;
    }


    public static void main(String[] args){
// "|(f, &(f, t))"
        BooleanParser obj = new BooleanParser();
        System.out.println(obj.parseBoolExpr("and(imp(not(1) , equiv(1,0)) , 1)"));

    }
}
