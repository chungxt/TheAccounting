package com.example.hsin_tingchung.htc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hsin_tingchung.nav.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //new DB write
    private static String DATABASE_TABLE_IN = "Now";
    private static String DATABASE_TABLE_OUT = "Spent";
    private SQLiteDatabase db;
    private DBhelper dbhelper;

    //new DB read
    private SQLiteDatabase dbget;
    private TextView output, output_total;

    //New Button
    LinearLayout layout;
    //private Button btn;
    private int CurrentButtonNumber = 0;
    private TextView time, amount, content;

    public AccountView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountView.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountView newInstance(String param1, String param2) {
        AccountView fragment = new AccountView();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
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

        //produce DB writable object
        dbhelper = new DBhelper(getActivity());
        new Thread(){
            public void run(){
                db = dbhelper.getWritableDatabase();
            }
        }.start();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account_view, container, false);

        //read data
        output = (TextView)v.findViewById(R.id.act_title);
        DBhelper dbhelper = new DBhelper(getActivity());
        dbget = dbhelper.getReadableDatabase();

        //print out total money
        output_total = (TextView)v.findViewById(R.id.now_money);
        String[] count = new String[]{"total"};
        int total=0;
        Cursor am = dbget.query("Now", count,null,null,null,null,null);
        am.moveToFirst();
        for (int i = 0; i < am.getCount(); i++) {
            total=total+am.getInt(0);
            am.moveToNext();  //move next
        }
        am.close();
        output_total.setText(Integer.toString(total));
        //List of the amount data
        String[] colNames=new String[]{"UID","Time","Money","Memo"};
        String str = "";
        Cursor c = dbget.query(DATABASE_TABLE_OUT, colNames,null, null, null, null,null);
        //Create new button layout scrollView
        layout = (LinearLayout)v.findViewById(R.id.act_output_space);

        // display name
        for (int i = 0; i < colNames.length; i++)
            str += colNames[i] + "\t\t";
        str += "\n";
        output.setText(str);
        c.moveToFirst();  // first one

        for (int i = 0; i < c.getCount(); i++) {
            str="";
            str += c.getString(c.getColumnIndex(colNames[0])) + "\t\t";
            str += c.getString(1) + "\t\t";
            str += c.getString(2) + "\t\t";
            str += c.getString(3);//+ "\n";

            /*
            Button btn = new Button(this);
            btn.setId(CurrentButtonNumber);
            CurrentButtonNumber++;
            btn.setText(str);
            btn.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //New a intent object
                    Intent intent = new Intent();
                    intent.setClass(AccountView.this, AccountEdit.class);

                    //New a Bundle object
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", v.getId());
                    //bundle.putString("time",now_time);
                    //bundle.putInt("oneHundred",now_oneHundred);
                    //bundle.putInt("fiveHundred",now_fiveHundred);
                    //bundle.putInt("OneThousand",now_oneThousand);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            LinearLayout.LayoutParams param =new LinearLayout.LayoutParams(1000,150);
            layout.addView(btn, param);

            c.moveToNext();
            */
        }
        c.close();

        // Inflate the layout for this fragment
        return v;
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
