package sample;


import com.sun.javafx.image.IntPixelGetter;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Dell on 08/10/2016.
 */

public class sintactico extends Controller implements Initializable {

    Arbol a= new Arbol();
    //ObservableList<infoSintactico> dataL = FXCollections.observableArrayList();
    Semantico s= new Semantico();



    boolean lib=false;
    int ct=-1;
    String anterior;

    public sintactico(){
        a.setVisible(true);

        auxEF=true;
        auxEP=true;


    }



    int r=0;
    public void recogerDatos(String vi, String ambito, String na, String dir){
        recibeTablaSintactico(data.get(r).getLinea(),
                data.get(r).getToken(),
                data.get(r).getIdentificador(),
                ambito,
                vi,
                na,
                dir,
                data.get(r).getId()
        );

        r++;
    }





    @FXML
    public void dameLista(ObservableList<infoLexico> data){
        for(int j=0; j<data.size();j++){
         //   almacenarLista(data.get(j).getId(),data.get(j).getLinea(),data.get(j).getToken(),data.get(j).getIdentificador());
        }
        //inicio();
    }



    public String dameToken(){
        ct++;
        if(data.isEmpty()){
            //nada
            return  "";
        }
        else{
            if(data.size()!=ct){

             //   ct++;
                //System.out.println("el token" + dataL.get(ct).getToken());
                return data.get(ct).getToken();
            }
            else {
                //System.out.print("Ya no hay");
                return "";
            }
        }

    }

    public String dameNumeroLinea(){
        if(data.isEmpty()){
            //nada
            return  "";
        }
        else{
            if(data.size()!=ct){

                //   ct++;
               // System.out.println("el token" + dataL.get(cd).getToken());
                return data.get(ct).getLinea();
            }
            else {
                //System.out.print("Ya no hay");
                return "";
            }
        }

    }

    public String dameID(){
        if(data.isEmpty()){
            //nada
            return  "";
        }
        else{
            if(data.size()!=ct){

                //   ct++;
                // System.out.println("el token" + dataL.get(cd).getToken());
                return data.get(ct).getId();
            }
            else {
                //System.out.print("Ya no hay");
                return "";
            }
        }

    }



    public void repite(String op){
        String n,n1,n2,n3;
        auxE=true;
        if(op.equals("repite")){
            a.crearNodo(op);
            a.unirNodos(anterior,op);
            recogerDatos("","Bloque","","");
            crearArchivo(op+"\n");

            anterior=op;
            n=dameToken();

            if(Identificador(n)||n.equals("si")||n.equals("para")||
                    n.equals("repite")||n.equals("hazlo_si")||n.equals("seleccion")||
                    n.equals("leer")||n.equals("escribir")||n.equals("escribirSL") || Comentario(n)){
                bloque(n);
                n1=dameToken();
                while (Identificador(n1)||n1.equals("si")||n1.equals("para")||
                        n1.equals("repite")||n1.equals("hazlo_si")||n1.equals("seleccion")||
                        n1.equals("leer")||n1.equals("escribir")||n1.equals("escribirSL")||Comentario(n1)){
                    bloque(n1);
                    n1=dameToken();
                }
                auxE=true;
                do {


                    if (n1.equals("finrepite")) {
                        a.crearNodo(n1);
                        a.unirNodos(anterior, n1);
                        if(auxE2)
                        recogerDatos("", "Bloque", "", "");
                        anterior = n1;
                        n2 = dameToken();
                        crearArchivo(n1+" ");
                        do {
                            if (Identificador(n2)) {
                                condicionante(n2);
                                auxE=true;
                                n3 = dameToken();
                                do {
                                    if (n3.equals(";")) {
                                        a.crearNodo(n3);
                                        a.unirNodos(anterior, n3);
                                        if(auxE2)
                                        recogerDatos("", "Bloque", "", "");
                                        anterior = n3;
                                        crearArchivo(n3+"\n");
                                        auxE=false;

                                    } else {
                                        //   System.out.print("Error ;");
                                        recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                        ct--;
                                        auxE2=false;
                                        n3=";";
                                    }
                                }while (auxE);

                            } else {

                                recibeErroresSintactico("Error falta condicion " + dameNumeroLinea());
                                ct--;
                                auxE2=false;
                                n2="Identificador";

                            }

                        }while (auxE);


                    } else {
                        // System.out.print("Error fin repite");
                        recibeErroresSintactico("Error falta finrepite " + dameNumeroLinea());
                        ct--;
                        auxE2=false;
                        n1="finrepite";
                    }
                }while (auxE);

            }
            else{
                //System.out.print("Error bloque");
                recibeErroresSintactico("Error en bloque "+dameNumeroLinea());
                crearArchivo("bloque\nfinrepite ( condicion ) ;\ninicio\nbloque\n");
                auxE=false;
            }

        }
        else{
           // System.out.print("Errir repite");
            recibeErroresSintactico("Error en repite "+dameNumeroLinea());
        }
    }

    boolean prosigueIdentificador=false;

    public void verificaDoble(String op){
        String aux;

        if(Identificador(op)){
        prosigueIdentificador=true;

            a.crearNodo(op);
            a.unirNodos(anterior,op);
            recogerDatos("","Bloque","","");
            crearArchivo(op+" ");
            anterior=op;
            aux=dameToken();

            if(aux.equals(")")){
                ct--;
            }

            if(banderaFuncion){
                s.verificarVariableFuncion(op,dameNumeroLinea());
            }
            else if(banderaProcedimiento){
                s.verificarVariableProcedimiento(op,dameNumeroLinea());
            }
            else if(paraSemantico){
                s.verificarVariablePara(op,dameNumeroLinea());
            }
            else{
                s.verificarVariable(op,dameNumeroLinea());
            }

            if(aux.equals(",")){
                a.crearNodo(aux);
                a.unirNodos(anterior,aux);
                recogerDatos("","Bloque","","");
                crearArchivo(aux+" ");
                anterior=aux;
                aux=dameToken();

                do {
                    if (Identificador(aux)) {
                        a.crearNodo(aux);
                        a.unirNodos(anterior, aux);
                        if(auxE2)
                        recogerDatos("", "Bloque", "", "");

                        crearArchivo(aux+" ");
                        anterior = aux;
                        if (banderaFuncion) {
                            s.verificarVariableFuncion(aux, dameNumeroLinea());
                        } else if (banderaProcedimiento) {
                            s.verificarVariableProcedimiento(aux, dameNumeroLinea());
                        } else if (paraSemantico) {
                            s.verificarVariablePara(aux, dameNumeroLinea());
                        } else {
                            s.verificarVariable(aux, dameNumeroLinea());
                        }
                        auxE=false;


                    } else {
                        recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                        ct--;
                        auxE2=false;
                        aux="Identificador";
                    }
                }while (auxE);


            }




        }

    }

    public void bloque(String op){
        String n,n1,n2,n3,
        n4,n5,n6,n7,
        n8,n9,n10,n11;

        String aux;

        if(Identificador(op)){
            a.crearNodo("<Asignacion>");
            a.unirNodos(anterior,"<Asignacion>");
            anterior="<Asignacion>";
            aux=dameToken();



            if(aux.equals("(")){

                a.crearNodo(op);
                a.unirNodos(anterior,op);


                a.crearNodo(aux);
                a.unirNodos(op,aux);
                recogerDatos("","Bloque","","");
                crearArchivo(op+" ");
                crearArchivo(aux+" ");

                anterior=aux;
                aux=dameToken();

                s.verificaInvocacion(op,dameNumeroLinea());
                verificaDoble(aux);

                if(prosigueIdentificador){

                    aux=dameToken();

                    prosigueIdentificador=false;
                }

                auxE=true;
                do {

                    if (aux.equals(")")) {
                        a.crearNodo(aux);
                        a.unirNodos(anterior, aux);
                        anterior = aux;
                        if(auxE2)
                        recogerDatos("", "Bloque", "", "");
                        crearArchivo(aux+" ");
                        aux = dameToken();

                        do {
                            if (aux.equals(";")) {
                                a.crearNodo(aux);
                                a.unirNodos(anterior, aux);
                                anterior = aux;
                                if (auxE2)
                                    recogerDatos("", "Bloque", "", "");
                                crearArchivo(aux + "\n");
                                auxE = false;
                                //aux=dameToken();
                            } else {
                                recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                ct--;
                                auxE2=false;
                                aux=";";
                            }
                        }while (auxE);

                    } else {
                        recibeErroresSintactico("Error no se declaro identificador " + dameNumeroLinea());
                        ct--;
                        auxE2=false;
                        aux=")";
                    }
                }while (auxE);

            }else {
                ct--;

                asignacion(op);
            }



        }
        else if(op.equals("si")){

            a.crearNodo("<Si>");
            a.unirNodos(anterior,"<Si>");
            anterior="<Si>";

            condicionSi(op);

        }
        else if(op.equals("para")){
            a.crearNodo("<Para>");
            a.unirNodos(anterior,"<Para>");
            anterior="<Para>";
            para(op);
        }
        else if(op.equals("repite")){
            a.crearNodo("<Repite>");
            a.unirNodos(anterior,"<Repite>");
            anterior="<Repite>";

            repite(op);
        }
        else if(op.equals("hazlo_si")){
            a.crearNodo("<Hazlo si>");
            a.unirNodos(anterior,"<Hazlo si>");
            anterior="<Hazlo si>";
            hazloSi(op);
        }
        else if(op.equals("seleccion")){
            a.crearNodo("<Seleccion>");
            a.unirNodos(anterior,"<Seleccion>");
            anterior="<Seleccion>";
            seleccion(op);
        }
        else if(op.equals("leer")){
            auxE=true;
            a.crearNodo(op);
            a.unirNodos(anterior,op);
            recogerDatos("","Bloque","","");
            anterior=op;
            n8=dameToken();
            crearArchivo(op+" ");
            do {
                if (n8.equals("(")) {
                    a.crearNodo(n8);
                    a.unirNodos(anterior, n8);
                    if (auxE2)
                        recogerDatos("", "Bloque", "", "");

                    crearArchivo(n8 + " ");

                    anterior = n8;
                    n9 = dameToken();
                do{
                    if (Identificador(n9)) {
                        a.crearNodo(n9);
                        a.unirNodos(anterior, n9);
                        if(auxE2)
                        recogerDatos("", "Bloque", "", "");
                        anterior = n9;
                        n10 = dameToken();
                        crearArchivo(n9+" ");

                        if (banderaFuncion) {
                            s.verificarVariableFuncion(n9, dameNumeroLinea());
                        } else if (banderaProcedimiento) {
                            s.verificarVariableProcedimiento(n9, dameNumeroLinea());
                        } else if (paraSemantico) {
                            s.verificarVariablePara(n9, dameNumeroLinea());
                        } else {
                            s.verificarVariable(n9, dameNumeroLinea());
                        }

                    do {
                        if (n10.equals(")")) {
                            a.crearNodo(n10);
                            a.unirNodos(anterior, n10);
                            if (auxE2)
                                recogerDatos("", "Bloque", "", "");
                            anterior = n10;
                            n11 = dameToken();
                            crearArchivo(n10 + " ");
                        do {
                            if (n11.equals(";")) {
                                a.crearNodo(n11);
                                a.unirNodos(anterior, n11);
                                if(auxE2)
                                recogerDatos("", "Bloque", "", "");
                                anterior = n11;

                                crearArchivo(n11+"\n");
                                auxE=false;

                            } else {
                                //  System.out.print("Error ;");
                                recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                auxE2=false;
                                ct--;
                                n11=";";
                            }
                        }while (auxE);
                        } else {
                            //System.out.print("Error )");
                            recibeErroresSintactico("Error falta ) " + dameNumeroLinea());
                            ct--;
                            auxE2 = false;
                            n10 = ")";
                        }
                    }while (auxE);
                    } else {
                        // System.out.print("Error Identificador");
                        recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                        ct--;
                        auxE2 = false;
                        n9 = "Identificador";
                    }
                }while (auxE);
                } else {
                    //System.out.print("Error (");
                    recibeErroresSintactico("Error falta ( " + dameNumeroLinea());
                    ct--;
                    auxE2 = false;
                    n8 = "(";
                }
            }while (auxE);





        }
        else if(op.equals("escribir")){
            auxE=true;
            a.crearNodo(op);
            a.unirNodos(anterior,op);
            recogerDatos("","Bloque","","");
            anterior=op;
            n=dameToken();
            crearArchivo(op+" ");

            do {
                if (n.equals("(")) {
                    a.crearNodo(n);
                    a.unirNodos(anterior, n);
                    if (auxE2)
                        recogerDatos("", "Bloque", "", "");
                    anterior = n;
                    n1 = dameToken();
                    crearArchivo(n + " ");
                    do {
                        if (Identificador(n1)) {
                            a.crearNodo(n1);
                            a.unirNodos(anterior, n1);
                            if (auxE2)
                                recogerDatos("", "Bloque", "", "");
                            anterior = n1;
                            n2 = dameToken();
                            crearArchivo(n1 + " ");

                            if (banderaFuncion) {
                                s.verificarVariableFuncion(n1, dameNumeroLinea());
                            } else if (banderaProcedimiento) {
                                s.verificarVariableProcedimiento(n1, dameNumeroLinea());
                            } else if (paraSemantico) {
                                s.verificarVariablePara(n1, dameNumeroLinea());
                            } else {
                                s.verificarVariable(n1, dameNumeroLinea());
                            }

                            do {
                                if (n2.equals(")")) {
                                    a.crearNodo(n2);
                                    a.unirNodos(anterior, n2);
                                    if (auxE2)
                                        recogerDatos("", "Bloque", "", "");
                                    anterior = n2;
                                    n3 = dameToken();

                                    crearArchivo(n2 + " ");

                                    do {
                                        if (n3.equals(";")) {
                                            a.crearNodo(n3);
                                            a.unirNodos(anterior, n3);
                                            if (auxE2)
                                                recogerDatos("", "Bloque", "", "");
                                            anterior = n3;
                                            crearArchivo(n3 + "\n");

                                            auxE=false;

                                        } else {
                                            //System.out.print("Error ;");
                                            recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                            ct--;
                                            auxE2 = false;
                                            n3 = ";";
                                        }
                                    } while (auxE);
                                } else {
                                    //System.out.print("Error )");
                                    recibeErroresSintactico("Error falta ) " + dameNumeroLinea());
                                    ct--;
                                    auxE2 = false;
                                    n2 = ")";
                                }
                            } while (auxE);
                        } else {
                            //System.out.print("Error Identificador");
                            recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                            ct--;
                            auxE2 = false;
                            n1 = "Identificador";
                        }
                    } while (auxE);
                } else {
                    //System.out.print("Error (");
                    recibeErroresSintactico("Error falta ( " + dameNumeroLinea());
                    ct--;
                    auxE2 = false;
                    n = "(";
                }
            }while (auxE);
        }

        else if(op.equals("escribirSL")){
            auxE=true;

            a.crearNodo(op);
            a.unirNodos(anterior,op);
            recogerDatos("","Bloque","","");
            anterior=op;
            n4=dameToken();
            crearArchivo(op+" ");
            do {
                if (n4.equals("(")) {
                    a.crearNodo(n4);
                    a.unirNodos(anterior, n4);
                    if (auxE2)
                        recogerDatos("", "Bloque", "", "");
                    anterior = n4;
                    n5 = dameToken();
                    crearArchivo(n4 + " ");

                    do {
                        if (Identificador(n5)) {
                            a.crearNodo(n5);
                            a.unirNodos(anterior, n5);
                            if (auxE2)
                                recogerDatos("", "Bloque", "", "");
                            anterior = n5;

                            crearArchivo(n5 + " ");

                            if (banderaFuncion) {
                                s.verificarVariableFuncion(n5, dameNumeroLinea());
                            } else if (banderaProcedimiento) {
                                s.verificarVariableProcedimiento(n5, dameNumeroLinea());
                            } else if (paraSemantico) {
                                s.verificarVariablePara(n5, dameNumeroLinea());
                            } else {
                                s.verificarVariable(n5, dameNumeroLinea());
                            }


                            n6 = dameToken();
                            do {
                                if (n6.equals(")")) {
                                    a.crearNodo(n6);
                                    a.unirNodos(anterior, n6);
                                    if (auxE2)
                                        recogerDatos("", "Bloque", "", "");
                                    anterior = n6;
                                    n7 = dameToken();
                                    crearArchivo(n6 + " ");

                                    do {
                                        if (n7.equals(";")) {
                                            a.crearNodo(n7);
                                            a.unirNodos(anterior, n7);
                                            if (auxE2)
                                                recogerDatos("", "Bloque", "", "");
                                            anterior = n7;

                                            crearArchivo(n7 + "\n");
                                            auxE = false;

                                        } else {
                                            //System.out.print("Error ;");
                                            recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                            ct--;
                                            auxE2 = false;
                                            n7 = ";";
                                        }
                                    } while (auxE);
                                } else {
                                    //  System.out.print("Error )");
                                    recibeErroresSintactico("Error falta ) " + dameNumeroLinea());
                                    ct--;
                                    auxE2 = false;
                                    n6 = ")";
                                }
                            } while (auxE);
                        } else {
                            //System.out.print("Error Identificador");
                            recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                            ct--;
                            auxE2 = false;
                            n5 = "Identificador";
                        }
                    } while (auxE);
                } else {
                    //System.out.print("Error (");
                    recibeErroresSintactico("Error falta ( " + dameNumeroLinea());
                    ct--;
                    auxE2 = false;
                    n4 = "(";
                }
            }while (auxE);






        }
        else if(Comentario(op)){
            //acepta
            String aux2;
            recogerDatos("","Bloque","","");
            if(op.equals("{//")){
                aux2=dameToken();
                recogerDatos("","Bloque","","");
                while(!aux2.equals("//}")){
                    aux2=dameToken();
                    recogerDatos("","Bloque","","");
                }
            }
        }
        else{
            System.out.print("Error en bloque "+dameNumeroLinea());
        }


    }


    public  void hazloSi(String op){
        String n,n1,n2,n3;
        auxE=true;

        if(op.equals("hazlo_si")) {
            a.crearNodo(op);
            a.unirNodos(anterior, op);
            recogerDatos("", "Bloque", "", "");
            anterior = op;
            n = dameToken();
            crearArchivo(op + " ");
        do{
            if (Identificador(n)) {
                condicionante(n);
                crearArchivo("\n");
                auxE = true;
                n1 = dameToken();

                if (Identificador(n1) || n1.equals("si") || n1.equals("para") ||
                        n1.equals("repite") || n1.equals("hazlo_si") || n1.equals("seleccion") ||
                        n1.equals("leer") || n1.equals("escribir") || n1.equals("escribirSL") || Comentario(n1)) {
                    bloque(n1);
                    n2 = dameToken();
                    while (Identificador(n2) || n2.equals("si") || n2.equals("para") ||
                            n2.equals("repite") || n2.equals("hazlo_si") || n2.equals("seleccion") ||
                            n2.equals("leer") || n2.equals("escribir") || n2.equals("escribirSL") || Comentario(n2)) {
                        bloque(n2);
                        n2 = dameToken();
                    }
                    auxE = true;

                do{
                    if (n2.equals("finhazlo")) {
                        a.crearNodo(n2);
                        a.unirNodos(anterior, n2);
                        if (auxE2)
                            recogerDatos("", "Bloque", "", "");
                        anterior = n2;
                        n3 = dameToken();
                        crearArchivo(n2 + " ");
                    do{
                        if (n3.equals(";")) {
                            a.crearNodo(n3);
                            a.unirNodos(anterior, n3);
                            if(auxE2)
                            recogerDatos("", "Bloque", "", "");
                            anterior = n3;
                            crearArchivo(";\n");
                            auxE=false;

                        } else {
                            //System.out.print("Error ;");
                            recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                            ct--;
                            auxE2 = false;
                            n3 = ";";
                        }
                    }while (auxE);
                    } else {
                        //System.out.print("Error finhazlo");
                        recibeErroresSintactico("Error falta finhazlo " + dameNumeroLinea());
                        ct--;
                        auxE2 = false;
                        n2 = "finhazlo";
                    }

                }while (auxE);

            } else {
                    //System.out.print("Errro bloque");
                    recibeErroresSintactico("Error en bloque " + dameNumeroLinea());
                    crearArchivo("bloque\nfinhazlo;\n\ninicio\nbloque\n");
                    auxE=false;
                }
            } else {
                //System.out.print("Error condicion");
                recibeErroresSintactico("Error falta condicion " + dameNumeroLinea());
                ct--;
                auxE2 = false;
                n = "Identificador";
            }
        }while(auxE);
        }
        else{
           // System.out.print("Error hazlo si");
            recibeErroresSintactico("Error en hazlo_si "+dameNumeroLinea());
        }
    }

