package eu.ptsdvps.dnslite.protocol;

import java.util.Arrays;
import java.util.logging.Logger;

public class DNSQuestion extends DNSMessage {

	static final Logger log = Logger.getLogger(DNSQuestion.class.getName());

	/* intent : (package) private constructors only, class should be immutable thus
	 * effectively final
	 */
	DNSQuestion(byte[] messageBytes) {
		super(messageBytes);
	}

	public static class DNSQuestionBuilder extends DNSMessage.DNSMessageBuilder {

//		private byte[] builderBytes = new byte[512];
//		private short realLength = 0;

		public DNSQuestionBuilder() {
			setOPCodetoQuery();
			// QR flag always false for questions
			unsetFlagQR();
			// unset other flags as default option.
			unsetFlagRD();
			unsetFlagCD();
			realLength = 12; // length of headers
		}

		public DNSQuestion build() {
			return new DNSQuestion(Arrays.copyOf(builderBytes, realLength));
		}

		public void setOPCodetoQuery() {
//			builderBytes[0] = (byte)((builderBytes[0] & OPCode.OPCODE_MASK) | OPCode.QUERY.bits);
			builderBytes[0] = OPCode.apply(OPCode.QUERY, builderBytes[0]);
		}

		public DNSQuestionBuilder setFieldTransactionID(int txid) {
			if (txid > DNSMessage.MAX_UNSIZED_SHORT)
				throw new IllegalArgumentException(
					String.format("TransactionID too big : was {}: should be <= 65535", txid));
			if (txid < MIN_UNSIZED_SHORT)
				throw new IllegalArgumentException(
					String.format("TransactionID too small: was {}: should be >= 0", txid));
			builderBytes[0] = (byte)(txid >> 8);
			builderBytes[1] = (byte)txid;
			return this;
		}

		// private  - always set for questions, always unset for answers
		DNSQuestionBuilder setFlagQR() {
			setFlag(Flag.QR, true);
			return this;
		}

		DNSQuestionBuilder unsetFlagQR() {
			setFlag(Flag.QR, false);
			return this;
		}

		/* setFlagRD
		 *
		 * Call this method to set the RD ("Recursion Desired") flag in the DNSQuestion to 1.
		 *
		 * This is meant to be set on a normal DNS client and should trigger a the DNS-Server with that capality (a resolver) to do the iterative
		 * queries needed to fill your query.
		 *
		 */
		public DNSQuestionBuilder setFlagRD() {
			setFlag(Flag.RD, true);
			return this;
		}

		public DNSQuestionBuilder unsetFlagRD() {
			setFlag(Flag.RD, false);
			return this;
		}

		/* setFlagRD
		 *
		 * Call this method to set the CD ("Checking Disabled") flag in the DNSQuestion to 1.
		 *
		 * This is set by a resolver and must be echoed in the answer.
		 * This is not meant to be set on a normal DNS client, but may be returned in the answer.
		 *
		 * It signifies DNSSEC was not used.
		 *
		 */
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
