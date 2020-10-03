package Helper;

import android.content.Context;
import android.util.Log;

import com.example.polytoic.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import Model.Questionnaire;

import static android.content.Context.MODE_PRIVATE;

public class ConfigurationSave {


    public static final String DB_NAME = "configuration.json";
    private Context _context;
    private JSONObject _mydatas;

    public ConfigurationSave(Context context) {
        this._context = context;
        this.openData();
    }

    /**
     * Ouvre le fichier JSON
     */
    public JSONObject openData() {
        String bddJSon = null;
        try { //Si le fichier bddjson existe on l'ouvre
            Log.e("MAIN " + DB_NAME, "le fichier est ouvert" );
            FileInputStream fileInputStream = _context.openFileInput(DB_NAME);
            int size = fileInputStream.available();
            byte[] buffer = new byte[size];
            fileInputStream.read(buffer);
            fileInputStream.close();
            bddJSon = new String(buffer, "UTF-8");
            _mydatas = new JSONObject(bddJSon);
            Log.i("messe",_mydatas.toString());
            return new JSONObject(bddJSon);
        } catch (FileNotFoundException e) { //Sinon on l'a crée
            //e.printStackTrace();
            Log.e("MAIN " + DB_NAME, "n'éxiste pas ou " + e.getMessage());
            InputStream inputStream = _context.getResources().openRawResource(R.raw.dbjson);
            int size = 0;
            try {
                size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();
                bddJSon = new String(buffer, "UTF-8");
                //_mydatas = new JSONObject();
                _mydatas = new JSONObject(bddJSon);
                _mydatas.put("tri_croissant_decroissant",R.id.radio_croissant);
                _mydatas.put("tri_mode",R.id.radio_nom);
                _mydatas.put("filtre_mode",R.id.radio_filtre_aucun);
                return new JSONObject(bddJSon);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveData(JSONObject datas) {
        Log.i("saveJSON", datas.toString());
        FileOutputStream fileOut = null;
        try {
            fileOut = _context.openFileOutput(DB_NAME, MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter os = new OutputStreamWriter(fileOut);
        BufferedWriter writer = new BufferedWriter(os);
        try {
            writer.write(datas.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveData(){
        saveData(_mydatas);
    }





    public void update( String cle ,int value ) {
        try {
            _mydatas.put(cle,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        saveData();
    }




    public JSONObject getMydatas() {
        return _mydatas;
    }

    public void setMydatas(JSONObject _mydatas) {
        this._mydatas = _mydatas;
    }

    public void setMydatas(JSONArray exam , JSONArray entrain , JSONArray fini ) {
        try {
            this._mydatas.put("examen",exam);
            this._mydatas.put("entrainement",entrain);
            this._mydatas.put("termine",fini);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getType(String type){
        JSONArray jsonArray=new JSONArray();
        try {
            jsonArray = (JSONArray) this._mydatas.get(type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("GET TYPE TEST ", jsonArray.toString());
        return  jsonArray;
    }

    public Questionnaire getQuestionnaire(String type , int position)  {
        JSONArray JA= null;
        JSONObject jo;
        Questionnaire q=null;

        try {
            JA = _mydatas.getJSONArray(type);
            jo = new JSONObject( );
            jo = (JSONObject) JA.get(position);

            if (type == "examen"){
                q = new Questionnaire(jo.getString("nom"));
            }
            else  q = new Questionnaire(jo.getString("nom") , jo.getJSONArray("listening").length());
            q.chargeQuestionnaire(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return q;
    }




}
