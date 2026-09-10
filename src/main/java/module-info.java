module ni.edu.uam.fact_app {

    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    exports ni.edu.uam.fact_app;
    exports ni.edu.uam.fact_app.application;
    exports ni.edu.uam.fact_app.model;

    opens ni.edu.uam.fact_app.controller to javafx.fxml;
    opens ni.edu.uam.fact_app.model to javafx.base;
}