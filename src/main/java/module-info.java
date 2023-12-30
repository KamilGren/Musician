module com.musician.musician {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.musician.musician to javafx.fxml;
    exports com.musician.musician;
}