package com.example.neelabh.projectpopularmovies;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neelabh on 3/27/2016.
 */
public class TrailerItem {
    private Integer id;
    private List<Trailer> results = new ArrayList<Trailer>();

    public List<Trailer> getTrailerList() {
        return results;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.results = trailerList;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public class Trailer{
        private String id;
        private String iso_639_1;
        private String iso_3166_1;
        private String key;
        private String name;
        private String site;
        private Integer size;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLanguage() {
            return iso_639_1;
        }

        public void setLanguage(String language) {
            this.iso_639_1 = language;
        }

        public String getCountry() {
            return iso_3166_1;
        }

        public void setCountry(String country) {
            this.iso_3166_1 = country;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
