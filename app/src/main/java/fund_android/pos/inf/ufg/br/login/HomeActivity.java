package fund_android.pos.inf.ufg.br.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        String welcomeMsg = getIntent().getStringExtra("welcome_text");
        tvWelcome.setText(welcomeMsg);

    }

    public void logout(View view) {
        // Send request to server to destroy current session
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
