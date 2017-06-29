/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2017, Sualeh Fatehi <sualeh@hotmail.com>.
All rights reserved.
------------------------------------------------------------------------

SchemaCrawler is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

SchemaCrawler and the accompanying materials are made available under
the terms of the Eclipse Public License v1.0, GNU General Public License
v3 or GNU Lesser General Public License v3.

You may elect to redistribute this code under any of these licenses.

The Eclipse Public License is available at:
http://www.eclipse.org/legal/epl-v10.html

The GNU General Public License v3 and the GNU Lesser General Public
License v3 are available at:
http://www.gnu.org/licenses/

========================================================================
*/
package datapager.util;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class DataPagerLogger {

	private static String loggerClass = DataPagerLogger.class.getName();
	private static final String LOG_PATH="log.log";
	public static final String DATE_PATTERN_FULL = "yyyy-MM-dd HH:mm:ss"; 

	public static DataPagerLogger getLogger(final String name) {
		return new DataPagerLogger(Logger.getLogger(name));
	}

	private static void updateSource(final LogRecord lr, final int depth) {
		final StackTraceElement[] steArray = Thread.currentThread().getStackTrace();
		if (steArray == null) {
			return;
		}
		for (int i = 1; i < steArray.length; i++) {
			final StackTraceElement ste = steArray[i];
			if (!loggerClass.equals(ste.getClassName())) {
				final int index = i + depth;
				if (index >= 0 && index < steArray.length) {
					final StackTraceElement ste_i = steArray[index];
					lr.setSourceMethodName(ste_i.getMethodName());
					lr.setSourceClassName(ste_i.getClassName());
					break;
				}
			}
		}
	}

	private final Logger logger;

	private DataPagerLogger(final Logger logger) {
		this.logger = logger;
	}

	public boolean isLoggable(final Level level) {
		return logger.isLoggable(level);
	}

	public void log(final Level level, final int depth, final String msg, final Throwable thrown) {
		requireNonNull(level);
		requireNonNull(msg);

		if (!logger.isLoggable(level)) {
			return;
		}

		final LogRecord lr = new LogRecord(level, msg);
		updateSource(lr, depth);
		if (thrown != null) {
			lr.setThrown(thrown);
		}

		logger.log(lr);
	}

	public void log(final Level level, final String msg) {
		log(level, msg, null);
	}

	public void log(final Level level, final String msg, final Throwable thrown) {
		log(level, 0, msg, thrown);
	}

	public void log(final Level level, final Supplier<String> msgSupplier) {
		if (!logger.isLoggable(level)) {
			return;
		}
		log(level, msgSupplier.get());
	}

	public void log(final Level level, final Supplier<String> msgSupplier, final Throwable thrown) {
		if (!logger.isLoggable(level)) {
			return;
		}
		log(level, msgSupplier.get(), thrown);
	}

	public void initLog() {
		this.logger.setLevel(Level.ALL);
		// 控制台输出的handler
		ConsoleHandler consoleHandler = new ConsoleHandler();
		// 设置控制台输出的等级（如果ConsoleHandler的等级高于或者等于log的level，则按照FileHandler的level输出到控制台，如果低于，则按照Log等级输出）
		consoleHandler.setLevel(Level.INFO);
		// 添加控制台的handler
		this.logger.addHandler(consoleHandler);
		
		FileHandler fileHandler = null;  
        try {  
            fileHandler = new FileHandler(LOG_PATH);  
            // 设置输出文件的等级（如果FileHandler的等级高于或者等于log的level，则按照FileHandler的level输出到文件，如果低于，则按照Log等级输出）  
            fileHandler.setLevel(Level.ALL);  
            fileHandler.setFormatter(new Formatter() {
				@Override
				public String format(LogRecord record) {

                    // 设置文件输出格式  
                    return "[ " + getCurrentDateStr(DATE_PATTERN_FULL) + " - Level:"  
                            + record.getLevel().getName().substring(0, 1) + " ]-" + "[" + record.getSourceClassName()  
                            + " -> " + record.getSourceMethodName() + "()] " + record.getMessage() + "\n";  
				}
			});
  
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        // 添加输出文件handler  
        this.logger.addHandler(fileHandler);  
		
	}
	/**  
     * 获取当前时间  
     *   
     * @return  
     */  
    public static String getCurrentDateStr(String pattern) {  
        Date date = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);  
        return sdf.format(date);  
    }  
}
