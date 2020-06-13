package com.example.todo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private OnItemClickListener mListener;
    public static final String SHARED_PREF = "sharedprefs";
    public static final String  CHECKBOX = "checkboxpref";

  public interface OnItemClickListener{
      void OnItemClick(int position,long id,String name);
      void  oncheck(int position,CheckBox mcheckbox);
  }

  public void SetOnItemClickListener(OnItemClickListener listener){
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

        public TaskViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mcheckbox = itemView.findViewById(R.id.Checkbox);
            dateText=itemView.findViewById(R.id.textdate);


            itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(listener != null){
                            int position = getAdapterPosition();

                            if (!mcursor.moveToPosition(position)) {
                                return;
                            }
                            String name = mcursor.getString(mcursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME));
                            long id = mcursor.getLong(mcursor.getColumnIndex(TaskContract.TaskEntry._ID));

                            if(position != RecyclerView.NO_POSITION){
                                listener.OnItemClick(position,id,name);
                            }

                        }


                }
            });
            mcheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mcheckbox.isChecked()){
                        mcheckbox.setTextColor(mcontext.getResources().getColor(R.color.colorPrimaryDark));
                } else
                        mcheckbox.setTextColor(mcontext.getResources().getColor(R.color.white));


                }

            });
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(R.layout.items_layout, parent, false);
        return new TaskViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

        if (!mcursor.moveToPosition(position)) {
            return;
        }

        String name = mcursor.getString(mcursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME));
        String date = mcursor.getString(mcursor.getColumnIndex(TaskContract.TaskEntry.DATE));
        long id = mcursor.getLong(mcursor.getColumnIndex(TaskContract.TaskEntry._ID));
        holder.mcheckbox.setText(name);
        holder.dateText.setText(date);
        holder.itemView.setTag(id);
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
