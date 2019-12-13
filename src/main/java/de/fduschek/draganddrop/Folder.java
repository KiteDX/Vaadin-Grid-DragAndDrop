package de.fduschek.draganddrop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Folder {
    private List<Folder> subFolders = new ArrayList<>();
    private String name;

    public Folder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Folder> getSubFolders() {
        return subFolders;
    }

    public Folder addSubFolder(Folder subFolder) {
        this.subFolders.add(subFolder);
        return this;
    }

    public Folder removeSubFolder(Folder subFolder) {
        this.subFolders.remove(subFolder);
        return this;
    }

    public Folder addSubFolders(List<Folder> subFolders) {
        this.subFolders.addAll(subFolders);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder = (Folder) o;
        return subFolders.equals(folder.subFolders) &&
                name.equals(folder.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subFolders, name);
    }

    public static List<Folder> createFolders() {
        final Folder c = new Folder("Test")
                .addSubFolder(new Folder("A")
                        .addSubFolder(new Folder("Sub 1"))
                        .addSubFolder(new Folder("Sub 2"))
                )
                .addSubFolder(new Folder("B"))
                .addSubFolder(new Folder("C"));

        return Arrays.asList(c);
    }
}