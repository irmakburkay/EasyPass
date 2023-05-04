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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class PassEntryFragment extends Fragment implements IEventListener {

    private View view;
    private RecyclerView recyclerView;
    private TextView emptyText;
    protected static List<Password> passwords = new ArrayList<>();
    private PassEntryRecyclerViewAdapter viewAdapter;
    private ItemTouchHelper helper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pass_entry, container, false);

        passwords = EasyPassMainActivity.passwordDao.loadAllPasswords();
        for (Password pass : passwords)
            PassEncryption.decrypt(pass);

        viewAdapter = new PassEntryRecyclerViewAdapter(passwords, this);

        recyclerView = view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));

        helper.attachToRecyclerView(recyclerView);

        emptyText = view.findViewById(R.id.empty_view);

        updateEmptyText(passwords.isEmpty());

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!requireActivity().isFinishing())
            requireActivity().finish();
    }

    private void insertEntry(int position, Password password) {
        EasyPassMainActivity.passwordDao.insertPassword(PassEncryption.encrypt(password));
        passwords.add(position, password);
        updateList(position, Constants.INSERT_CODE);
    }

    private void deleteEntry(int position, Password password) {
        EasyPassMainActivity.passwordDao.deletePasswordById(password.getId());
        passwords.remove(password);
        updateList(position, Constants.DELETE_CODE);
    }

    private void updateList(int position, int STATUS_CODE) {
        switch (STATUS_CODE) {
            case Constants.INSERT_CODE:
                viewAdapter.notifyItemInserted(position);
                break;
            case Constants.DELETE_CODE:
                viewAdapter.notifyItemRemoved(position);
                break;
        }
        updateEmptyText(passwords.isEmpty());
    }

    private void updateEmptyText(boolean status) {
        if (status){
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }
    }

    private void createHelper() {
        helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.END | ItemTouchHelper.START
                );
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int firstPosition = viewHolder.getAdapterPosition();
                int secondPosition = target.getAdapterPosition();

                int temp = passwords.get(firstPosition).getOrder();
                passwords.get(firstPosition).setOrder(passwords.get(secondPosition).getOrder());
                passwords.get(secondPosition).setOrder(temp);

                Collections.swap(passwords, firstPosition, secondPosition);

                EasyPassMainActivity.passwordDao.updatePassword(PassEncryption.encrypt(passwords.get(firstPosition)));
                EasyPassMainActivity.passwordDao.updatePassword(PassEncryption.encrypt(passwords.get(secondPosition)));


                viewAdapter.notifyItemMoved(firstPosition, secondPosition);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Password password = passwords.get(position);
                EasyPassMainActivity.changeFragment(getParentFragmentManager(), new EditPassFragment(password), true, true);
            }

        });

    }

    @Override
    public void onItemClick(int position) {
        String pass = passwords.get(position).getPass();
        ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Password", pass);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(view.getContext(), "Kopyalandı: " + pass, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Emin misiniz?");
        builder.setMessage("Şifreyi silmek istediğinizden emin misiniz?");
        builder.setIcon(R.drawable.ic_warning);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    Password password = passwords.get(position);
                    deleteEntry(position, password);
                    Snackbar snackbar = Snackbar.make(view, password.getName() + " silindi", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("Geri Al", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            insertEntry(position, password);
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