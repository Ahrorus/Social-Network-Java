// CIS_22C - George Korolev
// CIS_22C - Ahror Abdulhamidov

public class LinkedQueue<T> implements QueueInterface<T> {

    public Node<T> firstNode, lastNode;

    public LinkedQueue(){
        firstNode = null;
        lastNode = null;
    }

    public LinkedQueue(T newEntry){
        firstNode = new Node<T>(newEntry);
        lastNode = firstNode;
    }

    @Override
    public void enqueue(T newEntry) {
        Node<T> newNode = new Node<T>(newEntry);

        if (isEmpty()){
            firstNode = newNode;
            lastNode = firstNode;
        }else {
            lastNode.behind = newNode;
            lastNode = newNode;
        }
    }

    @Override
    public T dequeue() {
        if (isEmpty()){
            throw new EmptyQueueException("Cannot perform requested function, Queue is empty.");
        }else{
            T dataToBeRemoved = firstNode.data;
            if (firstNode == lastNode){
                firstNode = null;
                lastNode = null;
            }else{
                firstNode = firstNode.behind;
            }
            return dataToBeRemoved;
        }
    }

    @Override
    public T getFront() {
        if (isEmpty()){
            throw new EmptyQueueException("Cannot perform requested function, Queue is empty.");
        }else
        return firstNode.data;
    }

    @Override
    public boolean isEmpty() {
        boolean isEmpty = false;
        if (firstNode == null && lastNode == null)
            isEmpty = true;
        return isEmpty;
    }

    @Override
    public void clear() {
        firstNode = null;
        lastNode = null;
    }

    private static class Node<T>{
        T data;
        Node<T> behind;

        public Node (T data){
            this.data = data;
        }

        public Node (T data, Node<T> link){
            this.data = data;
            this.behind = link;
        }
    }
}
/*
Create a queue:


isEmpty() returns true

Add to queue to get
Joe Jess Jim Jill Jane Jerry


isEmpty() returns false



Testing getFront and dequeue:

Joe is at the front of the queue.
Joe is removed from the front of the queue.

Jess is at the front of the queue.
Jess is removed from the front of the queue.

Jim is at the front of the queue.
Jim is removed from the front of the queue.

Jill is at the front of the queue.
Jill is removed from the front of the queue.

Jane is at the front of the queue.
Jane is removed from the front of the queue.

Jerry is at the front of the queue.
Jerry is removed from the front of the queue.


The queue should be empty: isEmpty() returns true


Add to queue to get
Joe Jess Jim


Testing clear:


isEmpty() returns true


Add to queue to get
Joe Jess Jim

Joe is at the front of the queue.
Joe is removed from the front of the queue.

Jess is at the front of the queue.
Jess is removed from the front of the queue.

Jim is at the front of the queue.
Jim is removed from the front of the queue.



The queue should be empty: isEmpty() returns true

The next calls will throw an exception.

Exception in thread "main" StackAndQueuePackage.EmptyQueueException: Cannot perform requested function, Queue is empty.
	at StackAndQueuePackage.LinkedQueue.getFront(StackAndQueuePackage.LinkedQueue.java:49)
	at Driver.testQueueOperations(Driver.java:76)
	at Driver.main(Driver.java:12)

Process finished with exit code 1
 */