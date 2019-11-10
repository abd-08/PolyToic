package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polytoic.R;

import java.util.List;
import Model.Matrice;


public class MatriceItemAdapter extends BaseAdapter {

    //attribut
    private Context context;
    private List<Matrice> matriceItemList;
    private LayoutInflater inflater;

    public MatriceItemAdapter(Context context ,List<Matrice> matriceItemList ){
        this.context=context;
        this.matriceItemList=matriceItemList;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return matriceItemList.size();
    }

    @Override
    public Matrice getItem(int position) {
        return matriceItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_item_matrice,null);

        //recupération des information des item
        Matrice currentItem = getItem(position);
        final String itemName = currentItem.getNom();
        final String dimension = currentItem.getLigne()+" * "+currentItem.getColonne();

        TextView itemNameView = convertView.findViewById(R.id.item_name);
        itemNameView.setText(itemName);

        TextView itemDimView = convertView.findViewById(R.id.item_dimension);
        itemDimView.setText(dimension);

        return convertView;
    }



}


