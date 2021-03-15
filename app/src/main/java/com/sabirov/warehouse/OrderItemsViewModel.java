package com.sabirov.warehouse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sabirov.warehouse.db.tables.Items;

import java.util.List;


public class OrderItemsViewModel extends ViewModel {
    private MutableLiveData<List<Items>> itemsMutableLiveData;
    private Integer position;

    public OrderItemsViewModel() {
        itemsMutableLiveData = new MutableLiveData<>();
    }

    public void setItem(List<Items> items){
        itemsMutableLiveData.setValue(items);
    }

    public LiveData<List<Items>> getItem(){
        return itemsMutableLiveData;
    }

    public void setPostion(Integer pos){
        position=pos;
    }

    public Integer getPosition(){
        return position;
    }
}
