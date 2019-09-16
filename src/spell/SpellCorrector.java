package spell;

        import java.io.File;
        import java.io.IOException;
        import java.util.HashSet;
        import java.util.Scanner;
        import java.util.Set;

public class SpellCorrector implements ISpellCorrector {

    Set<String> dictionaryWords = new HashSet<String>();
    Trie dictionaryTree = new Trie();

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File myFile = new File(dictionaryFileName);
        Scanner scanner = new Scanner(myFile);

        while(scanner.hasNext()){
            //dictionaryWords.add(scanner.next());
            dictionaryTree.add(scanner.next());
        }

        //System.out.println(dictionaryWords);
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        if(dictionaryTree.find(inputWord) != null){
            return inputWord;
        }

        return null;
    }
}
