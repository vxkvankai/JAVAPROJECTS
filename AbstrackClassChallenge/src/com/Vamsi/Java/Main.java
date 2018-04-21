package com.Vamsi.Java;

public class Main {

    public static void main(String[] args) {
        SearchTree tree = new SearchTree(null);
        tree.traverse(tree.getRoot());
        String stringData = "Darwin Brisbane Perth Melbourne Canberra Adelaide Sydney Canberra";
        String[] data = stringData.split( " ");
        for (String s : data){
             list.addItem(new Node(s));
        }

        list.traverse(list.getRoot());

    }
}
