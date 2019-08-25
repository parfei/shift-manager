package shiftman.server;

/**
 * An exception to be thrown specific to the roster shift manager system.
 */
public class RosterException extends Exception{

	private static final long serialVersionUID = 1L;

	/**
	 * getMessage() will return this input String message.
	 * @param message description of the roster shift manager system exception.
	 */
	public RosterException(String message){
		super(message);
	}

}
