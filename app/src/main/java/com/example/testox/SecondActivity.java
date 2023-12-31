package com.example.testox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private boolean ipaVisible = false;
    private String ipaPronunciation;
    private TextView textViewShowIPA;
    private TextView textViewIpaPronunciation;
    private TextView textViewSynonyms; // Add TextView for synonyms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView textViewWord = findViewById(R.id.textViewWord);
        TextView textViewDefinition = findViewById(R.id.textViewDefinition);
        textViewShowIPA = findViewById(R.id.textViewShowIPA);
        textViewIpaPronunciation = findViewById(R.id.textViewIpaPronunciation);
        textViewSynonyms = findViewById(R.id.textViewSynonyms); // Initialize TextView for synonyms

        Intent intent = getIntent();
        String word = intent.getStringExtra("word");
        String definition = intent.getStringExtra("definition");
        String ipa = intent.getStringExtra("ipa");

        if (ipa != null && !ipa.isEmpty()) {
            ipaPronunciation = ipa;
            updateIpaVisibility();

            textViewShowIPA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleIpaVisibility();
                }
            });

        } else {
            textViewShowIPA.setText(getString(R.string.ipa_info_not_available));
        }

        if (word != null) {
            textViewWord.setText(word);
        }

        if (definition != null) {
            textViewDefinition.setText(definition);
        } else {
            textViewDefinition.setText(getString(R.string.definition_not_found));
        }

        // Check if synonyms are available and display them
        if (intent.hasExtra("synonyms")) {
            List<String> synonyms = intent.getStringArrayListExtra("synonyms");
            if (synonyms != null && !synonyms.isEmpty()) {
                StringBuilder synonymsText = new StringBuilder();
                for (String synonym : synonyms) {
                    synonymsText.append(synonym).append(", ");
                }
                // Remove the trailing comma and space
                synonymsText.setLength(synonymsText.length() - 2);

                // Use resource string with a placeholder to display synonyms
                String synonymsLabel = getString(R.string.synonyms_label, synonymsText.toString());
                textViewSynonyms.setText(synonymsLabel);
                textViewSynonyms.setVisibility(View.VISIBLE);
            } else {
                textViewSynonyms.setVisibility(View.GONE);
            }
        } else {
            textViewSynonyms.setText(getString(R.string.definition_not_found));
            textViewSynonyms.setVisibility(View.VISIBLE);
        }
    }

    private void toggleIpaVisibility() {
        ipaVisible = !ipaVisible;
        updateIpaVisibility();
    }

    private void updateIpaVisibility() {
        if (ipaVisible) {
            textViewIpaPronunciation.setText(ipaPronunciation);
            textViewIpaPronunciation.setVisibility(View.VISIBLE);
        } else {
            textViewIpaPronunciation.setVisibility(View.GONE);
        }
    }
}