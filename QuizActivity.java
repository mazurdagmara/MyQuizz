package com.example.myquizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_IN_MILLIS = 20000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQustionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewPytanie;
    private TextView textViewWynik;
    private TextView textViewNumerPytania;
    private TextView textViewKategorie;
    private TextView textViewTrudność;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rbA;
    private RadioButton rbB;
    private RadioButton rbC;
    private RadioButton rbD;
    private Button buttonCheck;

    private ColorStateList textColorRb;
    private ColorStateList textColorCountDown;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private ArrayList<Question> questionList;
    private int numerPytania;
    private int pytaniaWszystkie;
    private Question obecnePytanie;

    private int score;
    private boolean answered;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewPytanie = findViewById(R.id.text_view_question);
        textViewWynik = findViewById(R.id.text_view_score);
        textViewNumerPytania = findViewById(R.id.text_view_question_count);
        textViewKategorie = findViewById(R.id.text_view_category);
        textViewTrudność = findViewById(R.id.text_view_difficulty);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rbA = findViewById(R.id.radio_button1);
        rbB = findViewById(R.id.radio_button2);
        rbC = findViewById(R.id.radio_button3);
        rbD = findViewById(R.id.radio_button4);
        buttonCheck = findViewById(R.id.button_confirm_next);

        textColorRb = rbA.getTextColors();
        textColorCountDown = textViewCountDown.getTextColors();

        //wyświetlenie kategorii oraz trudności na górze ekranu
        Intent intent = getIntent(); //IDkategorii, nazwa, poziom trudności z MainActivity
        int categoryID = intent.getIntExtra(MainActivity.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);
        String difficulty = intent.getStringExtra(MainActivity.EXTRA_DIFFICULTY);

        textViewKategorie.setText("Kategoria: " + categoryName);
        textViewTrudność.setText("Poziom trudności: " + difficulty);

        if(savedInstanceState == null) {
            QuizDBHelper dbHelper = QuizDBHelper.getInstance(this); //inicjalizacja dbHelpera
            questionList = dbHelper.getQuestion(categoryID, difficulty); //filtracja pytań
            pytaniaWszystkie = questionList.size();
            Collections.shuffle(questionList); //wymieszanie

            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            pytaniaWszystkie = questionList.size();
            numerPytania = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            obecnePytanie = questionList.get(numerPytania - 1); //bo program numeruje pytania od 0, a my questionCounter zaczęliśmy od 1
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if(!answered){
                startCountDown(); //ale zaczyna od wartości zapisanej w timeLeftInMillis, czyli odliczanie jest kontynuowane
            } else{
                updateCountDownText();
                showSolution();
            }
        }
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(rbA.isChecked() || rbB.isChecked() || rbC.isChecked() || rbD.isChecked()){
                        checkAnswer();
                    } else{
                        Toast.makeText(QuizActivity.this, "Wybierz odpowiedź", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion(){
        rbA.setTextColor(textColorRb);
        rbB.setTextColor(textColorRb);
        rbC.setTextColor(textColorRb);
        rbD.setTextColor(textColorRb);
        rbGroup.clearCheck();

        if(numerPytania < pytaniaWszystkie){
            obecnePytanie = questionList.get(numerPytania);

            textViewPytanie.setText(obecnePytanie.getQuestion()); //wyświetlanie pytania
            rbA.setText(obecnePytanie.getOdpA()); //wyświetlanie odpowiedzi
            rbB.setText(obecnePytanie.getOdpB());
            rbC.setText(obecnePytanie.getOdpC());
            rbD.setText(obecnePytanie.getOdpD());

            numerPytania++;
            textViewNumerPytania.setText("Pytanie: " + numerPytania + "/" + pytaniaWszystkie);
            answered = false;
            buttonCheck.setText("Sprawdź");

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();

        } else{
            finishQuiz();
        }

    }

    private void startCountDown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }

    private void updateCountDownText(){
        int minutes = (int) (timeLeftInMillis / 1000) / 60 ;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted); //wyświetlanie czasu

        if(timeLeftInMillis < 5000){
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorCountDown);
        }
    }

    private void checkAnswer(){
        answered = true; //zaznaczona odpowiedź

        countDownTimer.cancel();

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId()); //zwraca ID zaznaczonego rb i zapisuje w rbSelected
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        if(answerNr == obecnePytanie.getPoprOdp()){ //jeśli wybrana = prawidłowej
            score++; //zwiększ wynik
            textViewWynik.setText(("Wynik: " + score));
        }

        showSolution(); //wyświetlanie prawidłowego rozwiązania
    }

    private void showSolution(){
        rbA.setTextColor(Color.RED); //wszystkie czerwone
        rbB.setTextColor(Color.RED);
        rbC.setTextColor(Color.RED);
        rbD.setTextColor(Color.RED);

        switch (obecnePytanie.getPoprOdp()){
            case 1:
                rbA.setTextColor(Color.GREEN);
                textViewPytanie.setText("Odpowiedź A jest poprawna");
                break;
            case 2:
                rbB.setTextColor(Color.GREEN);
                textViewPytanie.setText("Odpowiedź B jest poprawna");
                break;
            case 3:
                rbC.setTextColor(Color.GREEN);
                textViewPytanie.setText("Odpowiedź C jest poprawna");
                break;
            case 4:
                rbD.setTextColor(Color.GREEN);
                textViewPytanie.setText("Odpowiedź D jest poprawna");
                break;
        }

        if(numerPytania < pytaniaWszystkie){ //zmiana tekstu na przycisku dotycząca jego działania
            buttonCheck.setText("Następne pytanie");
        } else {
            buttonCheck.setText("Zakończ");
        }

    }

    private void finishQuiz(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() { //naciskanie wstecz
        if(backPressedTime + 2000 > System.currentTimeMillis()){ //jeśli minęło mniej niż 2s od pierwszego naciśnięcia wstecz
            finishQuiz(); //wychodzi i zapisuje wynik
        } else {
            Toast.makeText(this, "Naciśnij jeszcze raz aby zakończyć", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() { //kiedy zakończymy quiz zakończymy też odliczanie
        super.onDestroy();
        if(countDownTimer != null){ //upewnienie się że odlicza
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) { //przywraca stan przed "zabiciem" działania
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE,  score);
        outState.putInt(KEY_QUESTION_COUNT, numerPytania);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}