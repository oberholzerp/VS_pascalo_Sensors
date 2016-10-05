package ch.ethz.inf.vs.a1.pascalo.sensors.vs_pascalo_sensors;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;



public class SensorView extends AppCompatActivity implements SensorEventListener {

    private LineGraphSeries<DataPoint> series [];

    private SensorManager mSensorManager;
    private Sensor sensor;

    private int sensorType;

    private long uptime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        series = new LineGraphSeries [3];

        uptime = 0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor__view);

        Intent myIntent = this.getIntent();
        String sensorName = myIntent.getStringExtra("sensorId");
        sensorType = myIntent.getIntExtra("sensorType", 0);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = mSensorManager.getDefaultSensor(sensorType);

        TextView text = (TextView) findViewById(R.id.sensor_name);
        text.setText(sensorName);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        series [0] = new LineGraphSeries<DataPoint>();
        series [1] = new LineGraphSeries<DataPoint>();
        series [2] = new LineGraphSeries<DataPoint>();

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(false);
        graph.getViewport().setMaxX(15.0);
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMaxY(20.0);
        graph.getViewport().setMinY(-20.0);




        series[0].setColor(Color.RED);
        series[1].setColor(Color.GREEN);
        series[2].setColor(Color.BLUE);
        graph.addSeries(series[0]);
        graph.addSeries(series[1]);
        graph.addSeries(series[2]);



    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (uptime == 0) {
            uptime = event.timestamp;
        }
        double x = (double) ((event.timestamp - uptime))/1000000000.0;
        series[0].appendData(new DataPoint(x, event.values[0]), true, 1000);
        series[1].appendData(new DataPoint(x, event.values[1]), true, 1000);
        series[2].appendData(new DataPoint(x, event.values[2]), true, 1000);

        //set x value to textfield
        TextView xAxisValue = (TextView) findViewById(R.id.xAxisValue);
        xAxisValue.setText("x-Axis: " + Float.toString(event.values[0]));

        //set y value to textfield
        TextView yAxisValue = (TextView) findViewById(R.id.yAxisValue);
        yAxisValue.setText("y-Axis: " + Float.toString(event.values[1]));

        //set z value to textfield
        TextView zAxisValue = (TextView) findViewById(R.id.zAxisValue);
        zAxisValue.setText("z-Axis: " + Float.toString(event.values[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


}
