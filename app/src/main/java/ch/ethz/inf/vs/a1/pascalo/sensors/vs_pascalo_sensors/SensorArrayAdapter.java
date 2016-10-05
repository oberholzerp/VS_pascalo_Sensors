package ch.ethz.inf.vs.a1.pascalo.sensors.vs_pascalo_sensors;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.List;
import android.content.Context;
import android.widget.TextView;
import android.hardware.Sensor;

/**
 * Created by Joel on 05.10.2016.
 */

public class SensorArrayAdapter<T> extends ArrayAdapter<T> {

    private final LayoutInflater mInflater;

    private final Context mContext;

    private final int mResource;

    public SensorArrayAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        final TextView text;

        //no idea what this is about, took it from the superclass
        if (convertView == null) {
            view = mInflater.inflate(mResource, parent, false);
        } else {
            view = convertView;
        }

        //that if statement in the superclass is always true with this constructor
        text = (TextView) view;

        //do a getName instead of a toString here, that's the only real difference
        final Sensor item = (Sensor) getItem(position);
        text.setText(item.getName());

        return view;
    }
}