    boolean paraSemantico=false;


    boolean auxEPA,auxEPA2=true;

    public void para(String op){
        String n,n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11;
        auxEPA=true;

        if(op.equals("para")) {
            a.crearNodo(op);
            a.unirNodos(anterior, op);
            recogerDatos("", "Bloque", "", "");
            anterior = op;
            n = dameToken();
            crearArchivo(op + " ");

            do{
            if (Identificador(n)) {

                a.crearNodo(n);
                a.unirNodos(anterior, n);

                if (auxEPA2)
                    recogerDatos("", "Bloque", "", "");

                anterior = n;
                crearArchivo(n + " ");

                n1 = dameToken();

                do{
                if (n1.equals("como")) {
                    a.crearNodo(n1);
                    a.unirNodos(anterior, n1);
                    if(auxEPA2)
                    recogerDatos("", "Bloque", "", "");
                    anterior = n1;
                    n2 = dameToken();
                    crearArchivo(n1+" ");
                do {
                    if (n2.equals("entero") || n2.equals("largo") || n2.equals("byte")) {
                        a.crearNodo(n2);
                        a.unirNodos(anterior, n2);

                        anterior = n2;
                        n3 = dameToken();


                        //System.out.println(dameID());
                        if (auxEPA2) {
                            recogerDatos("", "Bloque", "", "");
                            s.dameKey(n, dameID(), "3");
                        }

                        crearArchivo(n2 + " ");

                        if (banderaProcedimiento) {
                            s.verificaParaProcedimiento(n, n2, dameNumeroLinea());
                        } else if (banderaFuncion) {
                            s.verificaParaFuncion(n, n2, dameNumeroLinea());
                        } else {
                            s.verificaPara(n, n2, dameNumeroLinea());
                        }
                        do {
                            if (n3.equals(":=")) {
                                a.crearNodo(n3);
                                a.unirNodos(anterior, n3);
                                if (auxEPA2)
                                    recogerDatos("", "Bloque", "", "");
                                anterior = n3;
                                n4 = dameToken();
                                crearArchivo(n3 + " ");
                            do{
                                if (Numero(n4)) {
                                    a.crearNodo(n4);
                                    a.unirNodos(anterior, n4);

                                    anterior = n4;
                                    n5 = dameToken();
                                    agregarValorInicial(n, n4);
                                    paraSemantico = true;

                                    if (auxEPA2) {
                                        recogerDatos("", "Bloque", "", "");
                                        s.verificaSiSeDeclaroPara(n, n4, dameNumeroLinea(), "");
                                    }
                                    crearArchivo(n4 + " ");

                                    do{
                                    if (n5.equals("hasta")) {
                                        a.crearNodo(n5);
                                        a.unirNodos(anterior, n5);
                                        if(auxEPA2)
                                        recogerDatos("", "Bloque", "", "");
                                        anterior = n5;
                                        n6 = dameToken();
                                        crearArchivo(n5+" ");

                                        do {
                                            if (Numero(n6)) {
                                                a.crearNodo(n6);
                                                a.unirNodos(anterior, n6);
                                                if (auxEPA2)
                                                    recogerDatos("", "Bloque", "", "");
                                                anterior = n6;
                                                n7 = dameToken();
                                                crearArchivo(n6 + " ");
                                            do {
                                                if (n7.equals("modo")) {
                                                    a.crearNodo(n7);
                                                    a.unirNodos(anterior, n7);
                                                    if (auxEPA2)
                                                        recogerDatos("", "Bloque", "", "");
                                                    anterior = n7;
                                                    n8 = dameToken();
                                                    crearArchivo(n7 + " ");

                                                    do{
                                                    if (Identificador(n8) || n8.equals("si") || n8.equals("para") ||
                                                            n8.equals("repite") || n8.equals("hazlo_si") || n8.equals("seleccion") ||
                                                            n8.equals("leer") || n8.equals("escribir") || n8.equals("escribirSL") || Comentario(n8)) {
                                                        valores(n8);
                                                        n9 = dameToken();

                                                        bloque(n9);
                                                        n11 = dameToken();
                                                        while (Identificador(n11) || n11.equals("si") || n11.equals("para") ||
                                                                n11.equals("repite") || n11.equals("hazlo_si") || n11.equals("seleccion") ||
                                                                n11.equals("leer") || n11.equals("escribir") || n11.equals("escribirSL") || Comentario(n11)) {
                                                            bloque(n11);
                                                            n11 = dameToken();
                                                        }
                                                        do {
                                                            if (n11.equals("finpara")) {
                                                                a.crearNodo(n11);
                                                                a.unirNodos(anterior, n11);
                                                                if(auxEPA2)
                                                                recogerDatos("", "Bloque", "", "");
                                                                anterior = n11;
                                                                n10 = dameToken();
                                                                crearArchivo(n11+" ");
                                                                do {
                                                                    if (n10.equals(";")) {
                                                                        a.crearNodo(n10);
                                                                        a.unirNodos(anterior, n10);

                                                                        anterior = n10;
                                                                        paraSemantico = false;

                                                                        if(auxEPA2) {
                                                                            recogerDatos("", "Bloque", "", "");
                                                                            s.removerValoresPara();
                                                                        }
                                                                        crearArchivo(n10+"\n");
                                                                        auxEPA=false;


                                                                    } else {
                                                                        // System.out.print("Error ;");
                                                                        recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                                                        auxEPA2=false;
                                                                        ct--;
                                                                        n10=";";
                                                                    }
                                                                }while (auxEPA);

                                                            } else {
                                                                // System.out.print("Error finpara");
                                                                recibeErroresSintactico("Error falta finpara " + dameNumeroLinea());
                                                                auxEPA2=false;
                                                                ct--;
                                                                n11="finpara";
                                                            }
                                                        }while (auxEPA);

                                                    } else {
                                                        //   System.out.print("Error valores");
                                                        recibeErroresSintactico("Error en valores " + dameNumeroLinea());
                                                        ct--;
                                                        auxEPA2 = false;
                                                        n8 = "Identificador";
                                                    }
                                                }while (auxEPA);

                                                } else {
                                                    //System.out.print("Error modo");
                                                    recibeErroresSintactico("Error falta modo " + dameNumeroLinea());
                                                    ct--;
                                                    auxEPA2 = false;
                                                    n7="modo";
                                                }
                                            }while (auxEPA);
                                            } else {
                                                // System.out.print("Error numero largo");
                                                recibeErroresSintactico("Error falta numero largo " + dameNumeroLinea());
                                                ct--;
                                                auxEPA2 = false;
                                                n6 = "10";
                                            }
                                        }while (auxEPA);

                                    } else {
                                        ///System.out.print("Error hasta");
                                        recibeErroresSintactico("Error falta hasta " + dameNumeroLinea());
                                        ct--;
                                        auxEPA2 = false;
                                        n5 = "hasta";
                                    }
                                }while (auxEPA);
                                } else {
                                    //System.out.print("Error numero largo");
                                    recibeErroresSintactico("Error falta numero largo " + dameNumeroLinea());
                                    ct--;
                                    auxEPA2 = false;
                                    n4 = "10";
                                }
                            }while (auxEPA);
                            } else {
                                //System.out.print("Error :=");
                                recibeErroresSintactico("Error falta := " + dameNumeroLinea());
                                ct--;
                                auxEPA2 = false;
                                n3 = ":=";
                            }
                        }while (auxEPA);
                    } else {
                        //System.out.print("Error tipo numero largo");
                        recibeErroresSintactico("Error falta numero largo " + dameNumeroLinea());
                        ct--;
                        auxEPA2 = false;
                        n2 = "largo";
                    }
                }while (auxEPA);
                } else {
                    //System.out.print("Error como");
                    recibeErroresSintactico("Error falta como " + dameNumeroLinea());
                    ct--;
                    auxEPA2 = false;
                    n1 = "como";
                }
            }while(auxEPA);
            } else {
                //System.out.print("Error identificador");
                recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                ct--;
                auxEPA2 = false;
                n = "Identificador";


            }
        }while (auxEPA);

        }else{
           //System.out.print("Error para");
            recibeErroresSintactico("Error en para "+dameNumeroLinea());
        }

    }

    public void valores(String op){
        String n,n4;
        auxE=true;

        if(Identificador(op)){
            a.crearNodo(op);
            a.unirNodos(anterior,op);
            recogerDatos("","Bloque","","");
            anterior=op;
            n=dameToken();
            crearArchivo(op+" ");

                s.verificaSiseImplemento(op, dameNumeroLinea());

        do {
            if (n.equals("++")) {
                a.crearNodo(n);
                a.unirNodos(anterior, n);
                if(auxE2)
                recogerDatos("", "Bloque", "", "");
                anterior = n;
                crearArchivo(n+"\n");
                auxE=false;


            } else if (n.equals("--")) {
                a.crearNodo(n);
                a.unirNodos(anterior, n);
                if(auxE2)
                recogerDatos("", "Bloque", "", "");
                anterior = n;
                crearArchivo(n+"\n");
                auxE=false;

            } else if (n.equals("(")) {
                a.crearNodo(n);
                a.unirNodos(anterior, n);
                if (auxE2)
                    recogerDatos("", "Bloque", "", "");
                anterior = n;
                n4 = dameToken();

                crearArchivo(n + " ");
            do{
                if (n4.equals(")")) {
                    a.crearNodo(n4);
                    a.unirNodos(anterior, n4);
                    if(auxE2)
                    recogerDatos("", "Bloque", "", "");
                    anterior = n4;
                    crearArchivo(n4+"\n");
                    auxE=false;

                } else {
                    // System.out.print("Error )");
                    recibeErroresSintactico("Error falta ) " + dameNumeroLinea());
                    ct--;
                    auxE2 = false;
                    n4 = ")";
                }
            }while (auxE);
            } else {
                // System.out.print("Error");
                recibeErroresSintactico("Error falta incremento o decremento " + dameNumeroLinea());
                ct--;
                auxE2=false;
                n="++";

            }
        }while (auxE);

        }
        else{
         //   System.out.print("Error valores");
            recibeErroresSintactico("Error en valores "+dameNumeroLinea());
        }
    }

    public void seleccion(String op){
        String n,n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12;

        auxE=true;

        if(op.equals("seleccion")){
            a.crearNodo(op);
            a.unirNodos(anterior,op);
            recogerDatos("","Bloque","","");
            crearArchivo(op+" ");
            anterior=op;
            n=dameToken();
            do {
                if (Identificador(n)) {
                    a.crearNodo(n);
                    a.unirNodos(anterior, n);
                    if (auxE2)
                        recogerDatos("", "Bloque", "", "");
                    anterior = n;
                    n1 = dameToken();

                    if(banderaProcedimiento){
                        s.verificarVariableProcedimiento(n,dameNumeroLinea());
                    }
                    else if(banderaFuncion){


                        s.verificarVariableFuncion(n,dameNumeroLinea());
                    }
                    else if(paraSemantico){

                        s.verificarVariablePara(n,dameNumeroLinea());
                    }
                    else{

                        s.verificarVariable(n,dameNumeroLinea());

                    }

                    crearArchivo(n + " ");

                    do {
                        if (n1.equals(":")) {
                            a.crearNodo(n1);
                            a.unirNodos(anterior, n1);
                            if (auxE2)
                                recogerDatos("", "Bloque", "", "");
                            anterior = n1;
                            n2 = dameToken();
                            crearArchivo(n1 + "\n");
                        do {
                            if (n2.equals("evalua")) {
                                a.crearNodo(n2);
                                a.unirNodos(anterior, n2);
                                if (auxE2)
                                    recogerDatos("", "Bloque", "", "");
                                anterior = n2;
                                n3 = dameToken();
                                crearArchivo(n2 + " ");
                        do{
                                if (Identificador(n3)) {
                                    a.crearNodo(n3);
                                    a.unirNodos(anterior, n3);
                                    if(auxE2)
                                    recogerDatos("", "Bloque", "", "");
                                    crearArchivo(n3+"\n");
                                    anterior = n3;

                                    if(banderaProcedimiento){
                                        s.verificarVariableProcedimiento(n3,dameNumeroLinea());
                                    }
                                    else if(banderaFuncion){


                                        s.verificarVariableFuncion(n3,dameNumeroLinea());
                                    }
                                    else if(paraSemantico){

                                        s.verificarVariablePara(n3,dameNumeroLinea());
                                    }
                                    else{

                                        s.verificarVariable(n3,dameNumeroLinea());

                                    }


                                    n3 = dameToken();
                                    do {
                                        if (n3.equals("inicio")) {
                                            a.crearNodo(n3);
                                            a.unirNodos(anterior, n3);
                                            if (auxE2)
                                                recogerDatos("", "Bloque", "", "");
                                            anterior = n3;
                                            n4 = dameToken();
                                            crearArchivo(n3 + "\n");

                                            if (Identificador(n4) || n4.equals("si") || n4.equals("para") ||
                                                    n4.equals("repite") || n4.equals("hazlo_si") || n4.equals("seleccion") ||
                                                    n4.equals("leer") || n4.equals("escribir") || n4.equals("escribirSL") || Comentario(n4)) {
                                                bloque(n4);
                                                n5 = dameToken();
                                                while (Identificador(n5) || n5.equals("si") || n5.equals("para") ||
                                                        n5.equals("repite") || n5.equals("hazlo_si") || n5.equals("seleccion") ||
                                                        n5.equals("leer") || n5.equals("escribir") || n5.equals("escribirSL") || Comentario(n5)) {
                                                    bloque(n5);
                                                    n5 = dameToken();
                                                }
                                                auxE = true;
                                            do{
                                                if (n5.equals("fin")) {
                                                    a.crearNodo(n5);
                                                    a.unirNodos(anterior, n5);
                                                    if(auxE2)
                                                    recogerDatos("", "Bloque", "", "");
                                                    anterior = n5;
                                                    n6 = dameToken();
                                                    crearArchivo(n5+" ");
                                            do {
                                                if (n6.equals(";")) {
                                                    a.crearNodo(n6);
                                                    a.unirNodos(anterior, n6);
                                                    if (auxE2)
                                                        recogerDatos("", "Bloque", "", "");
                                                    anterior = n6;
                                                    n7 = dameToken();
                                                    crearArchivo(n6 + "\n");
                                            do {
                                                if (n7.equals("por_omision")) {
                                                    a.crearNodo(n7);
                                                    a.unirNodos(anterior, n7);
                                                    if (auxE2)
                                                        recogerDatos("", "Bloque", "", "");

                                                    crearArchivo(n7 + "\n");
                                                    anterior = n7;
                                                    n8 = dameToken();


                                                    if (Identificador(n8) || n8.equals("si") || n8.equals("para") ||
                                                            n8.equals("repite") || n8.equals("hazlo_si") || n8.equals("seleccion") ||
                                                            n8.equals("leer") || n8.equals("escribir") || n8.equals("escribirSL") || Comentario(n8)) {
                                                        bloque(n8);
                                                        n9 = dameToken();
                                                        while (Identificador(n9) || n9.equals("si") || n9.equals("para") ||
                                                                n9.equals("repite") || n9.equals("hazlo_si") || n9.equals("seleccion") ||
                                                                n9.equals("leer") || n9.equals("escribir") || n9.equals("escribirSL") || Comentario(n9)) {
                                                            bloque(n9);
                                                            n9 = dameToken();
                                                        }
                                                        auxE=true;
                                                        do {
                                                            if (n9.equals("fin")) {
                                                                a.crearNodo(n9);
                                                                a.unirNodos(anterior, n9);
                                                                if (auxE2)
                                                                    recogerDatos("", "Bloque", "", "");
                                                                anterior = n9;
                                                                n10 = dameToken();
                                                                crearArchivo(n9 + " ");

                                                            do {

                                                                if (n10.equals(";")) {
                                                                    a.crearNodo(n10);
                                                                    a.unirNodos(anterior, n10);
                                                                    if (auxE2)
                                                                        recogerDatos("", "Bloque", "", "");
                                                                    anterior = n10;
                                                                    n11 = dameToken();

                                                                    crearArchivo(n10 + "\n");

                                                                    do {
                                                                    if (n11.equals("finsel")) {
                                                                        a.crearNodo(n11);
                                                                        a.unirNodos(anterior, n11);
                                                                        if (auxE2)
                                                                            recogerDatos("", "Bloque", "", "");
                                                                        anterior = n11;
                                                                        n12 = dameToken();
                                                                        crearArchivo(n11 + " ");
                                                                do {
                                                                    if (n12.equals(";")) {
                                                                        a.crearNodo(n12);
                                                                        a.unirNodos(anterior, n12);
                                                                        if(auxE2)
                                                                        recogerDatos("", "Bloque", "", "");
                                                                        anterior = n12;
                                                                        crearArchivo(n12+"\n");
                                                                        auxE=false;

                                                                    } else {
                                                                        //System.out.print("Error ;");
                                                                        recibeErroresSintactico("Error en ; " + dameNumeroLinea());
                                                                        ct--;
                                                                        auxE2=false;
                                                                        n12=";";
                                                                    }
                                                                }while (auxE);

                                                                    } else {
                                                                        recibeErroresSintactico("Error falta finsel " + dameNumeroLinea());
                                                                        ct--;
                                                                        auxE2 = false;
                                                                        n11 = "finsel";
                                                                    }
                                                                }while (auxE);
                                                                } else {
                                                                    //System.out.print("Error ;");
                                                                    recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                                                    ct--;
                                                                    auxE2 = false;
                                                                    n10 = ";";
                                                                }
                                                            }while (auxE);

                                                            } else {
                                                                // System.out.print("Error fin");
                                                                recibeErroresSintactico("Error falta fin " + dameNumeroLinea());
                                                                ct--;
                                                                auxE2 = false;
                                                                n9 = "fin";
                                                            }
                                                        }while (auxE);


                                                    } else {
                                                        //System.out.print("Error bloque");
                                                        recibeErroresSintactico("Error en bloque " + dameNumeroLinea());
                                                        crearArchivo("bloque\nfin;\nfinsel;\ninicio\nbloque\n");
                                                        auxE=false;
                                                    }
                                                } else {
                                                    // System.out.print("Error por omision");
                                                    recibeErroresSintactico("Error falta por_omision " + dameNumeroLinea());
                                                    ct--;
                                                    auxE2 = false;
                                                    n7 = "por_omision";
                                                }
                                            }while (auxE);


                                                } else {
                                                    //System.out.print("Error en ;");
                                                    recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                                    ct--;
                                                    auxE2 = false;
                                                    n6 = ";";
                                                }
                                            }while(auxE);

                                                } else {
                                                    //System.out.print("Error en fin");
                                                    recibeErroresSintactico("Error falta fin " + dameNumeroLinea());
                                                    ct--;
                                                    auxE2 = false;
                                                    n5 = "fin";
                                                }
                                            }while (auxE);

                                            } else {
                                                //   System.out.print("Error");
                                                recibeErroresSintactico("Error en bloque "+dameNumeroLinea());
                                                crearArchivo("bloque\nfin;\npor_omision\nbloque\nfin;\nfinsel;\ninicio\nbloque\n");
                                                auxE=false;
                                            }

                                        } else {
                                            //System.out.print("Error en inicio");
                                            recibeErroresSintactico("Error falta inicio " + dameNumeroLinea());
                                            ct--;
                                            auxE2 = false;
                                            n3="inicio";

                                        }
                                    }while (auxE);
                                } else {
                                    //System.out.print("Error Identificador");
                                    recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                                    ct--;
                                    auxE2 = false;
                                    n3 = "Identificador";
                                }
                            }while (auxE);


                            } else {
                                //System.out.print("Error");
                                recibeErroresSintactico("Error falta evalua " + dameNumeroLinea());
                                ct--;
                                auxE2 = false;
                                n2 = "evalua";
                            }
                        }while (auxE);
                        } else {
                            //System.out.print("Error");
                            recibeErroresSintactico("Error falta : " + dameNumeroLinea());
                            ct--;
                            auxE2 = false;
                            n1 = ":";
                        }
                    }while(auxE);
                } else {
                    //System.out.print("Error en seleccon");
                    recibeErroresSintactico("Error falta identicador " + dameNumeroLinea());
                    ct--;
                    auxE2=false;
                    n="Identificador";
                }

            }while (auxE);




        }
        else{
            //System.out.print("Error seleccion");
            recibeErroresSintactico("Error en seleccion "+dameNumeroLinea());
        }
    }

