package eu.pitsch_devops.dnslite.dnslite_auth_server;

import java.util.function.BooleanSupplier;

public class DNSQuestion extends DNSMessage {
	
	// constants for bitwise operations
	private static final int MAX_UNSIZED_SHORT = 0b1111_1111_1111_1111;
	private static final int MIN_UNSIZED_SHORT = 0;
	private static final int QR_FLAG_MASK = 0b0000_0000_0000_0000_1000_0000_0000_0000;
	
	// first 12 bytes
	int[] fixedSizeHeader;
	
	private short transactionID;
	
	private DNSQuestion() {
		
	}
	
	public static class DNSQuestionBuilder {
		
		DNSQuestion question;
		
		public DNSQuestionBuilder() {
			question = new DNSQuestion();
			question.fixedSizeHeader = new int[3];
			question.fixedSizeHeader[0] |= QR_FLAG_MASK;
		}
		
		public DNSQuestion build() {
			return question;
		}

		public DNSQuestionBuilder setFieldTransactionID(int i) {
			if (i > MAX_UNSIZED_SHORT)
				throw new IllegalArgumentException(
					String.format("TransactionID too big : was {}: should be <= 65535", i));
			if (i < MIN_UNSIZED_SHORT)
				throw new IllegalArgumentException(
					String.format("TransactionID too small: was {}: should be >= 0", i));
			question.fixedSizeHeader[0] = i << 16;
			return this;
		}
		
	}
	
	public int getFieldTransactionID() {
		return fixedSizeHeader[0] >> 16;
	}

	public boolean getFieldQR() {
		return (fixedSizeHeader[0] & QR_FLAG_MASK) == QR_FLAG_MASK;
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
