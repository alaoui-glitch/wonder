package com.example.testox;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private List<Word> wordList;
    private Map<String, Word> wordMap; // Map to store Word objects by their word

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadWordDefinitions();

        Button searchButton = findViewById(R.id.buttonSearch);
        EditText wordEditText = findViewById(R.id.editTextWord);

        searchButton.setOnClickListener(view -> {
            String searchWord = wordEditText.getText().toString().trim();
            Word wordObj = searchWordObject(searchWord);

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("word", searchWord);

            if (wordObj != null) {
                String definition = searchWordDefinition(searchWord);
                String ipa = wordObj.getIpa();
                intent.putExtra("definition", definition);
                intent.putExtra("ipa", ipa);
            } else {
                intent.putExtra("definition", "Definition not found for this word.");
            }

            startActivity(intent);
        });
    }

    private void loadWordDefinitions() {
        try {
            InputStream inputStream = getAssets().open("dictionary.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            wordList = new ArrayList<>();
            wordMap = new HashMap<>(); // Initialize the wordMap

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String word = jsonObject.getString("word");
                String definition = jsonObject.getString("definition");
                String ipa = jsonObject.getString("ipa"); // Get IPA from JSON

                Word wordObj = new Word();
                wordObj.setWord(word);
                wordObj.setDefinition(definition);
                wordObj.setIpa(ipa); // Set IPA for Word object

                wordList.add(wordObj);
                wordMap.put(word, wordObj); // Add Word object to wordMap
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private Word searchWordObject(String searchWord) {
        return wordMap.get(searchWord);
    }

    private String searchWordDefinition(String searchWord) {
        for (Word word : wordList) {
            if (word.getWord().equalsIgnoreCase(searchWord)) {
                return word.getDefinition();
            }
        }
        return null;
    }
}

