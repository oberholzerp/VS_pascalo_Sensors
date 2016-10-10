package ch.ethz.inf.vs.a1.pascalo.sensors.vs_pascalo_sensors;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class SensorActivity extends AppCompatActivity implements SensorEventListener, GraphContainer {

    private LineGraphSeries<DataPoint> series [];

    // Holds the last 100 y-values of each series in the graph
    private float[][] data;
    // Used to treat the array as ring
    private int data_end_pointer;

    private SensorManager mSensorManager;
    private Sensor sensor;

    private int sensorType;

    private long uptime;

    private SensorTypesImpl STI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        STI = new SensorTypesImpl();

        series = new LineGraphSeries [STI.getNumberValues(sensorType)];

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
        data = new float[STI.getNumberValues(sensorType)][];
        for (int i = 0; i < STI.getNumberValues(sensorType); i++) {
            series [i] = new LineGraphSeries<DataPoint>();
            data[i] = new float[100];
        }
        data_end_pointer = 0;

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(false);
        graph.getViewport().setMaxX(15.0);
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMaxY(20.0);
        graph.getViewport().setMinY(-20.0);

        graph.getGridLabelRenderer().setVerticalAxisTitle(STI.getUnitString(sensor.getType()));


        series[0].setColor(Color.RED);
        if (STI.getNumberValues(sensorType) > 1) {
            series[1].setColor(Color.GREEN);
        }
        if (STI.getNumberValues(sensorType) > 2) {
            series[2].setColor(Color.BLUE);
        }

        for (int i = 0; i < STI.getNumberValues(sensorType); i++) {
            graph.addSeries(series[i]);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Copy values because the event is not owned by the application
        float[] valuesCopy = event.values.clone();

        if (uptime == 0) {
            uptime = event.timestamp;
        }

        double x = (double) ((event.timestamp - uptime))/1000000000.0;
        this.addValues(x, valuesCopy);

        TextView xAxisValue = (TextView) findViewById(R.id.xAxisValue);
        xAxisValue.setText(Float.toString(valuesCopy[0]) + " " + STI.getUnitString(sensorType));

        if (STI.getNumberValues(sensorType) > 1) {
            //set y value to textfield
            TextView yAxisValue = (TextView) findViewById(R.id.yAxisValue);
            yAxisValue.setText(Float.toString(valuesCopy[1]) + " " + STI.getUnitString(sensorType));

            //set z value to textfield
            TextView zAxisValue = (TextView) findViewById(R.id.zAxisValue);
            zAxisValue.setText(Float.toString(valuesCopy[2]) + " " + STI.getUnitString(sensorType));
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
        for (int i= 0; i<STI.getNumberValues(sensorType); i++) {
            series[i].appendData(new DataPoint(xIndex, values[i]), true, 1000);
            data[i][data_end_pointer] = values[i];
        }
        if (++data_end_pointer >= 100) {
            data_end_pointer = 0;
        }
    }

    @Override
    public float[][] getValues() {
        float[][] result = new float[STI.getNumberValues(sensorType)][];
        for (int i = 0; i < STI.getNumberValues(sensorType); i++){
            result[i] = new float[100];
            for (int j = 0; j < 100; j++){
                result[i][j] = data[i][(data_end_pointer + 1 + j) % 100];
            }
        }
        return result;
    }
}
