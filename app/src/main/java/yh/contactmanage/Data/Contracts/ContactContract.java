package yh.contactmanage.Data.Contracts;

import android.provider.BaseColumns;

/**
 * Created by Magnus on 2014-09-23.
 */
public class ContactContract {

    public ContactContract(){}

    public static abstract class Contact implements BaseColumns {
        public final static String TABLE_NAME = "Contact";
        public final static String COLUMN_NAME_URL = "url";
        public final static String COLUMN_NAME_NAME = "name";
        public final static String COLUMN_NAME_AGE = "age";
        public final static String COLUMN_NAME_DESCRIPTION = "description";
    }
}
