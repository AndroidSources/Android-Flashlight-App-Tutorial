package com.androidsources.flashlight;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {

    Camera.Parameters params;
    ToggleButton toggle;
    private Camera camera;
    private boolean isFlashOn;
    private boolean flashAvailability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializing toggle button
        toggle = (ToggleButton) findViewById(R.id.toggleButton);

        //checking if therer is flash available
        flashAvailability = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        //If flash is not available show toast message
        if (!flashAvailability) {
            Toast.makeText(MainActivity.this, "Sorry, No flashlight found", Toast.LENGTH_LONG).show();
            return;
        }

        //initializing camera
        initCamera();

        //setting OnCheckedChangeListener for toggle
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //turn on the flash
                    flashON();
                } else {
                    if (isFlashOn) {
                        //turn off the flash
                        flashOFF();
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
    Initialize camera
     */
    private void initCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {

            }
        }
    }

   /*
   Making the flash ON
    */
    private void flashON() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;


        }

    }

   /*
   Making the flash OFF
    */
    private void flashOFF() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        flashOFF();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (flashAvailability && toggle.isChecked())
            flashON();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on starting the app get the camera params
        initCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

}
