package com.jhpj.pricesearch.ui.naversearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {
    @SerializedName("lastBuildDate")
    @Expose
    private String lastBuildDate;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("items")
    @Expose
    private List<SearchDataList> SearchDataList = null;

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<SearchDataList> getSearchDataList() {
        return SearchDataList;
    }

    public void setSearchDataList(List<SearchDataList> searchDataList) {
        SearchDataList = searchDataList;
    }
}