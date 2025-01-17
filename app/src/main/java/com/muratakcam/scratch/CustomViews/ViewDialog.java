package com.muratakcam.scratch.CustomViews;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.muratakcam.scratch.R;

import java.util.Random;

public class ViewDialog {

    Activity activity;
    Dialog dialog;
    int selectedLoadingGif;
    //..we need the context else we can not create the dialog so get context in constructor
    public ViewDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {
        Random rand = new Random();
        int loadingGifs[]={
                R.drawable.loading1,
                R.drawable.loading2,
                R.drawable.loading3,
                R.drawable.loading4,
                R.drawable.loading5,
                R.drawable.loading6,
                R.drawable.loading7,
        };
        selectedLoadingGif=loadingGifs[rand.nextInt(7)];
        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.loading_image);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //...initialize the imageView form infalted layout
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        //gifImageView.setBackgroundColor(Color.BLACK);
        /*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        */
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

        //...now load that gif which we put inside the drawble folder here with the help of Glide

        Glide.with(activity)
                .load(selectedLoadingGif)
                .placeholder(selectedLoadingGif)

                .into(imageViewTarget);

        //...finaly show it
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideDialog(){
        dialog.dismiss();
    }

}