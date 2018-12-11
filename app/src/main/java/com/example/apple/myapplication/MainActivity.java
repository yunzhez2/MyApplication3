package com.example.apple.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}
//add dependencies to your class

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button search = findViewById(R.id.button2);
        final EditText name = findViewById(R.id.editText);
        String word = name.getText().toString();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = name.getText().toString();

                new CallbackTask().execute(dictionaryEntries(word));
            }
        });
    }


    private String dictionaryEntries(String word) {
        final String language = "en";

        final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }


    //in android calling network requests on the main thread forbidden by default
    //create class to do async job
    private class CallbackTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            //TODO: replace with your own app id and app key
            final String app_id = "9cd26602";
            final String app_key = "629224c1ad521164e0d7415a79afedb4";
            try {
                URL url = new URL(params[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept","application/json");
                urlConnection.setRequestProperty("app_id",app_id);
                urlConnection.setRequestProperty("app_key",app_key);

                // read the output from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                return stringBuilder.toString();

            }
            catch (Exception e) {
                e.printStackTrace();
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            JsonParser jp = new JsonParser();

            JsonObject jo = jp.parse(result).getAsJsonObject();

            //String message = jo.get("result").getAsString();





            JsonArray address = jo.get("results").getAsJsonArray();
            JsonElement first = address.get(0);
            JsonArray lexical = first.getAsJsonObject().get("lexicalEntries").getAsJsonArray();
            first = lexical.get(0);
            JsonArray entries =  first.getAsJsonObject().get("entries").getAsJsonArray();
            first = entries.get(0);
            JsonArray senses =  first.getAsJsonObject().get("senses").getAsJsonArray();
            first = senses.get(0);
            JsonArray definitions =  first.getAsJsonObject().get("definitions").getAsJsonArray();
            first = definitions.get(0);
            String haha = first.getAsString();

            //get("lexicalEntries").getAsJsonObject().get("entries").getAsJsonObject().get("senses").getAsJsonObject().get("definitions").getAsString();








            //System.out.println(result);
            TextView text = findViewById(R.id.TextView);

            text.setText(haha);
            //text.setText(result);

        }
    }
}
