module com.musician.musician {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.musician.musician to javafx.fxml;
    exports com.musician.musician;
}