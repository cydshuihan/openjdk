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

/**
 * This class provides a skeletal implementation of the {@code Collection}
 * interface, to minimize the effort required to implement this interface. <p>
 *
 * To implement an unmodifiable collection, the programmer needs only to
 * extend this class and provide implementations for the {@code iterator} and
 * {@code size} methods.  (The iterator returned by the {@code iterator}
 * method must implement {@code hasNext} and {@code next}.)<p>
 *
 * To implement a modifiable collection, the programmer must additionally
 * override this class's {@code add} method (which otherwise throws an
 * {@code UnsupportedOperationException}), and the iterator returned by the
 * {@code iterator} method must additionally implement its {@code remove}
 * method.<p>
 *
 * The programmer should generally provide a void (no argument) and
 * {@code Collection} constructor, as per the recommendation in the
 * {@code Collection} interface specification.<p>
 *
 * The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if
 * the collection being implemented admits a more efficient implementation.<p>
 *
 * This class is a member of the
 * <a href="{@docRoot}/java.base/java/util/package-summary.html#CollectionsFramework">
 * Java Collections Framework</a>.
 *
 * @author  Josh Bloch
 * @author  Neal Gafter
 * @see Collection
 * @since 1.2
 */

// Collection集合抽象类，实现部分接口，另外一部分接口由子类实现
public abstract class AbstractCollection<E> implements Collection<E> {
    /**
     * Sole constructor.  (For invocation by subclass constructors, typically
     * implicit.)
     */
    protected AbstractCollection() {
    }

    // Query Operations

    /**
     * Returns an iterator over the elements contained in this collection.
     *
     * @return an iterator over the elements contained in this collection
     */
    public abstract Iterator<E> iterator();

