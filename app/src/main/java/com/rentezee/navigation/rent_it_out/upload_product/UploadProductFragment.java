package com.rentezee.navigation.rent_it_out.upload_product;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;
import com.rentezee.navigation.profile.general.UserSessionManager;
import com.rentezee.navigation.rent_it_out.upload_product.rentitData.RentItAdapter;
import com.rentezee.navigation.rent_it_out.upload_product.rentitData.RentItData;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.main.DashboardContainer;
import com.rentezee.main.R;
import com.rentezee.pojos.User;
import com.rentezee.services.GPSTracker;
import com.rentezee.services.ImageFilePath;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class UploadProductFragment extends Fragment
{

    private final static int PERMISSION_RQ = 84;
    List<Part> files = new ArrayList();
    CircleImageView circleImageView;
    private static final int TAKE_PHOTO_CODE = 108;
    private static final String IMAGE_DIRECTORY_NAME = "rentenzee";
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int PICK_IMAGE = 109;
    public static ArrayList<RentItData> rentItDatas = new ArrayList<>();
    RecyclerView recyclerView;
    RentItAdapter adapter;
    LinearLayoutManager layoutManager;
    RelativeLayout relativeUpload;
    String user_id;
    long  userId;
    User user;
    EditText product_name, discription,security_amount,rent_per_day,price;
    MaterialDialog dialog;
    BaseActivity baseActivity;
    int image_size = 2;
    DashboardContainer dashboardContainer;
    ArrayList<String> arrayList;
    public  int cate_id =0;
    String product_price,security_price;
    CheckBox saleCheckButton,rentCheckButton;
    double latitude,longitude;
    MaterialSpinner spinner;
    ArrayList<String>  category_data;
    Uri  fileUri;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_product2, container, false);

        circleImageView = (CircleImageView) view.findViewById(R.id.profile_image);

        product_name = (EditText) view.findViewById(R.id.edtProductName);

        discription = (EditText) view.findViewById(R.id.edtDiscription);

        price = (EditText) view.findViewById(R.id.edtPrice);

        security_amount = (EditText) view.findViewById(R.id.edtSecurityAmount);

        rent_per_day = (EditText) view.findViewById(R.id.edtRentPerDay);

        relativeUpload = (RelativeLayout) view.findViewById(R.id.relativeUpload);

        saleCheckButton = (CheckBox) view.findViewById(R.id.saleRadioButton);

        rentCheckButton = (CheckBox) view.findViewById(R.id.rentRadioButton);

        GPSTracker gpsTracker = new GPSTracker(getActivity());

         latitude = gpsTracker.getLatitude();

         longitude = gpsTracker.getLongitude();

        dashboardContainer = new DashboardContainer();

         spinner = (MaterialSpinner) view.findViewById(R.id.spinner);

        arrayList = new ArrayList<>();

         category_data = new ArrayList<>();

        arrayList = dashboardContainer.category_id;

        category_data= dashboardContainer.category_data;

        spinner.setItems(category_data);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item)
            {
                if (position==0)
                {
                  cate_id= 0;
                }
                else
                {
                    //Snackbar.make(view, "Clicked " + arrayList.get(position-1).toString(), Snackbar.LENGTH_LONG).show();
                    cate_id = Integer.parseInt(arrayList.get(position-1).toString());
                }


            }
        });

        baseActivity = new BaseActivity()
        {
            @Override
            public void showProgressBar(Context context)
            {
                super.showProgressBar(context);
            }
        };

        user = (User) new AppPreferenceManager(getActivity()).getObject(PreferenceKeys.savedUser, User.class);

        if(user != null)
        {

            userId = user.getUserId();
        }

        user_id = Long.toString(userId);

        rentItDatas.clear();

        setupRecyclerView(view);

        relativeUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                upload_image();
            }
        });



        circleImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission to save videos in external storage
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
                }

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    // Request permission to save videos in external storage
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_RQ);
                }
                else
                {
                    try {
                        showEditPicPopup();
                    } catch (Exception ex) {
                        //showMessage("Grant permission first");
                    }
                }


            }
        });

        saleCheckButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(saleCheckButton.isChecked())
                {
                    // is checked
                    rentCheckButton.setChecked(false);
                    price.setVisibility(View.VISIBLE);
                    security_amount.setVisibility(View.GONE);
                    rent_per_day.setVisibility(View.GONE);

                }
                else
                {
                    // not checked
                }

            }
        });


        rentCheckButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(rentCheckButton.isChecked())
                {
                    // is checked
                    saleCheckButton.setChecked(false);
                    price.setVisibility(View.GONE);
                    security_amount.setVisibility(View.VISIBLE);
                    rent_per_day.setVisibility(View.VISIBLE);
                }
                else
                {
                    // not checked
                }

            }
        });



        return view;
    }


    private void setupRecyclerView(View v)
    {

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        adapter = new RentItAdapter(getActivity(), rentItDatas);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setReverseLayout(true); // THIS ALSO SETS setStackFromBottom to true
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        System.out.println("Hi data is available===========");

        switch (requestCode)
        {
            case TAKE_PHOTO_CODE:
                if (resultCode == Activity.RESULT_OK)
                {
                    Log.i("result picture", "clicked");

                  /*  Bitmap photo = (Bitmap) data.getExtras().get("data");
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getActivity(), photo);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    //File finalFile = new File(getRealPathFromURI(tempUri));

                    String filepath = ImageFilePath.getPath(getActivity(),tempUri);
*/

                    String filepath = fileUri.getPath();

                    rentItDatas.add(new RentItData(filepath));

                    Log.i("result filepath1", filepath);
                    // imageViewDP.setImageURI(Uri.parse(finalFile.getAbsolutePath()));
                    adapter.notifyDataSetChanged();

                }
                break;
            case PICK_IMAGE:
                if (resultCode ==Activity. RESULT_OK)
                {
                    Uri selectedImage = data.getData();

                    if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    {

                        String filepath = ImageFilePath.getPath(getActivity(),selectedImage);

                        rentItDatas.add(new RentItData(filepath));

                       /* String wholeID = DocumentsContract.getDocumentId(selectedImage);

                        // Split at colon, use second item in the array
                        String id = wholeID.split(":")[1];

                        String[] column = {MediaStore.Images.Media.DATA};

                        // where id is equal to
                        String sel = MediaStore.Images.Media._ID + "=?";

                        Cursor cursor = getActivity().getContentResolver().
                                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        column, sel, new String[]{id}, null);

                        int columnIndex = cursor.getColumnIndex(column[0]);

                        if (cursor.moveToFirst())
                        {
                            String  filePath = cursor.getString(columnIndex);

                            System.out.println("filepath=============" + filePath);

                            rentItDatas.add(new RentItData(filePath));
                    }
                        cursor.close();
*/
                    }

                    else
                    {
                       /* String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);

                        System.out.println("imagepath=============" + filePath);

                        cursor.close();*/
                        String filepath = ImageFilePath.getPath(getActivity(),selectedImage);

                        rentItDatas.add(new RentItData(filepath));


                    }

                    //  imageViewDP.setImageURI(Uri.parse(filePath));
                    adapter.notifyDataSetChanged();
                }

                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    public Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri)
    {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    private void pickPictureIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    private void upload_image()
    {


         System.out.println("cate_id==========" + cate_id);

        int size = rentItDatas.size();

        if(size >= image_size)
        {

        if (product_name.getText().toString().equals(""))
        {
            showError("Please enter product name");
            return;
        }


            if (cate_id == 0)
            {
                showError("Please Choose Category");
                return;
            }


            if (discription.getText().toString().equals("")) {

            showError("Please enter discription");
            return;
        }

         if (security_amount.getVisibility()== View.VISIBLE)
         {

             if (security_amount.getText().toString().equals(""))
             {
                 showError("Please enter security amount");
                 return;
             }

             security_price =security_amount.getText().toString();

         }
         else
         {

             security_price = "0";

         }

        if(rent_per_day.getVisibility()== View.VISIBLE){

            if (rent_per_day.getText().toString().equals(""))
            {

                showError("Please enter per day rent amount");
                return;
            }

        }

            if(price.getVisibility()== View.VISIBLE){

                if (price.getText().toString().equals(""))
                {

                    showError("Please enter price amount");
                    return;
                }

            }

            baseActivity.showProgressBar(getActivity());
            System.out.println("image array size =====" + rentItDatas.size());

            for (int i = 0; i <rentItDatas.size(); i++)
            {
                files.add(new FilePart("image[]", savebitmap(rentItDatas.get(i).path)));
                Log.e("sellDatas",files.toArray().toString());
            }

            if(rent_per_day.getText().toString().equals(""))
            {
                product_price = price.getText().toString();

            }
            else
            {

                product_price = rent_per_day.getText().toString();
            }


            System.out.println("latitude---" + latitude + "longitude-------" + longitude);


            Log.e("sellDatas", files.toString());
            Ion.with(getActivity())
                    .load("https://netforcesales.com/renteeze/webservice/products/add_item")
                            //.setHeader("ENCTYPE", "multipart/form-data")
                    .addMultipartParts(files)
                    .setMultipartParameter("action", "add_item")
                           // .setMultipartFile("image", "image*//*", new File(rentItDatas.get(0).path))
                    .setMultipartParameter("user_id", user_id)
                    .setMultipartParameter("product_name", product_name.getText().toString())
                    .setMultipartParameter("description", discription.getText().toString())
                    .setMultipartParameter("category_id", String.valueOf(cate_id))
                    .setMultipartParameter("security_price", security_price)
                    .setMultipartParameter("price", product_price)
                    .setMultipartParameter("lat", String.valueOf(latitude))
                    .setMultipartParameter("lon", String.valueOf(longitude))
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                           // Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                            if (result == null)
                            {

                                Toast.makeText(getActivity(), "error called", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                baseActivity.dismissProgressBar();

                            }
                            else
                            {

                                System.out.println("result=============" + result);
                                Toast.makeText(getActivity(), "Product has been sucessfully uploaded", Toast.LENGTH_SHORT).show();
                                Log.e("result", result.toString());

                                product_name.setText("");
                                discription.setText("");
                                price.setText("");
                                security_amount.setText("");
                                rent_per_day.setText("");
                                rentItDatas.clear();
                                adapter.notifyDataSetChanged();


                                spinner.setSelectedIndex(0);

                                baseActivity.dismissProgressBar();

                              /*  ProductViewFragment productViewFragment = new ProductViewFragment();
                                productViewFragment.product_data();
                              */



                            }

                        }
                    });


        }
        else
        {
            showError("Please Choose 2  Images");
        }


      /*  if(!product_name.getText().toString().equals(""))
        {

            if(!discription.getText().toString().equals(""))
            {

                     String sec_amount = security_amount.getText().toString();
                     String rent = rent_per_day.getText().toString();
                     int security_amount = Integer.parseInt(sec_amount);
                     int rent_per_day_amount = Integer.parseInt(rent);

                      if(security_amount > rent_per_day_amount)
                      {

                          if(rentItDatas.size()> 0){
                      }
                    else
                      {
                          Toast.makeText(getActivity(),"Security Amount Should be Greater to Rent Par Day",Toast.LENGTH_SHORT).show();
                      }
                }
                else
                {

                    Toast.makeText(getActivity(),"Please Enter Discription",Toast.LENGTH_SHORT).show();

                }
            }
            else {

                Toast.makeText(getActivity(),"Please Enter Discription",Toast.LENGTH_SHORT).show();
            }
        }
        else {

            Toast.makeText(getActivity(), "Please Enter Product Name", Toast.LENGTH_SHORT).show();
        }
         */

    }


    private File savebitmap(String filePath)
    {
        File file = new File(filePath);
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        OutputStream outStream = null;
        try {
            // make a new bitmap from your file
            outStream = new FileOutputStream(file);
            if (extension.equalsIgnoreCase("png")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream);
            } else if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream);
            } else {
                return null;
            }
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }


    private void showEditPicPopup()
    {
        boolean wrapInScrollView = true;

       // Toast.makeText(getActivity(),"This is pic intent",Toast.LENGTH_SHORT).show();

        dialog = new MaterialDialog.Builder(getActivity())
                .title("Choose Picture")
                .customView(R.layout.dailog_editpic, wrapInScrollView)
                .negativeText("Cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
        dialog.findViewById(R.id.linearLayoutGalary).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission to save videos in external storage
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
                }
                else
                {
                    try
                    {
                        pickPictureIntent();
                    }catch (Exception ex){
                        //showMessage("Grant permission first");
                    }
                    dialog.dismiss();
                }

            }
        });
        dialog.findViewById(R.id.linearLayoutPicture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureIntent();
                dialog.dismiss();
            }
        });

    }
    private void takePictureIntent()
    {

        try {

            UserSessionManager userSessionManager = new UserSessionManager(getActivity());
            String name = userSessionManager.getName();
            Intent cameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
              fileUri = getOutputMediaFileUri();

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);


        } catch (Exception e)
        {
            e.printStackTrace();
        }







    }



    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }


    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            } else {
                Log.d(IMAGE_DIRECTORY_NAME, mediaStorageDir.getAbsolutePath());
            }

        } else {
            Log.d(IMAGE_DIRECTORY_NAME, mediaStorageDir.getAbsolutePath());
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = null;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        Log.i("result imagepath", mediaFile.getAbsolutePath());


        return mediaFile;
    }



    public  void showError(String content){

        new MaterialDialog.Builder(getActivity())
                .title("Rentenzee")
                .content(content)
                .positiveText("Ok")
                .show();
    }



}
