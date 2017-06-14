package com.cfsummit.hackathon.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaltokarz on 13/06/2017.
 */
@Data
public class Org {
    String id;
    String name;
    List<String> orgManagers = new ArrayList<>();

    public void addManager(String manager) {
        orgManagers.add(manager);
    }
}