    public void condicionante(String arg){

        String aux;

        if(arg.equals("(")){

                a.crearNodo(arg);
                a.unirNodos(anterior,arg);
                recogerDatos("","Bloque","","");
                anterior=arg;
                crearArchivo(arg+" ");
                aux=dameToken();

                condicion(aux);



                aux=dameToken();
                do {
                    if (aux.equals(")")) {
                        a.crearNodo(aux);
                        a.unirNodos(anterior, aux);
                        if(auxE2)
                        recogerDatos("", "Bloque", "", "");
                        anterior = aux;
                        crearArchivo(aux + " ");
                        auxE = false;

                        aux=dameToken();

                        if(aux.equals("y") || aux.equals("o")){
                            a.crearNodo(aux);
                            a.unirNodos(anterior,aux);
                            recogerDatos("","Bloque","","");
                            anterior=aux;
                            crearArchivo(aux+" ");

                            aux=dameToken();
                            condicionante(aux);

                        }
                        else{
                            ct--;
                        }




                    } else {

                        recibeErroresSintactico("Error falta un  ) " + dameNumeroLinea());
                        auxE2 = false;
                        aux=")";
                        ct--;

                    }
                }while (auxE);

        }
        else{
            condicion(arg);

            aux=dameToken();

            if(aux.equals("y") || aux.equals("o")){
                a.crearNodo(aux);
                a.unirNodos(anterior,aux);
                recogerDatos("","Bloque","","");
                anterior=aux;
                crearArchivo(aux+" ");

                aux=dameToken();
                condicionante(aux);

            }
            else{
                ct--;

            }


        }


    }


    public void condicionSi(String op){
        String n,n1,n2,n3,n4,n5,n6,n7,n8;

        auxE=true;

        if(op.equals("si")) {
            a.crearNodo(op);
            a.unirNodos(anterior, op);
            recogerDatos("", "Bloque", "", "");
            anterior = op;
            n = dameToken();
            crearArchivo(op + " ");
        do{
            if (n.equals("(")) {
                a.crearNodo(n);
                a.unirNodos(anterior, n);
                if(auxE2)
                recogerDatos("", "Bloque", "", "");
                anterior = n;
                n1 = dameToken();
                crearArchivo(n+"");
            do {
                if (Identificador(n1) || n1.equals("no") || n1.equals("(")) {

                    condicionante(n1);
                    auxE=true;
                    n2 = dameToken();


                do {
                    if (n2.equals(")")) {

                        a.crearNodo(n2);
                        a.unirNodos(anterior, n2);
                        if(auxE2)
                        recogerDatos("", "Bloque", "", "");
                        anterior = n2;
                        n3 = dameToken();
                        crearArchivo(n2+" ");

                    do {
                        if (n3.equals("entonces")) {
                            //bloques
                            a.crearNodo(n3);
                            a.unirNodos(anterior, n3);
                            if (auxE2)
                                recogerDatos("", "Bloque", "", "");
                            anterior = n3;
                            n4 = dameToken();
                            crearArchivo(n3 + "\n");

                            if (Identificador(n4) || n4.equals("si") || n4.equals("para") ||
                                    n4.equals("repite") || n4.equals("hazlo_si") || n4.equals("seleccion") ||
                                    n4.equals("leer") || n4.equals("escribir") || n4.equals("escribirSL") || Comentario(n4)) {

                                bloque(n4);


                                n5 = dameToken();
                                while (Identificador(n5) || n5.equals("si") || n5.equals("para") ||
                                        n5.equals("repite") || n5.equals("hazlo_si") || n5.equals("seleccion") ||
                                        n5.equals("leer") || n5.equals("escribir") || n5.equals("escribirSL") || Comentario(n5)) {
                                    bloque(n5);//bloque
                                    n5 = dameToken();
                                }
                                auxE=true;
                                do {
                                    if (n5.equals("finsi")) {
                                        a.crearNodo(n5);
                                        a.unirNodos(anterior, n5);
                                        if (auxE2)
                                            recogerDatos("", "Bloque", "", "");
                                        anterior = n5;
                                        n6 = dameToken();
                                        crearArchivo(n5 + " ");
                                    do{
                                        if (n6.equals(";")) {
                                            a.crearNodo(n6);
                                            a.unirNodos(anterior, n6);
                                            if(auxE2)
                                            recogerDatos("", "Bloque", "", "");
                                            anterior = n6;
                                            crearArchivo(";\n");
                                            auxE=false;

                                        } else {
                                            //System.out.print("Error ;");
                                            recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                            ct--;
                                            auxE2=false;
                                            n6=";";
                                        }
                                    }while (auxE);
                                    } else if (n5.equals("sino")) {
                                        a.crearNodo(n5);
                                        a.unirNodos(anterior, n5);
                                        if(auxE2)
                                        recogerDatos("", "Bloque", "", "");
                                        anterior = n5;
                                        n6 = dameToken();
                                        crearArchivo(n5+"\n");

                                        if (n6.equals("si")) {
                                            condicionSi(n6);

                                        } else if (Identificador(n6) || n6.equals("si") || n6.equals("para") ||
                                                n6.equals("repite") || n6.equals("hazlo_si") || n6.equals("seleccion") ||
                                                n6.equals("leer") || n6.equals("escribir") || n6.equals("escribirSL") || Comentario(n6)) {
                                            bloque(n6);

                                            n7 = dameToken();
                                            while (Identificador(n7) || n7.equals("si") || n7.equals("para") ||
                                                    n7.equals("repite") || n7.equals("hazlo_si") || n7.equals("seleccion") ||
                                                    n7.equals("leer") || n7.equals("escribir") || n7.equals("escribirSL") || Comentario(n7)) {
                                                bloque(n7);
                                                n7 = dameToken();
                                            }
                                            do {
                                                auxE=true;
                                                if (n7.equals("finsi")) {
                                                    a.crearNodo(n7);
                                                    a.unirNodos(anterior, n7);
                                                    if (auxE2)
                                                        recogerDatos("", "Bloque", "", "");
                                                    anterior = n7;
                                                    n8 = dameToken();
                                                    crearArchivo("finsi ");
                                                do{
                                                    if (n8.equals(";")) {
                                                        a.crearNodo(n8);
                                                        a.unirNodos(anterior, n8);
                                                        if(auxE2)
                                                        recogerDatos("", "Bloque", "", "");
                                                        anterior = n8;
                                                        crearArchivo(n8+"\n");
                                                        auxE=false;
                                                    } else {
                                                        // System.out.print("Error ;");
                                                        recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                                        auxE2=false;
                                                        ct--;
                                                        n8=";";
                                                    }
                                                }while (auxE);
                                                } else {
                                                    // System.out.print("Error finsi");
                                                    recibeErroresSintactico("Error falta finsi " + dameNumeroLinea());
                                                    ct--;
                                                    auxE2=false;
                                                    n7="finsi";
                                                }
                                            }while (auxE);
                                        } else {
                                            //System.out.print("Error si");
                                            recibeErroresSintactico("Error en bloque " + dameNumeroLinea());
                                            crearArchivo("bloque\nfinsi\ninicio\nbloque\n");
                                            auxE=false;
                                        }

                                    } else {
                                        // System.out.print("Error finsi");
                                        recibeErroresSintactico("Error falta finsi " + dameNumeroLinea());
                                        ct--;
                                        auxE2=false;
                                        n5="finsi";
                                    }
                                }while (auxE);


                            } else {
                                //  System.out.print("Error indetificador");
                                recibeErroresSintactico("Error en bloque " + dameNumeroLinea());
                                crearArchivo("bloque\nfinsi;\ninicio\nbloque\n");
                                auxE=false;

                            }


                        } else {
                            //System.out.print("Error en entonces");
                            recibeErroresSintactico("Error falta entonces " + dameNumeroLinea());
                            ct--;
                            auxE2 = false;
                            n3 = "entonces";
                        }
                    }while (auxE);
                    } else {
                        // System.out.print("Error )");
                        recibeErroresSintactico("Error falta ) " + dameNumeroLinea());
                        ct--;
                        auxE2=false;
                        n2=")";
                    }
                }while (auxE);
                } else {
                    // System.out.print("Comparador");
                    recibeErroresSintactico("Error en comparador " + dameNumeroLinea());
                    ct--;
                    auxE2 = false;
                    n1 = "Identificador";
                }
            }while (auxE);


            } else {
                //System.out.print("Error en (");
                recibeErroresSintactico("Error falta ( " + dameNumeroLinea());
                ct--;
                auxE2 = false;
                n = "(";
            }
        }while(auxE);

        }
        else{
           // System.out.print("Error en si");
            recibeErroresSintactico("Error en si "+dameNumeroLinea());
        }
    }

    public void condicion(String op){
        GeneracionIntermedia o= new GeneracionIntermedia();
        String n,n1,n2,n3,n4,n5,n6;

        auxE=true;
    do {
        if (op.equals("no")) {
            a.crearNodo(op);
            a.unirNodos(anterior, op);
            if (auxE2)
                recogerDatos("", "Bloque", "", "");
            anterior = op;
            n = dameToken();
            crearArchivo(op + " ");

            do {
                if (n.equals("(")) {
                    a.crearNodo(n);
                    a.unirNodos(anterior, n);
                    if (auxE2)
                        recogerDatos("", "Bloque", "", "");
                    anterior = n;
                    n3 = dameToken();
                    crearArchivo(n + " ");

                    do {
                        if (Identificador(n3)) {
                            operadorIdentificador(n3);
                            n4 = dameToken();
                            o.agregarCondicional(n3);

                            if (banderaProcedimiento) {
                                s.verificarVariableProcedimiento(n3, dameNumeroLinea());
                            } else if (banderaFuncion) {
                                s.verificarVariableFuncion(n3, dameNumeroLinea());
                            } else if (paraSemantico) {
                                s.verificarVariablePara(n3, dameNumeroLinea());
                            } else {
                                s.verificarVariable(n3, dameNumeroLinea());
                            }

                            do {
                                if (Comparador(n4) || n4.equals("y") || n4.equals("o")) {
                                    a.crearNodo(n4);
                                    a.unirNodos(anterior, n4);
                                    if (auxE2)
                                        recogerDatos("", "Bloque", "", "");
                                    anterior = n4;
                                    n5 = dameToken();
                                    crearArchivo(n4 + " ");
                                    do {
                                        if (Identificador(n5)) {
                                            operadorIdentificador(n5);
                                            n6 = dameToken();
                                            o.verificarCondicional(n5, dameNumeroLinea());
                                            if (banderaProcedimiento) {
                                                s.verificarVariableProcedimiento(n5, dameNumeroLinea());
                                            } else if (banderaFuncion) {
                                                s.verificarVariableFuncion(n5, dameNumeroLinea());
                                            } else if (paraSemantico) {
                                                s.verificarVariablePara(n5, dameNumeroLinea());
                                            } else {
                                                s.verificarVariable(n5, dameNumeroLinea());
                                            }
                                            do {
                                                if (n6.equals(")")) {
                                                    a.crearNodo(n6);
                                                    a.unirNodos(anterior, n6);
                                                    if (auxE2)
                                                        recogerDatos("", "Bloque", "", "");
                                                    anterior = n6;

                                                    crearArchivo(n6 + "");

                                                    auxE = false;


                                                } else {
                                                    //  System.out.print("Error )");
                                                    recibeErroresSintactico("Error falta ) " + dameNumeroLinea());
                                                    ct--;
                                                    auxE2 = false;
                                                    n6 = ")";
                                                }
                                            } while (auxE);

                                        } else {
                                            //    System.out.print("Error operador");
                                            recibeErroresSintactico("Error falta Operador " + dameNumeroLinea());
                                            auxE2 = false;
                                            ct--;
                                            n5 = "Operador";
                                        }
                                    } while (auxE);
                                } else {
                                    //     System.out.print("Error");
                                    recibeErroresSintactico("Error falta comparador " + dameNumeroLinea());
                                    ct--;
                                    auxE2 = false;
                                    n4 = "<";
                                }
                            } while (auxE);


                        } else {
                            // System.out.print("Error en operador");
                            recibeErroresSintactico("Error falta Operador " + dameNumeroLinea());
                            ct--;
                            auxE2 = false;
                            n3 = "Operador";
                        }
                    } while (auxE);


                } else {
                    // System.out.print("Error (");
                    recibeErroresSintactico("Error falta ( " + dameNumeroLinea());
                    ct--;
                    auxE2 = false;
                    n = "(";
                }
            } while (auxE);


        } else if (Identificador(op)) {
            operadorIdentificador(op);
            n1 = dameToken();
            o.agregarCondicional(op);

            if (banderaProcedimiento) {
                s.verificarVariableProcedimiento(op, dameNumeroLinea());
            } else if (banderaFuncion) {
                s.verificarVariableFuncion(op, dameNumeroLinea());
            } else if (paraSemantico) {
                s.verificarVariablePara(op, dameNumeroLinea());
            } else {
                s.verificarVariable(op, dameNumeroLinea());
            }
        do {
            if (Comparador(n1) || n1.equals("y") || n1.equals("o")) {
                a.crearNodo(n1);
                a.unirNodos(anterior, n1);
                if(auxE2)
                recogerDatos("", "Bloque", "", "");
                anterior = n1;
                n2 = dameToken();
                crearArchivo(n1+" ");
            do {
                if (Identificador(n2)) {
                    operadorIdentificador(n2);
                    o.verificarCondicional(n2, dameNumeroLinea());

                    if(auxE2) {
                    if (banderaProcedimiento) {
                        s.verificarVariableProcedimiento(n2, dameNumeroLinea());
                    } else if (banderaFuncion) {
                        s.verificarVariableFuncion(n2, dameNumeroLinea());
                    } else if (paraSemantico) {
                        s.verificarVariablePara(n2, dameNumeroLinea());
                    } else {
                        s.verificarVariable(n2, dameNumeroLinea());
                    }
                }

                    auxE=false;
                } else {
                    //System.out.print("Error en comparador");
                    recibeErroresSintactico("Error falta Identificador " + dameNumeroLinea());
                    ct--;
                    auxE2=false;
                    n2="Operador";

                }
            }while (auxE);
            } else {
                //System.out.print("Error comparador");
                recibeErroresSintactico("Error falta comparador " + dameNumeroLinea());
                ct--;
                auxE2=false;
                n1="<";
            }
        }while (auxE);
        } else {
            //  System.out.print("Error en condicion");
            recibeErroresSintactico("Error falta Identificador " + dameNumeroLinea());
            ct--;
            auxE2 = false;
            op = "Identificador";
        }
    }while (auxE);
    }

    public void llamarFuncion(String op){
        String n1,n2;
            if(op.equals("(")){
                a.crearNodo(op);
                a.unirNodos(anterior,op);
                anterior=op;
                n1=dameToken();
                if(n1.equals(")")){
                    a.crearNodo(n1);
                    a.unirNodos(anterior,n1);
                    anterior=n1;
                    n2=dameToken();
                }
                else{
                    System.out.print("en )");
                }

            }
            else{
                System.out.print("Error (");
            }

    }
    public void operadorIdentificador(String op){



            if (Identificador(op)) {
                a.crearNodo(op);
                a.unirNodos(anterior, op);
                if(auxE2)
                recogerDatos("", "Bloque", "", "");
                anterior = op;
                crearArchivo(op+" ");
            } else {
                recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
            }

    }

    int contFunc=1;
    boolean banderaFuncion=false;
    boolean auxEF=true,auxE2F=true;


