package Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Questionnaire{
    int NOMBRE_QUESTION = 100;
    String nom;
    ArrayList<Question> listening;
    ArrayList<Question> listening_correction;
    ArrayList<Question> reading_correction;
    ArrayList<Question> reading;
    ArrayList<  ArrayList<String> > historique;
    Boolean est_termine;
    int mode ;
    int etat;
    int reading_resultat;
    int listening_resultat;



    SimpleDateFormat date;
    Long chronometre = Long.valueOf(0);

    public Questionnaire(String nom) {
        this.nom = nom;
        this.mode = Mode.EXAMEN;
        this.est_termine=false;
        this.reading_resultat=0;
        this.listening_resultat=0;
        this.listening = new ArrayList<>();
        this.listening_correction = new ArrayList<>();
        this.reading = new ArrayList<>();
        this.reading_correction = new ArrayList<>();
        this.historique = new ArrayList<>();

        for (int i = 0; i < NOMBRE_QUESTION; i++) {
            this.listening.add(new Question(i + 1));
            this.listening_correction.add(new Question(i + 1));
            this.reading.add(new Question(NOMBRE_QUESTION + i + 1));
            this.reading_correction.add(new Question(NOMBRE_QUESTION + i + 1));
        }
    }


    public Questionnaire(String nom, int nombre) {
        this.nom = nom;
        this.mode = Mode.ENTRAINEMENT;
        est_termine=false;
        this.reading_resultat=0;
        this.listening_resultat=0;
        this.NOMBRE_QUESTION = nombre;
        this.listening = new ArrayList<>();
        this.listening_correction = new ArrayList<>();
        for (int i = 0; i < nombre; i++) {
            this.listening.add(new Question(i + 1));
            this.listening_correction.add(new Question(i + 1));
        }
        this.reading = new ArrayList<>();
        this.reading_correction = new ArrayList<>();
        this.historique = new ArrayList<>();

        /*this.date = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());*/
    }

    public Long getChronometre() {
        return chronometre;
    }

    public void setChronometre(Long chronometre) {
        this.chronometre = chronometre;
    }

    public int size() {
        return this.listening.size();
    }


    public void reinitTest() {
        this.est_termine=false;
        ArrayList<String> reading_historique=new ArrayList<>();
        ArrayList<String> listening_historique=new ArrayList<>();


        for (int i = 0; i < reading.size(); i++) {
            reading_historique.add(reading.get(i).whoIs());
            reading.get(i).deselectAll();
        }
        for (int i = 0; i < listening.size(); i++) {
            listening_historique.add(listening.get(i).whoIs());
            listening.get(i).deselectAll();
        }

        this.historique.add(listening_historique);
        if (reading_historique.size()==NOMBRE_QUESTION) this.historique.add(reading_historique);
    }


    public void augmenter(int quantite) {
        Question q;
        for (int i = 0; i < quantite; i++) {
            q = new Question(this.listening.size() + i);
            this.listening.add(q);
            this.listening_correction.add(q);
        }
    }

    public void diminuer(int quantite) {
        int position = this.listening.size() - quantite;
        for (int i = 0; i < quantite; i++) {
            this.listening.remove(position);
            this.listening_correction.remove(position);
        }
    }


    public void modifier(String nom, int nombre) {
        this.nom = nom;
        int taille = this.listening.size();
        if (taille > nombre) diminuer(taille - nombre);
        if (taille < nombre) augmenter(nombre - taille);
    }


    public boolean isComplete(ArrayList<Question> liste) {
        for (int i = 0; i < liste.size(); i++) {
            if (!liste.get(i).isSelect()) return false;
        }
        return true;
    }


    public int nombreSelectionner(int state) {
        int res = 0;
        switch (state) {
            case Etat.EN_COURS_RESULT:
                for (int i = 0; i < this.listening.size(); i++) {
                    if (this.listening.get(i).isSelect()) res++;
                }
                for (int i = 0; i < this.reading.size(); i++) {
                    if (this.reading.get(i).isSelect()) res++;
                }
                return res;

            case Etat.CORRECTION_RESULT:
                for (int i = 0; i < this.listening_correction.size(); i++) {
                    if (this.listening_correction.get(i).isSelect()) res++;
                }
                for (int i = 0; i < this.reading_correction.size(); i++) {
                    if (this.reading_correction.get(i).isSelect()) res++;
                }
                return res;

            default:
                return 233322;
        }
    }


    public boolean correctionExiste() {
        for (int i = 0; i < this.listening_correction.size(); i++) {
            if (!this.listening_correction.get(i).isSelect()) return false;
        }
        for (int i = 0; i < this.reading_correction.size(); i++) {
            if (!this.reading_correction.get(i).isSelect()) return false;
        }
        return true;
    }

    public int getReading_resultat() {
        return reading_resultat;
    }

    public int getListening_resultat() {
        return listening_resultat;
    }

    public int Corriger() {
        this.est_termine = true;
        int res = 0;

        for (int i = 0; i < this.listening_correction.size(); i++) {
            if (this.listening_correction.get(i).isEqual(this.listening.get(i))) {
                this.listening.get(i).setCorrect(true);
                res++;
            }
        }

        this.listening_resultat=res;
        for (int i = 0; i < this.reading_correction.size(); i++) {
            if (this.reading_correction.get(i).isEqual(this.reading.get(i))) {
                this.reading.get(i).setCorrect(true);
                res++;
            }
        }

        this.reading_resultat=res-this.listening_resultat;

        return res;
    }



    public int getType(){
        if(this.reading.size()==NOMBRE_QUESTION && this.listening.size()==NOMBRE_QUESTION){
            return Mode.EXAMEN;
        }
        else if (this.reading.size()==0) return Mode.ENTRAINEMENT;
        else return Mode.AUTRE;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }
    public int getMode() {  return mode; }

    public void setMode(int mode) {  this.mode = mode; }

    public int getNOMBRE_QUESTION() {
        return this.listening.size() + this.reading.size();
    }

    public ArrayList<Question> getListening_correction() {
        return listening_correction;
    }

    public ArrayList<Question> getReading_correction() {
        return reading_correction;
    }

    public ArrayList<Question> getListening() {
        return listening;
    }

    public ArrayList<Question> getReading() {
        return reading;
    }


    //fonction qui transforme un arrayliste<String> en jsonarray
    public JSONArray transforme(ArrayList<String> liste){

        JSONArray jliste = new JSONArray();
        for (int i = 0; i < liste.size(); i++) {
            Log.i("QUESTION "+i,liste.get(i));
            jliste.put(liste.get(i));
        }
        Log.i("JLISTE",jliste.toString());
        return  jliste;
    }

    //fonction qui transforme une matrice en objet JSon
    public JSONObject transformer() {
        JSONObject jsonObject = new JSONObject();
        JSONArray listening = new JSONArray();
        JSONArray listening_correction = new JSONArray();
        JSONArray reading = new JSONArray();
        JSONArray reading_correction = new JSONArray();
        JSONArray historique = new JSONArray();

        for (int i = 0; i < this.listening.size(); i++) {
            listening.put(this.listening.get(i).whoIs());
            listening_correction.put(this.listening_correction.get(i).whoIs());
        }

        for (int j = 0; j < this.reading.size(); j++) {
            reading.put(this.reading.get(j).whoIs());
            reading_correction.put(this.reading_correction.get(j).whoIs());
        }

        for(int k=0; k < this.historique.size(); k++){
           historique.put( transforme(this.historique.get(k)) );
        }

        try {
            jsonObject.put("nom", this.nom);
            jsonObject.put("listening", listening);
            jsonObject.put("reading", reading);
            jsonObject.put("listening_correction", listening_correction);
            jsonObject.put("reading_correction", reading_correction);
            jsonObject.put("historique",historique);
            jsonObject.put("etat", this.etat);
            jsonObject.put("mode", this.mode);
            jsonObject.put("est_termine", this.est_termine);
            jsonObject.put("chronometre", this.chronometre);
            Log.i("jsonObject",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void chargeQuestionnaire(JSONObject jsonObject) {
        JSONArray listening = null;
        JSONArray listening_correction = null;
        JSONArray reading = null;
        JSONArray reading_correction = null;
        JSONArray historique = null;

        try {
            listening = new JSONArray(jsonObject.getJSONArray("listening").toString());
            listening_correction = new JSONArray(jsonObject.getJSONArray("listening_correction").toString());
            reading = new JSONArray(jsonObject.getJSONArray("reading").toString());
            reading_correction = new JSONArray(jsonObject.getJSONArray("reading_correction").toString());
            historique = new JSONArray(jsonObject.getJSONArray("historique").toString());
            this.est_termine = jsonObject.getBoolean("est_termine");
            this.etat = jsonObject.getInt("etat");
            this.mode = jsonObject.getInt("mode");
            this.chronometre = jsonObject.getLong("chronometre");
            this.nom = jsonObject.getString("nom");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String ch;
        for (int i = 0; i < listening.length(); i++) {
            try {
                ch = listening.get(i).toString();
                this.listening.get(i).selectReponse(ch);
                ch = listening_correction.get(i).toString();
                this.listening_correction.get(i).selectReponse(ch);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < reading.length(); i++) {
            try {
                ch = reading.get(i).toString();
                this.reading.get(i).selectReponse(ch);
                ch = reading_correction.get(i).toString();
                this.reading_correction.get(i).selectReponse(ch);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        this.historique = new ArrayList<>();
        for (int i = 0; i < historique.length(); i++) {
            try {
                JSONArray jsonArray = new JSONArray(historique.get(i).toString()) ;
                ArrayList<String> list = new ArrayList<>();
                for (int j = 0; j < jsonArray.length(); j++) {
                    try {
                        ch = reading.get(i).toString();
                        list.add(ch);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                this.historique.add(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
