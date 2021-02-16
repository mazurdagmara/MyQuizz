package com.example.myquizz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.myquizz.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myquiz.db";

    private static QuizDBHelper instance;

    private SQLiteDatabase myquizdb; //stworzenie odniesienia do bazy danych

    private QuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static synchronized QuizDBHelper getInstance(Context context){
        if(instance == null){
            instance = new QuizDBHelper(context.getApplicationContext()); //QDBH wykorzystywany w całej aplikacji, nie jednorazowo
        } return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase myquizdb) {
        this.myquizdb = myquizdb;

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.NAZWA_TABELII + "( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.NAZWA_KATEGORII + " TEXT " +
                ")";

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.NAZWA_TABELII + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.TRESC_PYTANIA + " TEXT, " +
                QuestionsTable.ODP_A + " TEXT, " +
                QuestionsTable.ODP_B + " TEXT, " +
                QuestionsTable.ODP_C + " TEXT, " +
                QuestionsTable.ODP_D + " TEXT, " +
                QuestionsTable.POPR_ODP + " INTEGER, " +
                QuestionsTable.POZIOM_TRUDNOSCI + " TEXT, " +
                QuestionsTable.ID_KATEGORII + " INTEGER, " +
                "FOREIGN KEY(" + QuestionsTable.ID_KATEGORII + ") REFERENCES " +
                CategoriesTable.NAZWA_TABELII + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        myquizdb.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        myquizdb.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.NAZWA_TABELII);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.NAZWA_TABELII);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true); //włączenie ograniczenia klucza obcego, aby dodawać pytania do kategorii które istnieją
    }

    private void fillCategoriesTable(){
        Category c1 = new Category("Programowanie");
        addCategory(c1);
        Category c2 = new Category("Geografia");
        addCategory(c2);
        Category c3 = new Category("Matematyka");
        addCategory(c3);
    }

    private void addCategory(Category category){
        ContentValues cv = new ContentValues(); //przechowywaie wartości
        cv.put(CategoriesTable.NAZWA_KATEGORII, category.getNazwa()); //dodanie wartości
        myquizdb.insert(CategoriesTable.NAZWA_TABELII, null, cv);
    }

    private void fillQuestionsTable(){
        Question q1 = new Question("Język C to język",
                "A: strukturalny", "B: funkcyjny", "C: logiczny", "D: żadne z powyższych", 1,
                Question.POZIOM_LATWY, Category.PROGRAMOWANIE);
        addQuestion(q1);
        Question q2 = new Question("Char to typ danych ",
                "A: całkowity", "B: znakowy", "C: logiczny", "D: zmiennoprzecinkowy", 2,
                Question.POZIOM_LATWY, Category.PROGRAMOWANIE);
        addQuestion(q2);
        Question q3 = new Question("Typ danych zmiennoprzecinkowy to",
                "A: void", "B: float", "C: char", "D: int", 4,
                Question.POZIOM_LATWY, Category.PROGRAMOWANIE);
        addQuestion(q3);
        Question q4 = new Question("Ile gwiazdek zostanie wyświetlonych? \nfor(int k=1;k<=6;k++){\n   for(int j=1;j<=4;j++)" +
                "\n         cout << \"*\";}",
                "A: 6", "B: żadna", "C: 24", "D: 4", 3,
                Question.POZIOM_SREDNI, Category.PROGRAMOWANIE);
        addQuestion(q4);
        Question q5 = new Question("Co zostanie wyświetlone? \nfor(int k=1;k<=10;k++){\n    for(int j=1;j<=5;j++)\n       " +
                "    cout<<\"$\"<<endl;}",
                "A: 50 linii, każda z jednym znakiem $", "B: 5 linii, każda z jednym znakiem $",
                "C: 10 linii, każda z jednym znakiem $", "D: 50 znaków $ w jednej linii", 1,
                Question.POZIOM_SREDNI, Category.PROGRAMOWANIE);
        addQuestion(q5);
        Question q6 = new Question("Z jakiego języka \"wyrósł\" Python?",
                "A: Basic", "B: ABC", "C: Ruby", "D: C", 2,
                Question.POZIOM_TRUDNY, Category.PROGRAMOWANIE);
        addQuestion(q6);
        Question q7 = new Question("Jak inaczej nazwiemy procedurę?",
                "A: Źródło", "B: Program", "C: Kod", "D: Podprogram", 4,
                Question.POZIOM_TRUDNY, Category.PROGRAMOWANIE);
        addQuestion(q7);
        Question q8 = new Question("Jakiego kraju stolicą jest Ryga?",
                "A: Łotwy", "B: Litwy", "C: Estonii", "D: Białorusi", 1,
                Question.POZIOM_LATWY, Category.GEOGRAFIA);
        addQuestion(q8);
        Question q9 = new Question("Który kraj nie sąsiaduje z Niemcami?",
                "A: Szwajcaria", "B: Holandia", "C: Luksemburg", "D: Liechtenstein", 4,
                Question.POZIOM_LATWY, Category.GEOGRAFIA);
        addQuestion(q9);
        Question q10 = new Question("Wybierz spośród podanych nazwy krajów mających gęstość zaludnienia powyżej 200 osób na km kw.",
                "A: Liban, Mongolia, Sri Lanka", "B: Australia, Filipiny, Kanada",
                "C: Filipiny, Liban, Sri Lanka", "D: Filipiny, Kanada, Mongolia", 3,
                Question.POZIOM_SREDNI, Category.GEOGRAFIA);
        addQuestion(q10);
        Question q11 = new Question("Rzeka San na długości 55 km stanowi granicę między:",
                "A: Polską i Białorusią", "B: Polską i Ukrainą",
                "C: Polską i Słowacją", "D: Polską i Niemcami", 2,
                Question.POZIOM_SREDNI, Category.GEOGRAFIA);
        addQuestion(q11);
        Question q12 = new Question("Wielkie Góry Wododziałowe to potężny łańcuch górski, leżący w:",
                "A: Australii", "B: Nowej Zelandii",
                "C: Ameryce Północnej", "D: Afryce", 1,
                Question.POZIOM_TRUDNY, Category.GEOGRAFIA);
        addQuestion(q12);
        Question q13 = new Question("Który z tych regionów leży w Niemczech?",
                "A: Szlezwik Północny", "B: Antwerpia",
                "C: Langwedocja", "D: Turyngia", 4,
                Question.POZIOM_TRUDNY, Category.GEOGRAFIA);
        addQuestion(q13);
        Question q14 = new Question("Co jest wynikiem działania 2+2*2?",
                "A: 6", "B: 2",
                "C: 8", "D: Działanie jest niepoprawnie zapisane", 1,
                Question.POZIOM_LATWY, Category.MATEMATYKA);
        addQuestion(q14);
        Question q15 = new Question("Które z poniższych powinno być wykonywane jako pierwsze w działaniu?",
                "A: Działanie w nawiasie", "B: mnożenie/dzielenie",
                "C: dodawanie/odejmowanie", "D: potęgowanie", 1,
                Question.POZIOM_LATWY, Category.MATEMATYKA);
        addQuestion(q15);
        Question q16 = new Question("a=2E-12 oraz b=1E-20. Iloraz a/b wynosi:",
                "A: 0,5E-32", "B: 2E8",
                "C: 4E-8", "D: 1E-12", 2,
                Question.POZIOM_SREDNI, Category.MATEMATYKA);
        addQuestion(q16);
        Question q17 = new Question("Miejscem zerowym funkcji y=4x+2 jest:",
                "A: 2", "B: -2",
                "C: 0,5", "D: -0,5", 4,
                Question.POZIOM_SREDNI, Category.MATEMATYKA);
        addQuestion(q17);
        Question q18 = new Question("Który z tych wzorów jest ZAWSZE prawdziwy?",
                "A: sin(a)=cos(a)", "B: tg(a)=-tg(a)",
                "C: sin(-a)=-sin(a)", "D: cos(-a)=-cos(a)", 3,
                Question.POZIOM_TRUDNY, Category.MATEMATYKA);
        addQuestion(q18);
        Question q19 = new Question("Co jest dodatnie w drugiej ćwiartce układu współrzędnych?",
                "A: sin(a), cos(a), tg(a), ctg(a)", "B: sin(a)",
                "C: tg(a), ctg(a)", "D: cos(a)", 2,
                Question.POZIOM_TRUDNY, Category.MATEMATYKA);
        addQuestion(q19);


    }

    private void addQuestion(Question question){
        ContentValues cv = new ContentValues(); //przechowywaie wartości
        cv.put(QuestionsTable.TRESC_PYTANIA, question.getQuestion()); //dodawanie wartości
        cv.put(QuestionsTable.ODP_A, question.getOdpA());
        cv.put(QuestionsTable.ODP_B, question.getOdpB());
        cv.put(QuestionsTable.ODP_C, question.getOdpC());
        cv.put(QuestionsTable.ODP_D, question.getOdpD());
        cv.put(QuestionsTable.POPR_ODP, question.getPoprOdp());
        cv.put(QuestionsTable.POZIOM_TRUDNOSCI, question.getTrudnosc());
        cv.put(QuestionsTable.ID_KATEGORII, question.getIDkategorii());
        myquizdb.insert(QuestionsTable.NAZWA_TABELII, null, cv);
    }

    public List<Category> getAllCategories(){
        List<Category> categoryList = new ArrayList<>(); //tworzenie nowej listy typu kategoria i inicjalizujemy ją jako nowa ArrayList
        myquizdb = getReadableDatabase();
        Cursor c = myquizdb.rawQuery("SELECT * FROM " + CategoriesTable.NAZWA_TABELII, null);

        if(c.moveToFirst()){
            do{
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setNazwa(c.getString(c.getColumnIndex(CategoriesTable.NAZWA_KATEGORII)));
                categoryList.add(category);
            } while(c.moveToNext());
        }
        c.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestion(){
        ArrayList<Question> questionList = new ArrayList<>(); //arrayList - tablica o zmiennym rozmiarze
        myquizdb = getReadableDatabase();
        Cursor c = myquizdb.rawQuery("SELECT * FROM " + QuestionsTable.NAZWA_TABELII, null);
        if(c.moveToFirst()){
            do{
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable.TRESC_PYTANIA)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.TRESC_PYTANIA)));
                question.setOdpA(c.getString(c.getColumnIndex(QuestionsTable.ODP_A)));
                question.setOdpB(c.getString(c.getColumnIndex(QuestionsTable.ODP_B)));
                question.setOdpC(c.getString(c.getColumnIndex(QuestionsTable.ODP_C)));
                question.setOdpD(c.getString(c.getColumnIndex(QuestionsTable.ODP_D)));
                question.setPoprOdp(c.getInt(c.getColumnIndex(QuestionsTable.POPR_ODP)));
                question.setTrudnosc(c.getString(c.getColumnIndex(QuestionsTable.POZIOM_TRUDNOSCI)));
                question.setIDkategorii(c.getInt(c.getColumnIndex(QuestionsTable.ID_KATEGORII)));
                questionList.add(question);
            }while(c.moveToNext());
        }

        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestion(int categoryID, String difficulty){
        ArrayList<Question> questionList = new ArrayList<>();
        myquizdb = getReadableDatabase();

        String selection = QuestionsTable.ID_KATEGORII + " = ? " +
                " AND " + QuestionsTable.POZIOM_TRUDNOSCI + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};

        Cursor c = myquizdb.query(QuestionsTable.NAZWA_TABELII, null, selection, selectionArgs, null, null, null);

        if(c.moveToFirst()){
            do{
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable.TRESC_PYTANIA)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.TRESC_PYTANIA)));
                question.setOdpA(c.getString(c.getColumnIndex(QuestionsTable.ODP_A)));
                question.setOdpB(c.getString(c.getColumnIndex(QuestionsTable.ODP_B)));
                question.setOdpC(c.getString(c.getColumnIndex(QuestionsTable.ODP_C)));
                question.setOdpD(c.getString(c.getColumnIndex(QuestionsTable.ODP_D)));
                question.setPoprOdp(c.getInt(c.getColumnIndex(QuestionsTable.POPR_ODP)));
                question.setTrudnosc(c.getString(c.getColumnIndex(QuestionsTable.POZIOM_TRUDNOSCI)));
                question.setIDkategorii(c.getInt(c.getColumnIndex(QuestionsTable.ID_KATEGORII)));
                questionList.add(question);
            }while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}
