package com.sabirov.warehouse.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sabirov.warehouse.activities.MainActivity;
import com.sabirov.warehouse.R;

import java.util.ArrayList;

public class MenuRVA extends RecyclerView.Adapter<MenuRVA.ViewHolder> {
    public MenuRVA(MainActivity mainActivity, ArrayList<String> items){
        this.mainActivity=mainActivity;
        this.items=items;
    }
    private ArrayList<String> items;
    private MainActivity mainActivity;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
     this.onItemClickListener=onItemClickListener;
    }
    @NonNull
    @Override
    public MenuRVA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mainActivity)
                .inflate(R.layout.card_menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuRVA.ViewHolder holder, int position) {
        String stringBuilder = (position + 1) + "\t" + "—" + "\t" + items.get(position);
        holder.name_tv.setText(stringBuilder);
        holder.main_ll.setOnClickListener(v->{
            if (onItemClickListener!=null)
                onItemClickListener.onItemClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name_tv;
        private LinearLayout main_ll;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_tv=itemView.findViewById(R.id.name_tv);
            main_ll=itemView.findViewById(R.id.main_ll);
        }
    }
}
