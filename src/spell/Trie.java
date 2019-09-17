package spell;

import javax.swing.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

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
        char[] myLetters = myWord.toCharArray();
        Node myNode = findWord(rootNode, myLetters, 0);
        return myNode; //Wil be null if it's not found
    }

    @Override
    public int getWordCount() {
        return countWords(rootNode);
    }

    @Override
    public int getNodeCount() {
        return countNodes(rootNode) + 1; //Plus one to count the root node
    }

    public void populateTrie(Node myNode, char[] myWord, int letterIndex) {
        int letterInt = charToInt(myWord[letterIndex]); //Convert the char to an int
        if (myNode.children[letterInt] == null) { //If the node doesn't exist, generate it
            //System.out.print("(" + myWord[letterIndex] + ")");
            myNode.children[letterInt] = new Node(myWord[letterIndex], Arrays.copyOfRange(myWord, 0, letterIndex)); //Give it the current char
        }
        else{
            //System.out.print(myWord[letterIndex]);
        }
        letterIndex++;

        if (letterIndex < myWord.length) { //If we're not to the end of a word yet
            populateTrie(myNode.children[letterInt], myWord, letterIndex); //Recursive call
        } else { //We've reached the end of the word
            myNode.children[letterInt].increaseCount(); //Increase the count
            //System.out.printf("\n%d time(s)\n - \n", myNode.children[letterInt].getValue());
        }

    }

    private Node findWord(Node myNode, char[] myWord, int letterIndex){ //TODO: Fix this so that there is no letterIndex, just pass a substring of a string (and cut off the first letter
        int letterInt = charToInt(myWord[letterIndex]);
        Node finalNode;
        if(myNode.children[letterInt] == null){
            return null;
        }

        letterIndex++;

        if(letterIndex < myWord.length) { //If we're not to the end of the word yet
            finalNode = findWord(myNode.children[letterInt], myWord, letterIndex);
        }
        else{ //We've successfully found the end of the word
            if(myNode.children[letterInt].getValue() > 0) {
                return myNode.children[letterInt];
            }
            else{
                return null;
            }
        }

        return finalNode;
    }

    @Override
    public String toString() { //Todo: must be recursive
        return traverseTree(rootNode);
    }

    @Override
    public boolean equals(Object obj) { //Todo: must be recursive
        if(obj == null){
            return false;
        }
        if(obj.getClass() == spell.Trie.class){
            Trie myTrie = (Trie) obj;
            return compareTries(rootNode, myTrie.rootNode);
        }
        else{
            return false;
        }
    }

    private boolean compareTries(Node node1, Node node2){

        for (int i = 0; i < 26; i++) {
            if(node1.children[i] != null || node2.children[i] != null){
                if(node1.children[i] == null || node2.children[i] == null){
                    return false;
                }
                else if(node1.children[i].getValue() != node2.children[i].getValue()){
                    return false;
                }
                else{ //continue checking
                    if(compareTries(node1.children[i], node2.children[i]) == false){
                        return false;
                    }
                }
            }
        }

        //If you make it through the whole tree without finding and error return true
        return true;
    }

    @Override
    public int hashCode() { //Todo
        return super.hashCode();//Hashcode int that you update everytime that you add word (call hashcode on the word) then multiply it by int
    }

    private int charToInt(char myLetter) {
        return myLetter - 'a';
    }

    private int countWords(Node myNode){
        int wordCount = 0;

        for (int i = 0; i < 26; i++) {
            if(myNode.children[i] != null){
                wordCount += countWords(myNode.children[i]);
                if (myNode.children[i].getValue() > 0){
                    wordCount++;
                }
            }
        }

        return wordCount;
    }

    private int countNodes(Node myNode){
        int nodeCount = 0;

        for (int i = 0; i < 26; i++) {
            if(myNode.children[i] != null){
                nodeCount += countNodes(myNode.children[i]);
                nodeCount++;
                }
            }
        return nodeCount;
    }

    private String traverseTree(Node myNode){
        String output = "";

        for (int i = 0; i < 26; i++) {
            if(myNode.children[i] != null){
                output += traverseTree(myNode.children[i]);
                if (myNode.children[i].getValue() > 0){
                    output = output + myNode.children[i].toString() + "\n";
                }
            }
        }

        return output;
    }


}