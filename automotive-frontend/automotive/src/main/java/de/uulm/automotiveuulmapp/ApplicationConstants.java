package de.uulm.automotiveuulmapp;

import org.jetbrains.annotations.NotNull;

/**
 * This class should contain all globally required constants
 * to improve maintainability
 */
public abstract class ApplicationConstants {
    @NotNull
    public static final String MESSAGE_DB_NAME = "message-db";

    private ApplicationConstants() {
    }
    public static final String REGISTRATION_DB_NAME = "registrationData";
    public static final String LOCATION_DATA_DB_NAME = "locationData";
    public static final String DEVICE_TYPE = "Android Emulator";

    // REST Endpoints
    // environment variable BACKENDURL is set in the run configuration.
    // If you need to edit, do it in the run configuration
    // values at the moment: http://10.0.2.2:8080 (local backend),
    // http://elbitbackend.schuster.domains:8085 (global backend)
    private static final String APPLICATION_URL = System.getenv("BACKENDURL");
    public static final String ENDPOINT_SIGNUP = APPLICATION_URL + "/signup/";
    public static final String ENDPOINT_TOPIC = APPLICATION_URL + "/topic";
}
