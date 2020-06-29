package de.uulm.automotiveuulmapp;

public abstract class ApplicationConstants {
    private ApplicationConstants() {
    }
    public static final String REGISTRATION_DB_NAME = "registrationData";
    public static final String DEVICE_TYPE = "Android Emulator";

    // REST Endpoints
    private static final String APPLICATION_URL = "http://10.0.2.2:8080";
    public static final String ENDPOINT_SIGNUP = APPLICATION_URL + "/signup/";
    public static final String ENDPOINT_TOPIC = APPLICATION_URL + "/topic";
}
