package com.nehaeff.arfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class ItemListActivity extends SingleFragmentActivity {

    private static final String EXTRA_ROOM_ID =
            "com.nehaeff.arfinder.room_id";

    public static Intent newIntent(Context packageContext, UUID roomId) {
        Intent intent = new Intent(packageContext, ItemListActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {

        UUID roomId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_ROOM_ID);

        ItemLab.get(this).setTableName(roomId.toString());

        return new ItemListFragment();
    }
}
