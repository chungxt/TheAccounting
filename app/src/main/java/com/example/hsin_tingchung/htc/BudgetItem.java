package com.example.hsin_tingchung.htc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hsin_tingchung.nav.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BudgetItem.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BudgetItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetItem extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //新增DB write
    private static String DATABASE_TABLE_OUT = "Budget";
    private SQLiteDatabase db;
    private DBhelper dbhelper;

    //新增DB read
    private SQLiteDatabase dbget;

    //claim button
    private Button doneButton;

    //claim TextView
    private TextView itemName;
    private TextView itemAmount;
    private TextView itemPeriod;

    //claim variable
    public String ID;
    private String NAME;
    private int amount;
    private int period;

    private OnFragmentInteractionListener mListener;

    public BudgetItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BudgetItem.
     */
    // TODO: Rename and change types and number of parameters
    public static BudgetItem newInstance(String param1, String param2) {
        BudgetItem fragment = new BudgetItem();
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
            ID = getArguments().getString("ID");
            Toast.makeText(getActivity(), ID, Toast.LENGTH_SHORT).show();
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
        // Inflate the layout for this fragment
        View view=null;
        view=inflater.inflate(R.layout.fragment_budget_item, container, false);

        //Buttons
        /*doneButton=(Button)view.findViewById(R.id.budget_item_save);
        doneButton.setOnClickListener(this);
*/
        //getID
        itemName=(TextView)view.findViewById(R.id.budget_name);
        itemAmount=(TextView)view.findViewById(R.id.budget_used);
        itemPeriod=(TextView)view.findViewById(R.id.budget_period);
        Bundle bundle = getArguments();
        if(bundle !=null) {
            ID = bundle.getString("ID");
            Toast.makeText(getActivity(), ID, Toast.LENGTH_SHORT).show();
        }

        /*

        //produce DB writable object
        dbhelper = new DBhelper(getActivity());
        new Thread(){
            public void run(){
                db = dbhelper.getWritableDatabase();
            }
        }.start();

        //produce DB readable object
        DBhelper dbhelper = new DBhelper(getActivity());
        dbget = dbhelper.getReadableDatabase();
        String[] colNames=new String[]{"B_UID","name","period","amount"};
        String where = "B_ID =" + ID;
        String str = "";
        Cursor c = dbget.query(DATABASE_TABLE_OUT, colNames,where, null, null, null,null);

        c.moveToFirst();
        itemName.setText(c.getString(1));
        itemPeriod.setText(c.getString(2));
        itemAmount.setText(c.getString(3));
        c.close();
        */


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onClick(View v){
        switch (v.getId()) {

            case R.id.save_new_budget:
                if (  ( !itemAmount.getText().toString().equals("")) && ( !itemName.getText().toString().equals("") ) ) {
                    NAME = itemName.getText().toString();
                    amount = Integer.parseInt(itemAmount.getText().toString());
                    period = Integer.parseInt(itemPeriod.getText().toString());
                    Toast.makeText(getActivity(),NAME, Toast.LENGTH_SHORT).show();
                    ContentValues nbg = new ContentValues();
                    nbg.put("name",NAME);
                    nbg.put("period",period);
                    nbg.put("amount",amount);
                    db.insert(DATABASE_TABLE_OUT, null, nbg);
                    SwitchToViewBudget();
                }
                else{
                    Log.e("NB", "Both inputs are required");
                    Toast.makeText(getActivity(),"Both inputs are required!" , Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.back_new_budget:
                SwitchToViewBudget();
                break;
            default:
                break;
        }


    }
    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    */

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void SwitchToViewBudget(){
        //switch fragment
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        BudgetView nextFrag = new BudgetView();
        fragmentTransaction.replace(R.id.fragment_container,nextFrag);
        //provide the fragment ID of your first fragment which you have given in
        //fragment_layout_example.xml file in place of first argument
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
