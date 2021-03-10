package com.example.ankas.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ankas.Objects.Characteristic;
import com.example.ankas.R;

import java.util.List;

public class CharacteristicAdapter extends BaseAdapter {
    List<Characteristic> mCharacteristicList;
    Context mContext;

    public CharacteristicAdapter(List<Characteristic> mCharacteristicList, Context mContext) {
        this.mCharacteristicList = mCharacteristicList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mCharacteristicList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCharacteristicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View viewItem = View.inflate(mContext, R.layout.item_characteristic, null);
        TextView txt_name = viewItem.findViewById(R.id.txt_name);
        TextView txt_characteristic = viewItem.findViewById(R.id.txt_characteristic);
        Characteristic characteristic = mCharacteristicList.get(position);
        txt_name.setText(characteristic.getNameCharacteristic());
        txt_characteristic.setText(characteristic.getCharacteristic());
        return viewItem;
    }
}
