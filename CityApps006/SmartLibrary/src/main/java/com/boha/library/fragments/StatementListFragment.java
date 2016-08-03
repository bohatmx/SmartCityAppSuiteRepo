package com.boha.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.library.R;
import com.boha.library.activities.CityApplication;
import com.boha.library.adapters.StatementAdapter;
import com.boha.library.dto.AccountDTO;
import com.boha.library.transfer.RequestDTO;
import com.boha.library.transfer.ResponseDTO;
import com.boha.library.util.NetUtil;
import com.boha.library.util.SharedUtil;
import com.boha.library.util.Util;
import com.squareup.leakcanary.RefWatcher;

import org.joda.time.DateTime;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class StatementListFragment extends Fragment implements PageFragment {

    public interface StatementFragmentListener {
        void onPDFDownloaded(String fileName);

        void setBusy(boolean busy);
    }

    StatementFragmentListener statementFragmentListener;
    ResponseDTO response;
    TextView txtTitle, txtDate, txtCount, txtAccount;
    View view, fab, topView, handle;
    Context ctx;
    ImageView heroImage;
    Activity activity;
    String pageTitle;
    int primaryColor, darkColor, month, year, count;
    FragmentManager fragmentManager;
    List<AccountDTO> accountList;
    AccountDTO account;
    StatementAdapter statementAdapter;
    ListView listView;

    public void setAccount(AccountDTO account) {
        Log.d(LOG, "### setAccount");
        this.account = account;
        txtAccount.setText(account.getAccountNumber());
        getCachedStatements();
    }


    static final String LOG = StatementListFragment.class.getSimpleName();

    public static StatementListFragment newInstance() {
        StatementListFragment fragment = new StatementListFragment();

        return fragment;
    }

    public StatementListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            response = (ResponseDTO) getArguments().getSerializable("response");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(LOG, "### onCreateView");
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
        File myDir = new File(dir, "smartCity");
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        File[] files = myDir.listFiles();
        filePathList = new ArrayList<>();
        for (File file : files) {
            if (file.getName().contains(account.getAccountNumber())) {
                if (file.getName().contains("statement.pdf")) {
                    filePathList.add(file.getAbsolutePath());
                }
            }
        }

        if (filePathList.isEmpty()) {
            Log.d(LOG, "getCachedStatements: filePathList is empty");
        } else {
            fileContainerList.clear();
            for (String d : filePathList) {
                fileContainerList.add(new FileContainer(d));
            }
            Collections.sort(fileContainerList);
            filePathList.clear();
            for (FileContainer f : fileContainerList) {
                filePathList.add(f.fileName);
            }
            setList();
        }
    }

    private void setList() {
        txtCount.setText("" + filePathList.size());
        String p = filePathList.get(0);
        try {
            Pattern pat = Pattern.compile("-");
            String[] keyParts = pat.split(p);
            String acctNumber = keyParts[0];
            String year = keyParts[1];
            String month = keyParts[2];

            DateTime dateTime = new DateTime(Integer.parseInt(year), Integer.parseInt(month), 1, 0, 0);
            txtDate.setText(sdf.format(dateTime.toDate()));
        } catch (Exception e) {
            txtDate.setText("Unavailable Date");
        }
        statementAdapter = new StatementAdapter(ctx, R.layout.statement_item, filePathList);
        listView.setAdapter(statementAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (busyDownloading) {
                    Util.showToast(ctx, "Still downloading statements, please wait");
                    return;
                }
                File file = new File(filePathList.get(position));
                Log.e(LOG, "pdf file: " + file.getAbsolutePath() + " length: " + file.length());
                if (file.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(intent);
                } else {
                    Util.showToast(ctx, "Statement is still downloading");
                }
            }
        });

    }

    private void setFields() {
        topView = view.findViewById(R.id.ST_top);
        handle = view.findViewById(R.id.ST_handle);
        txtDate = (TextView) view.findViewById(R.id.ST_subtitle);
        txtCount = (TextView) view.findViewById(R.id.ST_count);
        txtAccount = (TextView) view.findViewById(R.id.ST_account);
        heroImage = (ImageView) view.findViewById(R.id.ST_hero);
        txtTitle = (TextView) view.findViewById(R.id.ST_title);
        listView = (ListView) view.findViewById(R.id.ST_list);


        txtDate.setText("Not Downloaded");
        txtCount.setText("0");
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
    public void onAttach(Activity a) {
        super.onAttach(activity);
        if (a instanceof StatementFragmentListener) {
            statementFragmentListener = (StatementFragmentListener) a;
        } else {
            throw new ClassCastException("Host activity " + a.getLocalClassName() + " must implement StatementFragmentListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = CityApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
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
                                Util.flashOnce(fab, 300, new Util.UtilAnimationListener() {
                                    @Override
                                    public void onAnimationEnded() {
                                    }
                                });
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

    boolean busyDownloading;

    public void getPDFStatement() {
        if (busyDownloading) {
            return;
        }

        busyDownloading = true;
        Snackbar.make(fab, "Statement download may take a few minutes", Snackbar.LENGTH_LONG).show();
        if (year == 0 || month == 0) {
            DateTime dateTime = new DateTime();
            year = dateTime.getYear();
            month = dateTime.getMonthOfYear();
        }
        Log.d(LOG, "*** year: " + year
                + " month: " + month +  " count: " + count + " selected for statement download");

        RequestDTO w = new RequestDTO(RequestDTO.GET_PDF_STATEMENT);
        w.setAccountNumber(account.getAccountNumber());
        w.setYear(year);
        w.setMonth(month);
        w.setCount(count);
        w.setMunicipalityID(SharedUtil.getMunicipality(ctx).getMunicipalityID());

        statementFragmentListener.setBusy(true);
        disableFab();
        NetUtil.sendRequest(ctx, w, new NetUtil.NetUtilListener() {
            @Override
            public void onResponse(final ResponseDTO response) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statementFragmentListener.setBusy(false);
                            busyDownloading = false;
                            enableFab();
                            if (response.getStatusCode() == 0) {
                                Log.i(LOG, "+++ statement(s) from server, we cool, cool ...");
                                if (response.getPdfHashMap() != null && !response.getPdfHashMap().isEmpty()) {
                                    Log.i(LOG, "Statements found on server: " + response.getPdfHashMap().size());

                                    for (String key : response.getPdfHashMap().keySet()) {
                                        byte[] data = response.getPdfHashMap().get(key);
                                        savePDF(key, data);
                                    }

                                } else {
                                    if (response.isMunicipalityAccessFailed()) {
                                        Util.showSnackBar(txtTitle, ctx.getString(R.string.unable_connect_muni),"OK", Color.parseColor("RED"));
                                    } else {
                                        Util.showSnackBar(txtTitle, "No statements found for the month selected","OK", Color.parseColor("YELLOW"));
                                    }
                                }
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
                            statementFragmentListener.setBusy(false);
                            enableFab();
                            snackbar = Util.showSnackBar(txtTitle, message, "OK", Color.parseColor("RED"));
                        }
                    });
                }
            }

            @Override
            public void onWebSocketClose() {

            }
        });
    }

    private void savePDF(String key, byte[] data) {
        Log.d(LOG, "savePDF: data length: " + data.length);
        File directory = Environment.getExternalStorageDirectory();
        File myDir = new File(directory, "smartCity");
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        File pdfFile = new File(myDir, key + "-statement.pdf");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pdfFile));
            bos.write(data);
            bos.flush();
            bos.close();

            Log.w(LOG, "savePDF: monthly Statement pdf file: " + pdfFile.getAbsolutePath() + " written to disk" );
            getCachedStatements();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    Snackbar snackbar;

    private void enableFab() {
        fab.setAlpha(1.0f);
        fab.setEnabled(true);
        txtDate.setAlpha(1.0f);
        txtDate.setEnabled(true);
    }

    private void disableFab() {
        fab.setAlpha(0.4f);
        fab.setEnabled(false);
        txtDate.setAlpha(0.4f);
        txtDate.setEnabled(false);
    }

    private void showDatePicker() {

        MonthPickerDialogFragment monthPicker = new MonthPickerDialogFragment();
        monthPicker.setListener(new MonthPickerDialogFragment.MonthPickerListener() {
            @Override
            public void onDatePicked(int month, int year, int count) {

                StatementListFragment.this.count = count;
                StatementListFragment.this.year = year;
                StatementListFragment.this.month = month + 1;

                getPDFStatement();
//                int index = 0;
//                boolean found = false;
//                final Pattern patt = Pattern.compile("-");
//
//                for (String s : filePathList) {
//                    if (s.contains(account.getAccountNumber())) {
//                        String[] parts = patt.split(s);
//                        int y = Integer.parseInt(parts[1]);
//                        int m = Integer.parseInt(parts[2]);
//                        if (y == year && m == month) {
//                            found = true;
//                            break;
//                        }
//
//                    }
//                    index++;
//                }
//                if (!found) {
//
//                } else {
//                    listView.setSelection(index);
//                    Util.showSnackBar(txtAccount,ctx.getString(R.string.stmt_exists), "OK", Color.parseColor("BLUE"));
//                }
            }
        });

        monthPicker.show(fragmentManager, "monthpicker");

    }


    static final Locale d = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", d);

    List<FileContainer> fileContainerList = new ArrayList<>();

    class FileContainer implements Comparable<FileContainer> {
        String fileName;
        Date date;

        public FileContainer(String filename) {
            this.fileName = filename;
            try {
                Pattern patt = Pattern.compile("-");
                String[] parts = patt.split(filename);

                DateTime dateTime = new DateTime(Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]), 1, 0, 0);
                date = dateTime.toDate();
            } catch (Exception e) {
                Log.e("FileContainer", "problem", e);
                date = new Date();
            }
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public int compareTo(FileContainer another) {
            if (this.date.before(another.date)) {
                return 1;
            }
            if (this.date.after(another.date)) {
                return -1;
            }
            return 0;
        }
    }
}
