package com.nehaeff.arfinder.database;

public class ItemDBSchema {
    public static final class ItemTable {
        public static final String NAME = "items";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String DETAILT = "details";
        }
    }
}
