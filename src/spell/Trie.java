package spell;

public class Trie implements ITrie {
    Node rootNode = new Node();

    @Override
    public void add(String myWord) {
        char[] myLetters = myWord.toCharArray();
        int index = 0;
        populateTrie(rootNode, myLetters, index);
    }

    @Override
    public INode find(String myWord) {
        return null;
    }

    @Override
    public int getWordCount() {
        return 0;
    }

    @Override
    public int getNodeCount() {
        return 0;
    }

    public void populateTrie(Node myNode, char[] myWord, int letterIndex) {
        int letterInt = charToInt(myWord[letterIndex]); //Convert the char to an int
        if (myNode.children[letterInt] == null) { //If the node doesn't exist, generate it
            System.out.print("(" + myWord[letterIndex] + ")");
            myNode.children[letterInt] = new Node(myWord[letterIndex]); //Give it the current char
        }
        else{
            System.out.print(myWord[letterIndex]);
        }
        letterIndex++;

        if (letterIndex < myWord.length) { //If we're not to the end of a word yet
            populateTrie(myNode.children[letterInt], myWord, letterIndex); //Recursive call
        } else { //We've reached the end of the word
            myNode.children[letterInt].increaseCount(); //Increase the count
            System.out.printf("\n%d time(s)\n - \n", myNode.children[letterInt].getValue());
        }
    }

    public int charToInt(char myLetter) {
        return myLetter - 'a';
    }
}
