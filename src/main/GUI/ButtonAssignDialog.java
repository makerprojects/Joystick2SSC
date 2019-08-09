package main.GUI;

import main.GUI.controller.ComponentConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

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

public class ButtonAssignDialog extends JDialog implements ActionListener, AdjustmentListener{
    private final JComboBox channels;
    private final JButton okBtn = new JButton("Ok");
    private final ComponentConfig component;
    private final JScrollBar value = new JScrollBar(JScrollBar.HORIZONTAL, 50, 0, 0, 100);
    private final JTextField field = new JTextField();
    public ButtonAssignDialog(ComponentConfig component) {
        super(MainFrame.getInstance(), true);
        field.setEditable(false);
        field.setMinimumSize(new Dimension(50, 20));
        field.setText(component.getSentValue() + "%");
        channels = new JComboBox();
        value.setValue(component.getSentValue());
        value.addAdjustmentListener(this);
        okBtn.addActionListener(this);
        this.component = component;
        for (int i = 1; i <= DeviceConfigPanel.SERVO_COUNT; i++) {
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
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(new JLabel("Choose value: "),gbc);

        gbc.gridx = 1;
        add(field, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(value, gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 1;
        add(okBtn,gbc);

        setPreferredSize(new Dimension(260, 150));
        setSize(getPreferredSize());
    }

    public void actionPerformed(ActionEvent e) {
        component.setSentValue(value.getValue());
        DeviceConfigPanel.addMapping(component, (Integer)channels.getSelectedItem() );
        setVisible(false);
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        field.setText(e.getValue() + "%");
    }
}
