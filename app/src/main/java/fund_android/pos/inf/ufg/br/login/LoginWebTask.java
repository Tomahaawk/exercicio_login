package fund_android.pos.inf.ufg.br.login;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginWebTask extends BaseWebTask {
    private static String SERVICE_URL = "login";
    private String userName;
    private String userPwd;

    public LoginWebTask(Context ctx, String userName, String userPwd) {
        super(ctx, SERVICE_URL);
    }

    @Override
    public String getRequestBody() {
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", userName);
        requestMap.put("password", userPwd);
        JSONObject json = new JSONObject(requestMap);

        return json.toString();
    }

    @Override
    public void handleResponse(String response) {
        User user = new User();
        try {
            JSONObject resJson = new JSONObject(response);
            String name = resJson.getString("name");
            String token = resJson.getString("token");
            String photoUrl = resJson.getString("photo_url");

            user.setName(name);
            user.setToken(token);
            user.setPhotoUrl(photoUrl);

            EventBus.getDefault().post(user);

        } catch (JSONException e) {
            if(!isSilent()) {
                EventBus.getDefault().post(new Error(getContext().getString(R.string.invaid_response_error)));
            }
        }

    }
}
