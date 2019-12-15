/*
 * Copyright (c) 1997, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package java.util;

import java.util.function.Consumer;

/**
 * This class provides a skeletal implementation of the {@link List}
 * interface to minimize the effort required to implement this interface
 * backed by a "random access" data store (such as an array).  For sequential
 * access data (such as a linked list), {@link AbstractSequentialList} should
 * be used in preference to this class.
 *
 * <p>To implement an unmodifiable list, the programmer needs only to extend
 * this class and provide implementations for the {@link #get(int)} and
 * {@link List#size() size()} methods.
 *
 * <p>To implement a modifiable list, the programmer must additionally
 * override the {@link #set(int, Object) set(int, E)} method (which otherwise
 * throws an {@code UnsupportedOperationException}).  If the list is
 * variable-size the programmer must additionally override the
 * {@link #add(int, Object) add(int, E)} and {@link #remove(int)} methods.
 *
 * <p>The programmer should generally provide a void (no argument) and collection
 * constructor, as per the recommendation in the {@link Collection} interface
 * specification.
 *
 * <p>Unlike the other abstract collection implementations, the programmer does
 * <i>not</i> have to provide an iterator implementation; the iterator and
 * list iterator are implemented by this class, on top of the "random access"
 * methods:
 * {@link #get(int)},
 * {@link #set(int, Object) set(int, E)},
 * {@link #add(int, Object) add(int, E)} and
 * {@link #remove(int)}.
 *
 * <p>The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if the
 * collection being implemented admits a more efficient implementation.
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/java.base/java/util/package-summary.html#CollectionsFramework">
 * Java Collections Framework</a>.
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @since 1.2
 */

