package fund_android.pos.inf.ufg.br.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {
    private LoginWebTask mAuthTask = null;

    private EditText usernameInput;
    private EditText passwordInput;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = (EditText) findViewById(R.id.etUserName);
        passwordInput = (EditText) findViewById(R.id.etUserPwd);
    }

    public void login(View view) {
        if(mAuthTask != null) {
            return;
        }

        usernameInput.setError(null);
        passwordInput.setError(null);

        String usuario = usernameInput.getText().toString();
        String senha = passwordInput.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Checks password
        if(!TextUtils.isEmpty(senha) && !isPasswordValid(senha)) {
            passwordInput.setError(getString(R.string.invalid_pwd_error));
            focusView = passwordInput;
            cancel = true;
        }

        // Checks email
        if(TextUtils.isEmpty(usuario)) {
            usernameInput.setError(getString(R.string.required_field_error));
            focusView = usernameInput;
            cancel = true;
        } else if (!isUserNameValid(usuario)) {
            usernameInput.setError(getString(R.string.invalid_username_error));
            focusView = usernameInput;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            mAuthTask = new LoginWebTask(this, usuario, senha);
            mAuthTask.execute();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private boolean isPasswordValid(String pwd) {
        return pwd.length() > 1;
    }

    private boolean isUserNameValid(String userName) {
        return userName.contains("@");
    }


    /*
    * EventBus event handler
    */
    @Subscribe
    public void onEvent(User user) {
        mAuthTask = null;
        System.out.println(user.getName());
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("welcome_text", "Bem vindo, " + user.getName() + "!");
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onEvent(Error error) {
        mAuthTask = null;
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onAnythin(NoSubscriberEvent randomEvent) {
        mAuthTask = null;
        Toast.makeText(this, "Random event...", Toast.LENGTH_SHORT).show();
    }
}
