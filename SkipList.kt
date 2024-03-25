package edu.ucdavis.cs.ecs036c

import kotlin.random.Random

/*
 * We are going to use a SkipList to implement a simplified Map, a key/value data
 * store.
 *
 * In our skiplist, keys need to be unique and need to support comparison operations,
 * but in return our iteration gives keys in sorted order.
 */

/*
 * SkipLists are a randomized data structure: They use a random function
 * in order to determine the "height" of each SkipListNode.
 */

fun toDo(): Nothing {
    throw Error("Need to implement")
}

val maxHeight = 20

class SkipList<K : Comparable<K>, V> {

    /*
     * Our internal nodes
     */
    class SkipListCell<K, V>(var key: K, var value: V){
        val pointers = makeNextArray()
        fun makeNextArray(): Array<SkipListCell<K, V>?> {
            var height = 1
            while (Random.nextBoolean() && height < maxHeight) {
                height++
            }

            return arrayOfNulls(height)
        }
    }


    class SkipListIterator<K, V>(var at: SkipListCell<K, V>?)
        : AbstractIterator<Pair<K, V>>() {
        override fun computeNext(): Unit {
            if (at == null) {
                done()
            } else {
                val keyVal = at!!.key to at!!.value
                at = at!!.pointers[0]
                setNext(keyVal)
            }
        }

    }

    operator fun iterator() = SkipListIterator(pointers[0])

    internal val pointers : Array<SkipListCell<K, V>?> = arrayOfNulls(maxHeight)
    internal var privateSize = 0
    val size : Int
        get() = privateSize

    /*
     * In order for toString to work you need your iterator to work
     */
    override fun toString(): String {
        return iterator().asSequence().joinToString(prefix = "[",
            postfix = "]", limit = 50) { it.toString() }
    }

    /*
     * If the key exists, it should override the existing
     * entry.  If the key does not exist, it should insert it
     */
    operator fun set( key: K,  value: V){
        /*
if data in self:
return
newnode = self.SkipListCell(data)
pointers = self.pointers
self.length += 1
# Remember, python ranges end just BEFORE the number.
for x in range(max_height-1, -1, -1):
    while pointers[x] != None and pointers[x].data < data:
        pointers = pointers[x].pointers
    if x < len(newnode.pointers):
        newnode.pointers[x] = pointers[x]
        pointers[x] = newnode
 */
        if (key in this) {
            getNode(key)?.value = value
        } else {
            val newnode = SkipListCell(key, value)
            var pointers = this.pointers
            for (x in maxHeight - 1 downTo 0) {
                while (pointers[x] != null && pointers[x]!!.key <= key) {
                    pointers = pointers[x]!!.pointers
                }

                if (x < newnode.pointers.size) {
                    newnode.pointers[x] = pointers[x]
                    pointers[x] = newnode

                }
                privateSize += 1
            }
        }
    }

    /*
     * An internal helper function, it returns the NODE
     * associated with a key.  It assumes that the
     * data IS in the skiplist.
     */
    fun getNode(key: K): SkipListCell<K, V> {
        var pointers = this.pointers
        for (x in maxHeight - 1 downTo 0) {
            while (pointers[x] != null && pointers[x]!!.key < key) {
                pointers = pointers[x]!!.pointers
//                if (pointers[0] != null && pointers[0]!!.key == key) {
//                    return pointers[0]
//                }
            }
        }
                return pointers[0] as SkipListCell<K, V>
        }
    /*
     * Returns the value or null if not present
     */
    operator fun get( key: K) : V?{
        if (key !in this) {
            return null
        }
        return getNode(key)?.value
    }

    /*
     * A check to see if the key is IN the skiplist.
     *
     * This is one of the first functions you are going to want
     * to implement.
     */
    operator fun contains( key: K) : Boolean{
        /*
        pointers = self.pointers
        for x in range(max_height-1, -1, -1):
            while pointers[x] != None and pointers[x].data < item:
                pointers = pointers[x].pointers
        if pointers[0] != None and pointers[0].data == item:
            return True
        return False
         */
        var pointers = this.pointers
        for (x in maxHeight - 1 downTo 0) {
            while (pointers[x] != null && pointers[x]!!.key < key) {
                pointers = pointers[x]!!.pointers
            }
        }
        if (pointers[0] != null && pointers[0]!!.key == key) {
            return true
        } else {
            return false
        }
    }

    /*
     * A nice debugging function to make sure allocations are right
     */
    fun heightCalculation(): Array<Int> {
        val ar:Array<Int?> = arrayOfNulls(maxHeight)
        for(x in 0..<maxHeight){
            ar[x] = 0
        }
        var at = pointers[0]
        @Suppress("UNCHECKED_CAST")
        ar as Array<Int>
        while(at != null){
            ar[at.pointers.size-1] += 1
            at = at.pointers[0]
        }
        return ar
    }
}

/*
 * And an initialization function that takes an array of pairs and puts
 * them all in the SkipList
 */
fun <K: Comparable<K>, V>skipListOf(vararg pairs: Pair<K, V>) : SkipList<K, V> {
    val ret = SkipList<K, V>()
    for (pair in pairs){
        ret[pair.first] = pair.second
    }
    return ret
}