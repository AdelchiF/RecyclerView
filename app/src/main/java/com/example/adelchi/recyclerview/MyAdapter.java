package com.example.adelchi.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adelchi on 14/08/2015.
 * Adapter per l'elenco di email della lista
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<MainActivity.Persona> persone;

    private ViewHolder viewHolder;

    private static RecyclerViewOnClickListner mRecyclerViewClickListner;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView textView;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            this.textView = (TextView) itemView.findViewById(R.id.text);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            this.checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v instanceof CheckBox){
                mRecyclerViewClickListner.getRecyclerViewPositionListnerCheckBox(this.getAdapterPosition());
            }else {
                mRecyclerViewClickListner.getRecyclerViewPositionListner(this.getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            mRecyclerViewClickListner.getRecyclerViewPoistionLongClick(this.getAdapterPosition());
            return true;
        }
    }

    public MyAdapter(List<MainActivity.Persona> persone, RecyclerViewOnClickListner recyclerViewOnClickListner) {
        mRecyclerViewClickListner = recyclerViewOnClickListner;
        this.persone = persone;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myrow, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);

        this.viewHolder = viewHolder;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final MainActivity.Persona persona = persone.get(i);

        viewHolder.textView.setText(persona.getEmail());

        viewHolder.checkBox.setChecked(persona.getChecked());
        /*
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(viewHolder.getAdapterPosition());

            }
        });

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!persona.getChecked())
                    persona.setChecked(true);
                else
                    persona.setChecked(false);
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return persone.size();
    }

    public void delete(int position) {
        persone.remove(position);
        notifyItemRemoved(position);
    }


}