    public void funcion(String op){
        String n,n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16;

        auxEF=true;
        auxE2F=true;

        String numero=Integer.toString(contFunc);

        if(op.equals("funcion")){

            contFunc++;
            a.crearNodo("<Funcion "+numero+" >");
            a.unirNodos("seccion","<Funcion "+numero+" >");
            recogerDatos("","Funcion","","");
            a.crearNodo(op);
            a.unirNodos("<Funcion "+numero+" >",op);
            anterior=op;
            n=dameToken();
            crearArchivo(op+" ");

            do{
            if(Identificador(n)) {
                a.crearNodo(n);
                a.unirNodos(anterior, n);


                anterior = n;
                n1 = dameToken();
                banderaFuncion = true;

                if (auxE2F) {
                    recogerDatos("", "Funcion", "", "");
                    s.verificaInicializaFuncion(n, dameNumeroLinea());
                }

                crearArchivo(n + " ");

                do {
                    if (n1.equals("(")) {
                        a.crearNodo(n1);
                        a.unirNodos(anterior, n1);

                            if (auxE2F) {
                                recogerDatos("", "Funcion", "", "");
                            }

                        crearArchivo(n1 + " ");
                        anterior = n1;
                        n2 = dameToken();
                    do{
                        if (TipoDeDato(n2)) {

                            a.crearNodo(n2);
                            a.unirNodos(anterior, n2);

                            if(auxE2F)
                            recogerDatos("", "Funcion", "", "");

                            anterior = n2;
                            n3 = dameToken();
                            crearArchivo(n2+" ");

                            do {
                                if (Identificador(n3)) {

                                    a.crearNodo(n3);
                                    a.unirNodos(anterior, n3);

                                    anterior = n3;
                                    n4 = dameToken();

                                    if (auxE2F) {
                                        recogerDatos("", "Parametro", "", "");
                                        s.verificaFuncion(n3, n2, dameNumeroLinea());
                                    }
                                    crearArchivo(n3 + " ");


                                    do {
                                        if (n4.equals(")")) {
                                            a.crearNodo(n4);
                                            a.unirNodos(anterior, n4);

                                            if (auxE2F)
                                                recogerDatos("", "Funcion", "", "");

                                            anterior = n4;

                                            crearArchivo(n4 + " ");

                                            n5 = dameToken();

                                            do {
                                                if (n5.equals("como")) {

                                                    a.crearNodo(n5);
                                                    a.unirNodos(anterior, n5);

                                                    if (auxE2F)
                                                        recogerDatos("", "Funcion", "", "");

                                                    anterior = n5;
                                                    n11 = dameToken();

                                                    crearArchivo(n5+" ");


                                                    do {
                                                        if (TipoDeDato(n11)) {
                                                            a.crearNodo(n11);
                                                            a.unirNodos(anterior, n11);

                                                            if(auxE2F)
                                                            recogerDatos("", "Funcion", "", "");

                                                            anterior = n11;
                                                            n12 = dameToken();

                                                            crearArchivo(n11+" ");

                                                            do {
                                                                if (n12.equals("var")) {
                                                                    auxEF=false;
                                                                    verificaVarFuncion(n12);
                                                                } else {
                                                                    // System.out.print("en var");
                                                                    recibeErroresSintactico("Error falta var " + dameNumeroLinea());
                                                                    ct--;
                                                                    n12="var";
                                                                    auxE2F=false;
                                                                }
                                                            }while (auxEF);

                                                        } else {
                                                            //System.out.print("Error en tipo de dato");
                                                            recibeErroresSintactico("Error falta tipo de dato " + dameNumeroLinea());
                                                            ct--;
                                                            auxE2F=false;
                                                            n11="entero";
                                                        }
                                                    }while (auxEF);


                                                } else {
                                                    //System.out.print("Error en como");
                                                    recibeErroresSintactico("Error falta como " + dameNumeroLinea());
                                                    ct--;
                                                    auxE2F=false;
                                                    n5="como";

                                                }
                                            }while (auxEF);

                                        } else if (n4.equals(",")) {


                                            a.crearNodo(n4);
                                            a.unirNodos(anterior, n4);

                                            if (auxE2F)
                                                recogerDatos("", "Funcion", "", "");

                                            anterior = n4;
                                            n7 = dameToken();
                                            crearArchivo(n4 + " ");


                                            do{
                                            if (TipoDeDato(n7)) {

                                                a.crearNodo(n7);
                                                a.unirNodos(anterior, n7);

                                                if(auxE2F)
                                                recogerDatos("", "Funcion", "", "");

                                                anterior = n7;
                                                n8 = dameToken();
                                                crearArchivo(n7+" ");

                                                do{
                                                if (Identificador(n8)) {
                                                    a.crearNodo(n8);
                                                    a.unirNodos(anterior, n8);
                                                    if(auxE2F)
                                                    recogerDatos("", "Parametro", "", "");
                                                    crearArchivo(n8+" ");
                                                    anterior = n8;
                                                    n9 = dameToken();
                                                    s.verificaFuncion(n8, n7, dameNumeroLinea());

                                                    do{
                                                    if (n9.equals(")")) {
                                                        a.crearNodo(n9);
                                                        a.unirNodos(anterior, n9);

                                                        if(auxE2F)
                                                        recogerDatos("", "Funcion", "", "");
                                                        crearArchivo(n9+" ");
                                                        anterior = n9;
                                                        n10 = dameToken();

                                                        do{
                                                        if (n10.equals("como")) {
                                                            a.crearNodo(n10);
                                                            a.unirNodos(anterior, n10);
                                                            if(auxE2F)
                                                            recogerDatos("", "Funcion", "", "");

                                                            crearArchivo(n10+" ");
                                                            anterior = n10;
                                                            n13 = dameToken();

                                                            do{
                                                            if (TipoDeDato(n13)) {
                                                                a.crearNodo(n13);
                                                                a.unirNodos(anterior, n13);

                                                                if(auxE2F)
                                                                recogerDatos("", "Funcion", "", "");

                                                                crearArchivo(n13+"\n");
                                                                anterior = n13;
                                                                n14 = dameToken();

                                                                do {
                                                                    if (n14.equals("var")) {
                                                                        auxEF=false;
                                                                        verificaVarFuncion(n14);
                                                                    } else {
                                                                        //   System.out.print("en var");
                                                                        recibeErroresSintactico("Error falta var " + dameNumeroLinea());
                                                                        ct--;
                                                                        auxE2F=false;
                                                                        n14="var";
                                                                    }
                                                                }while (auxEF);

                                                            } else {
                                                                //   System.out.print("Error en tipo de dato");
                                                                recibeErroresSintactico("Error falta tipo de dato " + dameNumeroLinea());
                                                                ct--;
                                                                auxE2F=false;
                                                                n13="entero";

                                                            }
                                                            }while (auxEF);


                                                        } else {
                                                            //  System.out.print("Error en como");
                                                            recibeErroresSintactico("Error falta como " + dameNumeroLinea());
                                                            auxE2F=false;
                                                            ct--;
                                                            n10="como";

                                                        }
                                                        }while (auxEF);

                                                    } else {
                                                        //  System.out.print("Error en )");

                                                        recibeErroresSintactico("Error falta ) " + dameNumeroLinea());
                                                        auxE2F=false;
                                                        ct--;
                                                        n9=")";


                                                    }
                                                    }while (auxEF);

                                                } else {
                                                    // System.out.print("Error en identificador");
                                                    recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                                                    ct--;
                                                    auxE2F=false;
                                                    n8="Identificador";

                                                }
                                                }while (auxEF);

                                            } else {
                                                //System.out.print("en tipo de dato");
                                                recibeErroresSintactico("Error falta tipo de dato " + dameNumeroLinea());
                                                ct--;
                                                auxE2F=false;
                                                n7="entero";



                                            }
                                            }while (auxEF);
                                        } else {
                                            //  System.out.print("Error ");
                                             recibeErroresSintactico("Error falta ) "+dameNumeroLinea());
                                            ct--;
                                            n4=")";
                                            auxE2F=false;
                                        }

                                    }while (auxEF);

                                } else {

                                    recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                                    ct--;
                                    n3="Identificador";
                                    auxE2F=false;

                                }
                            }while (auxEF);
                        }
                        else if (n2.equals(")")) {
                            a.crearNodo(n2);
                            a.unirNodos(anterior, n2);

                            if(auxE2F)
                            recogerDatos("", "Funcion", "", "");

                            anterior = n2;
                            n6 = dameToken();
                            crearArchivo(n2+" ");
do{
                            if (n6.equals("como")) {
                                a.crearNodo(n6);
                                a.unirNodos(anterior, n6);
                                if(auxE2F)
                                recogerDatos("", "Funcion", "", "");

                                crearArchivo(n6+" ");
                                anterior = n6;
                                n15 = dameToken();
                                do{
                                if (TipoDeDato(n15)) {
                                    a.crearNodo(n15);
                                    a.unirNodos(anterior, n15);

                                    anterior = n15;
                                    n16 = dameToken();


                                    //System.out.println(dameID());
                                    if(auxE2F && auxE2) {
                                        recogerDatos("", "Funcion", "", "");
                                        s.dameKey(n, dameID(), "5");
                                        s.agregarMemoriaFuncion(n15);
                                    }
                                    crearArchivo(n15+"\n");

                                    do {
                                        if (n16.equals("var")) {
                                            auxEF=false;
                                            verificaVarFuncion(n16);
                                        } else {
                                            //System.out.print("en var");
                                            recibeErroresSintactico("Error falta var " + dameNumeroLinea());
                                            ct--;
                                            n16="var";
                                            auxE2F=false;

                                        }
                                    }while (auxEF);

                                } else {
                                    //System.out.print("Error en tipo de dato");
                                    recibeErroresSintactico("Error falta tipo de dato " + dameNumeroLinea());
                                    ct--;
                                    n15="entero";
                                    auxE2F=false;

                                }
                                }while (auxEF);

                            } else {
                                //System.out.print("Error en como");
                                recibeErroresSintactico("Error falta como " + dameNumeroLinea());
                                ct--;
                                n6="como";
                                auxE2F=false;
                            }
}while (auxEF);

                        } else {
                            //  System.out.print("Error");
                            recibeErroresSintactico("Error falta ) " + dameNumeroLinea());
                            ct--;
                            n2 = ")";
                            auxE2F = false;
                        }
                    }while (auxEF);
                    } else {
                        // System.out.print("Error (");
                        recibeErroresSintactico("Error falta ( " + dameNumeroLinea());
                        ct--;
                        n1="(";
                        auxE2F=false;

                    }
                }while (auxEF);
                }

            else{
                    //System.out.print("Error identificador");
                    recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                    ct--;
                    n="Identificador";
                    auxE2F=false;


                }
            }while(auxEF);
        }
        else{
            //System.out.print("No hay procedimiento");
            recibeErroresSintactico("Error en funcion "+dameNumeroLinea());
        }
        banderaFuncion=false;
        s.removerValoresFuncion();
    }

    public void verificaVarFuncion(String op){
        String nombre,nombre2;

        auxEF=true;

        if(op.equals("var")) {

            a.crearNodo(op);
            a.unirNodos(anterior, op);
            recogerDatos("", "Funcion", "", "");
            anterior = op;
            nombre = dameToken();
            crearArchivo(op + "\n");

            do {

                if (Identificador(nombre)) {

                    valorIdentificadorFuncion(nombre);

                    nombre2 = dameToken();

                    do{
                    while (Identificador(nombre2)) {
                        auxEF=false;


                        valorIdentificadorFuncion(nombre2);
                        nombre2 = dameToken();

                    }
                    if (nombre2.equals("seccion") || nombre2.equals("inicio")) {

                        seccionFuncion(nombre2);
                    } else {
                        recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                        ct--;
                        nombre2 = "Identificador";

                        auxE2F = false;
                    }
                }while (auxEF);

                } else {
                    recibeErroresSintactico("Error falta identificador  " + dameNumeroLinea());
                    ct--;
                    nombre="Identificador";

                    auxE2F=false;

                }
            }while (auxEF);

        }

        else{
           // System.out.print("en var");
            recibeErroresSintactico("Error en var "+dameNumeroLinea());
        }
    }


    boolean banderaProcedimiento=false;
    int conPro=1;
    boolean auxEP=true,auxE2P=true;


    public void procedimiento(String op){
        String n,n1,n2,n3,n4,n5,n6,n7,n8,n9,n10;

        auxEP=true;
        auxE2P=true;

        if(op.equals("procedimiento")) {
            String nombre = Integer.toString(conPro);
            a.crearNodo("<Procedimiento " + nombre + " >");
            conPro++;
            a.unirNodos("seccion", "<Procedimiento " + nombre + " >");
            a.crearNodo(op);
            a.unirNodos("<Procedimiento " + nombre + " >", op);
            recogerDatos("", "Procedimiento", "", "");
            anterior = op;
            n = dameToken();

            crearArchivo(op + " ");
do{
            if (Identificador(n)) {
                a.crearNodo(n);
                a.unirNodos(anterior, n);

                anterior = n;
                n1 = dameToken();
                crearArchivo(n+" ");

                banderaProcedimiento = true;

                if(auxE2P) {
                    recogerDatos("", "Procedimiento", "", "");
                    s.verificaInicializaProcedimiento(n, dameNumeroLinea());
                }
do {
    if (n1.equals("(")) {
        a.crearNodo(n1);
        a.unirNodos(anterior, n1);
        if (auxE2P)
            recogerDatos("", "Procedimiento", "", "");
        anterior = n1;
        crearArchivo(n1 + " ");
        n2 = dameToken();
do{
        if (TipoDeDato(n2)) {
            a.crearNodo(n2);
            a.unirNodos(anterior, n2);

            if(auxE2P)
            recogerDatos("", "Procedimiento", "", "");

            crearArchivo(n2+" ");
            anterior = n2;
            n3 = dameToken();


            do {
                if (Identificador(n3)) {

                    a.crearNodo(n3);
                    a.unirNodos(anterior, n3);

                    anterior = n3;
                    n4 = dameToken();

                    if (auxE2P) {
                        recogerDatos("", "Parametro", "", "");
                        s.verificaProcedimiento(n3, n2, dameNumeroLinea());
                    }
                    crearArchivo(n3 + " ");
do{
                    if (n4.equals(")")) {
                        a.crearNodo(n4);
                        a.unirNodos(anterior, n4);

                        if(auxE2P)
                        recogerDatos("", "Procedimiento", "", "");

                        crearArchivo(n4+" ");
                        anterior = n4;
                        n5 = dameToken();
                        do {
                            if (n5.equals("var")) {
                                auxEP=false;
                                verificaVarProcedimiento(n5);
                            } else {
                                //System.out.print("en var");
                                recibeErroresSintactico("Error falta var " + dameNumeroLinea());
                                ct--;
                                auxE2P=false;
                                n5="var";
                            }
                        }while (auxEP);

                    } else if (n4.equals(",")) {
                        //<----aqui
                        a.crearNodo(n4);
                        a.unirNodos(anterior, n4);
                        recogerDatos("", "Procedimiento", "", "");
                        anterior = n4;
                        n7 = dameToken();

                        crearArchivo(n4+" ");
                        do {
                            if (TipoDeDato(n7)) {
                                a.crearNodo(n7);
                                a.unirNodos(anterior, n7);
                                if(auxE2P)
                                recogerDatos("", "Procedimiento", "", "");

                                crearArchivo(n7+" ");
                                anterior = n7;
                                n8 = dameToken();

                                do {
                                    if (Identificador(n8)) {
                                        a.crearNodo(n8);
                                        a.unirNodos(anterior, n8);

                                        anterior = n8;
                                        n9 = dameToken();

                                        if (auxE2P) {
                                            recogerDatos("", "Parametro", "", "");
                                            s.verificaProcedimiento(n8, n7, dameNumeroLinea());
                                        }
                                        crearArchivo(n8 + " ");
                                    do {
                                        if (n9.equals(")")) {
                                            a.crearNodo(n9);
                                            a.unirNodos(anterior, n9);
                                            if(auxE2P)
                                            recogerDatos("", "Procedimiento", "", "");
                                            anterior = n9;
                                            crearArchivo(n9+" ");
                                            n10 = dameToken();
                                        do {
                                            if (n10.equals("var")) {
                                                auxEP=false;
                                                verificaVarProcedimiento(n10);
                                            } else {
                                                recibeErroresSintactico("Error falta var "+dameNumeroLinea());
                                                ct--;
                                                auxE2P=false;
                                                n10="var";
                                            }
                                        }while (auxEP);

                                        } else {
                                            //System.out.print("Error en )");
                                            recibeErroresSintactico("Error falta ) " + dameNumeroLinea());
                                            ct--;
                                            auxE2P=false;
                                            n9=")";
                                        }
                                    }while (auxEP);


                                    } else {
                                        //System.out.print("Error en identificador");
                                        recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                                        ct--;
                                        auxE2P = false;
                                        n8 = "Identificador";
                                    }
                                }while (auxEP);

                            } else {
                                //System.out.print("");
                                recibeErroresSintactico("Error falta tipo de dato " + dameNumeroLinea());
                                ct--;
                                auxE2P=false;
                                n7="entero";
                            }
                        }while (auxEP);

                    } else {
                        //System.out.print("Error "+dameNumeroLinea());
                        recibeErroresSintactico("Error falta ) " + dameNumeroLinea());
                        ct--;
                        auxE2P=false;
                        n4=")";

                    }
                }while (auxEP);
                } else {
                    //System.out.print("Error Identificador");
                    recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                    auxE2P=false;
                    ct--;
                    n3="Identificador";
                }
            }while (auxEP);
        }//sigue

        else if (n2.equals(")")) {
                a.crearNodo(n2);
                a.unirNodos(anterior, n2);
                if(auxE2P)
                recogerDatos("", "Procedimiento", "", "");
                anterior = n2;
                n6 = dameToken();

                crearArchivo(n2+" ");
            do {
                if (n6.equals("var")) {
                    auxEP=false;
                    verificaVarProcedimiento(n6);

                } else {
                    // System.out.print("var");
                    recibeErroresSintactico("Error falta var " + dameNumeroLinea());
                    ct--;
                    auxE2P=false;
                    n6="var";
                }
            }while (auxEP);
            } else {
                recibeErroresSintactico("Error falta ) " + dameNumeroLinea());
            ct--;
            n2=")";
            auxE2P=false;

            }
        }while (auxEP);
    } else {
        //System.out.print("Error (");
        recibeErroresSintactico("Error falta ( " + dameNumeroLinea());
        ct--;
        n1 = "(";
        auxE2P = false;
    }
}while(auxEP);
            } else {
                //System.out.print("Error identificador");
                recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                ct--;
                n = "Identificador";
                auxE2P = false;
            }
        }while(auxEP);

        }
        else{
            //System.out.print("No hay procedimiento");
            recibeErroresSintactico("Error en procedimiento");
        }
        banderaProcedimiento=false;
        s.removerValoresProcedimiento();
    }

