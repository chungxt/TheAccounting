package com.example.hsin_tingchung.htc;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hsin_tingchung.nav.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BudgetView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BudgetView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //新增DB write
    private static String DATABASE_TABLE_IN = "Budget";
    private static String DATABASE_TABLE_OUT = "Budget";
    private SQLiteDatabase db;
    private DBhelper dbhelper;

    //新增DB read
    private SQLiteDatabase dbget;
    private TextView output;
    private Button bgtToNewButton;

    //New Button
    LinearLayout layout;

    //private Button btn;
    private int CurrentButtonNumber = 0;
    private String name;
    private int period, amount;

    public BudgetView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BudgetView.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetView newInstance(String param1, String param2) {
        BudgetView fragment = new BudgetView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_budget_view, container, false);
        bgtToNewButton = (Button) v.findViewById(R.id.BGT_to_New);
        bgtToNewButton.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                // TODO Auto-generated method stub

                jumpLayout_NewBudget();

            }
        });

        //produce DB writable object
        dbhelper = new DBhelper(getActivity());
        new Thread(){
            public void run(){
                db = dbhelper.getWritableDatabase();
            }
        }.start();

        //produce DB readable object
        output = (TextView) v.findViewById(R.id.title);
        DBhelper dbhelper = new DBhelper(getActivity());
        dbget = dbhelper.getReadableDatabase();

        String[] colNames=new String[]{"B_UID","name","period","amount"};
        String str = "";
        Cursor c = dbget.query(DATABASE_TABLE_OUT, colNames,null, null, null, null,null);

        //Create new button layout scrollView
        layout = (LinearLayout) v.findViewById(R.id.budget_output);

        // display data
        for (int i = 0; i < colNames.length; i++)
            str += colNames[i] + "\t\t";
        str += "\n";
        output.setText(str);
        c.moveToFirst();  // first one


        for (int i = 0; i < c.getCount(); i++) {
            str="";
            str += c.getString(c.getColumnIndex(colNames[0])) + "\t\t";
            //str += c.getString(1) + "\t\t";
            //str += c.getString(2) + "\t\t";
            //str += c.getString(3);//+ "\n";

            Button btn = new Button(getActivity());
            CurrentButtonNumber=Integer.parseInt(c.getString(c.getColumnIndex(colNames[0])));
            btn.setId(CurrentButtonNumber);
            //CurrentButtonNumber++;
            btn.setText(str);
            btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    jumpLayout_ItemBudget(v);
                }
            });
            //Toast.makeText(getActivity(),c.getString(1), Toast.LENGTH_SHORT).show();
            LinearLayout.LayoutParams param =new LinearLayout.LayoutParams(300,100);
            layout.addView(btn, param);
            c.moveToNext();  // last one
        }

        c.close();
        // Inflate the layout for this fragment
        return v;
    }

    public void jumpLayout_NewBudget(){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        BudgetNew nextFrag = new BudgetNew();
        fragmentTransaction.replace(R.id.fragment_container, nextFrag);
        //provide the fragment ID of your first fragment which you have given in
        //fragment_layout_example.xml file in place of first argument
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void jumpLayout_ItemBudget(View v){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();

        BudgetItem nextFrag = new BudgetItem();
        Bundle bundle = new Bundle();
        bundle.putString("ID", Integer.toString(v.getId()));
        nextFrag.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, nextFrag);
        //provide the fragment ID of your first fragment which you have given in
        //fragment_layout_example.xml file in place of first argument
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



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
        void onFragmentInteraction(Uri uri);
    }
}
