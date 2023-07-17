package eu.pitsch_devops.dnslite.dnslite_auth_server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DNSQuestionBuilderTest {

	@DisplayName("Test MessageBuilder")
	@Test
	void testBuildQuestion() {
		DNSQuestion question = new DNSQuestion.DNSQuestionBuilder().build();
	}

	@DisplayName("test Transaction ID field")
	@Test
	void testFieldTransactionID() {
	DNSQuestion question = new DNSQuestion.DNSQuestionBuilder()
				.setFieldTransactionID(1000)
				.build();
		assertEquals(1000, question.getFieldTransactionID());
		assertNotEquals(0, question.getFieldTransactionID());
	}

	void testFieldTransactionIDGreaterThan32767() {
		DNSQuestion question = new DNSQuestion.DNSQuestionBuilder()
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
					DNSQuestion question = new DNSQuestion.DNSQuestionBuilder()
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
					DNSQuestion question = new DNSQuestion.DNSQuestionBuilder()
						.setFieldTransactionID(65536)
						.build();
				});
		assertTrue(resultingException.getMessage().contains("too big"));
	}

	@DisplayName("test QR field")
	@Test
	void testFlagQRIsSet() {
		DNSQuestion question = new DNSQuestion.DNSQuestionBuilder()
			.build();
		assertTrue(question.getFlagQR());
	}

	@DisplayName("test RD field - false")
	@Test
	void testFlagRDIsUnSetByDefault() {
		DNSQuestion question = new DNSQuestion.DNSQuestionBuilder()
			.build();
		assertFalse(question.getFlagRD());
	}

	@DisplayName("test RD field - false")
	@Test
	void testFlagRDIsUnSet() {
		DNSQuestion question = new DNSQuestion.DNSQuestionBuilder()
			.setFlagRD(false)
			.build();
		assertFalse(question.getFlagRD());
	}

	@DisplayName("test RD field - true")
	@Test
	void testFlagRDIsSet() {
		DNSQuestion question = new DNSQuestion.DNSQuestionBuilder()
			.setFlagRD(true)
			.build();
		assertTrue(question.getFlagRD());
	}

	@DisplayName("test CD field default - false")
	@Test
	void testFlagCDIsUnSetByDefault() {
		DNSQuestion question = new DNSQuestion.DNSQuestionBuilder()
			.build();
		assertFalse(question.getFlagCD());
	}

	@DisplayName("test RD field - false")
	@Test
	void testFlagCDIsUnSet() {
		DNSQuestion question = new DNSQuestion.DNSQuestionBuilder()
			.setFlagCD(false)
			.build();
		assertFalse(question.getFlagCD());
	}

	@DisplayName("test RD field - true")
	@Test
	void testFlagCDIsSet() {
		DNSQuestion question = new DNSQuestion.DNSQuestionBuilder()
			.setFlagCD(true)
			.build();
		assertTrue(question.getFlagCD());
	}



}





