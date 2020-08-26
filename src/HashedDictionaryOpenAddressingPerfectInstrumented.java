// CIS_22C - George Korolev
// CIS_22C - Ahror Abdulhamidov

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class HashedDictionaryOpenAddressingPerfectInstrumented<K,V> implements DictionaryInterface<K,V>, Serializable {

    // The dictionary:
    private int numberOfEntries;
    private static final int DEFAULT_CAPACITY = 5; // Must be prime
    private static final int MAX_CAPACITY = 10000;

    // The hash table:
    private TableEntry<K, V>[] hashTable; // Dictionary entries
    private int tableSize;                          // Must be prime
    private static final int MAX_SIZE = 2 * MAX_CAPACITY;
    private boolean initialized = false;

    private static int totalProbes = 0;

    public static void resetTotalProbes(){
        totalProbes = 0;
    }

    public static int getTotalProbes(){
        return totalProbes;
    }

    // fraction of hash table that can be filled
    // In the original code this was static final, but we want to be able
    // to change it for our timings.
    private double MAX_LOAD_FACTOR = 0.5;

    public HashedDictionaryOpenAddressingPerfectInstrumented()
    {
        this(DEFAULT_CAPACITY); // Call next constructor
    } // end default constructor


    public HashedDictionaryOpenAddressingPerfectInstrumented(int initialCapacity)
    {
        checkCapacity(initialCapacity);
        numberOfEntries = 0;

        // Set up hash table:
        // Initial size of hash table is same as initialCapacity if it is prime
        // otherwise increase it until it is prime size
        tableSize = getNextPrime(initialCapacity);
        checkSize(tableSize);  // Check for max array size

        // The case is safe because the new array contains null entries
        @SuppressWarnings("unchecked")
        TableEntry<K, V>[] temp = (TableEntry<K, V>[]) new TableEntry[tableSize];
        hashTable = temp;
        initialized = true;
    } // end constructor


    private Random getHashGenerator(Object key){
        int val = key.toString().hashCode();
        val = Math.abs(val);
        return new Random(val);
    }


    /** Throws an exception if this object is not initialized.
     *
     */
    private void checkInitialization()
    {
        if (!initialized)
            throw new SecurityException("ArrayBag object is not initialized " +
                    "properly.");
    }

    /** Throws an exception if the desired capacity is too large.
     *
     */
    private void checkCapacity(int desiredCapacity)
    {
        if (desiredCapacity > MAX_CAPACITY)
            throw new IllegalStateException("Attempt to create a hash table " +
                    "whose capacity exceeds " +
                    "allowed maximum.");
    }

    /** Throws an exception if the desired array size is too large.
     *
     */
    private void checkSize(int desiredSize)
    {
        if (desiredSize > MAX_SIZE)
            throw new IllegalStateException("Attempt to create a hash table " +
                    "array whose size exceeds " +
                    "allowed maximum.");
    }

    // New method to change the load factor.
    public void setMaxLoadFactor(double loadFactor)
    {
        MAX_LOAD_FACTOR = loadFactor;
    } // end setMaxLoadFactor



    private int getNextPrime(int t)
    {
        t = getNextOdd(t);      // get the next odd

        while(!isOddPrime(t))
        {
            t+= 2;              // try odds until a prime is found
        }

        return t;
    } // end getNextPrime

    private int getNextOdd(int t)
    {
        if(t%2 == 0)
            return t+1;
        else
            return t;
    } // end getNextPrime    

    // Precondition: t is an odd value
    private boolean isOddPrime(int t)  // Not the most efficient method, but it will serve
    {
        int test = 3;
        boolean foundFactor = false;
        while(test*test < t && !foundFactor)
        {
            foundFactor = (t%test == 0);    // is it divisible by test
            test += 2;
        }

        return !foundFactor;
    } // end getNextPrime    


    private int getHashIndex(K key){
        int val = key.toString().hashCode();
        val = Math.abs(val);
        val = val % hashTable.length;
        return val;
    } // end getHashIndex


    @Override
    public V getValue(K key)
    {
        checkInitialization();

        V result = null;
        Random generator = getHashGenerator(key);
        int index = locate(generator, key);


        if (index != -1)
            result = hashTable[index].getValue(); // Key found; get value
        // Else key not found; return null

        return result;
    } // end getValue


    @Override
    public V remove(K key)
    {
        checkInitialization();

        V removedValue = null;
        Random generator = getHashGenerator(key);
        int index = locate(generator, key);


        if (index != -1)
        { // Key found; flag entry as removed and return its value
            removedValue = hashTable[index].getValue();
            hashTable[index].setToRemoved();
            numberOfEntries--;
        } // end if

        // else key not found; return null
        return removedValue;
    } // end remove

    // Precondition: checkInitialization has been called.
    private int locate(Random generator, K key)
    {
        boolean found = false;

        int index = generator.nextInt(hashTable.length);

        while ( !found && (hashTable[index] != null) )
        {
            if ( hashTable[index].isIn() &&
                    key.equals(hashTable[index].getKey()) )
                found = true; // key found
            else // follow probe sequence
                index = generator.nextInt(hashTable.length); // Linear probing
            totalProbes++;
        } // end while

        // Assertion: Either key or  null is found at hashTable[index]
        int result = -1;

        if (found)
            result = index;

        return result;
    } // end locate

    private boolean isHashTableTooFull()
    {
        return numberOfEntries > MAX_LOAD_FACTOR*hashTable.length;
    }

    @Override
    public V add(K key, V value) {
        checkInitialization();

        if ((key == null) || (value == null)) {
            throw new IllegalArgumentException();
        } else {
            V oldValue; // Value to return
            Random generator = getHashGenerator(key);
            int index = probe(generator, key);


            // Assertion: index is within legal range for hashTable
            assert (index >= 0) && (index < hashTable.length);

            if ((hashTable[index] == null) || hashTable[index].isRemoved()) { // Key not found, so insert new entry
                hashTable[index] = new TableEntry(key, value);
                numberOfEntries++;
                oldValue = null;
            } else { // Key found; get old value for return and then replace it
                oldValue = hashTable[index].getValue();
                hashTable[index].setValue(value);
            } // end if

            // Ensure that hash table is large enough for another add
            if (isHashTableTooFull()) {
                enlargeHashTable();
            }

            return oldValue;
        } // end if
    } // end add

    // Precondition: checkInitialization has been called.
    private int probe(Random generator, K key) {
        boolean found = false;

        int index = generator.nextInt(hashTable.length);

        int removedStateIndex = -1; // Index of first location in
        // removed state

        while (!found && (hashTable[index] != null)) {
            if (hashTable[index].isIn()) {
                if (key.equals(hashTable[index].getKey())) {
                    found = true; // Key found
                } else // Follow probe sequence
                {
                    index = generator.nextInt(hashTable.length); // Linear probing
                }
            } else // Skip entries that were removed
            {
                // Save index of first location in removed state
                if (removedStateIndex == -1) {
                    removedStateIndex = index;
                }
                index = generator.nextInt(hashTable.length); // Linear probing
            } // end if
            totalProbes++;
        } // end while


        // Assertion: Either key or null is found at hashTable[index]
        if (found || (removedStateIndex == -1) )
            return index; // Index of either key or null
        else
            return removedStateIndex; // Index of an available location
    } // end probe





    // Precondition: checkInitialization has been called.
    private void enlargeHashTable()
    {
        TableEntry<K,V>[] oldTable = hashTable;
        int oldSize = hashTable.length;
        int newSize = getNextPrime(oldSize + oldSize);

        // The case is safe because the new array contains null entries
        @SuppressWarnings("unchecked")
        TableEntry<K, V>[] temp = (TableEntry<K, V>[]) new TableEntry[newSize];
        hashTable = temp;
        numberOfEntries = 0; // Reset size of dictionary, since it will be
        // incremented by add during rehash

        // Rehash dictionary entries from old array to the new and bigger
        // array; skip both null locations and removed entries
        for (int index = 0; index < oldSize; index++)
        {
            if ( (oldTable[index] != null) && oldTable[index].isIn() )
                add(oldTable[index].getKey(), oldTable[index].getValue());
        } // end for
    } // end enlargeHashTable







    @Override
    public boolean contains(K key)
    {
        boolean result = false;

        Random generator = getHashGenerator(key);
        int index = locate(generator, key);

        if (index != -1)
            result = true; // key found; return true
        // else key not found; return false       
        return result;
    } // end contains

    @Override
    public Iterator<K> getKeyIterator()
    {
        return new KeyIterator();
    } // end getKeyIterator

    @Override
    public Iterator<V> getValueIterator()
    {
        return new ValueIterator();
    } // end getValueIterator

    @Override
    public boolean isEmpty()
    {
        return numberOfEntries == 0;
    } // end isEmpty


    @Override
    public int getSize()
    {
        return numberOfEntries;
    } // end getSize

    @Override
    public void clear()
    {
        hashTable =  new TableEntry[hashTable.length];   // use the old table size
        numberOfEntries = 0;
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

    private class TableEntry<S,T> implements Serializable
    {
        private S key;
        private T value;
        private boolean inTable; // true if entry is in hash table

        private TableEntry(S searchKey, T dataValue)
        {
            key = searchKey;
            value = dataValue;
            inTable = true;
        } // end constructor

        private T getValue(){
            return value;
        } // end getValue

        private void setValue(T x){
            value =x;
        } // end setValue

        private S getKey(){
            return key;
        } // end getKey

        private void setKey(S k){
            key = k;
        } // end setKey

        private void setToRemoved(){
            inTable = false;
        } // end setToRemoved

        private boolean isIn(){
            return inTable;
        } // end isIn

        private boolean isRemoved(){
            return !inTable;
        } // end isRemoved
    } // end TableEntry




    private class KeyIterator implements Iterator<K>, Serializable
    {
        private int currentIndex; // Current position in hash table
        private int numberLeft; // Number of entries left in iteration

        private KeyIterator()
        {
            currentIndex = 0;
            numberLeft = numberOfEntries;
        } // end default constructor

        @Override
        public boolean hasNext()
        {
            return numberLeft > 0;
        } // end hasNext

        @Override
        public K next()
        {
            K result = null;
            if (hasNext())
            {
                // Skip table locations that do not contain a current entry
                while ( (hashTable[currentIndex] == null) ||
                        hashTable[currentIndex].isRemoved() )
                {
                    currentIndex++;
                } // end while

                result = hashTable[currentIndex].getKey();
                numberLeft--;
                currentIndex++;
            }
            else
                throw new NoSuchElementException();

            return result;
        } // end next

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove

    } // end KeyIterator


    private class ValueIterator implements Iterator<V>, Serializable
    {
        private int currentIndex; // current position in hash table
        private int numberLeft; // number of entries left in iteration

        private ValueIterator()
        {
            currentIndex = 0;
            numberLeft = numberOfEntries;
        } // end default constructor

        @Override
        public boolean hasNext()
        {
            return numberLeft > 0;
        } // end hasNext

        @Override
        public V next()
        {
            V result = null;
            if (hasNext())
            {
                // Skip table locations that do not contain a current entry
                while ( (hashTable[currentIndex] == null) ||
                        hashTable[currentIndex].isRemoved() )
                {
                    currentIndex++;
                } // end while

                result = hashTable[currentIndex].getValue();
                numberLeft--;
                currentIndex++;
            }
            else
                throw new NoSuchElementException();

            return result;
        } // end next

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove

    } // end ValueIterator


}
/*
Linear hashing
    Total probes for inserting data values: 8457859
       Average probes made: 8.457859
    Total probes for successful searches: 1700261
       Average probes made: 1.700261
    Total probes for failure searches: 3422475
       Average probes made: 3.422475
Double hashing
    Total probes for inserting data values: 5131332
       Average probes made: 5.131332
    Total probes for successful searches: 1487005
       Average probes made: 1.487005
    Total probes for failure searches: 2358893
       Average probes made: 2.358893
Perfect hashing
    Total probes for inserting data values: 2728294
       Average probes made: 2.728294
    Total probes for successful searches: 1492038
       Average probes made: 1.492038
    Total probes for failure searches: 1370880
       Average probes made: 1.37088

Process finished with exit code 0

 */