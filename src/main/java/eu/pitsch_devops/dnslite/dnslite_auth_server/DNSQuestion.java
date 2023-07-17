package eu.pitsch_devops.dnslite.dnslite_auth_server;

import java.util.function.BooleanSupplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DNSQuestion extends DNSMessage {
	
	static final Logger log = Logger.getLogger(DNSQuestion.class.getName());
	// constants for bitwise operations
	private static final int MAX_UNSIZED_SHORT = 0b1111_1111_1111_1111;
	private static final int MIN_UNSIZED_SHORT = 0;
	
	// constants to set one-bit flags or to set flag, invert then and to unset flag
	// in 3rd byte
	private static final byte QR_FLAG = (byte)0b1000_0000; // casting necessary for ints > 127. bits preserved
	private static final byte AA_FLAG= (byte)0b0000_0100;
	private static final byte TC_FLAG= (byte)0b0000_0010;
	private static final byte RD_FLAG= (byte)0b0000_0001;
	
	static {
		log.fine("QR_FLAG: " + QR_FLAG);
		log.severe("QR_FLAG: " + QR_FLAG);
		log.severe("QR_FLAG: " + (int)QR_FLAG);
		log.severe("QR_FLAG: " + Byte.toUnsignedInt(QR_FLAG));
	}
	
	// in 4th byte
	private static final byte RA_FLAG = (byte)0b1000_0000;
	private static final byte Z_FLAG = (byte)0b0100_0000;
	private static final byte AD_FLAG = (byte)0b0010_0000;
	private static final byte CD_FLAG = (byte)0b0001_0000;
	
	// bits in 3rd byte that are not part of the opcode. use with & to unset OPCODE bits and leave others unchanged
	private static final byte OPCODE_MASK = (byte)0b1000_0111;  
	// constants to set OPCODE. set with |, (after applying mask with &)
	private static final byte OPCODE_QUERY_BITS = (byte)0b0000_0000;
	private static final byte OPCODE_IQUERY_BITS = (byte)0b0000_1000;
	private static final byte OPCODE_STATUS_BITS = (byte)0b0001_0000;
	private static final byte OPCODE_NOTIFY_BITS = (byte)0b0010_0000;
	private static final byte OPCODE_UPDATE_BITS = (byte)0b0010_1000;
	
	// bits in 3rd byte that are not part of the opcode. use with & to unset OPCODE bits and leave others unchanged
	private static final byte RCODE_MASK = (byte)0b1111_0000; // bits in 4th byte that are not part of the rcode 
	// constants to set RCODE. set with |, (after applying mask with &)
	private static final byte RCODE_NOERR_BITS = (byte)0b0000_0000;
	private static final byte RCODE_FORMERR_BITS = (byte)0b0000_0001;
	private static final byte RCODE_SERVFAIL_BITS = (byte)0b0000_0010;
	private static final byte RCODE_NXDOMAIN_BITS = (byte)0b0000_0011;
	private static final byte RCODE_NOTIMP_BITS = (byte)0b0000_0100;
	private static final byte RCODE_REFUSED_BITS = (byte)0b0000_0101;
	private static final byte RCODE_YXDOMAIN_BITS = (byte)0b0000_0110;
	private static final byte RCODE_YXRRSET_BITS = (byte)0b0000_0111;
	private static final byte RCODE_NXRRSET_BITS = (byte)0b0000_1000;
	private static final byte RCODE_NOTAUTH_BITS = (byte)0b0000_1001;
	private static final byte RCODE_NOTZONE_BITS = (byte)0b0000_1010;
	
	
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
			if (txid > MAX_UNSIZED_SHORT)
				throw new IllegalArgumentException(
					String.format("TransactionID too big : was {}: should be <= 65535", txid));
			if (txid < MIN_UNSIZED_SHORT)
				throw new IllegalArgumentException(
					String.format("TransactionID too small: was {}: should be >= 0", txid));
			question.fixedSizeHeader[0] = (byte)(txid >> 8);
			question.fixedSizeHeader[1] = (byte)txid;
			return this;
		}
		
		// private  - always set for any questions
		private DNSQuestionBuilder setFlagQR(boolean value) {
			if(value) question.fixedSizeHeader[2] |= QR_FLAG;
			else question.fixedSizeHeader[2] &= ~QR_FLAG;
			return this;
		}
		
		// public - client must be able to choose
		public DNSQuestionBuilder setFlagRD(boolean value) {
			if(value) question.fixedSizeHeader[2] |= RD_FLAG;
			else question.fixedSizeHeader[2] &= ~RD_FLAG;
			return this;
		}
		
		// public - client must be able to choose
		public DNSQuestionBuilder setFlagCD(boolean value) {
			if(value) question.fixedSizeHeader[3] |= CD_FLAG;
			else question.fixedSizeHeader[3] &= ~CD_FLAG;
			return this;
		}
		
		
	}
	
	public int getFieldTransactionID() {
		return (fixedSizeHeader[0] << 8) + Byte.toUnsignedInt(fixedSizeHeader[1]);
	}

	public boolean getFlagQR() {
		return (fixedSizeHeader[2] & QR_FLAG) == QR_FLAG;
	}
	
	public boolean getFlagRD() {
		return (fixedSizeHeader[2] & RD_FLAG) == RD_FLAG;
	}
	
	public boolean getFlagCD() {
		return (fixedSizeHeader[3] & CD_FLAG) == CD_FLAG;
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
