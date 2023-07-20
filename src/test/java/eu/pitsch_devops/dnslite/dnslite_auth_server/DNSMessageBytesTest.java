package eu.pitsch_devops.dnslite.dnslite_auth_server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.logging.Logger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DNSMessageBytesTest {

	final static Logger log = Logger.getLogger(DNSMessageBytesTest.class.getName());

	// bytes are a question
	final byte[] message_question_1 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 640 (512 + 128)
		(byte)0b0_0000_001, (byte)0b0000_0000, // QR == false(0), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0000, (byte)0b0000_0001,  // QDCOUNT = 1 (query count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// section bytes
	};

	final byte[] message_answer_1 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 640 (512 + 128)
		(byte)0b1_0000_001, (byte)0b0000_0000, // QR == true(1), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0000, (byte)0b0000_0000,  // QDCOUNT = 1 (query count)
		(byte)0b0000_0000, (byte)0b0000_0001,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// section bytes
	};

	@DisplayName("parse message from bytes array, check length")
	@Test
	void dnsMessageFromBytesGetLength() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_1);
		assertEquals(message.getLength(), 12);
	}

	@DisplayName("parse message from bytes array, check is Question")
	@Test
	void dnsMessageFromBytesIsQuestion() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_1);
//		log.severe(" message is " + (message == null ? "null" : "not null"));
		assertInstanceOf(DNSQuestion.class, message);
	}

	@DisplayName("parse message from bytes array, check is not Question")
	@Test
	void dnsMessageFromBytesIsNotQuestion() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_answer_1);
		assertFalse(message instanceof DNSQuestion);
	}

	@DisplayName("parse message from bytes array, check is Answer")
	@Test
	void dnsMessageFromBytesIsAnswer() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_answer_1);
		assertInstanceOf(DNSAnswer.class, message);
	}

	@DisplayName("parse message from bytes array. check is not Answer")
	@Test
	void dnsMessageFromBytesIsNotAnswer() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_1);
		assertFalse(message instanceof DNSAnswer);
	}

}
