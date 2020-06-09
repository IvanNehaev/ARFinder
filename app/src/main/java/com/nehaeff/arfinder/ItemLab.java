package com.nehaeff.arfinder;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemLab {
    private static ItemLab sItemLab;
    private List<Item> mItems;

    public static ItemLab get(Context context) {
        if (sItemLab == null) {
            sItemLab = new ItemLab(context);
        }
        return sItemLab;
    }

    private ItemLab(Context context) {
        mItems = new ArrayList<>();
    }

    public void addItem(Item item) {
        mItems.add(item);
    }

    public List<Item> getItems() {
        return mItems;
    }

    public Item getItem(UUID id) {
        for (Item item : mItems) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public boolean deleteItem(UUID id) {
        for (Item item : mItems) {
            if (item.getId().equals(id)) {
               mItems.remove(item);
               return true;
            }
        }
        return false;
    }

    public boolean deleteItem(Item item) {
        mItems.remove(item);
        return true;
    }
}
