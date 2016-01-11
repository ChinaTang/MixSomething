package mixsomething.tang.di.asynchronization;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;

/**
 * Created by tangdi on 2016/1/8.
 * android 的异步任务，用于下载或者处理耗时任务
 * 任务加载时将会出现dialog并且不可点击
 * 1.execute(Params... params)，执行一个异步任务，需要我们在代码中调用此方法，触发异步任务的执行。
 *2.onPreExecute()，在execute(Params... params)被调用后立即执行，一般用来在执行后台任务前对UI做一些标记。
 *3.doInBackground(Params... params)，在onPreExecute()完成后立即执行，用于执行较为费时的操作，此方法将接收输入参数和返回计算结果。在执行过程中可以调用publishProgress(Progress... values)来更新进度信息。
 *4.onProgressUpdate(Progress... values)，在调用publishProgress(Progress... values)时，此方法被执行，直接将进度信息更新到UI组件上。
 *5.onPostExecute(Result result)，当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，直接将结果显示到UI组件上。
 *在使用的时候，有几点需要格外注意：
 *1.异步任务的实例必须在UI线程中创建。
 *2.execute(Params... params)方法必须在UI线程中调用。
 *3.不要手动调用onPreExecute()，doInBackground(Params... params)，onProgressUpdate(Progress... values)，onPostExecute(Result result)这几个方法。
 *4.不能在doInBackground(Params... params)中更改UI组件的信息。
 *5.一个任务实例只能执行一次，如果执行第二次将会抛出异常。
 *
 * 该类默认实现下在开始时候出现一个dialog
 */

/**
 * 无法使用泛型来编写，所以功能有限
 */
abstract class Mixasynchronization extends AsyncTask<URL, Integer, String>{

    abstract MixasynchronizationInterface setTask();

    private ProgressDialog progressDialog;
    private int hasRead = 0;
    private Context context;
    private MixasynchronizationInterface mixasynchronizationInterface;

    public Mixasynchronization(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
    progressDialog = new ProgressDialog(context);

    }

    @Override
    protected String doInBackground(URL... params){
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values){

    }

    @Override
    protected void onPostExecute(String result) {
    }
}
