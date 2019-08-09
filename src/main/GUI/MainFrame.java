package main.GUI;

import main.GUI.controller.ControllerPanel;
import main.GUI.event.ControllerAddListener;
import main.controller.ControllerUtils;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class MainFrame extends JFrame implements ControllerListener, ControllerAddListener {
    private final JTabbedPane tabs = new JTabbedPane();
    private final DeviceConfigPanel deviceConfigPanel = new DeviceConfigPanel();
    private final ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
    private final List<Controller> controllerList;
    private static final MainFrame INSTANCE = new MainFrame();
    public static MainFrame getInstance() {
        return INSTANCE;
    }
    protected MainFrame() {
        controllerList = new ArrayList<Controller>(Arrays.asList(ce.getControllers()));
        createLayout();
        ce.addControllerListener(this);
    }


    private void createLayout() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        setSize(getPreferredSize());
        tabs.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        tabs.addTab("Device config", deviceConfigPanel);
        for (Controller controller: controllerList)
            controllerChoosed(controller);
        add(tabs, BorderLayout.CENTER);

    }

    private void initTabComponent(int i) {
        //tabs.setTabComponentAt(i,
        //         new ButtonTabComponent(tabs));
        //tabs.setSelectedIndex(i);
    }
    public void controllerRemoved(ControllerEvent ev) {
        if (ControllerUtils.isValidController(ev.getController())) {
            controllerList.remove(ev.getController()) ;
        }
    }

    public void controllerAdded(ControllerEvent ev) {
        if (ControllerUtils.isValidController(ev.getController())) {
            controllerList.add(ev.getController());
            controllerChoosed(ev.getController());
        }
    }

    public void controllerChoosed(Controller c) {
        if (!ControllerUtils.isValidController(c))
            return;
        for(int i =1; i < tabs.getTabCount(); i++) {
            if (((ControllerPanel)tabs.getComponentAt(i)).getController() == c) {
                return;
            }
        }
        tabs.addTab(c.getName(), new ControllerPanel(c));
        initTabComponent(tabs.getTabCount() - 1);
    }
}
