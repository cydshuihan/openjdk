package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName ListTest
 * @Description
 * @Author cyd
 * @Date 2019/12/8
 * @Version 1.0
 */
public class ListTest {
    public static void main(String[] args) {
        // 测试集合的子集合的非结构改变是否会影响集合本身
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        List<Integer> sub = list.subList(1,3);
        System.out.println("list=" + list + ",sub=" + sub);
        sub.set(0, 100);
        System.out.println("list=" + list + ",sub=" + sub); // 子集合元素改变，父集合对应位置也改变
        // 测试结构变化是否会影响集合本身
        sub.add(200);
        sub.add(300);
        System.out.println("list=" + list + ",sub=" + sub); // 子集合添加元素，父集合对应位置也改变

        // 验证实现了RandomAccess的List集合使用for循环随机访问要快于迭代器访问。
        ArrayList<Integer> array = new ArrayList<>();
        for(int i = 0; i < 1000000; i++) {
            array.add(i);
        }
        long time1 = System.currentTimeMillis();
        for(int i = 0; i < array.size(); i++) {
            Integer num = array.get(i);
        }
        System.out.println("使用for循环耗时：" + (System.currentTimeMillis() - time1));
        long time2 = System.currentTimeMillis();
        Iterator<Integer> iterator = array.iterator();
        while (iterator.hasNext()) {
            Integer num = iterator.next();
        }
        System.out.println("使用迭代器耗时：" + (System.currentTimeMillis() - time2));

        // 验证集合顺序删除时游标位置cursor最后总是指向位置0
        ArrayList<String> arr = new ArrayList<>();
        arr.add("a");
        arr.add("b");
        arr.add("c");
        Iterator<String> iter = arr.iterator();
        while (iter.hasNext()) {
            iter.next();
            iter.remove(); // 第一次删除后a后面元素左移，最后一位保持不变。elementData=[b,c,c]
        }
    }
}
