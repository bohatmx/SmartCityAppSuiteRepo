package com.boha.foureyes.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.boha.foureyes.dto.RequestDTO;
import com.boha.foureyes.dto.ResponseDTO;
import com.boha.foureyes.volley.BaseVolley;
import com.google.gson.Gson;

import java.util.Timer;

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
    static Timer timer;
    public static void sendRequest(Context ctx, RequestDTO request, NetUtilListener utilListener) {
        listener = utilListener;

        WebCheckResult wcr = WebCheck.checkNetworkAvailability(ctx,true);
        if (!wcr.isWifiConnected() && !wcr.isMobileConnected()) {
            listener.onError("No network available");
            return;
        }
        if (request.isRideWebSocket()) {
            sendViaWebSocket(ctx,request);
        } else {
            sendViaHttp(ctx, request);
        }

    }
    private static boolean hasResponded;
    private static void sendViaWebSocket(Context ctx, RequestDTO request) {

        WebSocketUtil.sendRequest(ctx, Statics.GATEWAY_SOCKET, request, new WebSocketUtil.SocketListener() {
            @Override
            public void onOpen() {
                Log.i("NetUtil","+++ Hooray! WebSocket is OPEN for business");
            }

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
    private static void sendViaHttp(Context ctx, RequestDTO request) {
        BaseVolley.getRemoteData(Statics.GATEWAY_SERVLET, request, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(String response) {
                ResponseDTO resp = gson.fromJson(response, ResponseDTO.class);
                hasResponded = true;
                listener.onResponse(resp);
            }

            @Override
            public void onVolleyError(VolleyError error) {
                hasResponded = true;
                listener.onError("Error communicating with server");
            }
        });
    }

    static Gson gson = new Gson();
}
