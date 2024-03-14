package com.example.reallifeproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reallifeproject.R;
import com.example.reallifeproject.model.DayModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DayRecViewAdapter extends FirestoreRecyclerAdapter<DayModel, DayRecViewAdapter.DayModelViewHolder> {
    private Context context;
    public DayRecViewAdapter(@NonNull FirestoreRecyclerOptions<DayModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DayModelViewHolder holder, int position, @NonNull DayModel model) {
        holder.dayTxt.setText(String.valueOf(model.getDay()));
        holder.eventTxt.setText(model.getEvent());
    }

    @NonNull
    @Override
    public DayModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_rec_row, parent, false);
        return new DayModelViewHolder(view);
    }

    public class DayModelViewHolder extends RecyclerView.ViewHolder{
    private TextView dayTxt, eventTxt;
    public DayModelViewHolder(@NonNull View itemView) {
        super(itemView);
        dayTxt = itemView.findViewById(R.id.dayTxt);
        eventTxt = itemView.findViewById(R.id.eventTxt);
    }
}
}
