package com.salamanca.jcs.celebritynetworth;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;



public class MainFragment extends Fragment {






    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void populateFragment(String celebString){
       // View view = fragmentManager.findFragmentById(R.id.mainFragment).getView();
        View view = getView();

        Celebrity celebrity = new Gson().fromJson(celebString,Celebrity.class);
        ImageView celebImage = (ImageView) view.findViewById(R.id.celebImageView);
        Picasso.with(view.getContext()).load(celebrity.getImageUrl()).into(celebImage);

        TextView netWorthTextView = (TextView)
                view.findViewById(R.id.textViewNetWorth);
        TextView salaryLabel = (TextView) view.findViewById(R.id.salaryTextView);
        TextView salaryLabelValue = (TextView) view.findViewById(R.id.textViewSalaryValue);
        TextView dateOfBirthLabel = (TextView) view.findViewById(R.id.dateOfBirthValueLabel);
        TextView professionLabel = (TextView) view.findViewById(R.id.professionValueTextView);
        TextView categoryLabel = (TextView) view.findViewById(R.id.catergoryValueLabel);
        TextView placeOfBirthLabel = (TextView) view.findViewById(R.id.placeOfBirthValueLabel);
        TextView bioTextView = (TextView) view.findViewById(R.id.bioTextView);

        salaryLabel.setText(celebrity.getName()+"'s ANNUAL SALARY");
        salaryLabelValue.setText(celebrity.getAnnualSalaryString());
        dateOfBirthLabel.setText(celebrity.getDateOfBirth());
        professionLabel.setText(celebrity.getProfession());
        categoryLabel.setText(celebrity.getCategory());
        netWorthTextView.setText(celebrity.getNetWorthString());
        placeOfBirthLabel.setText(celebrity.getPlaceOfBirth());
        bioTextView.setText("\t"  + celebrity.getInfo());
        netWorthTextView.setText(celebrity.getNetWorthString());

    }

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }





}
