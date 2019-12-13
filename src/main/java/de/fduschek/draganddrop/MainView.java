package de.fduschek.draganddrop;

import com.vaadin.flow.component.grid.dnd.GridDragEndEvent;
import com.vaadin.flow.component.grid.dnd.GridDragStartEvent;
import com.vaadin.flow.component.grid.dnd.GridDropEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import java.util.List;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
public class MainView extends VerticalLayout {

    public MainView() {
        add(buildLabel());
        add(buildVaadinFolderTree());
    }

    private TextArea buildLabel() {
        final TextArea textArea = new TextArea("Steps to reproduce the bug:",
                "1. Drag 'B' onto 'Sub 1'\n" +
                        "2. Drag 'C' onto 'Sub 2'\n" +
                        "3. Expand 'Sub 1'\n" +
                        "4. Drag 'B' onto 'Sub 2'\n" +
                        "-> 'Sub 2' can be expanded, but no children appear\n" +
                        "Press 'F5' to restart");
        textArea.setWidth("600px");
        return textArea;
    }

    MyHierarchicalDataProvider container = null;
    String folder = null;

    private TreeGrid<String> buildVaadinFolderTree() {
        TreeGrid<String> treeGrid = new TreeGrid<>(String.class);
        treeGrid.setWidth("800px");
        treeGrid.setHeight("400px");

        final List<Folder> folders = Folder.createFolders();
        Folder main = new Folder("dummyroot").addSubFolders(folders);
        container = new MyHierarchicalDataProvider(main);

        treeGrid.setDataProvider(container);

        treeGrid.setRowsDraggable(true);
        treeGrid.addDragStartListener(this::onDragStart);
        treeGrid.addDragEndListener(this::onDragEnd);
        treeGrid.addDropListener(this::onDrop);
        treeGrid.setDropMode(com.vaadin.flow.component.grid.dnd.GridDropMode.ON_TOP);

        treeGrid.removeAllColumns();
        treeGrid.addHierarchyColumn(String::valueOf);
        return treeGrid;
    }

     private void onDrop(GridDropEvent<String> e) {
        final Folder originalParent = container.getParent(folder);
        final Folder draggedFolder = container.findFolderByName(folder);
        final Folder targetFolder = container.findFolderByName(e.getDropTargetItem().orElse(null));

        if(targetFolder == draggedFolder) {
            return;
        }

        originalParent.removeSubFolder(draggedFolder);
        targetFolder.addSubFolder(draggedFolder);
        container.refreshAll();
    }

    private void onDragEnd(GridDragEndEvent<String> e) {
        folder = null;
    }

    private void onDragStart(GridDragStartEvent<String> e) {
        folder = e.getDraggedItems().get(0);
    }
}