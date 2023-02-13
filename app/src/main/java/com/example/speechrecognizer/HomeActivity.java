package com.example.speechrecognizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.loadinganimation.LoadingAnimation;

import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    private static final int SPEECH_REQUEST_CODE = 0;
    private SpeechRecognizer speechRecognizer;
    TextView txt;
    LoadingAnimation loadingAnim;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txt = findViewById(R.id.textView);
        loadingAnim = findViewById(R.id.loadingAnim);
        loadingAnim.setVisibility(View.INVISIBLE);
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
       startSpeechRecognition();
    }

    private void startSpeechRecognition() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                loadingAnim.setVisibility(View.VISIBLE);
                loadingAnim.setProgressVector(getDrawable(com.example.loadinganimation.R.drawable.bouncy_balls));
                loadingAnim.setTextViewVisibility(true);
                loadingAnim.setTextStyle(true);
                loadingAnim.setTextColor(Color.WHITE);
                loadingAnim.setTextSize(12F);
                loadingAnim.setTextMsg("Start Speaking");
                loadingAnim.setEnlarge(3);
            }

            @Override
            public void onBeginningOfSpeech() {
                loadingAnim.setTextMsg("Listening");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                loadingAnim.setVisibility(View.INVISIBLE);
                txt.setText("ERROR!!");
            }

            @Override
            public void onResults(Bundle results) {
                loadingAnim.setVisibility(View.INVISIBLE);
                List<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (result != null && result.size() > 0) {
                    String spokenText = "Hello "+result.get(0);
                    txt.setText(spokenText);
                    t1.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null,null);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                loadingAnim.setVisibility(View.INVISIBLE);
                List<String> result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (result != null && result.size() > 0) {
                    String spokenText = "Hello "+result.get(0);
                    txt.setText(spokenText);
                    t1.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null,null);
                }
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
            // Add other required methods
        });

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        speechRecognizer.startListening(intent);
    }

    private void stopSpeechRecognition() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.cancel();
            speechRecognizer.destroy();
        }
    }


}