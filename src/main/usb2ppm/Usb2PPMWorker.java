package main.usb2ppm;

import com.fazecast.jSerialComm.*;

import main.GUI.controller.ComponentConfig;
import main.usb2ppm.event.DataSentEvent;
import net.java.games.input.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 *     This file is part of joystick-to-ppm, a port of Flytron's Compufly
 *     to Java for cross platform use.
 *
 *     The source was obtained from code.google.com/p/joystick-to-ppm
 *     Copyright (C) 2011  Alexandr Vorobiev
 *
 *     Implemented new interface jserialComm and SSC command interface
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

public class Usb2PPMWorker implements Runnable {
    private static final String SET_V1 = "SET";
    private static final String SET_V2 = "S";
    private final SerialPort serialPort;
    private final OutputStream out;
    private Map<ComponentConfig, Integer> mapping;
    private volatile boolean sendParameters = false;
    private volatile int par1 =0;
    private volatile int par2 =0;
    private boolean v2 = false;
    private Vector<DataSentEvent> listeners = new Vector<DataSentEvent>();
    private Map<Integer, ServoParameter> parameterMap;
    public Usb2PPMWorker(SerialPort serialPort, OutputStream out, boolean v2) {
        this.serialPort = serialPort;
        this.out = out;
        this.v2 = v2;
    }

    private String getSetCommand() {
        return isV2() ? SET_V2 : SET_V1;
    }

    public synchronized boolean isV2() {
        return v2;
    }

    public synchronized void setMapping(Map<ComponentConfig, Integer> mapping) {
        this.mapping = mapping;
    }
    public synchronized void setV2(boolean v) {
        v2 = v;
    }
    public synchronized void setParameterMap(Map<Integer, ServoParameter> parameterMap) {
        this.parameterMap = parameterMap;
    }
    private synchronized Map<ComponentConfig, Integer> getMapping() {
        return mapping;
    }

