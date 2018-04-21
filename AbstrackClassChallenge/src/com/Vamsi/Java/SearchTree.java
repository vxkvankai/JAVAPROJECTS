package com.Vamsi.Java;

public class SearchTree implements NodeList {

    private ListItem root = null;

    public SearchTree(ListItem root) {
        this.root = root;
    }

    @Override
    public ListItem getRoot(ListItem item) {
        return null;
    }

    @Override
    public boolean addItem(ListItem item) {
        return false;
    }

    @Override
    public boolean removeItem(ListItem item) {
        return false;
    }

    @Override
    public void traverse(ListItem root) {
        // recursive method
        if(root != null){
            traverse(root.previous());
            System.out.println(root.getValue());
            traverse(root.next());

        }

    }
}
