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



public class SensorView extends AppCompatActivity implements SensorEventListener, GraphContainer {

    private LineGraphSeries<DataPoint> series [];

    private SensorManager mSensorManager;
    private Sensor sensor;

    private int sensorType;

    private long uptime;

    private SensorTypesImpl STI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        series = new LineGraphSeries [3];

        uptime = 0;

        STI = new SensorTypesImpl();

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

        for (int i = 0; i < STI.getNumberValues(sensorType); i++) {
            graph.addSeries(series[i]);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        SensorEvent eventCopy = event;
        if (uptime == 0) {
            uptime = eventCopy.timestamp;
        }

        double x = (double) ((eventCopy.timestamp - uptime))/1000000000.0;
        this.addValues(x, eventCopy.values);

        TextView xAxisValue = (TextView) findViewById(R.id.xAxisValue);
        xAxisValue.setText(Float.toString(eventCopy.values[0]) + " " + STI.getUnitString(sensorType));

        if (STI.getNumberValues(sensorType) > 1) {
            //set y value to textfield
            TextView yAxisValue = (TextView) findViewById(R.id.yAxisValue);
            yAxisValue.setText(Float.toString(eventCopy.values[1]) + " " + STI.getUnitString(sensorType));

            //set z value to textfield
            TextView zAxisValue = (TextView) findViewById(R.id.zAxisValue);
            zAxisValue.setText(Float.toString(eventCopy.values[2]) + " " + STI.getUnitString(sensorType));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
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


    @Override
    public void addValues(double xIndex, float[] values) {
        for (int i= 0; i<values.length; i++)
        series[i].appendData(new DataPoint(xIndex, values[i]), true, 1000);
    }

    @Override
    public float[][] getValues() {
        // TODO
        return new float[0][];
    }
}
