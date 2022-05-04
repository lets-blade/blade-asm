package com.hellokaton.blade.asm;

import junit.framework.TestCase;

import java.lang.reflect.Method;

/**
 * @author: chenchen_839@126.com
 * @date: 2018/7/18
 */
public class SameMethodTest extends TestCase{

    public void testSame() throws NoSuchMethodException {
        MethodAccess access = MethodAccess.get(SameMethodTest.Demo.class);
        Demo demo = new Demo();
        Method method = demo.getClass().getMethod("register",String.class);
        Object result = null;
        long start = System.currentTimeMillis();
        for (int i = 0;i<=100000000;i++) {
            result = access.invoke(demo,"register",new Class[]{Integer.class},i);
        }
        long end = System.currentTimeMillis();
        for (int i = 0;i<=100000000;i++) {
            result = access.invoke(demo, method, new Class[]{Integer.class}, i);
        }
        long fina = System.currentTimeMillis();

        System.out.println(end-start);
        System.out.println(fina - end);
    }

    public class Demo{
        public String register(String name) {
            return name;
        }
        public String register(Integer name) {
            return name.toString();
        }
    }
}
