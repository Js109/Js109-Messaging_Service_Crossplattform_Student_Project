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
    private static final String APPLICATION_URL = "http://10.0.2.2:8080";
    public static final String ENDPOINT_SIGNUP = APPLICATION_URL + "/signup/";
    public static final String ENDPOINT_TOPIC = APPLICATION_URL + "/topic";
}
