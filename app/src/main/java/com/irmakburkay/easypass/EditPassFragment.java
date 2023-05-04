package com.irmakburkay.easypass;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Random;

public class EditPassFragment extends Fragment {

    private View view;
    private Password password = null;

    private ImageView icon;
    private EditText name, pass;
    private Button apply;

    public EditPassFragment() {}

    public EditPassFragment(Password password) {
        this.password = password;
    }

    @Override
    public void onResume() {
        super.onResume();
        EasyPassMainActivity.extendedFab.setText(password == null ? "Ekle" : "Düzenle");
        EasyPassMainActivity.extendedFab.extend();
        EasyPassMainActivity.extendedFab.setIcon(AppCompatResources.getDrawable(getContext(), R.drawable.ic_close));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_pass, container, false);

        InitComponents();
        RegisterEventHandlers();

        return view;
    }

    @Override
    public void onPause() {
        EasyPassMainActivity.extendedFab.setIcon(AppCompatResources.getDrawable(getContext(), R.drawable.ic_add));
        EasyPassMainActivity.extendedFab.shrink();
        super.onPause();
    }

    private void InitComponents() {
        icon = view.findViewById(R.id.icon_image);
        name = view.findViewById(R.id.name_input);
        pass = view.findViewById(R.id.password_input);
        apply = view.findViewById(R.id.apply_button);

        if (password == null)
            apply.setText("Kaydet");
        else {
            icon.setImageResource(password.getIcon());
            name.setText(password.getName());
            pass.setText(password.getPass());
            apply.setText("Güncelle");
        }

    }

    private void RegisterEventHandlers() {

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!name.getText().toString().equals("") && !pass.getText().toString().equals("")) {
                    if (password != null) {
                        password.setName(name.getText().toString());
                        password.setPass(pass.getText().toString());
                        EasyPassMainActivity.passwordDao.updatePassword(PassEncryption.encrypt(password));
                    } else {
                        password = new Password(PassEntryFragment.passwords.size(), name.getText().toString(), pass.getText().toString(), R.drawable.ic_launcher_foreground);
                        EasyPassMainActivity.passwordDao.insertPassword(PassEncryption.encrypt(password));
                    }
                    EasyPassMainActivity.changeFragment(getParentFragmentManager(), new PassEntryFragment(), true, true);
                }

            }
        });
    }

}