    // size接口的实现交给子类
    public abstract int size();

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation returns {@code size() == 0}.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation iterates over the elements in the collection,
     * checking each element in turn for equality with the specified element.
     *
     * @throws ClassCastException   {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    // 根据o是否为null依次检查每个元素是否与指定元素o相等
    public boolean contains(Object o) {
        Iterator<E> it = iterator();
        if (o==null) {
            while (it.hasNext())
                if (it.next()==null)
                    return true;
        } else {
            while (it.hasNext())
                if (o.equals(it.next()))
                    return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation returns an array containing all the elements
     * returned by this collection's iterator, in the same order, stored in
     * consecutive elements of the array, starting with index {@code 0}.
     * The length of the returned array is equal to the number of elements
     * returned by the iterator, even if the size of this collection changes
     * during iteration, as might happen if the collection permits
     * concurrent modification during iteration.  The {@code size} method is
     * called only as an optimization hint; the correct result is returned
     * even if the iterator returns a different number of elements.
     *
     * <p>This method is equivalent to:
     *
     *  <pre> {@code
     * List<E> list = new ArrayList<E>(size());
     * for (E e : this)
     *     list.add(e);
     * return list.toArray();
     * }</pre>
     */
    // 将集合直接转换为Object[]数组，分配一个等大空间的数组（即使在迭代过程中集合的size发生变化，
    // 如果该集合允许在迭代过程中进行并发修改的话），然后依次对数组元素进行赋值。使用时不能直接将
    // 得到的对象数组Object[]直接进行类型转换，会报ClassCastException，需要取出每一个元素再转换。
    // 因为Java中的强制类型转换只是针对单个对象的，想要偷懒将整个数组转换成另外一种类型的数组是不行的，
    // 这和数组初始化时我们需要一个个给数组元素赋值是类似的。一般推荐用另一个重载方法toArray(T[] a)
    public Object[] toArray() {
        // Estimate size of array; be prepared to see more or fewer elements
        // 新建等大的数组
        Object[] r = new Object[size()];
        Iterator<E> it = iterator();
        for (int i = 0; i < r.length; i++) {
            // 判断是否遍历结束，以防多线程操作的时候集合size变得更小
            if (! it.hasNext()) // fewer elements than expected
                // 返回实际长度为i的新数组
                return Arrays.copyOf(r, i);
            r[i] = it.next();
        }
        //判断是否遍历未结束，以防多线程操作的时候集合变得更大，需要进行扩容
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation returns an array containing all the elements
     * returned by this collection's iterator in the same order, stored in
     * consecutive elements of the array, starting with index {@code 0}.
     * If the number of elements returned by the iterator is too large to
     * fit into the specified array, then the elements are returned in a
     * newly allocated array with length equal to the number of elements
     * returned by the iterator, even if the size of this collection
     * changes during iteration, as might happen if the collection permits
     * concurrent modification during iteration.  The {@code size} method is
     * called only as an optimization hint; the correct result is returned
     * even if the iterator returns a different number of elements.
     *
     * <p>This method is equivalent to:
     *
     *  <pre> {@code
     * List<E> list = new ArrayList<E>(size());
     * for (E e : this)
     *     list.add(e);
     * return list.toArray(a);
     * }</pre>
     *
     * @throws ArrayStoreException  {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    // 泛型方法toArray(T[] a)返回这个集合的对象数组，如果对象数组a足够大，就将集合中的元素填入这个数组中,并在后一位填补null；
    // 否则，分配一个新数组，其成员类型与a的成员类型相同，其长度等于集合的大小，并填充集合元素。该方法是将集合转为你所需类型的
    // 数组，我们使用的时候肯定转换为与集合内容相同的类型，否则会抛ArrayStoreException

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        // Estimate size of array; be prepared to see more or fewer elements
        int size = size();
        // 当对象数组a的长度大于等于集合长度size，直接将a赋给r；否则，使用反射机制中对象数组的getComponentType()方法，
        // 通过Array.newInstance()反射生成大小为size的数组对象
        T[] r = a.length >= size ? a :
                  (T[])java.lang.reflect.Array
                  .newInstance(a.getClass().getComponentType(), size);
        Iterator<E> it = iterator();

        for (int i = 0; i < r.length; i++) {
            // 判断是否遍历结束，以防多线程操作时集合变得更小
            if (! it.hasNext()) { // fewer elements than expected
                // a等于r时，如果集合大小小于数组长度，将r的下一项（即a的下一项）置为null，作为集合元素的结束标志，
                // 同时返回a。注意这里并非是将r的剩余项全部置为null
                if (a == r) {
                    r[i] = null; // null-terminate
                } else if (a.length < i) {
                    // 如果a的长度小于集合r的长度，即r是上面通过反射生成的数组对象，通过Arrays.copyOf截短复制创建一个新数组返回
                    return Arrays.copyOf(r, i);
                } else {
                    // r是上面反射创建的新数组，多线程操作下a的长度大于r的大小，将r数组复制到a数组当中，并将a中第i个位置设置为null并返回a
                    System.arraycopy(r, 0, a, 0, i);
                    if (a.length > i) {
                        a[i] = null;
                    }
                }
                return a;
            }
            r[i] = (T)it.next(); // 遍历未结束，将迭代器获取的值赋给r。类型转换可能会抛出ClassCastException异常
        }
        // 判断是否未遍历结束，以防多线程操作时集合变得更大，需要进行扩容
        // more elements than expected
        return it.hasNext() ? finishToArray(r, it) : r;
    }

    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    //设定容器的最大容量。数组作为一个对象，有些虚拟机需要一定的内存存储对象头信息，对象头信息最大占用内存不可超过8 byte
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Reallocates the array being used within toArray when the iterator
     * returned more elements than expected, and finishes filling it from
     * the iterator.
     *
     * @param r the array, replete with previously stored elements
     * @param it the in-progress iterator over this collection
     * @return array containing the elements in the given array, plus any
     *         further elements returned by the iterator, trimmed to size
     */
    // 对数组进行扩容：当toArray(T[] a)内部数组大小小于实际迭代器返回的个数时，重新分配一个数组，将原数组内容复制到新数组中
    // 扩容大小：cap+cap/2+1，其中cap=r.length，扩容前需判断数组长度是否溢出（即是否大于MAX_ARRAY_SIZE）
    @SuppressWarnings("unchecked")
    private static <T> T[] finishToArray(T[] r, Iterator<?> it) {
        int i = r.length;
        while (it.hasNext()) {
            int cap = r.length; // 第一次访问为未扩容前r长度，后面为扩容后r的长度
            if (i == cap) {
                int newCap = cap + (cap >> 1) + 1;
                // overflow-conscious code
                if (newCap - MAX_ARRAY_SIZE > 0) // 新扩大的容量发生溢出
                    // 这里这样传参是为了避免当r[]的长度cap刚好等于整形最大值，已不能再对其扩容。所以加1后溢出，会抛出异常。
                    newCap = hugeCapacity(cap + 1);
                r = Arrays.copyOf(r, newCap); // 创建长度为newCap的新数组，并将r元素复制过去，如果新数组的长度超过原数组r的长度，则保留数组中元素类型默认值
            }
            r[i++] = (T)it.next(); // 数组有效长度自增，并将迭代器获取的值赋给r
        }
        // 如果分配的长度过大，截短数组长度到有效长度，创建数组复制返回
        // trim if overallocated
        return (i == r.length) ? r : Arrays.copyOf(r, i);
    }

    // 判断数组容量是否溢出，最大为整形数据的最大值Integer.MAX_VALUE
    private static int hugeCapacity(int minCapacity) {
        // 超出整型的最大值,minCapacity数值溢出为负值
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError
                ("Required array size too large");
        //如果所需的最小容量minCapacity大于集合设定的最大容量，则返回整数的最大值，否则返回集合的最大容量
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }

    // Modification Operations

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
     * @throws IllegalStateException         {@inheritDoc}
     */
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation iterates over the collection looking for the
     * specified element.  If it finds the element, it removes the element
     * from the collection using the iterator's remove method.
     *
     * <p>Note that this implementation throws an
     * {@code UnsupportedOperationException} if the iterator returned by this
     * collection's iterator method does not implement the {@code remove}
     * method and this collection contains the specified object.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     */
    public boolean remove(Object o) {
        Iterator<E> it = iterator();
        if (o==null) {
            while (it.hasNext()) {
                if (it.next()==null) {
                    it.remove();
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (o.equals(it.next())) {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }


    // Bulk Operations

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation iterates over the specified collection,
     * checking each element returned by the iterator in turn to see
     * if it's contained in this collection.  If all elements are so
     * contained {@code true} is returned, otherwise {@code false}.
     *
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @see #contains(Object)
     */
    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation iterates over the specified collection, and adds
     * each object returned by the iterator to this collection, in turn.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} unless {@code add} is
     * overridden (assuming the specified collection is non-empty).
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     * @throws IllegalStateException         {@inheritDoc}
     *
     * @see #add(Object)
     */
    //批量操作：初始化一个modified标志，只要有一个参数集合的元素被操作成功并使得集合发生改变则返回true，
    // 而不是所有参数集合都被成功操作。
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c)
            if (add(e))
                modified = true;
        return modified;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation iterates over this collection, checking each
     * element returned by the iterator in turn to see if it's contained
     * in the specified collection.  If it's so contained, it's removed from
     * this collection with the iterator's {@code remove} method.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the iterator returned by the
     * {@code iterator} method does not implement the {@code remove} method
     * and this collection contains one or more elements in common with the
     * specified collection.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     *
     * @see #remove(Object)
     * @see #contains(Object)
     */
    // 删除集合中包含集合c的元素
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c); // 参数校验非null
        boolean modified = false;
        Iterator<?> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation iterates over this collection, checking each
     * element returned by the iterator in turn to see if it's contained
     * in the specified collection.  If it's not so contained, it's removed
     * from this collection with the iterator's {@code remove} method.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the iterator returned by the
     * {@code iterator} method does not implement the {@code remove} method
     * and this collection contains one or more elements not present in the
     * specified collection.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     *
     * @see #remove(Object)
     * @see #contains(Object)
     */
    // 保留集合中被集合C包含的元素
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec
     * This implementation iterates over this collection, removing each
     * element using the {@code Iterator.remove} operation.  Most
     * implementations will probably choose to override this method for
     * efficiency.
     *
     * <p>Note that this implementation will throw an
     * {@code UnsupportedOperationException} if the iterator returned by this
     * collection's {@code iterator} method does not implement the
     * {@code remove} method and this collection is non-empty.
     *
     * @throws UnsupportedOperationException {@inheritDoc}
     */
    // 集合中的iterator必须实现remove方法，否则抛UnsupportedOperationException
    public void clear() {
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }


    //  String conversion

    /**
     * Returns a string representation of this collection.  The string
     * representation consists of a list of the collection's elements in the
     * order they are returned by its iterator, enclosed in square brackets
     * ({@code "[]"}).  Adjacent elements are separated by the characters
     * {@code ", "} (comma and space).  Elements are converted to strings as
     * by {@link String#valueOf(Object)}.
     *
     * @return a string representation of this collection
     */
    public String toString() {
        Iterator<E> it = iterator();
        // 集合为空
        if (! it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (;;) {
            E e = it.next();
            // 这里加了个e== this的判断，主要是为了防止用户误操作给集合添加元素时，不小心添加了集合自己时，没加这个判断
            // 就会导致append调用时，又会调用集合自身的toString()，导致无限循环发生stack overflow
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }

}
