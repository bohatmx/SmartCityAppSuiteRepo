package com.boha.library.util;

import android.content.Context;
import android.util.Log;

import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by aubreyM on 15/07/07.
 */
public class CommsUtil {
    public interface CommsListener {
        void onDataOK(String data);

        void noDataFound();

        void onError(String message);
    }

    static CommsListener listener;
    static final String COMMS = CommsUtil.class.getSimpleName();
    private static final String USER_AGENT = "Mozilla/5.0",
            AUTH = "Authorization", BASIC = "Basic ", USER_AGENT_STRING = "User-Agent",
            GET = "GET";
    private static final int TIME_OUT_MILLISECONDS = 10000;

    public static void getData(Context ctx, final String url, int requestType, CommsListener commsListener) {
        listener = commsListener;
        RequestDTO w = new RequestDTO(requestType);
        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());
        w.setDataURL(url);
        w.setRideWebSocket(false);
        w.setZipResponse(false);

        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(ResponseDTO response) {
                Log.i(COMMS, "status code: " + response.getStatusCode() + " data: " + response.getData());
                if (response.getStatusCode() > 0) {
                    listener.onError(response.getMessage());
                    return;
                }
                JSONObject object = null;
                try {
                    object = new JSONObject(response.getData());
                    if (object.has("ERROR")) {
                        JSONObject err = object.getJSONObject("ERROR");
                        if (err != null) {
                            String x = err.getString("ErrorMessage");
                            listener.onError(x);
                            return;
                        }
                    }
                    if (object.has("News")) {
                        JSONObject news = object.getJSONObject("News");
                        if (news != null) {
                            String x = news.getString("body");
                            Log.d(COMMS, x);
                            listener.onDataOK(x);
                            return;
                        } else {
                            listener.onError("No detail data found");
                        }

                    }


                } catch (JSONException e) {
                    listener.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String message) {
                Log.e(COMMS, message);
                listener.onError(message);
            }

            @Override
            public void onWebSocketClose() {

            }
        });


    }

    private static String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1024);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }
}
