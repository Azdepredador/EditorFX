package sample;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dell on 25/09/2016.
 */
public class lexico extends Main {


    String terminacion="([.]|[;])";
    String palabrasJuntas="([}]|[{]|\\w+|programa\\w+|var\\w+|inicio\\w+|caracter\\w+|cadena\\w+|mod\\w+|libreria\\w+|no\\w+|y\\w+" +
            "|o\\w+|verdad\\w+|"
            +"falso\\w+|seleccion\\w+|sino\\w+|evalua\\w+|por_omision\\w+|finsel\\w+|" +
            "finsi\\w+|finhazlo\\w+|hazlo_si\\w+|repite\\w+|finrepite\\w+|como\\w+|para\\w+|finpara\\w+|modo\\w+|"
            +"finfunc\\w+|funcion\\w+|procedimiento\\w+|finproc\\w+|seccion\\w+|hasta\\w+|fin\\w+|si\\w+" +
            "|bool\\w+|entero\\w+|largo\\w+|byte\\w+|flotante\\w+|doble\\w+|caracter\\w+|cadena\\w+|string\\w+" +
            "|mod\\w+|\\w+mod|entonces\\w+)";
    String dobleComilla="(\")";
    String errores="([$]+|[@]+|[#]+|[%]+|[&]+|[|]+|[!]+|[~]+|[´]+|[¨]+|[:]+|[`]+|[_]+|[-]+|[?]+|[¡]+|[¿]+|[.]+)";
    String reservadas="(por_omision|programa|var|inicio|caracter|cadena|mod|libreria|no|y|o|verdad|"
            +"falso|seleccion|sino|evalua|finsel|finsi|finhazlo|hazlo_si|repite|finrepite|como|para|finpara|modo|"
            +"finfunc|funcion|procedimiento|finproc|seccion|hasta|fin|si|entonces|de)";

    String identificador="([A-Z][A-Za-z0-9]{2,255}|arreglo)";

    String tipoDeDato="(bool|entero|largo|byte|flotante|doble|caracter|cadena|string)";

    String cadena="([\"].*[\"])";

    String operadoresMat="([-][-]|[+][+]|[*][*]|[+]|[-]|[/]|[*]|mod)";

    String separador="([,]|[(]|[)]|[\\[]|[\\]])";

    String numero="([0-9]+)";
    String numeroFlotante="([0-9]+[.][0-9]+|"
            + "[0-9]+[.]E-[0-9]+|"
            + "[0-9]+[.]E[+][0-9]+|"
            + "[0-9]+[.]e-[0-9]+|"
            + "[0-9]+[.]e[+][0-9]+|"
            + "[0-9]+E-[0-9]+|"
            + "[0-9]+E[+][0-9]+|"
            + "[0-9]+e-[0-9]+|"
            + "[0-9]+e[+][0-9]+)";

    String comparador="(<=|>=|==|<>|<|>)";
    String asignacion="([:][=]|[.][.]|[:])";
    String librerias="(<[\\s|\\n]*standard[\\s|\\n]*.[\\s|\\n]*p[\\s|\\n]*>|<[\\s|\\n]*[A-Z][\\s|\\n]*[A-Za-z0-9]{2,255}[\\s|\\n]*.[\\s|\\n]*p[\\s|\\n]*>)";
    String identificadorLibreria="(leer|escribirSL|escribir)";
    String comentario="([/][/][}]|[{].*?[}]|[{][/][/]|[{][/][/][\\n]*.*?[\\n]*[\\n]*.*?[\\n]*.*?[\\n]*.*?[\\n]*.*?[\\n]*.*?[\\n]*.*?[\\n]*.*?[\\n]*.*?[/][/][}]"
            + "[\\n]*.*?[\\n]*.*?[\\n]*.*?[\\n]*.*?[\\n]*.*?[\\n]*.*?)";
    String comentarioPalabra="([a-z]*)";
   //String comentario="([{][/][/]|[/][/][}]|[{]|[}])";


}
