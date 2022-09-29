/**
 * AVLTree implementation that only has the functionality of balancing existing Binary Trees.
 * @param <E>   Type of the data contained in the tree.
 */
public class AVLTree<E extends Comparable<E>> extends RotatableBinaryTree<E> {

    /**
     * Constructs an AVLTree with given root.
     * @param root  Root of this tree.
     */
    private AVLTree(Node<E> root) {
        super(root);
    }

    /**
     * Balances given binary tree with rotations and returns the balanced tree as an AVLTree.
     * @param binaryTree    Tree to be balanced.
     * @return              Balanced AVLTree.
     * @param <E>           Type of the data contained in the tree.
     */
    public static<E extends Comparable<E>> AVLTree<E> balanceTree(BinaryTree<E> binaryTree) {
        AVLNode<E> root = balanceTree(binaryTree.root);
        return new AVLTree<>(root);
    }

    /**
     * Recursively balances binary tree by its root.
     * @param nodeRoot  Root node of the tree.
     * @return          Balanced root.
     * @param <E>       Type of the data contained in the tree.
     */
    private static<E extends Comparable<E>> AVLNode<E> balanceTree(Node<E> nodeRoot) {
        if (nodeRoot == null)       return null;                    // Self-explanatory
        else if (nodeRoot.isLeaf()) return new AVLNode<>(nodeRoot); // If node is leaf it's inherently balanced

        // Cast the node to an AVL node immediately.
        AVLNode<E> root = new AVLNode<>(nodeRoot);

        root.left  = balanceTree(root.left);    // Balance left subtree
        root.right = balanceTree(root.right);   // Balance right subtree

        // Left case
        if (root.getBalance() < AVLNode.LEFT_HEAVY) {
            AVLNode<E> left = root.getLeft();

            // Left-Right case
            if (left.getBalance() > AVLNode.BALANCED) {
                root.left = rotateLeft(left);
            }

            root = (AVLNode<E>) rotateRight(root);
            root.right = balanceTree(root.right); // Now that root is rotated right, right subtree may be unbalanced, fix that
        }
        // Right case
        else if (root.getBalance() > AVLNode.RIGHT_HEAVY) {
            AVLNode<E> right = root.getRight();

            // Right-Left case
            if (right.getBalance() < AVLNode.BALANCED) {
                root.right = rotateRight(right);
            }

            root = (AVLNode<E>) rotateLeft(root);
            root.left  = balanceTree(root.left); // Now that root is rotated left, left subtree may be unbalanced, fix that
        }

        // Return balanced root
        return root;
    }

    /**
     * Binary tree node with the quality of having balance.
     * @param <E>   Type of data that is stored inside.
     */
    private static class AVLNode<E> extends Node<E> {

        protected static final int LEFT_HEAVY  = -1;    // Node is unbalanced towards left node
        protected static final int BALANCED    =  0;    // Node is balanced
        protected static final int RIGHT_HEAVY =  1;    // Node is unbalanced towards right node

        /**
         * Construct an AVL node with the qualities of given binary tree node.
         * @param node  Binary tree whose data will be copied.
         */
        protected AVLNode(Node<E> node) {
            super(node);
        }

        /**
         * Returns the height of the tree if this node was to be the root.
         * @return  Height of tree.
         */
        protected int getHeight() {
            int height = 0;

            // Get the maximum of its subtrees
            if (hasLeft())  height = getLeft().getHeight();
            if (hasRight()) height = Math.max(height, getRight().getHeight());

            // It's one more than that
            return height + 1;
        }

        /**
         * Subtracts height of left node from right node and returns the result.
         * @return  Balance of this node.
         */
        protected int getBalance() {
            int leftHeight  = hasLeft()  ? getLeft().getHeight()  : 0;
            int rightHeight = hasRight() ? getRight().getHeight() : 0;

            return rightHeight - leftHeight;
        }

        /**
         * @return  Left node of this node.
         */
        @Override
        protected AVLNode<E> getLeft() {
            return (AVLNode<E>) super.getLeft();
        }

        /**
         * @return  Right node of this node.
         */
        @Override
        protected AVLNode<E> getRight() {
            return (AVLNode<E>) super.getRight();
        }
    }
}
