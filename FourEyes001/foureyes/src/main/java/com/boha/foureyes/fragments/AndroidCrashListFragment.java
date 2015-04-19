package com.boha.foureyes.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.foureyes.R;
import com.boha.foureyes.adapters.AndroidCrashAdapter;
import com.boha.foureyes.dto.ErrorStoreAndroidDTO;
import com.boha.foureyes.dto.ResponseDTO;
import com.boha.foureyes.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by aubreyM on 2014/04/09.
 */
public class AndroidCrashListFragment extends Fragment implements PageFragment {

    public interface CrashListListener {
        void onRefreshRequested();
    }

    CrashListListener crashListListener;
    TextView txtMsg;
    ImageView imageView;

    @Override
    public void onAttach(Activity a) {
        if ( a instanceof  CrashListListener) {
            crashListListener = (CrashListListener)a;
        } else {
            throw new UnsupportedOperationException("Host activity " + a.getLocalClassName() +
             " must implement CrashListListener");
        }
//        Log.i(LOG,
//                "onAttach ---- Fragment called and hosted by "
//                        + a.getLocalClassName()
//        );
        super.onAttach(a);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saved) {
        ctx = getActivity();
        inflater = getActivity().getLayoutInflater();
        view = inflater
                .inflate(R.layout.fragment_android, container, false);

        setFields();
        if (response != null) {
            setList();
            return view;
        }
        if (saved != null) {
            response = (ResponseDTO) saved.getSerializable("response");
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                response = (ResponseDTO) bundle.getSerializable("response");
                if (response.getErrorStoreAndroidList() == null)
                    errorStoreAndroidList = new ArrayList<ErrorStoreAndroidDTO>();
                else
                    errorStoreAndroidList = response.getErrorStoreAndroidList();
            }
        }
        setList();

        return view;
    }

    public void setAdndroidErrorList(List<ErrorStoreAndroidDTO> list) {
        Log.i(LOG, "setting errorList ....");
        this.errorStoreAndroidList = list;
        setList();

    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        b.putSerializable("response", response);
        super.onSaveInstanceState(b);
    }

    public void setFields() {
        listView = (ListView) view.findViewById(R.id.ERR_list);
        txtCount = (TextView) view.findViewById(R.id.ERR_count);
        txtMsg = (TextView) view.findViewById(R.id.ERR_msg);
        imageView = (ImageView)view.findViewById(R.id.ERR_image);
        imageView.setImageDrawable(getRandomDrawable());

        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtCount, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        crashListListener.onRefreshRequested();
                    }
                });

            }
        });




    }

    public void setList() {
        if (errorStoreAndroidList == null)
            errorStoreAndroidList = new ArrayList<>();
        if (errorStoreAndroidList.isEmpty()) {
            imageView.setVisibility(View.VISIBLE);
            txtMsg.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(getRandomDrawable());
            return;
        }
        imageView.setVisibility(View.GONE);
        txtMsg.setVisibility(View.GONE);
        androidCrashAdapter = new AndroidCrashAdapter(ctx, R.layout.android_crash_item, errorStoreAndroidList);
        listView.setAdapter(androidCrashAdapter);
        txtCount.setText("" + errorStoreAndroidList.size());
    }


    ListView listView;
    TextView txtHeader, txtCount;

    static final String LOG = "DroidCrashListFrag";
    Context ctx;
    View view;
    ResponseDTO response;
    List<ErrorStoreAndroidDTO> errorStoreAndroidList;
    AndroidCrashAdapter androidCrashAdapter;
    Random random = new Random(System.currentTimeMillis());
    private Drawable getRandomDrawable() {
        int index = random.nextInt(6);
        switch (index) {
            case 0:
                return ctx.getResources().getDrawable(R.drawable.happiness1);
            case 1:
                return ctx.getResources().getDrawable(R.drawable.happy2);
            case 2:
                return ctx.getResources().getDrawable(R.drawable.happy3);
            case 3:
                return ctx.getResources().getDrawable(R.drawable.happy4);
            case 4:
                return ctx.getResources().getDrawable(R.drawable.happy5);
            case 5:
                return ctx.getResources().getDrawable(R.drawable.happy6);
            case 6:
                return ctx.getResources().getDrawable(R.drawable.happy6);
        }

        return ctx.getResources().getDrawable(R.drawable.happiness1);
    }

}