    public void valorIdentificadorProcedimiento(String op){
        String nombre,nombre2,nombre3,nombre4,
                nombre5,nombre6,nombre7,nombre8,nombre9,nombre10,
                nombre11,nombre12,nombre13,nombre14,nombre15;

        auxEP=true;
        auxE2P=true;

        if(Identificador(op)){
            a.crearNodo(op);
            a.unirNodos(anterior,op);
            recogerDatos("0","Variable Local","","");
            anterior=op;
            nombre=dameToken();
            crearArchivo(op+" ");


            do {
                if (nombre.equals(":")) {
                    a.crearNodo(nombre);
                    a.unirNodos(anterior, nombre);
                    if (auxE2P)
                        recogerDatos("", "Procedimiento", "", "");

                    crearArchivo(nombre + " ");
                    anterior = nombre;
                    nombre2 = dameToken();

                do {
                    if (TipoDeDato(nombre2)) {
                        a.crearNodo(nombre2);
                        a.unirNodos(anterior, nombre2);

                        anterior = nombre2;
                        nombre3 = dameToken();
                        //System.out.println(dameID());
                        if (auxE2P && auxE2) {
                            recogerDatos("", "Procedimiento", "", "");
                            s.dameKey(op, dameID(), "3");
                            s.verificaProcedimiento(op, nombre2, dameNumeroLinea());
                        }
                        crearArchivo(nombre2 + " ");
                    do {
                        if (nombre3.equals(";")) {
                            a.crearNodo(nombre3);
                            a.unirNodos(anterior, nombre3);
                            if (auxE2P)
                                recogerDatos("", "Procedimiento", "", "");

                            auxEP = false;
                            crearArchivo(nombre3 + "\n");
                            anterior = nombre3;
                            //nombre20=dameToken();
                            //seccion(nombre20);


                        } else if (nombre3.equals(":=")) {
                            a.crearNodo(nombre3);
                            a.unirNodos(anterior, nombre3);
                            recogerDatos("", "Procedimiento", "", "");
                            anterior = nombre3;
                            nombre4 = dameToken();

                            crearArchivo(nombre3+" ");

                            do {
                                if (nombre4.equals("+") || nombre4.equals("-")) {
                                    a.crearNodo(nombre4);
                                    a.unirNodos(anterior, nombre4);
                                    anteriorSigno = nombre4;

                                    if (auxE2P)
                                        recogerDatos("", "Procedimiento", "", "");

                                    crearArchivo(nombre4 + " ");
                                    anterior = nombre4;
                                    nombre5 = dameToken();

                                do {
                                    if (Numero(nombre5) || NumeroFlotante(nombre5)) {
                                        a.crearNodo(nombre5);
                                        a.unirNodos(anterior, nombre5);


                                        agregarValorInicial(op, anteriorSigno + nombre5);


                                        anterior = nombre5;
                                        nombre7 = dameToken();

                                        if (auxE2P) {
                                            recogerDatos("", "Procedimiento", "", "");
                                            s.verificaSiSeDeclaroProcedimiento(op, nombre5, dameNumeroLinea(), anteriorSigno);
                                        }

                                        crearArchivo(nombre5 + " ");
                                    do {
                                        if (nombre7.equals(";")) {
                                            a.crearNodo(nombre7);
                                            a.unirNodos(anterior, nombre7);
                                            if(auxE2P)
                                            recogerDatos("", "Procedimiento", "", "");
                                            crearArchivo(nombre7+"\n");
                                            anterior = nombre7;

                                            auxEP=false;
                                            //nombre16=dameToken();

                                            //seccion(nombre16);


                                        } else {
                                            //System.out.print("en ;");
                                            recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                            nombre7=";";
                                            auxE2P=false;
                                            ct--;
                                        }
                                    }while (auxEP);

                                    } else {
                                        //
                                        //System.out.print("enn numero");
                                        recibeErroresSintactico("Error falta numero " + dameNumeroLinea());
                                        ct--;
                                        auxE2P = false;
                                        nombre5 = "10";
                                    }
                                }while (auxEP);


                                } else if (Cadena(nombre4) || Numero(nombre4) || NumeroFlotante(nombre4)) {
                                    a.crearNodo(nombre4);
                                    a.unirNodos(anterior, nombre4);
                                    agregarValorInicial(op, nombre4);

                                    if (auxE2P)
                                        recogerDatos("", "Procedimiento", "", "");

                                    crearArchivo(nombre4 + " ");
                                    anterior = nombre4;
                                    nombre6 = dameToken();

                                    do {
                                        if (nombre6.equals(";")) {
                                            a.crearNodo(nombre6);
                                            a.unirNodos(anterior, nombre6);

                                            anterior = nombre6;
                                            //nombre17=dameToken();
                                            //seccion(nombre17);
                                            if(auxE2P) {
                                                recogerDatos("", "Procedimiento", "", "");
                                                s.verificaSiSeDeclaroProcedimiento(op, nombre4, dameNumeroLinea(), "");
                                            }
                                            crearArchivo(nombre6+"\n");
                                            auxEP=false;

                                        } else {
                                            //System.out.print("en ;");
                                            recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                            ct--;
                                            auxE2P=false;
                                            nombre6=";";
                                        }
                                    }while (auxEP);


                                } else {
                                    //System.out.print("en signo");
                                    recibeErroresSintactico("Error falta numero " + dameNumeroLinea());
                                    ct--;
                                    auxE2P = false;
                                    nombre4 = "10";
                                }
                            }while (auxEP);

                        } else {
                            //System.out.print("en ;");
                            recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                            ct--;
                            auxE2P = false;
                            nombre3 = ";";
                        }
                    }while (auxEP);
                    } else if (nombre2.equals("arreglo")) {
                        a.crearNodo(nombre2);
                        a.unirNodos(anterior, nombre2);
                        recogerDatos("", "Procedimiento", "", "");
                        anterior = nombre2;
                        nombre8 = dameToken();
                        crearArchivo(nombre2+" ");

                    do {
                        if (nombre8.equals("[")) {
                            a.crearNodo(nombre8);
                            a.unirNodos(anterior, nombre8);

                            if (auxE2P)
                                recogerDatos("", "Procedimiento", "", "");
                            anterior = nombre8;
                            nombre9 = dameToken();
                            crearArchivo(nombre8 + " ");
                            do {
                                if (Numero(nombre9)) {
                                    a.crearNodo(nombre9);
                                    a.unirNodos(anterior, nombre9);
                                    if (auxE2P)
                                        recogerDatos("", "Procedimiento", "", "");

                                    crearArchivo(nombre9);
                                    anterior = nombre9;
                                    nombre10 = dameToken();

                                    do {
                                        if (nombre10.equals("..")) {
                                            a.crearNodo(nombre10);
                                            a.unirNodos(anterior, nombre10);
                                            if (auxE2P)
                                                recogerDatos("", "Procedimiento", "", "");

                                            crearArchivo(nombre10);
                                            anterior = nombre10;
                                            nombre11 = dameToken();

                                            do {
                                                if (Numero(nombre11)) {
                                                    a.crearNodo(nombre11);
                                                    a.unirNodos(anterior, nombre11);
                                                    if (auxE2P)
                                                        recogerDatos("", "Procedimiento", "", "");

                                                    crearArchivo(nombre11 + " ");
                                                    anterior = nombre11;
                                                    nombre12 = dameToken();

                                                    do {


                                                        if (nombre12.equals("]")) {
                                                            a.crearNodo(nombre12);
                                                            a.unirNodos(anterior, nombre12);
                                                            if (auxE2P)
                                                                recogerDatos("", "Procedimiento", "", "");

                                                            crearArchivo(nombre12 + " ");
                                                            anterior = nombre12;
                                                            nombre13 = dameToken();

                                                            do {


                                                                if (nombre13.equals("de")) {
                                                                    a.crearNodo(nombre13);
                                                                    a.unirNodos(anterior, nombre13);
                                                                    if (auxE2P)
                                                                        recogerDatos("", "Procedimiento", "", "");
                                                                    anterior = nombre13;
                                                                    nombre14 = dameToken();

                                                                    crearArchivo(nombre13 + " ");

                                                                    do {
                                                                        if (TipoDeDato(nombre14)) {
                                                                            a.crearNodo(nombre14);
                                                                            a.unirNodos(anterior, nombre14);

                                                                            if (auxE2P)
                                                                                recogerDatos("", "Procedimiento", "", "");

                                                                            crearArchivo(nombre14 + " ");
                                                                            anterior = nombre14;
                                                                            nombre15 = dameToken();

                                                                            do {
                                                                                if (nombre15.equals(";")) {
                                                                                    a.crearNodo(nombre15);
                                                                                    a.unirNodos(anterior, nombre15);

                                                                                    anterior = nombre15;
                                                                                    //nombre18=dameToken();
                                                                                    //seccion(nombre18);
                                                                                    if (auxE2P && auxE2) {
                                                                                        recogerDatos("", "Procedimiento", "", "");
                                                                                        s.dameKey(op, dameID(), "10");
                                                                                        s.verificaProcedimiento(op, nombre14, dameNumeroLinea());
                                                                                        s.valoresMemoriaArreglos(nombre9, nombre11, nombre14, dameNumeroLinea());
                                                                                    }
                                                                                    crearArchivo(nombre15 + "\n");
                                                                                    auxEP = false;


                                                                                } else {
                                                                                    //System.out.print("Error ;");
                                                                                    recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                                                                    ct--;
                                                                                    auxE2P = false;
                                                                                    nombre15 = ";";
                                                                                }
                                                                            } while (auxEP);

                                                                        } else {
                                                                            //System.out.print("en tipo de dato");
                                                                            recibeErroresSintactico("Error falta tipo de dato " + dameNumeroLinea());
                                                                            ct--;
                                                                            auxE2P = false;
                                                                            nombre14 = "entero";
                                                                        }
                                                                    } while (auxEP);
                                                                } else {
                                                                    //System.out.print("Error en de");
                                                                    recibeErroresSintactico("Error falta de " + dameNumeroLinea());
                                                                    ct--;
                                                                    auxE2P = false;
                                                                    nombre13 = "de";
                                                                }
                                                            } while (auxEP);
                                                        } else {
                                                            //System.out.print("Error en ]");
                                                            recibeErroresSintactico("Error falta ] " + dameNumeroLinea());
                                                            ct--;
                                                            auxE2P = false;
                                                            nombre12 = "]";
                                                        }
                                                    } while (auxEP);
                                                } else {
                                                    //System.out.print("Error en numero");
                                                    recibeErroresSintactico("Error falta numero " + dameNumeroLinea());
                                                    ct--;
                                                    auxE2P = false;
                                                    nombre11 = "10";

                                                }
                                            } while (auxEP);
                                        } else {
                                            //System.out.print("enn ..");
                                            recibeErroresSintactico("Error falta .. " + dameNumeroLinea());
                                            ct--;
                                            auxE2P = false;
                                            nombre10 = "..";
                                        }
                                    } while (auxEP);
                                } else {
                                    //ystem.out.print("en numero");
                                    recibeErroresSintactico("Error falta numero " + dameNumeroLinea());
                                    ct--;
                                    auxE2P = false;
                                    nombre9 = "0";
                                }
                            } while (auxEP);

                        } else {
                            //System.out.print("Error en [");
                            recibeErroresSintactico("Error falta [ " + dameNumeroLinea());
                            ct--;
                            auxE2P = false;
                            nombre8 = "[";
                        }
                    }while (auxEP);

                    } else {
                        //System.out.print("en tipo de dato");
                        recibeErroresSintactico("Error falta tipo de dato " + dameNumeroLinea());
                        ct--;
                        auxE2P = false;
                        nombre2 = "entero";

                    }
                }while (auxEP);


                } else {
                    //System.out.print("en :");
                    recibeErroresSintactico("Error falta : " + dameNumeroLinea());
                    ct--;
                    auxE2P = false;
                    nombre = ":";
                }

            }while(auxEP);
        }
        else{
            //System.out.print("en identificador");
            recibeErroresSintactico("Error en identificador "+dameNumeroLinea());
        }
    }

    public void valorIdentificadorFuncion(String op){
        String nombre,nombre2,nombre3,nombre4,
                nombre5,nombre6,nombre7,nombre8,nombre9,nombre10,
                nombre11,nombre12,nombre13,nombre14,nombre15;

        auxEF=true;



        if(Identificador(op)){
            a.crearNodo(op);
            a.unirNodos(anterior,op);
            recogerDatos("0","Variable local","","");
            anterior=op;
            nombre=dameToken();
            crearArchivo(op+" ");

            do {
                if (nombre.equals(":")) {
                    a.crearNodo(nombre);
                    a.unirNodos(anterior, nombre);


                    if (auxE2F)
                        recogerDatos("", "Funcion", "", "");

                    crearArchivo(nombre + " ");
                    anterior = nombre;
                    nombre2 = dameToken();

do {
    if (TipoDeDato(nombre2)) {
        a.crearNodo(nombre2);
        a.unirNodos(anterior, nombre2);

        anterior = nombre2;
        nombre3 = dameToken();

        //System.out.println(dameID());
        if (auxE2F && auxE2) {
            recogerDatos("", "Funcion", "", "");
            s.dameKey(op, dameID(), "3");
            s.verificaFuncion(op, nombre2, dameNumeroLinea());
        }
        crearArchivo(nombre2 + " ");
do {
    if (nombre3.equals(";")) {
        a.crearNodo(nombre3);
        a.unirNodos(anterior, nombre3);

        if (auxE2F)
            recogerDatos("", "Funcion", "", "");

        crearArchivo(nombre3 + "\n");
        anterior = nombre3;
        auxEF = false;
        //nombre20=dameToken();
        //seccion(nombre20);


    }

    else if (nombre3.equals(":=")) {
        a.crearNodo(nombre3);
        a.unirNodos(anterior, nombre3);

        if(auxE2F)
        recogerDatos("", "Funcion", "", "");


        anterior = nombre3;
        nombre4 = dameToken();
        crearArchivo(nombre3+" ");

        do {
            if (nombre4.equals("+") || nombre4.equals("-")) {
                a.crearNodo(nombre4);
                a.unirNodos(anterior, nombre4);
                anteriorSigno = nombre4;

                if (auxE2F)
                    recogerDatos("", "Funcion", "", "");

                crearArchivo(nombre4 + " ");
                anterior = nombre4;
                nombre5 = dameToken();

                do {

                    if (Numero(nombre5) || NumeroFlotante(nombre5)) {
                        a.crearNodo(nombre5);
                        a.unirNodos(anterior, nombre5);
                        agregarValorInicial(op, anteriorSigno + nombre5);
                        anterior = nombre5;
                        nombre7 = dameToken();

                        if(auxE2F) {
                            recogerDatos("", "Funcion", "", "");
                            s.verificaSiSeDeclaroFuncion(op, nombre5, dameNumeroLinea(), anteriorSigno);
                        }

                        crearArchivo(anteriorSigno+nombre5+" ");
                    do {
                        if (nombre7.equals(";")) {
                            a.crearNodo(nombre7);
                            a.unirNodos(anterior, nombre7);

                            if(auxE2F)
                            recogerDatos("", "Funcion", "", "");

                            anterior = nombre7;
                            //nombre16=dameToken();

                            //seccion(nombre16);
                            crearArchivo(nombre7+"\n");
                            auxEF=false;

                        } else {
                            //System.out.print("en ;");
                            recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                            ct--;
                            auxE2F=false;
                            nombre7=";";

                        }
                    }while (auxEF);

                    } else {
                        //
                        //System.out.print("enn numero");
                        recibeErroresSintactico("Error falta numero " + dameNumeroLinea());
                        ct--;
                        auxE2F=false;
                        nombre5="10";
                    }
                }while (auxEF);


            } else if (Cadena(nombre4) || Numero(nombre4) || NumeroFlotante(nombre4)) {
                a.crearNodo(nombre4);
                a.unirNodos(anterior, nombre4);
                agregarValorInicial(op, nombre4);

                if(auxE2F)
                recogerDatos("", "Funcion", "", "");

                anterior = nombre4;
                nombre6 = dameToken();
                crearArchivo(nombre4+" ");

                do {
                    if (nombre6.equals(";")) {
                        a.crearNodo(nombre6);
                        a.unirNodos(anterior, nombre6);



                        anterior = nombre6;
                        //nombre17=dameToken();
                        //seccion(nombre17);
                        if(auxE2F) {
                            recogerDatos("", "Funcion", "", "");
                            s.verificaSiSeDeclaroFuncion(op, nombre4, dameNumeroLinea(), "");
                        }
                        auxEF=false;

                        crearArchivo(nombre6+"\n");
                    } else {
                        //  System.out.print("en ;");
                        recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                        ct--;
                        auxE2F=false;
                        nombre6=";";
                    }
                }while (auxEF);


            } else {
                //System.out.print("en signo");
                recibeErroresSintactico("Error falta numero " + dameNumeroLinea());
                ct--;
                auxE2F=false;
                nombre4="10";

            }
        }while (auxEF);

    } else {
        //System.out.print("en ;");
        recibeErroresSintactico("Error falta ;  " + dameNumeroLinea());
        ct--;
        auxE2F=false;
        nombre3=";";
    }

}while (auxEF);

    } else if (nombre2.equals("arreglo")) {
        a.crearNodo(nombre2);
        a.unirNodos(anterior, nombre2);

        if(auxE2F)
        recogerDatos("", "Funcion", "", "");

        crearArchivo(nombre2+" ");
        anterior = nombre2;
        nombre8 = dameToken();


        do {
            if (nombre8.equals("[")) {
                a.crearNodo(nombre8);
                a.unirNodos(anterior, nombre8);

                if(auxE2F)
                recogerDatos("", "Funcion", "", "");

                crearArchivo(nombre8+" ");
                anterior = nombre8;
                nombre9 = dameToken();



do{
                if (Numero(nombre9)) {
                    a.crearNodo(nombre9);
                    a.unirNodos(anterior, nombre9);


                    if(auxE2F)
                    recogerDatos("", "Funcion", "", "");

                    crearArchivo(nombre9+" ");
                    anterior = nombre9;
                    nombre10 = dameToken();




do{
                    if (nombre10.equals("..")) {
                        a.crearNodo(nombre10);
                        a.unirNodos(anterior, nombre10);

                        if(auxE2F)
                        recogerDatos("", "Funcion", "", "");


                        anterior = nombre10;
                        nombre11 = dameToken();
                        crearArchivo(nombre10+" ");





do{
                        if (Numero(nombre11)) {
                            a.crearNodo(nombre11);
                            a.unirNodos(anterior, nombre11);

                            if(auxE2F)
                            recogerDatos("", "Funcion", "", "");

                            crearArchivo(nombre11+" ");
                            anterior = nombre11;
                            nombre12 = dameToken();


do{
                            if (nombre12.equals("]")) {
                                a.crearNodo(nombre12);
                                a.unirNodos(anterior, nombre12);
                                if(auxE2F)
                                recogerDatos("", "Funcion", "", "");

                                crearArchivo(nombre12+" ");
                                anterior = nombre12;
                                nombre13 = dameToken();
do{
                                if (nombre13.equals("de")) {
                                    a.crearNodo(nombre13);
                                    a.unirNodos(anterior, nombre13);

                                    if(auxE2F)
                                    recogerDatos("", "Funcion", "", "");

                                    crearArchivo(nombre13+" ");
                                    anterior = nombre13;
                                    nombre14 = dameToken();
do{
                                    if (TipoDeDato(nombre14)) {
                                        a.crearNodo(nombre14);
                                        a.unirNodos(anterior, nombre14);
                                        if(auxE2F)
                                        recogerDatos("", "Funcion", "", "");

                                        crearArchivo(nombre14+" ");
                                        anterior = nombre14;
                                        nombre15 = dameToken();
                                    do{
                                        if (nombre15.equals(";")) {
                                            a.crearNodo(nombre15);
                                            a.unirNodos(anterior, nombre15);

                                            anterior = nombre15;
                                        if(auxE2F) {
                                            recogerDatos("", "Funcion", "", "");
                                            s.dameKey(op, dameID(), "10");
                                            s.verificaFuncion(op, nombre14, dameNumeroLinea());
                                            s.valoresMemoriaArreglos(nombre9, nombre11, nombre14, dameNumeroLinea());
                                        }
                                        auxEF=false;
                                        crearArchivo(nombre15+"\n");


                                        } else {
                                            //System.out.print("Error ;");
                                            recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                            ct--;
                                            auxE2F = false;
                                            nombre15 = ";";
                                        }
                                    }while (auxEF);

                                    } else {
                                        //System.out.print("en tipo de dato");
                                        recibeErroresSintactico("Error falta tipo de dato " + dameNumeroLinea());
                                        ct--;
                                        auxE2F = false;
                                        nombre14 = "entero";
                                    }
                                }while (auxEF);
                                } else {
                                    //System.out.print("Error en de");
                                    recibeErroresSintactico("Error falta de " + dameNumeroLinea());
                                    ct--;
                                    auxE2F = false;
                                    nombre13 = "de";
                                }
                            }while (auxEF);
                            } else {
                                //System.out.print("Error en ]");
                                recibeErroresSintactico("Error falta ] " + dameNumeroLinea());
                                ct--;
                                auxE2F = false;
                                nombre12 = "]";
                            }
                        }while (auxEF);
                        } else {
                            //System.out.print("Error en numero");
                            recibeErroresSintactico("Error falta numero " + dameNumeroLinea());
                            ct--;
                            auxE2F = false;
                            nombre11 = "10";
                        }
                    }while (auxEF);
                    } else {
                        //System.out.print("enn ..");
                        recibeErroresSintactico("Error falta .. " + dameNumeroLinea());
                        ct--;
                        auxE2F = false;
                        nombre10 = "..";
                    }
                }while (auxEF);
                } else {
                    //System.out.print("en numero");
                    recibeErroresSintactico("Error falta numero " + dameNumeroLinea());
                    ct--;
                    auxE2F = false;
                    nombre9 = "0";
                }
            }while (auxEF);



            } else {
                //System.out.print("Error en [");
                recibeErroresSintactico("Error falta [ " + dameNumeroLinea());
                ct--;
                auxE2F=false;
                nombre8="[";

            }
        }while (auxEF);

    } else {
        //System.out.print("en tipo de dato");
        recibeErroresSintactico("Error falta tipo de dato " + dameNumeroLinea());
        ct--;
        nombre2="entero";
        auxE2F=false;
    }
}while (auxEF);


                } else {
                    //System.out.print("en :");
                    recibeErroresSintactico("Error falta : " + dameNumeroLinea());
                    ct--;
                    nombre=":";
                    auxE2F=false;
                }
            }while (auxEF);
        }
        else{
            //System.out.print("en identificador");
            recibeErroresSintactico("Error en indetificador "+dameNumeroLinea());
        }

    }

    public void verificaVarProcedimiento(String op){
        String nombre,nombre2;

        auxEP=true;

        if(op.equals("var")){

            a.crearNodo(op);
            a.unirNodos(anterior,op);
            recogerDatos("","Procedimiento","","");
            anterior=op;
            nombre=dameToken();
            crearArchivo(op+"\n");

            do {
                if (Identificador(nombre)) {
                    valorIdentificadorProcedimiento(nombre);

                    nombre2 = dameToken();
                    do {
                    while (Identificador(nombre2)) {
                        auxEP=false;
                        valorIdentificadorProcedimiento(nombre2);
                        nombre2 = dameToken();

                    }
                    if (nombre2.equals("seccion") || nombre2.equals("inicio")) {
                        seccionProcedimiento(nombre2);
                    } else {
                        recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                        ct--;
                        auxE2P=false;
                        nombre2="Identificador";

                    }
                }while (auxEP);

                } else {
                    recibeErroresSintactico("Error falta identificador " + dameNumeroLinea());
                    ct--;
                    auxE2P = false;
                    nombre = "Identificador";
                }
            }while (auxEP);
        }
        else{
         //   System.out.print("en var");
            recibeErroresSintactico("Error en var "+dameNumeroLinea());
        }
    }

