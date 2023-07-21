package eu.ptsdvps.dnslite.protocol;

import java.util.Arrays;

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
				return new DNSAnswer(content);
			}
			else {
				return new DNSQuestion(content);
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
}
