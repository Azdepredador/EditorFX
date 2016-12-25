package sample;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dell on 12/11/2016.
 */
public class GeneracionIntermedia extends Controller{

    public static HashMap <String,String> variablesDeclaradas= new HashMap<String,String>();
    public static HashMap<String,String> condicional= new HashMap<>();
    public static HashMap<String,String> funcProc= new HashMap<>();


    public void recibeFuncionesProcedimientos(String nom,String lin){
        funcProc.put(nom,lin);
    }
    public void seDeclaroFuncion(String nom){
        funcProc.remove(nom);
    }

    public void verificarCondicional(String d,String lin){
        if(condicional.containsKey(d)){
            recibeErroresSemanticos("Generacion intermedia Error de misma variable en asignacion "+lin);
        }
        condicional.clear();
    }

    public void agregarCondicional(String d){
        condicional.put(d,"");
    }

    public void recibeVariablesDeclaradas(String nombre, String lin){
        variablesDeclaradas.put(nombre,lin);
    }

    public void seDeclaro(String nombre){
        variablesDeclaradas.remove(nombre);
    }

    public void verVariables(){

        if(!variablesDeclaradas.isEmpty()){

            for (Map.Entry<String,String> entry : variablesDeclaradas.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                recibeErroresSemanticos("Generacion intermedia Warning variable "+key+" no fue usada linea "+value);
                // do stuff
            }



        }
    }

    public void verFunciones(){

        if(!funcProc.isEmpty()){

            for (Map.Entry<String,String> entry : funcProc.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                recibeErroresSemanticos("Generacion intermedia Warning funcion o procedimiento "+key+" no fue usada linea "+value);
                // do stuff
            }



        }
    }

    public void optimizador(){

        verVariables();
        verFunciones();
        variablesDeclaradas.clear();
        condicional.clear();
        funcProc.clear();
    }


}
