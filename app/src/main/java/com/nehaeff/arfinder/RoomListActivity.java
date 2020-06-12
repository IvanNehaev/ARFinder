package com.nehaeff.arfinder;

import androidx.fragment.app.Fragment;

import com.nehaeff.arfinder.RoomListFragment;
import com.nehaeff.arfinder.SingleFragmentActivity;

public class RoomListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RoomListFragment();
    }
}
