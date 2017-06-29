package datapager.tools.databaseconnector;

import java.io.Serializable;
import static datapager.util.Utility.isBlank;

public final class DatabaseServerType

		implements Serializable {

	private static final long serialVersionUID = 2160456864554076419L;

	public static final DatabaseServerType UNKNOWN = new DatabaseServerType();

	private final String databaseSystemIdentifier;
	private final String databaseSystemName;

	public DatabaseServerType(final String databaseSystemIdentifier, final String databaseSystemName) {
		if (isBlank(databaseSystemIdentifier)) {
			throw new IllegalArgumentException("No database system identifier provided");
		}
		this.databaseSystemIdentifier = databaseSystemIdentifier;

		if (isBlank(databaseSystemName)) {
			throw new IllegalArgumentException("No database system name provided");
		}
		this.databaseSystemName = databaseSystemName;

	}

	private DatabaseServerType() {
		databaseSystemIdentifier = null;
		databaseSystemName = null;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DatabaseServerType other = (DatabaseServerType) obj;
		if (databaseSystemIdentifier == null) {
			if (other.databaseSystemIdentifier != null) {
				return false;
			}
		} else if (!databaseSystemIdentifier.equals(other.databaseSystemIdentifier)) {
			return false;
		}
		return true;
	}

	public String getDatabaseSystemIdentifier() {
		return databaseSystemIdentifier;
	}

	public String getDatabaseSystemName() {
		return databaseSystemName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (databaseSystemIdentifier == null ? 0 : databaseSystemIdentifier.hashCode());
		return result;
	}

	public boolean isUnknownDatabaseSystem() {
		return isBlank(databaseSystemIdentifier);
	}

	@Override
	public String toString() {
		if (isUnknownDatabaseSystem()) {
			return "";
		} else {
			return String.format("%s - %s", databaseSystemIdentifier, databaseSystemName);
		}
	}

}