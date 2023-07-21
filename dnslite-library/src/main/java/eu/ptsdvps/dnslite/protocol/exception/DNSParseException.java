package eu.ptsdvps.dnslite.protocol.exception;

/*
 * Used typically when something needs to be parsed or validated into a DNSMessage
 */

public class DNSParseException extends DNSException {

	private static final long serialVersionUID = 5456018387873640473L;

	public DNSParseException() {super();}
	public DNSParseException(String message) {super(message);}
	public DNSParseException(Throwable cause) {super(cause);}
	public DNSParseException(String message, Throwable cause) {super(message, cause);}

}
