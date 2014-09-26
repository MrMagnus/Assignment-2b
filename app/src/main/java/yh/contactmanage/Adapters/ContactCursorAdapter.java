package yh.contactmanage.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import yh.contactmanage.Data.Contracts.ContactContract;
import yh.contactmanage.R;

/**
 * Created by Magnus on 2014-09-16.
 */
public class ContactCursorAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    private ViewHolder viewHolder;

    //Constructor
    public ContactCursorAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Instead of creating a new list item for every contact this method will create as many items
     * that will fit on the action and then re-use the list view by giving them new values
     * @param context
     * @param cursor
     * @param parent
     * @return
     */

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){

        viewHolder = new ViewHolder();

        View view = inflater.inflate(R.layout.row_contacts, parent, false);
        viewHolder.imageHolder = (ImageView)view.findViewById(R.id.listPortraitIv);
        viewHolder.nameHolder = (TextView)view.findViewById(R.id.listNameTv);

        view.setTag(viewHolder);
        return view;
    }

    /**
     * Takes the database row values and puts them into listItems
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor){

        viewHolder = (ViewHolder) view.getTag();

        viewHolder.nameHolder.setText(cursor.getString((cursor.getColumnIndex(ContactContract.Contact.COLUMN_NAME_NAME))));
        Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(ContactContract.Contact.COLUMN_NAME_URL))).
                placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder).into(viewHolder.imageHolder);

    }

    public static class ViewHolder{
        public TextView nameHolder;
        public ImageView imageHolder;
    }
}