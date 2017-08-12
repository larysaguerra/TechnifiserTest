package com.technifiser.api.result;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable{

    private String query;
    private Integer totalResults;
    private List<Group> groups = null;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

}
