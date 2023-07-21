package eu.ptsdvps.dnslite.protocol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.logging.Logger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import eu.ptsdvps.dnslite.protocol.exception.DNSParseException;

class DNSMessageBytesTest {

	final static Logger log = Logger.getLogger(DNSMessageBytesTest.class.getName());

	// bytes are a question
	final byte[] message_question_1 = new byte[] {
		// fixed size header
		(byte)0b0000_0010, (byte)0b1000_0000,  // TXID 640 (512 + 128)
		(byte)0b0_0000_001, (byte)0b0000_0000, // QR == false(0), RD == true, all others false, OPC==0(Query), RCode==0(NoError)
		(byte)0b0000_0001, (byte)0b1111_1110,  // QDCOUNT = 510 (query count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ANCOUNT = 0 (answer count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // NSCOUNT = 0 (authority record count)
		(byte)0b0000_0000, (byte)0b0000_0000,  // ADCOUNT = 0 (additional information count)
		// section bytes
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
		DNSMessage message = DNSMessage.fromBytes(message_question_1);
		assertEquals(12, message.getLength());
	}

	@DisplayName("parse message from bytes array, check is Question")
	@Test
	void dnsMessageFromBytesIsQuestion() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_1);
//		log.severe(" message is " + (message == null ? "null" : "not null"));
		assertInstanceOf(DNSMessageQuestion.class, message);
	}

	@DisplayName("parse message from bytes array, check is not Question")
	@Test
	void dnsMessageFromBytesIsNotQuestion() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_answer_1);
		assertFalse(message instanceof DNSMessageQuestion);
	}

	@DisplayName("parse message from bytes array, check is Answer")
	@Test
	void dnsMessageFromBytesIsAnswer() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_answer_1);
		assertInstanceOf(DNSMessageAnswer.class, message);
	}

	@DisplayName("parse message from bytes array. check is not Answer")
	@Test
	void dnsMessageFromBytesIsNotAnswer() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_1);
		assertFalse(message instanceof DNSMessageAnswer);
	}

	@DisplayName("parse message from bytes array. check immutable")
	@Test
	void dnsMessageisImmutable() throws DNSParseException {
		DNSMessage message = DNSMessage.fromBytes(message_question_1);
		assertFalse(message.getFlagQR());
		message_question_1[2] = (byte)0b1000_0001; // set QR flag in input array
		assertFalse(message.getFlagQR()); // should
	}

	@DisplayName("parse message with 1 question from bytes array. check length")
	@Test
	void dnsQuestionLength() throws DNSParseException {
		DNSMessageQuestion message = (DNSMessageQuestion)DNSMessage.fromBytes(message_question_2);
		assertEquals(33, message.getLength());
	}
	@DisplayName("parse message with 1 questions from bytes array. check number of questions")
	@Test
	void dnsQuestionSectionLength_1() throws DNSParseException {
		DNSMessageQuestion message = (DNSMessageQuestion)DNSMessage.fromBytes(message_question_1);
		assertEquals(510, message.getNumQuestions());
	}

	@DisplayName("parse message with 1 questions from bytes array. check number of questions")
	@Test
	void dnsQuestionSectionLength_2() throws DNSParseException {
		DNSMessageQuestion message = (DNSMessageQuestion)DNSMessage.fromBytes(message_question_2);
		assertEquals(1, message.getNumQuestions());
	}

	@DisplayName("parse message with 1 question (A www.example.com) from bytes array. check contents")
		@Test
	void dnsQuestion_check_contents_1() throws DNSParseException {
		DNSMessageQuestion message = (DNSMessageQuestion)DNSMessage.fromBytes(
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
		DNSMessageQuestion message = (DNSMessageQuestion)DNSMessage.fromBytes(
				message_question_4);

		assertEquals(1, message.getNumQuestions());
		DNSMessage.DNSQuestion question = message.getQuestion(0);
		assertEquals("www.amazon.co.uk", question.getName()); // question with index 0
		assertEquals(DNSMessage.Type.AAAA, question.getType());
		assertEquals(DNSMessage.DNSClass.INET, question.getDNSClass());
	}

	@DisplayName("parse message with 2 question (A www.example.com, A example.com) from bytes array. check name")
	@Test
	void dnsQuestion_check_contents_3() throws DNSParseException {
		DNSMessageQuestion message = (DNSMessageQuestion)DNSMessage.fromBytes(
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
