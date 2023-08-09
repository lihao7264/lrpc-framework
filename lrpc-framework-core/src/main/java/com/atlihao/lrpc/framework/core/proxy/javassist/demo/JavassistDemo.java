package com.atlihao.lrpc.framework.core.proxy.javassist.demo;

import javassist.*;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 10:00 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 10:00 上午
 * @Version: 1.0.0
 */
public class JavassistDemo {

    public static final String DEMO_CLASS = "com.atlihao.lrpc.framework.core.proxy.javassist.demo.Demo";

    /**
     * 在执行方法的前后插入相应的子节码内容，然后再重新构建新的实例
     *
     * @throws Exception
     */
    public static void addCodeBeforeAndAfterDemo() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get(DEMO_CLASS);
        // getMethod方法会将Object类型内置函数也获取
        // getDeclaredMethods方法只关注对象自身方法
        CtMethod[] ctMethods = ctClass.getDeclaredMethods();
        for (CtMethod ctMethod : ctMethods) {
            if ("doTest".equals(ctMethod.getName())) {
                ctMethod.insertBefore("System.out.println(\"this is before\");\n");
                ctMethod.insertAfter("System.out.println(\"this is after\");\n");
            }
        }
        Class<Demo> demoClass = ctClass.toClass();
        Demo demo = demoClass.newInstance();
        demo.doTest();
    }

    /**
     * 创建新的方法
     * public int calc(int a,int b){
     *     System.out.println("this is before");
     *     return a + b;
     * }
     *
     * @throws Exception
     */
    public static void addNewMethod() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get(DEMO_CLASS);
        CtMethod ctMethod = new CtMethod(CtClass.intType, "calc", new CtClass[]{CtClass.intType, CtClass.intType}, ctClass);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctMethod.setBody("return $1 + $2;");
        // 只要ctmethod预先设置了body属性，则接下来可以使用insertBefore函数方法去预处理
        ctMethod.insertBefore("System.out.println(\"this is before\");\n");
        ctClass.addMethod(ctMethod);
        // 加载这个类到jvm内存中
        ctClass.toClass();
        Demo demo = new Demo();
        Method method = Demo.class.getDeclaredMethod("calc", new Class[]{int.class, int.class});
        Object result = method.invoke(demo, 1, 2);
        System.out.println(result);
    }


    /**
     * 创建代理对象
     *
     * public class com.atlihao.lrpc.framework.core.proxy.javassist.NewDemoObject {
     *   private List demoList;
     *
     *   public NewDemoObject() {
     *      this.demoList = new java.util.ArrayList();
     *   }
     *   public addDemo(Demo demo) {
     *     this.demoList.add(demo);
     *     return true;
     *   }
     *   public List getDemoList() {
     *     return this.demoList;
     *   }
     * }
     * @throws Exception
     */
    public static void createProxyObject() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass newClass = classPool.makeClass("com.atlihao.lrpc.framework.core.proxy.javassist.NewDemoObject");
        newClass.setModifiers(Modifier.PUBLIC);
        CtClass listClass = classPool.get(List.class.getName());
        CtField demoField = new CtField(listClass, "demoList", newClass);
        demoField.setModifiers(Modifier.PUBLIC);
        // 设置新属性字段后，需关注 该属性的类型、名字、权限
        newClass.addField(demoField);
        // 设置构造函数
        CtConstructor ctConstructor = CtNewConstructor.make("public NewDemoObject(){" +
                "this.demoList = new java.util.ArrayList();" +
                "}", newClass);
        newClass.addConstructor(ctConstructor);

        // 新增demo对象
        CtMethod addDemoMethod = new CtMethod(CtClass.booleanType, "addDemo", new CtClass[]{classPool.get(DEMO_CLASS)}, newClass);
        addDemoMethod.setBody("{" +
                "this.demoList.add($1);return true;" +
                "}");
        addDemoMethod.setModifiers(Modifier.PUBLIC);
        newClass.addMethod(addDemoMethod);
        CtClass returnClassType = classPool.get(List.class.getName());

        // 获取demo集合
        CtMethod getDemoList = new CtMethod(returnClassType, "getDemoList", new CtClass[]{}, newClass);
        getDemoList.setBody("{" +
                "return this.demoList;" +
                "}");
        getDemoList.setModifiers(Modifier.PUBLIC);
        newClass.addMethod(getDemoList);
        Class<?> cls = newClass.toClass();
        Object result = cls.newInstance();
        for (Method declaredMethod : result.getClass().getDeclaredMethods()) {
            System.out.println(declaredMethod.getName());
            System.out.println(declaredMethod.getParameterTypes());
        }
        Method addDemoMethod1 = result.getClass().getDeclaredMethod("addDemo", Demo.class);
        addDemoMethod1.invoke(result, new Demo());
        System.out.println(cls.getName());
        Method getDemoListMethod2 = result.getClass().getDeclaredMethod("getDemoList", null);
        Object results = getDemoListMethod2.invoke(result);
        System.out.println(results);
    }


    public static void main(String[] args) throws Exception {
//        createProxyObject();
        addNewMethod();
    }

}
