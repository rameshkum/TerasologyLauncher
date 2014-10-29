/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.launcher.log;

import ch.qos.logback.core.PropertyDefinerBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Define a temporary log file name that can be used by Logback properties.
 */
public class TempLogFilePropertyDefiner extends PropertyDefinerBase {

    private static TempLogFilePropertyDefiner instance;

    private Path logFile;

    private String prefix;
    private String suffix;

    private boolean failed;

    /**
     * Default constructor (necessary) - called by Logback.
     */
    public TempLogFilePropertyDefiner() {
        if (instance != null) {
            throw new IllegalStateException("This class must not be instantiated twice");
        }

        instance = this;
    }

    public static TempLogFilePropertyDefiner getInstance() {
        return instance;
    }

    @Override
    public String getPropertyValue() {

        // Don't try again, if it failed before
        if (failed) {
            return null;
        }

        return getLogFile().toString();
    }

    public String getPrefix() {
        return prefix;
    }

    /**
     * Set the prefix string for generating the file's name.
     * @param prefix the prefix string to be used in generating the file's name; may be null
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    /**
     * Set the suffix string for generating the file's name.
     * @param suffix the suffix string to be used in generating the file's name; may be null, in which case ".tmp" is used
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Returns the temporary log file.
     * @return the log file
     */
    public Path getLogFile() {
        if (logFile == null) {
            try {
                logFile = Files.createTempFile(prefix, suffix);
                System.out.println("Using log file " + logFile);
            } catch (IOException e) {
                failed = true;
                // cannot use logger
                e.printStackTrace();
            }
        }

        return logFile;
    }
}
