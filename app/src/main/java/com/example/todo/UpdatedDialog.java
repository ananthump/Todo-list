package com.example.todo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdatedDialog extends AppCompatDialogFragment {
    public UpdatedDialog(String taskname) {
        this.taskname = taskname;
    }
    public Button datebut;
    private String taskname;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    private String date;
    private EditText editTask;
    private UpdateDialogListner listener;
    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_layout,null);
        builder.setView(view)
                .setTitle("Todo")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String taskname = editTask.getText().toString();
                        listener.UpdateText(taskname , date);
//                editTask.getText().clear();

                    }
                });
        datebut=view.findViewById(R.id.date_but);
        editTask = view.findViewById(R.id.editTask);
        editTask.setText(taskname);
        datebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
                                calendar.set(Calendar.YEAR,datePicker.getYear());
                                calendar.set(Calendar.MONTH,datePicker.getMonth());
                                calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                                Date d = calendar.getTime();
                                date = dateFormatter.format(d);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (UpdateDialogListner) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString()+
                    "must implement DialogListner");
        }
    }

    public interface UpdateDialogListner {
        void UpdateText(String taskname, String date);

    }
}
