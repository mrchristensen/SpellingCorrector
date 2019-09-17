package spell;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

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
        Node myNode;
        //If the word is in the dictionary
        myNode = findWord(rootNode, myLetters, 0);
        if(myNode != null){
            return myNode;
        }

        //If the word isn't in the dictionary try to find a similar word
        Set<String> candidateWords = new WordTransforms().generateSuggestions(myWord);

        //Call WORD TRANSFORMS TODO
        System.out.println("Candidate words: " + candidateWords);

        //Check to see if the candidate words are valid (in the dictionary)
        Set<String> potentialWords = new HashSet<String>();

        for (String word : candidateWords) {
            if(findWord(rootNode, word.toCharArray(), 0) != null){ //TODO: Clean up the args of findWord()
                potentialWords.add(word);
            }
        }
        System.out.println("Potential words: " + potentialWords);

        if(potentialWords.size() == 0){
            //TODO:Find distance of two
        }
        else if(potentialWords.size() == 1){
            return findWord(rootNode, potentialWords.iterator().next().toCharArray(), 0);  //TODO:potentialWords.iterator().next().toCharArray() is SOOOOOOO bad
        }
        else{ //If there are more than one potential word
            //TODO: most frequent
        }

        return null; //If the word is not in the dictionary, nor does a similar word exist
    }

    @Override
    public int getWordCount() {
        return countWords(rootNode);
    }

    @Override
    public int getNodeCount() {
        return countNodes(rootNode);
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
            return myNode.children[letterInt];
        }

        return finalNode;
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


}