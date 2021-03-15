package com.sabirov.warehouse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sabirov.warehouse.R;
import com.sabirov.warehouse.activities.OrderItemsActivity;
import com.sabirov.warehouse.db.tables.Bins;
import com.sabirov.warehouse.db.tables.Items;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import warehouse.Bin;

public class ItemsRVA extends RecyclerView.Adapter<ItemsRVA.ViewHolder> {
    public ItemsRVA(OrderItemsActivity activity, List<Items> itemsList, List<Bins> binsList) {
        this.activity=activity;
        this.itemsList=itemsList;
        this.binsList=binsList;
    }


    public interface OnItemClickListener {
        void onItemClick(Integer position, Items item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private static OnItemClickListener mListener;
    private OrderItemsActivity activity;
    private List<Items> itemsList;
    private List<Bins> binsList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(activity).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text=(position+1) + "\t" + "—" + "\t"
                + itemsList.get(position).getName();
        holder.name_tv.setText(text);
        if (binsList.size()!=0) {
            if (!binsList.get(position).getName().equals("Not in the bin")) {
                text = "Placed in " + binsList.get(position).getName();
                holder.bin_tv.setText(text);
            } else {
                holder.bin_tv.setText(binsList.get(position).getName());
            }
        }else {
            text="No information";
            holder.bin_tv.setText(text);
        }
        text="Required: "+itemsList.get(position).getQuantity() +" ";
        holder.quantity_tv.setText(text);
        holder.actions_btn.setOnClickListener(view -> {
            PopupMenu popupMenu=new PopupMenu(activity, view);
            popupMenu.inflate(R.menu.popup);
            try {
                Field[] fields = popupMenu.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popupMenu);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.manually:
                        if (mListener!=null){
                            mListener.onItemClick(position, itemsList.get(position));
                        }
                        break;
                    case R.id.scan:
                        Toast.makeText(activity, "Soon...", Toast.LENGTH_SHORT).show();
                            break;
                        }
                return false;
            });
            popupMenu.show();
        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void setBinsList(List<Bins> binsList){
        this.binsList=binsList;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name_tv, bin_tv, quantity_tv;
        private Button actions_btn;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_tv=itemView.findViewById(R.id.name_tv);
            bin_tv=itemView.findViewById(R.id.bin_tv);
            quantity_tv=itemView.findViewById(R.id.quantity_tv);
            actions_btn=itemView.findViewById(R.id.action_btn);
        }
    }
}
