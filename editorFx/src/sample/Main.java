package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.concurrent.ExecutorService;

public class Main extends Application {

    @FXML
    public CodeArea codeArea;
    public ExecutorService executor;

    public ListView lista;
    public Button bGuardarComo,bGuardar,bAbrir,bEjecutar,bErrores;
    public TableView tablaLexico,tablaSintactico,tablaSimbolo;





    @Override
    public void start(Stage primaryStage) throws Exception{



        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Editor");
        primaryStage.setScene(new Scene(root, 810, 650));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());


    }


    public static void main(String[] args) {

        launch(args);


    }
}
