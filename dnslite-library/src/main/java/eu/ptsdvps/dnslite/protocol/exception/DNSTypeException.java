package eu.ptsdvps.dnslite.protocol.exception;

public class DNSTypeException extends Exception {
	private static final long serialVersionUID = 1408236616681016538L;

	public DNSTypeException() {super();}
	public DNSTypeException(String message) {super(message);}
	public DNSTypeException(Throwable cause) {super(cause);}
	public DNSTypeException(String message, Throwable cause) {super(message, cause);}

}
