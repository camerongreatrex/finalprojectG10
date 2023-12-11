module greatrex.cameron {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens greatrex.cameron to javafx.fxml;

    exports greatrex.cameron;
}
