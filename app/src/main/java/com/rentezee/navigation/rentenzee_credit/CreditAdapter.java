package com.rentezee.navigation.rentenzee_credit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rentezee.main.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 11/28/2016.
 */
public class CreditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{


    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    ArrayList<Integer> values=new ArrayList<>();
    private List<CreditData> itemList;
    private Context context;
    CreditHolder myCartHolder;
   CreditActivity creditActivity;

    public CreditAdapter(Context context, List<CreditData> itemList)
    {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = inflater.inflate(R.layout.row_credits, parent, false);
        myCartHolder = new CreditHolder(view);
        return myCartHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        CreditHolder homeHolder = (CreditHolder) holder;

        if(itemList.get(position).type.toString().equals("Debit"))
        {

            homeHolder.txtCreditVia.setText("Debit By :"+itemList.get(position).cart_via);

        }
        else
        {

            homeHolder.txtCreditVia.setText("Credit By :"+itemList.get(position).cart_via);

        }


        homeHolder.tvAmount.setText(itemList.get(position).amount);

        homeHolder.tvCreated.setText(itemList.get(position).created);




    }

    private void showMessage(String s)
    {

        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return itemList.size();
//        return itemList.size();
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date());
    }





}


