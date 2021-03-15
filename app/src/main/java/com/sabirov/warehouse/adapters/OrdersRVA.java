package com.sabirov.warehouse.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sabirov.warehouse.R;
import com.sabirov.warehouse.activities.OrdersActivity;
import com.sabirov.warehouse.db.relations.OrderWithItems;
import com.sabirov.warehouse.db.tables.Orders;

import java.util.List;

public class OrdersRVA extends RecyclerView.Adapter<OrdersRVA.ViewHolder> {
    public OrdersRVA(OrdersActivity activity, List<Orders> ordersList) {
        this.activity=activity;
        this.ordersList=ordersList;
    }

    public void setOrdersList(List<Orders> ordersList) {
        this.ordersList=ordersList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onOrderClick(Orders order);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private static OnItemClickListener mListener;
    private OrdersActivity activity;
    private List<Orders> ordersList;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity)
                .inflate(R.layout.card_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text=(position+1) + "\t" + "—" + "\t"
                + ordersList.get(position).getCode1();
        holder.code_tv.setText(text);
        holder.name_tv.setText(ordersList.get(position).getName());
        holder.main_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null){
                    mListener.onOrderClick(ordersList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView code_tv, name_tv;
        private LinearLayout main_ll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            code_tv=itemView.findViewById(R.id.code_tv);
            name_tv =itemView.findViewById(R.id.name_tv);
            main_ll=itemView.findViewById(R.id.main_ll);
        }
    }
}
