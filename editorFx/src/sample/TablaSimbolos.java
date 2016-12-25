package sample;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Dell on 05/11/2016.
 */
public class TablaSimbolos {

    public final SimpleStringProperty linea;
    public final SimpleStringProperty token;
    public final SimpleStringProperty identificador;
    public final SimpleStringProperty ambito;
    public final SimpleStringProperty valorInicial;
    public final SimpleStringProperty nombreArchivo;
    public final SimpleStringProperty directorio;
    public final SimpleStringProperty valorFinal;
    public final SimpleStringProperty espacioMemoria;
    public final SimpleStringProperty id;


    public TablaSimbolos(String linea1, String token1, String identificador1,
                         String ambito1, String valorInicial1, String nombreArchivo1,
                         String directorio1, String valorFinal1, String espacioMemoria1, String id1) {

        this.linea =  new SimpleStringProperty(linea1);
        this.id =  new SimpleStringProperty(id1);
        this.token = new SimpleStringProperty(token1);
        this.identificador = new SimpleStringProperty(identificador1);
        this.ambito = new SimpleStringProperty(ambito1);
        this.valorInicial = new SimpleStringProperty(valorInicial1);
        this.nombreArchivo = new SimpleStringProperty(nombreArchivo1);
        this.directorio = new SimpleStringProperty(directorio1);
        this.valorFinal = new SimpleStringProperty(valorFinal1);
        this.espacioMemoria = new SimpleStringProperty(espacioMemoria1);
    }


    public String getLinea() {
        return linea.get();
    }

    public SimpleStringProperty lineaProperty() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea.set(linea);
    }

    public String getToken() {
        return token.get();
    }

    public SimpleStringProperty tokenProperty() {
        return token;
    }

    public void setToken(String token) {
        this.token.set(token);
    }

    public String getIdentificador() {
        return identificador.get();
    }

    public SimpleStringProperty identificadorProperty() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador.set(identificador);
    }

    public String getAmbito() {
        return ambito.get();
    }

    public SimpleStringProperty ambitoProperty() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito.set(ambito);
    }

    public String getValorInicial() {
        return valorInicial.get();
    }

    public SimpleStringProperty valorInicialProperty() {
        return valorInicial;
    }

    public void setValorInicial(String valorInicial) {
        this.valorInicial.set(valorInicial);
    }

    public String getNombreArchivo() {
        return nombreArchivo.get();
    }

    public SimpleStringProperty nombreArchivoProperty() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo.set(nombreArchivo);
    }

    public String getDirectorio() {
        return directorio.get();
    }

    public SimpleStringProperty directorioProperty() {
        return directorio;
    }

    public void setDirectorio(String directorio) {
        this.directorio.set(directorio);
    }

    public String getValorFinal() {
        return valorFinal.get();
    }

    public SimpleStringProperty valorFinalProperty() {
        return valorFinal;
    }

    public void setValorFinal(String valorFinal) {
        this.valorFinal.set(valorFinal);
    }

    public String getEspacioMemoria() {
        return espacioMemoria.get();
    }

    public SimpleStringProperty espacioMemoriaProperty() {
        return espacioMemoria;
    }

    public void setEspacioMemoria(String espacioMemoria) {
        this.espacioMemoria.set(espacioMemoria);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }
}
