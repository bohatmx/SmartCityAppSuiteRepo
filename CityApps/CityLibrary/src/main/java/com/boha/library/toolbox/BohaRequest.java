package com.boha.library.toolbox;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.math.BigDecimal;

public class BohaRequest extends Request<String> {

    private Listener<String> listener;
    private ErrorListener errorListener;
    private long start, end;

    public BohaRequest(int method, String url, ErrorListener listener) {
        super(method, url, listener);
    }

    public BohaRequest(int method, String url,
                       Listener<String> responseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = responseListener;
        this.errorListener = errorListener;
        start = System.currentTimeMillis();
        Log.i(LOG, "...Cloud Server communication started ...");

    }

    @Override
    protected Response<String> parseNetworkResponse(
            NetworkResponse response) {
        String dto = new String();
        try {
            Gson gson = new Gson();
            dto = new String(response.data);
            Log.i(LOG, "response string length returned: " + dto.length());

//            InputStream is = new ByteArrayInputStream(response.data);
//            ZipInputStream zis = new ZipInputStream(is);
//            @SuppressWarnings("unused")
//            ZipEntry entry;
//            ByteArrayBuffer bab = new ByteArrayBuffer(2048);
//
//            while ((entry = zis.getNextEntry()) != null) {
//                int size = 0;
//                byte[] buffer = new byte[2048];
//                while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
//                    bab.append(buffer, 0, size);
//                }
//                resp = new String(bab.toByteArray());
//                dto = gson.fromJson(resp, String.class);
//            }
        } catch (Exception e) {
            VolleyError ve = new VolleyError("Exception parsing server data", e);
            errorListener.onErrorResponse(ve);
            Log.e(LOG, "Unable to complete request", e);
            return Response.error(new VolleyError("Server error"));
        }
        end = System.currentTimeMillis();
        Log.e(LOG,"#### comms elapsed time in seconds: " + getElapsed(start,end));
        return Response.success(dto,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        end = System.currentTimeMillis();
        listener.onResponse(response);
    }

    public static double getElapsed(long start, long end) {
        BigDecimal m = new BigDecimal(end - start).divide(new BigDecimal(1000));
        return m.doubleValue();
    }

    static final String LOG = "BohaRequest";
}
