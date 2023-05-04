package com.irmakburkay.easypass;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

public class EasyPassMainActivity extends AppCompatActivity {

    protected static IPasswordDao passwordDao;
    protected static ExtendedFloatingActionButton extendedFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_pass_main);

        InitComponents();
        registerEventHandlers();
        changeFragment(getSupportFragmentManager(), new PassEntryFragment(), true, true);
    }

    private void InitComponents() {
        AppDatabase appDatabase = AppDatabase.getAppDatabase(this);
        passwordDao = appDatabase.getPasswordDao();

        extendedFab = findViewById(R.id.insert_fab);
        extendedFab.shrink();


//        ArrayList<Password> passwords = new ArrayList<>();
//        passwords.add(new Password(new Random().nextLong(), "name1", "pass1", R.drawable.ic_launcher_foreground));
//        passwords.add(new Password(new Random().nextLong(), "name2", "pass2", R.drawable.ic_launcher_foreground));
//        passwords.add(new Password(new Random().nextLong(), "name3", "pass3", R.drawable.ic_launcher_foreground));
//
//        passwordDao.deleteAllPasswords();
//
//        for (int i = 0; i < 2; i++) {
//            passwordDao.insertPassword(passwords.get(i));
//        }

    }

    private void registerEventHandlers() {
        extendedFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (extendedFab.isExtended()) {
                    changeFragment(getSupportFragmentManager(), new PassEntryFragment(), true, true);
                }
                else {
                    changeFragment(getSupportFragmentManager(), new EditPassFragment(), true, true);
                }
            }
        });
    }

    protected static void changeFragment(FragmentManager fragmentManager, Fragment frag, boolean saveInBackstack, boolean animate) {
        String backStateName = ((Object) frag).getClass().getName();

        try {
            FragmentManager manager = fragmentManager;
            boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

            if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
                //fragment not in back stack, create it.
                FragmentTransaction transaction = manager.beginTransaction();

                if (animate) {
                    Log.d("MyLog", "Change Fragment: animate");
                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }

                transaction.replace(R.id.fragment_container_view, frag, backStateName);

                if (saveInBackstack) {
                    Log.d("MyLog", "Change Fragment: addToBackTack " + backStateName);
                    transaction.addToBackStack(backStateName);
                } else {
                    Log.d("MyLog", "Change Fragment: NO addToBackTack");
                }

                transaction.commit();
            } else {
                // custom effect if fragment is already instanciated
            }
        } catch (IllegalStateException exception) {
            Log.w("MyLog", "Unable to commit fragment, could be activity as been killed in background. " + exception.toString());
        }
    }

}