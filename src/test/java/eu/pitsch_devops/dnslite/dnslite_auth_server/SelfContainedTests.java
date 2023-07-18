package eu.pitsch_devops.dnslite.dnslite_auth_server;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.logging.Logger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Tests that only test themselves
 *
 * these tests basically only test themselves. I'm writing them whenever i want to know some java behavior.
 * I'm often keeping them for reference
 */
class SelfContainedTests {

	static Logger log = Logger.getLogger(SelfContainedTests.class.getName());

	@DisplayName("test byte to int")
	@Test
	void testFieldBytestoInt() {
		int x = 257;
		byte[] bytes = new byte[2];
		bytes[0] = (byte) x;
		bytes[1] = (byte)(x >> 8);
		assertTrue(bytes[0] == 1);
		assertTrue(bytes[1] == 1);
	}

	@DisplayName("test int to byte")
	@Test
	void testFieldIntToBytes() {
		byte[] bytes = new byte[2];
		bytes[0] = 1;
		bytes[1] = 1;
		int x = bytes[0] + (bytes[1] << 8);
		assertTrue(x == 257);
	}


	// these next 2 tests do not influence each other because each runs in a separate instance of this class,
	// so each has its own `testMe` array
	int[] testMe = new int[] {0,1000};

	@DisplayName("test Junit Test class instance variables - 1")
	@Test
	void testModifyInstanceVariable1() {
		assertTrue(testMe[0] + testMe[1] == 1000);
		testMe[0] = 1;
	}

	@DisplayName("test Junit Test class instance variables - 2")
	@Test
	void testModifyInstanceVariable2() {
		assertTrue(testMe[0] + testMe[1] == 1000);
		testMe[1] = 2000;
	}


}
