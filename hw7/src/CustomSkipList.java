import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A novel version of skip list with level ascension for nodes depending on number of level one nodes around them.
 * Also increases the max level whenever number of level one nodes is power of 10.
 * @param <E>   Type of data that is contained in the list.
 */
public class CustomSkipList<E extends Comparable<E>> {
    private static final int SIZE_CONSTANT = 10;    // Size constant that decides the chance of node's ascension and also
                                                    // increase of max level
    private static final int HEAD_LEVEL = 2;        // Initial level of the head node

    private static final int TALL_LEVEL = 2;        // What is the minimum level of tall nodes

    private Node<E> head;

    private int levelOneNodeCount = 0;              // Number of level one nodes

    private int nextLevelingSize = SIZE_CONSTANT;   // Next number of level one nodes there has to be to increase max level

    /**
     * Constructs an empty skip list.
     */
    public CustomSkipList() { }

    /**
     * Adds the given item to this list.
     * @param item  Item to be added.
     * @return      <code>true</code> if the item was added successfully, <code>false</code> otherwise.
     */
    public boolean add(E item) {
        // First item to be added, make it head.
        if (head == null) {
            head = new Node<>(item, HEAD_LEVEL);
            return true;
        }
        // Item has to come before the first node.
        else if (head.data.compareTo(item) > 0) {
            E temp = head.data; // Then swap the values
            head.data = item;
            item = temp;        // And make the prior head value added
        }

        // Find the nodes that have or would have links to this item
        Node<E>[] priorNodes = findPriorNodes(item);

        // Check if item exists in the list already
        if (priorNodes[0].data.equals(item) || (priorNodes[0].links[0] != null && priorNodes[0].links[0].data.equals(item))) return false;

        // Calculate the level of new node.
        int level = 1;
        float ascendChance = (float) countConsecutiveLevelOneNodes(priorNodes[Math.min(1, head.links.length - 1)]) / SIZE_CONSTANT;
        while (level < head.links.length && ThreadLocalRandom.current().nextFloat() < ascendChance) level++;

        // Create the new node.
        Node<E> newNode = new Node<>(item, level);

        // Relink nodes.
        int i;
        for (i = 0; i < level; i++) {
            newNode.links[i] = priorNodes[i].links[i];
            priorNodes[i].links[i] = newNode;
        }

        if (level == 1) levelOneNodeCount++;                // Self-explanatory
        if (levelOneNodeCount % nextLevelingSize == 0) {    // Increase the max level if number of level one nodes is enough
            increaseLevel(head);
            nextLevelingSize *= SIZE_CONSTANT;              // Update needed count
        }

        return true;
    }

    /**
     * Finds the nodes that have links to given item or would have links to given item if it were in the list.
     * @param item  Item whose prior nodes will be found.
     * @return      Array of nodes that do/would link to given item.
     */
    @SuppressWarnings("unchecked")
    private Node<E>[] findPriorNodes(E item) {
        Node<E> currNode = head;

        // Array of nodes that do/would link to given item.
        Node<E>[] nodes = new Node[head.links.length];
        Arrays.fill(nodes, head);

        // Go as long as no node is traversed that has equal or greater data than given item.
        while (currNode.data.compareTo(item) < 0) {
            // For every level, from highest to lowest.
            for (int i = currNode.links.length - 1; i >= 0; i--) {
                // If the next linked node comes before the item.
                if (currNode.links[i] != null && currNode.links[i].data.compareTo(item) < 0) {
                    currNode = currNode.links[i];   // Then get to that node to check further
                    for (int j = i; j >= 0; j--) {  // This and all the lower levels would be linked from this node
                        nodes[j] = currNode;
                    }
                    break;                          // Current node is changed, should restart the loop for it
                }
                // Else this node links to the item.
                for (int j = i; j >= 0; j--) {
                    nodes[j] = currNode;
                }
            }

            // If there's no next node or there is, but it should come after the item then stop the loop
            if (currNode.links[0] == null || currNode.links[0].data.compareTo(item) >= 0) break;
        }

        return nodes;
    }

    /**
     * Counts every consecutive level one nodes after given node.
     * @param head  Start of the nodes that will be excluded from counting.
     * @return      Number of consecutive level one nodes after the given head node.
     */
    private int countConsecutiveLevelOneNodes(Node<E> head) {
        int count = 0;
        Node<E> currNode = head.links[0];

        while (currNode != null && currNode.links.length == 1) {
            count++;
            currNode = currNode.links[0];
        }

        return count;
    }

    /**
     * Increases the max level and level of tall nodes of this skip list.
     * @param head  First node of the list.
     */
    @SuppressWarnings("unchecked")
    private void increaseLevel(Node<E> head) {
        if (head == null) return;

        increaseLevel(head.links[0]);

        // Increase the level of the node only if it's tall.
        if (head.links.length > 1) {
            Node<E>[] oldLinks = head.links;
            head.links = new Node[head.links.length + 1];
            System.arraycopy(oldLinks, 0, head.links, 0, TALL_LEVEL);
            System.arraycopy(oldLinks, TALL_LEVEL - 1, head.links, TALL_LEVEL, oldLinks.length - 1);
        }
    }

    /**
     * @return String representation of this skip list.
     */
    @Override
    public String toString() {
        StringBuilder nodes = new StringBuilder();
        StringBuilder[] levels = new StringBuilder[head.links.length];

        Arrays.setAll(levels, i -> new StringBuilder());

        Node<E> currNode = head;

        while (currNode != null) {
            nodes.append(" ").append(currNode.data).append(" ");

            int i;
            for (i = 0; i < currNode.links.length; i++) {
                levels[i].append(">").append("#").append("-");
            }
            for (; i < levels.length; i++) {
                levels[i].append("---");
            }

            currNode = currNode.links[0];
        }

        nodes.deleteCharAt(0);
        nodes.deleteCharAt(nodes.length() - 1);

        for (StringBuilder levelSB : levels) {
            levelSB.deleteCharAt(0);
            levelSB.deleteCharAt(levelSB.length() - 1);
            nodes.append("\n").append(levelSB);
        }

        return nodes.toString();
    }

    /**
     * Skip list node class.
     * @param <E>   Type of data that is contained.
     */
    protected static class Node<E> {
        /**
         * Linked nodes.
         */
        protected Node<E>[] links;

        /**
         * Contained data.
         */
        protected E data;

        /**
         * Constructs a node with given data and level.
         * @param data  Data to be contained.
         * @param level Level of this node.
         */
        @SuppressWarnings("unchecked")
        protected Node(E data, int level) {
            this.data = data;
            links = new Node[level];
        }
    }
}
