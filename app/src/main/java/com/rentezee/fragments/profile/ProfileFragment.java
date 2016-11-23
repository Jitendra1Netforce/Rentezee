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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;
import com.rentezee.fragments.profile.general.UserSessionManager;
import com.rentezee.helpers.AppPreferenceManager;
import com.rentezee.helpers.PreferenceKeys;
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
    EditText product_name, discription,security_amount,rent_per_day;
    MaterialDialog dialog;
    private static final int PICK_IMAGE = 109;
    String filePath;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_comtext = getActivity();

        circleImageView = (CircleImageView) view.findViewById(R.id.profile_image);

        relativeUpload = (RelativeLayout) view.findViewById(R.id.relativeUpload);

        user = (User) new AppPreferenceManager(getActivity()).getObject(PreferenceKeys.savedUser, User.class);

        if(user != null){

            userId = user.getUserId();
        }

        user_id = Long.toString(userId);


        relativeUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Hi", Toast.LENGTH_SHORT).show();
                upload_image();
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

       // files.add(new FilePart("image[]", savebitmap(filePath)));

        Ion.with(getActivity())
                .load("http://netforce.biz/renteeze/webservice/Users/edit_profile")
                .setMultipartFile("image", "image/*", new File(filePath))
                .setMultipartParameter("user_id", user_id)
                .setMultipartParameter("name", "Mulayam")
                .setMultipartParameter("email", "mulayam@gmail.com")
                .setMultipartParameter("mobile", "9811501065")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                        if (result == null) {
                            Toast.makeText(getActivity(), "error called", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } else {
                            Toast.makeText(getActivity(), "success called", Toast.LENGTH_SHORT).show();
                            Log.e("result", result.toString());


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