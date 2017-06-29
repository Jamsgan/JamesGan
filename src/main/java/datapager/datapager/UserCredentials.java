package datapager.datapager;

public interface UserCredentials {

	void clearPassword();

	String getPassword();

	String getUser();

	boolean hasPassword();

	boolean hasUser();

}
