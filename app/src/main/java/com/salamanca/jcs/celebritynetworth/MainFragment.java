package com.salamanca.jcs.celebritynetworth;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */

public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentManager fragmentManager;
    private Fragment searchFragment;
    private Fragment resultFragment;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Main.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        savedInstanceState.get("celebrity");
       // populateFragment(getArguments().get("celebrity").toString());





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
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
