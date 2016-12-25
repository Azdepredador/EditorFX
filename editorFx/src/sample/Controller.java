package sample;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import javafx.scene.control.ScrollPane;
import org.reactfx.value.Val;

import javax.swing.*;
import java.net.URL;

public class Controller extends Main  implements Initializable {

    public static String rutaAbrir,nombreArchivo;
    public static ObservableList<infoLexico> data = FXCollections.observableArrayList();
    public static  ObservableList<infoTablaSintactico> dataSintactico= FXCollections.observableArrayList();

    public static  ObservableList<TablaSimbolos> ts= FXCollections.observableArrayList();
    public static HashMap<String,TablaSimbolos> tabla= new HashMap<>();

    ObservableList<String> items = FXCollections.observableArrayList ();


    public static ArrayList<String> errores= new ArrayList<String>();

    int c=1;
    lexico l= new lexico();
    int fila=0;
    File file=null;
    File ruta=null;
    public static boolean hayErrores=false;






    private static final String[] KEYWORDS = new String[] {
            "programa", "var", "inicio", "fin", "caracter",
            "cadena", "mod", "libreria", "no", "y",
            "o", "verdad", "falso", "seleccion", "si",
            "sino", "evalua", "por_omision", "finsel", "finsi",
            "finhazlo", "hazlo_si", "repite", "finrepite", "como",
            "para", "fin", "finpara", "modo", "finfunc",
            "funcion", "procedimiento", "finproc", "seccion", "hasta",
            "bool", "entero", "largo", "byte", "flotante",
            "doble", "caracter", "cadena", "string", "entonces","de"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                    + "|(?<BRACE>" + BRACE_PATTERN + ")"
                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                    + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                    + "|(?<STRING>" + STRING_PATTERN + ")"
                    + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );





    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = codeArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeArea.setStyleSpans(0, highlighting);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    public void seleccionaError()
    {

        IntFunction<Node> numberFactory = LineNumberFactory.get(codeArea);
        IntFunction<Node> arrowFactory =  new Error(codeArea.currentParagraphProperty());
        IntFunction<Node> graphicFactory = line -> {
            HBox hbox = new HBox(
                    numberFactory.apply(line),
                    arrowFactory.apply(line));
            hbox.setAlignment(Pos.CENTER_LEFT);
            return hbox;
        };
        codeArea.setParagraphGraphicFactory(graphicFactory);

    }

    public boolean esNumero(String j)
    {
        try{
            Integer.parseInt(j);
            return  true;
        }
        catch(NumberFormatException numberFormatException){
            return false;
        }
    }

    public boolean esPuntoyComa(String error){
        StringTokenizer token= new StringTokenizer(error," ");
        String e;
        while(token.hasMoreTokens()){
            e=token.nextToken();
            if(e.equals(";")){
                return  true;
            }
        }
        return false;
    }

    public void seleccionarItem(){
        int numero=0;
        String e;
        String error= lista.getSelectionModel().getSelectedItem().toString();
        StringTokenizer token= new StringTokenizer(error," ");
        while(token.hasMoreTokens()){
            e=token.nextToken();
            if(esNumero(e)){
                numero=Integer.parseInt(e);
                break;
            }
        }





        fila=numero;
        if(esPuntoyComa(error)){




            //codeArea.insertText(fila-1,0,";");
        }

        seleccionaError();
        codeArea.moveTo(fila);
    }

    class Error implements IntFunction<Node> {
        private ObservableValue<Integer> shownLine;

        Error(ObservableValue<Integer> shownLine) {
            this.shownLine = shownLine;
        }

        @Override
        public Node apply(int lineNumber) {

            Polygon triangle = new Polygon(0.0, 0.0, 10.0, 8.0, 0.0, 8.0);
            triangle.setFill(Color.RED);

            ObservableValue<Boolean> visible = Val.map(
                    shownLine  = new ReadOnlyObjectWrapper<Integer>(fila-1),
                    sl -> sl == lineNumber);

            triangle.visibleProperty().bind(
                    Val.flatMap(triangle.sceneProperty(), scene -> {
                        return scene != null ? visible : Val.constant(false);
                    }));

            return triangle;
        }
    }

