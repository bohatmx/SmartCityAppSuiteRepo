package com.boha.foureyes.util;

/**
 * Created by aubreyM on 15/07/15.
 */
public class StatusCode {
    public static final int
            OK = 0,
            ERROR_GCM = 33,
            ERROR_DATABASE = 99,
            ERROR_SERVER = 88,
            ERROR_SERVICES = 77,
            ERROR_JSON_SYNTAX = 79,
            ERROR_WEBSOCKET = 81,

            NEWS_ARTICLE_ADDED = -105,
            ALERT_ADDED = -100,
            COMPLAINT_ADDED = -101,
            USER_REGISTERED = -102,
            CITIZEN_REGISTERED = -103,
            STAFF_REGISTERED = -104,
            MUNICIPALITY_REGISTERED = 106,
            ERROR_DATA_COMPRESSION = 110;


}
