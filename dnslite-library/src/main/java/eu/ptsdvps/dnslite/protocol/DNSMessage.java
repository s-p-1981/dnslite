package eu.ptsdvps.dnslite.protocol;

import java.util.Arrays;
import java.util.logging.Logger;

import eu.ptsdvps.dnslite.protocol.exception.DNSParseException;

/*
 * abstract, but will contain most of the actual Code, since DNSMessages, whether Question or Answers
 * or quite similar in their overall structure, and share many aspects
 *
 * Subclasses mainly exist to differentiate between Question and Answer in client code
 */
abstract class DNSMessage {

	/* static fields */
	// constants for bitwise operations
	static final int MAX_UNSIZED_SHORT = 0b1111_1111_1111_1111;
	static final int MIN_UNSIZED_SHORT = 0;

	static Logger log = Logger.getLogger("logger for " + DNSMessage.class.getName());

	enum Flag {
		QR((byte)0b1000_0000, (byte)2),
		AA((byte)0b0000_0100, (byte)2),
		TC((byte)0b0000_0010, (byte)2),
		RD((byte)0b0000_0001, (byte)2),
		RA((byte)0b1000_0000, (byte)3),
		 Z((byte)0b0100_0000, (byte)3),
		AD((byte)0b0010_0000, (byte)3),
		CD((byte)0b0001_0000, (byte)3);

		final byte bit;
		final byte bytePos;

		Flag(byte setBit, byte bytePos) {
			this.bit = setBit;
			this.bytePos = bytePos;
		}
	}

	enum OPCode {
		// TODO: use and test
		// OPCode bits to set. use with `|` . but first clear with `&` using `OPCODE_MASK`
		QUERY ((byte)0b0000_0000),
		IQUERY((byte)0b0000_1000),
		STATUS((byte)0b0001_0000),
		NOTIFY((byte)0b0010_0000),
		UPDATE((byte)0b0010_1000);

		// bits in 3rd byte that are not part of the opcode.
		// use with `&` to unset OPCODE bits and leave others unchanged
		static final byte OPCODE_MASK = (byte)0b1000_0111;

		final byte bits;
		OPCode(byte bits) {
			this.bits = bits;
		}

		static byte apply(OPCode code, byte inByte) {
			return (byte)((inByte & OPCode.OPCODE_MASK) | OPCode.QUERY.bits);
		}

	}

	enum RCode {
		NOERR((byte)0b0000_0000),
		FORMERR((byte) 0b0000_0001),
		SERVFAIL((byte) 0b0000_0010),
		NXDOMAIN((byte) 0b0000_0011),
		NOTIMP((byte) 0b0000_0100),
		REFUSED((byte)0b0000_0101),
		YXDOMAIN((byte)0b0000_0110),
		YXRRSET((byte) 0b0000_0111),
		NXRRSET((byte)0b0000_1000),
		NOTAUTH((byte) 0b0000_1001),
		NOTZONE((byte) 0b0000_1010);


		// bits in 4th byte that are not part of the rcode. use with & to unset OPCODE bits and leave others unchanged
		static final byte RCODE_MASK = (byte)0b1111_0000; // bits in 4th byte that are not part of the rcode
		final byte bits;
		RCode(byte bits) {
			this.bits = bits;
		}
	}

	enum DNSClass {
		INET(1),
		NONE(254),
		ALL(255);

		byte higher;
		byte lower;

		DNSClass(int bits){ // just use int, must allow values of up to 65535 as per spec
			this.higher = 0; // currently no possible values have the 8 high bits set
			this.lower = (byte)bits; // same bits as in lowest 8 bits of int
		}

		static DNSClass fromInt(int dnsclassVal) {
			switch (dnsclassVal) {
			case 1:
				return INET;
			case 254:
				return NONE;
			case 255:
				return ALL;
			default:
				throw new IllegalArgumentException("Invalid DNSClass");
			}
		}
	}

	enum Type {
		A(1),
		AAAA(28);

		byte higher;
		byte lower;
		Type(int bits) {
			this.higher = 0; // currently no possible values have the 8 high bits set
			this.lower = (byte)bits; // same bits as in loweet 8 bits of int
		}

		static Type fromInt(int typeVal) {
			switch (typeVal) {
			case 1:
				return A;
			case 28:
				return AAAA;
			// TODO: add all currently valid types
			default:
				throw new IllegalArgumentException("Invalid Type");
			}
		}
	}

	static abstract class DNSMessageBuilder {

		byte[] builderBytes = new byte[512];
		short realLength = 0;

		void setFlag(Flag flag, boolean value) {
			if(value) builderBytes[flag.bytePos] |=flag.bit;
			else builderBytes[flag.bytePos] &= ~flag.bit;
		}
	}

	/* fields */
	byte[] messageBytes;

	/* to be called in Subtype-Builders only */
	DNSMessage(byte[] messageBytes) {
		this.messageBytes = Arrays.copyOf(messageBytes, messageBytes.length);
	}

	/* to be used with existing messageBytes (e.g., coming over network)
	 *
	 * no validation
	 *
	 * */
	public static DNSMessage fromBytes(byte[] content) throws DNSParseException {
		try {
			if (DNSMessage.getFlag(Flag.QR, content)) {
				return new DNSMessageAnswer(content);
			}
			else {
				return new DNSMessageQuestion(content);
			}
		}
		catch (Exception e) {
			throw new DNSParseException(e);
		}
	}

