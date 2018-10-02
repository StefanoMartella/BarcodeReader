package stefano_martella.barcodereader.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import stefano_martella.barcodereader.R;
import stefano_martella.barcodereader.roomdatabase.Item;

public class ItemAdapter extends BaseAdapter {

    private List<Item> data;

    public ItemAdapter(List<Item> items){
        if(items != null) data = items;
        else data = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Item getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if( convertView == null ) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.adapter_item, parent, false);
            holder.name = convertView.findViewById(R.id.adapter_item_name);
            holder.code = convertView.findViewById(R.id.adapter_item_code);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(getItem(position).getName());
        holder.code.setText(getItem(position).getCode());
        return convertView;
    }

    public static class ViewHolder{

        TextView name, code;

    }
}


