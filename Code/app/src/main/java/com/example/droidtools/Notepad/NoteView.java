package com.example.droidtools.Notepad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.droidtools.DatasOfUsersLogedIn.DataHolder;
import com.example.droidtools.ImageToTextTools.ImageToText;
import com.example.droidtools.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteView extends RecyclerView.Adapter<NoteView.NoteHolder> {

    private ArrayList<Note> list;
    private Context context;
    public NoteView( Context context) {
        this.list = new ArrayList<>();
        this.context = context;
    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteHolder(LayoutInflater.from(context).inflate(R.layout.page, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
            holder.title.setText(list.get(position).getTitle());
            holder.desc.setText(list.get(position).getDescription());
            holder.date.setText(list.get(position).getDate());
            holder.time.setText(list.get(position).getTime());
    }
    public void setData(ArrayList<Note> noteList){
        this.list=noteList;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        public TextView title,desc,date,time;


        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            desc = itemView.findViewById(R.id.note_desc);
            date = itemView.findViewById(R.id.note_date);
            time = itemView.findViewById(R.id.note_time);

            ImageButton menu_mode = itemView.findViewById(R.id.menu_mode);

            menu_mode.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.inflate(R.menu.menu_for_card);



                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_delete:

                                    String key = DataHolder.uidHolder;
                                    System.out.println(key+date.getText().toString()+time.getText().toString());

                                    String timestr = time.getText().toString();
                                    timestr = timestr.replace(":","");
                                    timestr=timestr.replace(" ","");

                                    String datestr = date.getText().toString();
                                    datestr = datestr.replace("-","");

                                    FirebaseDatabase.getInstance().getReference().child("Users").
                                            child(key).child("notes")
                                            .child(key+datestr+timestr).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    return true;
                                case R.id.menu_read:
                                    if(desc.getVisibility() == View.GONE){
                                        desc.setVisibility(View.VISIBLE);
                                    }else{
                                    desc.setVisibility(View.GONE);
                }
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    popupMenu.show();
                }
            });




        }

    }

}
