package com.statefarm.codingcomp;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import com.statefarm.codingcomp.model.Policy;
import com.statefarm.codingcomp.reader.Reader;

public class Analyzer {
	private List<Policy> policies;

	public Analyzer() throws Exception {
		policies = new Reader().read();
		assert policies.size() == 10000;
	}

	public Map<String, Long> getPolicyHoldersByState(Predicate<Policy> filter) {
		return policies.stream()
				.filter(filter)
				.collect(Collectors.groupingBy(Policy::getState, Collectors.counting()));
	}

	private Map<String, Double> getAvgXByState(Predicate<Policy> filter, ToDoubleFunction<Policy> target) {
		return policies.stream()
				.filter(filter)
				.collect(Collectors.groupingBy(Policy::getState, Collectors.averagingDouble(target)));
	}

	public Map<String, Double> getPremiumsByState(Predicate<Policy> filter) {
	    return getAvgXByState(filter, Policy::getAnnualPremium);
	}

	public Map<String, Double> getAgeByState(Predicate<Policy> filter) {
		return getAvgXByState(filter, Policy::getAge);
	}

	public Map<String, Double> getNumAccidentsByState(Predicate<Policy> filter) {
		return getAvgXByState(filter, Policy::getNumberOfAccidents);
	}

	public static void main(String[] args) throws Exception {
	    Analyzer analyzer = new Analyzer();

	    // Just a test to make sure it is read properly.
        analyzer.policies.forEach((policy -> System.out.println(policy.getPolicyType())));
	}
}
