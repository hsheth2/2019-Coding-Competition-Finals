package com.statefarm.codingcomp;

import com.google.gson.Gson;
import com.statefarm.codingcomp.enums.PolicyStatus;
import com.statefarm.codingcomp.model.Policy;
import spark.Request;

import java.util.function.Predicate;

import static spark.Spark.*;
public class Backend {
    public static Predicate<Policy> filterFromUrlParams(Request req) {
        String status = req.queryParams("status");
        Predicate<Policy> statusFilter = (policy -> {
            switch (status) {
                case "active":
                    return policy.getPolicyStatus() == PolicyStatus.ACTIVE;
                case "cancel-holder":
                    return policy.getPolicyStatus() == PolicyStatus.CANCELLED_BY_POLICYHOLDER;
                case "cancel-company":
                    return policy.getPolicyStatus() == PolicyStatus.CANCELLED_BY_COMPANY;
                default:
                    return true;
            }
        });

        String type = req.queryParams("type");
        Predicate<Policy> typeFilter = (policy -> {
            switch (type) {
                case "renters":
                    return policy.getPolicyType().equals("Renters");
                case "private-passenger":
                    return policy.getPolicyType().equals("Private Passenger");
                default:
                    return true;
            }
        });

        return statusFilter.and(typeFilter);
    }

    public static void main(String[] args) throws Exception {
        Analyzer analyzer = new Analyzer();
        Gson gson = new Gson();

        System.out.println("Running on: http://localhost:4567");

        staticFiles.location("html");
        //staticFiles.externalLocation("/home/hsheth/data/projects/statefarm-coding-comp-2019-finals/src/main/resources/html");

        get("/api/policyholders_by_state", (req, res) -> {
            res.type("application/json");
            return gson.toJson(analyzer.getPolicyHoldersByState(filterFromUrlParams(req)));
        });

        get("/api/premiums_by_state", (req, res) -> {
            res.type("application/json");
            return gson.toJson(analyzer.getPremiumsByState(filterFromUrlParams(req)));
        });

        get("/api/age_by_state", (req, res) -> {
            res.type("application/json");
            return gson.toJson(analyzer.getAgeByState(filterFromUrlParams(req)));
        });

        get("/api/accidents_by_state", (req, res) -> {
            res.type("application/json");
            return gson.toJson(analyzer.getNumAccidentsByState(filterFromUrlParams(req)));
        });
    }

}
