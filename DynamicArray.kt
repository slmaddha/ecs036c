package edu.ucdavis.cs.ecs036c

/**
 * Welcome to the second normal data structure homework assignment for ECS 032C
 *
 * In this you will be implementing many functions for the
 * DynamicArray class.  It is strongly advised that you start
 * by implementing append (which toDynamicArray uses), get, resize, and the
 * iterator, as those 3 function are required for all the test code.
 *
 * Initial tests will use an array that IS large enough to trigger resize.
 *
 * You will also need to write a lot of tests, as we provide only the
 * most basic tests and the autograder deliberately hides the test results
 * until after the grades are released.
 *
 * However, you should be able to use your tests from Homework1 with just
 * some simple refactoring: changing every reference from LinkedList to
 * DynamicArray (and the corresponding change in the creation function).
 */

fun toDo(): Nothing {
    throw Error("Need to implement")
}


/**
 * This is the basic DynamicArray class.  This implements
 * effectively an ArrayList style implementation: We can
 * append to the front and end in (amortized) constant time,
 * thus it can be used as a stack, a queue, or a double-ended
 * queue.
 */
class DynamicArray<T> {
    /*
     * We have 3 internal private variables: the backing array that
     * stores the data, the internal size of the data, and where
     * in the array/ring the first element exists.  You must NOT
     * add any other private variables, rename these,
     * or change the types of the variables.
     *
     * We use the "reflection" interface in the autograder to access these fields
     * and to perform tests to make sure you don't add other fields.
     */

    /*
     * This is a case where Kotlin's type system interacts annoyingly with
     * Java.  We KNOW it is an array of type T?, but Kotlin requires
     * arrays themselves not to use type parameterization (for awkard reasons),
     * but any nullable T? is a subtype of Any? so this is safe, so we
     * add an annotation saying "yeah, just ignore this"
     */
    @Suppress("UNCHECKED_CAST")
    internal var storage: Array<T?> = arrayOfNulls<Any?>(4) as Array<T?>
    internal var privateSize = 0
    internal var start: Int = 0

    /**
     * This allows .size to be accessed but not set, with the private
     * variable to track the actual size elsewhere.
     */
    val size: Int
        get() = privateSize

    /**
     * You need to implement this basic iterator for the DynamicArray.  you
     * should probably take advantage of get and size and just have
     * a variable in the class to keep track of where you are in the array
     */
    class DynamicArrayIterator<T>(var array: DynamicArray<T>) : AbstractIterator<T>() {
        /*
        Python pseudo:
        def __iter__(self):
            userindex = 0
            while userindex < self.privateSize:
            yield self[userindex]
            userindex += 1

         */

        var index = 0
        // setNext if index is less than array.size and increment index, once done --> call done
        override fun computeNext(): Unit {
            if (index < array.size) {
                setNext(array[index])
                index++
            } else {
                done()
            }
        }
    }


    /**
     * This needs to return an Iterator for the data in the cells.  This allows
     * the "for (x in aLinkedList) {...} to work as expected.
     */
    operator fun iterator() = DynamicArrayIterator(this)

    /**
     * And this is a useful one: indices, which is a range that
     * automatically covers the size
     */
    val indices: IntProgression
        get() = 0..<size

    /*
     * This function dynamically resizes the array by doubling its size and copying all
     * the elements over.  Although this is an O(N) operation, it gets amortized out over
     * the next N additions which means in the end the array's operations prove to be
     * constant time.
     */
    fun resize() {
        // usedSize == totalSize, we need to resize()
        @Suppress("UNCHECKED_CAST")
        // create array of nulls to resizde (multiply by 2)
        val newStorage: Array<T?> = arrayOfNulls<Any?>(storage.size * 2) as Array<T?>

        // keep iterating until privateSize is reached
        for (i in 0 until privateSize) {
            newStorage[i] = storage[(start + i) % storage.size]   // ring buffer
        }

        start = 0 // reset the value of start
        storage = newStorage
    }

    /**
     * Append ads an item to the end of the array.  It should be
     * a constant-time (O(1)) function.  The only exception is if
     * the storage is full, in which case you call resize() and
     * then append the item.
     */
    fun append(item: T) {
        /*
        Append pseudo:
        ○ Do resize() ...
        ○ Find the location in storage: (start + usedSize) %
        totalSize
        ○ usedSize+1
         */
        // if the array is full, call resize

        if (size + 1 > storage.size) {
            resize()
        }
        // circular buffer, add at end of list
        val at = (start + size) % storage.size
        storage[at] = item
        // increment size of list
        privateSize++
    }


