package ch.ethz.inf.vs.a1.pascalo.sensors.vs_pascalo_sensors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.Iterator;
import java.lang.Iterable;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //edit list_view
        ListView list_view = (ListView) findViewById(R.id.sensor_list);
        list_view.setOnItemClickListener(this);

        //get all sensors
        SensorManager mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = mgr.getSensorList(Sensor.TYPE_ALL);


        ArrayAdapter<Sensor> array_adapter = new ArrayAdapter<Sensor>(this,
                android.R.layout.simple_list_item_1,
                sensors);

        list_view.setAdapter(array_adapter);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Sensor sensor = (Sensor) parent.getItemAtPosition(position);

        //toast notification with sensor name
        Toast.makeText(getApplicationContext(), sensor.getName(), Toast.LENGTH_SHORT).show();

        //start SensorView activity
        Intent intent = new Intent(this, SensorView.class);
        this.startActivity(intent);


    }
}
