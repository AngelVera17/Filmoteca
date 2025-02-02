package dam.alumno.filmoteca;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //Archivos css
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dam/alumno/filmoteca/Main.css")).toExternalForm());

        stage.setTitle("Peliculas");
        stage.setResizable(false);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void init() {
        System.out.println("Cargando datos desde fichero datos/peliculas.json");
        DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Pelicula> lista = objectMapper.readValue(new File("datos/peliculas.json"),
                                    objectMapper.getTypeFactory().constructCollectionType(List.class, Pelicula.class));

            DatosFilmoteca.getListaPeliculas().setAll(lista);
        } catch (IOException e){
            System.out.println("ERROR al cargar los datos. La aplicación no puede iniciarse");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println(DatosFilmoteca.getListaPeliculas());
    }

    public void stop() {
        DatosFilmoteca.getInstancia();
        ObservableList<Pelicula> listaPeliculas = DatosFilmoteca.getListaPeliculas();
        System.out.println(listaPeliculas);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writeValue(new File("datos/peliculas.json"),listaPeliculas);
        }catch (IOException e) {
            System.out.println("ERROR no se ha podido guardar los datos de la aplicación");
            e.printStackTrace();
        }
    }
}



