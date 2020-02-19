package ROBDD;
import java.util.*;
public class ROBDD {

    public int noOfVars ;
     int idx;
    public HashMap<Integer, T_table> T_map;
    public HashMap<String, Integer> H_map;
    public HashMap<Integer, T_table> Tmap_restrict;
    public HashMap<String, Integer> Hmap_restrict;
    public HashMap<Integer, Integer> anySat ;
    int[] x ;
    BooleanParser boolParse ;

    public ROBDD(int vars, int idx, String expression){
       this.boolParse = new BooleanParser();
       this.noOfVars = vars;
       this.idx = idx;
       x = new int[vars];
       for(int i=0; i<x.length;i++){
           x[i] =0;
       }
        anySat = new HashMap<>();
        initT();
        initT_restrict();

        initH();
        initH_restrict();

        Build(expression, idx);

    }
    public ROBDD(int vars, int idx){
        this.noOfVars = vars;
        this.idx = idx;
        x = new int[vars];
        for(int i=0; i<x.length;i++){
            x[i] =0;
        }
        initT();
        initH();
    }

     void initT(){
        T_map  = new HashMap<>();
        T_map.put(0, new T_table(this.noOfVars+1,-1,-1));
        T_map.put(1, new T_table(this.noOfVars+1, -2,-2));

    }

    void initH(){
         H_map = new HashMap<>();
         H_map.put(HashH(noOfVars+1,-1,-1),0);
         H_map.put(HashH(noOfVars+1,-2,-2),1);
    }
    void initT_restrict(){
        Tmap_restrict = new HashMap<>();
        Tmap_restrict.put(0, new T_table(this.noOfVars+1, -1,-1));
        Tmap_restrict.put(1, new T_table(this.noOfVars+1, -2,-2));
    }

    void initH_restrict(){
        Hmap_restrict = new HashMap<>();
        Hmap_restrict.put(HashH(noOfVars+1,-1,-1),0);
        Hmap_restrict.put(HashH(noOfVars+1, -2, -2), 1);
    }

    /* Hash generation function to store hash of i,l,h in H_table */
    String HashH(int i, int v0, int v1){
        // return getHash(i, getHash(v0,v1));
         return i +"#"+String.valueOf(v0)+ "#"+v1;
    }
//    int getHash(int i,int j){
//         return ((i+j)*(i+j+1))/2 +1;
//    }

   /* ******************************************
   All initialization functions such as Build,
   Make, Lookup and Member
   ********************************************* */

   int Mk(int i, int l, int h){
       if(l==h) return l;
       else if(memberOfH(i, l, h))
           return lookupInH(i,l,h);
      int u = T_map.size();
       T_map.put(u,new T_table(i,l,h));
       H_map.put(HashH(i,l,h), u);

       return u;
   }

   /* Check whether given i,l,h values are present in the H Table*/
   boolean memberOfH(int i, int l, int h){
       if(H_map.containsKey(HashH(i,l,h))) return true;
       return false;
   }

   /* get the u value corresponding to i,l,h in H Table */
   int lookupInH(int i,int l, int h){
       return H_map.get(HashH(i,l,h));
   }


   int Build(String expression, int i){
       if(i > noOfVars)
           return boolParse.parseBoolExpr(expression) ? 1:0;
       else{
           int v0 = Build(expression.replaceAll("x"+ i, "0"), i+1);
           int v1 = Build(expression.replaceAll("x"+ i, "1"), i+1);
           return Mk(i, v0,v1);
       }
   }


//To execute restriction on one of the variables, two new T and H tables are created

    public void restrict(int j, int b){
//         for(Map.Entry<Integer,T_table> entry : T_map.entrySet()){
//             if(entry.getValue().getL() == -1 || entry.getValue().getL() == -2) continue;
//             if(entry.getValue().getVar() != j)
//                 restrict_help(entry.getKey(), j, b);
//         }
       restrict_help(T_map.size()-1,j,b);
    }

   public int restrict_help(int u, int j, int b){
       T_table v = T_map.get(u);
       if(v.var > j)  return Mk_restrict(v.var,v.l, v.h);
       else if(v.var < j) return Mk_restrict(v.var, restrict_help(v.l, j, b), restrict_help(v.h, j,b));
       else if(v.var == j){
           if (b==0)  return restrict_help(v.l, j, b);
           else if(b==1) return restrict_help(v.h, j, b);
       }
///This might cause an issue;
      // return restrict_help(u,j,b);

      return -1;

   }

//Helper make function of Restrict
   int Mk_restrict(int i, int l, int h){
       if(l==h) return l;
       else if(memberOfH_restrict(i, l, h))
           return lookupInH_restrict(i,l,h);
       int u = Tmap_restrict.size();
       Tmap_restrict.put(u,new T_table(i,l,h));
       Hmap_restrict.put(HashH(i,l,h), u);
       return u;
   }

