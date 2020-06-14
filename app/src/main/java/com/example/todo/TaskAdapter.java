package com.example.todo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.sql.Types.NULL;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private OnItemClickListener mListener;
    public Date dt;
    Date d1;
    Date d2;
    public static final String SHARED_PREF = "sharedprefs";
    public static final String CHECKBOX = "checkboxpref";

    public interface OnItemClickListener {
        void OnItemClick(int position, long id, String name);

        void oncheck(int position, CheckBox mcheckbox, long id);
    }

    public void SetOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private Context mcontext;
    private Cursor mcursor;

    public TaskAdapter(Context context, Cursor cursor) {
        mcontext = context;
        mcursor = cursor;
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder {
        public CheckBox mcheckbox;
        public TextView dateText;
        public TextView expText;



        public TaskViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mcheckbox = itemView.findViewById(R.id.Checkbox);
            dateText = itemView.findViewById(R.id.textdate);
            expText=itemView.findViewById(R.id.expiretext);
            Date dtf;


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        int position = getAdapterPosition();

                        if (!mcursor.moveToPosition(position)) {
                            return;
                        }
                        String name = mcursor.getString(mcursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME));
                        long id = mcursor.getLong(mcursor.getColumnIndex(TaskContract.TaskEntry._ID));

                        if (position != RecyclerView.NO_POSITION) {
                            listener.OnItemClick(position, id, name);
                        }

                    }


                }
            });
            mcheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (!mcursor.moveToPosition(position)) {
                        return;
                    }
                    long id = mcursor.getLong(mcursor.getColumnIndex(TaskContract.TaskEntry._ID));
                    listener.oncheck(position,mcheckbox,id);

//                    if (mcheckbox.isChecked()) {
//                        mcheckbox.setTextColor(mcontext.getResources().getColor(R.color.colorPrimaryDark));
//                    } else
//                        mcheckbox.setTextColor(mcontext.getResources().getColor(R.color.white));


                }

            });
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.items_layout, parent, false);
        return new TaskViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {




        if (!mcursor.moveToPosition(position)) {
            return;
        }
        int state = mcursor.getInt(mcursor.getColumnIndex(TaskContract.TaskEntry.STATE));



        String name = mcursor.getString(mcursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME));
        String date = mcursor.getString(mcursor.getColumnIndex(TaskContract.TaskEntry.DATE));
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");


        Calendar cal = Calendar.getInstance();
        Date dt2=cal.getTime();
        String datecur=dateFormatter.format(dt2);

        try {
            d2=dateFormatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            d1=dateFormatter.parse(datecur);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(d1.equals(d2)){
            holder.expText.setText("Due Today");
            holder. expText.setTextColor(mcontext.getResources().getColor(R.color.orange));
        }
        else if(d1.before(d2)){
            holder.expText.setText("");
        }
        else if(d1.after(d2)){
            holder.expText.setText("Date Expired");
            holder. expText.setTextColor(mcontext.getResources().getColor(R.color.red));
        }





        long id = mcursor.getLong(mcursor.getColumnIndex(TaskContract.TaskEntry._ID));
        System.out.println("state"+state+"id"+id);
        holder.mcheckbox.setText(name);
        holder.dateText.setText("Due date : "+date);
        holder.itemView.setTag(id);

        if(state==1)
        {System.out.println("state of"+state);
            holder.mcheckbox.setChecked(true);
            holder. mcheckbox.setTextColor(mcontext.getResources().getColor(R.color.colorPrimaryDark));
            holder. dateText.setTextColor(mcontext.getResources().getColor(R.color.green));
            holder.dateText.setText("Task Completed");
            holder.expText.setText("");
        }
        else
        {System.out.println("state of"+state);
            holder.mcheckbox.setChecked(false);
            holder.mcheckbox.setTextColor(mcontext.getResources().getColor(R.color.white));
            holder.dateText.setTextColor(mcontext.getResources().getColor(R.color.white));

        }
    }

    @Override
    public int getItemCount() {
        return mcursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mcursor != null) {
            mcursor.close();
        }
        mcursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }

    }

    public void swapCursorinsert(Cursor newCursor) {
        if (mcursor != null) {
            mcursor.close();
        }
        mcursor = newCursor;
        if (newCursor != null) {
            int pos = mcursor.getPosition();
            notifyItemInserted(pos);
        }

    }
}
