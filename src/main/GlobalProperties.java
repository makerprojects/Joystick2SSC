package main;

import main.GUI.controller.ComponentConfig;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 *     This file is part of joystick-to-ppm, a port of Flytron's Compufly
 *     to Java for cross platform use.
 *
 *     The source was obtained from code.google.com/p/joystick-to-ppm
 *     Copyright (C) 2011  Alexandr Vorobiev
 *
 *     Implemented new interface jserialComm and SSC command interface
 *     Copyright (C) 2019-2020  Gregor Schlechtriem
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class GlobalProperties {
    private static Properties properties;
    public static Properties getProperties() {
        if (properties == null)
            properties = new Properties();
        return properties;
    }

    public static Integer checkChannelForComponent(ComponentConfig componentConfig) {
        Integer v;
        try {
         v = Integer.valueOf(properties.getProperty("DC","0"));
        } catch (Exception e) {
            return null;
        }
        for (int i = 1; i <= v; i++) {
            if (properties.containsKey("DEVICE" + i) && properties.containsKey("DCHANNEL" + i)) {
                if (componentConfig.toString().equals(properties.getProperty("DEVICE" + i))) {
                    try {
                     v = Integer.valueOf(properties.getProperty("DCHANNEL" + i,"1"));
                    } catch (Exception e) {
                        return null;
                    }
                    return v;
                }
            }
        }
        return null;
    }

    public static String checkChannelCharacteristicForComponent(ComponentConfig componentConfig) {
        Integer v;
        String s;
        try {
            v = Integer.valueOf(properties.getProperty("DC","0"));
        } catch (Exception e) {
            return null;
        }
        for (int i = 1; i <= v; i++) {
            if (properties.containsKey("DEVICE" + i) && properties.containsKey("CHARACTERISTIC" + i)) {
                if (componentConfig.toString().equals(properties.getProperty("DEVICE" + i))) {
                    try {
                        s = properties.getProperty("CHARACTERISTIC" + i,"Button");
                    } catch (Exception e) {
                        return null;
                    }
                    return s;
                }
            }
        }
        return null;
    }

    public static Integer checkChannelSVForComponent(ComponentConfig componentConfig) {
        Integer v;
        try {
         v = Integer.valueOf(properties.getProperty("DC","0"));
        } catch (Exception e) {
            return null;
        }
        for (int i = 1; i <= v; i++) {
            if (properties.containsKey("DEVICE" + i) && properties.containsKey("CHANNEL_SV" + i)) {
                if (componentConfig.toString().equals(properties.getProperty("DEVICE" + i))) {
                    try {
                     v = Integer.valueOf(properties.getProperty("CHANNEL_SV" + i,"50"));
                    } catch (Exception e) {
                        return null;
                    }
                    return v;
                }
            }
        }
        return null;
    }
    public static void loadProperties() {
        Properties props = new Properties();
        try {
            FileInputStream in = new FileInputStream("application.properties");
            props.load(in);
            in.close();
            properties = props;
        } catch (Exception ignore) {}
    }
    public static void saveProperties() {
        try {
            FileOutputStream out = new FileOutputStream("application.properties");
            properties.store(out, "---No Comment---");
            out.close();
        } catch (Exception ignore) {}
    }

}
