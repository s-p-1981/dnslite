package eu.pitsch_devops.dnslite.dnslite_auth_server;

import java.util.logging.Logger;

public class DNSQuestion extends DNSMessage {

	static final Logger log = Logger.getLogger(DNSQuestion.class.getName());


	// first 12 bytes - Header
	byte[] varLengthSections;


	private DNSQuestion() {
	}

	public static class DNSQuestionBuilder {

		private DNSQuestion question;

		public DNSQuestionBuilder() {
			question = new DNSQuestion();
			question.fixedSizeHeader = new byte[12];
			// set Opcode field
			setOPCodetoQuery();
			// QR flag always false for questions
			setFlagQR();
			// unset other flags as default option.
			// superfluous, should already be in unset state when fixedSizeHeader is initialized
			unsetFlagRD();
			unsetFlagCD();
		}

		public DNSQuestion build() {
			return question;
		}

		public void setOPCodetoQuery() {
			question.fixedSizeHeader[0] = (byte)((question.fixedSizeHeader[0] & OPCode.OPCODE_MASK) | OPCode.QUERY.bits);
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


		private void setFlag(Flag flag, boolean value) {
			// TODO: move computation into Flag enum, and just use it here?
			if(value) question.fixedSizeHeader[flag.bytePos] |=flag.bit;
			else question.fixedSizeHeader[flag.bytePos] &= ~flag.bit;
		}

		// private  - always set for questions, always unset for answers
		private DNSQuestionBuilder setFlagQR() {
			setFlag(Flag.QR, true);
			return this;
		}

		private DNSQuestionBuilder unsetFlagQR() {
			setFlag(Flag.QR, false);
			return this;
		}

		// public - client must be able to choose these flags
		public DNSQuestionBuilder setFlagRD() {
			setFlag(Flag.RD, true);
			return this;
		}

		public DNSQuestionBuilder unsetFlagRD() {
			setFlag(Flag.RD, false);
			return this;
		}

		public DNSQuestionBuilder setFlagCD() {
			setFlag(Flag.CD, true);
			return this;
		}

		public DNSQuestionBuilder unsetFlagCD() {
			setFlag(Flag.CD, false);
			return this;
		}

	} // End of DNSQuestionBuilder

} // End of DNSQuestion
