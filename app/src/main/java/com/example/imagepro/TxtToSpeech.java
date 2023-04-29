package com.example.imagepro;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imagepro.databinding.ActivityTxtToSpeechBinding;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.Locale;

public class  TxtToSpeech extends AppCompatActivity {

    int tolanguageCode = -1;
    TextToSpeech textToSpeech;
    String translatedText;
    Locale languageT2S = Locale.ENGLISH;
    String finalText;
    ProgressDialog progressDialog;

    ActivityTxtToSpeechBinding activityTxtToSpeechBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTxtToSpeechBinding = ActivityTxtToSpeechBinding.inflate(getLayoutInflater());
        setContentView(activityTxtToSpeechBinding.getRoot());

        finalText = getIntent().getStringExtra("finaltxt");

        activityTxtToSpeechBinding.finalText.setText(finalText);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Translating Text...");
        progressDialog.setMessage("This may take some time during first time translating to a particular language :)");
        progressDialog.setCancelable(false);

        Spinner dropdown = findViewById(R.id.spinner1);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(TxtToSpeech.this, R.array.languages, R.layout.custon_drop_down_text);
        adapter.setDropDownViewResource(R.layout.custon_drop_down_text);
        dropdown.setAdapter(adapter);

        activityTxtToSpeechBinding.spinner1.setOnItemSelectedListener(new toSpinnerClass());

        activityTxtToSpeechBinding.speak.setOnClickListener(view -> {
//            Log.d("pinky", Arrays.toString(Locale.getAvailableLocales()));
            textToSpeech = new TextToSpeech(TxtToSpeech.this, i -> {
                if (i == TextToSpeech.SUCCESS) {
                    int res = textToSpeech.setLanguage(languageT2S);
                    if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(TxtToSpeech.this, "Language Not Supported", Toast.LENGTH_SHORT).show();
                    } else {
                        textToSpeech.setSpeechRate(0.9f);
                        textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, null);
                        Toast.makeText(TxtToSpeech.this, "Playing Translated Text", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(TxtToSpeech.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                }
            });
        });


        activityTxtToSpeechBinding.translateBtn.setOnClickListener(view -> {
            activityTxtToSpeechBinding.speak.setVisibility(View.GONE);
            translateText(finalText);
        });
    }

    class toSpinnerClass implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            String text = parent.getItemAtPosition(position).toString();
            tolanguageCode = getLanguageCode(text);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private void translateText(String input) {

        progressDialog.show();
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(FirebaseTranslateLanguage.EN)
                .setTargetLanguage(tolanguageCode)
                .build();

        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();


        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(unused -> translator.translate(input)
                .addOnSuccessListener(s -> {
                    progressDialog.dismiss();
                    translatedText = s;
                    activityTxtToSpeechBinding.translatedText.setText(s);
                    activityTxtToSpeechBinding.speak.setVisibility(View.VISIBLE);
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(TxtToSpeech.this, "Fail to Translate" + e.getMessage(), Toast.LENGTH_SHORT).show();
                })).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(TxtToSpeech.this, "Fail to Download Language Model" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    public int getLanguageCode(String language) {
        int languageCode = FirebaseTranslateLanguage.HI;
        switch (language) {

            case "Arabic":
                languageCode = FirebaseTranslateLanguage.AR;
                languageT2S = new Locale("ar_AE");
                break;
            case "Bengali":
                languageCode = FirebaseTranslateLanguage.BN;
                languageT2S = new Locale("bn_IN");
                break;
            case "German":
                //
                languageCode = FirebaseTranslateLanguage.DE;
                languageT2S = new Locale("de_AT");
                break;
            case "English":
                //
                languageCode = FirebaseTranslateLanguage.EN;
                languageT2S = Locale.US;
                break;
            case "Spanish":
                //
                languageCode = FirebaseTranslateLanguage.ES;
                languageT2S = new Locale("es_ES");
                break;
            case "French":
                //
                languageCode = FirebaseTranslateLanguage.FR;
                languageT2S = new Locale("fr_CA");
                break;
            case "Gujarati":
                languageCode = FirebaseTranslateLanguage.GU;
                languageT2S = new Locale("gu_IN");
                break;
            case "Hindi":
                languageCode = FirebaseTranslateLanguage.HI;
                languageT2S = new Locale("hi_IN");
                break;
            case "Italian":
                //
                languageCode = FirebaseTranslateLanguage.IT;
                languageT2S = new Locale("it_IT");
                break;
            case "Japanese":
                //
                languageCode = FirebaseTranslateLanguage.JA;
                languageT2S = new Locale("ja_JP");
                break;
            case "Kannada":
                //
                languageCode = FirebaseTranslateLanguage.KN;
                languageT2S = new Locale("kn_IN");
                break;
            case "Korean":
                //
                languageCode = FirebaseTranslateLanguage.KO;
                languageT2S = new Locale("ko_KR");
                break;
            case "Marathi":
                //
                languageCode = FirebaseTranslateLanguage.MR;
                languageT2S = new Locale("mr_IN");
                break;
            case "Malay":
                languageCode = FirebaseTranslateLanguage.MS;
                languageT2S = new Locale("ml_IN");
                break;

            case "Russian":
                //
                languageCode = FirebaseTranslateLanguage.UR;
                languageT2S = new Locale("ru_RU");
                break;
            case "Tamil":
                //
                languageCode = FirebaseTranslateLanguage.TA;
                languageT2S = new Locale("ta_IN");
                break;
            case "Telugu":
                //
                languageCode = FirebaseTranslateLanguage.TE;
                languageT2S = new Locale("te_IN");
                break;
            case "Urdu":
                //
                languageCode = FirebaseTranslateLanguage.UR;
                languageT2S = new Locale("ur-PK");
                break;
            default:
                languageCode = 0;
        }
        return languageCode;
    }
}