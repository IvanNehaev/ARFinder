package com.nehaeff.arfinder;

import android.annotation.SuppressLint;
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

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_item:
                Item iItem = new Item();
                ItemLab.get(getActivity()).addItem(iItem);
                Intent intent = ItemPagerActivity
                        .newIntent(getActivity(), iItem.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateEmptyListUI(int count) {

        View view = getView();

        if (view !=null) {
            mButtonAddFirstItem = (Button) view.findViewById(R.id.button_add_first);
            mTextViewNoneItemsNotice = (TextView) view.findViewById(R.id.textView_none_item_notice);
        }

        if (count == 0) {
            if ( mButtonAddFirstItem != null)
                mButtonAddFirstItem.setVisibility(View.VISIBLE);
            if ( mTextViewNoneItemsNotice != null)
                mTextViewNoneItemsNotice.setVisibility(View.VISIBLE);

        } else {
            if ( mButtonAddFirstItem != null)
                mButtonAddFirstItem.setVisibility(View.INVISIBLE);
            if ( mTextViewNoneItemsNotice != null)
                mTextViewNoneItemsNotice.setVisibility(View.INVISIBLE);
        }
    }

    private void updateSubtitle() {
        ItemLab itemLab = ItemLab.get(getActivity());
        int itemCount = itemLab.getItems().size();
        //@SuppressLint("StringFormatMatches") String subtitle = getString(R.string.subtitle_format, itemCount);
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, itemCount, itemCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

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
            Intent intent = ItemPagerActivity.newIntent(getActivity(), mItem.getId());
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

}
