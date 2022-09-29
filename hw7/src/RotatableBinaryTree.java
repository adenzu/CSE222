/**
 * A binary tree with the functionality of rotation of its nodes.
 * @param <E>   Type of data that is contained within.
 */
public abstract class RotatableBinaryTree<E> extends BinaryTree<E> {

    /**
     * Constructs an empty rotatable binary tree.
     */
    public RotatableBinaryTree() {
        super();
    }

    /**
     * Constructs a rotatable binary tree with given root.
     * @param root  Root of this tree.
     */
    protected RotatableBinaryTree(Node<E> root) {
        super(root);
    }

    /**
     * Rotates the given parent node to right and returns the new parent node.
     * @param root  Root (parent) to be rotated.
     * @return      New root (parent).
     * @param <E>   Type of data that is contained.
     */
    protected static<E> Node<E> rotateRight(Node<E> root) {
        Node<E> temp = root.left;
        root.left = temp.right;
        temp.right = root;
        return temp;
    }

    /**
     * Rotates the given parent node to left and returns the new parent node.
     * @param root  Root (parent) to be rotated.
     * @return      New root (parent).
     * @param <E>   Type of data that is contained.
     */
    protected static<E> Node<E> rotateLeft(Node<E> root) {
        Node<E> temp = root.right;
        root.right = temp.left;
        temp.left = root;
        return temp;
    }
}
