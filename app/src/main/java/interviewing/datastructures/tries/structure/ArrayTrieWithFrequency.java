package interviewing.datastructures.tries.structure;


import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArrayTrieWithFrequency extends ArrayTrie {
    private int frequency;
    private Character key;
    private ArrayTrieWithFrequency[] children;
    private boolean isEndOfWord;

    public ArrayTrieWithFrequency() {
        this(null);
    }

    public ArrayTrieWithFrequency(Character key) {
        this.key = key;
        children = new ArrayTrieWithFrequency[26];
        frequency = 0;
    }

    @Override
    public void insert(String word) {
        insertHelper(word, 0);
    }

    protected void insertHelper(String word, int offset) {
        if (offset == word.length()) {
            this.isEndOfWord = true;
            return;
        }
        char c = word.charAt(offset);
        int pos = c - 'a';
        if (children[pos] == null) {
            children[pos] = new ArrayTrieWithFrequency(c);
        }
        children[pos].insertHelper(word, offset + 1);
        children[pos].frequency++;
    }

    @Override
    public boolean contains(String word) {
        return findHelper(word, 0);
    }

    @Override
    public Collection<Trie> children() {
        return Arrays.stream(children)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Character getCharacter() {
        return key;
    }

    @Override
    public boolean hasChild(char c) {
        return children[c - 'a'] != null;
    }

    @Override
    public ArrayTrieWithFrequency getChild(char c) {
        return children[c - 'a'];
    }

    @Override
    public boolean isEndOfWord() {
        return isEndOfWord;
    }

    public void setEndOfWord() {
        isEndOfWord = true;
    }

    public ArrayTrieWithFrequency[] getChildren() {
        return children;
    }

    @Override
    public List<String> getWordsForPrefix(String prefix) {
        return getWordsForPrefixHelper(prefix, 0);
    }

    private List<String> getWordsForPrefixHelper(String prefix, int offset) {
        List<String> output = new ArrayList<>();
        if (offset == prefix.length()) {
            if (this.isEndOfWord) {
                output.add("");
            }
            output.addAll(children().stream()
                    .map(trie -> new AbstractMap.SimpleImmutableEntry<>(trie.getCharacter(), trie.getWordsForPrefix("")))
                    .map(kv -> kv.getValue().stream()
                            .map(str -> kv.getKey() + str)
                            .collect(Collectors.toList()))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList()));
        } else {
            char c = prefix.charAt(offset);
            int pos = c - 'a';
            output.addAll(children[pos].getWordsForPrefixHelper(prefix, offset + 1).stream()
                    .map(str -> c + str)
                    .collect(Collectors.toList()));
        }
        return output;
    }

    private boolean findHelper(String word, int offset) {
        if (offset == word.length()) {
            return isEndOfWord;
        }
        char c = word.charAt(offset);
        int pos = c - 'a';
        if (children[pos] == null) {
            return false;
        }
        return children[pos].findHelper(word, offset + 1);
    }

    public static ArrayTrieWithFrequency buildTrie(Collection<String> words) {
        ArrayTrieWithFrequency res = new ArrayTrieWithFrequency();
        for (String word : words) {
            res.insert(word);
        }
        return res;
    }


    @Override
    public String toString() {
        return ("(" + key + "," + frequency + ")") + children();
    }

    public int getFrequency() {
        return frequency;
    }
}