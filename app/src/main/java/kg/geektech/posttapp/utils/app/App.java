package kg.geektech.posttapp.utils.app;

import android.app.Application;

import kg.geektech.posttapp.data.remote.PostApi;
import kg.geektech.posttapp.data.remote.RetrofitClient;

public class App extends Application {
    private RetrofitClient retrofitClient;
    public static PostApi api;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofitClient = new RetrofitClient();
        api = retrofitClient.createApi();
    }
}
