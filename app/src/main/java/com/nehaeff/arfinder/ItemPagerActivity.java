package com.nehaeff.arfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class ItemPagerActivity extends AppCompatActivity {
    private static final String EXTRA_ITEM_ID =
            "com.nehaeff.arfinder.item_id";
    private static final String EXTRA_ROOM_ID =
            "com.nehaeff.arfinder.room_id";

    private ViewPager mViewPager;
    private List<Item> mItems;

    public static Intent newIntent(Context packageContext, UUID itemId, UUID roomId) {
        Intent intent = new Intent(packageContext, ItemPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        intent.putExtra(EXTRA_ROOM_ID, roomId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_pager);

        UUID itemId = (UUID) getIntent()
                        .getSerializableExtra(EXTRA_ITEM_ID);

        ItemLab.get().setSelectedItemId(itemId);

        mViewPager = (ViewPager) findViewById(R.id.activity_item_pager_view_pager);
        mItems = ItemLab.get(this).getItems();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Item item = mItems.get(position);
                return ItemFragment.newInstance(item.getId());
            }

            @Override
            public int getCount() {
                return mItems.size();
            }
        });

        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).getId().equals(itemId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
