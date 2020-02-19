import ROBDD.*;
import java.util.*;
public class Lab1 {

    public static void main(String[] args){

        Scanner myInput = new Scanner(System.in);
        System.out.println("Input the number of variables in an expression : ");
        int var = myInput.nextInt();
        myInput.nextLine();
        System.out.println("Enter the Boolean Expression to be tested: ");
        String expression = myInput.nextLine();
        //"and(imp(not(x1) , equiv(1,x2)) , not(x3))";
        //  expression = "and(or(x1,x2), and(and(x3,x4), equiv(x5,imp(x6,x7))))";
       // expression = "or(and (x1 , x2) , and(not(x1),x2))";
      //  expression = "and(equiv(x1,x2), equiv(x3,x4))";
      //   expression = "or(equiv(x1,x2),x3)";
      //   expression= "and(equiv(not(x1), x2), x3)";
        long startTime = System.nanoTime();
        ROBDD ROBDDobj = new ROBDD(var,1, expression);
        long endTime = System.nanoTime();
        System.out.println("********************************************");
        System.out.println("           ROBDD has been created           ");
        System.out.println("********************************************");
        System.out.println("Given expression is : "+ expression);
        System.out.println();
        Lab1 obj = new Lab1();
        obj.printROBDD(ROBDDobj);
        Double t = (endTime-startTime) * Math.pow(10, -9);
        System.out.println("Execution Time: "+ t +" sec");

      /* **************************************
      Restrict Testing
      *****************************************/
        System.out.println();
        System.out.println("********************************************");
        System.out.println("          Testing for Restrict Function      ");
        System.out.println("********************************************");
        System.out.println("Given expression is : "+ expression);
        System.out.println("Restricting for the node x[1] =0 ");
    //    startTime = System.nanoTime();
        ROBDDobj.restrict(2,0);
        endTime = System.nanoTime();
        System.out.println("Execution time: "+ t+ " ms");
        obj.printRestrict(ROBDDobj);
        t =(endTime-startTime) * Math.pow(10, -6);

     /* **************************************
      SatCount and AnySat Testing
     *****************************************/
       System.out.println("********************************************");
       System.out.println("     Testing for SatCount and AnySat        ");
       System.out.println("********************************************");
        System.out.println("Given expression is : "+ expression);
        startTime = System.nanoTime();
       System.out.println("Count of Satisfying Conditions is (SatCount):"+ ROBDDobj.satCount(7));
       ROBDDobj.anySat(7);
       endTime = System.nanoTime();
       System.out.println("Any of the Satisfying Conditions (AnySat) :");
       t = (endTime-startTime) * Math.pow(10, -6);

        for(Map.Entry<Integer,Integer> entry : ROBDDobj.anySat.entrySet()){
            System.out.print(entry.getKey()+" -> "+entry.getValue()+"    ");
        }
        System.out.println("Execution time: "+ t+ " ms");



      //*****************************************************************************
      //Testing for Apply
      //*****************************************************************************
        System.out.println("********************************************");
        System.out.println("          Testing for Apply Function        ");
        System.out.println("********************************************");
        startTime= System.nanoTime();
        ROBDD R1 = new ROBDD(3, 1, "or( equiv(x1, x2), x3 )");
        ROBDD R2 = new ROBDD(3, 1, "equiv( and(x1,x2) , x3 )");
        ROBDD.Apply apply = new ROBDD.Apply(3, 1);
        endTime = System.nanoTime();
        System.out.println("Expression for first ROBDD: or(equiv(x1, x2), x3 )");
        System.out.println("Expression for second ROBDD: equiv( and(x1,x2) , x3 )");
        System.out.println("The operator used in AND");
       System.out.println("Output of Apply: "+apply.apply("and",R1,R2));
       t = (endTime-startTime )*Math.pow(10,-6);
       System.out.println("Execution Time is "+t+" ms");
        obj.printROBDD(apply);
    }

    void printROBDD(ROBDD obj) {

        Iterator<Map.Entry<Integer, ROBDD.T_table>> itr1 = obj.T_map.entrySet().iterator();

        System.out.println("Table T from the ROBDD");
        System.out.println("u  |  var | l | h ");
        System.out.println("-------------------");
        while(itr1.hasNext()){
            Map.Entry<Integer, ROBDD.T_table> entry = itr1.next();
            System.out.println(entry.getKey() + "  :  " + entry.getValue().getVar() + "   " + entry.getValue().getL()+"  "+entry.getValue().getH());
        }

    }

    void printRestrict (ROBDD obj){
        Iterator<Map.Entry<Integer, ROBDD.T_table>> itr1 = obj.Tmap_restrict.entrySet().iterator();
        System.out.println("Table T from the ROBDD after restricting one of the nodes");
        System.out.println("u  |  var | l | h ");
        System.out.println("-------------------");
        while(itr1.hasNext()){
            Map.Entry<Integer, ROBDD.T_table> entry = itr1.next();
            System.out.println("  " + entry.getKey() + " : " + entry.getValue().getVar() + " " + entry.getValue().getL()+" "+entry.getValue().getH());
        }

    }



}
