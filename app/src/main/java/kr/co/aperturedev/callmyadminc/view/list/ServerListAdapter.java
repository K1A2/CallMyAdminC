package kr.co.aperturedev.callmyadminc.view.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.aperturedev.callmyadminc.R;

/**
 * Created by jckim on 2017-10-29.
 */

public class ServerListAdapter extends BaseAdapter {

    private ArrayList<ServerListItem> serverListItems = new ArrayList<ServerListItem>();
    private String svName;
    private String svAdmin;
    private String svPeople;

    public ServerListAdapter() {

    }

    @Override
    public int getCount() {
        return serverListItems.size();
    }

    @Override
    public Object getItem(int i) {
        return serverListItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_server, parent, false);
        }

        TextView svNameView = (TextView)convertView.findViewById(R.id.list_svname);
        TextView svAdminView = (TextView)convertView.findViewById(R.id.list_svadmin);
        TextView svPeopleView= (TextView)convertView.findViewById(R.id.list_svpeople);

        ServerListItem listItem = serverListItems.get(pos);

        svName = listItem.getSvName();
        svAdmin = listItem.getSvAdmin();
        svPeople = listItem.getSvPeople();

        svNameView.setText(svName);
        svAdminView.setText(svAdmin);
        svPeopleView.setText(svPeople);

        return convertView;
    }

    public void addItem (String name, String admin, String people) {
        ServerListItem listItem = new ServerListItem();

        listItem.setSvName(name);
        listItem.setSvAdmin(admin);
        listItem.setSvPeople(people);

        serverListItems.add(listItem);
    }

    public void remove (int position) {
        serverListItems.remove(position);
        DataChange();
    }

    public void clear () {
        serverListItems.clear();
        DataChange();
    }

    public void DataChange () {
        this.notifyDataSetChanged();
    }
}
