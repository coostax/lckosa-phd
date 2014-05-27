package pt.iscte.pramc.lof.exception;

public class TypeNotSupportedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String msg;

	public TypeNotSupportedException(String message) {
		super();
		this.msg = message;
	}

	@Override
	public String getMessage() {
		return msg;
	}
}
