package com.boha.library.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.volley.BaseVolley;
import com.google.gson.Gson;

/**
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

        if (request.getRideWebSocket()) {
            sendViaWebSocket(ctx, request);
        } else {
            sendViaHttp(ctx, request);
        }
    }

    private static void sendViaWebSocket(Context ctx, RequestDTO request) {

        WebSocketUtil.sendRequest(ctx, Statics.GATEWAY_SOCKET, request, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(ResponseDTO response) {
                Log.w("NetUtil", "WebSocket responded, message: " + response.getMessage());
                listener.onResponse(response);
            }

            @Override
            public void onClose() {
                Log.w("NetUtil", "WebSocket onClose");
                listener.onWebSocketClose();
            }

            @Override
            public void onError(String message) {
                Log.e("NetUtil", message);
                listener.onError(message);
            }
        });
    }

    private static void sendViaHttp(Context ctx, RequestDTO request) {
        BaseVolley.getRemoteData(Statics.GATEWAY_SERVLET, request, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(String response) {
                ResponseDTO resp = gson.fromJson(response, ResponseDTO.class);
                listener.onResponse(resp);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                listener.onError("Error communicating with server");
            }
        });
    }

    static Gson gson = new Gson();
}
