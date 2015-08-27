package com.boha.library.util;

import android.content.Context;

import com.android.volley.VolleyError;
import com.boha.library.R;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.volley.BaseVolley;

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


    private static void sendViaHttp(final Context ctx, RequestDTO request) {
        BaseVolley.getRemoteData(Statics.GATEWAY_SERVLET, request, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(ResponseDTO response) {
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
            public void onVolleyError(VolleyError error) {
                listener.onError("Error communicating with server");
            }
        });
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
