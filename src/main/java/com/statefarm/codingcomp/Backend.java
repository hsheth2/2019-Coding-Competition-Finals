package com.statefarm.codingcomp;

import com.statefarm.codingcomp.Analyzer;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;
public class Backend {
    /*
    public static String loadJson(String fileName) throws IOException {
        Gson gson = new Gson();
        CodingCompCSVUtil util = new CodingCompCSVUtil();
        List<List<String>> records = util.readCSVFileWithoutHeaders(fileName);
        List<DisasterDescription> dds = records.stream().map(DisasterDescription::new).collect(Collectors.toList());
        return gson.toJson(dds);
    }
     */

    public static void main(String[] args) throws Exception {
        Analyzer analyzer = new Analyzer();
        Gson gson = new Gson();

        System.out.println("Running on: http://localhost:4567");

        //staticFiles.location("html");
        staticFiles.externalLocation("/home/hsheth/data/projects/statefarm-coding-comp-2019-finals/src/main/resources/html");

        get("/api/premiums_by_state", (req, res) -> {
            res.type("application/json");
            return gson.toJson(analyzer.getPremiumsByState());
        });

        get("/api/age_by_state", (req, res) -> {
            res.type("application/json");
            return gson.toJson(analyzer.getAgeByState());
        });

        get("/api/accidents_by_state", (req, res) -> {
            res.type("application/json");
            return gson.toJson(analyzer.getNumAccidentsByState());
        });
    }

}
