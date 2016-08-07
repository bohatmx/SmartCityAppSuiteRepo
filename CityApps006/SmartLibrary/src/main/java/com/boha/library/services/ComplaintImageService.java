package com.boha.library.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.boha.library.dto.ComplaintDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.NetUtil;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ComplaintImageService extends IntentService {

    public static final String BROADCAST_COMPLAINT_IMAGES_FOUND = "com.boha.COMPLAINT_IMAGES";
    public ComplaintImageService() {
        super("ComplaintImageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ComplaintDTO complaint = (ComplaintDTO)intent.getSerializableExtra("complaint");
        if (complaint != null) {
            RequestDTO w = new RequestDTO(RequestDTO.GET_COMPLAINT_IMAGES);
            w.setMunicipalityID(complaint.getMunicipalityID());
            w.setReferenceNumber(complaint.getReferenceNumber());

            NetUtil.sendRequest(getApplicationContext(), w, new NetUtil.NetUtilListener() {
                @Override
                public void onResponse(ResponseDTO response) {
                    if (response.getComplaintImageList() != null) {
                        complaint.setComplaintImageList(response.getComplaintImageList());
                        Intent m = new Intent(BROADCAST_COMPLAINT_IMAGES_FOUND);
                        m.putExtra("complaint",complaint);
                        LocalBroadcastManager bm = LocalBroadcastManager.getInstance(getApplicationContext());
                        bm.sendBroadcast(m);
                    }
                }

                @Override
                public void onError(String message) {

                }

                @Override
                public void onWebSocketClose() {

                }
            });
        }
    }

}
