package edu.davis.cs.ecs036c

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import java.security.SecureRandom
import kotlin.random.asKotlinRandom
import kotlin.test.assertFailsWith
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.time.measureTime


/**
 * Randomness is often really useful for testing,
 * and this makes sure we have a guaranteed GOOD
 * random number generator
 */
val secureRNG = SecureRandom().asKotlinRandom()


class LinkedListTest  () {

    /**
     * You will need to write MANY more tests, but this is just a simple
     * example: it creates a LinkedList<String> of 3 entries,
     * and then calls the toString() function.  Since toString needs
     * iterator to work this actually tests a remarkable amount of your code!
     */
    @Test
    fun testInit() {
        val testArray = arrayOf("A", "B", "C")
        // The * operator here expands an array into the arguments
        // for a variable-argument function
        val data = toLinkedList(*testArray)
        assert(data.toString() == "[A, B, C]")
    }

    /**
     * Similarly, we give you this test as well.  It will require that
     * you implement the toLinkedList operation, which requires
     * also implementing append.  It also requires implementing
     * get.
     */
    @Test
    fun testBasicInit() {
        val testArray = arrayOf(0, 1, 2, 3, 4, 5)
        testArray.shuffle(random = secureRNG)
        val testList = toLinkedList(*testArray)
        for (x in 0..<testArray.size) {
            assert(testArray[x] == testList[x])
        }
        assert(testArray.size == testList.size)
        assertFailsWith<IndexOutOfBoundsException>() {
            testList[-1]
        }
        assertFailsWith<IndexOutOfBoundsException>() {
            testList[testList.size]
        }
    }

    @Test
    fun testBasicNullableData() {
        val testList = LinkedList<Int?>()
        for (x in 0..<5) {
            testList.append(x)
        }
        for (x in 5..<10) {
            testList.append(null)
        }
        for (x in 0..<10) {
            if (x < 5) {
                assert(testList[x] == x)
            } else {
                assert(testList[x] == null)
            }
        }

    }

    // This test tries out the append function on an empty list
    @Test
    fun testAppendToEmptyList() {
        val list = LinkedList<Int>()
        list.append(0)
        assertEquals(1, list.size)
        assertEquals(0, list[0])

        val list2 = LinkedList<String>()
        list2.append("please work!!")
        assertEquals(1, list2.size)
        assertEquals("please work!!", list2[0])
    }

    // This test tries out append on a string linked list and an integer one
    @Test
    fun testAppendToList() {
        val list = toLinkedList("This", "is", "a", "test")
        list.append("case")
        assertEquals(5, list.size)
        assertEquals("This", list[0])
        assertEquals("is", list[1])
        assertEquals("a", list[2])
        assertEquals("test", list[3])
        assertEquals("case", list[4])

        val list2 = toLinkedList(0, 1, 2, 3, 4, 5)
        list2.append(6)
        assertEquals(7, list2.size)
        assertEquals(0, list2[0])
        assertEquals(1, list2[1])
        assertEquals(2, list2[2])
        assertEquals(3, list2[3])
        assertEquals(4, list2[4])
        assertEquals(5, list2[5])
        assertEquals(6, list2[6])
    }


    // This test tries to prepend with an empty list
    @Test
    fun testPrependToEmptyList() {
        val list = LinkedList<Int>()
        list.append(0)
        assertEquals(1, list.size)
        assertEquals(0, list[0])

        val list2 = LinkedList<String>()
        list2.append("please work!!")
        assertEquals(1, list2.size)
        assertEquals("please work!!", list2[0])
    }

    // This test prepends with a string and integer linked lists.
    @Test
    fun testPrependToList() {
        val list = toLinkedList("is", "a", "test", "case")
        list.prepend("This")
        assertEquals(5, list.size)
        assertEquals("This", list[0])
        assertEquals("is", list[1])
        assertEquals("a", list[2])
        assertEquals("test", list[3])
        assertEquals("case", list[4])


        val list2 = toLinkedList(1, 2, 3, 4, 5)
        list2.prepend(0)
        assertEquals(6, list2.size)
        assertEquals(0, list2[0])
        assertEquals(1, list2[1])
        assertEquals(2, list2[2])
        assertEquals(3, list2[3])
        assertEquals(4, list2[4])
        assertEquals(5, list2[5])
    }

    //This test tries getting from the beg, end, and middle
    @Test
    fun testGet() {
        val list = toLinkedList("This", "is", "a", "test")
        assertEquals("This", list[0])
        assertEquals("is", list[1])
        assertEquals("a", list[2])
        assertEquals("test", list[3])
        assertEquals("test", list.get(3))
        assertFailsWith<IndexOutOfBoundsException> {
            list.get(-2)
        }

        //This is getting from the middle
        val list2 = toLinkedList(0, 1, 2, 3, 4, 5)
        assertEquals(0, list2[0])
        assertEquals(1, list2[1])
        assertEquals(2, list2[2])
        assertEquals(3, list2[3])
        assertEquals(4, list2[4])
        assertEquals(5, list2[5])
        assertEquals(1, list2.get(1))

        // This is getting from beginning
        val list3 = toLinkedList(0, 1, 2, 3, 4, 5)
        assertEquals(0, list3[0])
        assertEquals(1, list3[1])
        assertEquals(2, list3[2])
        assertEquals(3, list3[3])
        assertEquals(4, list3[4])
        assertEquals(5, list3[5])
        assertEquals(0, list3.get(0))

        //Throws exception if invalid index
        assertFailsWith<IndexOutOfBoundsException> {
            list.get(-2)
        }


    }

