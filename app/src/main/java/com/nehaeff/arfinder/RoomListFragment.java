package com.nehaeff.arfinder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RoomListFragment extends Fragment {

    private RecyclerView mRoomRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_list,
                container, false);
        mRoomRecyclerView = (RecyclerView) view
                .findViewById(R.id.room_recycler_view);
        mRoomRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));

        return view;
    }
}