    public void seccionProcedimiento(String op){
        String nombre17,nombre18;
        String n2;

        if(op.equals("seccion")){
            a.crearNodo(op);
            a.unirNodos("var",op);
            anterior=op;
            recogerDatos("","Procedimiento","","");
            crearArchivo("seccion \n");
            //implementa


        }
        else if(op.equals("inicio")){
            a.crearNodo(op);
            a.unirNodos(anterior,op);
            recogerDatos("","Procedimiento","","");
            anterior=op;
            nombre17=dameToken();
            crearArchivo(op+"\n");

            if(Identificador(nombre17)||nombre17.equals("si")||nombre17.equals("para")||
                    nombre17.equals("repite")||nombre17.equals("hazlo_si")||nombre17.equals("seleccion")||
                    nombre17.equals("leer")||nombre17.equals("escribir")||nombre17.equals("escribirSL") || Comentario(nombre17)){
                bloque(nombre17);


                nombre18=dameToken();
                while(Identificador(nombre18)||nombre18.equals("si")||nombre18.equals("para")||
                        nombre18.equals("repite")||nombre18.equals("hazlo_si")||nombre18.equals("seleccion")||
                        nombre18.equals("leer")||nombre18.equals("escribir")||nombre18.equals("escribirSL") || Comentario(nombre18)){
                    bloque(nombre18);//bloque
                    nombre18=dameToken();
                }

                if(nombre18.equals("finproc")){
                    a.crearNodo(nombre18);
                    a.unirNodos(anterior,nombre18);
                    recogerDatos("","Procedimiento","","");
                    anterior=nombre18;
                    n2=dameToken();

                    crearArchivo(nombre18+" ");

                    if(n2.equals(";")){
                        a.crearNodo(n2);
                        a.unirNodos(anterior,n2);
                        recogerDatos("","Procedimiento","","");
                        anterior=n2;

                        crearArchivo(";\n");

                    }
                    else{
                       // System.out.print("en ;");
                        recibeErroresSintactico("Error falta ; "+dameNumeroLinea());
                        crearArchivo(";\n");
                    }
                }
                else{
                    recibeErroresSintactico("Error falta finproc "+dameNumeroLinea());
                    crearArchivo("finproc;\n");
                }

            }
            else{
                //System.out.print("Error ");
                recibeErroresSintactico("Error en bloque "+dameNumeroLinea());
                crearArchivo("inicio\nbloque\nfinproc;");
            }
        }
        else{
            //System.out.print("Error");
           // recibeErroresSintactico("Error "+dameNumeroLinea());
        }
    }

    public void seccionFuncion(String op){
        String nombre17,nombre18;
        String n2;
        auxEF=true;


        if(op.equals("seccion")){
            a.crearNodo(op);
            a.unirNodos("var",op);

            if(auxE2F)
            recogerDatos("","Funcion","","");
            anterior=op;
            crearArchivo(op+" \n");
            //implementa


        }
        else if(op.equals("inicio")){
            a.crearNodo(op);
            a.unirNodos(anterior,op);

            if(auxE2F)
            recogerDatos("","Funcion","","");

            anterior=op;
            nombre17=dameToken();

            crearArchivo(op+" \n");




                if (Identificador(nombre17) || nombre17.equals("si") || nombre17.equals("para") ||
                        nombre17.equals("repite") || nombre17.equals("hazlo_si") || nombre17.equals("seleccion") ||
                        nombre17.equals("leer") || nombre17.equals("escribir") || nombre17.equals("escribirSL") || Comentario(nombre17)) {
                    bloque(nombre17);


                    nombre18 = dameToken();
                    while (Identificador(nombre18) || nombre18.equals("si") || nombre18.equals("para") ||
                            nombre18.equals("repite") || nombre18.equals("hazlo_si") || nombre18.equals("seleccion") ||
                            nombre18.equals("leer") || nombre18.equals("escribir") || nombre18.equals("escribirSL") || Comentario(nombre18)) {
                        bloque(nombre18);//bloque
                        nombre18 = dameToken();
                    }
                do {
                        if (nombre18.equals("finfunc")) {
                        a.crearNodo(nombre18);
                        a.unirNodos(anterior, nombre18);

                        if (auxE2F)
                            recogerDatos("", "Funcion", "", "");
                        anterior = nombre18;
                        n2 = dameToken();

                        crearArchivo(nombre18 + " ");

                        do {
                            if (n2.equals(";")) {
                                a.crearNodo(n2);
                                a.unirNodos(anterior, n2);
                                auxEF=false;
                                if(auxE2F)
                                recogerDatos("", "Funcion", "", "");
                                anterior = n2;
                                crearArchivo(n2+"\n");

                            } else {
                                //System.out.print("en ;");
                                recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                ct--;
                                n2=";";
                                auxE2F=false;


                            }
                        }while (auxEF);
                        }

                    else{
                        recibeErroresSintactico("Error falta finfunc "+dameNumeroLinea());

                        nombre18="finfunc";
                        ct--;
                        auxE2F=false;
                    }
                }while (auxEF);

                } else {
                    //System.out.print("Error bloque");
                    recibeErroresSintactico("Error en bloque " + dameNumeroLinea());
                    crearArchivo("bloque\nfinfunc;\ninicio\nbloque\nfin.\n");

                }


        }
        else{
            //
           // recibeErroresSintactico("Error "+dameNumeroLinea());
        }
    }

    public void seccion(String op){


        String nombre17,nombre18,nombre19,n,n1,n2,n3,n4,n5,n20;

        if(op.equals("seccion")){
            a.crearNodo(op);
            a.unirNodos("var",op);
            recogerDatos("","var","","");
            anterior=op;
            //implementa
            nombre19=dameToken();

            crearArchivo(op+"\n");

            comentario(nombre19);
            if(esComentario){
                nombre19=dameToken();
                esComentario=false;
            }


            if(nombre19.equals("funcion")){
                funcion(nombre19);
                n=dameToken();
                while (n.equals("funcion")){
                    funcion(n);
                    n=dameToken();
                }


                while (n.equals("procedimiento")){
                    procedimiento(n);
                    n=dameToken();
                }



                //aqui

                    if (n.equals("inicio")) {


                        a.crearNodo("inicio'");
                        a.unirNodos("Programa", "inicio'");
                        if(auxE2)
                        recogerDatos("", "Principal", "", "");
                        anterior = "inicio'";
                        n2 = dameToken();
                        crearArchivo(n+"\n");


                            if (Identificador(n2) || n2.equals("si") || n2.equals("para") ||
                                    n2.equals("repite") || n2.equals("hazlo_si") || n2.equals("seleccion") ||
                                    n2.equals("leer") || n2.equals("escribir") || n2.equals("escribirSL") || Comentario(n2)) {
                                bloque(n2);


                                n3 = dameToken();

                                while (Identificador(n3) || n3.equals("si") || n3.equals("para") ||
                                        n3.equals("repite") || n3.equals("hazlo_si") || n3.equals("seleccion") ||
                                        n3.equals("leer") || n3.equals("escribir") || n3.equals("escribirSL") || Comentario(n3)) {
                                    bloque(n3);
                                    n3 = dameToken();
                                }

                                if(n3.equals("fin")){
                                    a.crearNodo(n3);
                                    a.unirNodos(anterior, n3);
                                    recogerDatos("", "Principal", "", "");
                                    anterior = n3;

                                    crearArchivo(n3+" ");

                                    n3 = dameToken();

                                    if(n3.equals(".")){
                                        a.crearNodo(n3);
                                        a.unirNodos(anterior, n3);
                                        recogerDatos("", "Principal", "", "");
                                        anterior = n3;
                                        crearArchivo(n3+" ");
                                    }
                                    else{
                                        recibeErroresSintactico("Error falta . "+dameNumeroLinea());
                                        crearArchivo(".");
                                    }

                                }
                                else{
                                    recibeErroresSintactico("Error falta fin "+dameNumeroLinea());
                                    crearArchivo("fin.\n");
                                }


                            } else if (n2.equals("fin")) {
                                a.crearNodo(n2);
                                a.unirNodos(anterior, n2);
                                recogerDatos("", "Principal", "", "");
                                anterior = n2;

                                crearArchivo(n2+" ");

                                n3 = dameToken();
                                if (n3.equals(".")) {
                                    a.crearNodo(n3);
                                    a.unirNodos(anterior, n3);
                                    recogerDatos("", "Principal", "", "");
                                    anterior = n3;
                                    crearArchivo(n3+" ");

                                } else {
                                    //System.out.print("en .");
                                    recibeErroresSintactico("Error en . falta . despues de fin " + dameNumeroLinea());
                                    crearArchivo(".\n");
                                }


                            } else {
                                //System.out.print("Error en seccion");
                                recibeErroresSintactico("Error falta fin " + dameNumeroLinea());
                                crearArchivo("fin.\n");


                            }



                    } else {
                        //System.out.print("en inicio");
                        recibeErroresSintactico("Error falta inicio " + dameNumeroLinea());
                        crearArchivo("inicio\nbloque\nfin.");
                    }



            }
            else if(nombre19.equals("procedimiento")){
                procedimiento(nombre19);
                n1=dameToken();
                while (n1.equals("procedimiento")){
                    procedimiento(n1);
                    n1=dameToken();
                }


                while (n1.equals("funcion")){
                    funcion(n1);
                    n1=dameToken();
                }

                if(n1.equals("inicio")){


                    a.crearNodo("inicio'");
                    a.unirNodos("Programa","inicio'");
                    recogerDatos("","Principal","","");
                    anterior="inicio'";
                    crearArchivo(n1+"\n");

                    n4=dameToken();
                    if(Identificador(n4)||n4.equals("si")||n4.equals("para")||
                            n4.equals("repite")||n4.equals("hazlo_si")||n4.equals("seleccion")||
                            n4.equals("leer")||n4.equals("escribir")||n4.equals("escribirSL") || Comentario(n4)){
                        bloque(n4);


                        n5=dameToken();
                        while(Identificador(n5)||n5.equals("si")||n5.equals("para")||
                                n5.equals("repite")||n5.equals("hazlo_si")||n5.equals("seleccion")||
                                n5.equals("leer")||n5.equals("escribir")||n5.equals("escribirSL") || Comentario(n5)){
                            bloque(n5);
                            n5=dameToken();
                        }
                    }
                    else if(n4.equals("fin")){
                        a.crearNodo(n4);
                        a.unirNodos(anterior,n4);
                        recogerDatos("","Principal","","");
                        anterior=n4;
                        n5=dameToken();
                        crearArchivo(n4+" ");

                        if(n5.equals(".")){
                            a.crearNodo(n5);
                            a.unirNodos(anterior,n5);
                            recogerDatos("","Principal","","");
                            anterior=n5;
                            crearArchivo(".");

                        }
                        else{
                            //System.out.print("en .");
                            recibeErroresSintactico("Error en . falta . despues de fin "+dameNumeroLinea());
                            crearArchivo(".");
                        }

                    }
                    else{
                        //System.out.print("Error en seccion");
                        recibeErroresSintactico("Error falta fin "+dameNumeroLinea());
                        crearArchivo("fin.");
                    }
                }
                else{
                    //System.out.print("en inicio");
                    recibeErroresSintactico("Error falta inicio "+dameNumeroLinea());
                    crearArchivo("inicio\nbloque\nfin.");
                }









            }
            else{
                recibeErroresSintactico("Error falta funcion o procedimiento "+dameNumeroLinea());
                crearArchivo("\n[funcion|procedimiento]\n" +
                        "\ninicio\nbloque\nfin.");
            }


        }
        else if(op.equals("inicio")){
            a.crearNodo("inicio'");
            a.unirNodos("Programa","inicio'");
            recogerDatos("","Principal","","");
            anterior="inicio'";

            crearArchivo(op+"\n");

            nombre17=dameToken();
            if(Identificador(nombre17)||nombre17.equals("si")||nombre17.equals("para")||
                    nombre17.equals("repite")||nombre17.equals("hazlo_si")||nombre17.equals("seleccion")||
                    nombre17.equals("leer")||nombre17.equals("escribir")||nombre17.equals("escribirSL") || Comentario(nombre17)){
                bloque(nombre17);


                nombre18=dameToken();
                while(Identificador(nombre18)||nombre18.equals("si")||nombre18.equals("para")||
                        nombre18.equals("repite")||nombre18.equals("hazlo_si")||nombre18.equals("seleccion")||
                        nombre18.equals("leer")||nombre18.equals("escribir")||nombre18.equals("escribirSL") || Comentario(nombre18)){
                    bloque(nombre18);
                    nombre18=dameToken();
                }


                if(nombre18.equals("fin")){
                    a.crearNodo(nombre18);
                    a.unirNodos(anterior,nombre18);
                    recogerDatos("","Principal","","");
                    anterior=nombre18;
                    n20=dameToken();
                    crearArchivo(nombre18+" ");

                    if(n20.equals(".")){
                        a.crearNodo(n20);
                        a.unirNodos(anterior,n20);
                        recogerDatos("","Principal","","");
                        anterior=n20;
                        crearArchivo(".");

                    }
                    else{
                        //System.out.print("Error en .");
                        recibeErroresSintactico("Error en . falta . despues de fin "+dameNumeroLinea());
                        crearArchivo(".");
                    }

                }
                else{
                    //System.out.print("Error en fin");
                    recibeErroresSintactico("Error falta fin "+dameNumeroLinea());
                    crearArchivo("fin.");
                }


            }
            else if(nombre17.equals("fin")){
                a.crearNodo(nombre17);
                a.unirNodos(anterior,nombre17);
                recogerDatos("","Principal","","");
                anterior=nombre17;
                nombre18=dameToken();
                crearArchivo(nombre17+" ");

                if(nombre18.equals(".")){
                    a.crearNodo(nombre18);
                    a.unirNodos(anterior,nombre18);
                    recogerDatos("","Principal","","");
                    anterior=nombre18;
                    crearArchivo(nombre18+" ");

                }
                else{
                    //System.out.print("en .");
                    recibeErroresSintactico("Error en . falta . despues de fin "+dameNumeroLinea());
                    crearArchivo(".");
                }

            }
            else{
               // System.out.print("Error en seccion");
                recibeErroresSintactico("Error falta fin "+dameNumeroLinea());
                crearArchivo("fin.");
            }
        }
        else{
          //  System.out.print("Error");
            recibeErroresSintactico("Error falta inicio "+dameNumeroLinea());
            crearArchivo("inicio\nbloque\nfin.\n");
        }
    }


    int aux=1;

    boolean auxE;
    boolean auxE2;

   // boolean esSeccion=false;

    public void valorIdentificador(String op){
        String nombre,nombre2,nombre3,nombre4,
                nombre5,nombre6,nombre7,nombre8,nombre9,nombre10,
                nombre11,nombre12,nombre13,nombre14,nombre15;

        auxE=true;
        auxE2=true;


        if(Identificador(op)){

            String numero=Integer.toString(aux);
            aux++;
            a.crearNodo("<Declaracion "+numero+" >");
            a.unirNodos("var","<Declaracion "+numero+" >");

            if(auxE2)
            recogerDatos("0","Variable Global","","");


            a.crearNodo(op);
            a.unirNodos("<Declaracion "+numero+" >",op);
            nombre=dameToken();

            crearArchivo(op+" ");

    do {

        if (nombre.equals(":")) {

            a.crearNodo(nombre);
            a.unirNodos("<Declaracion " + numero + " >", nombre);

            if(auxE2)
            recogerDatos("", "Principal", "", "");



            crearArchivo(nombre+" ");

            nombre2 = dameToken();

        do {
            if (TipoDeDato(nombre2)) {
                a.crearNodo(nombre2);
                a.unirNodos("<Declaracion " + numero + " >", nombre2);

                if(auxE2)
                recogerDatos("", "Principal", "", "");

                //auxE2=true;

                crearArchivo(nombre2+" ");

                nombre3 = dameToken();


                do {
                    if (nombre3.equals(";")) {
                        a.crearNodo(nombre3);
                        a.unirNodos("<Declaracion " + numero + " >", nombre3);

                        if(auxE2) {
                            recogerDatos("", "Principal", "", "");
                            s.dameKey(op, dameID(), "3");
                            s.almacenaVariablesLocales(op, nombre2, dameNumeroLinea());
                            s.agregarValor(op, "0", dameNumeroLinea());
                        }
                        crearArchivo(nombre3+"\n");

                        auxE=false;




                    } else if (nombre3.equals(":=")) {
                        a.crearNodo(nombre3);
                        a.unirNodos("<Declaracion " + numero + " >", nombre3);
                        recogerDatos("", "Principal", "", "");
                        //anterior=nombre3;



                        nombre4 = dameToken();
                        crearArchivo(nombre3+" ");

                    do {
                        if (nombre4.equals("+") || nombre4.equals("-")) {
                            a.crearNodo(nombre4);
                            a.unirNodos("<Declaracion " + numero + " >", nombre4);
                            recogerDatos("", "Principal", "", "");
                            anteriorSigno = nombre4;
                            //anterior=nombre4;
                            nombre5 = dameToken();
                            crearArchivo(nombre4+" ");

                            do {

                                if (Numero(nombre5) || NumeroFlotante(nombre5)) {
                                    a.crearNodo(nombre5);
                                    a.unirNodos("<Declaracion " + numero + " >", nombre5);
                                    if(auxE2)
                                    recogerDatos("", "Principal", "", "");
                                    //  anterior=nombre5;
                                    nombre7 = dameToken();

                                    crearArchivo(nombre5+" ");


                                    do {

                                        if (nombre7.equals(";")) {
                                            a.crearNodo(nombre7);
                                            a.unirNodos("<Declaracion " + numero + " >", nombre7);
                                            //anterior=nombre7;
                                            //nombre16=dameToken();
                                            if(auxE2) {
                                                recogerDatos("", "Principal", "", "");

                                                s.dameKey(op, dameID(), "6");
                                                s.almacenaVariablesLocales(op, nombre2, dameNumeroLinea());

                                                s.verificaSiSeDeclaro(op, nombre5, dameNumeroLinea(), anteriorSigno);
                                                agregarValorInicial(op, anteriorSigno + nombre5);
                                            }
                                            crearArchivo(nombre7+"\n");
                                            auxE=false;


                                        } else {
                                            //System.out.print("en ;");
                                            recibeErroresSintactico("Error en ; " + dameNumeroLinea());
                                            nombre7=";";
                                            auxE2=false;
                                        }
                                    }while (auxE);

                                } else {
                                    //
                                    //System.out.print("enn numero");
                                    recibeErroresSintactico("Error en numero " + dameNumeroLinea());
                                    nombre5="10";
                                    auxE2=false;
                                }
                            }while (auxE);


                        } else if (Cadena(nombre4) || Numero(nombre4) || NumeroFlotante(nombre4)) {
                            a.crearNodo(nombre4);
                            a.unirNodos("<Declaracion " + numero + " >", nombre4);
                            agregarValorInicial(op, nombre4);

                            if(auxE2)
                            recogerDatos("", "Principal", "", "");

                            crearArchivo(nombre4+" ");

                            //anterior=nombre4;
                            nombre6 = dameToken();

                            do {
                                if (nombre6.equals(";")) {
                                    a.crearNodo(nombre6);
                                    a.unirNodos("<Declaracion " + numero + " >", nombre6);

                                    if(auxE2) {
                                        recogerDatos("", "Principal", "", "");
                                        //anterior=nombre6;
                                        //nombre17=dameToken();
                                        //seccion(nombre17);
                                        s.dameKey(op, dameID(), "5");
                                        s.almacenaVariablesLocales(op, nombre2, dameNumeroLinea());
                                        //s.agregarValor(op,anteriorSigno+nombre5);
                                        s.verificaSiSeDeclaro(op, nombre4, dameNumeroLinea(), "");
                                    }
                                    auxE=false;
                                    crearArchivo(nombre6+"\n");

                                } else {
                                    // System.out.print("en ;");
                                    recibeErroresSintactico("Error en ; " + dameNumeroLinea());
                                    nombre6=";";
                                    auxE2=false;
                                    ct--;
                                }
                            }while (auxE);


                        } else {
                            //System.out.print("en signo");
                            recibeErroresSintactico("Error falta numero " + dameNumeroLinea());
                            nombre4="10";
                            auxE2=false;
                            ct--;

                        }
                    }while(auxE);

                    } else {
                        //System.out.print("en ;");
                        recibeErroresSintactico("Error en ; " + dameNumeroLinea());
                        nombre3=";";
                        auxE2=false;
                        ct--;

                    }

                }while(auxE);

            } else if (nombre2.equals("arreglo")) {
                a.crearNodo(nombre2);
                a.unirNodos("<Declaracion " + numero + " >", nombre2);
                //anterior=nombre2;
                nombre8 = dameToken();
                recogerDatos("", "Principal", "", "");
                crearArchivo(nombre2+" ");

                do {

                    if (nombre8.equals("[")) {
                        a.crearNodo(nombre8);
                        a.unirNodos("<Declaracion " + numero + " >", nombre8);
                        //anterior=nombre8;
                        nombre9 = dameToken();

                        if(auxE2)
                        recogerDatos("", "Principal", "", "");

                        crearArchivo(nombre8+" ");

                        do {
                            if (Numero(nombre9)) {
                                a.crearNodo(nombre9);
                                a.unirNodos("<Declaracion " + numero + " >", nombre9);
                                //anterior=nombre9;
                                nombre10 = dameToken();

                                if(auxE2)
                                recogerDatos("", "Principal", "", "");

                                crearArchivo(nombre9+" ");

                                do{

                                if (nombre10.equals("..")) {
                                    a.crearNodo(nombre10);
                                    a.unirNodos("<Declaracion " + numero + " >", nombre10);
                                    //  anterior=nombre10;
                                    if(auxE2)
                                    recogerDatos("", "Principal", "", "");

                                    nombre11 = dameToken();

                                    crearArchivo(nombre10+" ");


                                    do{
                                    if (Numero(nombre11)) {
                                        a.crearNodo(nombre11);
                                        a.unirNodos("<Declaracion " + numero + " >", nombre11);
                                        // anterior=nombre11;
                                        if(auxE2)
                                        recogerDatos("", "Principal", "", "");

                                        nombre12 = dameToken();
                                        crearArchivo(nombre11+" ");

                                        do{
                                        if (nombre12.equals("]")) {
                                            a.crearNodo(nombre12);
                                            a.unirNodos("<Declaracion " + numero + " >", nombre12);
                                            //anterior=nombre12;
                                            if(auxE2)
                                            recogerDatos("", "Principal", "", "");

                                            nombre13 = dameToken();

                                            crearArchivo(nombre12+" ");


                                            do{
                                            if (nombre13.equals("de")) {
                                                a.crearNodo(nombre13);
                                                a.unirNodos("<Declaracion " + numero + " >", nombre13);
                                                //anterior=nombre13;
                                                if(auxE2)
                                                recogerDatos("", "Principal", "", "");

                                                nombre14 = dameToken();
                                               crearArchivo(nombre13+" ");
                                                do{

                                                if (TipoDeDato(nombre14)) {
                                                    a.crearNodo(nombre14);

                                                    if(auxE2)
                                                    recogerDatos("", "Principal", "", "");

                                                    a.unirNodos("<Declaracion " + numero + " >", nombre14);
                                                    //anterior=nombre14;
                                                    nombre15 = dameToken();
                                                    crearArchivo(nombre14+" ");

                                                do{
                                                    if (nombre15.equals(";")) {
                                                        a.crearNodo(nombre15);
                                                        a.unirNodos("<Declaracion " + numero + " >", nombre15);

                                                        if(auxE2) {
                                                            recogerDatos("", "Principal", "", "");
                                                            s.dameKey(op, dameID(), "10");

                                                            s.almacenaVariablesLocales(op, nombre14, dameNumeroLinea());
                                                            s.valoresMemoriaArreglos(nombre9, nombre11, nombre14, dameNumeroLinea());
                                                        }
                                                        crearArchivo(nombre15+"\n");
                                                        auxE=false;


                                                    } else {
                                                        //System.out.print("Error ;");
                                                        recibeErroresSintactico("Error en ; " + dameNumeroLinea());
                                                        ct--;
                                                        nombre15 = ";";
                                                        auxE2 = false;
                                                    }
                                                }while(auxE);
                                                } else {
                                                    //System.out.print("en tipo de dato");
                                                    recibeErroresSintactico("Error falta tipo de dato " + dameNumeroLinea());
                                                    ct--;
                                                    nombre14="entero";
                                                    auxE2=false;

                                                }
                                        }while(auxE);
                                            } else {
                                                //System.out.print("Error en de");
                                                recibeErroresSintactico("Error falta de " + dameNumeroLinea());
                                                ct--;
                                                nombre13="de";
                                                auxE2=false;
                                            }
                                }while(auxE);
                                        } else {
                                            //System.out.print("Error en ]");
                                            recibeErroresSintactico("Error falta ] " + dameNumeroLinea());
                                            ct--;
                                            nombre12="]";
                                            auxE2=false;
                                        }
                        }while(auxE);
                                    } else {
                                        //System.out.print("Error en numero");
                                        recibeErroresSintactico("Error falta numero " + dameNumeroLinea());
                                        ct--;
                                        nombre11="10";
                                        auxE2=false;
                                    }
                }while(auxE);
                                } else {
                                    //System.out.print("enn ..");
                                    recibeErroresSintactico("Error falta .. " + dameNumeroLinea());
                                    ct--;
                                    nombre10="..";
                                    auxE2=false;

                                }
        }while(auxE);
                            } else {
                                //System.out.print("en numero");
                                recibeErroresSintactico("Error falta numero " + dameNumeroLinea());
                                ct--;
                                nombre9="0";
                                auxE2=false;

                            }
                        }while (auxE);

                    } else {
                        //System.out.print("Error en [");
                        recibeErroresSintactico("Error en [ " + dameNumeroLinea());
                        nombre8="[";
                        ct--;
                        auxE2=false;
                    }
                }while(auxE);

            } else {
                //System.out.print("en tipo de dato");
                recibeErroresSintactico("Error en tipo de dato " + dameNumeroLinea());
                nombre2="entero";
                auxE2=false;
                ct--;
            }

        }while (auxE);


        } else {
            //System.out.print("en :");
            recibeErroresSintactico("Error falta : " + dameNumeroLinea());
            nombre=":";
            auxE2=false;
            ct--;


        }

    }while (auxE);

        }
        else{
            //System.out.print("en identificador");
            recibeErroresSintactico("Error en identificador "+dameNumeroLinea());
        }
    }