	/* methods */
	@Override
	public String toString() {
		String formatString = ""
				+ "DNSQuestion\n"
				+ "Flags: (QR    OPC   AA    TC    RD    RA    Z     AD    CD   RC )\n"
				+ "(       %1$b %2$s %3$b %4$b %5$b %6$b %7$b %8$b %9$b %10$s";
		return String.format(formatString, getFlagQR(), "DUMMY", true, true, getFlagRD(), false, false, getFlagCD(), true, "DUMMY");
	}

	public int getFieldTransactionID() {
		return (messageBytes[0] << 8) + Byte.toUnsignedInt(messageBytes[1]);
	}

	public boolean getFlag(Flag flag) {
		return getFlag(flag, messageBytes);
	}

	private static boolean getFlag(Flag flag, byte[] bytes) {
		return (bytes[flag.bytePos] & flag.bit) == flag.bit;
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

	public int getLength() {
		return this.messageBytes.length;
	}

	public int getNumQuestions() {
		int questions = Byte.toUnsignedInt(messageBytes[4]) * 256
						+ Byte.toUnsignedInt(messageBytes[5]);
		return questions;
	}

	public DNSQuestion getQuestion(int questionIndex) throws DNSParseException {
		if (questions == null)
			parseQuestions();
		return questions[questionIndex];
	}

	public final class DNSQuestion {
		private final int nameStart;  	// first byte of question in the message
		private final int nameEnd;  	// last byte of name in the message (0 byte)
		private final int[] labelLengths; // firstByte of each label in the message - label length
		private String readableName = null;

		private DNSQuestion(int nameStart, int nameEnd, int[] labelOffsets) {
			this.nameStart = nameStart;
			this.nameEnd = nameEnd;
			this.labelLengths = labelOffsets;
		}

		public String getName() {
			if (readableName == null)
				toReadableName(false);
			return readableName;
		}

		private void toReadableName(boolean explicitRoot) {
			char[] asChars = new char[nameEnd - nameStart - (explicitRoot ? 0 : 1)];
			int asCharsOffset = 0;
			int labelStart = nameStart;
			for (int labelCur = 0; labelCur < labelLengths.length; labelCur++ ) {
				int labelLength = labelLengths[labelCur];
				// messageBytes[labelOffset] is label length
				int labelEnd   = labelStart + labelLength;
				for (int i = labelStart + 1; i <= labelEnd ; i++)
					asChars[asCharsOffset++] = (char)messageBytes[i];
				if (explicitRoot || labelCur < labelLengths.length - 1) // not last lable
					asChars[asCharsOffset++] = '.';
				labelStart = labelEnd + 1;
			}
			readableName = String.valueOf(asChars);
			log.info("READABLE NAME = " + readableName);
		}

		public Type getType(){
			int type = Byte.toUnsignedInt(messageBytes[nameEnd+1]) * 256 +
					Byte.toUnsignedInt(messageBytes[nameEnd+2]);
			return Type.fromInt(type);
		}

		public DNSClass getDNSClass() {
			int dnsclass = Byte.toUnsignedInt(messageBytes[nameEnd+3]) * 256 +
					Byte.toUnsignedInt(messageBytes[nameEnd+4]);
			return DNSClass.fromInt(dnsclass);
		}


		@Override
		public String toString() {
			return String.format("%s %s %s", getType(), getDNSClass(), getName());
		}

	}

	private DNSQuestion[] questions;

	private void parseQuestions() throws DNSParseException {
		int foundQuestions = 0;
		int messageIndex = 11; // last byte of header

		questions = new DNSQuestion[getNumQuestions()];
		while(
				messageIndex < getLength() - 1 &&
				foundQuestions < getNumQuestions())
		// iterate over questions / possibly whole message body
		{
			int nameStart = ++messageIndex; // first name starts right after header
			byte labelLength = messageBytes[messageIndex]; // first byte is length of first label in name
			byte numLabels = 0;
			while(labelLength != 0) {  // iterate over current questions labels
				if(labelLength < 0 || labelLength > 64)
					throw new DNSParseException("invalid label length in name in question section: " + labelLength);
				messageIndex += labelLength+1; //jump to next label length
				labelLength = messageBytes[messageIndex];
				numLabels+=1;
			}
			int nameEnd = messageIndex;
			int[] labelOffsets = new int[numLabels];
			// reset pos to start of Name
			messageIndex = nameStart;
			int cLabel = 0;
			labelLength = messageBytes[messageIndex];
			for(int i = 0; i < labelOffsets.length; i++) {
				labelOffsets[cLabel++] = messageBytes[messageIndex];
				labelLength = messageBytes[messageIndex];
				messageIndex += labelLength+1;
			}

			questions[foundQuestions++] = new DNSQuestion(
				  nameStart, nameEnd, labelOffsets); // TODO : set Type and class, possibly, to cache computation
			messageIndex += 4; // skip type and class bytes to next question or section
		} // end of message reached. might break before that
		if (foundQuestions != getNumQuestions()) // TODO : test this
			throw new DNSParseException("QDCount does not match contents of query section");
	}


	// TODO: somehow provide iterator for the QuestionParts in a message as well
	// TODO: do the same for RRSets in answers


}
