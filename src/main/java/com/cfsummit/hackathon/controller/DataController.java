package com.cfsummit.hackathon.controller;

import com.cfsummit.hackathon.mapper.BuildpackDataMapper;
import com.cfsummit.hackathon.model.AppStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaltokarz on 13/06/2017.
 */
@RestController
public class DataController {
    private volatile List<AppStats> appsCache = new ArrayList<>();

    @Autowired
    private BuildpackDataMapper buildpackDataMapper;

    @GetMapping("/api/buildpackStats")
    public List<AppStats> buildpackStats() {
        return appsCache;
    }

    @Scheduled(fixedDelay = 1000 * 5)
    public void setAppsCache() {
        appsCache = buildpackDataMapper.map();
    }
}
