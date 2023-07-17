package eu.pitsch_devops.dnslite.dnslite_auth_server;

abstract public class DNSMessage {

	// constants for bitwise operations
	static final int MAX_UNSIZED_SHORT = 0b1111_1111_1111_1111;
	static final int MIN_UNSIZED_SHORT = 0;

	// constants to set one-bit flags or to set flag, invert then and to unset flag
	// in 3rd byte
	static final byte QR_FLAG = (byte)0b1000_0000; // casting necessary for ints > 127. bits preserved
	static final byte AA_FLAG= (byte)0b0000_0100;
	static final byte TC_FLAG= (byte)0b0000_0010;
	static final byte RD_FLAG= (byte)0b0000_0001;

	// in 4th byte
	static final byte RA_FLAG = (byte)0b1000_0000;
	static final byte Z_FLAG = (byte)0b0100_0000;
	static final byte AD_FLAG = (byte)0b0010_0000;
	static final byte CD_FLAG = (byte)0b0001_0000;

	// bits in 3rd byte that are not part of the opcode. use with & to unset OPCODE bits and leave others unchanged
	static final byte OPCODE_MASK = (byte)0b1000_0111;
	// constants to set OPCODE. set with |, (after applying mask with &)
	static final byte OPCODE_QUERY_BITS = (byte)0b0000_0000;
	static final byte OPCODE_IQUERY_BITS = (byte)0b0000_1000;
	static final byte OPCODE_STATUS_BITS = (byte)0b0001_0000;
	static final byte OPCODE_NOTIFY_BITS = (byte)0b0010_0000;
	static final byte OPCODE_UPDATE_BITS = (byte)0b0010_1000;

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
}
