package com.boha.foureyes.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.boha.foureyes.dto.RequestDTO;
import com.boha.foureyes.dto.ResponseDTO;
import com.boha.foureyes.volley.BaseVolley;
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
    private static void sendViaWebSocket(Context ctx, RequestDTO request) {


        WebSocketUtil.sendRequest(ctx, SharedUtil.getServerWebsocketUrl(ctx), request, new WebSocketUtil.SocketListener() {
            @Override
            public void onOpen() {
                Log.i("NetUtil","+++ Hooray! WebSocket is OPEN for business");
            }

            @Override
            public void onMessage(ResponseDTO response) {
                if (response.getStatusCode() > 0) {
                    onError(response.getMessage());
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
    private static void sendViaHttp(Context ctx, RequestDTO request) {
        BaseVolley.sendRequest(SharedUtil.getServerHTTPUrl(ctx), request, ctx, new BaseVolley.BohaVolleyListener() {
            @Override
            public void onResponseReceived(String response) {
                ResponseDTO resp = gson.fromJson(response, ResponseDTO.class);
                Log.i("NetUtil","Status code: " + resp.getStatusCode());
                if (resp.getStatusCode() == 0) {
                    listener.onResponse(resp);
                } else {
                    listener.onError(resp.getMessage());
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                listener.onError("Error communicating with server");
            }
        });
    }

    static Gson gson = new Gson();
}
