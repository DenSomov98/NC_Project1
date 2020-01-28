package server.controllers;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ClientDataHolder {
    private static int counter = 0;
    private int id = counter++;
    private ObjectOutputStream out;
    private boolean inSearch = false;
    private String[] searchCriteria;

    public ClientDataHolder(ObjectOutputStream out) {
        this.out = out;
    }

    public int getId() {
        return this.id;
    }

    public boolean isInSearch() {
        return this.inSearch;
    }

    public void setInSearch(boolean inSearch) {
        this.inSearch = inSearch;
    }

    public void setSearchCriteria(String[] searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public String[] getSearchCriteria() {
        return searchCriteria;
    }

    public void send(Object data) throws IOException {
        out.writeObject(data);
    }
}
