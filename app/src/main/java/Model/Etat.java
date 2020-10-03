package Model;

import java.util.Objects;

public class Etat {
    public static final int DEBUT_RESULT = 0;
    public static final int EN_COURS_RESULT = 5 ;
    public static final int CORRECTION_RESULT = 20;
    public static final int AFFICHE_RESULTAT = 50 ;
    private int result_code =DEBUT_RESULT;
    private boolean active;

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }


    public Etat(int result_code) {

        this.result_code = result_code;
        this.active = false;
    }


    public static String Affiche(int result_code) {
        switch (result_code){

            case EN_COURS_RESULT:
                return "encours";

            case DEBUT_RESULT:
                return "début";

            case CORRECTION_RESULT:
            case AFFICHE_RESULTAT:
                return "terminé";

            default:return "autre";
        }

    }
}