    public void addListener(DataSentEvent listener) {
        listeners.add(listener);

    }
    public void run() {
        try {
            while (!Thread.interrupted()) {
                if (sendParameters) {
                    sendParameters(par1, par2);
                    break;
                }
                Map<ComponentConfig, Integer> mapping = getMapping();
                Map<Integer, ServoParameter> parameterMap = getParameterMap();
                Map<Integer, ComponentConfig> filteredMap = new TreeMap<Integer, ComponentConfig>();
                if (mapping != null && parameterMap != null) {
                    /*for (Map.Entry<ComponentConfig, Integer> entry: mapping.entrySet()) {
                        int channel = entry.getValue();
                        Controller controller = entry.getKey().getController();
                        Component component = entry.getKey().getComponent();
                        if (!filteredMap.containsKey(channel))
                            filteredMap.put(channel, entry.getKey());
                        else {
                            Controller controller2 = filteredMap.get(channel).getController();
                            Component component2 = filteredMap.get(channel).getComponent();
                            synchronized (controller2) {
                                synchronized (controller) {
                                    float d = component.getPollData();
                                    float d2 = component2.getPollData();
                                    if (component2.isAnalog()) {
                                        // c2 - axis
                                        if (component.isAnalog()) {
                                            if (entry.getKey().getPercentage(d) > filteredMap.get(channel).getPercentage(d2))
                                        }
                                    }
                                }
                            }
                        }
                      }*/
                    for (Map.Entry<ComponentConfig, Integer> entry: mapping.entrySet()) {
                        int channel = entry.getValue();
                        synchronized (entry.getKey().getController()) {
                            if (entry.getKey().getController().poll()) {
                                float fdata = entry.getKey().getComponent().getPollData();
                                if (entry.getKey().getComponent().isAnalog()) {
                                    entry.getKey().updateMinMax(fdata);
                                    //axis
                                    if (entry.getKey().getComponent().getDeadZone() >= Math.abs(fdata))
                                        fdata = 0;
                                    servo(channel, fdata, entry.getKey().getMinValue(), entry.getKey().getMaxValue(), parameterMap.get(entry.getValue()).isReverse(), parameterMap.get(entry.getValue()).getTrim(), parameterMap.get(entry.getValue()).getEpa());
                                    //servo(channel, -0.9f, -1.0f, 1.0f, parameterMap.get(entry.getValue()).isReverse(), parameterMap.get(entry.getValue()).getTrim(), parameterMap.get(entry.getValue()).getEpa());
                                } else if (entry.getKey().getComponent().getIdentifier() == Component.Identifier.Axis.POV) {
                                    int data = 512;
                                    if (fdata == Component.POV.OFF) {
                                        data = 512;
                                    } else if (entry.getKey().isX())  {
                                        if (fdata == Component.POV.LEFT || fdata == Component.POV.DOWN_LEFT || fdata == Component.POV.UP_LEFT) {
                                            data = entry.getKey().getPrevValue() - 50;
                                        } else if (fdata == Component.POV.RIGHT || fdata == Component.POV.DOWN_RIGHT || fdata == Component.POV.UP_RIGHT) {
                                            data = entry.getKey().getPrevValue() + 50;
                                        }
                                    } else {
                                        if (fdata == Component.POV.DOWN || fdata == Component.POV.DOWN_LEFT || fdata == Component.POV.DOWN_RIGHT) {
                                            data = entry.getKey().getPrevValue() - 50;
                                        } else if (fdata == Component.POV.UP || fdata == Component.POV.UP_RIGHT || fdata == Component.POV.UP_LEFT) {
                                            data = entry.getKey().getPrevValue() + 50;
                                        }
                                    }
                                    data = Math.min(data, 1024);
                                    data = Math.max(data,0);
                                    entry.getKey().setPrevValue(data);
                                    servo(channel, data, 0, 1024, parameterMap.get(entry.getValue()).isReverse(), parameterMap.get(entry.getValue()).getTrim(), parameterMap.get(entry.getValue()).getEpa());
                                } else {
                                    if (fdata == 0.0f){
                                        servo(channel, 0, 0, 100, parameterMap.get(entry.getValue()).isReverse(), parameterMap.get(entry.getValue()).getTrim(), parameterMap.get(entry.getValue()).getEpa());
                                    } else if (fdata == 1.0f) {
                                        servo(channel, entry.getKey().getSentValue(), 0, 100, parameterMap.get(entry.getValue()).isReverse(), parameterMap.get(entry.getValue()).getTrim(), parameterMap.get(entry.getValue()).getEpa());
                                    }
                                }

                            }
                        }

                    }
                }
                Thread.sleep(50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // serialPort.close();
            serialPort.closePort();
        }
    }

    private synchronized Map<Integer, ServoParameter> getParameterMap() {
        return parameterMap;
    }

    private void sendParameters(int par1, int par2) throws Exception {
        out.write((getSetCommand()  +((char)21) + ""+((char)0) +""+((char)par1)).getBytes());
        out.flush();
        Thread.sleep(500);
        out.write((getSetCommand() + ((char) 22) + "" + ((char) 0) + "" + ((char) par2)).getBytes());
        out.flush();
        for(int i= 0; i <= 20; i++) {
            out.write((char)255);
            out.flush();
            Thread.sleep(10);
        }
    }

    private void servo( int channel, float value, float min, float max, boolean inverse, int trim, int epa) throws IOException {
        System.out.println("send cmd ch="+channel +" min/max/val="+ String.format("%.1f",min)+ String.format("%.1f",max) +"/"+String.format("%.1f",value) + " trim="+trim + " epa="+epa);
        /* max = max - min;
           value =value - min; */
        int data = (int)Math.floor(1000.0f*(value - min) / (max - min));  // pulse width would be 1 - 2 ms eq. a range 1000 µs
        data = ((data - 500) * epa) / 100 ;
        if (data>500)
            data = 500;
        if (data < -500)
            data = -500;

        data = data + 500;
        data = data + (5*trim);
        if (data<0)
            data = 0;
        if (data>1000)
            data = 1000;
        if (inverse)
            data = 1000 - data;

        sendValues(channel, 1000 + data);  // add 1000 µs representing the base offset
        updateServoBar(channel, data);
    }

    private void sendValues(int channel, int value) throws IOException {
        String LeadingZero = "";
        if (isV2()) {
            value = ((value - 1000) * 5) + 5000;  // calculate HP value
            if (value < 10000) {
                LeadingZero = "0";
            }
        }
        String cmd = Integer.toString(channel) + "=" + LeadingZero + Integer.toString(value);
        byte[] b = cmd.getBytes();
        out.write(b);
        System.out.println("send cmd: " + cmd);
        out.flush();
    }

    private void updateServoBar(int channel, int data) {
        for (DataSentEvent listener: listeners)
            listener.dataSent(channel, data);
    }

    public void sendNewParameterAndTerminate(int par1, int par2) {
        this.par1 = par1;
        this.par2 = par2;
        sendParameters = true;
    }
}
