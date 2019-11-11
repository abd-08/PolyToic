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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Model.Cellule;
import Model.Ligne;
import Model.Matrice;

import static android.content.Context.MODE_PRIVATE;

public class MyJSONFile {
    public static final String DB_NAME = "dbjson.json";
    private Context _context;
    private JSONArray _mydatas;

    public MyJSONFile(Context context) {
        this._context = context;
        this.openData();
    }

    /**
     * Ouvre le fichier JSON
     */
    public JSONArray openData() {
        String bddJSon = null;
        try { //Si le fichier bddjson existe on l'ouvre
            FileInputStream fileInputStream = _context.openFileInput(DB_NAME);
            int size = fileInputStream.available();
            byte[] buffer = new byte[size];
            fileInputStream.read(buffer);
            fileInputStream.close();
            bddJSon = new String(buffer, "UTF-8");
            _mydatas = new JSONArray(bddJSon);
            return new JSONArray(bddJSon);
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
                _mydatas = new JSONArray(bddJSon);
                return new JSONArray(bddJSon);
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

    public void saveData(JSONArray datas) throws IOException {
        Log.i("saveJSON", datas.toString());
        FileOutputStream fileOut = null;
        fileOut = _context.openFileOutput(DB_NAME, MODE_PRIVATE);
        OutputStreamWriter os = new OutputStreamWriter(fileOut);
        BufferedWriter writer = new BufferedWriter(os);
        writer.write(datas.toString());
        writer.flush();
        writer.close();
    }

    public void saveData() throws IOException {
        saveData(_mydatas);
    }



    public ArrayList<String> getCategories() throws JSONException {
        return getItems(_mydatas);

    }


    public ArrayList<String> getItems(JSONArray jsonArray) throws JSONException {
        ArrayList<String> myResultList = new ArrayList<>();
        for (int i=0; i<jsonArray.length(); i++){
            JSONObject currentJsonObject = jsonArray.getJSONObject(i);
            myResultList.add(currentJsonObject.getString("name"));
        }
        return myResultList;
    }

    public ArrayList<String> getChoices(JSONObject category) throws JSONException {
        return getItems(category.getJSONArray("data"));
    }


    /**
     * Remplace un objet dans le objet jsonArray
     * @param jsonArray Le JSONArray qu'on souhaite modifier
     * @param position La position de l'élément qu'on souhaite remplacer
     * @param object Le nouveau objet
     * @throws JSONException
     */
    public void update(JSONArray jsonArray, int position, JSONObject object) throws JSONException {
        jsonArray.put(position, object);
    }

    /**
     * Supprime une catégorie
     * @param position La position de la catégorie
     * @throws JSONException
     */
    public void remove(int position) throws JSONException {
        _mydatas = remove(_mydatas, position);
    }

    /**
     * Supprime un choix
     * @param parentPosition La position de sa catégorie
     * @param position Le position du choix
     */
    public void remove(int parentPosition, int position) throws JSONException {
        _mydatas.getJSONObject(parentPosition).put("data", remove(_mydatas.getJSONObject(parentPosition).getJSONArray("data"), position));
    }

    /**
     * Supprime un objet dans le tableau objet donné en paramètre
     * @param jsonArray Le tableau objet qu'on souhaite modifier
     * @param position La position de l'élément a effacé
     * @return JSONArray
     * @throws JSONException
     */
    public JSONArray remove(JSONArray jsonArray, int position) throws JSONException {
        JSONArray jsonArrayResult = new JSONArray();
        for (int i=0; i<jsonArray.length(); i++){
            if (position != i){
                JSONObject jo = jsonArray.getJSONObject(i);
                jsonArrayResult.put(jo);
            }
        }
        return jsonArrayResult;
    }

    public JSONArray getMydatas() {
        return _mydatas;
    }

    public void setMydatas(JSONArray _mydatas) {
        this._mydatas = _mydatas;
    }

    public void setMydatas(JSONObject jo,int position) {
        try {
            this._mydatas.put(position,jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






    //fonction qui transforme une matrice en jsonobjet
    public JSONObject transformer(Matrice m){
        JSONObject jsonObject=new JSONObject();
        JSONArray Liste=new JSONArray();

        //pour chaque ligne
        for (int i=0;i<m.getLigne();i++){
            JSONArray ligne=new JSONArray();

            //Pour chaque cellule
            for (int j=0;j<m.getColonne();j++){
                ligne.put(m.getCellulePosition(i,j).isSelected());
            }
            Liste.put(ligne);
        }

        try{
            jsonObject.put("nom",m.getNom());
            jsonObject.put("tableau",Liste);
        }
        catch
        (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONArray Transforme(ArrayList<Matrice> Liste){
        JSONObject jo = new JSONObject();
        JSONArray ja=new JSONArray();
        JSONObject jom = new JSONObject();
        for(int i=0;i<Liste.size();i++){
            jom = this.transformer(Liste.get(i)) ;
            ja.put(jom);
        }
        try {
            jo.put("data",ja);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ja;
    }



    //focntion qui transforme un json objet en matrice
    public Matrice getMatrice(JSONObject jsonObject) throws JSONException{
        //on crée un nouvelle matrice avec le nom utilisé.

        Ligne ligne;
        JSONArray line;

        //on récupère le tableau de la matrice
        JSONArray array=new JSONArray(jsonObject.getJSONArray("tableau").toString());
        Matrice matrice = new Matrice(jsonObject.getString("nom"));

        //on parcours les ligne de la matrice
        for(int i=0;i<array.length();i++){
            line=array.getJSONArray(i);
            ligne=new Ligne();

            //on parcours les colonne de la matrice
            for(int j=0;j<line.length();j++){
                Cellule c= new Cellule(i,j,line.getBoolean(j));
                ligne.add(c);
            }
            matrice.getList().add(ligne);

        }
        return matrice;
    }


    public ArrayList<Matrice> getListeMatrices() throws JSONException{

        ArrayList<Matrice> List=new ArrayList<>();
        JSONArray JA=_mydatas;
        for(int i=0;i<JA.length();i++){
            JSONObject jo=new JSONObject(JA.getJSONObject(i).toString());
            List.add(this.getMatrice(jo));
        }
        return List;
    }



    public Matrice getMatrice(int position) throws JSONException {
        JSONArray JA=_mydatas;
        Matrice matrice;

        JSONObject jo;
        jo = new JSONObject(JA.getJSONObject(position).toString());
        matrice=getMatrice(jo);

        return matrice;


    }
}
