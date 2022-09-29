import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data structure consisting of nodes that may or may not have other nodes on their left and right.
 * @param <E>   Type of elements contained in the tree.
 */
public class BinaryTree<E> {
    /**
     * Root of the tree.
     */
    protected Node<E> root = null;

    /**
     * Default constructor.
     */
    public BinaryTree() {}

    /**
     * Merges two trees with the given value at the top.
     * @param data      Value to be at the top.
     * @param leftTree  Left subtree of this tree.
     * @param rightTree Right subtree of this tree.
     */
    public BinaryTree(E data, BinaryTree<E> leftTree, BinaryTree<E> rightTree) {
        root = new Node<>(data, leftTree == null ? null : leftTree.root, rightTree == null ? null : rightTree.root);
    }

    /**
     * Constructs a tree with given node as root.
     * @param root  New tree's root.
     */
    protected BinaryTree(Node<E> root) {
        this.root = root;
    }

    /**
     * Checks if tree is empty.
     * @return  <code>true</code> if binary tree is empty, <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * @return Left subtree.
     */
    public BinaryTree<E> getLeftSubtree() {
        return new BinaryTree<>(root.getLeft());
    }

    /**
     * @return Right subtree.
     */
    public BinaryTree<E> getRightSubtree() {
        return new BinaryTree<>(root.getRight());
    }

    /**
     * @return  The value of the top node in the tree.
     */
    public E getData() {
        return root.getData();
    }

    /**
     * @return The total number of nodes in this tree.
     */
    public int getSize() {
        if(root == null) return 0;
        return getLeftSubtree().getSize() + getRightSubtree().getSize() + 1;
    }

    /**
     * @return  <code>String</code> representation of the tree.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for(StringBuilder line : toString(root)){
            result.append(line);
            result.append("\n");
        }

        return result.toString();
    }

    /**
     * The class that contains element values.
     * @param <E>   Type of the element.
     */
    protected static class Node<E> {
        /**
         * Data that is hold by this node.
         */
        protected E data;

        /**
         * Left and right nodes.
         */
        protected Node<E> left = null, right = null;

        /**
         * Constructs the node with given item.
         * @param data  Item to contain.
         */
        protected Node(E data) {
            this.data = data;
        }

        /**
         * Copies given node.
         * @param node  Node to be copied.
         */
        protected Node(Node<E> node) {
            data  = node.data;
            left  = node.left;
            right = node.right;
        }

        /**
         * Constructs the node with given item.
         * @param data  Item to contain.
         * @param left  Left node.
         * @param right Right node.
         */
        protected Node(E data, Node<E> left, Node<E> right) {
            this(data);
            this.left = left;
            this.right = right;
        }

        /**
         * Checks if this node has left node.
         * @return  <code>true</code> if this node has left node, <code>false</code> otherwise.
         */
        protected boolean hasLeft() {
            return left != null;
        }

        /**
         * Checks if this node has right node.
         * @return  <code>true</code> if this node has right node, <code>false</code> otherwise.
         */
        protected boolean hasRight() {
            return right != null;
        }

        /**
         * Checks if node is leaf.
         * @return  <code>true</code> if it is, <code>false</code> otherwise.
         */
        protected boolean isLeaf() {
            return left == null && right == null;
        }

        /**
         * Generates <code>String</code> representation of this node.
         * @return  <code>String</code> representation of this node.
         */
        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    ", left=" + (left == null ? null : left.data) +
                    ", right=" + (right == null ? null : right.data) +
                    '}';
        }

        /**
         * @return  Data of this node.
         */
        protected E getData() {
            return data;
        }

        /**
         * @return  Left node of this node.
         */
        protected Node<E> getLeft() {
            return left;
        }

        /**
         * @return  Right node of this node.
         */
        protected Node<E> getRight() {
            return right;
        }

        /**
         * Sets data of this node to given data.
         * @param data  New data value for this node.
         */
        protected void setData(E data) {
            this.data = data;
        }

        /**
         * Sets left node of this node to given node.
         * @param left  New left node for this node.
         */
        protected void setLeft(Node<E> left) {
            this.left = left;
        }

        /**
         * Sets right node of this node to given node.
         * @param right  New right node value for this node.
         */
        protected void setRight(Node<E> right) {
            this.right = right;
        }
    }

    private List<StringBuilder> toString(Node<E> root) {
        if(root == null) return new ArrayList<>(Collections.singleton(new StringBuilder("null")));

        List<StringBuilder> result = new ArrayList<>(Collections.singleton(new StringBuilder().append(root.data)));

        for(StringBuilder line : toString(root.left)){
            result.add(line.insert(0, "  "));
        }

        for(StringBuilder line : toString(root.right)){
            result.add(line.insert(0, "  "));
        }

        return result;
    }


    /**
     * <a href="https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram-in-java">https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram-in-java</a>
     */
    public static class BTreePrinter {

        /**
         * Fancy prints a binary tree.
         * @param root  Root of tree.
         * @param <T>   Type of data contained in the tree.
         */
        public static <T extends Comparable<?>> void printNode(Node<T> root) {
            int maxLevel = BTreePrinter.maxLevel(root);

            printNodeInternal(Collections.singletonList(root), 1, maxLevel);
        }

        private static <T extends Comparable<?>> void printNodeInternal(List<Node<T>> nodes, int level, int maxLevel) {
            if (nodes.isEmpty() || BTreePrinter.isAllElementsNull(nodes))
                return;

            int floor = maxLevel - level;
            int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
            int firstSpaces = (int) Math.pow(2, (floor)) - 1;
            int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

            BTreePrinter.printWhitespaces(firstSpaces);

            List<Node<T>> newNodes = new ArrayList<Node<T>>();
            for (Node<T> node : nodes) {
                if (node != null) {
                    System.out.print(node.data);
                    newNodes.add(node.left);
                    newNodes.add(node.right);
                } else {
                    newNodes.add(null);
                    newNodes.add(null);
                    System.out.print(" ");
                }

                BTreePrinter.printWhitespaces(betweenSpaces);
            }
            System.out.println("");

            for (int i = 1; i <= endgeLines; i++) {
                for (int j = 0; j < nodes.size(); j++) {
                    BTreePrinter.printWhitespaces(firstSpaces - i);
                    if (nodes.get(j) == null) {
                        BTreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
                        continue;
                    }

                    if (nodes.get(j).left != null)
                        System.out.print("/");
                    else
                        BTreePrinter.printWhitespaces(1);

                    BTreePrinter.printWhitespaces(i + i - 1);

                    if (nodes.get(j).right != null)
                        System.out.print("\\");
                    else
                        BTreePrinter.printWhitespaces(1);

                    BTreePrinter.printWhitespaces(endgeLines + endgeLines - i);
                }

                System.out.println("");
            }

            printNodeInternal(newNodes, level + 1, maxLevel);
        }

        private static void printWhitespaces(int count) {
            for (int i = 0; i < count; i++)
                System.out.print(" ");
        }

        private static <T extends Comparable<?>> int maxLevel(Node<T> node) {
            if (node == null)
                return 0;

            return Math.max(BTreePrinter.maxLevel(node.left), BTreePrinter.maxLevel(node.right)) + 1;
        }

        private static <T> boolean isAllElementsNull(List<T> list) {
            for (Object object : list) {
                if (object != null)
                    return false;
            }

            return true;
        }
    }
}
