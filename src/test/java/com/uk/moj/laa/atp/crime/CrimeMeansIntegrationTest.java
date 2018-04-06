package com.uk.moj.laa.atp.crime;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;
import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverters;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources", glue= {"com.uk.moj.laa.atp.crime.steps"}, plugin ={"pretty" , "html:target/crime-means-tests"})
public class CrimeMeansIntegrationTest {
	


}
