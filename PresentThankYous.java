import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class PresentThankYous {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentLinkedList list = new ConcurrentLinkedList();
        Queue<Integer> queue = new ConcurrentLinkedQueue<>();
        Random random = new Random();
        List<Integer> listQueue = new ArrayList<>();
        for (int i = 1; i <= 500000; i++) {
            listQueue.add(i);
        }

        Collections.shuffle(listQueue);
        queue.addAll(listQueue);

        Thread thread1 = new Thread(new LinkedListThread(list, queue));
        Thread thread2 = new Thread(new LinkedListThread(list, queue));
        Thread thread3 = new Thread(new LinkedListThread(list, queue));
        Thread thread4 = new Thread(new LinkedListThread(list, queue));
        long startTimeMS = System.currentTimeMillis();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        long endTimeMS = System.currentTimeMillis();
        System.out.println(endTimeMS-startTimeMS);
        //System.out.println(queue.isEmpty());
        //System.out.println(list.isEmpty());
    }
}
class LinkedListThread implements Runnable {
    private ConcurrentLinkedList list;
    private Queue<Integer> queue;

    public LinkedListThread(ConcurrentLinkedList list, Queue<Integer> queue) {
        this.list = list;
        this.queue = queue;
    }

    @Override
    public void run() {
        Random random = new Random();
        boolean add = true;
        int exitFlag = 0;
        Integer value;
        while (exitFlag == 0) {
            
            int operation = add ? 0 : 1;
            switch (operation) {
                case 0:
                	value = queue.poll();
                    if (value == null) {
                        break;
                    }
                    list.add(value);
                    //System.out.println(Thread.currentThread().getName() + " added " + value);
                    break;
                case 1:
                    value = list.removeFirst();
                    if(value==null)
                    	exitFlag = 1;
                    //System.out.println(Thread.currentThread().getName() + " wrote a thank you to " + value);
                    break;
                case 2:
                    // Never executed, checks if 'value' is present in the chain already.
                	//boolean contains = list.contains(value);
                    //System.out.println(Thread.currentThread().getName() + " checked " + value + " (contains: " + contains + ")");
                    break;
            }
            add = !add;
            try {
                Thread.sleep(random.nextInt(1));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


}



class ConcurrentLinkedList {
    private Node head;
    private Lock lock;
    private Node tail;
    public ConcurrentLinkedList() {
        head = null;
        tail = null;
        lock = new ReentrantLock();
    }
    
    public boolean isEmpty() {
    	if(head == null)
    		return true;
    	else {
    		System.out.println(head.value);
    		return false;
    	}
    }

    public void add(int value) {
        Node newNode = new Node(value);
        lock.lock();
        try {
            if (head == null || value < head.value) {
                newNode.next = head;
                head = newNode;
            } else {
                Node current = head;
                while (current.next != null && value >= current.next.value) {
                    current = current.next;
                }
                newNode.next = current.next;
                current.next = newNode;
            }
        } finally {
            lock.unlock();
        }
    }
/*
    public void remove(int value) {
        lock.lock();
        try {
            if (head == null) {
                return;
            }
            if (head.value == value) {
                head = head.next;
                return;
            }
            Node current = head;
            while (current.next != null && current.next.value <= value) {
                if (current.next.value == value) {
                    current.next = current.next.next;
                    return;
                }
                current = current.next;
            }
        } finally {
            lock.unlock();
        }
    }
    */
    public Integer removeFirst() {
        lock.lock();
        try {
            if (head == null) {
                return null;
            }
            Integer value = head.value;
            head = head.next;
            if (head == null) {
                tail = null;
            }
            return value;
        } finally {
            lock.unlock();
        }
    }
    
    /*
    public Integer removeFirst() {
        lock.lock();
        try {
            if (head == null || head.next==null) {
                return null;
            }
            Integer value = head.value;
            head.next = head.next.next;
            head = head.next;
            return value;
        } finally {
            lock.unlock();
        }
    }
    */

    public boolean contains(int value) {
        lock.lock();
        try {
            Node current = head;
            while (current != null && current.value <= value) {
                if (current.value == value) {
                    return true;
                }
                current = current.next;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private class Node {
        int value;
        Node next;

        public Node(int value) {
            this.value = value;
            this.next = null;
        }
    }
}


