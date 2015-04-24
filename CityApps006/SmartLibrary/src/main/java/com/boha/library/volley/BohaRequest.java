package com.boha.library.volley;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.zip.InflaterInputStream;

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
    public static String decompress(byte[] bytes) {
        InputStream in = new InflaterInputStream(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[8192];
            int len;
            while((len = in.read(buffer))>0)
                baos.write(buffer, 0, len);
            return new String(baos.toByteArray(), "UTF-8");
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
    @Override
    protected Response<String> parseNetworkResponse(
            NetworkResponse response) {
        String dto = new String(response.data);
//        try {
//            Gson gson = new Gson();
//            dto = new String(response.data);
//            Log.i(LOG, "response string length returned: " + dto.length());
//            byte[] bytes = Base64.decode(dto,Base64.NO_WRAP);
//            GZIPInputStream zis = new GZIPInputStream(new ByteArrayInputStream(bytes));
//            BufferedReader bf = new BufferedReader(new InputStreamReader(zis));
//            String outStr = "";
//            String line;
//            while ((line=bf.readLine())!=null) {
//                outStr += line;
//            }
//            dto = outStr;
//        } catch (Exception e) {
//            VolleyError ve = new VolleyError("Exception parsing server data", e);
//            errorListener.onErrorResponse(ve);
//            Log.e(LOG, "Unable to complete request", e);
//            return Response.error(new VolleyError("Server error"));
//        }
        end = System.currentTimeMillis();
        Log.e(LOG, "#### comms elapsed time in seconds: " + getElapsed(start, end));
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
