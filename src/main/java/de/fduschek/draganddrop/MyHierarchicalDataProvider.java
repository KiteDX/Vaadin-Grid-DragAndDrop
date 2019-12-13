package de.fduschek.draganddrop;

import com.vaadin.flow.data.provider.hierarchy.AbstractHierarchicalDataProvider;
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery;
import com.vaadin.flow.function.SerializablePredicate;

import java.util.List;
import java.util.stream.Stream;

public class MyHierarchicalDataProvider extends AbstractHierarchicalDataProvider<String, SerializablePredicate<String>> {
    private final Folder rootFolder;

    public MyHierarchicalDataProvider(Folder rootFolder) {
        this.rootFolder = rootFolder;
    }

    @Override
    public boolean hasChildren(String itemId) {
        Folder folder = findFolderByName(rootFolder, itemId);
        if(folder == null) {
            return !rootFolder.getSubFolders().isEmpty();
        }

        return !folder.getSubFolders().isEmpty();
    }

    public Folder getParent(String itemId) {
        return getParent(rootFolder.getSubFolders(), itemId);
    }

    public Folder getParent(List<Folder> folders, String itemId) {
        for(Folder folder : folders) {
            if(folder.getSubFolders().stream().filter(fldr -> fldr.getName().equals(itemId)).count() > 0) {
                return folder;
            }
            Folder parentFolder = getParent(folder.getSubFolders(), itemId);
            if(parentFolder != null) {
                return parentFolder;
            }
        }
        return null;
    }

    public Folder findFolderByName(String itemId) {
        return findFolderByName(rootFolder, itemId);
    }

    public Folder findFolderByName(Folder parentFolder, String itemId) {
        if(parentFolder.getName().equals(itemId)) {
            return parentFolder;
        }

        for(Folder folder : parentFolder.getSubFolders()) {
            final Folder folderByName = findFolderByName(folder, itemId);
            if(folderByName != null) {
                return folderByName;
            }
        }
        return null;
    }

    @Override
    public int getChildCount(HierarchicalQuery<String, SerializablePredicate<String>> query) {
        final String parentName = query.getParent();
        final Folder folder = findFolderByName(rootFolder, parentName);
        if(folder == null) {
            return rootFolder.getSubFolders().size();
        }

        return folder.getSubFolders().size();
    }

    @Override
    public Stream<String> fetchChildren(HierarchicalQuery<String, SerializablePredicate<String>> query) {
        final String parentId = query.getParent();
        Folder folder = findFolderByName(rootFolder, parentId);
        if(folder == null) {
            return rootFolder.getSubFolders().stream().map(fldr -> fldr.getName());
        }

        return folder.getSubFolders().stream().map(fldr -> fldr.getName());
    }

    @Override
    public boolean isInMemory() {
        return true;
    }
}