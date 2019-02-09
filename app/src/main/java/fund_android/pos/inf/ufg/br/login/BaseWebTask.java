package fund_android.pos.inf.ufg.br.login;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Request;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;



public abstract class BaseWebTask extends AsyncTask<Void, Void, Void> {
    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_INVALID_REQUEST = 403;
    private static int TIMEOUT = 15;
    private static String BASE_URL = "http://private-2bb041-sandromoreira.apiary-mock.com/";

    private String serviceUrl;
    private Context ctx;
    private int responseCode;
    private String responseString;
    private boolean silent;
    private Error error;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public BaseWebTask(Context ctx, String serviceUrl) {
        this.ctx = ctx;
        this.serviceUrl = serviceUrl;

    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(!isOnline(ctx)) {
            this.error = new Error(ctx.getString(R.string.connection_error));
            this.responseString = null;
            return null;
        }
        doRegularCall();
        return null;
    }

    private void doRegularCall() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, this.getRequestBody());

        Request request = new Request.Builder()
                .url(BASE_URL + serviceUrl)
                .post(body)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            responseCode = response.code();
            responseString = response.body().string();
        } catch (IOException e) {
            error = new Error(ctx.getString(R.string.connection_error));
            Toast.makeText(ctx, "IO Excepion", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(error != null && !silent) {
            EventBus.getDefault().post(error);
        } else {
            switch (getResponseCode()) {
                case RESPONSE_OK:
                    try {
                        JSONObject responseJson = new JSONObject(responseString);
                        String errorMsg = responseJson.getString("error");
                        if(!this.silent) {
                            EventBus.getDefault().post(new Error(errorMsg));
                        }
                    } catch (JSONException e) {
                        this.handleResponse(responseString);
                    } catch (NullPointerException e) {
                        this.handleResponse("");
                    }
                    break;

                case RESPONSE_INVALID_REQUEST:
                    EventBus.getDefault().post(new Error(ctx.getString(R.string.invalid_request_error)));
                    break;
            }
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public abstract String getRequestBody();

    public abstract void handleResponse(String response);


    public Context getContext() {
        return this.ctx;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public boolean isSilent() {
        return this.silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }
}
