package livs.code.frontoffice.view.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Room;


public class SearchRoomReadyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Room> arrayList;
    private int view;

    public SearchRoomReadyAdapter(Context context, ArrayList<Room> arrayList, int view) {
        this.context = context;
        this.arrayList = arrayList;
        this.view = view;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        SearchRoomReadyAdapter.ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            row = inflater.inflate(view, parent, false);

            holder = new SearchRoomReadyAdapter.ViewHolder();
            holder.tv_VFname = row.findViewById(R.id.text1);
            row.setTag(holder);

        } else {
            holder = (SearchRoomReadyAdapter.ViewHolder) row.getTag();
        }

        final Room item = arrayList.get(position);
        holder.tv_VFname.setText(item.getRoomType() +" "+item.getRoomCode());
        return row;
    }
    class ViewHolder {
        TextView tv_VFname;
    }
}
