package com.boha.library.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.VolleyError;
import com.boha.library.R;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.volley.BaseVolley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

/**
 * Utility class to manage server communications via HTTP or WebSocket protocols
 * <p/>
 * Created by aubreyM on 15/a01/31.
 */
public class NetUtil {
    public interface NetUtilListener {
        public void onResponse(ResponseDTO response);

        public void onError(String message);

        public void onWebSocketClose();
    }

    static NetUtilListener listener;

    public static void sendRequest(Context ctx, RequestDTO request, NetUtilListener utilListener) {
        listener = utilListener;

        WebCheckResult wcr = WebCheck.checkNetworkAvailability(ctx, true);
        if (!wcr.isWifiConnected() && !wcr.isMobileConnected()) {
            listener.onError(ctx.getString(R.string.no_network));
            return;
        }
        if (request.getRideWebSocket()) {
            if (request.getRequestList() == null) {
                sendViaWebSocket(ctx, request);
            } else {
                sendCachedRequestsViaWebSocket(ctx, request);
            }
        } else {
            if (request.getRequestList() == null) {
                sendViaHttp(ctx, request);
            } else {
                sendCachedRequestsViaHttp(ctx, request);
            }
        }

    }

    public static void sendDistrictMessageRequest(Context ctx, RequestDTO request, NetUtilListener utilListener) {
        listener = utilListener;

        WebCheckResult wcr = WebCheck.checkNetworkAvailability(ctx, true);
        if (!wcr.isWifiConnected() && !wcr.isMobileConnected()) {
            listener.onError(ctx.getString(R.string.no_network));
            return;
        }
        if (request.getRideWebSocket()) {
            if (request.getRequestList() == null) {
                sendViaWebSocket(ctx, request);
            } else {
                sendCachedRequestsViaWebSocket(ctx, request);
            }
        } else {
            if (request.getRequestList() == null) {
                sendDistrictMessageViaHttp(ctx, request);
            } else {
                sendCachedRequestsViaHttp(ctx, request);
            }
        }

    }

    public static void sendSuburbMessageRequest(Context ctx, RequestDTO request, NetUtilListener utilListener) {
        listener = utilListener;

        WebCheckResult wcr = WebCheck.checkNetworkAvailability(ctx, true);
        if (!wcr.isWifiConnected() && !wcr.isMobileConnected()) {
            listener.onError(ctx.getString(R.string.no_network));
            return;
        }
        if (request.getRideWebSocket()) {
            if (request.getRequestList() == null) {
                sendViaWebSocket(ctx, request);
            } else {
                sendCachedRequestsViaWebSocket(ctx, request);
            }
        } else {
            if (request.getRequestList() == null) {
                sendSuburbMessageViaHttp(ctx, request);
            } else {
                sendCachedRequestsViaHttp(ctx, request);
            }
        }

    }


    private static void sendViaHttp(final Context ctx, RequestDTO request) {
        BaseVolley.getRemoteData(Statics.GATEWAY_SERVLET, request, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
                if (response == null) {
                    listener.onError("Corrupted, null response from server. Please try again.");
                } else {
                    if (response.isMunicipalityAccessFailed()) {
                        FirebaseCrash.report(new Exception("Municipality Service failed or unavailable"));
                    }
                    if (response.getStatusCode() > 0) {
                        listener.onError(response.getMessage());
                        setAnalyticsEvent(ctx,"app", "Unsuccessful");
                        FirebaseCrash.report(new Exception("Application Error, status code: " + response.getMessage()));
                    } else {
                        listener.onResponse(response);
                    }
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                listener.onError("Error communicating with server");
                setAnalyticsEvent(ctx,"network", "Error");
                FirebaseCrash.report(new Exception("Server Network Error: " + error.getMessage()));
            }
        });
    }

    private static void sendSuburbMessageViaHttp(final Context ctx, RequestDTO request) {
        BaseVolley.getRemoteData(Statics.SUBURB_MESSAGE_SERVLET, request, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
                if (response == null) {
                    listener.onError("Corrupted, null response from server. Please try again.");
                } else {
                    if (response.isMunicipalityAccessFailed()) {
                        FirebaseCrash.report(new Exception("Municipality Service failed or unavailable"));
                    }
                    if (response.getStatusCode() > 0) {
                        listener.onError(response.getMessage());
                        setAnalyticsEvent(ctx,"app", "Unsuccessful");
                        FirebaseCrash.report(new Exception("Application Error, status code: " + response.getMessage()));
                    } else {
                        listener.onResponse(response);
                    }
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                listener.onError("Error communicating with server");
                setAnalyticsEvent(ctx,"network", "Error");
                FirebaseCrash.report(new Exception("Server Network Error: " + error.getMessage()));
            }
        });
    }
    private static void sendDistrictMessageViaHttp(final Context ctx, RequestDTO request) {
        BaseVolley.getRemoteData(Statics.DISTRICT_MESSAGE_SERVLET, request, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
                if (response == null) {
                    listener.onError("Corrupted, null response from server. Please try again.");
                } else {
                    if (response.isMunicipalityAccessFailed()) {
                        FirebaseCrash.report(new Exception("Municipality Service failed or unavailable"));
                    }
                    if (response.getStatusCode() > 0) {
                        listener.onError(response.getMessage());
                        setAnalyticsEvent(ctx,"app", "Unsuccessful");
                        FirebaseCrash.report(new Exception("Application Error, status code: " + response.getMessage()));
                    } else {
                        listener.onResponse(response);
                    }
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                listener.onError("Error communicating with server");
                setAnalyticsEvent(ctx,"network", "Error");
                FirebaseCrash.report(new Exception("Server Network Error: " + error.getMessage()));
            }
        });
    }
    static FirebaseAnalytics mFirebaseAnalytics;
    private static void setAnalyticsEvent(Context ctx,String id, String name) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);

        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(ctx);
        }
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.w("NetUtil","network analytics event sent .....");


    }
    private static void sendCachedRequestsViaHttp(final Context ctx, RequestDTO request) {
        BaseVolley.getRemoteData(Statics.CACHED_REQUESTS_SERVLET, request, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
                if (response == null) {
                    listener.onError("Corrupted, null response from server. Please try again.");
                } else {
                    listener.onResponse(response);
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                listener.onError("Error communicating with server");
            }
        });
    }

    private static void sendViaWebSocket(final Context ctx, RequestDTO request) {
        WebSocketUtil.sendRequest(ctx, Statics.GATEWAY_SOCKET, request, new WebSocketUtil.WebSocketListener() {

            @Override
            public void onMessage(ResponseDTO response) {
                if (response == null) {
                    listener.onError("Corrupted, null response from server. Please try again.");
                } else {
                    if (response.getStatusCode() > 0) {
                        listener.onError(response.getMessage());
                    } else {
                        listener.onResponse(response);
                    }
                }
            }

            @Override
            public void onError(String message) {
                listener.onError(message);
            }
        });
    }

    private static void sendCachedRequestsViaWebSocket(final Context ctx, RequestDTO request) {
        WebSocketUtil.sendRequest(ctx, Statics.CACHED_REQUESTS_SOCKET, request, new WebSocketUtil.WebSocketListener() {

            @Override
            public void onMessage(ResponseDTO response) {
                if (response == null) {
                    listener.onError("Corrupted, null response from server. Please try again.");
                } else {
                    listener.onResponse(response);
                }
            }

            @Override
            public void onError(String message) {
                listener.onError(message);
            }
        });
    }

}
