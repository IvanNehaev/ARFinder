package com.nehaeff.arfinder;

import androidx.fragment.app.Fragment;

public class ItemListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ItemListFragment();
    }
}
