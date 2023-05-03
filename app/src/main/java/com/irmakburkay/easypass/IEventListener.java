package com.irmakburkay.easypass;

import android.view.View;

public interface IEventListener{
    void onItemClick(int position);
    boolean onItemLongClick(int position);
    void onClick(View view);
}
