/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.sun.javafx.scene.control.behavior;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.DateCell;

import com.sun.javafx.scene.control.skin.DatePickerContent;
import com.sun.javafx.scene.traversal.Direction;

import static java.time.temporal.ChronoUnit.*;
import static javafx.scene.input.KeyCode.*;

/**
 * Behaviors for LocalDate based cells types. Simply defines methods
 * that subclasses implement so that CellSkinBase has API to call.
 *
 */
public class DateCellBehavior extends CellBehaviorBase<DateCell> {
    /**************************************************************************
     *                          Setup KeyBindings                             *
     *************************************************************************/
    protected static final List<KeyBinding> DATE_CELL_BINDINGS = new ArrayList<KeyBinding>();
    static {
        DATE_CELL_BINDINGS.add(new KeyBinding(UP, "TraverseUp"));
        DATE_CELL_BINDINGS.add(new KeyBinding(DOWN, "TraverseDown"));
        DATE_CELL_BINDINGS.add(new KeyBinding(LEFT, "TraverseLeft"));
        DATE_CELL_BINDINGS.add(new KeyBinding(RIGHT, "TraverseRight"));
        DATE_CELL_BINDINGS.add(new KeyBinding(HOME, "Today"));
        DATE_CELL_BINDINGS.add(new KeyBinding(PAGE_UP, "PreviousMonth"));
        DATE_CELL_BINDINGS.add(new KeyBinding(PAGE_DOWN, "NextMonth"));
        DATE_CELL_BINDINGS.add(new KeyBinding(ENTER, "SelectDate"));
        DATE_CELL_BINDINGS.add(new KeyBinding(SPACE, "SelectDate"));
    }


    public DateCellBehavior(DateCell dateCell) {
        super(dateCell);
    }

    @Override public void callAction(String name) {
        DateCell cell = getControl();
        DatePickerContent dpc = findDatePickerContent(cell);

        if (dpc != null) {
            switch (name) {
              case "Today":         dpc.goToDate(LocalDate.now()); break;
              case "PreviousMonth": dpc.goToDayCell(cell, -1, MONTHS); break;
              case "NextMonth":     dpc.goToDayCell(cell, +1, MONTHS); break;
              case "SelectDate":    dpc.selectDayCell(cell); break;
              default: super.callAction(name);
            }
            return;
        }
        super.callAction(name);
    }

    @Override public void traverse(Node node, Direction dir) {
        switch (dir) {
          case UP:
          case DOWN:
          case LEFT:
          case RIGHT:
              if (node instanceof DateCell) {
                  DatePickerContent dpc = findDatePickerContent(node);
                  if (dpc != null) {
                      DateCell cell = (DateCell)node;
                      switch (dir) {
                        case UP:    dpc.goToDayCell(cell, -1, WEEKS); break;
                        case DOWN:  dpc.goToDayCell(cell, +1, WEEKS); break;
                        case LEFT:  dpc.goToDayCell(cell, -1, DAYS); break;
                        case RIGHT: dpc.goToDayCell(cell, +1, DAYS); break;
                      }
                      return;
                  }
              }
        }
        super.traverse(node, dir);
    }

    protected DatePickerContent findDatePickerContent(Node node) {
        Node parent = node;
        while ((parent = parent.getParent()) != null && !(parent instanceof DatePickerContent));
        return (DatePickerContent)parent;
    }


    @Override protected List<KeyBinding> createKeyBindings() {
        return DATE_CELL_BINDINGS;
    }
}