    /**
     * Adds an item to the START of the list.  It should be
     * a constant-time (O(1)) function as well.
     *
     * One important note, % in kotlin/java is not like % in Python.
     * In python -1 % x is x-1.  In kotlin it is -1.  So if you want
     * to decrement and mod you will want to do (-1 + x) % x.
     */
    fun prepend(item: T) {
        /*
        Prepend pseudo
        ○ Do resize() ...
        ○ Remember to update usedSize
        ○ and start: use (start - 1 + totalSize) % totalSize
         */
        // call resize if the array is full
        if (size + 1 > storage.size) {
            resize()
        }
        // increment privateSize
        privateSize++
        val at = (start - 1 + storage.size) % storage.size // circular buffer
        // add to beginning of list, start is now equal to new element
        start = at
        storage[at] = item

    }


    /**
     * Get the data at the specified index.  Because the storage
     * is an array it is constant time no matter the index.
     *
     * One note on typing: the storage array is typed as <T?>,
     * that is, T or null.  You want to return something of type T
     * however, so for the return statement, do a cast by going
     * return {whatever} as T.
     *
     * We need to do the cast rather than the !! operator because
     * we could have a DynamicArray<Int?> or similar that could
     * include nullable entries.
     *
     * You can suppress the compiler warning this generates with
     * a @Suppress("UNCHECKED_CAST") annotation before the return
     * statement.
     *
     * Invalid indices should throw an IndexOutOfBoundsException
     */
    operator fun get(index: Int): T {
        // invalid indices bc negative
        if (index < 0) {
            throw IndexOutOfBoundsException("Negative index")
        }
        // invalid index throws exception
        if (index >= size) {
            throw IndexOutOfBoundsException()
        }

        // circular buffer
        val at = (index + start) % storage.size

        @Suppress("UNCHECKED_CAST")
        return storage[at] as T  // have to return as T because of the :T (null)
    }

    /**
     * Replace the data at the specified index.  Again, this is an
     * constant time operation.
     *
     * Invalid indexes should throw an IndexOutOfBoundsException
     */
    operator fun set(index: Int, data: T): Unit {
        // invalid indices bc negative
        if (index < 0) {
            throw IndexOutOfBoundsException("Negative index")
        }
        // invalid indices bc out of range
        if (index >= size) {
            throw IndexOutOfBoundsException()
        }
        // circular buffer
        val at = (index + start) % storage.size
        // set data equal to the at var in arraylist
        storage[at] = data
    }


    /**
     * This inserts the element at the index.
     *
     * If the index isn't valid, throw an IndexOutOfBounds exception
     *
     * This should be O(1) for the start and the end, O(n) for all other cases.
     */
    fun insertAt(index: Int, value: T) {
        /*
        Pseudo from discussion:
        ● If index == 0, prepend
        ● If index == usedSize, append
        ● Insert in the middle: InsertAt(3, ‘Z’)
        ● Do a prepend/append first
        */

        // throw exception if index is out of bounds
        if (index < 0 || index > size) {
            throw IndexOutOfBoundsException()
        }

        // if privateSize is equal to storage size, call resize
        if (privateSize == storage.size) {
            resize()
        }
        // if index is equal to zero, call prepend for the value
        if (index == 0) {
            prepend(value)
        // if index is equal to privatesize (last index), call append for the value
        } else if (index == privateSize) {
            append(value)
        } else {
            val insertAt = (start + index) % storage.size
            val moveNumber = privateSize - index
            System.arraycopy(storage, insertAt, storage, insertAt + 1, moveNumber)

            // circular buffer to insert
            storage[(start + index) % storage.size] = value
            // increment the size of the list
            privateSize++
        }
    }


    /**
     * This removes the element at the index and return the data that was there.
     *
     * Again, if the data doesn't exist it should throw an
     * IndexOutOfBoundsException.
     *
     * This should be O(1) for the first or last element, O(N) otherwise
     */

    fun removeAt(index: Int):  T  {

        /*
        Removeat pseudo
        ● If index == 0: decrease usedSize and update start !
        ● If index == usedSize - 1, decrease usedSize
        ● remove in the middle ?: removeAt(3)
        ● Opposite assign operation as in InsertAt(3, ‘Z’)

        for i in range(index, self.size - 1):
        self[i] = self[i + 1]
        self.remove_at(self.size - 1)

         */

        // throw exception if index is out of bounds
        if (index < 0 || index >= size) {
            throw IndexOutOfBoundsException()
        }

        // cast as T?
        val removedItem: T? = storage[(start + index) % storage.size]

        // if index 0, decrement and remove 1st element
        if (index == 0) {
            privateSize -= 1
            start = (start + 1) % storage.size
        // if index is equal to privateSize, decrement
        } else if (index == privateSize - 1) {
            privateSize -= 1
        } else {
            privateSize -= 1
            // as long as i is less than private size, shift all the indexes accordingly
            var i = index
            while (i < privateSize) {
                val currInd = (start + i) % storage.size
                val nextInd = (start + i + 1) % storage.size
                storage[currInd] = storage[nextInd]
                i++
            }

        }
            // return as ? and throw exception if not
        return removedItem ?: throw IllegalStateException()
    }





