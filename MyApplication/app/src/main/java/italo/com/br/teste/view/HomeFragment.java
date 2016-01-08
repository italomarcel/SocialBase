package italo.com.br.teste.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;

import io.realm.Realm;
import italo.com.br.teste.R;
import italo.com.br.teste.adapter.ListViewAdapter;
import italo.com.br.teste.model.Geoname;

public class HomeFragment extends ListFragment {
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Realm realm = Realm.getInstance(getActivity());
        if (!realm.getTable(Geoname.class).isEmpty()) {
            setListAdapter(new ListViewAdapter(getActivity(), realm.allObjects(Geoname.class)));
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    WebViewFragment fragment = new WebViewFragment();
                    fragment.setUrl(view.getTag().toString());
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
                }
            });
        } else {
            setEmptyText(getActivity().getString(R.string.empty));
        }

    }
}
