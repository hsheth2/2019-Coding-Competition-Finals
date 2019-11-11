package com.statefarm.codingcomp;

import java.util.List;
import java.util.Map;
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

	private Map<String, Double> getAvgXByState(ToDoubleFunction<Policy> target) {
		return policies.stream()
				.collect(Collectors.groupingBy(Policy::getState, Collectors.averagingDouble(target)));
	}

	public Map<String, Double> getPremiumsByState() {
	    return getAvgXByState(Policy::getAnnualPremium);
	}

	public Map<String, Double> getAgeByState() {
		return getAvgXByState(Policy::getAge);
	}

	public Map<String, Double> getNumAccidentsByState() {
		return getAvgXByState(Policy::getNumberOfAccidents);
	}

	public static void main(String[] args) throws Exception {
	    Analyzer analyzer = new Analyzer();

	    // Just a test to make sure it is read properly.
        analyzer.policies.forEach((policy -> System.out.println(policy.getAge())));
	}
}
