package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.polytoic.R;
import java.util.List;
import Model.Questionnaire;

public class QuestionnaireAdapter extends BaseAdapter {

    //attribut
    private Context context;
    private List<Questionnaire> ItemList;
    private LayoutInflater inflater;

    public QuestionnaireAdapter(Context context ,List<Questionnaire> matriceItemList ){
        this.context=context;
        this.ItemList=matriceItemList;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ItemList.size();
    }

    @Override
    public Questionnaire getItem(int position) {
        return ItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_item_matrice,null);

        //recupération des information des item
        Questionnaire currentItem = getItem(position);
        final String itemName = currentItem.getNom();

        TextView itemNameView = convertView.findViewById(R.id.item_name);
        itemNameView.setText(itemName);

        return convertView;
    }



    }




