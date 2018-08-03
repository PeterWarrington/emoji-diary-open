package peter.diary140;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by peterwarrington on 07/07/2017.
 */

public class ListAdapterClass extends RecyclerView.Adapter<ListAdapterClass.ViewHolder>{
    private ArrayList<ArrayList<String>> Data;
    ViewGroup latestpearent;
    View gv;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    int adnum = 0;
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public View holder;
        @Override
        public void onClick(final View v) {
            //if (getLayoutPosition()> 0) { getnum = getLayoutPosition()-adnum;} else {
            getnum = (int) ((Data.size() - getLayoutPosition() -1 )+(Math.floor(getLayoutPosition()/4)));
            //}
            if (v.getId() == R.id.share) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String emoji = null;
                if (Data.get(getnum).get(5).equals("Standard")) {
                    switch (Data.get(getnum).get(2)) {
                        case "5":
                            emoji = "\uD83D\uDE01";
                            break;
                        case "4":
                            emoji = "\uD83D\uDE03";
                            break;
                        case "3":
                            emoji = "\uD83D\uDE10";
                            break;
                        case "2":
                            emoji = "\uD83D\uDE15";
                            break;
                        case "1":
                            emoji = "\uD83D\uDE22";
                            break;
                    }
                } else {
                    SharedPreferences sharedPref = v.getContext().getApplicationContext().getSharedPreferences("d140",Context.MODE_PRIVATE);
                    String emoji1 = sharedPref.getString("emojis", "");
                        int number = (Integer.valueOf(Data.get(getnum).get(2))-1) *2;
                        emoji = emoji1.substring(number, number + 2);
                }
                sendIntent.putExtra(Intent.EXTRA_TEXT, Data.get(getnum).get(0) + " " + emoji + " " + v.getContext().getString(R.string.sharemsg));
                if (!Data.get(getnum).get(3).equals("none")) {
                    sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Data.get(getnum).get(3)));
                    sendIntent.setType("image/*");
                } else {
                    sendIntent.setType("text/plain");
                }
                Bundle report = new Bundle();
                report.putString("with_image", String.valueOf(!Data.get(getnum).get(3).equals("none")));
                report.putString("entry_size", String.valueOf(Data.size()));
                v.getContext().startActivity(sendIntent);
            }
            if (v.getId() == R.id.delete) {
                final ViewHolder context = this;
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.suredelete)
                        // Add action buttons
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                    Data.remove(getnum);
                                    ((RecyclerView) v.getRootView().findViewById(R.id.List)).getAdapter().notifyDataSetChanged();
                                    File file = new File(v.getContext().getFilesDir(), "arraylist");
                                    GsonBuilder gsonbuilder = new GsonBuilder(); // Make Gsonbuilder
                                    String a = gsonbuilder.create().toJson(Data); // Convert back to JSON
                                    FileOutputStream outputStream; // Setup FileOutputStream
                                    try {
                                        outputStream = v.getContext().openFileOutput(file.getName(), Context.MODE_PRIVATE); // Open file for write
                                        outputStream.write(a.getBytes()); // Write to file
                                        outputStream.close(); // Close outputStream

                                    } catch (Exception e) {
                                        Snackbar error = Snackbar.make(holder.getRootView().findViewById(R.id.mainlayout), holder.getContext().getString(R.string.errorocur), 3500);
                                        error.show();
                                        e.printStackTrace();
                                    }
                            }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                Dialog dialog = builder.create();
                dialog.show(); // Show dialog
                Bundle report = new Bundle();
                report.putString("with_image", String.valueOf(!Data.get(getnum).get(3).equals("none")));
                report.putString("entry_size", String.valueOf(Data.size()));
            }
            if (v.getId() == R.id.imagetaken) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Uri.parse(Data.get(getnum).get(3)).getPath())), "image/*");
                v.getContext().startActivity(intent);
                Bundle report = new Bundle();
                report.putString("entry_size", String.valueOf(Data.size()));
            }
        }
        public ViewHolder(View v, final Context context) {
            super(v);
            holder = v;
            try {
                holder.findViewById(R.id.share).setOnClickListener(this);
                holder.findViewById(R.id.delete).setOnClickListener(this);
                holder.findViewById(R.id.imagetaken).setOnClickListener(this);
            } catch (Exception e) {
                Log.d("d140", "is ad");
            }
            gv = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListAdapterClass(ArrayList<ArrayList<String>> Dataset) {
        Data = Dataset;
    }
    public void updatedata(ArrayList<ArrayList<String>> d) {
        Data=d;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // create a new view
        latestpearent = parent;
        View v;

        if (viewType==20) {
            v = new ImageView(parent.getContext());
            ((ImageView) v).setImageBitmap(null);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_list_frame, parent, false);
            // set the view's size, margins, paddings and layout parameters
        }
        ViewHolder vh = new ViewHolder(v, parent.getContext());
        return vh;
    }
    int getnum;
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that

             getnum = (int) ((Data.size() - position -1 )+(Math.floor(position/4)));
             System.out.println(String.valueOf(Math.floor(position/4)));
            if (holder.getItemViewType() != 20) {
                ((RelativeLayout) holder.holder.findViewById(R.id.maincontente)).setVisibility(View.VISIBLE);
                ((TextView) holder.holder.findViewById(R.id.TextMain)).setText(Data.get(getnum).get(0));
                ((TextView) holder.holder.findViewById(R.id.TextDate)).setText(Data.get(getnum).get(1));
                if (Data.get(getnum).get(5).equals("Standard")) {
                    switch (Data.get(getnum).get(2)) {
                        case "5":
                            ((ImageView) holder.holder.findViewById(R.id.howfeel)).setImageResource(R.drawable.emoji5);
                            break;
                        case "4":
                            ((ImageView) holder.holder.findViewById(R.id.howfeel)).setImageResource(R.drawable.emoji4);
                            break;
                        case "3":
                            ((ImageView) holder.holder.findViewById(R.id.howfeel)).setImageResource(R.drawable.emoji3);
                            break;
                        case "2":
                            ((ImageView) holder.holder.findViewById(R.id.howfeel)).setImageResource(R.drawable.emoji2);
                            break;
                        case "1":
                            ((ImageView) holder.holder.findViewById(R.id.howfeel)).setImageResource(R.drawable.emoji1);
                            break;

                    }
                } else {
                        File file = new File(holder.holder.getContext().getFilesDir() + "/" + Data.get(getnum).get(5), "/emoji" + Data.get(getnum).get(2) + ".png");

                        //if (file.exists()) {
                            ((ImageView) holder.holder.findViewById(R.id.howfeel)).setImageURI(Uri.fromFile(file));
                        //}
                }
                ImageView image = ((ImageView) holder.holder.findViewById(R.id.imagetaken));
                if (!Data.get(getnum).get(3).equals("none")) { // Is image?
                    try {
                        image.setVisibility(View.VISIBLE);
                        image.setImageURI(Uri.parse(Data.get(getnum).get(3)));
                    } catch (Exception e) {
                        Snackbar error = Snackbar.make(holder.holder.getRootView().findViewById(R.id.mainlayout), holder.holder.getContext().getString(R.string.errorocur), 3500);
                        error.show();
                        e.printStackTrace();
                    }
                } else {
                    image.setVisibility(View.GONE);
                }
                if (holder.holder != null) {
                    ViewGroup parent = (ViewGroup) holder.holder.getParent();
                    if (parent != null) {
                        parent.removeView(holder.holder);
                    }
                }
            }

        }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (Data != null) {
            return (int) (Data.size() + (Math.floor(Data.size()/4)));
        } else {
            return 0;
        }
    }
    @Override
    public int getItemViewType(int position)
    {
        if (((position % 3) == 0) && position > 0) {
            return 20;
        }
        return 0;
    }
}
