package com.example.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class UpdatedDialog extends AppCompatDialogFragment {
    public UpdatedDialog(String taskname) {
        this.taskname = taskname;
    }

    private String taskname;
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
                        listener.UpdateText(taskname);
//                editTask.getText().clear();

                    }
                });
        editTask = view.findViewById(R.id.editTask);
        editTask.setText(taskname);
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
        void UpdateText(String taskname);

    }
}
