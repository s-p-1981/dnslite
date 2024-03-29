package eu.ptsdvps.dnslite.protocol;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.*;

class DNSQuestionBuilderTest {

	static Logger log = Logger.getLogger(DNSQuestionBuilderTest.class.getName());

	@DisplayName("Test MessageBuilder")
	@Test
	void testBuildQuestion() {
		DNSMessage question = new DNSQuestionBuilder().build();
	}

	@DisplayName("test Transaction ID field")
	@Test
	void testFieldTransactionID() {
	DNSMessage question = new DNSQuestionBuilder()
				.setFieldTransactionID(1000)
				.build();
		assertEquals(1000, question.getFieldTransactionID());
		assertNotEquals(0, question.getFieldTransactionID());
	}

	void testFieldTransactionIDGreaterThan32767() {
		DNSMessage question = new DNSQuestionBuilder()
				.setFieldTransactionID(65534)
				.build();
		assertEquals(65534, question.getFieldTransactionID());
		assertNotEquals(0, question.getFieldTransactionID());
		System.out.println(question);
	}

	@DisplayName("test Transaction ID too Small")
	@Test
	void testFieldTransactionIDRangeTooSmall() {
		Exception resultingException = assertThrows(
				java.lang.IllegalArgumentException.class,
				() -> {
					DNSMessage question = new DNSQuestionBuilder()
						.setFieldTransactionID(-1)
						.build();
				});
		assertTrue(resultingException.getMessage().contains("too small"));
	}

	@DisplayName("test Transaction ID too Big")
	@Test
	void testFieldTransactionIDRangeTooBig() {
		Exception resultingException = assertThrows(
				java.lang.IllegalArgumentException.class,
				() -> {
					DNSMessage question = new DNSQuestionBuilder()
						.setFieldTransactionID(65536)
						.build();
				});
		assertTrue(resultingException.getMessage().contains("too big"));
	}

	@DisplayName("test QR field - always false in Question")
	@Test
	void testFlagQRIsUnSet() {
		DNSMessage question = new DNSQuestionBuilder()
			.build();
		assertFalse(question.getFlagQR());
		// other flags also have expected values
		assertFalse(question.getFlagRD());
		assertFalse(question.getFlagCD());
	}

	@DisplayName("test RD field default - false")
	@Test
	void testFlagRDIsUnSetByDefault() {
		DNSMessage question = new DNSQuestionBuilder()
			.build();
		assertFalse(question.getFlagRD());
	}

	@DisplayName("test RD field - false")
	@Test
	void testFlagRDIsUnSet() {
		DNSMessage question = new DNSQuestionBuilder()
			.unsetFlagRD()
			.build();
		assertFalse(question.getFlagRD());
		// other flags have expected values
		assertFalse(question.getFlagQR());
		assertFalse(question.getFlagCD());
	}

	@DisplayName("test RD field - true")
	@Test
	void testFlagRDIsSet() {
		DNSMessage question = new DNSQuestionBuilder()
			.setFlagRD()
			.build();
		assertTrue(question.getFlagRD());
		// other flags have expected values
		assertFalse(question.getFlagQR());
		assertFalse(question.getFlagCD());
	}

	@DisplayName("test CD field default - false")
	@Test
	void testFlagCDIsUnSetByDefault() {
		DNSMessage question = new DNSQuestionBuilder()
			.build();
		assertFalse(question.getFlagCD());
	}

	@DisplayName("test CD field - false")
	@Test
	void testFlagCDIsUnSet() {
		DNSMessage question = new DNSQuestionBuilder()
			.unsetFlagCD()
			.build();
		assertFalse(question.getFlagCD());
		// other flags have expected values
		assertFalse(question.getFlagQR());
		assertFalse(question.getFlagRD());
	}

	@DisplayName("test CD field - true")
	@Test
	void testFlagCDIsSet() {
		DNSMessage question = new DNSQuestionBuilder()
			.setFlagCD()
			.build();
		assertTrue(question.getFlagCD());
		// other flags have expected values
		assertFalse(question.getFlagQR());
		assertFalse(question.getFlagRD());
	}



}
