package com.nehaeff.arfinder;

import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

public class ItemListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mItemRecyclerView;
    private ItemAdapter mAdapter;
    private int itemChangePosition = 0;
    private boolean mSubtitleVisible;
    private Button mButtonAddFirstItem;
    private TextView mTextViewNoneItemsNotice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list,
                                        container, false);
        mItemRecyclerView = (RecyclerView) view
                .findViewById(R.id.item_recycler_view);
        mItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        mButtonAddFirstItem = (Button) view.findViewById(R.id.button_add_first);
        mTextViewNoneItemsNotice = (TextView) view.findViewById(R.id.textView_none_item_notice);

        mButtonAddFirstItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item iItem = new Item();
                ItemLab.get(getActivity()).addItem(iItem);
                Intent intent = ItemPagerActivity
                        .newIntent(getActivity(), iItem.getId(), RoomLab.get().getSelectedRoomId());
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
        inflater.inflate(R.menu.fragment_item_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_item:
                Item iItem = new Item();
                ItemLab.get(getActivity()).addItem(iItem);
                Intent intent = ItemPagerActivity
                        .newIntent(getActivity(), iItem.getId(), RoomLab.get().getSelectedRoomId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateEmptyListUI(int count) {

        if (mButtonAddFirstItem != null && mTextViewNoneItemsNotice != null) {

            int visibility = 0;
            if (count == 0)
                visibility = View.VISIBLE;
            else
                visibility = View.INVISIBLE;

            mButtonAddFirstItem.setVisibility(visibility);
            mTextViewNoneItemsNotice.setVisibility(visibility);
        }
    }

    private void updateSubtitle() {
        ItemLab itemLab = ItemLab.get(getActivity());
        int itemCount = itemLab.getItems().size();

        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, itemCount, itemCount);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        ItemLab itemLab = ItemLab.get(getActivity());
        List<Item> items = itemLab.getItems();

        if (mAdapter == null) {
            mAdapter = new ItemAdapter(items);
            mItemRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
            //mAdapter.notifyItemChanged(itemChangePosition);
        }

        updateSubtitle();

        updateEmptyListUI(items.size());
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Item mItem;

        public ItemHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_item_title_text_view);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_item_date_text_view);
        }

        @Override
        public void onClick(View v) {
            itemChangePosition = super.getAdapterPosition();
            Intent intent = ItemPagerActivity.newIntent(getActivity(), mItem.getId(), RoomLab.get().getSelectedRoomId());
            startActivity(intent);
        }

        public void bindItem(Item item) {
            mItem = item;
            mTitleTextView.setText(mItem.getTitle());
            mDateTextView.setText(android.text.format.DateFormat.format("dd.MM.yyyy", mItem.getDate()).toString());

        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<Item> mItems;

        public ItemAdapter(List<Item> items) {
            mItems = items;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_item, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Item item = mItems.get(position);
            holder.bindItem(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void setItems(List<Item> items) {
            mItems = items;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

}
