package de.uulm.automotiveuulmapp;

/**
 * This class should contain all globally required constants
 * to improve maintainability
 */
public abstract class ApplicationConstants {
    private ApplicationConstants() {
    }
    public static final String REGISTRATION_DB_NAME = "registrationData";
    public static final String LOCATION_DATA_DB_NAME = "locationData";
    public static final String DEVICE_TYPE = "Android Emulator";

    // REST Endpoints
    private static final String APPLICATION_URL = "http://elbitbackend.schuster.domains:8085";
    public static final String ENDPOINT_SIGNUP = APPLICATION_URL + "/signup/";
    public static final String ENDPOINT_TOPIC = APPLICATION_URL + "/topic";
}
