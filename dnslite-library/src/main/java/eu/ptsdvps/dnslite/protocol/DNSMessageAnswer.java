package eu.ptsdvps.dnslite.protocol;

import java.util.logging.Logger;

public class DNSMessageAnswer extends DNSMessage {

	static final Logger log = Logger.getLogger(DNSMessageAnswer.class.getName());

	public DNSMessageAnswer(byte[] messageBytes) {
		super(messageBytes);
	}

	public static class DNSAnswerBuilder {
		private byte[] builderBytes = new byte[512];
		private short realLength = 0;
	}

//	public DNSAnswerBuilder() {
//		setFlagQR();
//	}

}
