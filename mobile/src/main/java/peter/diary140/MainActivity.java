package peter.diary140;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codesnippets4all.json.parsers.JsonParserFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView ListRecycler;
    RecyclerView.LayoutManager ListManager;
     RecyclerView.Adapter ListAdapter;
    ArrayList<ArrayList<String>> data;
    int postsleft = 5;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menumain, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.privacy:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lilpete.me/emoji-diary-blog-post"));
                startActivity(browserIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListRecycler = (RecyclerView) findViewById(R.id.List); // Define RecyclerView
        ListRecycler.setHasFixedSize(true); //Improve performance

        ListManager = new LinearLayoutManager(this);
        ListRecycler.setLayoutManager(ListManager);

        ListAdapter = new ListAdapterClass(data);
        ListRecycler.setAdapter(ListAdapter);
        getData();
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        if (sharedPref.getString("packset", "none").equals("none")) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("packset", "Standard");
            editor.apply();
        }
    }
    boolean isEmojiSelected = false;
    int WhichPressed = 0;
    String whichemoji;
    public void selectEmoji(View view) throws InterruptedException {
        if (WhichPressed != 0) {
                view.getRootView().findViewById(WhichPressed).setBackgroundColor(Color.parseColor("#00000000"));

        }
        WhichPressed = view.getId();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ColorDrawable transparentDrawable = new ColorDrawable();
            transparentDrawable.setColor(0x00000000); //transparent white

            Drawable finalColorDrawable = getResources().getDrawable(R.drawable.circle);

            Drawable[] layers = new Drawable[]{transparentDrawable, finalColorDrawable};
            TransitionDrawable transitionDrawable = new TransitionDrawable(layers);



            view.setBackground(transitionDrawable);
        transitionDrawable.startTransition(250); //duration in milliseconds
        } else {
            view.setBackgroundResource(R.drawable.circle);
        }
        switch (view.getId()) {
            case (R.id.emoji1) :
                whichemoji="1";
                break;
            case (R.id.emoji2) :
                whichemoji="2";
                break;
            case (R.id.emoji3) :
                whichemoji="3";
                break;
            case (R.id.emoji4) :
                whichemoji="4";
                break;
            case (R.id.emoji5) :
                whichemoji="5";
                break;
        }
        isEmojiSelected=true;
    }
    Boolean IMGlibary = false;
    String b64Image = "none";
    Uri photo;
    Dialog dialog;
    public void selectEmojiPack(String name, String s) {
        if (!name.equals("Standard")) {
                String dir = "";
                if (name.equals("none")) {
                    SharedPreferences sharedPref = getPreferences( Context.MODE_PRIVATE);
                    dir = sharedPref.getString("packset", "Standard");
                } else {
                    dir = name;
                }
                for (int i = 1; i <= 5; i++) {
                    ImageView img = ((ImageView) viewdialog.findViewById(getResources().
                            getIdentifier("emoji" + String.valueOf(i), "id", getPackageName())));

                        File file = new File(MainActivity.this.getFilesDir()  + "/" + dir, "/emoji" + String.valueOf(i) + ".png");
                        if (file.exists()) {
                            img.setImageURI(Uri.fromFile(file));
                        } else {
                        }
                        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("d140", Context.MODE_PRIVATE);
                        SharedPreferences.Editor sedit= sharedPref.edit();
                        sedit.putString("emojis", s);
                        sedit.commit();

                        }
                } else {
            for (int i = 1; i <= 5; i++) {
                ImageView img = ((ImageView) viewdialog.findViewById(getResources().
                        getIdentifier("emoji" + String.valueOf(i), "id", getPackageName())));
                switch (i) {
                    case 1:
                        img.setImageResource(R.drawable.emoji1);
                    case 2:
                        img.setImageResource(R.drawable.emoji2);
                    case 3:
                        img.setImageResource(R.drawable.emoji3);
                    case 4:
                        img.setImageResource(R.drawable.emoji4);
                    case 5:
                        img.setImageResource(R.drawable.emoji5);
                }
                img.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("emoji" + String.valueOf(i), "drawable", getPackageName())));
            }
        }

        }


    View viewdialog;
    public void onNew(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        viewdialog = inflater.inflate(R.layout.dialog_create, null);
        ViewGroup parent = viewdialog.findViewById(R.id.emojiv);
        for(int i = 0; i < ((ViewGroup)parent.getChildAt(0)).getChildCount(); i++) {
            View child = ((ViewGroup)parent.getChildAt(0)).getChildAt(i);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        selectEmoji(view);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        builder.setView(viewdialog).setTitle(getString(R.string.create))
                // Add action buttons
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle report = new Bundle();
                        report.putString("entry_size", String.valueOf(ListAdapter.getItemCount() - (Math.floor(ListAdapter.getItemCount()/4))));
                        setdata(viewdialog, view, b64Image);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle report = new Bundle();
                        report.putString("entry_size", String.valueOf(ListAdapter.getItemCount() - (Math.floor(ListAdapter.getItemCount()/4))));
                    }
                });
        dialog = builder.create();
        dialog.show(); // Show dialog
        selectEmojiPack("none", null);
        viewdialog.findViewById(R.id.select_pack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"Open source edition does not support adding Packs.", Toast.LENGTH_LONG).show();
            }
        });
        viewdialog.findViewById(R.id.AddImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle report = new Bundle();
                report.putString("entry_size", String.valueOf(ListAdapter.getItemCount() - (Math.floor(ListAdapter.getItemCount()/4))));
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.WhatDo)
                        .setItems(R.array.takeOrBrowser, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (ContextCompat.checkSelfPermission(MainActivity.this,
                                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        MainActivity.this.dialog.dismiss();
                                        Snackbar error = Snackbar.make(findViewById(R.id.mainlayout), getString(R.string.permissionallow), 6200);
                                        error.setAction(getString(R.string.open), new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                            }
                                        });
                                        error.show();
                                        return;
                                    } else {

                                        // No explanation needed, we can request the permission.

                                        ActivityCompat.requestPermissions(MainActivity.this,
                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                        // app-defined int constant. The callback method gets the
                                        // result of the request.
                                    }
                                }
                                if (which == 0) { // If capture image press
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    // Ensure that there's a camera activity to handle the intent
                                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                        // Create the File where the photo should go
                                        File photoFile = null;
                                        try {
                                            photoFile = createImageFile();
                                        } catch (IOException ex) {
                                            Snackbar error = Snackbar.make(findViewById(R.id.mainlayout), getString(R.string.errorocur), 3500);
                                            error.show();
                                            ex.printStackTrace();
                                        }
                                        // Continue only if the File was successfully created
                                        if (photoFile != null) {
                                            photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                                    "peter.diary140.MainActivity",
                                                    photoFile);
                                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                            startActivityForResult(takePictureIntent, 1);
                                        }
                                    }
                                } else {
                                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); // Create intent for picking picture
                                    startActivityForResult(galleryIntent, 1); // Start activity
                                    IMGlibary = true; // Knows that it is picking from the img libary
                                }
                            }
                        });
                Dialog dialog = builder.create();
                dialog.show(); // Show dialog
            }
        });
    }
    public void setdata(View viewdialog, View view, String base64s) {
        ArrayList<String> data = new ArrayList<String>();
        ((TextView)viewdialog.findViewById(R.id.postleft)).setText("");
        data.add(((TextView)viewdialog.findViewById(R.id.SayText)).getText().toString());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        data.add(dateFormat.format(date));
        if (isEmojiSelected) {
            data.add(whichemoji);
            isEmojiSelected=false;
        } else {
            Snackbar sb = Snackbar.make(view, "Sorry, You must select an emoji.", 3000);
            sb.show();
            return;
        }
        data.add(b64Image);
        b64Image = "none";
        data.add(String.valueOf(ListAdapter.getItemCount()));
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        data.add(sharedPref.getString("packset", "Standard"));
        File file = new File(MainActivity.this.getFilesDir(), "arraylist");
        ArrayList<ArrayList<String>> fromjson = new ArrayList<>();
        try {
            String input = (new BufferedReader(new FileReader(file))).readLine(); // Read file
            Gson gsoninterface = new Gson();
            fromjson = gsoninterface.fromJson(input, ArrayList.class); // Store items in JSON file to Java object
        } catch (java.io.IOException e) {
        }
        fromjson.add(data); // Add your value
                        /*
                        for (int i = 0; i != fromjson.size(); i++) {
                            if (fromjson.get(i).get(1).equals(dateFormat.format(date))) {
                                postsleft--;
                            }
                        }
                        if (postsleft <= 0) {
                            Snackbar sb = Snackbar.make(view, "Sorry, You cannot post more than 5 times every day", 3000);
                            sb.show();
                            return;
                        } */
        postsleft=5;
        GsonBuilder gsonbuilder = new GsonBuilder(); // Make Gsonbuilder
        String a = gsonbuilder.create().toJson(fromjson); // Convert back to JSON
        FileOutputStream outputStream; // Setup FileOutputStream
        try {
            outputStream = openFileOutput(file.getName(), Context.MODE_PRIVATE); // Open file for write
            outputStream.write(a.getBytes()); // Write to file
            outputStream.close(); // Close outputStream
            getData();
        } catch (Exception e) {
            Snackbar error = Snackbar.make(findViewById(R.id.mainlayout), getString(R.string.errorocur), 3500);
            error.show();
            e.printStackTrace();
        }
    }
    public void getData() {
        File file = new File(MainActivity.this.getFilesDir(), "arraylist");
        ArrayList<ArrayList<String>> fromjson;
        try {
            String input = (new BufferedReader(new FileReader(file))).readLine(); // Read file
            Gson gsoninterface = new Gson();
            fromjson = gsoninterface.fromJson(input, ArrayList.class); // Store items in JSON file to Java
            ((ListAdapterClass) ListAdapter).updatedata(fromjson);
            ListAdapter.notifyDataSetChanged();
            if (ListAdapter.getItemCount() == 0) {
                findViewById(R.id.tocreate).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.tocreate).setVisibility(View.GONE);
            }
        } catch (java.io.IOException e) {
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE  || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getData();
        }
    }
    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "EMOJIDIARY_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) { // Check that the user didn't cancel
            if (!IMGlibary) {
                new Base64ify().execute(photoURI);
            } else { // If chose from image libary
                IMGlibary = false; // Reset variable
                new Base64ify().execute(data.getData());
            }
        }
    }
    public void setB64(Uri string) {
        InputStream iStream = null;
        try {

                iStream = getContentResolver().openInputStream(string);
                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];

                int len = 0;
                while ((len = iStream.read(buffer)) != -1) {
                    byteBuffer.write(buffer, 0, len);
                }
                ContextWrapper cw = new ContextWrapper(this);
            (new File(Environment.getExternalStorageDirectory() + "/Pictures/EmojiDiary/")).mkdirs();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "EMOJIDIARY_" + timeStamp + "_" + ".jpg";
                FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Pictures/EmojiDiary/" + imageFileName);
                fos.write(byteBuffer.toByteArray());
                File file = new File(Environment.getExternalStorageDirectory() + "/Pictures/EmojiDiary/" + imageFileName);
                b64Image = Uri.fromFile(file).toString();

        } catch (FileNotFoundException e) {
            MainActivity.this.dialog.dismiss();
            if (e.getMessage().contains("Permission denied")) {
                Snackbar error = Snackbar.make(findViewById(R.id.mainlayout), getString(R.string.permissionallow), 6200);
                error.setAction(getString(R.string.open), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                });
                error.show();
            } else {
                e.printStackTrace();
            }
        } catch (IOException e) {
            MainActivity.this.dialog.dismiss();
            e.printStackTrace();
        }
    }
    Uri photoURI;
    private class Base64ify extends AsyncTask<Uri, Integer, Uri> { // This used to base64 the image but that was too resourse costly. It now just adds the location of image but is called the same because I was too lazy.
        @Override
        protected Uri doInBackground(Uri... params) {
            try {
                setB64(params[0]);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }
}
