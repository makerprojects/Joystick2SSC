package main;

import main.GUI.MainFrame;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

/**
 *     This file is part of joystick-to-ppm, a port of Flytron's Compufly
 *     to Java for cross platform use.
 *
 *     The source was obtained from code.google.com/p/joystick-to-ppm
 *     Copyright (C) 2011  Alexandr Vorobiev
 *
 *     UI changes
 *     Copyright (C) 2019  Gregor Schlechtriem
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

public class Main {
    private static final String VERSION = "1.2";
    public final class Debug {
        // set false to allow compiler to identify and eliminate unreachable code
        public static final boolean ON = false;
    }
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {

        GlobalProperties.loadProperties();
        final MainFrame frame = MainFrame.getInstance();
        frame.setTitle("JoystickRC v"+VERSION);
        frame.addWindowListener( new WindowListener() {
            public void windowOpened(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowClosing(WindowEvent e) {
                GlobalProperties.saveProperties();
            }

            public void windowClosed(WindowEvent e) {

            }

            public void windowIconified(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowDeiconified(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowActivated(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowDeactivated(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtilities.invokeAndWait(() -> frame.setVisible(true));
    }


}
