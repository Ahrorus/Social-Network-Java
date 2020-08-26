// CIS_22C - George Korolev
// CIS_22C - Ahror Abdulhamidov

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedDictionary<K, V> implements DictionaryInterface<K, V>, Serializable
{
    private Node firstNode; // Reference to first node of chain
    private int numberOfEntries;

    public void initializeDataFields(){
        numberOfEntries = 0;
        firstNode = null;
    }

    public LinkedDictionary()
    {
        initializeDataFields();
    } // end default constructor


    public V add(K key, V value) {
        if (!contains(key)) {
            if (numberOfEntries >= 1) {
                Node nextNode = firstNode;
                while(nextNode.getNextNode() != null){
                    nextNode = nextNode.getNextNode();
                }
                nextNode.setNextNode(new Node(key, value));
            }else{
                firstNode = new Node(key, value);
            }
            numberOfEntries++;
            return value;
        } else {
            Node currentNode = firstNode;
            while (currentNode.getKey() != key){
                currentNode = currentNode.getNextNode();
            }
            V value1 = currentNode.getValue();
            currentNode.setValue(value);
            return value1;
        }
    }

    @Override
    public V remove(K key) {
        if (contains(key)){
            Node currentNode, previousNode;
            if (firstNode.getNextNode() != null) {
                previousNode = firstNode;
                currentNode = firstNode.getNextNode();
            }else{//contains only one entry and also contains the key so it must be cleared
                V value = firstNode.getValue();
                clear();
                return value;
            }
            if (firstNode.getKey() == key){//edge case for if the first entry is the correct entry
                firstNode = firstNode.getNextNode();
                numberOfEntries--;
            }else {
                while (currentNode.getKey() != key) {
                    previousNode = currentNode;
                    currentNode = currentNode.getNextNode();
                }

                V value = currentNode.getValue();

                if (currentNode.getNextNode() != null) {
                    previousNode.setNextNode(currentNode.getNextNode());
                }else{
                    previousNode.setNextNode(null);
                }

                numberOfEntries--;
                return value;
            }



        }else{
            return null;
        }
        return null;
    }

    @Override
    public V getValue(K key) {
        V value;
        K currentK;
        Iterator<K> scannerKey = getKeyIterator();
        Iterator<V> scannerValue = getValueIterator();

        while(scannerKey.hasNext()){
            currentK = scannerKey.next();
            value = scannerValue.next();
            if (key == currentK){
                return value;
            }
        }
        
        return null;
        
    }

    @Override
    public boolean contains(K key) {
        Iterator<K> scanner = getKeyIterator();

        while(scanner.hasNext()){
            if (key == scanner.next()){
                return true;//if found return true
            }
        }
        return false;//otherwise return false
    }

    @Override
    public Iterator<K> getKeyIterator() {
        return new KeyIterator();
    }

    @Override
    public Iterator<V> getValueIterator() {
        return new ValueIterator();
    }

    @Override
    public boolean isEmpty() {
        return (numberOfEntries == 0)&&(firstNode == null);
    }

    @Override
    public int getSize() {
        return numberOfEntries;
    }

    @Override
    public void clear() {
        initializeDataFields();
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("[");
        Iterator<K> iterator = getKeyIterator();
        while (iterator.hasNext()) {
            K key = iterator.next();
            str.append("\n\t").append(key).append(": ").
                    append(getValue(key)).append(",");
        }
        str.append("\n]");
        return str.toString();
    }


    // Same as in SortedLinkedDictionary.
    // Since iterators implement Iterator, methods must be public.
    private class KeyIterator implements Iterator<K>, Serializable
    {
        private Node nextNode;

        private KeyIterator()
        {
            nextNode = firstNode;
        } // end default constructor

        public boolean hasNext()
        {
            return nextNode != null;
        } // end hasNext

        public K next()
        {
            K result;

            if (hasNext())
            {
                result = nextNode.getKey();
                nextNode = nextNode.getNextNode();
            }
            else
            {
                throw new NoSuchElementException();
            } // end if

            return result;
        } // end next

        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove
    } // end KeyIterator

    private class ValueIterator implements Iterator<V>, Serializable
    {
        private Node nextNode;

        private ValueIterator()
        {
            nextNode = firstNode;
        } // end default constructor

        public boolean hasNext()
        {
            return nextNode != null;
        } // end hasNext

        public V next()
        {
            V result;

            if (hasNext())
            {
                result = nextNode.getValue();
                nextNode = nextNode.getNextNode();
            }
            else
            {
                throw new NoSuchElementException();
            } // end if

            return result;
        } // end next

        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove
    } // end getValueIterator









    private class Node implements Serializable
    {
        private K key;
        private V value;
        private Node next;

        private Node(K searchKey, V dataValue)
        {
            key = searchKey;
            value = dataValue;
            next = null;
        } // end constructor

        private Node(K searchKey, V dataValue, Node nextNode)
        {
            key = searchKey;
            value = dataValue;
            next = nextNode;
        } // end constructor

        private K getKey()
        {
            return key;
        } // end getKey

        private V getValue()
        {
            return value;
        } // end getValue

        private void setValue(V newValue)
        {
            value = newValue;
        } // end setValue

        private Node getNextNode()
        {
            return next;
        } // end getNextNode

        private void setNextNode(Node nextNode)
        {
            next = nextNode;
        } // end setNextNode
    } // end Node

} // end LinkedDictionary

