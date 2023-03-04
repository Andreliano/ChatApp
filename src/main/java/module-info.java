module com.example.laborator6 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.laborator6 to javafx.fxml;
    opens com.example.laborator6.domain to javafx.base;
    opens com.example.laborator6.utils.events to javafx.base;
    exports com.example.laborator6;
}