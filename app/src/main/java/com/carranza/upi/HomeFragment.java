package com.carranza.upi;

import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    // protected ImageView ivIcon;
    // protected TextView tvItemName;

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    protected Button enableBluetooth;
    protected Button disableBluetooth;
    protected Button visibleBluetooth;

    private Button openAttendanceButton;
    private Button setAttendanceButton;

    public static final String IMAGE_RESOURCE_ID = "iconResourceID";
    public static final String ITEM_NAME = "itemName";

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        enableBluetooth = (Button) view.findViewById(R.id.enable_bluetooth_button);
        enableBluetooth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enable, 0);

                    Toast.makeText(getView().getContext(), "Enabled", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getView().getContext(), "It's already enabled!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        disableBluetooth = (Button) view.findViewById(R.id.disable_bluetooth_button);
        disableBluetooth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBluetoothAdapter.disable();
                Toast.makeText(getView().getContext(), "Disabled", Toast.LENGTH_SHORT).show();
            }
        });

        visibleBluetooth = (Button) view.findViewById(R.id.enable_bluetooth_visibility_button);
        visibleBluetooth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                enable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);

                startActivityForResult(enable, 0);
            }
        });

        openAttendanceButton = (Button) view.findViewById(R.id.action_open_attendance_button);
        setAttendanceButton = (Button) view.findViewById(R.id.action_set_attendance_button);

        if (User.getUser().getRol().equals("student")) {
            openAttendanceButton.setVisibility(View.GONE);
        } else { // admin or professor
            setAttendanceButton.setVisibility(View.GONE);
        }

        openAttendanceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new GetSubjectsAsyncTask().execute(Util.getHost() + "api/subject");
            }
        });

        return view;
    }

    private class GetSubjectsAsyncTask extends AsyncTask<String, Void, String> {

        private JSONArray subjects;
        private String mSelectedSubject;
        private ArrayAdapter<String> s = new ArrayAdapter<String>(
                getView().getContext(),
                android.R.layout.select_dialog_singlechoice);

        @Override
        protected String doInBackground(String... params) {
            return Util.requestGet(params[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                subjects = new JSONArray(result);
                for (int i = 0; i < subjects.length(); i++) {
                    JSONObject subject = subjects.getJSONObject(i);

                    s.add(subject.getString("iniciales"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
            builder.setTitle(R.string.title_dialog_select_subject)
                    .setSingleChoiceItems(s, -1,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSelectedSubject = s.getItem(which);
                                }
                            })
                    .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            new CreateAttendanceAsyncTask().execute(Util.getHost() + "api/attendance", mSelectedSubject);
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private class CreateAttendanceAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("end", "30")); // por defecto
            nameValuePairs.add(new BasicNameValuePair("tipo", "T")); // por defecto
            nameValuePairs.add(new BasicNameValuePair("iniciales", params[1]));
            nameValuePairs.add(new BasicNameValuePair("bMAC", "00:15:83:0C:BF:EB")); // por defecto

            return Util.requestPost(params[0], nameValuePairs);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getView().getContext(), "Attendance open", Toast.LENGTH_SHORT).show();
        }
    }
}
