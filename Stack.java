/*
* Generic Stack helper part of ACS Project:
* World's Worst Compiler
*
* see github.com/jahatfriends/DijkstrasDeque for mor information
*
* (c) 2021 Joel Hammer
* Friends School of Baltimore
*
*
*/

import java.util.NoSuchElementException;
import java.util.Iterator;

/**
* Generic implementation of a stack.
*/
public class Stack<T> implements Iterable<T>{
    
    private Node first;
    private int size;
    
    /**
    * Construct a stack, initially empty.
    */
    public Stack() {
        first = null;
    }
    
    //Node class to create a singly-linked list
    private class Node {
        T item;
        Node next;
    }
    
    /**
    * Add the given item to the top of the stack.
    * @param item The item to be added to the top of the stack.
    */
    public void push(T item) {
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
    }
    
    /**
    * Pop the top item from the stack. The item is returned and removed.
    * @return the item popped from the top of the stack.
    */
    public T pop() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("The stack is empty. Cannot pop additional elements");
        }
        
        T item = first.item;
        first = first.next;
        return item;
    }
    
    /**
    * Allows the top item of the stack to be seen without popping. The value is
    * returned but not removed.
    * @return the top element of the stack.
    */
    public T peek() {
        return first.item;
    }
    
    /**
    * Checks if the stack is empty.
    * @return {@code true} if the stack is empty.
    */
    public boolean isEmpty() {
        return first == null;
    }
    
    /**
    * Returns an iterator over the stack. Iteration begins from the top of the
    * stack and continues down. Elements are touched in the reverse order that
    * they were pushed (that is the order in which they would be popped).
    */
    @Override
    public Iterator<T> iterator() {
        return new StackIterator();
    }
    
    //Iterator over the stack--A helper class.
    private class StackIterator implements Iterator<T> {
        private Node current = first;
        
        @Override
        public boolean hasNext() {
            return current != null;
        }
        
        @Override
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            T item = current.item;
            current = current.next;
            return item;
        }
    }
    
    /**
    * Simple test of the stack using a for-each loop.
    * @param args[] command line args not used
    */
    public static void main(String[] args) {
        Stack<String> strings = new Stack<String>();
        
        String[] words = {"It", "was", "the", "best", "of", "times"};
        
        for(String x:words)
            strings.push(x);
        
        for(String x:strings)
            System.out.println(x);
    }
}