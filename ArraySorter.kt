package edu.ucdavis.cs.ecs036c

// import jdk.vm.ci.code.Register.None
import kotlin.random.Random

/*
 * This is declaring an "Extension Function", basically we are
 * creating a NEW method for Array<T> items.  this will
 * refer to the Array<T> it is called on.
 *
 * We allow an optional comparison function, and this does NOT NEED TO BE
 * a stable sort.
 */
fun <T: Comparable<T>> Array<T>.selectionSort(reverse: Boolean = false) : Array<T>{
    if(reverse){
        return this.selectionSortWith(object: Comparator<T> {
            override fun compare(a: T, b: T): Int {
                return b.compareTo(a)
            }})
    }
    return this.selectionSortWith(object: Comparator<T> {
        override fun compare(a: T, b: T): Int {
            return a.compareTo(b)
        }})
}

fun <T: Comparable<T>> Array<T>.selectionSortWith(comp: Comparator<in T>) : Array<T> {
    for(i in 0..<size){
        var min = this[i]
        var minAt = i
        for(j in i..<size){
            val compare = comp.compare(this[j], min)
            if(compare < 0){
                min = this[j]
                minAt = j
            }
        }
        this[minAt] = this[i]
        this[i] = min
    }
    return this
}

fun <T: Comparable<T>> Array<T>.quickSort(reverse: Boolean = false) : Array<T>{
    if(reverse){
        return this.quickSortWith(object: Comparator<T> {
            override fun compare(a: T, b: T): Int {
                return b.compareTo(a)
            }})
    }
    return this.quickSortWith(object: Comparator<T> {
        override fun compare(a: T, b: T): Int {
            return a.compareTo(b)
        }})
}


/*
 * Here is the QuickSort function you need to implement
 */
fun <T> Array<T>.quickSortWith( comp: Comparator<in T>) : Array<T> {


    /*
     data = None
  def swap(a, b):
      tmp = ar[a]
      ar[a] = ar[b]
      ar[b] = tmp
      if data != None:
          tmp = data[a]
          data[a] = data[b]
          data[b] = tmp

  */
    fun swap(a: Int, b: Int){
        val tmp = this[a]
        this[a] = this[b]
        this[b] = tmp
    }

        /*
        # sorting a 1 or 0 element region
        if end <= start:
        return
        */
    fun quickSortInternal(start: Int, end: Int) {
        if (end <= start) {
            return
        }
    /*
     # For us, we will use pivot = last one:
      pivot_point = end
      swap_point = start
      examine_point = start
      # Swap a random element into the pivot position
      swap(random.randrange(start, end+1), pivot_point)
     */
        val pivot_point = end
        var swap_point = start
        val rand = Random.nextInt(start, end + 1)
        swap(rand, pivot_point)

        var examine_point = start

        while (examine_point < pivot_point) {
            val compare = comp.compare(this[examine_point], this[pivot_point])
            if (compare < 0 || (compare == 0 && (0..1).random() == 0)) {
                swap(swap_point, examine_point)
                swap_point +=1
            }
            examine_point+= 1
        }

        /*
        swap(swap_point, pivot_point)
        quicksort_internal(start, swap_point - 1)
        quicksort_internal(swap_point + 1, end)
        quicksort_internal(0, len(ar)-1)
         */
        swap(swap_point, pivot_point)


        quickSortInternal(start, swap_point - 1)
        quickSortInternal(swap_point + 1, end)
    }


    quickSortInternal(0, size - 1)
    return this
}





    /*
      while examine_point < pivot_point:
        if ((not reverse and ar[examine_point] < ar[pivot_point]) or
           (reverse and ar[pivot_point] < ar[examine_point]) or
             (ar[pivot_point] == ar[examine_point] and random.randrange(2) == 0)):
              swap(swap_point, examine_point)
                swap_point += 1
                examine_point += 1
            else:
                examine_point += 1

     */




