package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Defects_Adapter extends RecyclerView.Adapter<Defects_Adapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> id2, type, place;  // ← Изменили name → type, place

    private OnDefectClickListener listener;
    public interface OnDefectClickListener{
        void onDefectClick(int position);
    }

    Defects_Adapter(Context context, ArrayList id, ArrayList defectType, ArrayList place){
        this.context = context;
        this.id2 = id;
        this.type = type;
        this.place = place;
    }

    @NonNull
    @Override
    public Defects_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.defects_list, parent, false);  // ← Тот же layout!

        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Defects_Adapter.MyViewHolder holder, int position) {
        holder.id.setText(String.valueOf(id2.get(position)));
        holder.type.setText(String.valueOf(type.get(position)));  // ← name → type
        // Если в layout только 2 TextView, place можно добавить или убрать
    }

    @Override
    public int getItemCount() {
        return id2.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, type;  // ← name → type

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.text_id);
            type = itemView.findViewById(R.id.text_name);  // ← Тот же ID!

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onDefectClick(position);
                    }
                }
            });
        }
    }

    public void setOnDefectClickListener(OnDefectClickListener listener) {
        this.listener = listener;
    }
}

