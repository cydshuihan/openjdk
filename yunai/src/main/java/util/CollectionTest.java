package util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * @ClassName CollectionTest
 * @Description
 * @Author cyd
 * @Date 2019/12/1
 * @Version 1.0
 */
public class CollectionTest {
    public static void main(String[] args){
        Collection<String> c = new ArrayList<>();
        c.add("a");
        c.add("b");
        c.add("c");
        System.out.println(c);
        Iterator<String> iterator = c.iterator();
        // 连续删除两个元素，必须按照如下进行操作。因为next()和remove()有依赖性，如果调用remove()之前没有
        // 调用next(),会抛出IllegalStateException
        iterator.next();
        iterator.remove();
        iterator.next();
        iterator.remove();
        System.out.println(c);
        testArrayCopy();
    }

    public static void testArrayCopy() {
        int[] arr1 = {1, 2, 3, 4, 5};
        int[] arr2 = Arrays.copyOf(arr1, 5);
        int[] arr3 = Arrays.copyOf(arr1, 10);
        for(int i = 0; i < arr2.length; i++)
            System.out.print(arr2[i] + " ");
        System.out.println();
        for(int i = 0; i < arr3.length; i++)
            System.out.print(arr3[i] + " ");
        System.out.println(">>>>>判断调用remove()删除对象时是否会删除list中所有该值");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("a");
        arrayList.add("a");
        arrayList.add("a");
        arrayList.remove("a");
        System.out.println(arrayList);
        arrayList.add(null);
        System.out.println(arrayList);

        System.out.println("测试数组元素类型获取");
        String[] tests = {"a", "b"};
        System.out.println(tests.getClass().getComponentType()); // class java.lang.String

        Class<char[]>  aa = (Class<char[]>) char.class.getComponentType();
        System.out.println("the componentType of the char is :" + char.class.getComponentType());
        System.out.println("the componentType of the char[] is :" + char[].class.getComponentType());
        System.out.println("the componentType of the String is :" + String.class.getComponentType());
        System.out.println("the componentType of the String[] is :" + String[].class.getComponentType());
        System.out.println("the componentType of the int is :" + int.class.getComponentType());
        System.out.println("the componentType of the int[] is :" + int[].class.getComponentType());
        System.out.println("the componentType of the Integer is :" + Integer.class.getComponentType());
        System.out.println("the componentType of the Integer[] is :" + Integer[].class.getComponentType());
        try {
            // 会报错
            char c = (char)Array.newInstance(char.class.getComponentType(), 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        char[] charArray = (char []) Array.newInstance(char[].class.getComponentType(), 100);
        System.out.println("the length of the charArray is :" + charArray.length);
        try {
            // 会报错
            String c = (String)Array.newInstance(String.class.getComponentType(), 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] strArray = (String [])Array.newInstance(String[].class.getComponentType(), 10);
        System.out.println("the length of the strArray is :" + strArray.length);


        // 测试集合toArray（T[]a）方法,当a大于集合大小时情况
        String[] test = new String[5];
        for(int i = 0; i < test.length; i++) {
            test[i] = "test";
        }
        ArrayList<String> testList = new ArrayList<>();
        testList.add("a");
        testList.add("a");
        testList.add("a");
        String[] test1 = testList.toArray(test);
        System.out.println(Arrays.toString(test1));

        int max = Integer.MAX_VALUE;
        System.out.println(max);
        max = max + 1;
        System.out.println("max=" + max + ", max-1=" + (max-1));
        long l = 2147483648L;
        System.out.println(l);

        int[] array = {1,3,5};
        int[] arrayCopy = Arrays.copyOf(array, 5);
        System.out.println(Arrays.toString(arrayCopy));
    }
}
