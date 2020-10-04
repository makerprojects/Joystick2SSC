package main.GUI.controller;

import net.java.games.input.Component;
import net.java.games.input.Controller;

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

public class ComponentConfig {
    private final Controller controller;
    private final Component component;
    private float maxValue = 1.0f;
    private float minValue = -1.0f;
    private int sentValue = 50;
    private int prevValue = 512;
    private String characteristics = "Button";
    private boolean switchState = false;
    private boolean switchWas0 = true;

    public int getPrevValue() {
        return prevValue;
    }

    public void setPrevValue(int prevValue) {
        this.prevValue = prevValue;
    }

    private boolean x = true;

    public boolean isX() {
        return x;
    }

    public ComponentConfig(Controller controller, Component component) {
        this.controller = controller;
        this.component = component;
    }

    public int getPercentage(float val) {
        float max = maxValue - minValue;
        float value = val - minValue;
        return (int)((value / max) * 100.0f);
    }

    public int getSentValue() {
        return sentValue;
    }

    public void setSentValue(int sentValue) {
        this.sentValue = sentValue;
    }

    public ComponentConfig(Controller controller, Component component, boolean X) {
        this(controller,component);
        this.x = X;
    }
    public Controller getController() {
        return controller;
    }

    public Component getComponent() {
        return component;
    }

    public float getMinValue() {
        return minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void updateMinMax(float v) {
        if (v > maxValue)
            maxValue = v;
        if (v < minValue)
            minValue = v;

    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String s) {
        characteristics = s;
    }

    public boolean getSwitchState () {
        return switchState;
    }

    public void setSwitchState (boolean b) {
        switchState = b;
    }

    public boolean getSwitchWas0 () {
        return switchWas0;
    }

    public void setSwitchWas0 (boolean b) {
        switchWas0 = b;
    }

    @Override
    public String toString() {
        return controller.getName() +";" + component.getName() + ";" + x;
    }
}
