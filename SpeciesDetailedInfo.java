package com.ecosystem.mammalsdemo.ui.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ecosystem.mammalsdemo.MainActivity;
import com.ecosystem.mammalsdemo.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpeciesDetailedInfo extends Fragment {

    private static final String LATIN_NAME = "latin";
    private String latin;
    private TextView orderTextView;
    private TextView familyTextView;
    private TextView genusTextView;
    private TextView nameTextView;
    private static ImageView photoImageView;
    private static ImageView drawImageView;
    private static ExpandableListView detailsExpandableListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            latin = getArguments().getString(LATIN_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.species_detailed_info, container, false);

        Context context = getContext();

        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.species_detailed_info_header, detailsExpandableListView, false);

        orderTextView = header.findViewById(R.id.tv_species_order);
        familyTextView = header.findViewById(R.id.tv_species_family);
        genusTextView = header.findViewById(R.id.tv_species_genus);
        nameTextView = header.findViewById(R.id.tv_species_name);
        photoImageView = header.findViewById(R.id.iv_photo_image);
        drawImageView = header.findViewById(R.id.iv_draw_image);
        detailsExpandableListView = root.findViewById(R.id.elv_details);

        int nameId = context.getResources().getIdentifier(latin, "string", context.getPackageName());
        int latinId = context.getResources().getIdentifier(latin + "_latin", "string", context.getPackageName());
        String namePlusLatin = context.getString(nameId) + " - " + context.getString(latinId);
        if (nameTextView == null) {
            Log.d("err", "object null");
        }
        nameTextView.setText(namePlusLatin);

        int orderId = context.getResources().getIdentifier(latin + "_order", "string", context.getPackageName());
        orderTextView.setText(context.getString(orderId));

        int familyId = context.getResources().getIdentifier(latin + "_family", "string", context.getPackageName());
        familyTextView.setText(context.getString(familyId));

        int genusId = context.getResources().getIdentifier(latin + "_genus", "string", context.getPackageName());
        genusTextView.setText(context.getString(genusId));

        String photoImageName = latin + "_photo.jpg";
        try {
            InputStream photoImageStream = context.getAssets().open(photoImageName);
            Drawable photo = Drawable.createFromStream(photoImageStream, null);
            photoImageView.setImageDrawable(photo);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String drawImageName = latin + "_draw.jpg";
        try {
            InputStream drawImageStream = context.getAssets().open(drawImageName);
            Drawable draw = Drawable.createFromStream(drawImageStream, null);
            drawImageView.setImageDrawable(draw);
        } catch (IOException e) {
            e.printStackTrace();
        }

        detailsExpandableListView.addHeaderView(header);

        String[] detailCodeNames = new String[] {"appearance", "areal", "biology", "footprints", "voice", "food", "reproduction"};
        String[] groupNames = new String[detailCodeNames.length];
        for (int i = 0; i < detailCodeNames.length; i++) {
            groupNames[i] = latin + "_" + detailCodeNames[i];
        }

        ArrayList<Map<String, String>> groupData = new ArrayList<>();

        for (int i = 0; i < detailCodeNames.length; i++) {
            String s = detailCodeNames[i];
            Map<String, String> map = new HashMap<>();
            map.put("groupName", context.getString(getResources().getIdentifier(s, "string", getActivity().getBaseContext().getPackageName())));
            groupData.add(map);
        }
        String[] groupFrom = new String[] {"groupName"};
        int[] groupTo = new int[] {R.id.tv_expandable_group};

        ArrayList<ArrayList<Map<String, String>>> childDataList = new ArrayList<>();

        for (int i = 0; i < groupNames.length; i++) {
            String s = groupNames[i];
            ArrayList<Map<String, String>> childListItem = new ArrayList<>();
            Map<String, String> map = new HashMap<>();
            map.put("groupDetails", context.getString(getResources().getIdentifier(s, "string", getActivity().getBaseContext().getPackageName())));
            childListItem.add(map);
            childDataList.add(childListItem);
        }
        String[] childGroupFrom = new String[] {"groupDetails"};
        int[] childGroupTo = new int[] {R.id.tv_expandable_item};

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                getActivity().getBaseContext(), groupData, R.layout.expandable_group, groupFrom, groupTo,
                childDataList, R.layout.expandable_item, childGroupFrom, childGroupTo
        );

        detailsExpandableListView.setAdapter(adapter);

        return root;
    }
}