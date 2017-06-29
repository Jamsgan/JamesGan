package datapager.datapager;

import static datapager.util.Utility.isBlank;

/**
 * 单个用户认证
 * 
 * @author James
 *
 */
public final class SingleUseUserCredentials implements UserCredentials {
	private final String user;
	private final char[] password;
	private boolean isCleared;

	public SingleUseUserCredentials() {
		user = null;
		password = null;
	}

	public SingleUseUserCredentials(final String user, final String password) {
		this.user = user;
		if (password == null) {
			this.password = null;
		} else {
			this.password = password.toCharArray();
		}
	}

	@Override
	public void clearPassword() {
		isCleared = true;
		if (hasPassword()) {
			for (int i = 0; i < password.length; i++) {
				password[i] = 0;
			}
		}
	}

	@Override
	public String getPassword() {
		if (isCleared) {
			throw new IllegalAccessError("Password has been cleared");
		}

		final String passwordString;
		if (password == null) {
			passwordString = null;
		} else {
			passwordString = new String(password);
		}

		clearPassword();

		return passwordString;
	}

	@Override
	public String getUser() {
		return user;
	}

	@Override
	public boolean hasPassword() {
		return !isCleared && password != null;
	}

	@Override
	public boolean hasUser() {
		return !isBlank(user);
	}

	@Override
	public String toString() {
		return "UserCredentials [user=\"" + user + "\", password=\"*****\"]";
	}

}
