package eu.ptsdvps.dnslite.protocol.exception;

public class DNSException extends Exception {

	private static final long serialVersionUID = -1840826664923139047L;
	public DNSException() {super();}
	public DNSException(String message) {super(message);}
	public DNSException(Throwable cause) {super(cause);}
	public DNSException(String message, Throwable cause) {super(message, cause);}

}