/*
Create a dictionary:

Initial dictionary should be empty; isEmpty() returns true


Testing add():

11 (should be 11) items in dictionary, as follows:

Dirk : 555-1234
Abel : 555-5678
Miguel : 555-9012
Tabatha : 555-3456
Tom : 555-5555
Sam : 555-7890
Reiss : 555-2345
Bette : 555-7891
Carole : 555-7892
Derek : 555-7893
Nancy : 555-7894



Testing getValue():


Abel:   555-5678 should be 555-5678

Sam:    555-7890 should be 555-7890

Tom:    555-5555 should be 555-5555

Reiss:  555-2345 should be 555-2345

Miguel: 555-9012 should be 555-9012



Testing contains():


Sam is in dictionary - OK

Abel is in dictionary - OK

Miguel is in dictionary - OK

Tom is in dictionary - OK

Bo is not in dictionary - OK



Removing first item Nancy - Nancy's number is 555-7894 should be 555-7894
Replacing phone number of Reiss and Miguel:

Reiss's old number 555-2345 is replaced by 555-5432
Miguel's old number 555-9012 is replaced by 555-2109

10 (should be 10) items in dictionary, as follows:

Dirk : 555-1234
Abel : 555-5678
Miguel : 555-2109
Tabatha : 555-3456
Tom : 555-5555
Sam : 555-7890
Reiss : 555-5432
Bette : 555-7891
Carole : 555-7892
Derek : 555-7893



Removing interior item Reiss:


9 (should be 9) items in dictionary, as follows:

Dirk : 555-1234
Abel : 555-5678
Miguel : 555-2109
Tabatha : 555-3456
Tom : 555-5555
Sam : 555-7890
Bette : 555-7891
Carole : 555-7892
Derek : 555-7893



Removing last item Dirk:


8 (should be 8) items in dictionary, as follows:

Abel : 555-5678
Miguel : 555-2109
Tabatha : 555-3456
Tom : 555-5555
Sam : 555-7890
Bette : 555-7891
Carole : 555-7892
Derek : 555-7893


Removing Bo (not in dictionary):

Bo was not found in the dictionary.


8 (should be 8) items in dictionary, as follows:

Abel : 555-5678
Miguel : 555-2109
Tabatha : 555-3456
Tom : 555-5555
Sam : 555-7890
Bette : 555-7891
Carole : 555-7892
Derek : 555-7893



Testing clear():

Dictionary should be empty; isEmpty() returns true


Done.

Process finished with exit code 0

 */