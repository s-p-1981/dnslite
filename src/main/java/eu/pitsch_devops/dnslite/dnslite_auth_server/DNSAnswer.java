package eu.pitsch_devops.dnslite.dnslite_auth_server;

import java.util.logging.Logger;

public class DNSAnswer extends DNSMessage {

	static final Logger log = Logger.getLogger(DNSAnswer.class.getName());

	public DNSAnswer(byte[] messageBytes) {
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
