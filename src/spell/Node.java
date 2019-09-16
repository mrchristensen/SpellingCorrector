package spell;

public class Node implements INode {
    Node[] children = new Node[26];
    int count = 0;
    char letter;

    public Node() { //Root node constructor
    }

    public Node(char letter) {
        this.letter = letter;
    }

    @Override
    public int getValue() {
        return count;
    }

    public void increaseCount(){
        count++;
    }

}
