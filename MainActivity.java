package com.example.myquizz;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ = 1; //bo nie mamy żadnej innej 'Activity'
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore"; //klucz pod którym zapiszemy najlepszy wynik w sharedPreferences

    private TextView textViewNajwyszyWynik;
    private Spinner spinnerKategorie;
    private Spinner spinnerTrudnosc;

    private int najwyzszywynik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewNajwyszyWynik = findViewById(R.id.text_view_highscore);
        spinnerKategorie = findViewById(R.id.spinner_category);
        spinnerTrudnosc = findViewById(R.id.spinner_difficulty);

        loadCategories();
        loadDifficultyLevels();
        loadHighscore(); //ładuje największy wynik z SP i wstawia do textViewHighscore

        Button buttonStartQuiz = findViewById(R.id.button_start_quiz); //tworzenie obiektu klasy Button; przypisanie do zmiennej przycisku
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() { //naciśnięcie
            @Override
            public void onClick(View v) {startQuiz();}  //przywołanie metody startQuiz
        });

    }

    private void startQuiz(){
        Category selectedCategory = (Category) spinnerKategorie.getSelectedItem() ; // wybrana kategoria
        int categoryID = selectedCategory.getId(); //dostajemy ID kategorii z tego objektu
        String categoryName = selectedCategory.getNazwa(); //dostajemy nazwę z tego objektu
        String difficulty = spinnerTrudnosc.getSelectedItem().toString(); //wybór trudności i zapisanie do difficulty

        Intent intent = new Intent(MainActivity.this, QuizActivity.class); //otwieranie klasy QuizActivity
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID); //wysłanie informacji o kategorii oraz o trudności do QuizActivity
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        startActivityForResult(intent, REQUEST_CODE_QUIZ); //wynik z QuizActivity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_QUIZ){
            if(resultCode == RESULT_OK){
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0); //przypisanie wyniku z QuizActivity
                if(score > najwyzszywynik){
                    updateHighscore(score);
                }
            }
        }
    }

    private void loadCategories(){
        QuizDBHelper dbHelper = QuizDBHelper.getInstance(this);
        List<Category> categories = dbHelper.getAllCategories();

        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerKategorie.setAdapter(adapterCategories);
    }

    private void loadDifficultyLevels(){
        String[] difficultyLevels = Question.getAllDifficultyLevels(); //static method -> przywołujemy tę metodę w klasie nie w objekcie (Q nie q)

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrudnosc.setAdapter(adapterDifficulty); //wypełnienie spinnera trudnościami
    }

    private void loadHighscore(){ //po odpaleniu aplikacji wyświetla najlepszy ostatni wynik
        SharedPreferences sp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        najwyzszywynik = sp.getInt(KEY_HIGHSCORE, 0);
        textViewNajwyszyWynik.setText("Najlepszy wynik: " + najwyzszywynik);
    }

    private void updateHighscore(int najlepszywynikNowy){
        najwyzszywynik = najlepszywynikNowy; //przypisanie nowego najwyższego wyniku
        textViewNajwyszyWynik.setText("Najlepszy wynik: " + najwyzszywynik);

        SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit(); //zapisywanie wartości w SP
        editor.putInt(KEY_HIGHSCORE, najwyzszywynik); //zapisywanie największego wyniku
        editor.apply();
    }
}