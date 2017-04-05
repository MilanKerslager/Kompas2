package cz.kerslager.android.kompas;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    ImageView imageViewArrow;
    TextView textViewStupne;
    private static SensorManager sensorManager;
    private Sensor sensor;
    private float aktualnipozice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageViewArrow = (ImageView) findViewById(R.id.imageViewArrow);
        textViewStupne = (TextView) findViewById(R.id.textViewStupne);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int stupne = Math.round(event.values[0]);
        stupne-=90; // korekce směru šipky (naše míří doprava, tj. otočit o -90 stupnu)
        textViewStupne.setText(Integer.toString(stupne)+(char) 0x00B0);
        RotateAnimation rotate = new RotateAnimation(
                aktualnipozice,
                -stupne,
                Animation.RELATIVE_TO_SELF, 0.5f, // rotace kolem středu obrázku X
                Animation.RELATIVE_TO_SELF, 0.5f  // rotace kolem středu obrázku Y
        );
        rotate.setDuration(1000);  // délka animace otáčení
        rotate.setFillAfter(true); // otočit a zůstat v nové pozici
        imageViewArrow.startAnimation(rotate); // otočíme obrázkem
        aktualnipozice = -stupne;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
