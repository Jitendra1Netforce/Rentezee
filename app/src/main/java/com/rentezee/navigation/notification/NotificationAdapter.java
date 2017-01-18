package com.rentezee.navigation.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.rentezee.main.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 11/11/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private static final int SIMPLE_TYPE = 0;
    private static final int IMAGE_TYPE = 1;
    private final LayoutInflater inflater;
    ArrayList<Integer> values=new ArrayList<>();
    private List<NotificationData> itemList;
    private Context context;
    NotificationHolder notificationHolder;


    public NotificationAdapter(Context context, List<NotificationData> itemList)
    {
        this.itemList = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.row_notification, parent, false);
        notificationHolder = new NotificationHolder(view);
        return notificationHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        NotificationHolder homeHolder = (NotificationHolder) holder;

        if(itemList.get(position).type.toString().equals("transaction"))
        {
            homeHolder.tvProductTitle.setText(itemList.get(position).title);

            String data_message = itemList.get(position).message+"\n"+"Transaction id -"+itemList.get(position).transaction_id.toString()+"\n"+"Order id -"+itemList.get(position).order_id.toString();

            homeHolder.tv_product_descricption.setText(data_message);

            String dtStart = itemList.get(position).date;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date = format.parse(dtStart);
                System.out.println(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            homeHolder.tv_date.setText(dtStart.toString().substring(0,10));

            homeHolder.tv_time.setText(dtStart.toString().substring(11,16));

        }
        else{

            homeHolder.tvProductTitle.setText(itemList.get(position).title);

            homeHolder.tv_product_descricption.setText(itemList.get(position).message);

            String dtStart = itemList.get(position).date;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date date = format.parse(dtStart);
                System.out.println(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            homeHolder.tv_date.setText(dtStart.toString().substring(0,10));

            homeHolder.tv_time.setText(dtStart.toString().substring(11,16));

        }



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