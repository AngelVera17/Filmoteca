package dam.alumno.filmoteca;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class MainController {


    //Declaro los elementos de la vista a los que quiero acceder
    private final DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();

    public Text textoTitulo;
    public TextArea textoDescripcion;
    public Text textoAnio;
    public Text textoValoracion;
    public ImageView imagen;

    @FXML
    private TableColumn<Pelicula, Integer> columnaAnio;

    @FXML
    private TableColumn<Pelicula, String> columnaDescripcion;

    @FXML
    private TableColumn<Pelicula, Integer> columnaId;

    @FXML
    private TableColumn<Pelicula, String> columnaTitulo;

    @FXML
    private TableColumn<Pelicula, Float> columnaValoracion;

    @FXML
    private TableView<Pelicula> tablaPeliculas;

    public void initialize() {

        //Cargo los valores y establezco cada columna con sus respectivos datos
        ObservableList<Pelicula> listaPeliculas = DatosFilmoteca.getListaPeliculas();
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("title"));
        columnaAnio.setCellValueFactory(new PropertyValueFactory<Pelicula, Integer>("year"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("description"));
        columnaId.setCellValueFactory(new PropertyValueFactory<Pelicula, Integer>("id"));
        columnaValoracion.setCellValueFactory(new PropertyValueFactory<Pelicula, Float>("rating"));

        tablaPeliculas.setItems(listaPeliculas);

        //Cargo los valores de la tabla y se los paso a los textos
        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                textoTitulo.setText(newValue.getTitle());
                textoDescripcion.setText(newValue.getDescription());
                textoValoracion.setText(String.valueOf(newValue.getRating()));
                textoAnio.setText(String.valueOf(newValue.getYear()));
                try {
                    Image img = new Image(newValue.getPoster());
                    imagen.setImage(img);
                } catch (Exception e) {
                    imagen.setImage(null); //Sí hay un error, limpio la imagen
                    System.out.println("Error al cargar la imagen: " + e.getMessage());
                }
            }
        });
    }

    //Funcionalidad al pulsar el boton borrar
    public void handlerBorrar(ActionEvent actionEvent) {

        //Carga el indice
        int indice = tablaPeliculas.getSelectionModel().getSelectedIndex();
        if (indice >= 0) {

            //Muestra la alerta de confirmacion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(null);
            alert.setHeaderText("¿Desea borrar esta pelicula?");
            alert.setContentText("Este cambio es irreversible");

            Optional<ButtonType> result = alert.showAndWait();

            //Borra la pelicula
            if (result.isPresent() && result.get() == ButtonType.OK) {
                tablaPeliculas.getItems().remove(indice);
            }
            //Si no hay una pelicula seleccionada, muestra una alerta de error
        } else {
            Alert error = new Alert(Alert.AlertType.WARNING);
            error.setTitle("Error");
            error.setHeaderText(null);
            error.setContentText("Debe seleccionar una pelicula");
            error.showAndWait();
        }
    }

    //Funcionalidad al pulsar el boton cerrar
    public void handlerCerrar(ActionEvent actionEvent) {

        //Muestra la alerta de confirmacion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setHeaderText(null);
        alert.setContentText("¿Desea cerrar la aplicacion?");

        Optional<ButtonType> result = alert.showAndWait();

        //Si se pulsa el boton de confirmacion, cierra la aplicacion
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) (actionEvent.getSource())).getScene().getWindow();
            stage.close();
        }
    }

    //Funcionalidad para el boton añadir
    public void handlerAniadir(ActionEvent actionEvent) {

        //Crea el objeto mediante la vista EditarView
        Scene escena = null;
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/dam/alumno/filmoteca/EditarView.fxml"));

        //Carga la escena
        try {
            escena = new Scene(fxmlLoader.load());
            EditarView controlador = fxmlLoader.getController();
            controlador.setId();
        } catch (IOException ex) {
            System.out.println("Error al cargar el FXML");
            ex.printStackTrace();
        }
        Stage stage = new Stage();

        //Archivo css
        assert escena != null;
        escena.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dam/alumno/filmoteca/Editar.css")).toExternalForm());

        //Titulo de la ventana
        stage.setTitle("Nueva pelicula");
        stage.setScene(escena);

        //Muestra la escena y espera
        stage.showAndWait();
    }

    //Funcionalidad para el boton modificar
    public void handlerModificar(ActionEvent actionEvent) {

        //Comprueba el índice
        int indice = tablaPeliculas.getSelectionModel().getSelectedIndex();
        if (indice >= 0) {
            //Crea el objeto mediante la vista EditarView
            Scene escena = null;
            FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("/dam/alumno/filmoteca/EditarView.fxml"));

            //Carga la escena
            try {
                escena = new Scene(fxmlLoader.load());
                EditarView controlador = fxmlLoader.getController();
                controlador.setPelicula(tablaPeliculas.getSelectionModel().getSelectedItem());
            } catch (IOException ex) {
                System.out.println("Error al cargar el FXML");
                ex.printStackTrace();
            }
            Stage stage = new Stage();

            //Archivo css
            assert escena != null;
            escena.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/dam/alumno/filmoteca/Editar.css")).toExternalForm());

            //Titulo de la ventana
            stage.setTitle("Editar pelicula");
            stage.setScene(escena);

            //Muestra la escena y espera
            stage.showAndWait();

            //Si no hay pelicula seleccionada, muestra la alerta de error
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Debe seleccionar una pelicula");
            alert.showAndWait();
        }
    }
}
