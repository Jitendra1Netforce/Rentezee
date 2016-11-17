package com.rentezee.fragments.upload_product;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rentezee.main.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class UploadProductFragment extends Fragment
{

    private final static int PERMISSION_RQ = 84;
    private MaterialDialog dialog;
    CircleImageView circleImageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_product, container, false);

        circleImageView = (CircleImageView) view.findViewById(R.id.profile_image);


        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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



    private void showEditPicPopup() {
        boolean wrapInScrollView = true;
        dialog = new MaterialDialog.Builder(getActivity())
                .title("")
                .customView(R.layout.dailog_editpic, wrapInScrollView)
                .negativeText("")
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
                        //pickPictureIntent();
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
               // takePictureIntent();
                dialog.dismiss();
            }
        });

    }

}
