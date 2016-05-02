package com.excilys.android.formation.chatlite_rev.tasks;

/**
 * Created by loic on 29/04/2016.
 */
public interface AsyncTaskController<T> {
    void onPreExecute();
    void onPostExecute(T success);
}
