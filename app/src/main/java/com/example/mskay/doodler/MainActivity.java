package com.example.mskay.doodler;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private CustomView CustomView;
    private Toolbar Toolbar_top;
    private Toolbar Toolbar_bottom;
    private String rand_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar_top = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(Toolbar_top);

        Toolbar_bottom = (Toolbar)findViewById(R.id.toolbar_bottom);
        Toolbar_bottom.inflateMenu(R.menu.menu_drawing);
        Toolbar_bottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handleDrawingIconTouched(item.getItemId());
                return false;
            }
        });

    }

    private void handleDrawingIconTouched(int itemId) {
        switch (itemId){
            case R.id.action_delete:
                CustomView.eraseAll();
                break;
            case R.id.action_undo:
                CustomView.onClickUndo();
                break;
            case R.id.action_redo:
                CustomView.onClickRedo();
                break;
            case R.id.action_brush:
                final Dialog seekDialog = new Dialog(this);
                seekDialog.setContentView(R.layout.brush_size_chooser);
                final TextView seekTxt = (TextView)seekDialog.findViewById(R.id.brush_size_txt);
                final SeekBar seekOpq = (SeekBar)seekDialog.findViewById(R.id.brush_size_seek);
                //set max on seekbar
                seekOpq.setMax(100);
                float currLevel = CustomView.getLastBrushSize();
                // +"" is needed to convert from float to string to display
                seekTxt.setText(currLevel+"");
                seekOpq.setProgress(Math.round(currLevel));
                //listener for when interacting with seekbar
                seekOpq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekTxt.setText(Integer.toString(progress));
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}

                });
                //listen for clicks on ok
                Button opqBtn = (Button)seekDialog.findViewById(R.id.brush_size_ok);
                opqBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        CustomView.setLastBrushSize(seekOpq.getProgress());
                        CustomView.setBrushSize(seekOpq.getProgress());
                        seekDialog.dismiss();
                    }
                });
                // display dialog
                seekDialog.show();
                break;
            case R.id.action_opacity:
                final Dialog seekDialogOpacity = new Dialog(this);
                seekDialogOpacity.setContentView(R.layout.opacity_chooser);
                final TextView seekTxtOpacity = (TextView)seekDialogOpacity.findViewById(R.id.opq_txt);
                final SeekBar seekOpqOpacity = (SeekBar)seekDialogOpacity.findViewById(R.id.opacity_seek);
                //set max
                seekOpqOpacity.setMax(100);
                //show current level
                int currLevelOpacity = CustomView.getPaintAlpha();
                // Opacity includes a '%' at the end
                seekTxtOpacity.setText(currLevelOpacity+"%");
                seekOpqOpacity.setProgress(currLevelOpacity);
                seekOpqOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekTxtOpacity.setText(Integer.toString(progress)+"%");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });
                //listen for clicks on ok
                Button opqBtnOpacity = (Button)seekDialogOpacity.findViewById(R.id.opq_ok);
                opqBtnOpacity.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        CustomView.setPaintAlpha(seekOpqOpacity.getProgress());
                        seekDialogOpacity.dismiss();
                    }
                });
                // display dialog
                seekDialogOpacity.show();
                break;
            case R.id.action_color:
                final Dialog dialogColor = new Dialog(this);
                dialogColor.setContentView(R.layout.color_chooser);

                ImageButton color_img1 = (ImageButton)dialogColor.findViewById(R.id.color1);
                color_img1.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        CustomView.setColor("#FF0000");
                        CustomView.setPaintAlpha(CustomView.getPaintAlpha());
                        dialogColor.dismiss();
                    }
                });

                ImageButton color_img2 = (ImageButton)dialogColor.findViewById(R.id.color2);
                color_img2.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        CustomView.setColor("#FFA500");
                        CustomView.setPaintAlpha(CustomView.getPaintAlpha());
                        dialogColor.dismiss();
                    }
                });

                ImageButton color_img3 = (ImageButton)dialogColor.findViewById(R.id.color3);
                color_img3.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        CustomView.setColor("#FFFF00");
                        CustomView.setPaintAlpha(CustomView.getPaintAlpha());
                        dialogColor.dismiss();
                    }
                });

                ImageButton color_img4 = (ImageButton)dialogColor.findViewById(R.id.color4);
                color_img4.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        CustomView.setColor("#008000");
                        CustomView.setPaintAlpha(CustomView.getPaintAlpha());
                        dialogColor.dismiss();
                    }
                });

                ImageButton color_img5 = (ImageButton)dialogColor.findViewById(R.id.color5);
                color_img5.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        CustomView.setColor("#0000FF");
                        CustomView.setPaintAlpha(CustomView.getPaintAlpha());
                        dialogColor.dismiss();
                    }
                });

                ImageButton color_img6 = (ImageButton)dialogColor.findViewById(R.id.color6);
                color_img6.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        CustomView.setColor("#800080");
                        CustomView.setPaintAlpha(CustomView.getPaintAlpha());
                        dialogColor.dismiss();
                    }
                });

                ImageButton color_img7 = (ImageButton)dialogColor.findViewById(R.id.color7);
                color_img7.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        CustomView.setColor("#A52A2A");
                        CustomView.setPaintAlpha(CustomView.getPaintAlpha());
                        dialogColor.dismiss();
                    }
                });

                ImageButton color_img8 = (ImageButton)dialogColor.findViewById(R.id.color8);
                color_img8.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        rand_color = randomColor();
                        CustomView.setColor(rand_color);
                        CustomView.setPaintAlpha(CustomView.getPaintAlpha());
                        dialogColor.dismiss();
                    }
                });

                dialogColor.show();
                break;
        }
    }

    // Constructs menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        CustomView = (CustomView)findViewById(R.id.custom_view);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    // Create random hex color
    private String randomColor() {
        Random random = new Random();

        // create large random number
        int nextInt = random.nextInt(256*256*256);

        // format the number as hexadecimal string
        String color = String.format("#%06x", nextInt);

        return color;
    }
}
