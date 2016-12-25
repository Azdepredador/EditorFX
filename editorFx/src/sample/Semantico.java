package sample;


import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


import resultado.*;

/**
 * Created by Dell on 31/10/2016.
 */
public class Semantico extends Controller{
    public  static String identificador;
    public  static  String id;
    public static  boolean verificaFinal=false;


    boolean prosigue=true;
    HashMap<String,String> variablesLocales= new HashMap<String,String>();
    HashMap<String,String> valores= new HashMap<String,String>();
    HashMap<String,String> valoresPara= new HashMap<String,String>();
    HashMap<String,String> para= new HashMap<String,String>();
    HashMap<String,String> procedimiento= new HashMap<String,String>();
    HashMap<String,String> procedimientoInicia= new HashMap<String,String>();

    HashMap<String,String> funcion= new HashMap<String,String>();
    HashMap<String,String> funcionInicia= new HashMap<String,String>();

    public static Stack<String> pila = new Stack<String>();
    GeneracionIntermedia op= new GeneracionIntermedia();


    public Semantico(){
        variablesLocales.clear();
        valores.clear();
        valoresPara.clear();
        para.clear();
        pila.removeAllElements();
        procedimientoInicia.clear();
        funcionInicia.clear();
        verificaFinal=false;
    }

    public void dameKey(String tok,String iden, String valor){
        int auxIden,auxValor,res;

        auxIden= Integer.parseInt(iden);
        auxValor = Integer.parseInt(valor);

        res=auxIden-auxValor;

        String idCorrecto=Integer.toString(res);

        id=idCorrecto+tok;





    }

    public void valoresMemoriaArreglos(String dato2, String dato, String tipoDeDato, String lin){

        int op,op2,res;

        if(tipoDeDato.equals("entero")){
            op= Integer.parseInt(dato);
            op2= Integer.parseInt(dato2);
            res= ((op-op2)+1)*4;
            String valor=Integer.toString(res);
            agregarEspacioMemoria(id,valor+" bytes");

            if(res<0){
                recibeErroresSemanticos("Error semantico al declarar tamano de arreglo "+lin);
            }

            //System.out.print(id+ "valor "+valor);
        }
        else if(tipoDeDato.equals("bool")){
            //agregarEspacioMemoria(id,"1 byte");
            op= Integer.parseInt(dato);
            op2= Integer.parseInt(dato2);
            res= ((op-op2)+1)*1;
            agregarEspacioMemoria(id,Integer.toString(res)+" bytes");

            if(res<0){
                recibeErroresSemanticos("Error semantico al declarar tamano de arreglo "+lin);
            }

        }
        else if(tipoDeDato.equals("byte")){
            //agregarEspacioMemoria(id,"1 byte");
            op= Integer.parseInt(dato);
            op2= Integer.parseInt(dato2);
            res= ((op-op2)+1)*1;
            agregarEspacioMemoria(id,Integer.toString(res)+" bytes");
            if(res<0){
                recibeErroresSemanticos("Error semantico al declarar tamano de arreglo "+lin);
            }
        }
        else if(tipoDeDato.equals("largo")){
            //agregarEspacioMemoria(id,"8 bytes");
            op= Integer.parseInt(dato);
            op2= Integer.parseInt(dato2);
            res= ((op-op2)+1)*8;
            agregarEspacioMemoria(id,Integer.toString(res)+" bytes");

            if(res<0){
                recibeErroresSemanticos("Error semantico al declarar tamano de arreglo "+lin);
            }
        }
        else if(tipoDeDato.equals("flotante")){
            //agregarEspacioMemoria(id,"8 bytes");
            op= Integer.parseInt(dato);
            op2= Integer.parseInt(dato2);
            res= ((op-op2)+1)*8;
            agregarEspacioMemoria(id,Integer.toString(res)+" bytes");

            if(res<0){
                recibeErroresSemanticos("Error semantico al declarar tamano de arreglo "+lin);
            }
        }
        else if(tipoDeDato.equals("doble")){
            //agregarEspacioMemoria(id,"16 bytes");
            op= Integer.parseInt(dato);
            op2= Integer.parseInt(dato2);
            res= ((op-op2)+1)*16;
            agregarEspacioMemoria(id,Integer.toString(res)+" bytes");

            if(res<0){
                recibeErroresSemanticos("Error semantico al declarar tamano de arreglo "+lin);
            }
        }
        else if(tipoDeDato.equals("caracter")){
            //agregarEspacioMemoria(id,"1 byte");
            op= Integer.parseInt(dato);
            op2= Integer.parseInt(dato2);
            res= ((op-op2)+1)*1;
            agregarEspacioMemoria(id,Integer.toString(res)+" bytes");

            if(res<0){
                recibeErroresSemanticos("Error semantico al declarar tamano de arreglo "+lin);
            }
        }
        else if(tipoDeDato.equals("string")){
            //agregarEspacioMemoria(id,"255 bytes");
            op= Integer.parseInt(dato);
            op2= Integer.parseInt(dato2);
            res= ((op-op2)+1)*255;
            agregarEspacioMemoria(id,Integer.toString(res)+" bytes");

            if(res<0){
                recibeErroresSemanticos("Error semantico al declarar tamano de arreglo "+lin);
            }
        }
    }

