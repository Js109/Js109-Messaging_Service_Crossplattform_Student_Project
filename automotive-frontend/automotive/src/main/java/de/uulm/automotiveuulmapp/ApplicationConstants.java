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
    // BuildConfig variable BACKEND_URL is set in build.gradle and depends on the used Build Variant
    // values at the moment: http://10.0.2.2:8080 (local backend),
    // http://elbitbackend.schuster.domains:8085 (global backend)
    private static String APPLICATION_URL = BuildConfig.BACKEND_URL; // System.getenv("BACKENDURL");
    public static final String ENDPOINT_SIGNUP = APPLICATION_URL + "/signup/";
    public static final String ENDPOINT_TOPIC = APPLICATION_URL + "/topic";
}
