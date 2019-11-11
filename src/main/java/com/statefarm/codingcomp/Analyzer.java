package com.statefarm.codingcomp;

import java.util.List;

import com.statefarm.codingcomp.model.Policy;
import com.statefarm.codingcomp.reader.Reader;

public class Analyzer {

	public static void main(String[] args) throws Exception {
		List<Policy> policies = new Reader().read();
		assert policies.size() == 10000;
		//TODO - find something interesting in the data

        policies.stream().forEach((policy -> System.out.println(policy.getAge())));
	}
}
