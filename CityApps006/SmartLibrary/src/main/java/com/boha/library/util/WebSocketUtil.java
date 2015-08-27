package com.boha.library.util;

import android.content.Context;
import android.util.Log;

import com.boha.library.R;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.google.gson.Gson;

import org.acra.ACRA;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

/**
 * Convenience class to wrap Autobahn WebSocket library. Sends RequestDTO as JSON
 * string to web application.
 * <p/>
 * Created by aubreyM on 15/04/19.
 */
public class WebSocketUtil {

    static final String LOG = WebSocketUtil.class.getSimpleName();
    static WebSocketListener webSocketListener;
    static WebSocketConnection mConnection = new WebSocketConnection();
    static final Gson GSON = new Gson();
    static Context context;
    static final int CONNECT_RETRIES = 100, WAIT_INTERVAL = 2000;
    static int retryCount;
    static RequestDTO request;
    static String mSuffix;

    private static void reconnectSession() {
        if (request != null) {
            if (webSocketListener != null) {
                mConnection.disconnect();
                sendRequest(context, mSuffix, request, webSocketListener);
            }
         }
    }

    public interface WebSocketListener {
        void onMessage(ResponseDTO response);

        void onError(String message);
    }


    static TimerUtil timerUtil;
    public static void sendRequest(Context ctx, final String suffix,
                                   RequestDTO w, WebSocketListener listener) {
        webSocketListener = listener;
        request = w;
        mSuffix = suffix;
        context = ctx;
        retryCount = 0;
        final String url = Statics.WEBSOCKET_URL + suffix;
        final String json = GSON.toJson(w);
        try {
            timerUtil = new TimerUtil();
            timerUtil.startTimer(new TimerUtil.TimerListener() {
                @Override
                public void onTimerExpired() {
                    timerUtil.killTimer();
                    mConnection.disconnect();
                    sendRequest(context, mSuffix, request, webSocketListener);
                    return;
                }
            });

            if (mConnection.isConnected()) {
                Log.i(LOG, "### WebSocket Status: Connected. using: " + url + " sending: \n" + json);
                mConnection.sendTextMessage(json);
            } else {
                connect(url, json);
            }
        } catch (Exception e) {
            listener.onError("Websocket failure: " + e.getMessage());
        }
    }

    private static void connect(final String url, final String json) {
        Log.d(LOG,"&&&&&&& CONNECT TO WEBSOCKET, retryCount: " + retryCount);
        WebSocketOptions options = new WebSocketOptions();
        options.setSocketConnectTimeout(5000);
        options.setSocketReceiveTimeout(1000);
        try {
            mConnection.connect(url, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    Log.e(LOG, "connect(). OnOpen: Connected to " + url + " sending...: \n" + json);
                    timerUtil.killTimer();
                    mConnection.sendTextMessage(json);
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(LOG,"+++ onTextMessage payload size: " + getSize(payload.length()));
                    timerUtil.killTimer();
                    retryCount = 0;
                    try {
                        ResponseDTO r = GSON.fromJson(payload, ResponseDTO.class);
                        if (r.getSessionID() != null) {
                            Log.i(LOG, "Response with sessionID: " + r.getSessionID());
//                            mConnection.sendTextMessage(json);

                        } else {
                            if (r.getStatusCode() == 0) {
                                webSocketListener.onMessage(r);
                            } else {
                                webSocketListener.onError(r.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        webSocketListener.onError(context.getString(R.string.failed_comms));
                    }
                }

                @Override
                public void onBinaryMessage(byte[] payload) {
                    timerUtil.killTimer();
                    retryCount = 0;
                    ByteBuffer byteBuffer = ByteBuffer.wrap(payload);
                    parseData(byteBuffer);
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.e(LOG, "----onClose().  Connection lost. " + reason + ". will issue disconnect");
                    timerUtil.killTimer();
                    mConnection.disconnect();
                    try {
                        if (retryCount < CONNECT_RETRIES) {
                            retryCount++;
                            final Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (!mConnection.isConnected()) {
                                        connect(url, json);
                                    } else {
                                        timer.cancel();
                                        sendRequest(context,mSuffix,request,webSocketListener);
                                    }
                                }
                            }, retryCount * WAIT_INTERVAL);

                        } else {
                            webSocketListener.onError(context.getString(R.string.conn_interrupted));
                        }
                    } catch (Exception e) {
                        Log.w(LOG,"websocket failed", e);
                    }


                }
            }, options);

        } catch (WebSocketException e) {
            Log.e(LOG, "WebSocket failed.", e);
            webSocketListener.onError(e.getMessage());
        }
    }

    private static void parseData(ByteBuffer bb) {
        Log.d(LOG, "### parseData ByteBuffer capacity: " + ZipUtil.getKilobytes(bb.capacity()));
        String content = null;
        try {
            try {
                content = new String(bb.array());
                ResponseDTO response = GSON.fromJson(content, ResponseDTO.class);
                if (response.getStatusCode() == 0) {
                    webSocketListener.onMessage(response);
                } else {
                    webSocketListener.onError(response.getMessage());
                }
                return;

            } catch (Exception e) {
                content = ZipUtil.uncompressGZip(bb);
            }

            if (content != null) {
                ResponseDTO response = GSON.fromJson(content, ResponseDTO.class);
                if (response.getStatusCode() == 0) {
                    Log.i(LOG, "### response status code is 0 - OK");
                    webSocketListener.onMessage(response);
                } else {
                    Log.e(LOG, "--- response status code is "+response.getStatusCode()+ " - " + response.getMessage());
                    webSocketListener.onError(response.getMessage());
                }
            } else {
                Log.e(LOG, "-- Content from server failed. Response content is null");
                try {
                    ACRA.getErrorReporter().handleException(new UnsupportedOperationException("Response content is NULL"),false);
                } catch (Exception ex) {}
                webSocketListener.onError("Content from server failed. Response is null");
            }

        } catch (Exception e) {
            Log.e(LOG, "parseData Failed", e);
            try {
                ACRA.getErrorReporter().handleException(e,false);
            } catch (Exception ex) {}
            webSocketListener.onError("Failed to unpack server response. Please try again.");
        }
    }

    private static String getSize(int size) {
        Double x = Double.parseDouble("" + size);
        Double y = x/Double.parseDouble("1024");
        return df.format(y) + "KB";
    }
static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,##0.00");
}

