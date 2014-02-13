package org.deejdev.database.sqlitecursorloader;

import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SQLiteCursorLoader extends AsyncTaskLoader<Cursor> {
    private final SQLiteOpenHelper sqLiteOpenHelper;
    private final boolean distinct;
    private final String table;
    private final String[] columns;
    private final String selection;
    private final String[] selectionArgs;
    private final String groupBy;
    private final String having;
    private final String orderBy;
    private final String limit;
    private Cursor cursor;

    public SQLiteCursorLoader(Context context, SQLiteOpenHelper sqLiteOpenHelper, boolean distinct,
                              String table, String[] columns, String selection,
                              String[] selectionArgs, String groupBy, String having,
                              String orderBy, String limit) {
        super(context);
        this.sqLiteOpenHelper = sqLiteOpenHelper;
        this.distinct = distinct;
        this.table = table;
        this.columns = columns;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.groupBy = groupBy;
        this.having = having;
        this.orderBy = orderBy;
        this.limit = limit;
    }

    /* Runs on a worker thread */
    @Override
    public Cursor loadInBackground() {
        Cursor cursor = sqLiteOpenHelper.getReadableDatabase()
                .query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if (cursor != null) {
            // Ensure the cursor window is filled
            cursor.getCount();
        }
        return cursor;
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        Cursor oldCursor = this.cursor;
        this.cursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    /**
     * Starts an asynchronous load of the list data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     * <p/>
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        if (cursor != null) {
            deliverResult(cursor);
        }
        if (takeContentChanged() || cursor == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = null;
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        writer.print(prefix);
        writer.print("distinct=");
        writer.println(distinct);
        writer.print(prefix);
        writer.print("table=");
        writer.println(table);
        writer.print(prefix);
        writer.print("columns=");
        writer.println(Arrays.toString(columns));
        writer.print(prefix);
        writer.print("selection=");
        writer.println(selection);
        writer.print(prefix);
        writer.print("selectionArgs=");
        writer.println(Arrays.toString(selectionArgs));
        writer.print(prefix);
        writer.print("groupBy=");
        writer.println(groupBy);
        writer.print(prefix);
        writer.print("having=");
        writer.println(having);
        writer.print(prefix);
        writer.print("orderBy=");
        writer.println(orderBy);
        writer.print(prefix);
        writer.print("limit=");
        writer.println(limit);
        writer.print(prefix);
        writer.print("cursor=");
        writer.println(cursor);
    }
}
