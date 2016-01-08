package italo.com.br.teste.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import io.realm.RealmResults;
import italo.com.br.teste.R;
import italo.com.br.teste.model.Geoname;

public class ListViewAdapter extends ArrayAdapter<Geoname> {

    private Context context;

    public ListViewAdapter(Context context, RealmResults<Geoname> list) {
        super(context, R.layout.adapter_listview, list);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_listview, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.textViewListView);
        TextView textView2 = (TextView) rowView.findViewById(R.id.textViewListView2);
        textView.setText(getItem(position).getToponymName());
        textView2.setText(String.valueOf(getItem(position).getPopulation()));
        rowView.setTag(getItem(position).getWikipedia());
        return rowView;
    }

}
