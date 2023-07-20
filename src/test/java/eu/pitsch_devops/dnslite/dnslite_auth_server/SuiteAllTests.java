package eu.pitsch_devops.dnslite.dnslite_auth_server;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
	SelfContainedTests.class,
	DNSMessageBytesTest.class,
	DNSQuestionBuilderTest.class
})

public class SuiteAllTests {

}
