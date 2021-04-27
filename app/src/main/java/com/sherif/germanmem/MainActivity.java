package com.sherif.germanmem;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements TTSEngineListner {

    TextView germanWord;
    TextView englishWord;

    Button nextWord;
    Button prevWord;

    TextView wordcount;
    SeekBar seekBar3;
    String responseText;
    JSONObject thousandwords;
    JSONArray thousandwordsarray;
    int wordnumber=1;
    private TTSEngine engine;


    private void initTTS() {
        engine = new TTSEngine(getBaseContext(),this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        wordcount = findViewById(R.id.wordCount);
        germanWord =findViewById(R.id.german_word);
        englishWord =findViewById(R.id.english_word);
        nextWord = findViewById(R.id.nextWord);
        prevWord = findViewById(R.id.prevWord);
        germanWord = findViewById(R.id.german_word);
        seekBar3 = findViewById(R.id.seekBar3);
        initTTS();


        RequestQueue requestQueue = Volley.newRequestQueue(this);
JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://134.122.84.249/webservice1.php", null, new Response.Listener<JSONObject>() {
    @Override
    public void onResponse(JSONObject response) {

      Log.e("Rest Response",response.toString());
        responseText=response.toString();
        thousandwords=response;
        try {
            thousandwordsarray = thousandwords.toJSONArray(thousandwords.names());
        } catch (JSONException e) {
            e.printStackTrace();
        }
//       germanWord.setText( keys.next().toString());
    }
}, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("Rest Response",error.toString());

    }
});

requestQueue.add(jsonObjectRequest);





        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                wordnumber=progress;
                justdoit();
                System.out.println(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        nextWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                justdoit();
                seekBar3.setProgress(wordnumber);
                wordnumber++;


            }


        });

        prevWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                justdoit();
                seekBar3.setProgress(wordnumber);
                wordnumber--;


            }


        });




        germanWord.setTextKeepState("German Word");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Word is saved successfully", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public void onInitFailed() {
    }

    @Override
    public void onInitSuccess() {

    }

    @Override
    public void onPronounceFinish() {
       // changePronounceButtonState(true);
    }

    @Override
    public void onPronounceStart() {
    }
    @Override
    public void onLanguageDataRequired() {

    }

    @Override
    public void onWillDownloadLanguage() {

    }

    @Override
    public void onLanguageDownloadFinished() {

    }


    public void justdoit(){
        try {
            String mydata =  thousandwordsarray.get(wordnumber).toString();
            Pattern pattern1 = Pattern.compile("german\":\"(.+?)\"");
            Pattern pattern2 = Pattern.compile("english\":\"(.+?)\"");

            Matcher matcher1 = pattern1.matcher(mydata);
            Matcher matcher2 = pattern2.matcher(mydata);

            if (matcher1.find())
            {
                if (matcher2.find()) {
                    germanWord.setText("German: " + matcher1.group(0).substring(9).substring(0, matcher1.group(0).substring(9).length() - 1));
                    englishWord.setText("English: " + matcher2.group(0).substring(10).substring(0, matcher2.group(0).substring(10).length() - 1));

                    engine.speak(matcher1.group(0).substring(9).substring(0, matcher1.group(0).substring(9).length() - 1));
                    wordcount.setText("Word "+wordnumber + " out of 1000");
                   // Log.e(matcher1.group(0).substring(9),matcher1.group(0).substring(9));

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent i = new Intent(this,Settings.class);
            startActivity(i);

            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
