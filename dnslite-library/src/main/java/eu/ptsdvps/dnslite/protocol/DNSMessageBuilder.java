package eu.ptsdvps.dnslite.protocol;

import eu.ptsdvps.dnslite.protocol.DNSMessage.Flag;

abstract class DNSMessageBuilder {

	byte[] builderBytes = new byte[512];
	short realLength = 0;

	void setFlag(Flag flag, boolean value) {
		if(value) builderBytes[flag.bytePos] |=flag.bit;
		else builderBytes[flag.bytePos] &= ~flag.bit;
	}
}