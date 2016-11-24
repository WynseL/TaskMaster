package com.mclinica.asynctorx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mclinica.taskmaster.TaskMaster;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1
        new TaskMaster<String>() {

            @Override
            protected void onStart() {

            }

            @Override
            protected String onProcess() {
                String hex = "";
                for (int i = 0; i < 1000; i++) {
                    hex += i % 2 == 0 ? "0" : "1";
                    if (hex.length() == 60) {
                        hex = "";
                    }
                }

                return hex;
            }

            @Override
            protected void onEnd(String s) {

            }
        };

        //2
        new SampleTaskMaster().execute();
    }

    private class SampleTaskMaster extends TaskMaster<String> {

        @Override
        protected void onStart() {

        }

        @Override
        protected String onProcess() {
            String hex = "";
            for (int i = 0; i < 1000; i++) {
                hex += i % 2 == 0 ? "0" : "1";
                if (hex.length() == 60) {
                    hex = "";
                }
            }

            return hex;
        }

        @Override
        protected void onEnd(String s) {

        }
    }
}
