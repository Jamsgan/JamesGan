package datapager.datapager;

import static datapager.util.PropertiesUtility.loadProperties;
import static datapager.util.Utility.isBlank;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datapager.util.DataPagerLogger;
import datapager.util.ObjectToString;
import datapager.util.StringFormat;

public class Config implements Map<String, String> {
	private static final long serialVersionUID = 8720699738076915453L;
	private static final Logger logger = LoggerFactory.getLogger(AbstractProduceSql.class);
	/**
	 * Loads the SchemaCrawler configuration from properties file.
	 *
	 * @param configFilename
	 *            Configuration file name.
	 * @return Configuration properties.
	 * @throws IOException
	 */
	public static Config loadFile(final String configFilename) {
		final Properties configProperties;
		if (!isBlank(configFilename)) {
			final Path configPath = Paths.get(configFilename).normalize().toAbsolutePath();
			configProperties = loadProperties(configPath);
		} else {
			configProperties = new Properties();
		}
		return new Config(configProperties);
	}

	/**
	 * Loads the SchemaCrawler configuration, from a properties file stream.
	 *
	 * @param configStream
	 *            Configuration stream.
	 * @return Configuration properties.
	 */
	public static Config loadResource(final String resource) {
		final Properties configProperties = loadProperties(resource);
		return new Config(configProperties);
	}

	/**
	 * Copies properties into a map.
	 *
	 * @param properties
	 *            Properties to copy
	 * @return Map of properties and values
	 */
	private static Map<String, String> propertiesMap(final Properties properties) {
		final Map<String, String> propertiesMap = new HashMap<>();
		if (properties != null) {
			final Set<Map.Entry<Object, Object>> entries = properties.entrySet();
			for (final Map.Entry<Object, Object> entry : entries) {
				propertiesMap.put((String) entry.getKey(), (String) entry.getValue());
			}
		}
		return propertiesMap;
	}

	private final Map<String, String> config;

	/**
	 * Creates an empty config.
	 */
	public Config() {
		config = new HashMap<>();
	}

	/**
	 * Copies config into a map.
	 *
	 * @param config
	 *            Config to copy
	 */
	public Config(final Map<String, String> config) {
		this();
		if (config != null) {
			putAll(config);
		}
	}

	/**
	 * Copies properties into a map.
	 *
	 * @param properties
	 *            Properties to copy
	 */
	public Config(final Properties properties) {
		this(propertiesMap(properties));
	}

	/**
	 * Gets the value of a property as a boolean.
	 *
	 * @param propertyName
	 *            Property name
	 * @return Boolean value
	 */
	public boolean getBooleanValue(final String propertyName) {
		return getBooleanValue(propertyName, false);
	}

	public boolean getBooleanValue(final String propertyName, final boolean defaultValue) {
		return Boolean.parseBoolean(getStringValue(propertyName, Boolean.toString(defaultValue)));
	}

	/**
	 * Gets the value of a property as an double.
	 *
	 * @param propertyName
	 *            Property name
	 * @return Double value
	 */
	public double getDoubleValue(final String propertyName, final double defaultValue) {
		try {
			return Double.parseDouble(getStringValue(propertyName, String.valueOf(defaultValue)));
		} catch (final NumberFormatException e) {
			logger.error("Could not parse double value for property <%{}>", propertyName,
					e);
			return defaultValue;
		}
	}

	/**
	 * Gets the value of a property as an integer.
	 *
	 * @param propertyName
	 *            Property name
	 * @return Integer value
	 */
	public int getIntegerValue(final String propertyName, final int defaultValue) {
		try {
			return Integer.parseInt(getStringValue(propertyName, String.valueOf(defaultValue)));
		} catch (final NumberFormatException e) {
			logger.error("Could not parse integer value for property <{}>", propertyName,
					e);
			return defaultValue;
		}
	}

	/**
	 * Gets the value of a property as an long.
	 *
	 * @param propertyName
	 *            Property name
	 * @return Long value
	 */
	public long getLongValue(final String propertyName, final long defaultValue) {
		try {
			return Long.parseLong(getStringValue(propertyName, String.valueOf(defaultValue)));
		} catch (final NumberFormatException e) {
			logger.error("Could not parse integer value for property <{}>", propertyName,
					e);
			return defaultValue;
		}
	}

	/**
	 * Gets the value of a property as a string.
	 *
	 * @param propertyName
	 *            Property name
	 * @param defaultValue
	 *            Default value
	 * @return String value
	 */
	public String getStringValue(final String propertyName, final String defaultValue) {
		// String value = get(propertyName);
		String value = null;
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	@Override
	public void clear() {
		config.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return config.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return config.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return config.entrySet();
	}

	@Override
	public String get(Object key) {
		return config.get(key);
	}

	@Override
	public boolean isEmpty() {
		return config.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return config.keySet();
	}

	@Override
	public String put(String key, String value) {
		return config.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends String> map) {
		config.putAll(map);
	}

	@Override
	public String remove(Object key) {
		return config.remove(key);
	}

	@Override
	public int size() {
		return config.size();
	}

	@Override
	public Collection<String> values() {
		return config.values();
	}

	public boolean hasValue(String string) {

		return !isBlank(config.get(string));
	}
}
