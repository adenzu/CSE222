package com.company;

/**
 * Main class
 */
public class Main {

    /**
     * Main function
     * @param args Console arguments
     */
    public static void main(String[] args) {
        driverFunc();
    }

    private static void driverFunc(){
        System.out.println("Creating a Quadtree with range of (0,100)");
        QuadTree quadTree = new QuadTree(0, 100);
        addQuadTree(quadTree,30, 30);
        addQuadTree(quadTree,20, 15);
        addQuadTree(quadTree,50, 40);
        addQuadTree(quadTree,10, 12);
        addQuadTree(quadTree,40, 20);
        addQuadTree(quadTree,25, 60);
        addQuadTree(quadTree,15, 25);

        System.out.println("Resulting Quadtree:");
        System.out.println(quadTree);
        System.out.println();
        System.out.println();

        //---------------------------------------------

        System.out.println("Creating first binary heap");
        BinaryHeap<Integer> binaryHeap1 = new BinaryHeap<>();

        System.out.println("Creating second binary heap");
        BinaryHeap<Integer> binaryHeap2 = new BinaryHeap<>();

        System.out.println("Filling first binary heap");
        for(int i = 7; i < 24; ++i){
            System.out.println("Adding " + i * i % 31);
            binaryHeap1.add(i * i % 31);
        }
        System.out.println("Result:");
        System.out.println(binaryHeap1);

        System.out.println("Filling second binary heap");
        for(int i = 11; i < 31; ++i){
            System.out.println("Adding " + i * i % 29);
            binaryHeap2.add(i * i % 29);
        }
        System.out.println("Result:");
        System.out.println(binaryHeap2);

        System.out.println("Merging heaps..");
        binaryHeap1.merge(binaryHeap2);
        System.out.println("Result:");
        System.out.println(binaryHeap1);
        System.out.println();
        System.out.println();

        //-----------------------------------

        System.out.println("Creating binary search tree...");
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();

        System.out.println("Filling the binary search tree.");
        for(int i = 13; i < 19; ++i){
            System.out.println("Adding " + i * i % 19);
            binarySearchTree.add(i * i % 19);
        }
        System.out.println(binarySearchTree);

        for(int i = 13; i < 19; ++i){
            deleteBinarySearchTree(binarySearchTree,i * i % 19);
        }
    }

    private static void addQuadTree(QuadTree quadTree, int x, int y) {
        System.out.println("Adding " + x + " " + y);
        System.out.println("Showing iterated nodes:");
        quadTree.add(x, y);
        System.out.println("Tree after insertion:");
        System.out.println(quadTree);
    }

    private static <E extends Comparable<E>> void deleteBinarySearchTree(BinarySearchTree<E> binarySearchTree, E item) {
        System.out.println("Deleting " + item + " from binary search tree...");
        binarySearchTree.delete(item);
        System.out.println("Resulting binary search tree:");
        System.out.println(binarySearchTree);
    }
}