   boolean memberOfH_restrict(int i, int l, int h){
       if(Hmap_restrict.containsKey(HashH(i,l,h))) return true;
       return false;
   }

   int lookupInH_restrict(int i,int l, int h){
        return Hmap_restrict.get(HashH(i,l,h));
    }
   /* Main SatCount function*/
  public int satCount(int u){
    T_table temp = T_map.get(u);
    int count = count(u);
    return (int) (Math.pow(2, temp.var-1)*count);
}

public void anySat(int u){
      if(u==0) throw new IllegalArgumentException(" Error !!!");
      else if(u==1) return;
      else if(T_map.get(u).getL() == 0) {
          anySat.put(u, 1);
          anySat(T_map.get(u).getH());
      }
      else {
          anySat.put(u,0);
          anySat(T_map.get(u).getL());
      }
}

//Support function for satCount
int count(int u){
    if(u==0)  return 0;
    else if(u==1) return 1;
    T_table temp = T_map.get(u);
    return (int) (Math.pow(2,(T_map.get(temp.l).var - temp.var -1)) * count(temp.l)
                   + Math.pow(2,(T_map.get(temp.h).var - temp.var -1)) * count(temp.h) );
}

/* ***************************
*   AnySat Function
* ******************************/



   /*  An object containing i,l,h values corresponding to u */
  public class T_table{
          int var;
          int l;
          int h;


         public int getVar(){
             return this.var;
         }
         public int getL(){
             return this.l;
         }
         public int getH(){
             return this.h;
         }

         T_table(int var, int l, int h ){
             this.var = var;
             this.l = l;
             this.h = h;
         }
   }

   public static class Apply extends ROBDD{

      HashMap<String, Integer> G ;
      HashMap<Integer, T_table> T1;
      HashMap<Integer, T_table> T2;
       public Apply(int vars, int idx, String expression) {
           super(vars, idx, expression);
           initApply();
       }

       public Apply(int vars, int idx){
           super(vars, idx);
           initApply();
       }

       void initApply(){
           G = new HashMap<>();
       }


      public int apply(String op, ROBDD t1, ROBDD t2){
           T1 = t1.T_map;
           T2 = t2.T_map;

          return App(T1.size()-1 , T2.size()-1 , op);
       }



       int App(int u1, int u2, String op){
           int u=-1;
            if(G.containsKey(getHashG(u1,u2))) return G.get(getHashG(u1,u2));
          else if((T1.get(u1).getL() < 0) && (T2.get(u2).getL()< 0)){
                BooleanParser newParse = new BooleanParser();
                u = newParse.parseBoolExpr(op+"("+u1+","+u2+")") ? 1:0;

            }
          else if(T1.get(u1).getVar() == T2.get(u2).getVar()) u = Mk(T1.get(u1).getVar(), App(T1.get(u1).getL() , T2.get(u2).getL(), op), App(T1.get(u1).getH() , T2.get(u2).getH(), op));
          else if(T1.get(u1).getVar() < T2.get(u2).getVar()) u = Mk(T1.get(u1).getVar(), App(T1.get(u1).getL() , u2, op), App(T1.get(u1).getH() , u2, op));
          else  if(T1.get(u1).getVar() > T2.get(u2).getVar()) u = Mk(T1.get(u2).getVar(), App(u1 , T2.get(u2).getL(), op), App(u1, T2.get(u2).getH(), op));

          G.put(getHashG(u1, u2), u);
          return u;
       }

       String getHashG(int u1, int u2){
           return String.valueOf(u1) + "#" + String.valueOf(u1);
       }




   }


    /* ************************************
    Boolean Operations as Functions
    *************************************** */

    int not(int i){
        return (i==0)? 1:0;
    }

    int and(int x1, int x2){
        return x1 & x2;
    }

    int or(int x1, int x2){
        return x1 | x2;
    }

    int imp(int x1, int x2){
        if(x1 == 1) return 1;
        else return x2 == 0? 1:0;
    }

    int equiv(int x1, int x2){
        if(x1 == x2) return 1;
        else return 0;
    }


}
