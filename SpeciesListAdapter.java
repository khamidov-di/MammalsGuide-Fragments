package com.ecosystem.mammalsdemo.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecosystem.mammalsdemo.R;

import java.io.IOException;
import java.io.InputStream;

/*
    This adapter is used to manage list of species.
    It extends RecyclerView Adapter.
 */

public class SpeciesListAdapter extends RecyclerView.Adapter<SpeciesListAdapter.SpeciesViewHolder> {

    // TODO replace this array of species with list from database with sort options
    private static String[] speciesList; // list of available species
    private int numberOfSpecies; // number of species
    private Context context; // outer context
    private static MediaPlayer mediaPlayer = new MediaPlayer(); // media player to play animal sounds
    private static SpeciesListFragment.SpeciesListFragmentListener listener; // listener used to manage clicks in RecyclerView

    // Set listener of SpeciesListFragment.SpeciesListFragmentListener to adapter
    public void setListener(SpeciesListFragment.SpeciesListFragmentListener listener) {
        this.listener = listener;
    }

    // Adapter constructor
    public SpeciesListAdapter (Context context) {
        speciesList = new SpeciesList().species;
        numberOfSpecies = speciesList.length;
        this.context = context;
    }

    // Create View Holder for RecyclerView
    @NonNull
    @Override
    public SpeciesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.species_list_item;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutId, parent, false);

        return new SpeciesViewHolder(view);
    }

    // Bind View Holder with appropriate values
    @Override
    public void onBindViewHolder(@NonNull SpeciesViewHolder holder, int position) {

        try {
            holder.bind(context, position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get item count
    @Override
    public int getItemCount() {
        return numberOfSpecies;
    }

    // Main body of RecyclerView. Represents ViewHolder in which all information is contained
    static class SpeciesViewHolder extends RecyclerView.ViewHolder {

        // Declaration of views
        ImageView speciesSmallImage;
        TextView speciesName;
        TextView speciesSecondName;
        TextView speciesLatinName;
        ImageButton imageButton;

        // ViewHolder constructor
        public SpeciesViewHolder(@NonNull View itemView) {
            super(itemView);

            speciesSmallImage = itemView.findViewById(R.id.iv_species_list_image);
            speciesName = itemView.findViewById(R.id.tv_species_list_name);
            speciesSecondName = itemView.findViewById(R.id.tv_species_list_second_name);
            speciesLatinName = itemView.findViewById(R.id.tv_species_list_latin_name);
            imageButton = itemView.findViewById(R.id.ib_audio);
        }

        // bind() is used to attach values to ViewHolder
        void bind(final Context context, final int position) throws IOException {

            // Get latin name for further work
            final String latin = speciesList[position];
            // TODO consider rename images to latin.png
            String miniImageName = latin + "_min_img.png";
            InputStream image = context.getApplicationContext().getAssets().open(miniImageName); // open image

            // Set name to TextView
            int nameId = context.getResources().getIdentifier(latin, "string", context.getPackageName());
            String name = context.getString(nameId);
            speciesName.setText(name);
            // Set second name to TextView
            int secondNameId = context.getResources().getIdentifier(latin + "_second_name", "string", context.getPackageName());
            String secondName = context.getString(secondNameId);
            speciesSecondName.setText(secondName);
            // Set latin name to TextView
            int latinNameId = context.getResources().getIdentifier(latin + "_latin", "string", context.getPackageName());
            String latinName = context.getString(latinNameId);
            speciesLatinName.setText(latinName);
            // Set image to ImageView
            Drawable d = Drawable.createFromStream(image, null);
            speciesSmallImage.setImageDrawable(d);

            // OnClickListener to view detailed info about animal
            View.OnClickListener onSpeciesSelected = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        listener.onItemClick(latin);
                }
            };
            speciesSmallImage.setOnClickListener(onSpeciesSelected);

            // Set id for audio view
            final int audioId = context.getResources().getIdentifier(latin+"_audio", "raw", context.getPackageName());
            // Draw audio icon and use it only if audio exists
            if (audioId != 0) {

                Drawable soundIcon = Drawable.createFromStream(context.getResources().getAssets().open("sound.png"), null);
                imageButton.setImageDrawable(soundIcon);

                imageButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        try {
                            mediaPlayer.stop();
                            mediaPlayer = MediaPlayer.create(context, audioId);
                            mediaPlayer.start();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}