    public void seleccionar(){
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
    }

    public void abrirArchivo(){

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("(*.pas)", "*.pas");
        fileChooser.getExtensionFilters().add(extFilter);

        if(file!=null){
            File existDirectory = file.getParentFile();
            fileChooser.setInitialDirectory(existDirectory);
        }


        file = fileChooser.showOpenDialog(null);
        rutaAbrir=file.getPath();
        System.out.println("Ruta :"+rutaAbrir);
        nombreArchivo=file.getName();

        System.out.println("Nombre del archivo : "+file.getName());
        if(file!=null){
            codeArea.replaceText(0,0,leerArchivo(file));

        }

    }



    public String leerArchivo(File archivo){
        StringBuilder stringBuffer = new StringBuilder();

        BufferedReader bufferedReader = null;


        stringBuffer.delete(0,stringBuffer.length());
        codeArea.clear();

        try {

            bufferedReader = new BufferedReader(new FileReader(archivo));

            String text;
            while ((text = bufferedReader.readLine()) != null) {
                stringBuffer.append(text+"\n");
            }
            bufferedReader.close();

        } catch (FileNotFoundException ex) {
            //Logger.getLogger(JavaFX_OpenFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(JavaFX_OpenFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                //  Logger.getLogger(JavaFX_OpenFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return stringBuffer.toString();
    }

    public void guardarArchivo(){
        if(ruta!=null){
            try(PrintWriter pw=new PrintWriter(ruta)){
                pw.write(codeArea.getText());
                pw.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("File");
                alert.show();
                alert.setHeaderText(null);
                alert.setContentText("Archivo Guardado!");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        else{
            guardarArchivoComo();
        }
    }

    public void guardarArchivoComo(){
        FileChooser file = new FileChooser();
        file.setTitle("Guardar un archivo");
        FileChooser.ExtensionFilter extFilter= new FileChooser.ExtensionFilter("(*.pas)", "*.pas");
        file.setTitle("inicio");
        file.getExtensionFilters().add(extFilter);



        File archivo = file.showSaveDialog(null);
        ruta=archivo;
        rutaAbrir=ruta.getPath();
        System.out.println("Ruta :"+ruta);
        nombreArchivo=archivo.getName();
        System.out.println("Nombre del archivo : "+archivo.getName());
        if(archivo != null){
            guardar(codeArea.getText(),archivo);
        }
    }

    public void guardar(String content, File file){
        try {
            FileWriter fileWriter;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            Alert cuadro= new Alert(Alert.AlertType.INFORMATION);
            cuadro.setTitle("Error");
            cuadro.setContentText("Error al guardar");
        }

    }





    @FXML
    public void recibeErroresSintactico(String e){
        hayErrores=true;
        errores.add("Sintactico "+e);



    }

    public void vaciaErrorSintactico(){
        for(int i=0; i<errores.size();i++){
            estadoErrores(errores.get(i));
        }

    }


    public void agregarTabla(String id, String pos, String token, String iden){

        data.add(new infoLexico(id,pos,token,iden));
        tablaLexico.setItems(data);



    }

    public class infoLexico{

        public final SimpleStringProperty id;
        public final SimpleStringProperty linea;
        public final SimpleStringProperty token;
        public final SimpleStringProperty identificador;




        public infoLexico(String id1, String linea1, String token1, String identificador1){
            this.id=new SimpleStringProperty(id1);
            this.linea=new SimpleStringProperty(linea1);
            this.token=new SimpleStringProperty(token1);
            this.identificador=new SimpleStringProperty(identificador1);
        }
        public String getId() {
            return id.get();
        }

        public void setId(String id1) {
            id.set(id1);
        }

        public String getLinea() {
            return linea.get();
        }

        public void setLinea(String linea1) {
            linea.set(linea1);
        }

        public String getToken() {
            return token.get();
        }

        public void setToken(String token1) {
            token.set(token1);
        }
        public String getIdentificador() {
            return identificador.get();
        }

        public void setIdentificador(String identificador1) {
            identificador.set(identificador1);
        }
    }





    @FXML
    public void estadoErrores(String lin){

        items.add(lin);

        lista.setItems(items);

    }


    public boolean PalabrasReservadas(String a){
        Pattern p= Pattern.compile(l.reservadas);
        Matcher m=p.matcher(a);
        return m.matches();

    }

    public boolean Cadena(String a){
        Pattern p= Pattern.compile(l.cadena);
        Matcher m=p.matcher(a);
        return m.matches();
    }

    public boolean TipoDeDato(String a){
        Pattern p= Pattern.compile(l.tipoDeDato);
        Matcher m=p.matcher(a);
        return m.matches();
    }

    public boolean OperadoresMatematicos(String a){
        Pattern p= Pattern.compile(l.operadoresMat);
        Matcher m=p.matcher(a);
        return m.matches();
    }

    public boolean Identificador(String a){
        Pattern p= Pattern.compile(l.identificador);
        Matcher m=p.matcher(a);
        return m.matches();
    }
    public boolean Separador(String a){
        Pattern p= Pattern.compile(l.separador);
        Matcher m=p.matcher(a);
        return m.matches();
    }
    public boolean Asignacion(String a){
        Pattern p= Pattern.compile(l.asignacion);
        Matcher m=p.matcher(a);
        return m.matches();
    }
    public boolean NumeroFlotante(String a){
        Pattern p= Pattern.compile(l.numeroFlotante);
        Matcher m=p.matcher(a);
        return m.matches();
    }
    public boolean Numero(String a){
        Pattern p= Pattern.compile(l.numero);
        Matcher m=p.matcher(a);
        return m.matches();
    }

    public boolean Comparador(String a){
        Pattern p= Pattern.compile(l.comparador);
        Matcher m=p.matcher(a);
        return m.matches();
    }
    public boolean Librerias(String a){
        Pattern p= Pattern.compile(l.librerias);
        Matcher m=p.matcher(a);
        return m.matches();
    }
    public boolean LibreriaStandard(String a){
        Pattern p= Pattern.compile("<[\\s|\\n]*standard[\\s|\\n]*.[\\s|\\n]*p[\\s|\\n]*>");
        Matcher m=p.matcher(a);
        return m.matches();
    }
    public boolean IdentificadorLibreria(String a){
        Pattern p= Pattern.compile(l.identificadorLibreria);
        Matcher m=p.matcher(a);
        return m.matches();
    }
    public boolean Comentario(String a){
        Pattern p= Pattern.compile(l.comentario);
        Matcher m=p.matcher(a);
        return m.matches();
    }
    public boolean DobleComilla(String a){
        Pattern p= Pattern.compile(l.dobleComilla);
        Matcher m=p.matcher(a);
        return m.matches();
    }
    public boolean Terminacion(String a){
        Pattern p= Pattern.compile(l.terminacion);
        Matcher m=p.matcher(a);
        return m.matches();
    }


    boolean libreriaStandard=true;
    boolean esComentario=false;


    public void verifica(String a, int l){

        String cont,lin;
        cont=Integer.toString(c);
        lin=Integer.toString(l);


        if(a.equals("//}")){
            esComentario=false;
        }


        if(Identificador(a)){
            c++;
            //System.out.println("Identificador"+"------"+a);
            agregarTabla(cont,lin,a,"Identificador");





        }
        else if(Terminacion(a)){
            c++;
            agregarTabla(cont,lin,a,"Terminacion");

        }
        else if(IdentificadorLibreria(a) && libreriaStandard==true){
            // System.out.println("Identificador libreria"+"------"+a);
            c++;
            agregarTabla(cont,lin,a,"identificador libreria");
            libreriaStandard=false;

        }
        else if(Librerias(a)){
            // System.out.println("Libreria"+"------"+a);
            c++;
			if(LibreriaStandard(a)){
				//System.out.println("hay libreria");
				libreriaStandard=true;

			}
            agregarTabla(cont,lin,a,"Libreria");

        }
        else if(PalabrasReservadas(a)){
            //System.out.println("Palabra Reservada"+"------"+a);
            c++;
            agregarTabla(cont,lin,a,"Palabra reservada");




        }
        else if(TipoDeDato(a)){
            c++;
            //System.out.println("Tipo de dato"+"------"+a);
            agregarTabla(cont,lin,a,"Tipo de dato");

        }
        else if(Cadena(a)){
            c++;
            //System.out.println("Cadena"+"------"+a);
            agregarTabla(cont,lin,a,"Cadena");

        }
        else if(Separador(a)){
            c++;
            //System.out.println("Separador"+"------"+a);
            agregarTabla(cont,lin,a,"Separador");

        }
        else if(Comparador(a)){
            c++;
            //System.out.println("Comparador"+"------"+a);
            agregarTabla(cont,lin,a,"Comparador");

        }
        else if(Asignacion(a)){
            c++;
            //System.out.println("Asignacion"+"------"+a);
            agregarTabla(cont,lin,a,"Asignacion");

        }
        else if(OperadoresMatematicos(a)){
            c++;
            //System.out.println("Operador matematico"+"------"+a);
            agregarTabla(cont,lin,a,"Operador matematico");

        }
        else if(NumeroFlotante(a)){
            c++;
            //System.out.println("Numero Flotante"+"------"+a);
            agregarTabla(cont,lin,a,"Numero Flotante");

        }
        else if(Numero(a)){
            c++;
            //System.out.println("Numero Entero"+"------"+a);
            agregarTabla(cont,lin,a,"Numero Entero");
        }
        else if(Comentario(a)){
            c++;
            //System.out.println("Comentario"+"------"+a);
            agregarTabla(cont,lin,a,"Comentario");


            if(a.equals("{//")) {
                esComentario = true;
            }

        }

        else if(DobleComilla(a)){
            c++;
            //System.out.println("Comentario"+"------"+a);
            agregarTabla(cont,lin,a,"Doble comilla");

        }
        else{

                if(esComentario){

                }else {
                    //  System.out.println("Error verifica linea "+lin);
                    estadoErrores("Error en linea " + lin + " token " + a);
                }

        }


    }



    public void  Verifica(String arg,int li){

        Pattern p=Pattern.compile(l.comentario+"|"+l.numeroFlotante+"|"+l.palabrasJuntas+"|"
                +l.librerias+"|"+l.identificador+"|"+l.reservadas+"|"
                +l.tipoDeDato+"|"+l.cadena+"|"+l.asignacion
                +"|"+l.numero+"|"+l.comparador+"|"+l.terminacion
                +"|"+l.separador+"|"+l.identificadorLibreria+"|"+l.operadoresMat+"|"+l.dobleComilla+"|"+l.errores);
        Matcher m=p.matcher(arg);

        while(m.find()){
            if(m.group(1)!=null){

                //System.out.println("1 "+m.group(1));
                verifica(m.group(1),li);


            }
            if(m.group(2)!=null){
                //System.out.println("2 "+m.group(2));
                verifica(m.group(2),li);

            }
            else if(m.group(3)!=null){

                verifica(m.group(3),li);

            }
            else if(m.group(4)!=null){
                //System.out.println("4 "+m.group(4));
                verifica(m.group(4),li);

            }
            else if(m.group(5)!=null){
                //System.out.println("5 "+m.group(5));
                verifica(m.group(5),li);

            }
            else if(m.group(6)!=null){

                verifica(m.group(6),li);

            }
            else if(m.group(7)!=null){

                verifica(m.group(7),li);

            }
            else if(m.group(8)!=null){

                verifica(m.group(8),li);

            }
            else if(m.group(9)!=null){

                verifica(m.group(9),li);

            }
            else if(m.group(10)!=null){

                verifica(m.group(10),li);

            }
            else if(m.group(11)!=null){

                verifica(m.group(11),li);

            }
            else if(m.group(12)!=null){

                verifica(m.group(12),li);

            }
            else if(m.group(13)!=null){

                verifica(m.group(13),li);

            }
            else if(m.group(14)!=null){

                verifica(m.group(14),li);

            }
            else if(m.group(15)!=null){

                verifica(m.group(15),li);

            }
            else if(m.group(16)!=null){

                verifica(m.group(16),li);

            }








        }


        //pedo

    }

    public void eliminarArchivo(){
        File file= new File("Archivo.txt");
        if(file.exists()){
            file.delete();
        }

    }

    public void proceso(){
        hayErrores=false;
        sintactico s= new sintactico();

        eliminarArchivo();
        lista.getItems().clear();
        tablaLexico.getItems().clear();
        tablaSintactico.getItems().clear();
        tablaSimbolo.getItems().clear();
        errores.clear();
        data.removeAll();
        tabla.clear();
        ts.clear();



        c=1;
        //lexico l= new lexico();
        int i=1;
        int tokenStartIndex=1;





        for(String token : codeArea.getText().split("\n")){
            if(token.equals("")){

            }
            else{
                // System.out.println("token: "+token+", indice: "+tokenStartIndex);

                Verifica(token,tokenStartIndex);


                //  agregarTabla(cont,pos,l.tokenG,l.ident);

                i++;
            }
            tokenStartIndex++;
        }



       // s.dameLista(data);
        s.inicio();

        GeneracionIntermedia op= new GeneracionIntermedia();

        op.optimizador();

        vaciaErrorSintactico();


        verTabla();
        //tablaSemantico();
   //     tabla.get("3inicio").setToken("Japones");
     //   System.out.println("-- "+tabla.get("3inicio").getToken());
        verSimboloTabla();







            //tabla.get("3inicio").setToken("Japones");
            //System.out.println("-- "+tabla.get("3inicio").getToken());

        //codeArea.insertText(0,2,";");




    }




    public void recibeTablaSintactico(String lin, String tok, String rol, String ambito, String vi, String na, String dir, String id){
    if(na.equals("")){

       dataSintactico.add(new infoTablaSintactico(lin, tok, rol, ambito, vi, nombreArchivo, rutaAbrir));


        tabla.put(id+tok, new TablaSimbolos(lin,tok,rol,ambito,vi,nombreArchivo,rutaAbrir,"","",id));
        ts.add(new TablaSimbolos(lin,tok,rol,ambito,vi,nombreArchivo,rutaAbrir,"","",id));


    }else {
        dataSintactico.add(new infoTablaSintactico(lin, tok, rol, ambito, vi, na, dir));

        ts.add(new TablaSimbolos(lin,tok,rol,ambito,vi,na,dir,"","",id));
        tabla.put(id+tok, new TablaSimbolos(lin,tok,rol,ambito,vi,na,dir,"","",id));

    }
    }



    public void verTabla(){
        tablaSintactico.setItems(dataSintactico);
    }

    public void verSimboloTabla(){tablaSimbolo.setItems(ts);}


    public class infoTablaSintactico{

        public final SimpleStringProperty linea;
        public final SimpleStringProperty token;
        public final SimpleStringProperty identificador;
        public final SimpleStringProperty ambito;
        public final SimpleStringProperty valorInicial;
        public final SimpleStringProperty nombreArchivo;
        public final SimpleStringProperty directorio;




        public infoTablaSintactico(String linea1, String token1, String identificador1, String ambito1,
                                   String valorInicial1, String nombreArchivo1, String directorio1){

            this.linea=new SimpleStringProperty(linea1);
            this.token=new SimpleStringProperty(token1);
            this.identificador=new SimpleStringProperty(identificador1);
            this.ambito=new SimpleStringProperty(ambito1);
            this.valorInicial=new SimpleStringProperty(valorInicial1);
            this.nombreArchivo=new SimpleStringProperty(nombreArchivo1);
            this.directorio=new SimpleStringProperty(directorio1);
        }
        public String getLinea() {
            return linea.get();
        }

        public void setLinea(String linea1) {
            linea.set(linea1);
        }

        public String getToken() {
            return token.get();
        }

        public void setToken(String token1) {
            token.set(token1);
        }
        public String getIdentificador() {
            return identificador.get();
        }

        public void setIdentificador(String identificador1) {
            identificador.set(identificador1);
        }

        public String getAmbito() {
            return ambito.get();
        }

        public void setAmbito(String id1) {
            ambito.set(id1);
        }

        public String getValorInicial() {
            return valorInicial.get();
        }

        public void setValorInicial(String id1) {
            valorInicial.set(id1);
        }

        public String getNombreArchivo() {
            return nombreArchivo.get();
        }

        public void setNombreArchivo(String id1) {
            nombreArchivo.set(id1);
        }

        public String getDirectorio() {
            return directorio.get();
        }

        public void setDirectorio(String id1) {directorio.set(id1);
        }
    }




    public void recibeErroresSemanticos(String e){
        errores.add(e);
    }

    //public TextArea textErrores;

    StringBuilder recolecta = new StringBuilder();

    public String leerArchivo(){

        try {


        File archivoE = new File("Archivo.txt");
        FileInputStream ft = new FileInputStream(archivoE);
        DataInputStream in = new DataInputStream(ft);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strline;


            while((strline = br.readLine()) != null){
                recolecta.append("     "+strline+"\n");
            }

            br.close();

            return recolecta.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    @FXML
    public void mostrarErrores() throws IOException{


        if(hayErrores) {
            recolecta.delete(0,recolecta.length());
            JFrame frame = new JFrame("Solucion");
            JTextArea texto = new JTextArea();
            JScrollPane scroll = new JScrollPane();

            texto.setEditable(false);
            texto.setFont(new Font("Calibri",Font.PLAIN, 14));
            texto.setText("\n"+leerArchivo());

            scroll.getViewport().add(texto);
            scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

            frame.setSize(600,500);
            //frame.setLocationRelativeTo(null);
            frame.setLocation(900,100);
            frame.setVisible(true);


            frame.add(scroll);

           // textErrores.setText(";v");

        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alerta");
            alert.show();
            alert.setHeaderText(null);
            alert.setContentText("No hay errores para corregir! ");
        }


    }


    public void declaraTablaSimbolo(){

        TableColumn lin = new TableColumn("Linea");
        lin.setMinWidth(1);
        lin.setMaxWidth(50);
        lin.setCellValueFactory(new PropertyValueFactory<>("Linea"));
        tablaSimbolo.getColumns().addAll(lin);

        TableColumn tok = new TableColumn("Token");
        tok.setMinWidth(100);
        tok.setCellValueFactory(new PropertyValueFactory<>("Token"));
        tablaSimbolo.getColumns().addAll(tok);

        TableColumn rol = new TableColumn("Identificador");
        rol.setMinWidth(120);
        rol.setCellValueFactory(new PropertyValueFactory<>("Identificador"));
        tablaSimbolo.getColumns().addAll(rol);

        TableColumn ambito = new TableColumn("Ambito");
        ambito.setMinWidth(100);
        ambito.setCellValueFactory(new PropertyValueFactory<>("Ambito"));
        tablaSimbolo.getColumns().addAll(ambito);

        TableColumn valorInicial = new TableColumn("V. ini");
        valorInicial.setMinWidth(1);
        valorInicial.setMaxWidth(50);
        valorInicial.setCellValueFactory(new PropertyValueFactory<>("valorInicial"));
        tablaSimbolo.getColumns().addAll(valorInicial);

        TableColumn valorFinal = new TableColumn("V. fin");
        valorFinal.setMinWidth(1);
        valorFinal.setCellValueFactory(new PropertyValueFactory<>("valorFinal"));
        tablaSimbolo.getColumns().addAll(valorFinal);

        TableColumn espacioMemoria = new TableColumn("Mem");
        espacioMemoria.setMinWidth(1);
        espacioMemoria.setCellValueFactory(new PropertyValueFactory<>("espacioMemoria"));
        tablaSimbolo.getColumns().addAll(espacioMemoria);

        TableColumn nombreArchivo = new TableColumn("N. archivo");
        nombreArchivo.setMinWidth(70);
        nombreArchivo.setCellValueFactory(new PropertyValueFactory<>("nombreArchivo"));
        tablaSimbolo.getColumns().addAll(nombreArchivo);

        TableColumn dir = new TableColumn("Dir");
        dir.setMinWidth(120);

        dir.setCellValueFactory(new PropertyValueFactory<>("directorio"));
        tablaSimbolo.getColumns().addAll(dir);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        TableColumn id = new TableColumn("Id");
        id.setMinWidth(160);
        id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tablaLexico.getColumns().addAll(id);

        TableColumn linea = new TableColumn("Linea");
        linea.setMinWidth(160);
        linea.setCellValueFactory(new PropertyValueFactory<>("Linea"));
        tablaLexico.getColumns().addAll(linea);

        TableColumn token = new TableColumn("Token");
        token.setMinWidth(160);
        token.setCellValueFactory(new PropertyValueFactory<>("Token"));
        tablaLexico.getColumns().addAll(token);

        TableColumn identificador = new TableColumn("identificador");
        identificador.setMinWidth(260);
        identificador.setCellValueFactory(new PropertyValueFactory<>("Identificador"));
        tablaLexico.getColumns().addAll(identificador);


        TableColumn lin = new TableColumn("Linea");
        lin.setMinWidth(10);
        lin.setCellValueFactory(new PropertyValueFactory<>("Linea"));
        tablaSintactico.getColumns().addAll(lin);

        TableColumn tok = new TableColumn("Token");
        tok.setMinWidth(10);
        tok.setCellValueFactory(new PropertyValueFactory<>("Token"));
        tablaSintactico.getColumns().addAll(tok);

        TableColumn rol = new TableColumn("Identificador");
        rol.setMinWidth(120);
        rol.setCellValueFactory(new PropertyValueFactory<>("Identificador"));
        tablaSintactico.getColumns().addAll(rol);

        TableColumn ambito = new TableColumn("Ambito");
        ambito.setMinWidth(120);
        ambito.setCellValueFactory(new PropertyValueFactory<>("Ambito"));
        tablaSintactico.getColumns().addAll(ambito);

        TableColumn valorInicial = new TableColumn("V. ini");
        valorInicial.setMinWidth(10);
        valorInicial.setCellValueFactory(new PropertyValueFactory<>("valorInicial"));
        tablaSintactico.getColumns().addAll(valorInicial);

        TableColumn nombreArchivo = new TableColumn("N. archivo");
        nombreArchivo.setMinWidth(120);
        nombreArchivo.setCellValueFactory(new PropertyValueFactory<>("nombreArchivo"));
        tablaSintactico.getColumns().addAll(nombreArchivo);

        TableColumn dir = new TableColumn("Dir");
        dir.setMinWidth(190);
        dir.setCellValueFactory(new PropertyValueFactory<>("directorio"));
        tablaSintactico.getColumns().addAll(dir);


        declaraTablaSimbolo();


        executor = Executors.newSingleThreadExecutor();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        codeArea.position(1, 1);
        codeArea.selectRange(0, 0, 0, 0);
        codeArea.richChanges()
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved())) // XXX
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(codeArea.richChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);

        codeArea.getStylesheets().add(Controller.class.getResource("java-keywords.css").toExternalForm());





    }







}
