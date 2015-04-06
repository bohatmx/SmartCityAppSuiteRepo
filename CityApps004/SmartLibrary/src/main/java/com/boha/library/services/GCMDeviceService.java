package com.boha.library.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.boha.library.dto.GcmDeviceDTO;
import com.boha.library.dto.MunicipalityStaffDTO;
import com.boha.library.dto.ProfileInfoDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.GCMUtil;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GCMDeviceService extends IntentService {

    static final String LOG = GCMDeviceService.class.getSimpleName();
    public GCMDeviceService() {
        super("GCMDeviceService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(LOG, "*** onHandleIntent service starting");
        if (intent != null) {
            ProfileInfoDTO profile = (ProfileInfoDTO)intent.getSerializableExtra("profile");
            if (profile != null) {
                registerGCMDevice(profile);
            }
            MunicipalityStaffDTO staff = (MunicipalityStaffDTO)intent.getSerializableExtra("staff");
            if (staff != null) {
                registerGCMDevice(staff);
            }
        }
    }
    private void registerGCMDevice(final MunicipalityStaffDTO staff) {

        Log.w(LOG, "... registering staff for GCM");
        GCMUtil.startGCMRegistration(getApplicationContext(), new GCMUtil.GCMUtilListener() {
            @Override
            public void onDeviceRegistered(String id) {

                Log.i(LOG, "############# GCM - STAFF device registered: we cool, cool.....: " + id);
                GcmDeviceDTO gcmDevice = getDevice(id);
                gcmDevice.setMunicipalityID(staff.getMunicipalityID());
                gcmDevice.setMunicipalityStaffID(staff.getMunicipalityStaffID());

                RequestDTO w = new RequestDTO(RequestDTO.ADD_GCM_DEVICE);
                w.setGcmDevice(gcmDevice);

                NetUtil.sendRequest(getApplicationContext(), w, new NetUtil.NetUtilListener() {
                    @Override
                    public void onResponse(final ResponseDTO resp) {
                        if (resp.getStatusCode() > 0) {
                            Log.e(LOG, "ERROR - " + resp.getMessage());
                            return;
                        }
                        Log.i(LOG, "### response OK from server");
                        SharedUtil.storeRegistrationId(getApplicationContext(), resp.getGcmRegistrationID());
                    }

                    @Override
                    public void onError(String message) {
                        Log.e(LOG, "ERROR - " + message);
                    }

                    @Override
                    public void onWebSocketClose() {

                    }
                });
            }

            @Override
            public void onGCMError() {
                Log.e(LOG, "##### onGCMError");
            }
        });
    }
    private void registerGCMDevice(final ProfileInfoDTO profile) {

        Log.w(LOG, "... registering citizen USER for GCM");
        GCMUtil.startGCMRegistration(getApplicationContext(), new GCMUtil.GCMUtilListener() {
            @Override
            public void onDeviceRegistered(String id) {
                Log.i(LOG, "############# GCM - USER registered: we cool, cool.....: " + id);
                GcmDeviceDTO gcmDevice = getDevice(id);
                gcmDevice.setMunicipalityID(profile.getMunicipalityID());
                gcmDevice.setProfileInfoID(profile.getProfileInfoID());

                RequestDTO w = new RequestDTO(RequestDTO.ADD_GCM_DEVICE);
                w.setGcmDevice(gcmDevice);

                NetUtil.sendRequest(getApplicationContext(), w, new NetUtil.NetUtilListener() {
                    @Override
                    public void onResponse(final ResponseDTO resp) {
                        if (resp.getStatusCode() > 0) {
                            Log.e(LOG, "ERROR - " + resp.getMessage());
                            return;
                        }
                        Log.i(LOG, "### response OK from server");
                        SharedUtil.storeRegistrationId(getApplicationContext(), resp.getGcmRegistrationID());
                    }

                    @Override
                    public void onError(String message) {
                        Log.e(LOG, "ERROR - " + message);
                    }

                    @Override
                    public void onWebSocketClose() {

                    }
                });
            }

            @Override
            public void onGCMError() {
                Log.e(LOG, "##### onGCMError");
            }
        });
    }

    private GcmDeviceDTO getDevice(String registrationID) {
        GcmDeviceDTO gcmDevice = new GcmDeviceDTO();
        gcmDevice.setManufacturer(Build.MANUFACTURER);
        gcmDevice.setModel(Build.MODEL);
        gcmDevice.setSerialNumber(Build.SERIAL);
        gcmDevice.setAndroidVersion(Build.VERSION.RELEASE);
        gcmDevice.setGcmRegistrationID(registrationID);

        return gcmDevice;
    }
}
