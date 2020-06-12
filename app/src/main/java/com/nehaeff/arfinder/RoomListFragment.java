package com.nehaeff.arfinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoomListFragment extends Fragment {
    private RecyclerView mRoomRecyclerView;
    private int roomChangePosition = 0;
    private RoomListFragment.RoomAdapter mAdapter;
    private Button mButtonAddFirstRoom;
    private TextView mTextViewNoneRoomsNotice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_list,
                container, false);
        mRoomRecyclerView = (RecyclerView) view
                .findViewById(R.id.room_recycler_view);
        mRoomRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));

        mButtonAddFirstRoom = (Button) view.findViewById(R.id.button_add_first_room);
        mTextViewNoneRoomsNotice = (TextView) view.findViewById(R.id.textView_none_room_notice);

        mButtonAddFirstRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Room room = new Room();
                RoomLab.get(getActivity()).addRoom(room);
                Intent intent = RoomPagerActivity
                        .newIntent(getActivity(), room.getId());
                startActivity(intent);
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_romm_list_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_room_new_room:
                Room room = new Room();
                RoomLab.get(getActivity()).addRoom(room);
                Intent intent = RoomPagerActivity
                        .newIntent(getActivity(), room.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        RoomLab roomLab = RoomLab.get(getActivity());
        List<Room> rooms = roomLab.getRooms();

        if (mAdapter == null) {
            mAdapter = new RoomListFragment.RoomAdapter(rooms);
            mRoomRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setRooms(rooms);
            mAdapter.notifyDataSetChanged();
            //mAdapter.notifyItemChanged(itemChangePosition);
        }

        //updateSubtitle();

        updateEmptyListUI(rooms.size());
    }

    private void updateEmptyListUI(int count) {

        if (mButtonAddFirstRoom != null && mTextViewNoneRoomsNotice != null) {

            int visibility = 0;
            if (count == 0)
                visibility = View.VISIBLE;
            else
                visibility = View.INVISIBLE;

            mButtonAddFirstRoom.setVisibility(visibility);
            mTextViewNoneRoomsNotice.setVisibility(visibility);
        }
    }

    private class RoomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private Room mRoom;
        private Button mButtonEdit;

        public RoomHolder(View roomView) {
            super(roomView);
            roomView.setOnClickListener(this);
            mTitleTextView = (TextView)
                    roomView.findViewById(R.id.text_view_list_item_room_title);
            mButtonEdit = (Button)
                    roomView.findViewById(R.id.button_list_room_edit);
        }

        @Override
        public void onClick(View v) {
            //Intent intent = new Intent(getActivity(), ItemListActivity.class);
            Intent intent = ItemListActivity.newIntent(getActivity(), mRoom.getId());
            startActivity(intent);
        }

        public void bindRoom(Room room) {
            mRoom = room;
            mTitleTextView.setText(mRoom.getTitle());
        }
    }

    private class RoomAdapter extends RecyclerView.Adapter<RoomListFragment.RoomHolder> {
        private List<Room> mRooms;

        public RoomAdapter(List<Room> rooms) {
            mRooms = rooms;
        }

        @Override
        public RoomListFragment.RoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_room, parent, false);
            return new RoomListFragment.RoomHolder(view);
        }

        @Override
        public void onBindViewHolder(RoomListFragment.RoomHolder holder, int position) {
            final Room room = mRooms.get(position);
            holder.bindRoom(room);
            holder.mButtonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = RoomPagerActivity.newIntent(getActivity(), room.getId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mRooms.size();
        }

        public void setRooms(List<Room> rooms) {
            mRooms = rooms;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }
}
