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

public class RoomPagerActivity extends AppCompatActivity {
    private static final String EXTRA_ITEM_ID =
            "com.nehaeff.arfinder.room_id";

    private ViewPager mViewPager;
    private List<Room> mRooms;

    public static Intent newIntent(Context packageContext, UUID roomId) {
        Intent intent = new Intent(packageContext, RoomPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, roomId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_pager);

        UUID roomId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_ITEM_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_item_pager_view_pager);
        mRooms = RoomLab.get(this).getRooms();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Room room = mRooms.get(position);
                return RoomFragment.newInstance(room.getId());
            }

            @Override
            public int getCount() {
                return mRooms.size();
            }
        });

        for (int i = 0; i < mRooms.size(); i++) {
            if (mRooms.get(i).getId().equals(roomId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
