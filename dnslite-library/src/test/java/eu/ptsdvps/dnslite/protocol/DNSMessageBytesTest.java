package eu.ptsdvps.dnslite.protocol;

import static org.junit.jupiter.api.Assertions.*;

import java.util.logging.Logger;

import org.junit.jupiter.api.*;

import eu.ptsdvps.dnslite.protocol.exception.DNSParseException;

class DNSMessageBytesTest {

	final static Logger log = Logger.getLogger(DNSMessageBytesTest.class.getName());

	// bytes are a question
	final byte[] message_question_faulty_1 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 640 (512 + 128)
		(byte)0b0_0000_001, (byte)0b0000_0000, // QR == false(0), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0001, (byte)0b1111_1110,  // QDCOUNT = 510 (query count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// section bytes - empty. invalid, does not match QDCOUNT
	};

	final byte[] message_question_faulty_2 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 640 (512 + 128)
		(byte)0b0_0000_001, (byte)0b0000_0000, // QR == false(0), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0000, (byte)0b0000_0001,  // QDCOUNT = 1
		(byte)0b0000_0000, (byte)0b0000_0000,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// section bytes - empty. invalid, label length -1
		// question section bytes, asking for www.example.com.
		(byte)-1,(byte)'w',(byte)'w',(byte)'w',(byte)0
	};

	final byte[] message_question_faulty_3 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 640 (512 + 128)
		(byte)0b0_0000_001, (byte)0b0000_0000, // QR == false(0), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0000, (byte)0b0000_0001,  // QDCOUNT = 1
		(byte)0b0000_0000, (byte)0b0000_0000,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// section bytes - empty. invalid, label length 64
		// question section bytes, asking for www.example.com.
		(byte)64,(byte)'w',(byte)'w',(byte)'w',(byte)0
	};

	final byte[] message_question_faulty_4 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 640 (512 + 128)
		(byte)0b0_0000_001, (byte)0b0000_0000, // QR == false(0), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0000, (byte)0b0000_0001,  // QDCOUNT = 1
		(byte)0b0000_0000, (byte)0b0000_0000,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// section bytes - empty. invalid, invalid question type specifier
		// question section bytes, asking for com.
		(byte)3,(byte)'c',(byte)'o',(byte)'m',(byte)0,
		// query type
		(byte)0b0000_0000 , (byte)0b0000_0011, // 3 (?)
		// query class
		(byte)0b0000_0000 , (byte)0b0000_0001 // 1 (internet class)
	};

	final byte[] message_question_faulty_5 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 640 (512 + 128)
		(byte)0b0_0000_001, (byte)0b0000_0000, // QR == false(0), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0000, (byte)0b0000_0001,  // QDCOUNT = 1
		(byte)0b0000_0000, (byte)0b0000_0000,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// section bytes - empty. invalid, invalid question type specifier
		// question section bytes, asking for com.
		(byte)3,(byte)'c',(byte)'o',(byte)'m',(byte)0,
		// query type
		(byte)0b0000_0000 , (byte)0b0000_0001, // 1 (A)
		// query class
		(byte)0b0000_0000 , (byte)0b0000_0010 // 2 (?)
	};

	final byte[] message_question_faulty_6 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 640 (512 + 128)
		(byte)0b0_0000_001, (byte)0b0000_0000, // QR == false(0), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0000, (byte)0b0000_0001,  // QDCOUNT = 1
		(byte)0b0000_0000, (byte)0b0000_0000,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// section bytes - empty. invalid, missing dnsclass
		// question section bytes, asking for com.
		(byte)3,(byte)'c',(byte)'o',(byte)'m',(byte)0,
		// query type
		(byte)0b0000_0000 , (byte)0b0000_0001, // 1 (A)
	};

	final byte[] message_question_faulty_7 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 640 (512 + 128)
		(byte)0b0_0000_001, (byte)0b0000_0000, // QR == false(0), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0000, (byte)0b0000_0001,  // QDCOUNT = 1
		(byte)0b0000_0000, (byte)0b0000_0000,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// section bytes - empty. invalid, missing type and dnsclass
		// question section bytes, asking for com.
		(byte)3,(byte)'c',(byte)'o',(byte)'m',(byte)0,
	};

	final byte[] message_question_2 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 641 (512 + 128 + 1)
		(byte)0b0_0000_001, (byte)0b0000_0000, // QR == false(0), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0000, (byte)0b0000_0001,  // QDCOUNT = 1 (query count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// question section bytes, asking for www.example.com.
		(byte)3,(byte)'w',(byte)'w',(byte)'w',
		(byte)7,(byte)'e',(byte)'x',(byte)'a',(byte)'m',(byte)'p',(byte)'l',(byte)'e',
		(byte)3,(byte)'c',(byte)'o',(byte)'m',
		(byte)0,
		// query type
		(byte)0b0000_0000 , (byte)0b0000_0001, // A
		// query class
		(byte)0b0000_0000 , (byte)0b0000_0001 // 1 (internet class)
	};

	final byte[] message_question_3 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 641 (512 + 128 + 1)
		(byte)0b0_0000_001, (byte)0b0000_0000, // QR == false(0), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0000, (byte)0b0000_0010,  // QDCOUNT = 2 (query count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// question section bytes, asking for www.example.com.
		(byte)3,(byte)'w',(byte)'w',(byte)'w',
		(byte)7,(byte)'e',(byte)'x',(byte)'a',(byte)'m',(byte)'p',(byte)'l',(byte)'e',
		(byte)3,(byte)'c',(byte)'o',(byte)'m',
		(byte)0,
		// query type
		(byte)0b0000_0000 , (byte)0b0000_0001, // A
		// query class
		(byte)0b0000_0000 , (byte)0b0000_0001, // 1 (internet class)
		// question section bytes, asking for example.com.
		(byte)7,(byte)'e',(byte)'x',(byte)'a',(byte)'m',(byte)'p',(byte)'l',(byte)'e',
		(byte)3,(byte)'c',(byte)'o',(byte)'m',
		(byte)0,
		// query type
		(byte)0b0000_0000 , (byte)0b0000_0001, // 1 (A)
		// query class
		(byte)0b0000_0000 , (byte)0b0000_0001 // 1 (internet class)
	};

	final byte[] message_question_4 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 641 (512 + 128 + 1)
		(byte)0b0_0000_001, (byte)0b0000_0000, // QR == false(0), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0000, (byte)0b0000_0001,  // QDCOUNT = 1 (query count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// question section bytes, asking for www.amazon.co.uk
		(byte)3,(byte)'w',(byte)'w',(byte)'w',
		(byte)6,(byte)'a',(byte)'m',(byte)'a',(byte)'z',(byte)'o',(byte)'n',
		(byte)2,(byte)'c',(byte)'o',
		(byte)2,(byte)'u',(byte)'k',
		(byte)0,
		// query type
		(byte)0b0000_0000 , (byte)0b0001_1100, // 28 (AAAA)
		// query class
		(byte)0b0000_0000 , (byte)0b0000_0001, // 1 (internet class)
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
		DNSMessage message = DNSMessage.fromBytes(message_question_faulty_1);
		assertInstanceOf(DNSMessage.class, message);
		assertEquals(12, message.getLength());
	}

	@DisplayName("parse message from bytes array. check Flags")
	@Test
	void dnsMessage_getFlags_1() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_faulty_1);
		assertFalse(message.getFlagQR());
		assertFalse(message.getFlagCD());
		assertTrue(message.getFlagRD());
	}

	@DisplayName("parse message from bytes array. check flags")
	@Test
	void dnsMessage_getFlags_2() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_answer_1);
		assertTrue(message.getFlagQR());
		assertFalse(message.getFlagCD());
		assertTrue(message.getFlagRD());
	}

	@DisplayName("parse message from bytes array. check Message is immutable")
	@Test
	void dnsMessageisImmutable_1() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_faulty_1);
		assertFalse(message.getFlagQR());
		message_question_faulty_1[2] = (byte)0b1000_0001; // set QR flag in input array
		assertFalse(message.getFlagQR()); // should not see the change
	}

	@DisplayName("parse message from bytes array. check Message is immutable")
	@Test
	void dnsMessageisImmutable_2() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_answer_1);
		assertTrue(message.getFlagQR());
		message_question_faulty_1[2] = (byte)0b1000_0001; // set QR flag in input array
		assertTrue(message.getFlagQR()); // should not see the change
	}

	@DisplayName("parse message with 1 question from bytes array. check length")
	@Test
	void dnsQuestionLength() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_2);
		assertEquals(33, message.getLength());
	}

	@DisplayName("parse message with 0 questions from bytes array. check number of questions")
	@Test
	void dnsQuestionSectionLength_1() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_faulty_1);
		assertEquals(510, message.getNumQuestions());
	}

	@DisplayName("throw Exception on getQuestion from Message where QDCount does not match section")
	@Test
	void dnsQuestionSectionLength_doesNotMatchQD() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_faulty_1);
		assertThrows( DNSParseException.class, () -> message.getQuestion(0));
	}

	@DisplayName("throw Exception on getQuestion from Message where a label length is <0")
	@Test
	void dnsQuestion_LabelLengthSmaller0() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_faulty_2);
		assertThrows( DNSParseException.class, () -> message.getQuestion(0));
	}

	@DisplayName("throw Exception on getQuestion from Message where a label length is >64")
	@Test
	void dnsQuestion_labelLengthGreater63() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_faulty_3);
		assertThrows( DNSParseException.class, () -> message.getQuestion(0));
	}

	@DisplayName("throw Exception on getQuestion from Message where invalid query type")
	@Test
	void dnsQuestion_invalidQueryType() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_faulty_4);
		assertThrows( DNSParseException.class, () -> message.getQuestion(0));
	}

	@DisplayName("throw Exception on getQuestion from Message where invalid dns class")
	@Test
	void dnsQuestion_invalidDNSClass() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_faulty_5);
		assertThrows( DNSParseException.class, () -> message.getQuestion(0));
	}

	@DisplayName("throw Exception on getQuestion from Message where missing dns class")
	@Test
	void dnsQuestion_missingDNSClass() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_faulty_6);
		assertThrows( DNSParseException.class, () -> message.getQuestion(0));
	}

	@DisplayName("throw Exception on getQuestion from Message where missing query type and dns class")
	@Test
	void dnsQuestion_missingQueryType() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_faulty_7);
		assertThrows( DNSParseException.class, () -> message.getQuestion(0));
	}

	@DisplayName("parse message with 1 questions from bytes array. check number of questions")
	@Test
	void dnsQuestionSectionLength_2() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_2);
		assertEquals(1, message.getNumQuestions());
	}

	@DisplayName("parse message with 1 question (A www.example.com) from bytes array. check contents")
		@Test
	void dnsQuestion_check_contents_1() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(
				message_question_2);

		assertEquals(1, message.getNumQuestions());
		DNSMessage.DNSQuestion question = message.getQuestion(0);
		assertEquals("www.example.com", question.getName()); // question with index 0
		assertEquals(DNSMessage.Type.A, question.getType());
		assertEquals(DNSMessage.DNSClass.INET, question.getDNSClass());
	}

	@DisplayName("parse message with 1 question (AAAA www.amazon.co.uk) from bytes array. check name")
	@Test
	void dnsQuestion_check_contents_2() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(
				message_question_4);

		assertEquals(1, message.getNumQuestions());
		DNSMessage.DNSQuestion question = message.getQuestion(0);
		assertEquals("www.amazon.co.uk", question.getName()); // question with index 0
		assertEquals(DNSMessage.Type.AAAA, question.getType());
		assertEquals(DNSMessage.DNSClass.INET, question.getDNSClass());
		// get Cached name
		assertEquals("www.amazon.co.uk", question.getName()); // question with index 0

	}

	@DisplayName("parse message with 2 question (A www.example.com, A example.com) from bytes array. check name")
	@Test
	void dnsQuestion_check_contents_3() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(
				message_question_3);
		assertEquals(2, message.getNumQuestions());

		DNSMessage.DNSQuestion question = message.getQuestion(0);
		assertEquals("www.example.com", question.getName()); // question with index 0
		assertEquals(DNSMessage.Type.A, question.getType());
		assertEquals(DNSMessage.DNSClass.INET, question.getDNSClass());

		question = message.getQuestion(1);
		assertEquals("example.com", question.getName()); // question with index 0
		assertEquals(DNSMessage.Type.A, question.getType());
		assertEquals(DNSMessage.DNSClass.INET, question.getDNSClass());
	}

}
