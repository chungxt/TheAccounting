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
import android.widget.Switch;
import android.widget.TextView;

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
        /*

        for (int i = 0; i < c.getCount(); i++) {
            str="";
            str += c.getString(c.getColumnIndex(colNames[0])) + "\t\t";
            str += c.getString(1) + "\t\t";
            str += c.getString(2) + "\t\t";
            str += c.getString(3);//+ "\n";

            Button btn = new Button(this);
            btn.setId(CurrentButtonNumber);
            CurrentButtonNumber++;
            btn.setText(str);
            btn.setOnClickListener(this);
            LinearLayout.LayoutParams param =new LinearLayout.LayoutParams(1000,150);
            layout.addView(btn, param);

            c.moveToNext();  // last one
        }

        */
        // Inflate the layout for this fragment
        return v;
    }

    public void jumpLayout_NewBudget(){
        /*
        setContentView(R.layout.activity_budget_new_item);

        Button button02= (Button)findViewById(R.id.NewBugdetSave);

        //OnClickListener
        button02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Switch pd= (Switch)findViewById(R.id.NewBudgetPeriod);
                //period =Integer.parseInt(pd.getText().toString());

                EditText nm= (EditText)findViewById(R.id.NewBudgetName);
                name =nm.getText().toString();
                EditText am= (EditText)findViewById(R.id.NewBudgetAmount);
                amount =Integer.parseInt(am.getText().toString());

                ContentValues cv1 = new ContentValues();
                cv1.put("name",name);
                cv1.put("period",1);
                cv1.put("amount",amount);
                db.insert(DATABASE_TABLE_IN, null, cv1);



                Intent intent = new Intent();
                intent.setClass(BudgetView.this, BudgetView.class);
                startActivity(intent);
                BudgetView.this.finish();
            }
        });
        */
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
