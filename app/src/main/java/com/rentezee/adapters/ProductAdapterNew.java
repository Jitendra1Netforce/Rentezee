package com.rentezee.adapters;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.rentezee.helpers.Constants;
import com.rentezee.main.Detail;
import com.rentezee.main.ProductListData;
import com.rentezee.main.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by John on 12/6/2016.
 */

public class ProductAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context context;
    List<ProductListData> list;
    private String rs;
    private final LayoutInflater inflater;
    ProductHolder addressHolder;
    public  static  String product_id;

    public ProductAdapterNew(Context context, List<ProductListData> itemList)
    {

        this.list = itemList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        rs = context.getString(R.string.rs);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View view = inflater.inflate(R.layout.row_products, parent, false);
        addressHolder = new ProductHolder(view);

        return addressHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {

        ProductHolder homeHolder = (ProductHolder) holder;

    Glide.with(context)
        .load(list.get(position).image_url)
        .centerCrop()
    //.placeholder(R.mipmap.ic_loading)
    .crossFade()
    .into(homeHolder.ivProductImage);

        homeHolder.tvProductName.setText(list.get(position).product_name);
        homeHolder.tvProductCategoryName.setText(list.get(position).category_name);

    System.out.println("security amount============="+ list.get(position).security_price.toString());

    if(list.get(position).security_price.toString().equals("0"))
    {

        homeHolder.tvPrice.setText(rs + list.get(position).price);

    }
    else
    {
        homeHolder.tvPrice.setText(rs + list.get(position).price + " per day");
    }

        homeHolder.linearlayout_productView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Detail.class);
                intent.putExtra(Constants.PRODUCT_ID, list.get(position).product_id);
                context.startActivity(intent);
            }
        });


}

    private void showMessage(String s)
    {

        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return list.size();
//        return itemList.size();
    }

    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("dd MMM yyyy HH:mm").format(new Date());
    }





}
