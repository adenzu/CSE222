import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * Main class.
 */
public class Main {

    /**
     * Main function.
     * @param args  Console arguments.
     */
    public static void main(String[] args) {
        print("10 random cases will be tested for the first question...");
        for (int i = 0; i < 10; i++) {
            print("\nQ1." + i + ")");
            question1(17);
        }

        print("10 random cases will be tested for the second question trees with size of 8...");
        for (int i = 0; i < 10; i++) {
            print("\nQ2." + i + ")");
            question2(8);
        }

        print("One custom case for question 2 where all the nodes are on the same side:");
        BinaryTree<Integer> binaryTree = new BinaryTree<>(0, null, null);
        for (int i = 1; i < 8; i++) binaryTree = new BinaryTree<>(i, binaryTree, null);
        BinaryTree.BTreePrinter.printNode(binaryTree.root);

        AVLTree<Integer> avlTree = AVLTree.balanceTree(binaryTree);
        System.out.println("After balancing:");
        BinaryTree.BTreePrinter.printNode(avlTree.root);

        print("10 random cases will be tested for the third question with varying sizes...\n(the number of digits of integers mess up printing but links and node levels are represented correctly)");
        for (int i = 0; i < 11; i++) {
            print("\nQ3." + i + ")");
            question3(20 * i + 10, 100 * i + 10);
        }
    }

    private static void print(Object object) {
        System.out.println(object);
    }

    private static void question1(int size) {
        BinaryTree<String> binaryTree = createRandomBinaryTree(i -> "*", size);
        System.out.println("Randomly generated binary tree (used placeholders instead of actual values for more pleasing visual):");
        System.out.println(binaryTree);

        Integer[] integers = new Integer[binaryTree.getSize()]; Arrays.setAll(integers, i -> i);
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>(binaryTree, integers);
        System.out.println("Generated BinarySearchTree with first " + size + " integers that have the same structure:");
        System.out.println(binarySearchTree);
    }

    private static void question2(int size) {
        BinaryTree<Integer> binaryTree = createRandomBinaryTree(i -> i, size);

        System.out.println("Randomly generated binary tree:");
        BinaryTree.BTreePrinter.printNode(binaryTree.root);

        AVLTree<Integer> avlTree = AVLTree.balanceTree(binaryTree);
        System.out.println("After balancing:");
        BinaryTree.BTreePrinter.printNode(avlTree.root);
    }

    private static void question3(int size, int bound) {
        CustomSkipList<Integer> customSkipList = new CustomSkipList<>();
        for (int i = 0; i < size; i++) {
            customSkipList.add(ThreadLocalRandom.current().nextInt(0, bound));
        }
        System.out.println("Skip list created with adding " + size + " randomly generated integers:");
        System.out.println(customSkipList);
    }

    private static<E> BinaryTree<E> createRandomBinaryTree(Function<Integer, E> itemFunc, int size) {
        BinaryTree<E> binaryTree = new BinaryTree<>(itemFunc.apply(0), null, null);

        int offset = ThreadLocalRandom.current().nextInt(0, 1);

        for (int i = 1; i < size; i++) {
            BinaryTree.Node<E> node = binaryTree.root;

            nodeAddition: {
                while (true) {
                    switch (ThreadLocalRandom.current().nextInt(offset, 3 + offset) % 2) {
                        case 0:
                            if (node.hasLeft()) node = node.left;
                            else {
                                node.left = new BinaryTree.Node<>(itemFunc.apply(i));
                                break nodeAddition;
                            }
                            break;
                        case 1:
                            if (node.hasRight()) node = node.right;
                            else {
                                node.right = new BinaryTree.Node<>(itemFunc.apply(i));
                                break nodeAddition;
                            }
                            break;
                    }
                }
            }
        }

        return binaryTree;
    }
}