package eu.pitsch_devops.dnslite.dnslite_auth_server;

abstract public class DNSMessage {

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
	}

	enum RCode {
		// TODO: Implement
	}


	// bits in 3rd byte that are not part of the opcode. use with & to unset OPCODE bits and leave others unchanged
	static final byte RCODE_MASK = (byte)0b1111_0000; // bits in 4th byte that are not part of the rcode
	// constants to set RCODE. set with |, (after applying mask with &)
	static final byte RCODE_NOERR_BITS = (byte)0b0000_0000;
	static final byte RCODE_FORMERR_BITS = (byte)0b0000_0001;
	static final byte RCODE_SERVFAIL_BITS = (byte)0b0000_0010;
	static final byte RCODE_NXDOMAIN_BITS = (byte)0b0000_0011;
	static final byte RCODE_NOTIMP_BITS = (byte)0b0000_0100;
	static final byte RCODE_REFUSED_BITS = (byte)0b0000_0101;
	static final byte RCODE_YXDOMAIN_BITS = (byte)0b0000_0110;
	static final byte RCODE_YXRRSET_BITS = (byte)0b0000_0111;
	static final byte RCODE_NXRRSET_BITS = (byte)0b0000_1000;
	static final byte RCODE_NOTAUTH_BITS = (byte)0b0000_1001;
	static final byte RCODE_NOTZONE_BITS = (byte)0b0000_1010;


	/* fields */
	byte[] fixedSizeHeader;

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
}