    // This tests out set using string and integer linked lists
    @Test
    fun testSet() {
        val list = toLinkedList("This", "is", "a", "test")
        list.set(3, "string")
        assertEquals("This", list[0])
        assertEquals("is", list[1])
        assertEquals("a", list[2])
        assertEquals("string", list[3])
        assertFailsWith<IndexOutOfBoundsException> {
            list.set(-2, "fail")
        }

        // middle
        val list2 = toLinkedList(10, 20, 30, 40, 50)
        list2.set(2, 60)
        assertEquals(10, list2[0])
        assertEquals(20, list2[1])
        assertEquals(60, list2[2])
        assertEquals(40, list2[3])
        assertEquals(50, list2[4])

        assertFailsWith<IndexOutOfBoundsException> {
            list2.set(-2, 4)
        }
        // tests out beg
        val list3 = toLinkedList(10, 20, 30, 40, 50)
        list3.set(0, 0)
        assertEquals(0, list3[0])
        assertEquals(20, list3[1])
        assertEquals(30, list3[2])
        assertEquals(40, list3[3])
        assertEquals(50, list3[4])

    }

    // This tests out the insert at function
    @Test
    // add in middle
    fun testinsertAt() {
        val list = toLinkedList("This", "is", "test")
        list.insertAt(2, "a")
        assertEquals("This", list[0])
        assertEquals("is", list[1])
        assertEquals("a", list[2])
        assertEquals("test", list[3])

        assertFailsWith<IndexOutOfBoundsException> {
            list.insertAt(-2, "fail")
        }

        assertFailsWith<IndexOutOfBoundsException> {
            list.insertAt(6, "fail")
        }
    // add to beg
        val listaddtobeg = toLinkedList("is", "a", "test")
        listaddtobeg.insertAt(0, "this")
        assertEquals("this", listaddtobeg[0])
        assertEquals("is", listaddtobeg[1])
        assertEquals("a", listaddtobeg[2])
        assertEquals("test", listaddtobeg[3])
        assertEquals(4, listaddtobeg.size)

        // add to end
        val listend = toLinkedList("This", "is", "a")
        listend.insertAt(3, "test")
        assertEquals("This", listend[0])
        assertEquals("is", listend[1])
        assertEquals("a", listend[2])
        assertEquals("test", listend[3])
        assertEquals(4, listend.size)


    }

    // This tests out remove at
    @Test
    fun testRemoveAt() {

        // remove mid
        val list = toLinkedList("This", "is", "an", "a", "test")
        list.removeAt(2)
        assertEquals("This", list[0])
        assertEquals("is", list[1])
        assertEquals("a", list[2])
        assertEquals("test", list[3])

        assertFailsWith<IndexOutOfBoundsException> {
            list.removeAt(-2)
        }

        val list2 = LinkedList<Int>()
        assertFailsWith<IndexOutOfBoundsException> {
            list2.removeAt(0)
        }

        // end
        val listend = toLinkedList("This", "is", "a", "test")
        listend.removeAt(3)
        assertEquals("This", listend[0])
        assertEquals("is", list[1])
        assertEquals("a", list[2])


        // beg
        val listbeg = toLinkedList("Meow", "This", "is", "a", "test")
        listbeg.removeAt(0)
        assertEquals("This", listbeg[0])
        assertEquals("is", listbeg[1])
        assertEquals("a", listbeg[2])
        assertEquals("test", listbeg[3])


    }

    // index of test checking beg, end, and middle, along with returning -1
    @Test
    fun testIndexOf() {

        val list1 = toLinkedList("This", "is", "a", "test")
        val index1 = list1.indexOf("a")
        assertEquals(2, index1)

        val list2 = toLinkedList(10, 20, 30, 40, 50)
        val index2 = list2.indexOf(10)
        assertEquals(0, index2)

        val list3 = toLinkedList(10, 20, 30, 40, 50)
        val index3 = list3.indexOf(50)
        assertEquals(4, index3)

        val list4 = toLinkedList("This", "is", "a", "test")
        val index4 = list4.indexOf("shouldn't work")
        assertEquals(-1, index4)


    }

    // this tests out fold function by concatenating strings
    @Test
    fun testfold() {
        val list = toLinkedList("This", " ", "is", " ", "a", " ", "test")
        val concat = list.fold("") { concat, element -> concat + element }
        assertEquals("This is a test", concat)

    }


    // testing out map to list
    @Test
    fun testMap() {

        val list = LinkedList<Int>()
        list.append(10)
        list.append(20)
        list.append(30)

        val div = { x: Int -> x / 2 }
        val quotient = list.map(div)
        assertEquals(5, quotient[0])
        assertEquals(10, quotient[1])
        assertEquals(15, quotient[2])


    }

    // testing out map in place to existing list
    @Test
    fun testMapinPlace(){
        val list = toLinkedList(10, 20, 30, 40, 50)
        list.mapInPlace { it / 10 }
        assertEquals(toLinkedList(1, 2, 3, 4, 5).toString(), list.toString())

        val emptylist = LinkedList<Int>()
        emptylist.mapInPlace { it / 10}
        assertEquals(0, emptylist.size)


    }

    // testing out the filter and creating new linked list
    @Test
    fun testFilter() {
        val list = toLinkedList(10, 20, 31, 40, 50)
        val divby10 = list.filter { it % 5 == 0 }
        assertEquals(toLinkedList(10, 20, 40, 50) .toString(), divby10.toString())

        val emptylist = LinkedList<Int>()
        val emptyFilter = emptylist.filter { it.toString().startsWith("1") }
        assertEquals(0, emptyFilter.size)
    }

    // testing out the filter in place and creating new linked list
    @Test
    fun testFilterInPlace() {
        val list = toLinkedList(10, 21, 30, 41, 50)
        list.filterInPlace { it % 5 == 1 }
        assertEquals(toLinkedList(21, 41).toString(), list.toString())

    }
}






