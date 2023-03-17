module com.devpinda.autoclicker.minecraftautoclickerjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jnativehook;
    requires java.logging;


    opens com.devpinda.autoclicker.minecraftautoclickerjava to javafx.fxml;
    exports com.devpinda.autoclicker.minecraftautoclickerjava;
}