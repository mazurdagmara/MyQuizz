package com.example.myquizz;

//kontener na zmienne potrzebne do db

public final class QuizContract {

    public static class CategoriesTable {
        public static final String _ID = "_id";
        public static final String NAZWA_TABELII = "Kategorie";
        public static final String NAZWA_KATEGORII = "Nazwa";
    }

    public static class QuestionsTable { //BC - ponieważ daje dostęp do tego interfejsu który zawiera ID
        public static final String _ID = "_id";
        public static final String NAZWA_TABELII = "Pytania"; //nazwa tabeli
        public static final String TRESC_PYTANIA = "Tresc"; //nazwy kolumn
        public static final String ODP_A = "OdpA";
        public static final String ODP_B = "OdpB";
        public static final String ODP_C = "OdpC";
        public static final String ODP_D = "OdpD";
        public static final String POPR_ODP = "PoprOdp";
        public static final String POZIOM_TRUDNOSCI = "Poziom_Trudnosci";
        public static final String ID_KATEGORII = "ID_kategorii";


    }
}
