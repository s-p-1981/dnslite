package eu.pitsch_devops.dnslite.dnslite_auth_server;

import java.util.logging.Logger;

public class DNSQuestion extends DNSMessage {

	static final Logger log = Logger.getLogger(DNSQuestion.class.getName());


	// first 12 bytes - Header
	byte[] fixedSizeHeader;
	byte[] varLengthSections;


	private DNSQuestion() {

	}

	public static class DNSQuestionBuilder {

		DNSQuestion question;

		public DNSQuestionBuilder() {
			question = new DNSQuestion();
			question.fixedSizeHeader = new byte[12];
			// set Opcode field
			question.fixedSizeHeader[0] = (byte)((question.fixedSizeHeader[0] & OPCODE_MASK) | OPCODE_QUERY_BITS);
			// set QR field  - must be set for question
			setFlagQR(true);
			// set RD flag default
			setFlagRD(false);
			// set CD flag default
			setFlagCD(false);
		}

		public DNSQuestion build() {
			return question;
		}

		public DNSQuestionBuilder setFieldTransactionID(int txid) {
			if (txid > DNSMessage.MAX_UNSIZED_SHORT)
				throw new IllegalArgumentException(
					String.format("TransactionID too big : was {}: should be <= 65535", txid));
			if (txid < MIN_UNSIZED_SHORT)
				throw new IllegalArgumentException(
					String.format("TransactionID too small: was {}: should be >= 0", txid));
			question.fixedSizeHeader[0] = (byte)(txid >> 8);
			question.fixedSizeHeader[1] = (byte)txid;
			return this;
		}

		// private  - always set for questions, unset for answers
		private DNSQuestionBuilder setFlagQR(boolean value) {
			if(value) question.fixedSizeHeader[Flag.QR.bytePos] |= Flag.QR.bit;
			else question.fixedSizeHeader[Flag.QR.bytePos] &= ~Flag.QR.bit;
			return this;
		}

		// public - client must be able to choose
		public DNSQuestionBuilder setFlagRD(boolean value) {
			if(value) question.fixedSizeHeader[Flag.RD.bytePos] |= Flag.RD.bit;
			else question.fixedSizeHeader[Flag.RD.bytePos] &= ~Flag.RD.bit;
			return this;
		}

		// public - client must be able to choose
		public DNSQuestionBuilder setFlagCD(boolean value) {
			if(value) question.fixedSizeHeader[Flag.CD.bytePos] |= Flag.CD.bit;
			else question.fixedSizeHeader[Flag.CD.bytePos] &= ~Flag.CD.bit;
			return this;
		}


	}

	public int getFieldTransactionID() {
		return (fixedSizeHeader[0] << 8) + Byte.toUnsignedInt(fixedSizeHeader[1]);
	}

	public boolean getFlag(Flag flag) {
		return (fixedSizeHeader[flag.bytePos] & flag.bit) == flag.bit;
	}

	public boolean getFlagQR() {
		return getFlag(Flag.QR);
	}

	public boolean getFlagRD() {
		return getFlag(Flag.RD);
	}

	public boolean getFlagCD() {
		return getFlag(Flag.CD);
	}

	@Override
	public String toString() {
		String formatString = ""
				+ "DNSQuestion\n"
				+ "Flags: (QR    OPC   AA    TC    RD    RA    Z     AD    DC   )\n"
				+ "(       %1$b %2$d %3$b %4$b %5$b %6$b %7$b %8$b %9$b ";
		return String.format(formatString, true, true, true, true, false, false, false, true, true);
	}

}
