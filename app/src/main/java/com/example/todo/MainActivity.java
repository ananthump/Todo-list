package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements Dialog.DialogListner, UpdatedDialog.UpdateDialogListner {
    private SQLiteDatabase mDatabase;
    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private String Taskname;
    long id;
    int pos;
    FloatingActionButton fl ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskDBHelper dbHelper = new TaskDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        buildRecyclerView();


        fl=findViewById(R.id.floatingActionButton);
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDialog();
            }

            private void OpenDialog() {
                Dialog dialog = new Dialog();
                dialog.show(getSupportFragmentManager(),"Dialog ");
            }
        });


    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TaskAdapter(this,getAllItems());
        mRecyclerView.setAdapter(mAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(mRecyclerView);

        mAdapter.SetOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position,long Id,String taskname) {

                id = Id;
                pos = position;
                Taskname = taskname;

                     OpenDialog();

            }

            @Override
            public void oncheck(int position, CheckBox mcheckbox) {
                System.out.println(position);



               }
          });

    }

    @Override
    public void applyText(String taskname) {

        if(taskname.trim().length()==0){
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(TaskContract.TaskEntry.COLUMN_NAME, taskname);
        System.out.println(taskname);
        mDatabase.insert(TaskContract.TaskEntry.TABLE_NAME,null,cv);
        mAdapter.swapCursorinsert(getAllItems());

    }

    private void removeItem(long id){
         mDatabase.delete(TaskContract.TaskEntry.TABLE_NAME,
                 TaskContract.TaskEntry._ID + "=" + id,null);
         mAdapter.swapCursor(getAllItems());
    }



    private Cursor getAllItems(){
        return mDatabase.query(
                TaskContract.TaskEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TaskContract.TaskEntry.COLUMN_TIMESTAMP +" DESC"
        );
    }

    @Override
    public void UpdateText(String taskname) {

        ContentValues cv = new ContentValues();
        cv.put(TaskContract.TaskEntry.COLUMN_NAME, taskname);

        mDatabase.update(TaskContract.TaskEntry.TABLE_NAME,cv,TaskContract.TaskEntry._ID + "=" + id,null);
        mAdapter.swapCursor(getAllItems());
//        System.out.println(pos);
//        System.out.println(id);
//        System.out.println(taskname);
    }

    private void OpenDialog() {

        UpdatedDialog updatedialog = new UpdatedDialog(Taskname);
        updatedialog.show(getSupportFragmentManager(),"Dialog2");
    }


}