    public void verificaVar(String op) {

        String nombre, nombre2;
        auxE=true;
        auxE2=true;



            if (op.equals("var")) {


                a.crearNodo(op);
                a.unirNodos("Programa", op);
                recogerDatos("", "Principal", "", "");
                anterior = op;

                nombre = dameToken();
                crearArchivo(op+"\n");

                comentario(nombre);
                if (esComentario) {
                    nombre = dameToken();
                    esComentario = false;
                }
                    do {
                        if (Identificador(nombre)) {
                            //a.crearNodo("<Declaracion>");
                            //a.unirNodos("var","<Declaracion>");


                            valorIdentificador(nombre);

                            nombre2 = dameToken();



                            comentario(nombre2);
                            if (esComentario) {
                                nombre2 = dameToken();
                                esComentario = false;
                            }

                            do {
                                while (Identificador(nombre2)) {



                                    valorIdentificador(nombre2);
                                    nombre2 = dameToken();

                                    comentario(nombre2);
                                    if (esComentario) {
                                        nombre2 = dameToken();
                                        esComentario = false;
                                    }

                                }
                                auxE=false;


                                comentario(nombre2);
                                if (esComentario) {
                                    nombre2 = dameToken();
                                    esComentario = false;
                                }

                                if (nombre2.equals("seccion") || nombre2.equals("inicio")) {
                                    seccion(nombre2);
                                } else {

                                    nombre2="Identificador";
                                    ct--;
                                    auxE2=false;


                                }

                            } while (auxE);

                        } else {
                            recibeErroresSintactico("Error falta Identificador " + dameNumeroLinea());
                            nombre="Identificador";
                            ct--;
                            auxE2=false;


                        }

                    }while (auxE);


                } else{
                    //System.out.print("en var");
                    recibeErroresSintactico("Error falta var " + dameNumeroLinea());

                }

            }



    public void libreria(String op){
        String nombre,nombre2;

        auxE=true;
        auxE2=true;

        if(op.equals("libreria")){
            a.crearNodo("<libreria>");
            a.crearNodo(op);
            a.unirNodos("Programa","<libreria>");
            a.unirNodos("<libreria>",op);
            recogerDatos("","Libreria","","");
            anterior=op;
            nombre=dameToken();
            crearArchivo(op+" ");

            do {
                if (Librerias(nombre)) {
                    a.crearNodo(nombre);
                    a.unirNodos("<libreria>", nombre);

                    anterior = nombre;
                    if(auxE2)
                    limpiarLibreria(nombre);

                    crearArchivo(nombre+" ");

                    nombre2 = dameToken();
                    do {
                        if (nombre2.equals(";")) {
                            a.crearNodo(nombre2);
                            if(auxE2)
                            recogerDatos("", "Libreria", "", "");
                            a.unirNodos("<libreria>", nombre2);
                            lib = true;
                            auxE=false;
                            crearArchivo(nombre2+"\n");
                        } else {
                            //System.out.println("en ;");
                            recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                            ct--;
                            auxE2=false;
                            nombre2=";";
                        }
                    }while (auxE);
                } else {
                    //System.out.println("Error en la libreria");
                    recibeErroresSintactico("Error falta libreria " + dameNumeroLinea());
                    ct--;
                    auxE2 = false;
                    nombre = "<Libreria.p>";
                }
            }while (auxE);
        }

    }

    boolean esComentario=false;

    public void comentario(String op){

        if(Comentario(op)) {
            esComentario = true;
            String aux;
            recogerDatos("", "Principal", "", "");

            if (op.equals("{//")) {
                aux = dameToken();
                recogerDatos("", "Principal", "", "");

                while (!aux.equals("//}")) {
                    aux = dameToken();
                    recogerDatos("", "Principal", "", "");
                }

            }
        }


    }

    public void principal(String op){



        comentario(op);
        if(esComentario){
            op=dameToken();
            esComentario=false;
        }


        libreria(op);

        if(lib){

            op=dameToken();
            lib=false;
        }



        String nombre,nombre2,nombre3,nombre4,nombre5,nombre6;
        boolean error=true,bandera=true;

        comentario(op);
        if(esComentario){
            op=dameToken();
            esComentario=false;
        }


        do {
            if (op.equals("programa")) {

                if(bandera) {

                    a.crearNodo(op);
                    a.unirNodos("Programa", op);


                    recogerDatos("", "principal", "", "");
                }
                    anterior = op;
                    crearArchivo(op + " ");
                    error=false;



                nombre = dameToken();



                if (Identificador(nombre)) {
                    a.crearNodo(nombre);
                    a.unirNodos(anterior, nombre);
                    recogerDatos("", "principal", "", "");
                    crearArchivo(nombre + "\n");


                    if (nombreArchivo == null) {
                        //if(nombreArchivo.equals(null)){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Error");
                        alert.show();
                        alert.setHeaderText(null);
                        alert.setContentText("Error! no se ha guardado el archivo, necesita guardar el archivo antes de ejecutar");

                    }


                    validarElprograma(nombre);
                    anterior = nombre;
                    nombre2 = dameToken();

                    comentario(nombre2);
                    if (esComentario) {
                        nombre2 = dameToken();
                        esComentario = false;
                    }

                    if (nombre2.equals("inicio")) {


                        a.crearNodo("inicio'");
                        a.unirNodos("Programa", "inicio'");
                        recogerDatos("", "principal", "", "");
                        anterior = "inicio'";
                        crearArchivo(nombre2 + "\n");


                        nombre3 = dameToken();

                        if (nombre3.equals("fin")) {
                            a.crearNodo(nombre3);
                            a.unirNodos(anterior, nombre3);
                            recogerDatos("", "principal", "", "");
                            anterior = nombre3;
                            crearArchivo(nombre3);

                            nombre4 = dameToken();
                            if (nombre4.equals(".")) {
                            //    System.out.println("Entra aqui "+nombre4);
                                a.crearNodo(nombre4);
                                a.unirNodos(anterior, nombre4);
                                recogerDatos("", "principal", "", "");
                                anterior = nombre4;
                                crearArchivo(nombre4);

                            } else {
                                // System.out.println("Error en .");
                                //estadoErrores("Error en . linea "+dameNumeroLinea());

                                recibeErroresSintactico("Error en . falta . despues de fin " + dameNumeroLinea());
                                crearArchivo(".");

                            }


                        } else if (Identificador(nombre3) || nombre3.equals("si") || nombre3.equals("para") ||
                                nombre3.equals("repite") || nombre3.equals("hazlo_si") || nombre3.equals("seleccion") ||
                                nombre3.equals("leer") || nombre3.equals("escribir") || nombre3.equals("escribirSL") || Comentario(nombre3)) {


                            bloque(nombre3);


                            nombre5 = dameToken();
                            while (Identificador(nombre5) || nombre5.equals("si") || nombre5.equals("para") ||
                                    nombre5.equals("repite") || nombre5.equals("hazlo_si") || nombre5.equals("seleccion") ||
                                    nombre5.equals("leer") || nombre5.equals("escribir") || nombre5.equals("escribirSL") || Comentario(nombre5)) {
                                bloque(nombre5);
                                nombre5 = dameToken();
                            }

                            if (nombre5.equals("fin")) {
                                a.crearNodo(nombre5);
                                a.unirNodos(anterior, nombre5);
                                anterior = nombre5;
                                recogerDatos("", "principal", "", "");
                                nombre6 = dameToken();
                                crearArchivo(nombre5);

                                if (nombre6.equals(".")) {
                                    a.crearNodo(nombre6);
                                    a.unirNodos(anterior, nombre6);
                                    anterior = nombre6;
                                    recogerDatos("", "principal", "", "");
                                    crearArchivo(nombre6);

                                } else {
                                    //System.out.println("Error en .");
                                    //estadoErrores("Error en . linea "+dameNumeroLinea());
                                    recibeErroresSintactico("Error en . falta . despues de fin " + dameNumeroLinea());
                                    crearArchivo(".");
                                }


                            } else {
                                //System.out.print("Error en fin");
                                //  estadoErrores("Error en fin linea "+dameNumeroLinea());
                                //recibeErroresSintactico("Error en fin linea "+dameNumeroLinea());
                                recibeErroresSintactico("Error en fin " + dameNumeroLinea());
                                crearArchivo("fin.");
                            }

                        } else {
                            //estadoErrores("Error en linea "+dameNumeroLinea());
                            //recibeErroresSintactico();
                            //  recibeErroresSintactico("Error "+dameNumeroLinea());
                            recibeErroresSintactico("Error falta fin " + dameNumeroLinea());
                            crearArchivo("fin. ");
                        }
                    } else if (nombre2.equals("var")) {
                        verificaVar(nombre2);
                    } else {
                        //System.out.println("Error en inicio");
                        //  estadoErrores("Error en linea "+dameNumeroLinea());
                        recibeErroresSintactico("Error " + dameNumeroLinea());
                        crearArchivo("inicio\n fin.");
                        //    error.add("Error en linea "+dameNumeroLinea());
                    }
                } else {
                    //System.out.print("Error en identificador "+dameNumeroLinea());
                    recibeErroresSintactico("Error en identificador " + dameNumeroLinea());
                    crearArchivo(" Identificador " + " \ninicio\nfin.");

///                error.add("Error en identificador linea "+dameNumeroLinea());
                }

            } else {

                recibeErroresSintactico("Error falta programa " + dameNumeroLinea());
                //crearArchivo("programa");
                op="programa";
                ct--;
                bandera=false;



                //System.out.print();
                //error.add("Error en programa  linea "+dameNumeroLinea());
            }

        }while (error);
    }

    //+,-,*,/

    public void operadorMat(String ant){
        String aux;
        int cont=3;

        aux=dameToken();
        do {
            if (Identificador(aux)) {

                a.crearNodo(aux);
                a.unirNodos(anterior, aux);
                anterior = aux;

                if(auxE2) {
                    recogerDatos("", "bloque", "", "");

                    s.almacenaPila(aux);
                    s.verificaOperacionMatematica(aux, dameNumeroLinea());
                }
                crearArchivo(aux+" ");

                aux = dameToken();


                cont++;
                do {
                    while (!aux.equals(";") && OperadoresMatematicos(aux)) {

                        do {
                            if (OperadoresMatematicos(aux)) {
                                a.crearNodo(aux);
                                a.unirNodos(anterior, aux);
                                anterior = aux;

                                if(auxE2) {
                                    recogerDatos("", "bloque", "", "");
                                    s.almacenaPila(aux);
                                }

                                crearArchivo(aux + " ");
                                aux = dameToken();
                                cont++;

                            do {
                                if (Identificador(aux)) {
                                    a.crearNodo(aux);
                                    a.unirNodos(anterior, aux);
                                    anterior = aux;
                                    if(auxE2) {
                                        recogerDatos("", "bloque", "", "");
                                        s.almacenaPila(aux);
                                        s.verificaOperacionMatematica(aux, dameNumeroLinea());
                                    }
                                    crearArchivo(aux + " ");

                                    aux = dameToken();
                                    cont++;
                                    auxE=false;

                                } else {
                                    recibeErroresSintactico("Error falta Identificador " + dameNumeroLinea());
                                    ct--;
                                    aux="Identificador";
                                }
                            }while (auxE);

                            } else {
                                recibeErroresSintactico("Error falta operador " + dameNumeroLinea());
                                ct--;
                                auxE2=false;
                                aux="+";
                            }
                        }while (auxE);

                    }
                    auxE=true;
                    if (aux.equals(";")) {

                        a.crearNodo(aux);
                        a.unirNodos(anterior, aux);
                        anterior = aux;

                        cont++;
                        String posicionId = Integer.toString(cont);

                        crearArchivo(";\n");

                    if(auxE2) {
                        recogerDatos("", "bloque", "", "");
                        s.dameKey(s.identificador, dameID(), posicionId);
                        s.verificaFinal = true;
                        s.operacionMatematica();
                    }
                    auxE=false;

                    } else {
                        recibeErroresSintactico("Error falta ; en " + dameNumeroLinea());
                        auxE2=false;
                        ct--;
                        aux=";";

                    }
                }while (auxE);


            } else {
                recibeErroresSintactico("Error  falta identificador en " + dameNumeroLinea());
                ct--;
                auxE2=false;
                aux="Identificador";
            }
        }while (auxE);


    }

    String anteriorSigno;

