package edu.uml.cs.isense.credentials;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import edu.uml.cs.isense.R;

public class LoginOrKeyDialog extends Activity {
    private Context mContext = this;
    private RadioButton key;
    private RadioButton login;

    //strings for intent
    public static final String uploadMethod = "WhichMethod?";
    public static final String loginRBSelected = "LoginMethod";
    public static final String keyRBSelected = "keyMethod";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_or_key);

        Button ok = (Button) findViewById(R.id.button_ok);
        Button cancel = (Button) findViewById(R.id.button_cancel);

        key = (RadioButton) findViewById(R.id.rbKey);
        login = (RadioButton) findViewById(R.id.rbLogin);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (key.isChecked()) {
                    Intent result = new Intent();
                    result.putExtra(uploadMethod, keyRBSelected);
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    Intent result = new Intent();
                    result.putExtra(uploadMethod, loginRBSelected);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

    }
}
