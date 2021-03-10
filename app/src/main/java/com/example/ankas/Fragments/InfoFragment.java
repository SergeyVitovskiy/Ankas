package com.example.ankas.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ankas.R;

public class InfoFragment extends Fragment {
    Context context;
    View InfoFragmentView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        InfoFragmentView = inflater.inflate(R.layout.info_fragment, null);
        context = InfoFragmentView.getContext();
        return InfoFragmentView;
    }
}
