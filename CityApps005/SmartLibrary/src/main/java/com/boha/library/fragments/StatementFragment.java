package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.adapters.StatementAdapter;
import com.boha.library.dto.AccountDTO;
import com.boha.library.services.StatementService;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.FileDownloader;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;

import org.joda.time.DateTime;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class StatementFragment extends Fragment implements PageFragment{

    ResponseDTO response;
    TextView txtTitle, txtDate, txtCount;
    View view, fab, topView, handle;
    Context ctx;
    ImageView heroImage;
    Activity activity;
    String pageTitle;
    int primaryColor, darkColor, month, year;
    FragmentManager fragmentManager;
    List<AccountDTO> accountList;
    AccountDTO account;
    List<String> fileNames;
    StatementAdapter statementAdapter;
    ListView listView;
    ProgressBar progressBar;

    public void setAccountList(List<AccountDTO> accountList) {
        Log.d(LOG,"### setAccountList");
        this.accountList = accountList;
        account = accountList.get(0);
        getCachedStatements();
    }


    static final String LOG = StatementFragment.class.getSimpleName();

    public static StatementFragment newInstance() {
        StatementFragment fragment = new StatementFragment();

        return fragment;
    }

    public StatementFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            response = (ResponseDTO) getArguments().getSerializable("response");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(LOG,"### onCreateView");
        view = inflater.inflate(R.layout.fragment_statement, container, false);
        fragmentManager = getFragmentManager();
        ctx = getActivity();
        activity = getActivity();
        setFields();

        animateSomething();
        return view;
    }


    List<String> filePathList = new ArrayList<>();
    private void getCachedStatements() {

        File dir = Environment.getExternalStorageDirectory();
        File[] files = dir.listFiles();
        for (File file: files) {
            if (file.getName().contains(account.getAccountNumber())) {
                if (file.getName().contains(".pdf")) {
                    filePathList.add(file.getAbsolutePath());
                }
            }
        }
        if (filePathList.isEmpty()) {
            getPDFStatements();
        } else {
            setList();
        }
    }

    private void setList() {
        txtCount.setText("" + filePathList.size());
        String p = filePathList.get(0);
        try {
            int i =  p.indexOf("_");
            String ys = p.substring(i + 1, i + 5);
            i = p.lastIndexOf("_");
            int j = p.lastIndexOf(".");
            String ms = p.substring(i + 1, j);
            DateTime dateTime = new DateTime(Integer.parseInt(ys),Integer.parseInt(ms),1,0,0);
            txtDate.setText(sdf.format(dateTime.toDate()));
        } catch (Exception e) {
            txtDate.setText("Unavailable Date");
        }
        statementAdapter = new StatementAdapter(ctx,R.layout.statement_item,filePathList);
        listView.setAdapter(statementAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(filePathList.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                ctx.startActivity(intent);
            }
        });
    }
    private void setFields() {
        topView = view.findViewById(R.id.ST_top);
        handle = view.findViewById(R.id.ST_handle);
        txtDate = (TextView) view.findViewById(R.id.ST_subtitle);
        txtCount = (TextView) view.findViewById(R.id.ST_count);
        heroImage = (ImageView)view.findViewById(R.id.ST_hero);
        txtTitle = (TextView) view.findViewById(R.id.ST_title);
        listView = (ListView)view.findViewById(R.id.ST_list);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        fab = view.findViewById(R.id.FAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(fab, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        showDatePicker();
                    }
                });
            }
        });
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtDate, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        showDatePicker();
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void animateSomething() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timer.cancel();
                        heroImage.setImageDrawable(Util.getRandomBackgroundImage(ctx));
                        Util.expand(heroImage, 1000, new Util.UtilAnimationListener() {
                            @Override
                            public void onAnimationEnded() {
                                Util.flashOnce(fab, 300, null);
                            }
                        });
                    }
                });
            }
        }, 500);
    }

    @Override
    public void setThemeColors(int primaryColor, int primaryDarkColor) {
        this.primaryColor = primaryColor;
        this.darkColor = primaryDarkColor;
        if (topView != null) {
            topView.setBackgroundColor(primaryColor);
        }
    }

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    private void getPDFStatements() {
        Log.w(LOG,"$$$ getPDFStatements");
        RequestDTO w = new RequestDTO(RequestDTO.GET_PDF_STATEMENT);
        w.setAccountNumber(account.getAccountNumber());
        w.setYear(year);
        w.setMonth(month);
        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());

        progressBar.setVisibility(View.VISIBLE);
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            if (response.getStatusCode() == 0) {
                                Log.i(LOG, "+++ we cool, cool ...");
                                if (response.isMunicipalityAccessFailed()) {
                                    Util.showErrorToast(ctx, ctx.getString(R.string.unable_connect_muni));
                                    return;
                                }
                                fileNames = response.getPdfFileNameList();
                                FileDownloader.downloadStatementPDF(ctx, account.getAccountNumber(),
                                        year, month, new FileDownloader.FileDownloaderListener() {
                                            @Override
                                            public void onFileDownloaded(File file) {
                                                Intent w = new Intent(ctx, StatementService.class);
                                                w.putExtra("response", response);
                                                w.putExtra("accountNumber", account.getAccountNumber());
                                                ctx.startService(w);

                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                                ctx.startActivity(intent);

                                            }

                                            @Override
                                            public void onError() {

                                            }
                                        });
                            }
                        }
                    });
                }

            }

            @Override
            public void onError(final String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Util.showErrorToast(ctx, message);
                        }
                    });
                }
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }
    private void showDatePicker() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setDatePickerListener(new DatePickerFragment.DatePickerListener() {
            @Override
            public void onYearMonthPicked(int y, int m) {
                year = y;
                month = m + 1;
                getPDFStatements();
            }
        });
        newFragment.show(fragmentManager, "datePicker");
    }
    static final Locale d = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", d);
}
