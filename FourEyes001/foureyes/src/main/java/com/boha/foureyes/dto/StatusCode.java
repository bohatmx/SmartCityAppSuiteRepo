package com.boha.foureyes.dto;

/**
 * Created by aubreyM on 15/04/26.
 */
public class StatusCode {
    public static final int OK = 0,
            ERROR_DATABASE = 99,
            ERROR_SERVER = 88,
            ERROR_SERVICES = 77,
            ERROR_JSON_SYNTAX = 79,
            NEWS_ARTICLE_ADDED = 105,
            ALERT_ADDED = 100,
            COMPLAINT_ADDED = 101,
            USER_REGISTERED = 102,
            CITIZEN_REGISTERED = 103,
            STAFF_REGISTERED = 104,
            MUNICIPALITY_REGISTERED = 106,
            ERROR_DATA_COMPRESSION = 110;

    public static final String OK_MESSAGE = "OK",
            ERROR_DATABASE_MESSAGE = "Database related error",
            ERROR_SERVER_MESSAGE = "Unexpected Server Error",
            ERROR_SERVICES_MESSAGE = "Error communicating with external service";
}
