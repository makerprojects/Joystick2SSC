package main.GUI.controller;

import main.GUI.AssignDialog;
import main.GUI.DeviceConfigPanel;
import main.GlobalProperties;
import net.java.games.input.Component;
import net.java.games.input.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class POVPanel extends JPanel implements ActionListener{
    private final POVComponent povComponent;
    private final Component component;
    private final JButton assignBtnX = new JButton("+ X");
    private final JButton assignBtnY = new JButton("+ Y");
    private final ComponentConfig componentConfigX;
    private final ComponentConfig componentConfigY;
    private final JTextField valueField = new JTextField("0.0");
    public POVPanel(Controller controller,Component component) {
        this.component = component;
        povComponent = new POVComponent(component);
        setLayout(new BorderLayout());
        add(povComponent, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel();
        valueField.setEditable(false);
        valueField.setMinimumSize(new Dimension(60,20));
        btnPanel.add(valueField);
        btnPanel.add(assignBtnX);
        btnPanel.add(assignBtnY);
        add(btnPanel, BorderLayout.SOUTH);
        componentConfigX = new ComponentConfig(controller,component, true);
        Integer value = GlobalProperties.checkChannelForComponent(componentConfigX);
        if (value != null ) {
            DeviceConfigPanel.addMapping(componentConfigX, value, false);
            assignBtnX.setText("- X");
        }
        componentConfigY = new ComponentConfig(controller,component, false);
        value = GlobalProperties.checkChannelForComponent(componentConfigY);
        if (value != null ) {
            DeviceConfigPanel.addMapping(componentConfigY, value, false);
            assignBtnY.setText("- Y");
        }

        assignBtnX.addActionListener(this);
        assignBtnY.addActionListener(this);
    }

    public void poll() {
        povComponent.poll();
        valueField.setText(povComponent.getStringValue());
    }

    public void actionPerformed(ActionEvent e) {
        if ( e.getSource() == assignBtnX && DeviceConfigPanel.getAssignMap().containsKey(componentConfigX)) {
                DeviceConfigPanel.removeMapping(componentConfigX);
                assignBtnX.setText("+ X");
        } else if ( e.getSource() == assignBtnY && DeviceConfigPanel.getAssignMap().containsKey(componentConfigY)) {
                DeviceConfigPanel.removeMapping(componentConfigY);
                assignBtnY.setText("+ Y");
        } else {
            if (e.getSource() == assignBtnX) {
                (new AssignDialog(componentConfigX)).setVisible(true);
                if (DeviceConfigPanel.getAssignMap().containsKey(componentConfigX))
                    assignBtnX.setText("- X");
            } else {
                (new AssignDialog(componentConfigY)).setVisible(true);
                if (DeviceConfigPanel.getAssignMap().containsKey(componentConfigY))
                    assignBtnY.setText("- Y");
            }
        }
    }
}
