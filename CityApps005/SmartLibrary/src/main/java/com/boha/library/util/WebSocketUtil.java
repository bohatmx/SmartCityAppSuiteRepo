package com.boha.library.util;

import android.content.Context;
import android.util.Log;

import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.google.gson.Gson;

import java.nio.ByteBuffer;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

/**
 * Created by aubreyM on 15/04/19.
 */
public class WebSocketUtil {

    static final String LOG = WebSocketUtil.class.getSimpleName();
    static SocketListener socketListener;
    static final WebSocketConnection mConnection = new WebSocketConnection();
    static final Gson GSON = new Gson();
    static boolean shouldReconnect;
    public interface SocketListener {
        void onOpen();
        void onMessage(ResponseDTO response);
        void onError(String message);
    }

    public static void sendRequest(Context ctx, final String suffix,RequestDTO w, SocketListener listener) {
        socketListener = listener;
        final String url = Statics.WEBSOCKET_URL + suffix;
        final String json = GSON.toJson(w);
        try {

            if (mConnection.isConnected()) {
                Log.i(LOG, "### WebSocket Status: Connected. using: " + url + " sending: \n" + json);
                mConnection.sendTextMessage(json);
            } else {
               connect(url,json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void connect(final String url, final String json) {
        WebSocketOptions options = new WebSocketOptions();
        options.setSocketConnectTimeout(5000);
        options.setSocketReceiveTimeout(1000);
        try {

            if (mConnection.isConnected()) {
                Log.i(LOG, "### Status: Connected to " + url + " sending: \n" + json);
                mConnection.sendTextMessage(json);
            } else {
                mConnection.connect(url, new WebSocketHandler() {
                    @Override
                    public void onOpen() {
                        Log.e(LOG, "OnOpen: Connected to " + url + " sending...: \n" + json);
                        mConnection.sendTextMessage(json);
                    }

                    @Override
                    public void onTextMessage(String payload) {
                        try {
                            ResponseDTO r = GSON.fromJson(payload, ResponseDTO.class);
                            Log.i(LOG, "Response with sessionID: " + r.getSessionID());
                        } catch (Exception e) {
                            socketListener.onError("Failed to communicate, data format may be a problem");
                        }
                    }

                    @Override
                    public void onBinaryMessage(byte[] payload) {
                        ByteBuffer byteBuffer = ByteBuffer.wrap(payload);
                        parseData(byteBuffer);
                    }

                    @Override
                    public void onClose(int code, String reason) {
                        Log.e(LOG, "Connection lost. " + reason + ". will issue disconnect");
                        mConnection.disconnect();
                        socketListener.onError("Connection to server lost. Please try again.");
                    }
                },options);
            }
        } catch (WebSocketException e) {
            Log.e(LOG, "WebSocket failed.", e);
            socketListener.onError(e.getMessage());
        }
    }
    private static void parseData(ByteBuffer bb) {
        Log.i(LOG, "### parseData ByteBuffer capacity: " + ZipUtil.getKilobytes(bb.capacity()));
        String content = null;
        try {
            try {
                content = new String(bb.array());
                ResponseDTO response = GSON.fromJson(content, ResponseDTO.class);
                if (response.getStatusCode() == 0) {
                    socketListener.onMessage(response);
                } else {
                    socketListener.onError(response.getMessage());
                }
                return;

            } catch (Exception e) {
                content = ZipUtil.uncompressGZip(bb);
            }

            if (content != null) {
                ResponseDTO response = GSON.fromJson(content, ResponseDTO.class);
                if (response.getStatusCode() == 0) {
                    Log.w(LOG, "### response status code is 0 - OK");
                    socketListener.onMessage(response);
                } else {
                    Log.e(LOG, "## response status code is > 0 - server found ERROR");
                    socketListener.onError(response.getMessage());
                }
            } else {
                Log.e(LOG, "-- Content from server failed. Response content is null");
                socketListener.onError("Content from server failed. Response is null");
            }

        } catch (Exception e) {
            Log.e(LOG, "parseData Failed", e);
            socketListener.onError("Failed to unpack server response");
        }
    }


}

