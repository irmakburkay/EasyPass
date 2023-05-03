package com.irmakburkay.easypass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Random;

public class EasyPassMainActivity extends AppCompatActivity {

    protected static IPasswordDao passwordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_pass_main);

        InitComponents();
        getFragment();

    }

    public void InitComponents() {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(this);
        passwordDao = appDatabase.getPasswordDao();

        ArrayList<Password> passwords = new ArrayList<>();
        passwords.add(new Password(new Random().nextLong(), "name1", "pass1", R.drawable.ic_launcher_foreground));
        passwords.add(new Password(new Random().nextLong(), "name2", "pass2", R.drawable.ic_launcher_foreground));

        for (int i = 0; i < 2; i++) {
            passwordDao.insertPassword(passwords.get(i));
        }

    }

    public void getFragment() {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, PassEntryFragment.class, null)
                .commit();
    }

}