package main.usb2ppm;

/**
 *     This file is part of joystick-to-ppm, a port of Flytron's Compufly
 *     to Java for cross platform use.
 *
 *     The source was obtained from code.google.com/p/joystick-to-ppm
 *     Copyright (C) 2011  Alexandr Vorobiev
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

public class ServoParameter {
    private boolean reverse;
    private int trim;
    private int epa;
    public ServoParameter(boolean reverse, int trim, int epa) {
        this.reverse = reverse;
        this.trim = trim;
        this.epa = epa;
    }

    public boolean isReverse() {
        return reverse;
    }

    public int getTrim() {
        return trim;
    }

    public int getEpa() {
        return epa;
    }
}
