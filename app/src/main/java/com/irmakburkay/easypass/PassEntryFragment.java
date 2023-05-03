package com.irmakburkay.easypass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class PassEntryFragment extends Fragment implements IEventListener {

    private View view;
    private List<Password> passwords = new ArrayList<>();
    private PassEntryRecyclerViewAdapter viewAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passwords = EasyPassMainActivity.passwordDao.loadAllPasswords();
        viewAdapter = new PassEntryRecyclerViewAdapter(passwords, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pass_entry, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));

        return view;
    }

    @Override
    public void onItemClick(int position) {
        String pass = passwords.get(position).getPass();
        ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Password", pass);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getContext(), "Kopyalandı: " + pass, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(int position) {
        Password password = passwords.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Emin misiniz?");
        builder.setMessage("Şifreyi silmek istediğinizden emin misiniz?");
        builder.setIcon(R.drawable.ic_warning);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    EasyPassMainActivity.passwordDao.deletePassword(password);
                    passwords.remove(password);
                    viewAdapter.notifyItemRemoved(position);
                    Snackbar snackbar = Snackbar.make(view, password.getName() + " silindi", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Geri Al", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EasyPassMainActivity.passwordDao.insertPassword(password);
                            passwords.add(position, password);
                            viewAdapter.notifyItemInserted(position);
                        }
                    });
                    snackbar.show();
                }
            }
        };

        builder.setPositiveButton("Evet", listener);
        builder.setNegativeButton("Hayır", listener);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return true;
    }

    @Override
    public void onClick(View view) {
        TextView pass = (TextView) view;
        if (pass.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            Log.i("MyLOG", pass.getText() + " is pass sets to text");
            pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);

        }
        else {
            Log.i("MyLOG", pass.getText() + " is text sets to pass");
            pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        }
    }
}