package com.boha.library.util;

import android.content.Context;

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
            sendViaAutobahn(ctx,request);
        } else {
            sendViaHttp(ctx, request);
        }

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

    private static void sendViaAutobahn(Context ctx, RequestDTO request) {
        WebSocketUtil.sendRequest(ctx, Statics.GATEWAY_SOCKET, request, new WebSocketUtil.SocketListener() {

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
