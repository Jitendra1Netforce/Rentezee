package com.rentezee.fragments.profile;

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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;
import com.rentezee.fragments.myorder.PastOrder.PastOrderData;
import com.rentezee.fragments.profile.general.UserSessionManager;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.BaseActivity;
import com.rentezee.helpers.PreferenceKeys;
import com.rentezee.helpers.Validator;
import com.rentezee.main.Login;
import com.rentezee.main.R;
import com.rentezee.pojos.User;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment
{


    List<Part> files = new ArrayList();
    Context profile_comtext;
    private final static int PERMISSION_RQ = 84;
    CircleImageView circleImageView;
    private static final int TAKE_PHOTO_CODE = 108;
    RelativeLayout relativeUpload;
    String user_id;
    long  userId;
    User user;
    EditText etName,etMobile;
    MaterialDialog dialog;
    private static final int PICK_IMAGE = 109;
    String filePath;
    BaseActivity baseActivity;
    Button buttonEditEnable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_comtext = getActivity();

        circleImageView = (CircleImageView) view.findViewById(R.id.profile_image);

        circleImageView.setEnabled(false);

        relativeUpload = (RelativeLayout) view.findViewById(R.id.relativeUpload);

        etName = (EditText) view.findViewById(R.id.etName);

        etName.setEnabled(false);

        buttonEditEnable = (Button) view.findViewById(R.id.buttonEditEnable);

        etMobile = (EditText) view.findViewById(R.id.etMobile);

        etMobile.setEnabled(false);

        user = (User) new AppPreferenceManager(getActivity()).getObject(PreferenceKeys.savedUser, User.class);

        baseActivity = new BaseActivity()
        {
            @Override
            public void showProgressBar(Context context)
            {
                super.showProgressBar(context);
            }
        };

        if(user != null)
        {

            userId = user.getUserId();
            etMobile.setText(user.getMobile());
            etName.setText(user.getName());

            Glide.with(getActivity())
                    .load(user.getImageUrl())
                    .centerCrop()
                            //.placeholder(R.mipmap.ic_loading)
                    .crossFade()
                    .into(circleImageView);


        }
        else
        {

            Intent  login = new Intent(getActivity(), Login.class);
            startActivity(login);
            getActivity().finish();


        }
        user_id = Long.toString(userId);


        relativeUpload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                upload_image();
            }
        });

        buttonEditEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etName.setEnabled(true);
                etMobile.setEnabled(true);
                circleImageView.setEnabled(true);

            }
        });


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    // Request permission to save videos in external storage
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_RQ);
                }
                else
                {
                    try
                    {
                        showEditPicPopup();
                    }
                    catch (Exception ex)
                    {
                        //showMessage("Grant permission first");
                    }
                }


            }
        });


        return view;
    }


    public  void showError(String content){

        new MaterialDialog.Builder(getActivity())
                .title("Rentenzee")
                .content(content)
                .positiveText("Ok")
                .show();
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
        dialog.findViewById(R.id.linearLayoutGalary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Request permission to save videos in external storage
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RQ);
                } else {
                    try {
                        pickPictureIntent();
                    } catch (Exception ex) {
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
            startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void upload_image()
    {

        System.out.println("filePath=============="+ filePath);


        final String name = etName.getText().toString();
        if (!Validator.isValidName(name)) {

            showError(getString(R.string.error_name));
            return;
        }


        final String mobile = etMobile.getText().toString();
        if (mobile.isEmpty()) {


            showError(getString(R.string.error_mobile_empty));
            return;
        }

        if (!Validator.isValidMobile(mobile)) {

            showError(getString(R.string.error_mobile_not_valid));

            return;
        }

       // files.add(new FilePart("image[]", savebitmap(filePath)));
        baseActivity.showProgressBar(getActivity());

        Ion.with(getActivity())
                .load("http://netforce.biz/renteeze/webservice/Users/edit_profile")
                .setMultipartFile("image", "image/*", new File(filePath))
                .setMultipartParameter("user_id", user_id)
                .setMultipartParameter("name", etName.getText().toString())
                //.setMultipartParameter("email", etEmail.getText().toString())
                .setMultipartParameter("mobile", etMobile.getText().toString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {

                        if (result == null) {
                            Toast.makeText(getActivity(), "Profile Not Update Successfully", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            baseActivity.dismissProgressBar();
                        }
                        else
                        {
                            // Toast.makeText(getActivity(), "success called", Toast.LENGTH_SHORT).show();
                             System.out.println("data=====" + result.toString());

                               JsonObject jsonObject = (JsonObject) result.getAsJsonObject("data");

                                String name = jsonObject.get("name").getAsString();
                                String email = jsonObject.get("email").getAsString();
                                String mobile = jsonObject.get("mobile").getAsString();
                                String image_url = jsonObject.get("imageUrl").getAsString();

                                long userId = Long.parseLong(user_id);
                                User user = new User(userId, name, email, mobile, image_url);
                                new AppPreferenceManager(getActivity()).putObject(PreferenceKeys.savedUser, user);

                            Log.e("result", result.toString());
                            baseActivity.dismissProgressBar();
                            showError("Profile Has Been Successfully Updated");
                            etName.setText("");
                            etMobile.setText("");
                        }

                    }
                });

    }


    private void pickPictureIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

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
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getActivity(), photo);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));

                    filePath =finalFile.getAbsolutePath();

                    circleImageView.setImageURI(Uri.parse(finalFile.getAbsolutePath()));

                }
                break;
            case PICK_IMAGE:
                if (resultCode ==Activity. RESULT_OK)
                {
                    Uri selectedImage = data.getData();

                    if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    {

                        String wholeID = DocumentsContract.getDocumentId(selectedImage);

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
                              filePath = cursor.getString(columnIndex);

                            System.out.println("filepath=============" + filePath);

                            circleImageView.setImageURI(Uri.parse(filePath));
                        }
                        cursor.close();
                    }

                    else
                    {
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                         filePath = cursor.getString(columnIndex);

                        System.out.println("imagepath=============" + filePath);

                        cursor.close();
                        circleImageView.setImageURI(Uri.parse(filePath));
                    }

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





}