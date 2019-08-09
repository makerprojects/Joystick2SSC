package main.GUI;

import main.GUI.controller.ComponentConfig;

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

public class AssignDialog extends JDialog implements ActionListener{
    private final JComboBox channels;
    private final JButton okBtn = new JButton("Ok");
    private final ComponentConfig component;
    public AssignDialog(ComponentConfig component) {
        super(MainFrame.getInstance(), true);
        channels = new JComboBox();
        okBtn.addActionListener(this);
        this.component = component;
        for (int i = 1; i <= DeviceConfigPanel.SERVO_COUNT; i++) {
            if (!DeviceConfigPanel.getAssignMap().containsValue(i))
                channels.addItem(i);
        }
        createLayout();
    }

    private void createLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5,0,5,5);
        gbc.weighty =1;
        gbc.weightx =0;
        add(new JLabel("Choose channel: "),gbc);
        gbc.gridx = 1;
        gbc.weightx =1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(channels,gbc);
        gbc.gridx = 1;
        add(okBtn,gbc);

        setPreferredSize(new Dimension(250, 110));
        setSize(getPreferredSize());
    }

    public void actionPerformed(ActionEvent e) {
        DeviceConfigPanel.addMapping(component, (Integer)channels.getSelectedItem() );
        setVisible(false);
    }
}
