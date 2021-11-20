package com.example.movieapp.components;

import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

/**
 * source: https://edencoding.com/responsive-layouts/
 */
public class BootstrapPane extends GridPane {
    private Breakpoint currentWindowSize = Breakpoint.XSMALL;
    private final List<BootstrapRow> rows = new ArrayList<>();

    public BootstrapPane() {
        super();
        setAlignment(Pos.TOP_CENTER);
        setColumnConstraints();
        setWidthEventHandlers();
    }

    private void setWidthEventHandlers() {
        this.widthProperty().addListener((observable, oldValue, newValue) -> {
            Breakpoint newBreakpoint = Breakpoint.XSMALL;
            if (newValue.doubleValue() >= 576 && newValue.doubleValue() < 768) {
                newBreakpoint = Breakpoint.SMALL;
            } else if (newValue.doubleValue() >= 768 && newValue.doubleValue() < 992) {
                newBreakpoint = Breakpoint.MEDIUM;
            } else if (newValue.doubleValue() >= 992 && newValue.doubleValue() < 1200) {
                newBreakpoint = Breakpoint.LARGE;
            } else if (newValue.doubleValue() >= 1200) {
                newBreakpoint = Breakpoint.XLARGE;
            }

            if (newBreakpoint != currentWindowSize) {
                currentWindowSize = newBreakpoint;
                calculateNodePositions();
            }

        });
    }

    private void calculateNodePositions() {
        int currentGridPaneRow = 0;
        for (BootstrapRow row : rows) {
            currentGridPaneRow += row.calculateRowPositions(currentGridPaneRow, currentWindowSize);
        }
    }

    private void setColumnConstraints() {
        // Remove all current columns;
        getColumnConstraints().clear();

        // Create 12 equally sized columns for layout
        double width = 100.0 / 12.0;
        for (int i = 0; i < 12; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(width);
            getColumnConstraints().add(columnConstraints);
        }
    }

    public void addRow(BootstrapRow row) {
        // prevent duplicate children error
        if (!rows.contains(row)) {
            rows.add(row);
            calculateNodePositions();

            for (BootstrapColumn column : row.getColumns()) {
                getChildren().add(column.getContent());
                GridPane.setFillWidth(column.getContent(), true);
                GridPane.setFillHeight(column.getContent(), true);
            }
        }
    }

    public void removeRow(BootstrapRow row) {
        rows.remove(row);
        calculateNodePositions();
        for (BootstrapColumn column : row.getColumns()) {
            getChildren().remove(column.getContent());
        }
    }
}
