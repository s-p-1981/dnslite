package eu.ptsdvps.dnslite.protocol;

/*
 * Used typically when something needs to be parsed or validated into a DNSMessage
 */

public class DNSParseException extends DNSException {

	private static final long serialVersionUID = 5456018387873640473L;

	public DNSParseException() {
		super();
	}
	public DNSParseException(Throwable cause) {
		super(cause);
	}

}
