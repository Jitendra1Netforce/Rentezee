package com.rentezee.fragments.myorder.PastOrder;

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
 * Created by John on 11/21/2016.
 */
public class PastOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    ArrayList<Integer> values=new ArrayList<>();
    private List<PastOrderData> itemList;
    private Context context;
    PastOrderHolder pastOrderHolder;


    public PastOrderAdapter(Context context, List<PastOrderData> itemList)
    {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.row_my_order, parent, false);
        pastOrderHolder = new PastOrderHolder(view);

        return pastOrderHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        PastOrderHolder homeHolder = (PastOrderHolder) holder;

        homeHolder.buttonPrice.setText("\u20B9"+itemList.get(position).price);

        homeHolder.tv_f_date.setText(itemList.get(position).order_date.toString().substring(0,10));

        homeHolder.tv_l_date.setText(itemList.get(position).order_date.toString().substring(0,10));

        String order_time = itemList.get(position).order_date.toString().substring(11,itemList.get(position).order_date.toString().length());
        System.out.println("order_time======"+ order_time);

        homeHolder.tv_time.setText(order_time);


    }

    private void showMessage(String s)
    {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount()
    {
        return itemList.size();
//        return itemList.size();
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date());
    }
}