package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import Helper.Other;

import com.example.polytoic.R;

import java.util.ArrayList;
import java.util.List;

import Helper.Other;
import Model.Cellule;
import Model.Matrice;

public class CelluleItemAdapter extends BaseAdapter {
    private Context context;
    private List<Cellule> celluleList;
    private LayoutInflater inflater;
    private int colonne;
    private int ligne;


    public CelluleItemAdapter(Context context , List<Cellule> celluleList ,int colonne){
        this.context=context;
        this.celluleList=celluleList;
        this.colonne=colonne;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return celluleList.size();
    }

    @Override
    public Cellule getItem(int position) {
        return celluleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public void setCelluleList(List<Cellule> celluleList) {
        this.celluleList = celluleList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_cellule,null);

        //recupération des information des item
        Cellule currentItem = getItem(position);
        TextView textView = convertView.findViewById(R.id.cellule_nom);
        int ligne = Other.convertLigne(position,colonne);
        int column = Other.convertColonne(position,colonne);
        textView.setText(ligne+"."+Other.getAlphabet(column));


        if (currentItem.isSelected()) textView.setBackgroundColor(Color.argb(255, 255, 0, 0));
        else textView.setBackgroundColor(Color.argb(255, 155, 155, 155));


        return convertView;
    }



}
