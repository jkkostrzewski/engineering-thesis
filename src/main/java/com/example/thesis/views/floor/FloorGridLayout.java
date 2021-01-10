package com.example.thesis.views.floor;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.klaudeta.PaginatedGrid;

@Slf4j
public class FloorGridLayout extends VerticalLayout {

    private final FloorRepository floorRepository;

    private final ListDataProvider<Floor> floorProvider;
    private final PaginatedGrid<Floor> grid;

    public FloorGridLayout(FloorRepository floorRepository) {
        this.floorRepository = floorRepository;

        grid = new PaginatedGrid<>(Floor.class);
        floorProvider = DataProvider.ofCollection(this.floorRepository.findAll());
        grid.setItems(floorProvider);
        grid.setPageSize(15);
        grid.setPaginatorSize(5);

        HorizontalLayout filters = new HorizontalLayout();

        TextField floorName = new TextField("Floor name");
        floorName.addValueChangeListener(event -> floorProvider.addFilter(
                property -> StringUtils.containsIgnoreCase(property.getName(), floorName.getValue()))
        );
        floorName.setValueChangeMode(ValueChangeMode.EAGER);

        filters.add(floorName);
        add(filters);

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setConfirmButtonTheme("error primary");
        dialog.setHeader("Confirm delete");
        dialog.setText("Are you sure you want to delete the item?");

        dialog.setCancelButton("Cancel", this::onCancel);
        add(dialog);

        GridContextMenu<Floor> contextMenu = new GridContextMenu<>(grid);
        contextMenu.addItem("Remove", event -> {
            event.getItem().ifPresent(floor -> {
                Button saveButton = new Button("Delete", VaadinIcon.DEL.create());
                saveButton.addClickListener(saveButtonEvent -> {
                    removeFloor(floor);
                    dialog.close();
                });
                saveButton.getElement().setAttribute("theme", "primary");
                dialog.setConfirmButton(saveButton);

                dialog.open();
            });
        });

//        grid.addColumn(new NativeButtonRenderer<>("Remove", this::removeProperty));

        add(grid);
    }

    private void removeFloor(Floor floor) {
        log.info(floor.toString() + " floor with id " + floor.getId() + " is being removed", FloorGridLayout.class);
        floorProvider.getItems().remove(floor);
        floorProvider.refreshAll();
        floorRepository.delete(floor);

        Notification.show("Property has been removed");
    }

    private void onCancel(ConfirmDialog.CancelEvent cancelEvent) {

    }
}
