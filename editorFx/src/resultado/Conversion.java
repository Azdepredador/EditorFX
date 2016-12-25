package resultado;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;
/**
 * Created by Dell on 10/11/2016.
 */
public class Conversion {

    Stack<String> pila1= new Stack();
    Stack<String> pila2= new Stack();


    public float conversion(String expr){

        StringBuilder temp = new StringBuilder();

        boolean entra=false;
        int i=0;
        int c=0;
        String v=null,op=null,v2=null;
        float resultado=0;


        do{
            if(expr.charAt(i)=='+' || expr.charAt(i)=='-'){
                pila2.push(Character.toString(expr.charAt(i)));

            }
            if(expr.charAt(i)=='*' || expr.charAt(i)=='/' || expr.charAt(i)=='%' || expr.charAt(i)=='^'){
                c=1;
                pila2.push(Character.toString(expr.charAt(i)));
                //System.out.println("Pila 2: "+pila2.toString());

            }

            if(esNumero(expr.charAt(i))|| expr.charAt(i)=='.'){

                temp.append(expr.charAt(i));


                while((i+1) != expr.length() && (Character.isDigit(expr.charAt(i+1)) || expr.charAt(i+1) == '.')){
                    temp.append(expr.charAt(++i));
                }

                pila1.push(temp.toString());
                temp.delete(0, temp.length());

                //		resultado=Float.parseInt(temp.toString());

                //System.out.println("Pila 1: "+pila1.toString());

                if(c==1 ){

                    v=pila1.pop();
                    op=pila2.pop();
                    v2=pila1.pop();

                    if(op.equals("*")){
                        resultado=Float.parseFloat(v)*Float.parseFloat(v2);
                    }
                    else if(op.equals("/")){
                        resultado=Float.parseFloat(v2)/Float.parseFloat(v);
                    }
                    else if(op.equals("%")){
                        resultado=Float.parseFloat(v2)%Float.parseFloat(v);
                    }
                    else if(op.equals("^")){
                        resultado=(float)Math.pow((double)Float.parseFloat(v),(double)Float.parseFloat(v2));
                    }

                    pila1.push(Float.toString(resultado));
                   // System.out.println(resultado);
                    c=0;

                }

            }

            if(expr.charAt(i)=='('){

                pila2.push(Character.toString(expr.charAt(i)));


            }

            if(expr.charAt(i)==')'){

                //pila2.push(Character.toString(expr.charAt(i)));

                while(!op.equals("(")){
                    v=pila1.pop();
                    op=pila2.pop();

                    if(op.equals("+")){
                        v2=pila1.pop();
                        resultado=Float.parseFloat(v2)+Float.parseFloat(v);
                      //  System.out.println(resultado);
                    }
                    else if(op.equals("-")){

                        v2=pila1.pop();
                        resultado=Float.parseFloat(v2)-Float.parseFloat(v);

                        //resultado=resultado-Float.parseInt(v);
                    }
                    else if(op.equals("*")){
                        resultado=resultado*Float.parseFloat(v);
                    }
                    else if(op.equals("/")){
                        resultado=resultado/Float.parseFloat(v);
                    }
                    else if(op.equals("%")){
                        resultado=resultado%Float.parseFloat(v);
                    }
                    else if(op.equals("^")){
                        resultado=(float)Math.pow((double)resultado,(double)Float.parseFloat(v2));
                    }

                    //System.out.println("resultado "+resultado);
                    pila1.push(Float.toString(resultado));


                }

                resultado=0;


            }

            i++;

        }while(i<expr.length());

        //System.out.println(temp);
        entra=false;
        do{
            if(entra==false){
                if(pila1.isEmpty())break;
                v=pila1.pop();
                if(pila2.isEmpty())break;


                op=pila2.pop();
                v2=pila1.pop();

                if(op.equals("+")){
                    resultado=Float.parseFloat(v)+Float.parseFloat(v2);
                }
                else if(op.equals("-")){
                    resultado=Float.parseFloat(v2)-Float.parseFloat(v);
                }
                else if(op.equals("*")){
                    resultado=Float.parseFloat(v)*Float.parseFloat(v2);
                }
                else if(op.equals("/")){
                    resultado=Float.parseFloat(v2)/Float.parseFloat(v);
                }
                else if(op.equals("%")){
                    resultado=Float.parseFloat(v2)%Float.parseFloat(v);
                }
                else if(op.equals("^")){
                    resultado=(float)Math.pow((double)Float.parseFloat(v),(double)Float.parseFloat(v2));
                }

            }

            if(entra){
                v=pila1.pop();
                op=pila2.pop();

                if(op.equals("+")){
                    resultado=resultado+Float.parseFloat(v);
                }
                else if(op.equals("-")){
                    resultado=resultado-Float.parseFloat(v);
                }
                else if(op.equals("*")){
                    resultado=resultado*Float.parseFloat(v);
                }
                else if(op.equals("/")){
                    resultado=resultado/Float.parseFloat(v);
                }
                else if(op.equals("%")){
                    resultado=resultado%Float.parseFloat(v);
                }
                else if(op.equals("^")){
                    resultado=(float)Math.pow((double)resultado,(double)Float.parseFloat(v2));
                }

            }
            entra=true;

        }while(!pila1.isEmpty() && !pila2.isEmpty());

        //System.out.println("Resultado: "+resultado);
        //dameResultado(resultado);
        return resultado;

    }


    public boolean esNumero(char n){
        switch(n){
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
            default:
                return false;

        }

    }

}
