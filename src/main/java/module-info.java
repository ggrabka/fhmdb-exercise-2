module at.ac.fhcampuswien.fhmdb {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.jfoenix;
    requires com.google.gson;
    requires java.net.http;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires java.sql;
    requires ormlite.core;
    requires ormlite.jdbc;

    opens at.ac.fhcampuswien.fhmdb to javafx.fxml;
    opens at.ac.fhcampuswien.fhmdb.models to com.google.gson;
    exports at.ac.fhcampuswien.fhmdb;
    exports at.ac.fhcampuswien.fhmdb.database; // <--- WICHTIG für ORMLite
    exports at.ac.fhcampuswien.fhmdb.models;
    exports at.ac.fhcampuswien.fhmdb.ui;
    exports at.ac.fhcampuswien.fhmdb.util;
    exports at.ac.fhcampuswien.fhmdb.logic;


    opens at.ac.fhcampuswien.fhmdb.database to ormlite.core; // <--- Fügt Zugriffsrecht für Reflektion hinzu


}