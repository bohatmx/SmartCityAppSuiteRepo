package com.boha.library.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.boha.library.R;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.volley.BaseVolley;
import com.google.gson.Gson;

/**
 * Utility class to manage server communications via HTTP or WebSocket protocols
 *
 * Created by aubreyM on 15/01/31.
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

        WebCheckResult wcr = WebCheck.checkNetworkAvailability(ctx,true);
        if (!wcr.isWifiConnected() && !wcr.isMobileConnected()) {
            listener.onError(ctx.getString(R.string.no_network));
            return;
        }
        if (request.getRideWebSocket()) {
            sendViaWebSocket(ctx, request);
        } else {
            sendViaHttp(ctx, request);
        }

    }

    private static void sendViaHttp(final Context ctx, RequestDTO request) {
        BaseVolley.getRemoteData(Statics.GATEWAY_SERVLET, request, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(String response) {
                try {
                    ResponseDTO resp = gson.fromJson(response, ResponseDTO.class);
                    if (resp.getStatusCode() == 0) {
                        listener.onResponse(resp);
                    } else {
                        listener.onError(resp.getMessage());
                    }
                } catch (Exception e) {
                    try {
                        String json = ZipUtil.uncompressString(response);
                        ResponseDTO resp = gson.fromJson(json, ResponseDTO.class);
                        if (resp.getStatusCode() == 0) {
                            listener.onResponse(resp);
                        } else {
                            listener.onError(resp.getMessage());
                        }
                    } catch (Exception e1) {
                        Log.e("NetUtil", "Failed", e1);
                        listener.onError("Failed to unpack response");
                    }

                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                listener.onError("Error communicating with server");
            }
        });
    }

    private static void sendViaWebSocket(Context ctx, RequestDTO request) {
        WebSocketUtil.sendRequest(ctx, Statics.GATEWAY_SOCKET, request, new WebSocketUtil.WebSocketListener() {

            @Override
            public void onMessage(ResponseDTO response) {
                listener.onResponse(response);
            }

            @Override
            public void onError(String message) {
                listener.onError(message);
            }
        });
    }

    static Gson gson = new Gson();
}
