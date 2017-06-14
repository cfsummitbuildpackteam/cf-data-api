package com.cfsummit.hackathon.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaltokarz on 13/06/2017.
 */
@Data
public class AppStats {
    String name;
    String org;
    String space;
    String buildpack;
    List<String> managers = new ArrayList<>();

    public void addManager(String manager) {
        managers.add(manager);
    }
}
