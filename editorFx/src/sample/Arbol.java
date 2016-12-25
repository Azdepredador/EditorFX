package sample;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by Dell on 08/10/2016.
 */
public class Arbol extends JFrame {
    int anchura = 150, altura = -180, movLib=-180, desplazarSec=600, desplazarIni=0;
    int mov=0;
    mxGraph graph = new mxGraph();
    Object parent = graph.getDefaultParent();
    mxGraphComponent graphComponent = new mxGraphComponent(graph);
    protected static HashMap m = new HashMap();
    boolean lib = false, prog = false, var = false, sec=false, ini=true;

    public Arbol() {

        setSize(800, 500);
        crearNodoPrincipal("Programa", 0, 0);
    }



    public boolean pathLibreria(String n){
        StringTokenizer token= new StringTokenizer(n," ");
        String e,i;


        while (token.hasMoreTokens()) {
            e = token.nextToken();


            if (e.equals("<Declaracion")) {
                return true;

            }
        }
        return  false;
    }


    public boolean numero(String n){
        StringTokenizer token= new StringTokenizer(n," ");
        String e,i;


            while (token.hasMoreTokens()) {
                e = token.nextToken();


                if (e.equals("<Declaracion")) {
                    return true;

                }
            }
            return  false;
    }


    public boolean esFuncion(String n){
        StringTokenizer token= new StringTokenizer(n," ");
        String e,i;


        while (token.hasMoreTokens()) {
            e = token.nextToken();


            if (e.equals("<Funcion")) {
                return true;

            }
        }
        return  false;
    }


    public boolean esProcedimiento(String n){
        StringTokenizer token= new StringTokenizer(n," ");
        String e,i;


        while (token.hasMoreTokens()) {
            e = token.nextToken();


            if (e.equals("<Procedimiento")) {
                return true;

            }
        }
        return  false;
    }

    int iniCont=0;
    int llamada=0;
    boolean compl=false;
    boolean ruta=false;
    void crearNodo(String nombre){

        if(esFuncion(nombre) || esProcedimiento(nombre)){
            llamada++;
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 450, 50+mov, 80,30);
            mov+=50;
            m.put(nombre,v1);
            graph.getModel().endUpdate();


        }
        else if(nombre.equals("inicio'")){
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 150, 450+altura, 80,30);
            altura+=200;
            m.put(nombre,v1);
            graph.getModel().endUpdate();
            ini=true;
            sec=false;
            var=false;
        }
        else if(sec){
            if(llamada!=1){
                desplazarSec=600;
            }
            llamada=1;
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, desplazarSec, mov, 80,30);
            desplazarSec+=100;
            m.put(nombre,v1);
            graph.getModel().endUpdate();
           // sec=false;

        }
        else if(ruta){
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 550, 70, 480,30);

            m.put(nombre,v1);
            graph.getModel().endUpdate();
            ruta=false;
        }
        else if(nombre.equals("<Ruta>")){
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 450, 70, 80,30);
            m.put(nombre,v1);
            graph.getModel().endUpdate();
            ruta=true;
        }
       /* else if(nombre.equals("inicio")){
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 130, mov+400, 80,30);
            sec=false;
            ini=true;

            m.put(nombre,v1);
            graph.getModel().endUpdate();

        }*/

        else if(nombre.equals("<libreria>")){
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 100, 90, 80,30);
           // anchura+=100;
           // altura+=0;
            m.put(nombre,v1);
            graph.getModel().endUpdate();
            lib=true;
        }
        else if(numero(nombre)){
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 300, 250+mov, 80,30);
            mov+=200;
            m.put(nombre,v1);
            graph.getModel().endUpdate();
        }
        else if(nombre.equals("seccion")){
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 300, 220+mov, 80,30);
            mov+=170;
            var=false;
            sec=true;
            m.put(nombre,v1);
            graph.getModel().endUpdate();
        }
        else if(nombre.equals("var")){
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 130, 250, 80,30);
            m.put(nombre,v1);
            graph.getModel().endUpdate();
            var=true;
        }
        else if(nombre.equals("programa")){
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 130, 190, 80,30);
            m.put(nombre,v1);
            graph.getModel().endUpdate();
            lib=false;
            prog=true;
            ruta=false;
        }

        else if(esFuncion(nombre)){
            //System.out.print("Entra");
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 450, 50+mov, 80,30);
            mov+=200;
            m.put(nombre,v1);
            graph.getModel().endUpdate();
        }
        else if(lib){

            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 200, 200+movLib, 80,30);
            // anchura+=100;
             movLib+=50;
            m.put(nombre,v1);
            graph.getModel().endUpdate();
        }

        else if(prog){
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 100+anchura, 190, 80,30);
             anchura+=100;
            m.put(nombre,v1);
            graph.getModel().endUpdate();
            prog=false;
        }
        else if(var){
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 450, 330+altura, 80,30);
            altura+=50;
            m.put(nombre,v1);
            graph.getModel().endUpdate();

        }
        else if(ini){
            getContentPane().add(graphComponent);
            graph.getModel().beginUpdate();
            Object v1 = graph.insertVertex(parent, null, nombre, 300+desplazarIni, 250+altura, 80,30);
            desplazarIni+=100;
            m.put(nombre,v1);
            graph.getModel().endUpdate();
        }




        }


    void crearNodoPrincipal(String nombre,int lados,int arriba){

        getContentPane().add(graphComponent);
        graph.getModel().beginUpdate();
        Object v1 = graph.insertVertex(parent, null, nombre, 10+lados, 190+arriba, 80,30);
        m.put(nombre,v1);
        graph.getModel().endUpdate();



    }


    void unirNodos(String n1, String n2){
        getContentPane().add(graphComponent);
        graph.getModel().beginUpdate();
        Object v1=m.get(n1);
        Object v2=m.get(n2);
        graph.insertEdge(parent,null,"",v1,v2);
        graph.getModel().endUpdate();
    }
}
