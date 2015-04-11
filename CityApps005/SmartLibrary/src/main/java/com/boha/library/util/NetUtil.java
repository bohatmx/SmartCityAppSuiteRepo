package com.boha.library.util;

import android.content.Context;

import com.android.volley.VolleyError;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.volley.BaseVolley;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

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

        WebCheckResult wcr = WebCheck.checkNetworkAvailability(ctx);
        if (!wcr.isWifiConnected() && !wcr.isMobileConnected()) {
            listener.onError("No network connected. Please connect and try again");
            return;
        }
        if (request.getRideWebSocket()) {
            sendViaWebSocket(ctx,request);
        } else {
            sendViaHttp(ctx, request);
        }
        if (Statics.URL.contains("192.168.1.33")) { //Pecanwood dev
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    timer.cancel();
                    if (!hasResponded) {
                        listener.onError("Development Server not available via Mobile Network.\nRequest TimeOut. Try again.");
                        return;
                    }

                }
            }, 3000);
        }
    }
    private static boolean hasResponded;
    private static void sendViaWebSocket(Context ctx, RequestDTO request) {

        WebSocketUtil.sendRequest(ctx, Statics.GATEWAY_SOCKET, request, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(ResponseDTO response) {
                hasResponded = true;
                listener.onResponse(response);
            }

            @Override
            public void onClose() {
                listener.onWebSocketClose();
            }

            @Override
            public void onError(String message) {
                hasResponded = true;
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
