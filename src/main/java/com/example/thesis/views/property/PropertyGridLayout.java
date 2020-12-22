package com.example.thesis.views.property;

import com.example.thesis.backend.floor.Floor;
import com.example.thesis.backend.floor.FloorRepository;
import com.example.thesis.backend.reservation.Property;
import com.example.thesis.backend.reservation.PropertyRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.klaudeta.PaginatedGrid;


@Slf4j
public class PropertyGridLayout extends VerticalLayout {

    private final PropertyRepository propertyRepository;
    private final FloorRepository floorRepository;

    private final ListDataProvider<Property> propertyProvider;

    @Id("grid")
    private final PaginatedGrid<Property> grid;
    private Property clicked;
    private TextField propertyName;
    private ComboBox<Floor> propertyOwner;

    public PropertyGridLayout(PropertyRepository propertyRepository, FloorRepository floorRepository) {
        this.propertyRepository = propertyRepository;
        this.floorRepository = floorRepository;

        grid = new PaginatedGrid<>(Property.class);
        propertyProvider = DataProvider.ofCollection(this.propertyRepository.findAll());
        grid.setItems(propertyProvider);
        grid.setPageSize(15);
        grid.setPaginatorSize(5);

        HorizontalLayout filters = new HorizontalLayout();

        TextField propertyName = new TextField("Property name");
        propertyName.addValueChangeListener(event -> propertyProvider.addFilter(
                property -> StringUtils.containsIgnoreCase(property.getName(), propertyName.getValue()))
        );
        propertyName.setValueChangeMode(ValueChangeMode.EAGER);

        TextField floorName = new TextField("Floor name");
        floorName.addValueChangeListener(event -> propertyProvider.addFilter(
                property -> StringUtils.containsIgnoreCase(property.getOwner().getName(), floorName.getValue()))
        );
        propertyName.setValueChangeMode(ValueChangeMode.EAGER);     //actually enter has to be pressed to search (?)

        filters.add(propertyName, floorName);
        add(filters);

        ConfirmDialog removeDialog = constructRemoveDialog();
        add(removeDialog);

        Dialog editDialog = constructEditDialog();
        add(editDialog);

        GridContextMenu<Property> contextMenu = new GridContextMenu<>(grid);
        contextMenu.addItem("Remove", event -> {
            event.getItem().ifPresent(property -> {
                clicked = property;
                removeDialog.open();
            });
        });
        contextMenu.addItem("Edit", event -> {
            event.getItem().ifPresent(property -> {
                clicked = property;
                setValuesForEditDialog();
                editDialog.open();
            });
        });

//        grid.addColumn(new NativeButtonRenderer<>("Remove", this::removeProperty));

        add(grid);

    }

    private Dialog constructEditDialog() {
        Dialog editDialog = new Dialog();

        VerticalLayout layout = new VerticalLayout();

        Text dialogName = new Text("Edit property");

        propertyName = new TextField("Name");

        propertyOwner = new ComboBox<>();
        propertyOwner.setItems(floorRepository.findAll());

        HorizontalLayout buttons = new HorizontalLayout();

        Button cancel = new Button("Cancel");
        cancel.addClickListener(e -> editDialog.close());

        Button confirm = new Button("Confirm");
        confirm.addClickListener(e -> {
            clicked.setName(propertyName.getValue());
            clicked.setOwner(propertyOwner.getValue());
            propertyRepository.save(clicked);
            propertyProvider.refreshAll();
            editDialog.close();
        });

        layout.add(dialogName, propertyName, propertyOwner);
        buttons.add(cancel, confirm);
        layout.add(buttons);

        editDialog.add(layout);
        return editDialog;
    }

    private void setValuesForEditDialog() {
        propertyName.setValue(clicked.getName());
        propertyOwner.setValue(clicked.getOwner());
    }

    private ConfirmDialog constructRemoveDialog() {
        ConfirmDialog removeDialog = new ConfirmDialog();
        removeDialog.setConfirmButtonTheme("error primary");
        removeDialog.setHeader("Confirm delete");
        removeDialog.setText("Are you sure you want to delete the item?");

        Button saveButton = new Button("Delete", VaadinIcon.DEL.create());
        saveButton.addClickListener(saveButtonEvent -> {
            removeProperty(clicked);
            removeDialog.close();
        });
        saveButton.getElement().setAttribute("theme", "primary");
        removeDialog.setConfirmButton(saveButton);

        removeDialog.setCancelButton("Cancel", this::onCancel);
        return removeDialog;
    }

    public void refreshGrid() {
        this.propertyProvider.refreshAll();
    }

    private void onCancel(ConfirmDialog.CancelEvent cancelEvent) {

    }

    private void removeProperty(Property property) {
        log.info(property.toString() + " property with id " + property.getId() + " is being removed", PropertyGridLayout.class);
        propertyProvider.getItems().remove(property);
        propertyProvider.refreshAll();
        propertyRepository.delete(property);

        Notification.show("Property has been removed");
    }
}
