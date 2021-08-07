package com.slack.cache;

import com.google.inject.Singleton;

import java.util.*;

class TrieNode
{
    final Map<Character, TrieNode> children;
    boolean isEndWord;

    public TrieNode()
    {
        this.isEndWord = false;
        this.children = new HashMap<>();
    }

    //Function to mark the currentNode as Leaf
    public void markAsLeaf()
    {
        this.isEndWord = true;
    }
}

@Singleton
public class Trie
{
    private final TrieNode root; //Root Node

    Trie()
    {
        root = new TrieNode();
    }

    public void populateTrie(Set<String> movieNames)
    {
        for (String movie : movieNames)
        {
            insert(movie);
        }
    }

    private void insert(String movieName)
    {
        if (movieName == null)
        {
            return;
        }
        TrieNode current = root;
        for (char c : movieName.toCharArray())
        {
            TrieNode temp = current.children.get(c);
            if (temp == null)
            {
                temp = new TrieNode();
            }
            current.children.put(c, temp);
            current = temp;
        }
        current.markAsLeaf();
    }

    public List<String> getRecommendations(String prefix)
    {
        List<String> suggestions = new ArrayList<>();
        TrieNode lastNode = root;
        StringBuilder curr = new StringBuilder();
        for (char c : prefix.toCharArray())
        {
            lastNode = lastNode.children.get(c);
            if (lastNode == null)
            {
                return suggestions;
            }
            curr.append(c);
        }
        recommendationHelper(lastNode, suggestions, curr);
        return suggestions;
    }

    private void recommendationHelper(final TrieNode root, final List<String> suggestions, final StringBuilder curr)
    {
        if (root.isEndWord)
        {
            suggestions.add(curr.toString());
        }
        if (root.children.isEmpty() || suggestions.size() == 10)
        {
            return;
        }
        Map<Character, TrieNode> children = root.children;
        for (Map.Entry<Character, TrieNode> entry : children.entrySet())
        {
            recommendationHelper(entry.getValue(), suggestions, curr.append(entry.getKey()));
            curr.deleteCharAt(curr.length() - 1);
        }
    }
}
