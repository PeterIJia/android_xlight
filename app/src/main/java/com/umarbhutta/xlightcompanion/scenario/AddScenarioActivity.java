package com.umarbhutta.xlightcompanion.scenario;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.SDK.xltDevice;
import com.umarbhutta.xlightcompanion.main.MainActivity;

public class AddScenarioActivity extends AppCompatActivity {

    private static final String TAG = AddScenarioActivity.class.getSimpleName();
    private SeekBar brightnessSeekBar;
    private TextView colorTextView;
    private Button addButton;
    private EditText nameEditText;
    private ImageView backImageView;
    private Spinner filterSpinner;

    private int scenarioBrightness = 0;
    private int c = 0, cw = 0, ww = 0, r = 0, g = 0, b = 0;
    private String colorHex, scenarioName, scenarioInfo, scenarioFilter;

    private LinearLayout llBack;
    private TextView tvTitle;

    private LinearLayout linear;
    private LayoutInflater mInflater;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scenario);
        mInflater = LayoutInflater.from(this);
        //hide nav bar
        getSupportActionBar().hide();

////        change status bar color to accent
//        Window window = this.getWindow();
//        //clear FLAG_TRANSLUCENT_STATUS flag:
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        //finally change the color
//        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));

        brightnessSeekBar = (SeekBar) findViewById(R.id.brightnessSeekBar);
        colorTextView = (TextView) findViewById(R.id.colorTextView);
        addButton = (Button) findViewById(R.id.addButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        backImageView = (ImageView) findViewById(R.id.backImageView);

        linear = (LinearLayout) findViewById(R.id.ll_horizontal_scrollview);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.add_scene);
        filterSpinner = (Spinner) findViewById(R.id.filterSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this, R.layout.control_scenario_spinner_item, MainActivity.filterNames);
        // Specify the layout to use when the list of choices appears
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the scenarioAdapter to the spinner
        filterSpinner.setAdapter(filterAdapter);

        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        colorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 弹出选择颜色的页面
                onFabPressed();
                //以前的
//                new ChromaDialog.Builder()
//                        .initialColor(ContextCompat.getColor(AddScenarioActivity.this, R.color.colorPrimary))
//                        .colorMode(ColorMode.RGB) // There's also ARGB and HSV
//                        .onColorSelected(new ColorSelectListener() {
//                            @Override
//                            public void onColorSelected(int color) {
//                                Log.e(TAG, "int: " + color);
//                                colorHex = String.format("%06X", (0xFFFFFF & color));
//                                Log.e(TAG, "HEX: #" + colorHex);
//
//                                c = (int)Long.parseLong(colorHex, 16);
//                                r = (c >> 16) & 0xFF;
//                                g = (c >> 8) & 0xFF;
//                                b = (c >> 0) & 0xFF;
//                                Log.e(TAG, "RGB: " + r + "," + g + "," + b);
//
//                                colorHex = "#" + colorHex;
//                                colorTextView.setText(colorHex);
//                                colorTextView.setTextColor(Color.parseColor(colorHex));
//                            }
//                        })
//                        .create()
//                        .show(getSupportFragmentManager(), "dialog");
            }
        });

        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e(TAG, "The brightness value is " + seekBar.getProgress());
                scenarioBrightness = seekBar.getProgress();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send info back to ScenarioFragment
                scenarioName = nameEditText.getText().toString();

                scenarioInfo = "A " + colorHex + " color with " + scenarioBrightness + "% brightness";

                //SEND TO PARTICLE CLOUD FOR ALL RINGS
                MainActivity.m_mainDevice.sceAddScenario(ScenarioFragment.name.size(), scenarioBrightness, cw, ww, r, g, b, xltDevice.DEFAULT_FILTER_ID);

                //send data to update the list
                Intent returnIntent = getIntent();
                returnIntent.putExtra(ScenarioFragment.SCENARIO_NAME, scenarioName);
                returnIntent.putExtra(ScenarioFragment.SCENARIO_INFO, scenarioInfo);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go back to ScenarioFragment, do nothing
                finish();
            }
        });
        initScenario();
    }

    private void onFabPressed() {
        Intent intent = new Intent(AddScenarioActivity.this, ColorSelectActivity.class);
        startActivityForResult(intent, 1);
    }
    private void initScenario() {

        for (int i = 0; i < 4; i++) {
            View view;
            if(i==0){
                view = mInflater.inflate(R.layout.add_scenario_zdy_item,
                        linear, false);
            }else{
                view = mInflater.inflate(R.layout.add_scenario_item,
                        linear, false);
            }

            linear.addView(view);
        }
    }
}
