package datapager.datapager;

public class DataPagerException extends Exception {

	private static final long serialVersionUID = 3257848770627713076L;

	public DataPagerException(final String message) {
		super(message);
	}

	public DataPagerException(final String message, final Throwable cause) {
		super(message + ": " + cause.getMessage(), cause);
	}

}