    /*
     * Functions to make this a full double ended queue, they call the appropriate
     * functions
     */
    fun push(item: T) = append(item)
    fun pushLeft(item: T) = prepend(item)
    fun pop() = removeAt(size - 1)
    fun popLeft() = removeAt(0)


    /**
     * This does a linear search for the item to see
     * what index it is at, or -1 if it isn't in the list
     */
    fun indexOf(item: T): Int {
        var i = 0
        while (i < privateSize) {
            val at = (start + i) % storage.size
            if (storage[at] == item) {
                return i
            }
            i++
        }
        return -1
    }


    operator fun contains(item:T) = indexOf(item) != -1


    /**
     * A very useful function for debugging, as it will print out
     * the list in a convenient form.
     */
    override fun toString(): String {
        return iterator()
            .asSequence()
            .joinToString(prefix="[", postfix = "]", limit = 50)
    }

    /**
     * Fold
     */
    fun <R>fold(initial: R, operation: (R, T) -> R): R {
        var count = initial

        var i = 0
        while (i < privateSize) {
            val at = (start + i) % storage.size
            count = operation(count, storage[at]!!)
            i++
        }
        return count
    }

    /**
     * And you need to implement map, creating a NEW DynamicArray
     * and applying the function to each element in the old list.
     *
     * One useful note, because append is constant time, you
     * can just go in order and make a new dynamic array.
     */
    fun <R>map(operator: (T)->R): DynamicArray<R>{
        val res = DynamicArray<R>()

        var i = 0
        while (i < privateSize) {
            val at = (start + i) % storage.size
            res.append(operator(storage[at]!!))
            i++
        }
        return res // return new arraylist with mapped out elements
    }


    /**
     * Finally we have mapInPlace.  mapInPlace is like Map with a difference:
     * instead of creating a new array it applies the function to each data
     * element and uses that to replace the element in the existing array, returning
     * the array itself when done.
     */
    fun mapInPlace(operator: (T) -> T): DynamicArray<T> {
        var i = 0
        while (i < privateSize) {
            val at = (start + i) % storage.size
            storage[at] = operator(storage[at]!!)
            i++
        }
        return this // return same list with mapped elements
    }

    /**
     * Likewise, filter returns a new DynamicArray.
     */
    fun filter(operator: (T) -> Boolean): DynamicArray<T> {
        val res = DynamicArray<T>()

        var i = 0
        while (i < privateSize) {
            val at = (start + i) % storage.size
            val element = storage[at]!! // not-null assertion operator, variables cannot be null

            if (operator(element)) {
                res.append(element)
            }

            i++
        }


        return res // return new dynamic array with filtered elements
    }


    /**
     * And filterInPlace.  filterInPlace will keep only the elements
     * that are true.  Critically this should be LINEAR in the size
     * of the array.
     */
    fun filterInPlace(operator: (T)->Boolean) : DynamicArray<T>{

        var readInd = 0
        var writeInd = 0

        while (readInd < privateSize) {
            val at = (start + readInd) % storage.size
            val element = storage[at]!! // not-null assertion operator, variables cannot be null

            if (operator(element)) {
                storage[writeInd] = element
                writeInd++
            }

            readInd++
        }

        privateSize = writeInd
        start = 0

        return this // return same list with new filtered elements

    }
}
/**
 * And this function builds a new LinkedList of the given type with
 * a vararg (variable argument) set of inputs.
 */
fun <T> toDynamicArray(vararg input:T) : DynamicArray<T> {
    val retval = DynamicArray<T>()
    for (item in input){
        retval.append(item)
    }
    return retval
}


/*
Sources used:
Kotlin array list: https://www.geeksforgeeks.org/kotlin-list-arraylist/
Kotlin array list class: https://www.javatpoint.com/kotlin-arraylist
Arraylist: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-array-list/
Clone a list in kotlin: https://stackoverflow.com/questions/46846025/how-to-clone-or-copy-a-list-in-kotlin
How to use copyOf: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/copy-of.html
O(n) in memory: https://stackoverflow.com/questions/8228758/what-is-the-meaning-of-o1-on-onn-memory
Big O Notation: https://en.wikipedia.org/wiki/Big_O_notation
Circular buffers: https://gist.github.com/ToxicBakery/05d3d98256aaae50bfbde04ae0c62dbd
Implementing ring array: https://stackoverflow.com/questions/74050952/how-to-implement-a-ring-array-or-stack-in-kotlin
Queues in kotlin: https://www.kodeco.com/books/data-structures-algorithms-in-kotlin/v1.0/chapters/5-queues
Buffer in Java: https://www.baeldung.com/java-ring-buffer
 */