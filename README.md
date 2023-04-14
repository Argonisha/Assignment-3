# Assignment-3  
## Problem 1  
### Setup  
In this problem, I create a list of 1-500000 and shuffle them, then addAll() to a ConcurrentLinkedQueue. I couldn't find a better way to shuffle.  
### Running
Once the queue (present box) has been created, I spawn 4 threads that alternate between adding presents to the chain and removing the first present in the chain and updating accordingly. I had a function that would remove a specific node but wasn't sure if we were supposed to be randomizing which present is taken out, so opted to always take the head.  

The threads will add presents and send letters until there are no more presents left in the sorted chain. I tested and found that both the queue and the list were empty after.  

Runtime line 77 (prints a thank you to the console) is higher than normal (about 2800ms on average) vs the runtime when not printing anything to console (about 300ms average). This line is commented out but if needed can be uncommented.
### Reasoning
My ConcurrentLinkedList has a lock, and a simple linked list that finds the proper position when adding a new number.  

I added an isEmpty() function to check if the chain is empty.  

The removeFirst() function will remove the head and update pointers accordingly.

Each method that alters the list itself makes sure to lock and unlock properly.

## Problem 2
I ran out of time to implement this in code as problem 1 took much longer than I anticipated (still my bad) but regardless here is what I WOULD do.
### Idea
Create a class, maybe something like 'log' that consists of a temperature and a time it was taken.  
Create a list that keeps track of logs based on temperatures in ascending order.  
Create a list that keeps track of logs based on time in order of occurence (first occurred before last, last is most recent)  

Have 8 threads as producers that add to the concurrent list, making sure it is placed in the right position in both temperatures and time lists (The lists will have identical values in different orders).

You can easily get top 5 and bottom 5 temps. Just take first 5 indices and last 5 indices of the 'temperature' list.  
It would also be easy to determine the biggest 10 minute stretch with a largest temperature variance using the 'time' list.  

