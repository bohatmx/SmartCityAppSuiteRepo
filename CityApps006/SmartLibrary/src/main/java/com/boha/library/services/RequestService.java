package com.boha.library.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.NetUtil;
import com.boha.library.util.RequestCache;
import com.boha.library.util.RequestList;

import org.joda.time.DateTime;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RequestService extends IntentService {


    public RequestService() {
        super("RequestService");
    }

    public interface RequestServiceListener {
        void onRequestsProcessed(int goodResponses, int badResponses);
        void onError(String message);
    }

    RequestServiceListener listener;
    static final String LOG = RequestService.class.getSimpleName();

    public void sendRequests(RequestServiceListener listener) {
        this.listener = listener;
        onHandleIntent(null);
    }
    static final int THIRTY_SECONDS = 1000 * 30;
    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(LOG,"*** getting cached requests");
        RequestCache.getRequests(getApplicationContext(), new RequestCache.RequestCacheListener() {
            @Override
            public void onRequestAdded() {

            }

            @Override
            public void onRequestsFound(RequestList list) {

                if (list.getRequestList().isEmpty()) {
                    listener.onRequestsProcessed(0,0);
                    return;
                }
                RequestDTO req = list.getRequestList().get(0);
                DateTime now = new DateTime();
                DateTime lastSync = null;
                if (req.getLastSyncAttemptDate() != null) {
                   lastSync = new DateTime(req.getLastSyncAttemptDate().longValue());
                } else {
                    RequestCache.updateRequests(getApplicationContext(),null);
                    return;
                }

                if (now.getMillis() - lastSync.getMillis() < THIRTY_SECONDS) {
                    Log.d(LOG,"*** not sending cached request, within pause window");
                    return;
                }
                for (RequestDTO p: list.getRequestList()) {
                    if (p.getProfileInfo() != null) {
                        ProfileInfoDTO x = new ProfileInfoDTO();
                        x.setProfileInfoID(p.getProfileInfo().getProfileInfoID());
                        p.setProfileInfo(x);
                    }
                }
                RequestDTO w = new RequestDTO(RequestDTO.PROCESS_CACHED_REQUESTS);
                w.setRequestList(list);
                Log.d(LOG, "### sending cached requests: " + list.getRequestList().size());

                NetUtil.sendRequest(getApplicationContext(), w, new NetUtil.NetUtilListener() {
                    @Override
                    public void onResponse(final ResponseDTO response) {
                        Log.i(LOG,"### cached requests synced: good: " + response.getGoodResponses() + " bad: " + response.getBadResponses());
                        if (response.getBadResponses() > 0) {
                            Log.e(LOG,"--- keeping the cache because of bad responses");
                            RequestCache.updateRequests(getApplicationContext(),null);
                        } else {
                            RequestCache.removeRequests(getApplicationContext(), new RequestCache.RequestCacheListener() {
                                @Override
                                public void onRequestAdded() {

                                }

                                @Override
                                public void onRequestsFound(RequestList list) {

                                }

                                @Override
                                public void onError() {

                                }

                                @Override
                                public void onRequestsRemoved() {
                                    Log.w(LOG, "---- onRequestsRemoved .....");
                                    listener.onRequestsProcessed(response.getGoodResponses(), response.getBadResponses());
                                }
                            });
                        }

                    }

                    @Override
                    public void onError(String message) {
                        Log.e(LOG,"-- RequestService sendRequest failed: " + message);
                        listener.onError(message);
                    }

                    @Override
                    public void onWebSocketClose() {

                    }
                });
            }

            @Override
            public void onError() {

            }

            @Override
            public void onRequestsRemoved() {

            }
        });
    }


    public class LocalBinder extends Binder {
        public RequestService getService() {
            Log.e(LOG, "LocalBinder getService");
            return RequestService.this;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.w(LOG, "IBinder onBind - returning binder");
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();
}
