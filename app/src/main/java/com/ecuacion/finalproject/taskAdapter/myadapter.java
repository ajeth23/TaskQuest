package com.ecuacion.finalproject.taskAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecuacion.finalproject.ui.auth.ActivityLogin;
import com.ecuacion.finalproject.R;
import com.ecuacion.finalproject.databinding.ActivityAddDataBinding;
import com.ecuacion.finalproject.databinding.ActivityMainScreenBinding;
import com.ecuacion.finalproject.model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class myadapter extends FirebaseRecyclerAdapter<model, myadapter.myviewholder> {

    private static final String titleExtra = "titleExtra";
    private static final String channelID = "channel1";
    private static final String messageExtra = "messageExtra";
    private Context context;
    private static final int notificationID = 1;
    private int notificationCounter = 1;

    private ActivityLogin binding_main_screen;

    private ActivityAddDataBinding binding;


    public myadapter(@NonNull FirebaseRecyclerOptions<model> options, Context context, ActivityMainScreenBinding binding_main_screen) {
        super(options);
        this.context = context;
     //   this.binding_main_screen = binding_main_screen;
    }


    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull final model model) {
        holder.title_task.setText(model.getTitle());
        holder.message_task.setText(model.getMessage());

        //update Task
        holder.btnEdit.setOnClickListener(view -> {
            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.title_task.getContext())
                    .setContentHolder(new ViewHolder(R.layout.task_update))
                    .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT) // Set width to match the screen width
                    .setContentHeight(ViewGroup.LayoutParams.MATCH_PARENT) // Set height to match the screen height
                    .setGravity(Gravity.CENTER)
                    .create();


            View myview = dialogPlus.getHolderView();
            final EditText titleEditText = myview.findViewById(R.id.update_Title);
            final EditText messageEditText = myview.findViewById(R.id.update_Message);
            final TimePicker updateTime = myview.findViewById(R.id.updateTimePicker);
            final DatePicker updateDate = myview.findViewById(R.id.updateDatePicker);
            Button update_task = myview.findViewById(R.id.btnUpdate);


            //cancel Update
            Button btnCancelUpdate = myview.findViewById(R.id.btn_cancel_update);
            btnCancelUpdate.setOnClickListener(v -> {
                dialogPlus.dismiss();
            });


            titleEditText.setText(model.getTitle());
            messageEditText.setText(model.getMessage());

            dialogPlus.show();


            //Update task
            update_task.setOnClickListener(view1 -> {

//                String updatedTitle = titleEditText.getText().toString();
//                String updatedMessage = messageEditText.getText().toString();

                //update the Title and Message
                Map<String, Object> map = new HashMap<>();
                map.put("title", titleEditText.getText().toString());
                map.put("message", titleEditText.getText().toString());

                //update the Date and Time
                map.put("hour", updateTime.getHour());
                map.put("minute", updateTime.getMinute());
                map.put("day", updateDate.getDayOfMonth());
                map.put("month", updateDate.getMonth());
                map.put("year", updateDate.getYear());

                //Update the task to the FIREBASE REALTIME
                FirebaseDatabase.getInstance().getReference().child("Task")
                        .child(Objects.requireNonNull(getRef(position).getKey())).updateChildren(map)
                        .addOnSuccessListener(aVoid -> {
                            dialogPlus.dismiss();

                        })
                        .addOnFailureListener(e -> dialogPlus.dismiss());
            });
        });


        holder.btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.btnDelete.getContext());
            builder.setTitle("Delete Panel");
            builder.setMessage("Are you sure you want to delete?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> FirebaseDatabase.getInstance()
                    .getReference().child("Task")
                    .child(Objects.requireNonNull(getRef(position).getKey())).removeValue());
            builder.setNegativeButton("No", (dialogInterface, i) -> {
            });
            builder.show();
        });
        holder.linear1.setOnClickListener(view -> holder.toggleExpansion());
    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder {
        Button btnDelete, btnEdit, btn_cancel_update;
        TextView title_task, message_task;
        LinearLayout linear1, linear2;
        boolean isExpanded = false;
        TimePicker updateTime;
        DatePicker updateDate;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            title_task = itemView.findViewById(R.id.title_task);
            message_task = itemView.findViewById(R.id.message_task);
            updateTime = itemView.findViewById(R.id.updateTimePicker);
            updateDate = itemView.findViewById(R.id.updateDatePicker);

            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            linear1 = itemView.findViewById(R.id.linear1);
            linear2 = itemView.findViewById(R.id.linear2);
        }

        private void toggleExpansion() {
            isExpanded = !isExpanded;
            TransitionManager.beginDelayedTransition((ViewGroup) itemView, new AutoTransition());
            linear2.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }


    }
}
