package spell;

public class Node implements INode {
    Node[] children = new Node[26];
    char[] prefix;
    int count = 0;
    char letter;

    public Node() { //Root node constructor
    }

    public Node(char letter, char[] prefix) {
        this.letter = letter;
        this.prefix = prefix;
    }

    @Override
    public int getValue() {
        return count;
    }

    @Override
    public String toString() {
        return String.valueOf(prefix) + letter;
    }

    public void increaseCount(){
        count++;
    }

}
