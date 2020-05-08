package com.example.hp_awareness_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;

import java.util.List;
import java.util.Objects;

public class PhoneActivity extends AppCompatActivity {
    private Spinner spinner;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));

        editText = findViewById(R.id.editTextPhone);

        findViewById(R.id.btnAdminLogin).setOnClickListener(v -> {
            Intent intent = new Intent(PhoneActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.buttonContinue).setOnClickListener(v -> {
            String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];

            String number = editText.getText().toString().trim();

            if (number.isEmpty() || number.length() < 10) {
                editText.setError("Valid number is required");
                editText.requestFocus();
                return;
            }

            String phoneNumber = "+" + code + number;

            Intent intent = new Intent(PhoneActivity.this, VerifyPhoneActivity.class);
            intent.putExtra("phonenumber", phoneNumber);
            startActivity(intent);

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            List<? extends UserInfo> pd = user.getProviderData();
            UserInfo providerData = pd.get(1);
            String pid = providerData.getProviderId();
            if(Objects.equals(pid, "password"))
            {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("type", "Admin");
                startActivity(intent);
            }
            else if(Objects.equals(pid, "phone")) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("type", "User");
                startActivity(intent);
            }
        }
    }
}

