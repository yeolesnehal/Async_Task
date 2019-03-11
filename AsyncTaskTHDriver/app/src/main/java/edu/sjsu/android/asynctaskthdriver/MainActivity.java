package edu.sjsu.android.asynctaskthdriver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView tempVal,humidityVal,activityVal,logResult;
    EditText sensorviewReadings;
    Integer tempReading, humidityReading, activityReading, sParam;
    Button generate, cancel;
    Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempVal = (TextView) findViewById(R.id.temperatureView);
        humidityVal = (TextView) findViewById(R.id.humidityView);
        activityVal = (TextView) findViewById(R.id.activityView);
        sensorviewReadings = (EditText) findViewById(R.id.enterReadings);
        logResult = (TextView) findViewById(R.id.resultLog);
        generate = (Button)  findViewById(R.id.generate);
        cancel = (Button) findViewById(R.id.cancel);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sParam = Integer.parseInt(sensorviewReadings.getText().toString());
                Log.i("Values",sParam.toString());
                MyTask myTask = new MyTask();
                myTask.execute(sParam);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<Integer, Integer, Void> {
        String logReport = "";
        Integer count = 1;

        @Override
        protected Void doInBackground(Integer... sParam) {
            while(count <= sParam[0]){
                tempReading = r.nextInt((100 - 25) + 1) + 25;
                humidityReading = r.nextInt((100 - 40) + 1) + 40;
                activityReading = r.nextInt((500 - 1) + 1) + 1;
                publishProgress(count);
                count++;
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.wtf("Logging", e.getMessage());
                }
            }
            return null;
        }
        protected void onProgressUpdate(Integer... count) {
            super.onProgressUpdate();
            String result = "\nOutput" + count[0].toString() + ":\n" +
                    "Temperature : " + tempReading.toString() + " F\n" +
                    "Humidity : " + humidityReading.toString() + "%\n" +
                    "Activity : " + activityReading.toString();
            logReport = logReport + result;
            tempVal.setText(String.valueOf(tempReading) + " F");
            humidityVal.setText(String.valueOf(humidityReading) + "%");
            activityVal.setText(String.valueOf(activityReading));
            logResult.setMovementMethod(new ScrollingMovementMethod());
            logResult.setText(logReport);
            Log.i("Result progress update",result);
        }
    }
}
