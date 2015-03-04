package com.boha.citizenapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boha.citizenapp.R;
import com.boha.citylibrary.dto.AccountDTO;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by aubreyM on 14/12/17.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountHolder> {

    public interface AccountListener {
        public void onAccountSelected(AccountDTO account);
    }

    private List<AccountDTO> accountList;
    private Context ctx;
    private AccountListener listener;

    public AccountAdapter(List<AccountDTO> list,
                          Context context, AccountListener listener) {
        this.accountList = list;
        this.ctx = context;
        this.listener = listener;
    }

    @Override
    public AccountHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        return new AccountHolder(v);
    }

    @Override
    public void onBindViewHolder(final AccountHolder holder, final int position) {

        final AccountDTO p = accountList.get(position);
        holder.txtNumber.setText("" + (position + 1));
        holder.txtAccountNumber.setText(p.getAccountNumber());
        holder.txtName.setText(p.getCustomerAccountName());
        holder.txtCurrentBal.setText(df.format(p.getCurrentBalance()));

        holder.txtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAccountSelected(p);
            }
        });
        holder.txtAccountNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAccountSelected(p);
            }
        });
        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAccountSelected(p);
            }
        });
        holder.txtCurrentBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAccountSelected(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accountList == null ? 0 : accountList.size();
    }

    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdfDate = new SimpleDateFormat("EEE dd MMM yyyy", loc);
    static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", loc);

    public class AccountHolder extends RecyclerView.ViewHolder  {
        protected TextView txtNumber, txtAccountNumber, txtName, txtCurrentBal;

        public AccountHolder(View itemView) {
            super(itemView);
            txtNumber = (TextView) itemView.findViewById(R.id.ITEM_ACCT_number);
            txtAccountNumber = (TextView) itemView.findViewById(R.id.ITEM_ACCT_accountNumber);
            txtName = (TextView) itemView.findViewById(R.id.ITEM_ACCT_accountName);
            txtCurrentBal = (TextView) itemView.findViewById(R.id.ITEM_ACCT_currBal);
        }

    }

    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");
    static final String LOG = AccountAdapter.class.getSimpleName();
}
