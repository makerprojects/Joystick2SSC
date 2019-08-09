package main.GUI.controller;


import main.GUI.ButtonAssignDialog;
import main.GUI.DeviceConfigPanel;
import main.GlobalProperties;
import net.java.games.input.Component;
import net.java.games.input.Controller;

import javax.swing.*;
import javax.swing.border.LineBorder;
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

public class ButtonPanel extends JPanel implements ActionListener{
    private final Component component;
    private final JButton assignBtn= new JButton("+");
    private final ComponentConfig componentConfig;

    public ButtonPanel(Controller controller, Component component) {
        this.component = component;
        componentConfig = new ComponentConfig(controller, (net.java.games.input.Component) component);
        assignBtn.addActionListener(this);
        assignBtn.setMinimumSize(new Dimension(60,20));
        setPreferredSize(new Dimension(50,50));
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        setBorder(LineBorder.createGrayLineBorder());
        setBackground(Color.gray);
        add(new JLabel(component.getName()));
        add(assignBtn);

        Integer value = GlobalProperties.checkChannelForComponent(componentConfig);
        if (value != null ) {

            DeviceConfigPanel.addMapping(componentConfig, value, false);
            value = GlobalProperties.checkChannelSVForComponent(componentConfig);
            if (value != null)
                componentConfig.setSentValue(value);

            assignBtn.setText("-");
        }
        //assignBtn.addActionListener(this);

    }

    public void poll() {
        float data = component.getPollData();
        if (data == 0.0f){
            setBackground(Color.GRAY);
        } else if ( data == 1.0f) {
            setBackground(Color.GREEN);
        }else { // should never happen
            setBackground(Color.red);
        }
    }


    public void actionPerformed(ActionEvent e) {
        if (DeviceConfigPanel.getAssignMap().containsKey(componentConfig)) {
            DeviceConfigPanel.removeMapping(componentConfig);
            assignBtn.setText("+");
        } else {
            (new ButtonAssignDialog(componentConfig)).setVisible(true);
            if (DeviceConfigPanel.getAssignMap().containsKey(componentConfig))
                assignBtn.setText("-");

        }
    }
}
