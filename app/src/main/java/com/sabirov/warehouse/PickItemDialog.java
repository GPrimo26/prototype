package com.sabirov.warehouse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.sabirov.warehouse.db.tables.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PickItemDialog extends DialogFragment {

    private EditText code_et;
    private Button next_btn;
    private Integer position;
    private OrderItemsViewModel orderItemsViewModel;
    private List<Items> itemsList;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_pick_item, container, false);
        orderItemsViewModel=new ViewModelProvider(requireActivity()).get(OrderItemsViewModel.class);
        itemsList=new ArrayList<>(Objects.requireNonNull(orderItemsViewModel.getItem().getValue()));
        position=orderItemsViewModel.getPosition();
        findIDs(view);
        setActions();
        return view;
    }

    private void findIDs(View view) {
        code_et=view.findViewById(R.id.code_et);
        next_btn=view.findViewById(R.id.next_btn);
    }

    private void setActions() {
        code_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")){
                    next_btn.setVisibility(View.VISIBLE);
                }else {
                    next_btn.setVisibility(View.GONE);
                }
            }
        });
        next_btn.setOnClickListener(v->{
            if (code_et.getText().toString()
                    .equals(String.valueOf(itemsList.get(position).getCode1()))){
                int q=itemsList.get(position).getQuantity();
                q--;
                itemsList.get(position).setQuantity(q);
                if (q==0){
                    Toast.makeText(requireContext(), "All items picked", Toast.LENGTH_SHORT)
                            .show();
                    orderItemsViewModel.setItem(itemsList);
                    dismiss();
                }
            }else {
                Toast.makeText(requireContext(), "No matches", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
