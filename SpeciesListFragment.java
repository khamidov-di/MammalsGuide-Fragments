package com.ecosystem.mammalsdemo.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecosystem.mammalsdemo.R;

public class SpeciesListFragment extends Fragment {

    private SpeciesListFragmentListener listener;
    RecyclerView speciesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_species_list, container, false);

        speciesList = root.findViewById(R.id.rv_species_list);

        LinearLayoutManager speciesLayoutManager = new LinearLayoutManager(root.getContext());
        speciesList.setLayoutManager(speciesLayoutManager);
        speciesList.setHasFixedSize(true);

        SpeciesListAdapter speciesListAdapter = new SpeciesListAdapter(root.getContext());
        if (speciesListAdapter != null) speciesListAdapter.setListener(listener);
        speciesList.setAdapter(speciesListAdapter);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof SpeciesListFragmentListener) {
            listener = (SpeciesListFragmentListener) getParentFragment();
        }
    }

    public interface SpeciesListFragmentListener {
        void onItemClick(String latin);
    }
}
