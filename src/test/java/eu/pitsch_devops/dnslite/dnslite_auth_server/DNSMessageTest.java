package eu.pitsch_devops.dnslite.dnslite_auth_server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DNSMessageTest {
	
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
	
	@DisplayName("test Transaction ID field")
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
		
	@DisplayName("test Transaction ID field")
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
	void testFieldQRWasSet() {
		DNSQuestion question = new DNSQuestion.DNSQuestionBuilder()
			.build();
		assertTrue(question.getFieldQR());
	}
	
	
	

}

	
	
	
	
	