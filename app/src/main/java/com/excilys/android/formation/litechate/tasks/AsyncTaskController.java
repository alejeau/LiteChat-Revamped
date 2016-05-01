package com.excilys.android.formation.litechate.tasks;

/**
 * Created by loic on 29/04/2016.
 */
public interface AsyncTaskController<T> {
    void onPreExecute();
    void onPostExecute(T success);
}
