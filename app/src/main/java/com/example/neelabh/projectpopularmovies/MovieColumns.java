package com.example.neelabh.projectpopularmovies;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by neelabh on 4/10/2016.
 */
public interface MovieColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey
    public static final String _ID =
            "_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String NAME = "name";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String IMAGE = "image";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String LANGUAGE = "language";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String RATING = "rating";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String OVERVIEW = "overview";

}