    public void verificaPara(String iden, String dato, String lin){
        if(variablesLocales.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else if(para.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else {
            para.put(iden,dato);
            espacioEnMemoria(dato);


        }
    }

    public void verificaParaProcedimiento(String iden, String dato, String lin){
        if(variablesLocales.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else if(procedimiento.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else if(para.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else {
            para.put(iden,dato);
            espacioEnMemoria(dato);


        }
    }

    public void verificaParaFuncion(String iden, String dato, String lin){
        if(variablesLocales.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else if(funcion.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else if(para.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else {
            para.put(iden,dato);
            espacioEnMemoria(dato);


        }
    }



    public void verificaProcedimiento(String iden, String dato, String lin){
        if(variablesLocales.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else if(procedimientoInicia.containsKey(iden)){ ///aqui
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else if(procedimiento.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else {
            procedimiento.put(iden,dato);
            espacioEnMemoria(dato);
            op.recibeVariablesDeclaradas(iden,lin);


        }
    }

    public void verificaFuncion(String iden, String dato, String lin){
        if(variablesLocales.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else if(funcionInicia.containsKey(iden)){ ///aqui
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else if(funcion.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else {
            funcion.put(iden,dato);
            espacioEnMemoria(dato);
            op.recibeVariablesDeclaradas(iden,lin);



        }
    }

    public void verificaInicializaProcedimiento(String iden , String lin){
        if(variablesLocales.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else if(procedimientoInicia.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else{
            procedimientoInicia.put(iden,"");
            op.recibeFuncionesProcedimientos(iden,lin);
        }
    }

    public void verificaInicializaFuncion(String iden , String lin){
        if(variablesLocales.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else if(funcionInicia.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }
        else{
            funcionInicia.put(iden,"");
            op.recibeFuncionesProcedimientos(iden,lin);
        }
    }


    public void verificarVariable(String iden, String lin){
        if(!variablesLocales.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" no ha sido inicializada linea "+lin);
            prosigue=false;
        }
        op.seDeclaro(iden);
    }

    public void verificarVariablePara(String iden, String lin){
        if(!variablesLocales.containsKey(iden) && !para.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" no ha sido inicializada linea "+lin);
            prosigue=false;
        }
        op.seDeclaro(iden);
    }

    public void verificarVariableProcedimiento(String iden, String lin){
        if(!variablesLocales.containsKey(iden) && !procedimiento.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" no ha sido inicializada linea "+lin);
            prosigue=false;
        }
        op.seDeclaro(iden);
    }

    public void verificarVariableFuncion(String iden, String lin){
        if(!variablesLocales.containsKey(iden) && !funcion.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" no ha sido inicializada linea "+lin);
            prosigue=false;
        }
        op.seDeclaro(iden);
    }

    public void verificaInvocacion(String iden, String lin){



        if(!procedimientoInicia.containsKey(iden) && !funcionInicia.containsKey(iden)){
            recibeErroresSemanticos("Error semanticos variable "+iden+" no ha sido inicializada linea "+lin);
            prosigue=false;
        }
        op.seDeclaroFuncion(iden);
    }

    public void  verificaSiseImplemento(String iden , String lin){
        if(para.containsKey(iden)){
            //tabla
            op.seDeclaro(iden);
        }
        else{
            recibeErroresSemanticos("Error semanticos variable "+iden+" no coincide linea "+lin);
        }
    }

    public void almacenaVariablesLocales(String iden, String dato, String lin){
        if(variablesLocales.containsKey(iden)){
                recibeErroresSemanticos("Error semantico variable "+iden+" ya ha sido inicializada linea "+lin);
        }else{
            variablesLocales.put(iden,dato);
            espacioEnMemoria(dato);
            op.recibeVariablesDeclaradas(iden,lin);



        }
    }
    public void espacioEnMemoria(String dato){
        if(dato.equals("entero")){
            agregarEspacioMemoria(id,"4 bytes");
        }
        else if(dato.equals("bool")){
            agregarEspacioMemoria(id,"1 byte");
        }
        else if(dato.equals("byte")){
            agregarEspacioMemoria(id,"1 byte");
        }
        else if(dato.equals("largo")){
            agregarEspacioMemoria(id,"8 bytes");
        }
        else if(dato.equals("flotante")){
            agregarEspacioMemoria(id,"8 bytes");
        }
        else if(dato.equals("doble")){
            agregarEspacioMemoria(id,"16 bytes");
        }
        else if(dato.equals("caracter")){
            agregarEspacioMemoria(id,"1 byte");
        }
        else if(dato.equals("string")){
            agregarEspacioMemoria(id,"255 bytes");
        }
    }

    public void agregarMemoriaFuncion(String dato){
        espacioEnMemoria(dato);
    }

    //bool|entero|largo|byte|flotante|doble|caracter|cadena|string

    public void verificaSiSeDeclaroProcedimiento(String iden,String dato, String lin, String signo){
        String tipoDato;


        if(variablesLocales.containsKey(iden) || procedimiento.containsKey(iden)){

            if(procedimiento.containsKey(iden)) {
                tipoDato = procedimiento.get(iden);
            }
            else{
                tipoDato = variablesLocales.get(iden);
            }

            if(tipoDato.equals("entero")){
                if(!Numero(dato) ){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+signo+dato);
                }
            }
            else if(tipoDato.equals("flotante")){
                if(!NumeroFlotante(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);

                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("bool")){
                if(!(dato.equals("verdad") || dato.equals("falso")) ){
                    recibeErroresSemanticos("Error tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("largo")){
                if(!Numero(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);
                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }
            else if(tipoDato.equals("byte")){
                if(!(dato.equals("1") || dato.equals("0"))){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);
                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("doble")){
                if(!Numero(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);
                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }
            else if(tipoDato.equals("caracter")){
                if(!Cadena(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);
                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                 //   System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("cadena")){
                if(!Cadena(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }
            else if(tipoDato.equals("string")){
                if(!Cadena(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }


        }
        else{
            recibeErroresSemanticos("Error semantico variable "+ iden + " no fue declarada linea "+lin);
        }


    }

    public void verificaSiSeDeclaroFuncion(String iden,String dato, String lin, String signo){
        String tipoDato;


        if(variablesLocales.containsKey(iden) || funcion.containsKey(iden)){

            if(funcion.containsKey(iden)) {
                tipoDato = funcion.get(iden);
            }
            else{
                tipoDato = variablesLocales.get(iden);
            }

            if(tipoDato.equals("entero")){
                if(!Numero(dato) ){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);
                    //System.out.println("Identificador "+iden+" valor= "+signo+dato);
                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                }
            }
            else if(tipoDato.equals("flotante")){
                if(!NumeroFlotante(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);

                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);
                    //System.out.println("Identificador "+iden+" valor= "+dato);}
                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                }

            }
            else if(tipoDato.equals("bool")){
                if(!(dato.equals("verdad") || dato.equals("falso")) ){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);
                   // System.out.println("Identificador "+iden+" valor= "+dato);
                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                }

            }
            else if(tipoDato.equals("largo")){
                if(!Numero(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                }
            }
            else if(tipoDato.equals("byte")){
                if(!(dato.equals("1") || dato.equals("0"))){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;


                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("doble")){
                if(!Numero(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);
                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                   // System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }
            else if(tipoDato.equals("caracter")){
                if(!Cadena(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                   // System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("cadena")){
                if(!Cadena(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                   // System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }
            else if(tipoDato.equals("string")){
                if(!Cadena(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                   // System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }


        }
        else{
            recibeErroresSemanticos("Error semantico variable "+ iden + " no fue declarada linea "+lin);
        }


    }

    public void verificaSiSeDeclaroPara(String iden,String dato, String lin, String signo){
        String tipoDato;


        if(variablesLocales.containsKey(iden) || para.containsKey(iden)){

            if(para.containsKey(iden)) {
                tipoDato = para.get(iden);
            }
            else{
                tipoDato = variablesLocales.get(iden);
            }

            if(tipoDato.equals("entero")){
                if(!Numero(dato) ){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put("0"+signo+iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                   // System.out.println("Identificador "+iden+" valor= "+signo+dato);

                }
            }
            else if(tipoDato.equals("flotante")){
                if(!NumeroFlotante(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);

                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("bool")){
                if(!(dato.equals("verdad") || dato.equals("falso")) ){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                  //  System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("largo")){
                if(!Numero(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                   // System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }
            else if(tipoDato.equals("byte")){
                if(!(dato.equals("1") || dato.equals("0"))){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("doble")){
                if(!Numero(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);


                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }
            else if(tipoDato.equals("caracter")){
                if(!Cadena(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("cadena")){
                if(!Cadena(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }
            else if(tipoDato.equals("string")){
                if(!Cadena(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }


        }
        else{
            recibeErroresSemanticos("Error semantico variable "+ iden + " no fue declarada linea "+lin);
        }


    }


    public void removerValoresPara(){
        for (Map.Entry<String,String> entry : para.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
      //      System.out.println("Remove: "+key);
            valores.remove(key);
            // do stuff
        }


    }

    public void removerValoresProcedimiento(){
        for (Map.Entry<String,String> entry : procedimiento.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //      System.out.println("Remove: "+key);
            valores.remove(key);
            // do stuff
        }
    }

    public void removerValoresFuncion(){
        for (Map.Entry<String,String> entry : funcion.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            //      System.out.println("Remove: "+key);
            valores.remove(key);
            // do stuff
        }
    }


    public void verificaSiSeDeclaro(String iden,String dato, String lin, String signo){
        String tipoDato;


        if(variablesLocales.containsKey(iden)){
            tipoDato=variablesLocales.get(iden);


            if(tipoDato.equals("entero")){
                if(!Numero(dato) ){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                   // System.out.println("Identificador "+iden+" valor= "+signo+dato);
                }
            }
            else if(tipoDato.equals("flotante")){
                if(!NumeroFlotante(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);

                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                       // System.out.print("Entra");
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                 //   System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("bool")){
                if(!(dato.equals("verdad") || dato.equals("falso")) ){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);
                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("largo")){
                if(!Numero(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }
            else if(tipoDato.equals("byte")){
                if(!(dato.equals("1") || dato.equals("0"))){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                   // System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("doble")){
                if(!Numero(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,"0"+signo+dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, signo + dato);
                    }
                    verificaFinal=false;
                   // System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }
            else if(tipoDato.equals("caracter")){
                if(!Cadena(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                 //   System.out.println("Identificador "+iden+" valor= "+dato);
                }

            }
            else if(tipoDato.equals("cadena")){
                if(!Cadena(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                   //System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }
            else if(tipoDato.equals("string")){
                if(!Cadena(dato)){
                    recibeErroresSemanticos("Error semantico tipo de dato "+dato+ "no valido "+lin);
                }
                else{
                    valores.put(iden,dato);
                    op.seDeclaro(iden);

                    if(verificaFinal) {
                        agregarValorFinal(id, dato);
                    }
                    verificaFinal=false;
                    //System.out.println("Identificador "+iden+" valor= "+dato);
                }
            }


        }
        else{
            recibeErroresSemanticos("Error semantico variable "+ iden + " no fue declarada linea "+lin);
        }

    }




    public void almacenaPila(String d){

        String valor;
        if(Identificador(d)){
         valor=valores.get(d);
            op.seDeclaro(d);
            pila.add(valor);
            //System.out.println("key: "+d+" valor "+valor);
        }
        else if(d.equals("mod")){
            pila.add("%");
        }
        else if(d.equals("**")){
            pila.add("^");
        }
        else{
            pila.add(d);
        }

    }



    public void verificaOperacionMatematica(String iden, String lin){
        if(!valores.containsKey(iden)){
            recibeErroresSemanticos("Error semantico variable "+iden+" no fue declarada linea "+lin);
            prosigue=false;
        }
        op.seDeclaro(iden);

    }


    //String postfix = S.toString().replaceAll("[\\]\\[,]", "");
    void operacionMatematica(){

        float resultado;

        if(prosigue) {
            String valor=pila.toString().replaceAll("[\\]\\[,]", "");
            String infija=valor.replace(" ","");


            pila.clear();


            Conversion c= new Conversion();
            resultado=c.conversion(infija);
            //PostFixConverter pc = new PostFixConverter(infija);
           // pc.printExpression();
            //PostFixCalculator calc = new PostFixCalculator(pc.getPostfixAsList());
            //System.out.println("Result: " + calc.result());
           // boolean entroCondicion=false;
            if(verificaFinal) {
                if(NumeroFlotante(valores.get(identificador))){
            //        entroCondicion=true;
                    //System.out.println(valores.get(identificador)+" identificador "+calc.result().toString());
                   // System.out.print("Leega");
                    agregarValorFinal(id, Float.toString(resultado));
                }else {

                    agregarValorFinal(id, Integer.toString((int)resultado));
                }


            }
            verificaFinal=false;
            //System.out.println("Identificador "+identificador+" valor= "+calc.result());

                valores.put(identificador, Float.toString(resultado));



        }
        else{
            //retornara 0
        }


    }


    public void agregarValorFinal(String key, String valor){

        String id=tabla.get(key).getId();

        ts.get(Integer.parseInt(id)-1).setValorFinal(valor);

    }

    public void agregarEspacioMemoria(String key, String valor){
        String id=tabla.get(key).getId();

        ts.get(Integer.parseInt(id)-1).setEspacioMemoria(valor);
    }

    public void agregarValor(String iden, String dato, String lin){

            valores.put(iden,dato);
            op.recibeVariablesDeclaradas(iden,lin);
    }



}
