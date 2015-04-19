package com.boha.foureyes.util;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by aubreyM on 15/03/07.
 */
public class DistanceUtil {

    public interface DistanceListener {
        public void onDistanceAcquired(String fromAddress,
                                       String toAddress, double distance, int durationMinutes);
        public void onError(String message);
    }
    static DistanceListener distanceListener;
    public static void getDistance(Location locOrigin, Location locDest,
                                   DistanceListener dl){

        distanceListener = dl;
        StringBuilder sb = new StringBuilder();
        sb.append(Statics.GOOGLE_DISTANCE_MATRIX_URL);
        sb.append(locOrigin.getLatitude()).append(",").append(locOrigin.getLongitude());
        sb.append("&destinations=").append(locDest.getLatitude()).append(",");
        sb.append(locDest.getLongitude());
        Log.e(LOG, "## URL: " + sb.toString());
        request = sb.toString();
        new Comms().execute();
    }
    public static void getDistance(double origLat, double origLn,
                                   double destLat, double destLng,
                                   DistanceListener dl) {

        distanceListener = dl;
        StringBuilder sb = new StringBuilder();
        sb.append(Statics.GOOGLE_DISTANCE_MATRIX_URL);
        sb.append(origLat).append(",").append(origLn);
        sb.append("&destinations=").append(destLat).append(",");
        sb.append(destLng);
        Log.e(LOG, "## URL: " + sb.toString());
        request = sb.toString();
        new Comms().execute();
    }


    static String request, response, fromAddress, toAddress;
    static double distance;
    static int duration;
    static class Comms extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            HttpURLConnection con = null;
            URL url;
            response = null;
            InputStream is = null;

            try {
                url = new URL(request);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                is = con.getInputStream();
                int httpCode = con.getResponseCode();
                String msg = con.getResponseMessage();
                Log.w(LOG, "### HTTP response code: " + httpCode + " msg: " + msg);
                response = readStream(is);
                Log.d(LOG, "### RESPONSE string: \n" + response);
                int idx = response.indexOf("DOCTYPE html");
                if (idx > -1) {
                    Log.e(LOG, "@@@ ERROR RESPONSE, some html received:\n"
                            + response);
                    return 7;
                }
                //get distance
                StringBuilder sb = new StringBuilder();
                JSONObject object = new JSONObject(response);
                String status = object.getString("status");
                if (status.equalsIgnoreCase("OK")) {
                    JSONArray originArr = object.getJSONArray("origin_addresses");
                    for (int i = 0; i < originArr.length(); i++) {
                        fromAddress = originArr.getString(i);
                        sb.append("From: ").append(fromAddress).append(" to ");
                    }
                    JSONArray destArr = object.getJSONArray("destination_addresses");
                    for (int i = 0; i < destArr.length(); i++) {
                        toAddress = destArr.getString(i);
                        sb.append(toAddress).append("\n");
                    }
                    Log.w(LOG, "## " + sb.toString());
                    JSONArray rows = object.getJSONArray("rows"); // Get all JSONArray rows

                    for (int i = 0; i < rows.length(); i++) { // Loop over each each row
                        Log.d(LOG, "** loop thru rows");
                        JSONObject row = rows.getJSONObject(i); // Get row object
                        JSONArray elements = row.getJSONArray("elements"); // Get all elements for each row as an array
                        for (int j = 0; j < elements.length(); j++) { // Iterate each element in the elements array
                            Log.d(LOG, "** loop thru elements");
                            JSONObject element = elements.getJSONObject(j); // Get the element object
                            JSONObject x = element.getJSONObject("distance"); // Get distance sub object
                            distance = x.getDouble("value")/1000;
                            JSONObject y = element.getJSONObject("duration");
                            duration = y.getInt("value")/60;
                            Log.i(LOG, "Distance calculated: " + distance);
                        }

                    }
                }
                Log.i(LOG, sb.toString());
            } catch (JSONException e) {
                Log.e(LOG, "Houston, we have an JSONException. F%$%K!", e);
                return 6;
            } catch (IOException e) {
                Log.e(LOG, "Houston, we have an IOException. F%$%K!", e);
                return 9;

            } catch (Exception e) {
                Log.e(LOG, "Failed", e);
                return 8;
            } finally {
                try {
                    is.close();
                } catch (Exception e) {
                    Log.e(LOG, "InputStream close Failed", e);
                }
            }


            return 0;
        }
        private String readStream(InputStream in) throws IOException {
            StringBuilder sb = new StringBuilder();
            BufferedReader r = new BufferedReader(new InputStreamReader(in), 1024);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                sb.append(line);
            }
            in.close();
            return sb.toString();
        }
        @Override
        public void onPostExecute(Integer result) {

            if (result > 0) {
                if (distanceListener != null) {
                    distanceListener.onError("Unable to calculate distance");
                }
                Log.e(LOG, "-- ERROR getting Google DistanceMatrix");
                return;
            }
            if (distanceListener != null) {
                distanceListener.onDistanceAcquired(fromAddress,toAddress,distance,duration);
            }
        }
    }

    private static final String LOG = DistanceUtil.class.getSimpleName();
}
