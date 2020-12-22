package com.seanyj.mysamples.app.ui.dialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.seanyj.mysamples.R;

public class DialogTestActivity extends AppCompatActivity implements View.OnClickListener{
    private int mStylePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_test);

        Spinner spinner = (Spinner) findViewById(R.id.styleSpinner);
        findViewById(R.id.alertDlgBtn).setOnClickListener(this);
        findViewById(R.id.classDialogBtn).setOnClickListener(this);
        findViewById(R.id.dialogFragmentBtn).setOnClickListener(this);
        findViewById(R.id.activityDlgBtn).setOnClickListener(this);
        findViewById(R.id.progressDlgBtn).setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dialog_style,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("hello", "onItemSelected: " + position);
                mStylePos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alertDlgBtn:
                showAlertDlg();
                break;
            case R.id.classDialogBtn:
                break;
            case R.id.dialogFragmentBtn:
                break;
            case R.id.activityDlgBtn:
                break;
            case R.id.progressDlgBtn:
                showProgressDlg();
                break;
        }
    }

    private void showProgressDlg() {
        ProgressDialog dlg = new ProgressDialog(this);
        dlg.setTitle("Wait");
        dlg.setMessage("loading...");
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();
    }

    private void showAlertDlg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Question")
                .setMessage("you sure to quit")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DialogTestActivity.this, "you click yes", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DialogTestActivity.this, "you click no", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog dlg = builder.create();
        dlg.show();
    }
}