    public void asignacion(String op){
        String tipo,tipo2;
        String nombre1,nombre2,nombre3,nombre4;
        String ar,ar1,ar2,ar3,ar4;
        nombre1=op;

        auxE=true;

        //auxE2=true;

        if(Identificador(nombre1)){
        a.crearNodo(nombre1);
            a.unirNodos(anterior,nombre1);


            recogerDatos("","Bloque","","");

            crearArchivo(nombre1+" ");


            anterior=nombre1;
            s.identificador=nombre1;


            if(banderaProcedimiento){
                s.verificarVariableProcedimiento(nombre1,dameNumeroLinea());
            }
            else if(banderaFuncion){


                s.verificarVariableFuncion(nombre1,dameNumeroLinea());
            }
            else if(paraSemantico){

                s.verificarVariablePara(nombre1,dameNumeroLinea());
            }
            else{

                s.verificarVariable(nombre1,dameNumeroLinea());

            }


            nombre2=dameToken();

        do {
            if (nombre2.equals(":=")) {
                a.crearNodo(nombre2);
                a.unirNodos(anterior, nombre2);

                if (auxE2) {
                    recogerDatos("", "Bloque", "", "");
                }

                anterior = nombre2;
                crearArchivo(nombre2 + " ");

                tipo = dameToken();

                do {
                    if (Numero(tipo) || NumeroFlotante(tipo) || Identificador(tipo) || OperadoresMatematicos(tipo) || Cadena(tipo)
                            || tipo.equals("verdad") || tipo.equals("falso")) {

                        a.crearNodo(tipo);
                        a.unirNodos(anterior, tipo);

                        if (banderaProcedimiento) {

                            if (OperadoresMatematicos(tipo)) {
                                //nada
                            } else if (!Identificador(tipo)) {

                                if (auxE2 && auxE2P) {
                                    s.dameKey(nombre1, dameID(), "2");
                                    s.verificaFinal = true;
                                    s.verificaSiSeDeclaroProcedimiento(nombre1, tipo, dameNumeroLinea(), "");
                                }
                            } else {

                                String aux;
                                aux = dameToken();
                                if (aux.equals("(")) {
                                    ct--;

                                } else {
                                    s.almacenaPila(tipo);
                                    //s.verificaOperacionMatematica(tipo, dameNumeroLinea());
                                    s.verificarVariableProcedimiento(tipo, dameNumeroLinea());
                                    ct--;
                                }

                            }
                        } else if (banderaFuncion) {
                            if (OperadoresMatematicos(tipo)) {
                                //nada
                            } else if (!Identificador(tipo)) {


                                if (auxE2F && auxE2) {
                                    s.dameKey(nombre1, dameID(), "2");
                                    s.verificaFinal = true;
                                    s.verificaSiSeDeclaroFuncion(nombre1, tipo, dameNumeroLinea(), "");
                                }

                            } else {

                                String aux;
                                aux = dameToken();
                                if (aux.equals("(")) {
                                    ct--;
                                } else {

                                    s.almacenaPila(tipo);
                                    //s.verificaOperacionMatematica(tipo, dameNumeroLinea());
                                    s.verificarVariableFuncion(tipo, dameNumeroLinea());
                                    ct--;
                                }

                            }
                        } else if (paraSemantico) {
                            if (OperadoresMatematicos(tipo)) {
                                //nada
                            } else if (!Identificador(tipo)) {

                                if (auxE2 && auxEPA2) {
                                    s.dameKey(nombre1, dameID(), "2");
                                    s.verificaFinal = true;
                                    s.verificaSiSeDeclaroPara(nombre1, tipo, dameNumeroLinea(), "");
                                }

                            } else {
                                String aux;
                                aux = dameToken();
                                if (aux.equals("(")) {
                                    ct--;

                                } else {
                                    s.almacenaPila(tipo);
                                    //s.verificaOperacionMatematica(tipo, dameNumeroLinea());
                                    s.verificarVariablePara(tipo, dameNumeroLinea());
                                    ct--;
                                }

                            }
                        } else {

                            if (OperadoresMatematicos(tipo)) {
                                //nada
                            } else if (!Identificador(tipo)) {
                                if (auxE2) {
                                    s.dameKey(nombre1, dameID(), "2");
                                    s.verificaFinal = true;
                                    s.verificaSiSeDeclaro(nombre1, tipo, dameNumeroLinea(), "");
                                }
                            } else {
                                String aux;
                                aux = dameToken();
                                if (aux.equals("(")) {
                                    ct--;
                                } else {
                                    //System.out.println("El id es "+dameID());
                                    s.almacenaPila(tipo);
                                    s.verificaOperacionMatematica(tipo, dameNumeroLinea());
                                    ct--;
                                }
                            }

                        }

                    /*if(tipo.equals("-")) {
                        anteriorSigno=tipo;
                    }
                    else{
                        agregarValorInicial(nombre1, tipo);
                    }*/
                        if (auxE2)
                            recogerDatos("", "Bloque", "", "");


                        anterior = tipo;

                        crearArchivo(tipo + " ");

                        tipo2 = dameToken();

                        if (tipo2.equals("(")) {
                            a.crearNodo(tipo2);
                            a.unirNodos(anterior, tipo2);
                            recogerDatos("", "Bloque", "", "");
                            anterior = tipo2;
                            nombre3 = dameToken();
                            crearArchivo(tipo2 + " ");

                            s.verificaInvocacion(tipo, dameNumeroLinea());
                            verificaDoble(nombre3);

                            if (prosigueIdentificador) {
                                nombre3 = dameToken();
                                prosigueIdentificador = false;
                            }

                        do{
                            if (nombre3.equals(")")) {
                                a.crearNodo(nombre3);
                                if (auxE2)
                                    recogerDatos("", "Bloque", "", "");

                                crearArchivo(nombre3 + " ");
                                a.unirNodos(anterior, nombre3);
                                anterior = nombre3;


                                nombre4 = dameToken();
                            do{

                                if (nombre4.equals(";")) {
                                    a.crearNodo(nombre4);
                                    a.unirNodos(anterior, nombre4);

                                    if(auxE2)
                                    recogerDatos("", "Bloque", "", "");
                                    anterior = nombre4;

                                    auxE=false;

                                    crearArchivo(nombre4+"\n");

                                } else {

                                    recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                    ct--;
                                    auxE2=false;
                                    nombre4=";";
                                }

                            }while (auxE);
                            } else {
                                // System.out.println("erro )");
                                recibeErroresSintactico("Error falta ) " + dameNumeroLinea());
                                ct--;
                                auxE2=false;
                                nombre3=")";
                            }

                        }while(auxE);

                        } else if (Identificador(tipo2) || Numero(tipo2) || NumeroFlotante(tipo2)) {
                            //String signo="";
                            a.crearNodo(tipo2);
                            a.unirNodos(anterior, tipo2);
                            //signo=anteriorSigno+tipo2;
                            //agregarValorInicial(nombre1,signo);
                            s.dameKey(nombre1, dameID(), "3");
                            s.verificaFinal = true;
                            s.verificaSiSeDeclaro(nombre1, tipo2, dameNumeroLinea(), tipo);

                            recogerDatos("", "Bloque", "", "");
                            anterior = tipo2;

                            nombre4 = dameToken();
                            crearArchivo(tipo+tipo2+" ");

                            do {
                                if (nombre4.equals(";")) {
                                    a.crearNodo(nombre4);
                                    a.unirNodos(anterior, nombre4);
                                    if(auxE2)
                                    recogerDatos("", "Bloque", "", "");

                                    crearArchivo(nombre4+"\n");
                                    anterior = nombre4;
                                    auxE=false;

                                } else {
                                    recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                    ct--;
                                    nombre4=";";
                                    auxE2=false;
                                }
                            }while (auxE);

                        } else if (tipo2.equals("+") || tipo2.equals("-") || tipo2.equals("/") || tipo2.equals("**") || tipo2.equals("mod") || tipo2.equals("*")) {

                            a.crearNodo(tipo2);
                            a.unirNodos(anterior, tipo2);
                            recogerDatos("", "Bloque", "", "");
                            anterior = tipo2;
                            s.almacenaPila(tipo2);
                            crearArchivo(tipo2+" ");
                            operadorMat(anterior);


                        } else {
                            do {
                                if (tipo2.equals(";")) {
                                    a.crearNodo(tipo2);
                                    a.unirNodos(anterior, tipo2);

                                    if(auxE2) {
                                        recogerDatos("", "Bloque", "", "");

                                        if (Identificador(tipo)) {
                                            s.operacionMatematica();
                                        }
                                    }
                                    crearArchivo(";\n");
                                    auxE=false;

                                    anterior = tipo2;

                                } else {
                                    //        System.out.println("Error ;");
                                    recibeErroresSintactico("Error falta  ; " + dameNumeroLinea());
                                    ct--;
                                    auxE2=false;
                                    tipo2=";";
                                }
                            }while(auxE);
                        }


                    } else {
                        //  System.out.println("Error tipo de dato");
                        recibeErroresSintactico("Error falta asignacion " + dameNumeroLinea());
                        ct--;
                        auxE2 = false;
                        tipo="10";
                    }
                }while (auxE);
            } else if (nombre2.equals("[")) {
                a.crearNodo(nombre2);
                a.unirNodos(anterior, nombre2);
                recogerDatos("", "Bloque", "", "");
                anterior = nombre2;
                ar = dameToken();
                crearArchivo(nombre2+" ");

                do {
                    if (Numero(ar)) {

                        a.crearNodo(ar);
                        a.unirNodos(anterior, ar);

                        if (auxE2)
                            recogerDatos("", "Bloque", "", "");
                        anterior = ar;
                        ar1 = dameToken();

                        crearArchivo(ar + " ");

                        do {
                            if (ar1.equals("]")) {
                                a.crearNodo(ar1);
                                a.unirNodos(anterior, ar1);
                                if (auxE2)
                                    recogerDatos("", "Bloque", "", "");
                                anterior = ar1;
                                ar2 = dameToken();
                                crearArchivo(ar1 + " ");

                                do {
                                    if (ar2.equals(":=")) {
                                        a.crearNodo(ar2);
                                        a.unirNodos(anterior, ar2);
                                        if (auxE2)
                                            recogerDatos("", "Bloque", "", "");
                                        anterior = ar2;
                                        ar3 = dameToken();
                                        crearArchivo(ar2 + " ");
                                    do{
                                        if (Numero(ar3) || NumeroFlotante(ar3)) {
                                            a.crearNodo(ar3);
                                            a.unirNodos(anterior, ar3);
                                            if(auxE2)
                                            recogerDatos("", "Bloque", "", "");
                                            anterior = ar3;
                                            ar4 = dameToken();
                                            crearArchivo(ar3+" ");
                                        do {
                                            if (ar4.equals(";")) {
                                                a.crearNodo(ar4);
                                                a.unirNodos(anterior, ar4);

                                                anterior = ar4;
                                                //System.out.println(dameID());
                                                if(auxE2) {
                                                    recogerDatos("", "Bloque", "", "");
                                                    s.dameKey(nombre1, dameID(), "6");
                                                    s.verificaFinal = true;
                                                    s.verificaSiSeDeclaro(nombre1, ar3, dameNumeroLinea(), "");
                                                }
                                                crearArchivo(ar4+"\n");
                                                auxE=false;
                                            } else {
                                                recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                                ct--;
                                                auxE2=false;
                                                ar4=";";
                                            }
                                        }while (auxE);

                                        } else if (ar3.equals("-")) {
                                            a.crearNodo(ar3);
                                            a.unirNodos(anterior, ar3);
                                            if(auxE2)
                                            recogerDatos("", "Bloque", "", "");
                                            String signo = ar3;
                                            anterior = ar3;
                                            ar3 = dameToken();
                                            crearArchivo(ar3+" ");

                                        do {
                                            if (Numero(ar3) || NumeroFlotante(ar3)) {
                                                a.crearNodo(ar3);
                                                a.unirNodos(anterior, ar3);
                                                if(auxE2)
                                                recogerDatos("", "Bloque", "", "");
                                                anterior = ar3;
                                                ar4 = dameToken();
                                                crearArchivo(ar3+" ");
                                            do {
                                                if (ar4.equals(";")) {
                                                    a.crearNodo(ar4);
                                                    a.unirNodos(anterior, ar4);

                                                    anterior = ar4;

                                                    // System.out.println(dameID());
                                                    if(auxE2) {
                                                        recogerDatos("", "Bloque", "", "");
                                                        s.dameKey(nombre1, dameID(), "7");
                                                        s.verificaFinal = true;
                                                        s.verificaSiSeDeclaro(nombre1, ar3, dameNumeroLinea(), signo);
                                                    }
                                                    crearArchivo(ar4+"\n");
                                                    auxE=false;
                                                } else {
                                                    recibeErroresSintactico("Error falta ; " + dameNumeroLinea());
                                                    ct--;
                                                    auxE2=false;
                                                    ar4=";";
                                                }
                                            }while (auxE);


                                            } else {
                                                recibeErroresSintactico("Error falta Numero " + dameNumeroLinea());
                                                ct--;
                                                auxE2=false;
                                                ar3="10";
                                            }
                                        }while (auxE);


                                        } else {
                                            recibeErroresSintactico("Error falta numero " + dameNumeroLinea());
                                            ct--;
                                            auxE2 = false;
                                            ar3 = "10";
                                        }
                                    }while (auxE);

                                    } else {
                                        recibeErroresSintactico("Error falta := " + dameNumeroLinea());
                                        ct--;
                                        auxE2=false;
                                        ar2=":=";
                                    }
                                }while (auxE);

                            } else {
                                recibeErroresSintactico("Error falta ] " + dameNumeroLinea());
                                ct--;
                                auxE2=false;
                                ar1="]";
                            }
                        }while (auxE);

                    } else {
                        recibeErroresSintactico("Error falta numero en " + dameNumeroLinea());
                        ct--;
                        auxE2=false;
                        ar="0";
                    }
                }while (auxE);

            } else {
                //System.out.println("Error :=");
                recibeErroresSintactico("Error falta := " + dameNumeroLinea());
                ct--;
                auxE2=false;
                nombre2=":=";
            }

        }while (auxE);
        }else{
            //System.out.println("Error identificador");
            recibeErroresSintactico("Error identificador "+dameNumeroLinea());
        }





    }


    public void agregarValorInicial(String n,String t){
        for(int i=0; i<dataSintactico.size();i++){

            if(n.equals(dataSintactico.get(i).getToken())){
                    dataSintactico.get(i).setValorInicial(t);
                    ts.get(i).setValorInicial(t);
            }
        }
       // recogerDatos("","Valor","","");

    }
    public void limpiarLibreria(String l){
        String e="";
        StringTokenizer token= new StringTokenizer(l,"<|>");
        while(token.hasMoreTokens()){
            e=token.nextToken();

        }
        leerLibreria(e);
        arch=e;

    }
    String arch;
    String ruta="";

    StringBuilder string = new StringBuilder();

    public void leerLibreria(String ar){

        //try{
        try {
            File file = new File(ar);
           // System.out.println(file.getCanonicalPath());


            a.crearNodo("<Ruta>");
            a.crearNodo(file.getCanonicalPath());
            a.unirNodos("<"+ar+">","<Ruta>");
            a.unirNodos("<Ruta>",file.getAbsolutePath());
            recogerDatos("","Libreria","<"+ar+">",file.getAbsolutePath());
            ruta=file.getAbsolutePath();


            FileInputStream ft = new FileInputStream(file);

            DataInputStream in = new DataInputStream(ft);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strline;


            while((strline = br.readLine()) != null){
                //System.out.println(strline);
               // recolectaLineas(strline,ar,ruta);
                string.append(strline+" ");
            }
            br.close();


        } catch (Exception e) {

            recibeErroresSintactico("Error archivo no encontrado "+dameNumeroLinea());
        }
        recolectaLineas(string.toString(),arch,ruta);



        //}catch(Exception){
            //System.err.println("Error: " + e.getMessage());
           // if(file.equals(null))





    }

    public void modificarLiberia(String lib,String r){
        for(int i=0; i<dataSintactico.size();i++){
            if("<"+lib+">"==dataSintactico.get(i).getToken()){
                dataSintactico.get(i).setDirectorio(r);
                dataSintactico.get(i).setNombreArchivo(lib);
                break;

            }

        }

    }

    public void separaLineas(String s,String lib, String ruta){

        //lexico l= new lexico();
        Pattern p=Pattern.compile(l.reservadas+"|"+l.identificador+"|"+l.separador+"|"+l.terminacion
                +"|"+l.numero+"|"+l.numeroFlotante+"|"+l.cadena+"|"+l.tipoDeDato);

        Matcher m=p.matcher(s);


        while (m.find()){
            if(m.group(1)!=null){
                //return m.group(1);

                colocarEnTablaLibreria(m.group(1),lib,ruta);
            }
            if(m.group(2)!=null){
                //return m.group(2);
                colocarEnTablaLibreria(m.group(2),lib,ruta);
            }
            if(m.group(3)!=null){
                //return m.group(2);
                colocarEnTablaLibreria(m.group(3),lib,ruta);
            }
            if(m.group(4)!=null){
                //return m.group(1);

                colocarEnTablaLibreria(m.group(4),lib,ruta);
            }
            if(m.group(5)!=null){
                //return m.group(1);

                colocarEnTablaLibreria(m.group(5),lib,ruta);
            }
            if(m.group(6)!=null){
                //return m.group(1);

                colocarEnTablaLibreria(m.group(6),lib,ruta);
            }
            if(m.group(7)!=null){
                //return m.group(1);

                colocarEnTablaLibreria(m.group(7),lib,ruta);
            }
            if(m.group(8)!=null){
                //return m.group(1);

                colocarEnTablaLibreria(m.group(8),lib,ruta);
            }





        }


    }

    String auxLib=" ";


    public void colocarEnTablaLibreria(String i,String lib,String r){
        GeneracionIntermedia op= new GeneracionIntermedia();
       // System.out.println("Cadena "+auxLib);
        if(PalabrasReservadas(i)){
            //Verifica(s,1);
           // recibeTablaSintactico(dameNumeroLinea(),i,"Palabra Reservada","Libreria","",lib,r,"");


        }

        else if(i.equals(":") || i.equals(":=")){
           // recibeTablaSintactico(dameNumeroLinea(),i,"Asignacion","Libreria","",lib,r,"");

        }
        else if(TipoDeDato(i)){
           // recibeTablaSintactico(dameNumeroLinea(),i,"Tipo de dato","Libreria","",lib,r,"");
            if(!auxLib.equals("arreglo")) {
                s.variablesLocales.put(auxLib,i);
                op.recibeVariablesDeclaradas(auxLib,dameNumeroLinea());


            }

        }
        else if(i.equals(";")){
            //recibeTablaSintactico(dameNumeroLinea(),i,"Punto y coma","Libreria","",lib,r,"");
        }
        else if(Numero(i)){
            //recibeTablaSintactico(dameNumeroLinea(),i,"Numero entero","Libreria","",lib,r,"");
            if(!auxLib.equals("arreglo"))
                s.variablesLocales.put(auxLib,i);
               op.recibeVariablesDeclaradas(auxLib,dameNumeroLinea());


        }
        else if(i.equals("..")){
           // recibeTablaSintactico(dameNumeroLinea(),i,"Separador","Libreria","",lib,r,"");

        }
        else if(i.equals("[")){
           // recibeTablaSintactico(dameNumeroLinea(),i,"Separador","Libreria","",lib,r,"");
        }
        else if(i.equals("]")){
            //recibeTablaSintactico(dameNumeroLinea(),i,"Separador","Libreria","",lib,r,"");
        }
        else if(Identificador(i)){
            if(!i.equals("arreglo")) {
                //recibeTablaSintactico(dameNumeroLinea(), i, "Identificador", "Libreria", "0", lib, r,"");
            }else{
                //recibeTablaSintactico(dameNumeroLinea(), i, "Identificador", "Libreria", "", lib, r,"");
            }
            auxLib=i;

        }

    }

    public void recolectaLineas(String s,String lib,String r){

        modificarLiberia(lib,r);
        separaLineas(s,lib,r);
/*        modificarLiberia(lib,r);
        if(PalabrasReservadas(separaLineas(s))){
           //Verifica(s,1);
           recibeTablaSintactico(dameNumeroLinea(),separaLineas(s),"Palabra Reservada","Libreria","",lib,r);

        }

        else if(separaLineas(s).equals(":") || separaLineas(s).equals(":=")){
            recibeTablaSintactico(dameNumeroLinea(),separaLineas(s),"Asignacion","Libreria","",lib,r);
        }
        else if(TipoDeDato(separaLineas(s))){
            recibeTablaSintactico(dameNumeroLinea(),separaLineas(s),"Tipo de dato","Libreria","",lib,r);
        }
        else if(separaLineas(s).equals(";")){
            recibeTablaSintactico(dameNumeroLinea(),separaLineas(s),"Punto y coma","Libreria","",lib,r);
        }
        else if(Numero(separaLineas(s))){
                recibeTablaSintactico(dameNumeroLinea(),separaLineas(s),"Numero entero","Libreria","",lib,r);

        }
        else if(separaLineas(s).equals("..")){
                recibeTablaSintactico(dameNumeroLinea(),separaLineas(s),"Separador","Libreria","",lib,r);

        }
        else if(separaLineas(s).equals("[")){
            recibeTablaSintactico(dameNumeroLinea(),separaLineas(s),"Separador","Libreria","",lib,r);
        }
        else if(separaLineas(s).equals("]")){
            recibeTablaSintactico(dameNumeroLinea(),separaLineas(s),"Separador","Libreria","",lib,r);
        }
        else if(Identificador(separaLineas(s))){
            recibeTablaSintactico(dameNumeroLinea(),separaLineas(s),"Identificador","Libreria","",lib,r);

        }
        else{
            recibeErroresSintactico("Error var "+dameNumeroLinea());
        }*/
    }

    public void validarElprograma(String cadena){
        String cadenaPas=cadena+".pas";

        if(!nombreArchivo.equals(cadenaPas)){
            recibeErroresSintactico("Error el nombre del archivo no coincide con programa "+dameNumeroLinea());
        }

    }



    public void crearArchivo(String dato){

        try {

            PrintWriter  pr = new PrintWriter(new FileOutputStream("Archivo.txt",true));
            pr.print(dato);

            pr.flush();
            pr.close();


        } catch (FileNotFoundException ex) {
            System.out.println("Error");
           // Logger.getLogger(JavaApplication1.class.getName()).log(Level.SEVERE, null, ex);
        }


    }




    @FXML
    public void inicio(){

        String op=dameToken();
        principal(op);






        //recibeErroresSintactico(error);





    }


/*------ */





}
