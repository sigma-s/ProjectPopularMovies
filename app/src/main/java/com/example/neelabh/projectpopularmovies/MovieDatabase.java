package com.example.neelabh.projectpopularmovies;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by neelabh on 4/10/2016.
 */
@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {
    private MovieDatabase(){}

    public static final int VERSION = 4;

    @Table(MovieColumns.class) public static final String MOVIES = "movies";
}
