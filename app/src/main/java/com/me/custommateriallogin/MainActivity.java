package com.me.custommateriallogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView logoText;
    private TextView textLearn;
    private TextView textErrorEmailLogin;
    private TextView textErrorPasswordLogin;
    private EditText emailLogin;
    private EditText passwordLogin;
    private AppCompatCheckBox passwordToggle;

    private CardView cardTogglePassword;

    private Button loginButton;
    private TextView reglink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.background_default_layout));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        logoText = findViewById(R.id.logo);
        textLearn = findViewById(R.id.textlearn);
        textErrorEmailLogin = findViewById(R.id.text_error_email_login);
        textErrorPasswordLogin = findViewById(R.id.text_error_password_login);
        emailLogin = findViewById(R.id.email_login);
        passwordLogin = findViewById(R.id.password_login);
        loginButton = findViewById(R.id.button_login);
        reglink = findViewById(R.id.registrationlink);
        passwordToggle = findViewById(R.id.toggle_password);
        cardTogglePassword = findViewById(R.id.card_toggle_password);

        String n = getColoredSpanned("n","#3cba54");
        String u = getColoredSpanned("u","#f4c20d");
        String d = getColoredSpanned("d","#db3236");
        String l = getColoredSpanned("l","#4885ed");
        String e = getColoredSpanned("e","#3cba54");

        logoText.setText(Html.fromHtml(n+" "+u+" "+d+" "+l+" "+e));

        String text = "with your Nudle Account. Learn more";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(MainActivity.this, "Learn More", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#07BFE4"));
                ds.setUnderlineText(false);
            }
        };

        ss.setSpan(clickableSpan, 25, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textLearn.setText(ss);
        textLearn.setMovementMethod(LinkMovementMethod.getInstance());

        reglink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordLogin.setText(null);
                Intent intent = new Intent(getApplicationContext(), RegistrasiActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(loginButton);
                if (!validateEmail(emailLogin) | !validatePassword()) {
                    Log.d("login Activity", "onClick: login gagal");
                    Toast.makeText(getApplicationContext(), "Login Gagal", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_LONG).show();
                }
            }
        });

        emailLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String emailInput =  emailLogin.getText().toString();

                if (emailInput.isEmpty()) {
                    textErrorEmailLogin.setText("");
                    textErrorEmailLogin.setVisibility(View.GONE);
                    Log.d("valemail", "validateEmail: email kosong");
                } else if (emailInput.length() > 0){
                    textErrorEmailLogin.setError("");
                    textErrorEmailLogin.setVisibility(View.GONE);
                }
            }
        });

        passwordLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String passwordInput =  passwordLogin.getText().toString().trim();

                if (passwordInput.equals("")){
                    textErrorPasswordLogin.setVisibility(View.GONE);
                    cardTogglePassword.setVisibility(View.GONE);
                } else if (passwordInput.length() > 0 || passwordInput.length()<8){
                    cardTogglePassword.setVisibility(View.VISIBLE);
                    textErrorPasswordLogin.setVisibility(View.VISIBLE);
                    textErrorPasswordLogin.setText("Password minimal 8 karakter");
                }
            }
        });

        passwordToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    passwordLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean validatePassword() {
        String passwordInput =  passwordLogin.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textErrorPasswordLogin.setText("Password masih kosong");
            textErrorPasswordLogin.setVisibility(View.VISIBLE);
            return false;
        } else if (passwordInput.length()<8){
            textErrorPasswordLogin.setText("Password minimal 8 karakter");
            textErrorPasswordLogin.setVisibility(View.VISIBLE);
            return false;
        } else {
            textErrorPasswordLogin.setText(null);
            textErrorPasswordLogin.setVisibility(View.GONE);
            return true;
        }
    }

    private boolean validateEmail(EditText emailLogin) {
        String emailInput =  emailLogin.getText().toString();

        if (emailInput.isEmpty()) {
            textErrorEmailLogin.setText("Email masih kosong");
            textErrorEmailLogin.setVisibility(View.VISIBLE);
            Log.d("valemail", "validateEmail: email kosong");
            return false;
        }
        //incase untuk pattern email address
// else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
//            textErrorEmailLogin.setError("Format email salah");
//            Log.d("valemail", "validateEmail: format salah");
//            return false;
        else {
            textErrorEmailLogin.setError(null);
            textErrorEmailLogin.setVisibility(View.GONE);
            return true;
        }
    }
    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
}