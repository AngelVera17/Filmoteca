package dam.alumno.filmoteca;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//Elementos de la vista
public class EditarView {
    public Slider fieldValoracion;
    public TextField fieldAnio;
    public TextArea fieldDescripcion;
    public TextField fieldTitulo;
    public TextField fieldId;
    public Text textoTitulo;
    public TextField fieldPoster;
    private final DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
    private final ObservableList<Pelicula> listaPeliculas = DatosFilmoteca.getListaPeliculas();
    public TextField fieldValoracionText;
    public Button btnAceptar;
    public Text txtError;
    private Pelicula peliculaAModificar = null;

    @FXML
    public void initialize() {

        //Sincroniza el slider con el campo de texto en tiempo real
        fieldValoracion.valueProperty().addListener((observable, oldValue, newValue) -> {
            fieldValoracionText.setText(String.format("%.1f", newValue.floatValue()));
        });

        //Listeners para cada campo
        fieldTitulo.textProperty().addListener((obs, oldValue, newValue) -> validarCampos());
        fieldDescripcion.textProperty().addListener((obs, oldValue, newValue) -> validarCampos());
        fieldPoster.textProperty().addListener((obs, oldValue, newValue) -> validarCampos());
        fieldAnio.textProperty().addListener((obs, oldValue, newValue) -> validarCampos());

        //Valida al iniciar (cuando los campos ya tengan valores)
        validarCampos();
    }

    //Metodo para validar los campos
    private void validarCampos() {
        //Verifica los campos vacíos
        boolean tituloValido = !fieldTitulo.getText().isEmpty();
        boolean descripcionValida = !fieldDescripcion.getText().isEmpty();
        boolean posterValido = !fieldPoster.getText().isEmpty();

        //Verifica si el año es válido
        boolean anioValido = esValido(fieldAnio.getText());

        //Deshabilita el botón si alguna condición falla, y muestra el mensaje de error
        btnAceptar.setDisable(!(tituloValido && descripcionValida && posterValido && anioValido));
        txtError.setVisible(!(tituloValido && descripcionValida && posterValido && anioValido));
    }

    //Metodo para comprobar si el año es valido o no
    public boolean esValido(String valor) {
        try {
            //Intenta convertir el valor a un entero
            int num = Integer.parseInt(valor);
            int anioActual = java.time.Year.now().getValue(); //Obtiene el año actual
            return num >= 1888 && num <= anioActual; //Valida entre 1888 y el año actual (primera pelicula creada)
        } catch (NumberFormatException e) {
            //Si no, devuelve false
            return false;
        }
    }

    //Funcionalidad al pulsar el boton aceptar
    public void handlerAceptar(ActionEvent actionEvent) {
        if (peliculaAModificar == null) {
            //Establece los valores para los campos
            Pelicula pelicula = new Pelicula();
            pelicula.setId(Integer.parseInt(fieldId.getText()));
            pelicula.setTitle(fieldTitulo.getText());
            pelicula.setDescription(fieldDescripcion.getText());
            pelicula.setYear(Integer.parseInt(fieldAnio.getText()));
            pelicula.setRating(Math.round(fieldValoracion.getValue() * 10) / 10.0f); //Solo obtengo 1 digito
            pelicula.setPoster(fieldPoster.getText());

            //Agrega la pelicula
            listaPeliculas.add(pelicula);
        } else {
            //Modifica la película existente
            peliculaAModificar.setTitle(fieldTitulo.getText());
            peliculaAModificar.setDescription(fieldDescripcion.getText());
            peliculaAModificar.setYear(Integer.parseInt(fieldAnio.getText()));
            peliculaAModificar.setRating(Math.round(fieldValoracion.getValue() * 10) / 10.0f); //Solo obtengo 1 digito
            peliculaAModificar.setPoster(fieldPoster.getText());
        }

        //Cierra la ventana
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    //Funcionalidad al pulsar el boton cancelar
    public void handlerCancelar(ActionEvent actionEvent) {

        //Cierra la ventana
        Stage stage = (Stage) ((Node) (actionEvent.getSource())).getScene().getWindow();
        stage.close();

    }

    //Metodo para cargar los valores en el caso de pulsar el boton modificar
    public void setPelicula(Pelicula pelicula) {
        this.peliculaAModificar = pelicula;
        if (pelicula != null) {
            fieldId.setText(String.valueOf(pelicula.getId()));
            fieldTitulo.setText(pelicula.getTitle());
            fieldDescripcion.setText(pelicula.getDescription());
            fieldAnio.setText(String.valueOf(pelicula.getYear()));
            fieldValoracion.setValue(pelicula.getRating());
            fieldPoster.setText(pelicula.getPoster());

            fieldId.setDisable(true); //No permite modificar el ID
            textoTitulo.setText("Modificar Película"); //Cambia el título de la ventana
        }
    }

    //Metodo para establecer el ID
    public void setId() {

        int nuevoId = datosFilmoteca.getSiguienteId();
        fieldId.setText(String.valueOf(nuevoId));
        fieldId.setDisable(true); //No permite modificar el ID
    }
}
