module com.example.groupmaker_ma {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.groupmaker_ma to javafx.fxml;
    exports com.example.groupmaker_ma;

    opens groupMaker to javafx.fxml;
    exports groupMaker;
}