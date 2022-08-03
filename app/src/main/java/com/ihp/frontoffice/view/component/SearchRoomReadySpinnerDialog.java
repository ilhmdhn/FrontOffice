package com.ihp.frontoffice.view.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.ihp.frontoffice.R;
import com.ihp.frontoffice.data.entity.Room;


public class SearchRoomReadySpinnerDialog {
    private ArrayList<Room> items;
    private Activity context;
    private String dTitle;
    private OnSpinerItemClick onSpinerItemClick;
    private AlertDialog alertDialog;
    private SearchRoomReadyAdapter adapter;
    private ArrayList<Room> tmpList = new ArrayList<>();

    public SearchRoomReadySpinnerDialog(Activity activity, ArrayList<Room> items, String dialogTitle) {
        this.items = items;
        this.context = activity;
        this.dTitle = dialogTitle;
    }



    public void bindOnSpinerListener(OnSpinerItemClick onSpinerItemClick1) {
        this.onSpinerItemClick = onSpinerItemClick1;
    }

    public void showSpinerDialog() {
        tmpList.clear();
        tmpList.addAll(items);
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        @SuppressLint("InflateParams")
        View v = context.getLayoutInflater().inflate(R.layout.dialog_layout, null);
        TextView rippleViewClose = v.findViewById(R.id.close);
        TextView title = v.findViewById(R.id.spinerTitle);
        rippleViewClose.setText("close");
        title.setText(dTitle);
        final ListView listView = v.findViewById(R.id.list);
        final EditText searchBox = v.findViewById(R.id.searchBox);
        adapter = new SearchRoomReadyAdapter(context, tmpList, R.layout.custom_sp);
        listView.setAdapter(adapter);
        adb.setView(v);
        alertDialog = adb.create();
        alertDialog.setCancelable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onSpinerItemClick.onClick(tmpList.get(i));
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchBox.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                alertDialog.dismiss();
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        rippleViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private synchronized void filter(String text) {
        if (text.isEmpty()) {
            tmpList.clear();
            tmpList.addAll(items);
            adapter.notifyDataSetChanged();
            return;
        }
        ArrayList<Room> temp = new ArrayList<>();
        for (Room d : items) {
            if (d.getRoomType().toLowerCase().contains(text.toLowerCase())||
                    d.getRoomCode().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        tmpList.clear();
        tmpList.addAll(temp);
        adapter.notifyDataSetChanged();
    }

    public interface OnSpinerItemClick {
        void onClick(Room listModel);
    }

}