// AbstractList继承自AbstractCollection并实现了List接口，实现了List接口中大部分函数，
// 从而方便其他类实现List接口。AbstractList的源码在结构上分为了两种内部迭代器，两种内部类以及AbstractList本身的代码
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {
    /**
     * Sole constructor.  (For invocation by subclass constructors, typically
     * implicit.)
     */
    //只提供了一个protected修饰的无参构造器，供子类使用
    protected AbstractList() {
    }

    /**
     * Appends the specified element to the end of this list (optional
     * operation).
     *
     * <p>Lists that support this operation may place limitations on what
     * elements may be added to this list.  In particular, some
     * lists will refuse to add null elements, and others will impose
     * restrictions on the type of elements that may be added.  List
     * classes should clearly specify in their documentation any restrictions
     * on what elements may be added.
     *
     * @implSpec
     * This implementation calls {@code add(size(), e)}.
     *
     * <p>Note that this implementation throws an
     * {@code UnsupportedOperationException} unless
     * {@link #add(int, Object) add(int, E)} is overridden.
     *
     * @param e element to be appended to this list
     * @return {@code true} (as specified by {@link Collection#add})
     * @throws UnsupportedOperationException if the {@code add} operation
     *         is not supported by this list
     * @throws ClassCastException if the class of the specified element
     *         prevents it from being added to this list
     * @throws NullPointerException if the specified element is null and this
     *         list does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     *         prevents it from being added to this list
     */
    // 添加元素到列表末尾，由子类去具体实现
    public boolean add(E e) {
        // 这里add(index, object) 和size()方法都由子类实现
        add(size(), e);
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    // 根据索引获取集合中某个元素，抽象方法，由子类去实现
    public abstract E get(int index);

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation always throws an
     * {@code UnsupportedOperationException}.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation always throws an
     * {@code UnsupportedOperationException}.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation always throws an
     * {@code UnsupportedOperationException}.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }


    // Search Operations

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation first gets a list iterator (with
     * {@code listIterator()}).  Then, it iterates over the list until the
     * specified element is found or the end of the list is reached.
     *
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    // 获取某个元素第一次在集合中出现的索引。该实现首先获取ListIterator，然后迭代集合直到该元素被找到
    // 或集合尾部到达为止
    public int indexOf(Object o) {
        // AbstractList内部已经提供了Iterator、ListIterator迭代器的实现类，分别为Itr、ListItr。
        // 这里是调用了一个实例化ListItr的方法
        ListIterator<E> it = listIterator();
        if (o==null) {
            while (it.hasNext())
                if (it.next()==null) // 当前游标下一个元素为null，游标向后移动一个元素距离
                    return it.previousIndex(); // 返回当前游标上一个元素索引，即为null元素索引
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return it.previousIndex();
        }
        return -1; // 未找到返回-1
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation first gets a list iterator that points to the end
     * of the list (with {@code listIterator(size())}).  Then, it iterates
     * backwards over the list until the specified element is found, or the
     * beginning of the list is reached.
     *
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    // 获取某个元素在集合中最后一次出现的索引
    public int lastIndexOf(Object o) {
        ListIterator<E> it = listIterator(size()); // 获取指向集合最后一个元素后面的ListIterator
        if (o==null) {
            while (it.hasPrevious())
                if (it.previous()==null) // 当前游标上一个元素值为null，游标向前移动一个元素距离
                    return it.nextIndex(); // 返回当前游标下一个元素索引，即为null元素索引
        } else {
            while (it.hasPrevious())
                if (o.equals(it.previous()))
                    return it.nextIndex();
        }
        return -1;
    }

    // 批量操作
    // Bulk Operations

    /**
     * Removes all of the elements from this list (optional operation).
     * The list will be empty after this call returns.
     *
     * @implSpec
     * This implementation calls {@code removeRange(0, size())}.
     *
     * <p>Note that this implementation throws an
     * {@code UnsupportedOperationException} unless {@code remove(int
     * index)} or {@code removeRange(int fromIndex, int toIndex)} is
     * overridden.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *         is not supported by this list
     */
    // 清除集合中元素，结束调用后集合将变成空集合
    public void clear() {
        removeRange(0, size());
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation gets an iterator over the specified collection
     * and iterates over it, inserting the elements obtained from the
     * iterator into this list at the appropriate position, one at a time,
     * using {@code add(int, E)}.
     * Many implementations will override this method for efficiency.
     *
     * <p>Note that this implementation throws an
     * {@code UnsupportedOperationException} unless
     * {@link #add(int, Object) add(int, E)} is overridden.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IndexOutOfBoundsException     {@inheritDoc}
     */
    // 从某个索引开始，将集合c中元素添加到集合中
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);
        boolean modified = false;
        for (E e : c) {
            add(index++, e);
            modified = true;
        }
        return modified;
    }


    // Iterators

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @implSpec
     * This implementation returns a straightforward implementation of the
     * iterator interface, relying on the backing list's {@code size()},
     * {@code get(int)}, and {@code remove(int)} methods.
     *
     * <p>Note that the iterator returned by this method will throw an
     * {@link UnsupportedOperationException} in response to its
     * {@code remove} method unless the list's {@code remove(int)} method is
     * overridden.
     *
     * <p>This implementation can be made to throw runtime exceptions in the
     * face of concurrent modification, as described in the specification
     * for the (protected) {@link #modCount} field.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    // 获取Iterator接口Itr实现类迭代器
    public Iterator<E> iterator() {
        return new Itr();
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation returns {@code listIterator(0)}.
     *
     * @see #listIterator(int)
     */
    // 获取从0开始（初始位置）的ListIterator的实现类ListItr
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation returns a straightforward implementation of the
     * {@code ListIterator} interface that extends the implementation of the
     * {@code Iterator} interface returned by the {@code iterator()} method.
     * The {@code ListIterator} implementation relies on the backing list's
     * {@code get(int)}, {@code set(int, E)}, {@code add(int, E)}
     * and {@code remove(int)} methods.
     *
     * <p>Note that the list iterator returned by this implementation will
     * throw an {@link UnsupportedOperationException} in response to its
     * {@code remove}, {@code set} and {@code add} methods unless the
     * list's {@code remove(int)}, {@code set(int, E)}, and
     * {@code add(int, E)} methods are overridden.
     *
     * <p>This implementation can be made to throw runtime exceptions in the
     * face of concurrent modification, as described in the specification for
     * the (protected) {@link #modCount} field.
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    // 获取索引等于index的位置的迭代器ListItr
    public ListIterator<E> listIterator(final int index) {
        rangeCheckForAdd(index);

        return new ListItr(index);
    }

    // 实现了Iterator接口的内部实现类Itr
    private class Itr implements Iterator<E> {
        /**
         * Index of element to be returned by subsequent call to next.
         */
        // 后续调用next返回的元素索引，即光标位置
        int cursor = 0;

        /**
         * Index of element returned by most recent call to next or
         * previous.  Reset to -1 if this element is deleted by a call
         * to remove.
         */
        // 最近一次返回的元素索引。每一次调用next或者previous时会将lastRet设置为刚要返回的元素索引，
        // 如果执行remove方法删除了该元素，则将lastRet重置为-1，用来标识不能再删除元素。
        int lastRet = -1;

        /**
         * The modCount value that the iterator believes that the backing
         * List should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        // 并发标志，如果两个值不一致，说明发生了并发操作，就会报错
        int expectedModCount = modCount;

        // 判断是否存在下一条数据，即迭代器的位置是否走到集合末尾
        public boolean hasNext() {
            // 如果光标位置不等于集合个数，说明迭代器没有走到末尾，返回true
            return cursor != size();
        }

        // 获取下一个元素
        public E next() {
            // 判断是否有并发操作
            checkForComodification();
            try {
                int i = cursor; // 保存光标指向的当前元素位置
                E next = get(i);// 通过索引位置来获取元素，可能抛IndexOutOfBoundsException
                lastRet = i; // 将lastRet置为要返回的元素的索引
                cursor = i + 1; // 光标后移一位，指向下一个元素
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification(); // 检查是否并发操作导致越界
                throw new NoSuchElementException();
            }
        }

        /**
         * modCount这个变量代表着当前列表对象的结构性修改的次数，每次进行修改都会进行加1的操作，
         * 而expectedModCount代表的是迭代器对对象进行结构性修改的次数。每次对List进行结构性修改的时候
         * 都会将expectedModCount和modCount进行对比，如果相等的话，说明没有别的迭代器对对对象进行修改。
         * 如果不相等，说明发生了并发的操作，就会抛出一个异常。当也不全部都是如此，比如下面remove方法，将
         * 集合列表结构性修改次数modCount和迭代器对集合修改的次数进行同步，保持两者值相等，避免误判导致
         * 出现ConcurrentModificationException
         */

        // 删除最近一次调用next或previous方法返回的元素，如果删除的是next返回的元素，游标左移1位。
        // 每次删除后lastRet重置为-1
        public void remove() {
            if (lastRet < 0) // 抛异常来防止连续删除
                throw new IllegalStateException();
            checkForComodification(); // 迭代期间集合发生改变，抛出异常

            try {
                //调用在子类实现的remove方法
                // 删除索引位置为lastRet的元素，其后元素左移一位，迭代期间集合发生改变，
                // 可能抛IndexOutOfBoundsException
                AbstractList.this.remove(lastRet);
                if (lastRet < cursor) // 当删除调用previous返回的元素时也进入，进入后此时cursor == lastRet
                    cursor--; // 调用next方法后游标指向返回的元素下一个元素位置，调用remove后需要向前移一位
                lastRet = -1; // 每次删除后，将lastRet置为-1，防止连续的删除
                expectedModCount = modCount;// 将修改次数赋给迭代器对对象的结构修改次数
            } catch (IndexOutOfBoundsException e) {
                // 进行删除操作后，将修改次数和迭代器对象进行同步，虽然在方法的开始进行了checkForComodification()
                // 方法的判断，但是在进行删除操作的时有可能发生了并发的操作，所以在这里进行了异常捕获处理，当发生了
                // 索引越界的异常的时候，说明一定是发生了并发的操作，所以抛出一个ConcurrentModificationException()
                throw new ConcurrentModificationException();
            }
        }

        // 判断是否有并发操作
        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    // 继承Itr并实现ListIterator迭代接口
    private class ListItr extends Itr implements ListIterator<E> {
        // 指定光标位置等于索引的迭代器构造函数
        ListItr(int index) {
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0; // 光标不在0位置返回true
        }

        // 返回当前游标的上一个元素，并将游标的位置向前移动一个元素的距离
        public E previous() {
            checkForComodification();
            try {
                int i = cursor - 1; // 将游标位置减1，获得当前游标上一个元素的索引位置
                E previous = get(i); // 根据索引获取元素，可能抛出IndexOutOfBoundsException
                // 每一次调用next或者previous时会将lastRet设置为刚要返回的元素索引，即此处设置为i，
                // 游标位置需要向前移动一个元素距离，所以cursor=cursor-1
                lastRet = cursor = i;
                return previous;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException();
            }
        }

        // 下一位的索引值等于游标值
        public int nextIndex() {
            return cursor;
        }

        // 上一位的索引值等于游标值减一
        public int previousIndex() {
            return cursor-1;
        }

        // 设置元素
        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                // 设置的位置是上一次迭代器越过的元素。即替换上一次调用next或previous集合的返回值，
                // 在调用set方法之前，如果没有调用一次next或者previous方法，或者在调用了next、
                // previous方法之后已经调用过了remove方法或add方法，会抛IllegalStateException
                AbstractList.this.set(lastRet, e);
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        // 添加元素，并将游标后移1位指向新添加的元素后，同时将lastRet重置为-1。
        // 所以在add方法之后紧跟remove方法或者set方法都将抛出IllegalStateException异常。
        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;  // 设置添加的位置为当前游标所在的位置
                AbstractList.this.add(i, e); // add方法子类实现，并将当前游标所在位置及其后元素后移1位
                lastRet = -1; // 将lastRet设置为-1，即新添加的元素不允许接着调用remove删除或set重新设置它值
                cursor = i + 1; // 添加后游标后移1位，指向下一个元素
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation returns a list that subclasses
     * {@code AbstractList}.  The subclass stores, in private fields, the
     * size of the subList (which can change over its lifetime), and the
     * expected {@code modCount} value of the backing list.  There are two
     * variants of the subclass, one of which implements {@code RandomAccess}.
     * If this list implements {@code RandomAccess} the returned list will
     * be an instance of the subclass that implements {@code RandomAccess}.
     *
     * <p>The subclass's {@code set(int, E)}, {@code get(int)},
     * {@code add(int, E)}, {@code remove(int)}, {@code addAll(int,
     * Collection)} and {@code removeRange(int, int)} methods all
     * delegate to the corresponding methods on the backing abstract list,
     * after bounds-checking the index and adjusting for the offset.  The
     * {@code addAll(Collection c)} method merely returns {@code addAll(size,
     * c)}.
     *
     * <p>The {@code listIterator(int)} method returns a "wrapper object"
     * over a list iterator on the backing list, which is created with the
     * corresponding method on the backing list.  The {@code iterator} method
     * merely returns {@code listIterator()}, and the {@code size} method
     * merely returns the subclass's {@code size} field.
     *
     * <p>All methods first check to see if the actual {@code modCount} of
     * the backing list is equal to its expected value, and throw a
     * {@code ConcurrentModificationException} if it is not.
     *
     * @throws IndexOutOfBoundsException if an endpoint index value is out of range
     *         {@code (fromIndex < 0 || toIndex > size)}
     * @throws IllegalArgumentException if the endpoint indices are out of order
     *         {@code (fromIndex > toIndex)}
     */

    // 获取子列表
    public List<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size()); // 检查索引有效性
        // 判断当前类是否实现了随机访问接口，根据是否支持随机访问返回不同类型的子集合列表
        // RandomAccess是个标记接口，表明实现这个接口的List是支持快速随机访问的。实现了这个接口的List，
        // 使用for循环的方式获取数据会优于使用迭代器获取数据。如ArrayList实现了该接口而LinkedList没有实现
        // 该接口。使用for循环访问ArrayList要快于使用迭代器访问。
        return (this instanceof RandomAccess ?
                new RandomAccessSubList<>(this, fromIndex, toIndex) :
                new SubList<>(this, fromIndex, toIndex));
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        if (toIndex > size)
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex +
                                               ") > toIndex(" + toIndex + ")");
    }

    // Comparison and hashing

    /**
     * Compares the specified object with this list for equality.  Returns
     * {@code true} if and only if the specified object is also a list, both
     * lists have the same size, and all corresponding pairs of elements in
     * the two lists are <i>equal</i>.  (Two elements {@code e1} and
     * {@code e2} are <i>equal</i> if {@code (e1==null ? e2==null :
     * e1.equals(e2))}.)  In other words, two lists are defined to be
     * equal if they contain the same elements in the same order.
     *
     * @implSpec
     * This implementation first checks if the specified object is this
     * list. If so, it returns {@code true}; if not, it checks if the
     * specified object is a list. If not, it returns {@code false}; if so,
     * it iterates over both lists, comparing corresponding pairs of elements.
     * If any comparison returns {@code false}, this method returns
     * {@code false}.  If either iterator runs out of elements before the
     * other it returns {@code false} (as the lists are of unequal length);
     * otherwise it returns {@code true} when the iterations complete.
     *
     * @param o the object to be compared for equality with this list
     * @return {@code true} if the specified object is equal to this list
     */
    // 判断集合是否相等，如果两个集合类型相同，包含相同的元素且元素之间顺序也相同，则返回true
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof List))
            return false;

        ListIterator<E> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator(); // 不知道集合o元素具体类型，使用?表示任意类型
        while (e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();
            // 1、o1 == null  2、o1 ！= null
            if (!(o1==null ? o2==null : o1.equals(o2)))
                return false;
        }
        // 1、e1后还有元素，e2后没有  2、e1后没有元素，e2后有
        return !(e1.hasNext() || e2.hasNext());
    }

    /**
     * Returns the hash code value for this list.
     *
     * @implSpec
     * This implementation uses exactly the code that is used to define the
     * list hash function in the documentation for the {@link List#hashCode}
     * method.
     *
     * @return the hash code value for this list
     */
    // 根据列表元素hash值来计算集合列表hash值，能够保证通过equals判断相等的list的hashCode也相等
    public int hashCode() {
        int hashCode = 1;
        for (E e : this)
            hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
        return hashCode;
    }

    /**
     * Removes from this list all of the elements whose index is between
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
     * Shifts any succeeding elements to the left (reduces their index).
     * This call shortens the list by {@code (toIndex - fromIndex)} elements.
     * (If {@code toIndex==fromIndex}, this operation has no effect.)
     *
     * <p>This method is called by the {@code clear} operation on this list
     * and its subLists.  Overriding this method to take advantage of
     * the internals of the list implementation can <i>substantially</i>
     * improve the performance of the {@code clear} operation on this list
     * and its subLists.
     *
     * @implSpec
     * This implementation gets a list iterator positioned before
     * {@code fromIndex}, and repeatedly calls {@code ListIterator.next}
     * followed by {@code ListIterator.remove} until the entire range has
     * been removed.  <b>Note: if {@code ListIterator.remove} requires linear
     * time, this implementation requires quadratic time.</b>
     *
     * @param fromIndex index of first element to be removed
     * @param toIndex index after last element to be removed
     */
    // 删除[fromIndex,toIndex)范围内的元素
    protected void removeRange(int fromIndex, int toIndex) {
        ListIterator<E> it = listIterator(fromIndex);
        for (int i=0, n=toIndex-fromIndex; i<n; i++) {
            // 返回索引位置等于当前游标cursor的元素，设置lastRet=cursor，游标后移一位指向下一个元素
            it.next();
            // 删除索引位置等于lastRet的元素，集合元素左移，cursor=cursor-1，游标重新指向最开始删除元素位置
            it.remove();
        }
    }

    /**
     * The number of times this list has been <i>structurally modified</i>.
     * Structural modifications are those that change the size of the
     * list, or otherwise perturb it in such a fashion that iterations in
     * progress may yield incorrect results.
     *
     * <p>This field is used by the iterator and list iterator implementation
     * returned by the {@code iterator} and {@code listIterator} methods.
     * If the value of this field changes unexpectedly, the iterator (or list
     * iterator) will throw a {@code ConcurrentModificationException} in
     * response to the {@code next}, {@code remove}, {@code previous},
     * {@code set} or {@code add} operations.  This provides
     * <i>fail-fast</i> behavior, rather than non-deterministic behavior in
     * the face of concurrent modification during iteration.
     *
     * <p><b>Use of this field by subclasses is optional.</b> If a subclass
     * wishes to provide fail-fast iterators (and list iterators), then it
     * merely has to increment this field in its {@code add(int, E)} and
     * {@code remove(int)} methods (and any other methods that it overrides
     * that result in structural modifications to the list).  A single call to
     * {@code add(int, E)} or {@code remove(int)} must add no more than
     * one to this field, or the iterators (and list iterators) will throw
     * bogus {@code ConcurrentModificationExceptions}.  If an implementation
     * does not wish to provide fail-fast iterators, this field may be
     * ignored.
     */
    // modCount指已从结构上修改此列表的次数。从结构上修改是指更改列表的大小，或者打乱列表，从而使正在进行的
    // 迭代产生错误的结果。此字段由 iterator 和 listIterator 方法返回的迭代器和列表迭代器实现使用。如果
    // 意外更改了此字段中的值，则迭代器（或列表迭代器）将抛出 ConcurrentModificationException 来响应
    // next、remove、previous、set 或 add 操作。在迭代期间面临并发修改时，它提供了快速失败 行为，而不是
    // 非确定性行为。调用List的remove和add方法都会修改modCount。modCount是transient的，序列化时候不需要储存
    protected transient int modCount = 0;

    // 检查索引是否合法
    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size();
    }

    /**
     * An index-based split-by-two, lazily initialized Spliterator covering
     * a List that access elements via {@link List#get}.
     *
     * If access results in an IndexOutOfBoundsException then a
     * ConcurrentModificationException is thrown instead (since the list has
     * been structurally modified while traversing).
     *
     * If the List is an instance of AbstractList then concurrent modification
     * checking is performed using the AbstractList's modCount field.
     */
    static final class RandomAccessSpliterator<E> implements Spliterator<E> {

        private final List<E> list;
        private int index; // current index, modified on advance/split
        private int fence; // -1 until used; then one past last index

        // The following fields are valid if covering an AbstractList
        private final AbstractList<E> alist;
        private int expectedModCount; // initialized when fence set

        RandomAccessSpliterator(List<E> list) {
            assert list instanceof RandomAccess;

            this.list = list;
            this.index = 0;
            this.fence = -1;

            this.alist = list instanceof AbstractList ? (AbstractList<E>) list : null;
            this.expectedModCount = alist != null ? alist.modCount : 0;
        }

        /** Create new spliterator covering the given  range */
        private RandomAccessSpliterator(RandomAccessSpliterator<E> parent,
                                int origin, int fence) {
            this.list = parent.list;
            this.index = origin;
            this.fence = fence;

            this.alist = parent.alist;
            this.expectedModCount = parent.expectedModCount;
        }

        private int getFence() { // initialize fence to size on first use
            int hi;
            List<E> lst = list;
            if ((hi = fence) < 0) {
                if (alist != null) {
                    expectedModCount = alist.modCount;
                }
                hi = fence = lst.size();
            }
            return hi;
        }

        public Spliterator<E> trySplit() {
            int hi = getFence(), lo = index, mid = (lo + hi) >>> 1;
            return (lo >= mid) ? null : // divide range in half unless too small
                    new RandomAccessSpliterator<>(this, lo, index = mid);
        }

        public boolean tryAdvance(Consumer<? super E> action) {
            if (action == null)
                throw new NullPointerException();
            int hi = getFence(), i = index;
            if (i < hi) {
                index = i + 1;
                action.accept(get(list, i));
                checkAbstractListModCount(alist, expectedModCount);
                return true;
            }
            return false;
        }

        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            List<E> lst = list;
            int hi = getFence();
            int i = index;
            index = hi;
            for (; i < hi; i++) {
                action.accept(get(lst, i));
            }
            checkAbstractListModCount(alist, expectedModCount);
        }

        public long estimateSize() {
            return (long) (getFence() - index);
        }

        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED;
        }

        private static <E> E get(List<E> list, int i) {
            try {
                return list.get(i);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        static void checkAbstractListModCount(AbstractList<?> alist, int expectedModCount) {
            if (alist != null && alist.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    // 继承自AbstractList的内部类SubList，代表了它父类的一部分
    private static class SubList<E> extends AbstractList<E> {
        private final AbstractList<E> root; // 根列表
        private final SubList<E> parent; // 父子列表
        private final int offset; // 子列表相对根列表初始位置0的偏移量
        protected int size;

        /**
         * Constructs a sublist of an arbitrary AbstractList, which is
         * not a SubList itself.
         */
        // 构造一个任意AbstractList的子列表，它本身不是SubList。范围包含fromIndex，不包含toIndex
        public SubList(AbstractList<E> root, int fromIndex, int toIndex) {
            this.root = root;
            this.parent = null;
            this.offset = fromIndex; // 偏移量为根列表抽取的起始位置
            this.size = toIndex - fromIndex;
            this.modCount = root.modCount; // 修改次数（并发标志）和父类保持一致
        }

        /**
         * Constructs a sublist of another SubList.
         */
        // 根据SubList构建子列表
        protected SubList(SubList<E> parent, int fromIndex, int toIndex) {
            this.root = parent.root; // 字列表subList的父列表
            this.parent = parent;
            this.offset = parent.offset + fromIndex; // 偏移量等于父子列表偏移+起始索引=相对于根列表的偏移
            this.size = toIndex - fromIndex;
            this.modCount = root.modCount;
        }

        // 设置子列表索引位置index的元素，实际上还是调用根列表的set方法
        public E set(int index, E element) {
            Objects.checkIndex(index, size);
            checkForComodification();
            return root.set(offset + index, element); // 设置根列表offset+index位置元素
        }

        public E get(int index) {
            Objects.checkIndex(index, size);
            checkForComodification();
            return root.get(offset + index);
        }

        // size = toIndex - fromIndex
        public int size() {
            checkForComodification();
            return size;
        }

        public void add(int index, E element) {
            rangeCheckForAdd(index);
            checkForComodification();
            // 实际上还是在父类上进行添加元素
            root.add(offset + index, element);
            // 修改modCount和size
            updateSizeAndModCount(1);
        }

        // 删除并返回被删元素
        public E remove(int index) {
            Objects.checkIndex(index, size);
            checkForComodification();
            // 实际上还是在父类上进行删除
            E result = root.remove(offset + index);
            updateSizeAndModCount(-1);
            return result;
        }

        protected void removeRange(int fromIndex, int toIndex) {
            checkForComodification();
            // 调用根列表的removeRange方法
            root.removeRange(offset + fromIndex, offset + toIndex);
            updateSizeAndModCount(fromIndex - toIndex);
        }

        public boolean addAll(Collection<? extends E> c) {
            return addAll(size, c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            rangeCheckForAdd(index);
            int cSize = c.size();
            if (cSize==0)
                return false;
            checkForComodification();
            root.addAll(offset + index, c);
            updateSizeAndModCount(cSize);
            return true;
        }

        public Iterator<E> iterator() {
            return listIterator();
        }

        // 获取子列表索引位置为index的列表迭代器，实际返回的是父类根列表offset+index位置的列表迭代器
        public ListIterator<E> listIterator(int index) {
            checkForComodification();
            rangeCheckForAdd(index);

            //返回的是一个匿名内部类
            return new ListIterator<E>() {
                // 实际还是返回父类根列表对应位置的列表迭代器
                private final ListIterator<E> i =
                        root.listIterator(offset + index);

                // 子列表内部索引大小需小于子列表元素个数
                public boolean hasNext() {
                    return nextIndex() < size;
                }

                public E next() {
                    if (hasNext())
                        return i.next();
                    else
                        throw new NoSuchElementException();
                }

                public boolean hasPrevious() {
                    return previousIndex() >= 0;
                }

                public E previous() {
                    if (hasPrevious())
                        return i.previous();
                    else
                        throw new NoSuchElementException();
                }

                // 子列表下一个索引位置，即相对于偏移量offset的位置
                public int nextIndex() {
                    return i.nextIndex() - offset;
                }

                // 子列表上一个索引位置，即相对于偏移量offset的位置
                public int previousIndex() {
                    return i.previousIndex() - offset;
                }

                public void remove() {
                    i.remove(); // 删除父类根列表offset+index位置的元素
                    updateSizeAndModCount(-1);
                }

                public void set(E e) {
                    i.set(e);
                }

                public void add(E e) {
                    i.add(e);
                    updateSizeAndModCount(1);
                }
            };
        }

        // 从子列表中抽取孩子子列表
        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new SubList<>(this, fromIndex, toIndex);
        }

        private void rangeCheckForAdd(int index) {
            if (index < 0 || index > size)
                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }

        private String outOfBoundsMsg(int index) {
            return "Index: "+index+", Size: "+size;
        }

        private void checkForComodification() {
            if (root.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }

        // 将子列表的modCount与根列表设置为一致，同时size增加sizeChange，如果直接由AbstractList创建而来
        // 则当前子列表的size增加sizeChange，modCout和根列表保持一致。如果由SubList创建而来，则第一次将
        // 当前子列表本身的size和modCount进行修改，然后将父亲子列表的size和modCount进行修改
        private void updateSizeAndModCount(int sizeChange) {
            SubList<E> slist = this;
            do {
                slist.size += sizeChange;
                slist.modCount = root.modCount;
                slist = slist.parent; // 如果parent为SubList，则开始下一次循环，直到parent为null
            } while (slist != null);
        }
    }

    // 继承于SubList内部类，多了一个是否可以随机访问的标志
    private static class RandomAccessSubList<E>
            extends SubList<E> implements RandomAccess {

        /**
         * Constructs a sublist of an arbitrary AbstractList, which is
         * not a RandomAccessSubList itself.
         */
        RandomAccessSubList(AbstractList<E> root,
                int fromIndex, int toIndex) {
            super(root, fromIndex, toIndex);
        }

        /**
         * Constructs a sublist of another RandomAccessSubList.
         */
        RandomAccessSubList(RandomAccessSubList<E> parent,
                int fromIndex, int toIndex) {
            super(parent, fromIndex, toIndex);
        }

        public List<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, size);
            return new RandomAccessSubList<>(this, fromIndex, toIndex);
        }
    }
}
