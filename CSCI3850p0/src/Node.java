import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * keyword: Dictionary item
 * concurrence: how many times it appears
 * specific: filenames for document appearances 
 */
public class Node {
    String keyword;
    int concurrence = 0;
    ConcurrentLinkedQueue<Node> specific = new ConcurrentLinkedQueue();
}
