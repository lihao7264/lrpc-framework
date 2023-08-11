package com.atlihao.lrpc.framework.jmh.serialize;

import com.atlihao.lrpc.framework.core.serialize.SerializeFactory;
import com.atlihao.lrpc.framework.core.serialize.fastjson.FastJsonSerializeFactory;
import com.atlihao.lrpc.framework.core.serialize.hessian.HessianSerializeFactory;
import com.atlihao.lrpc.framework.core.serialize.jdk.JdkSerializeFactory;
import com.atlihao.lrpc.framework.core.serialize.kryo.KryoSerializeFactory;
import com.atlihao.lrpc.framework.jmh.entity.User;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 8:55 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 8:55 下午
 * @Version: 1.0.0
 */
public class SerializeCompareTest {

    private static User buildUserDefault() {
        User user = new User();
        user.setAge(26);
        user.setAddress("杭州市西湖区");
        user.setBankNo(123456789L);
        user.setSex(1);
        user.setId(123456);
        user.setIdCardNo("330825199702107777");
        user.setRemark("测试");
        user.setUsername("天天帝天");
        return user;
    }

    @Benchmark
    public void jdkSerializeTest() {
        SerializeFactory serializeFactory = new JdkSerializeFactory();
        User user = buildUserDefault();
        byte[] result = serializeFactory.serialize(user);
        User deserializeUser = serializeFactory.deserialize(result, User.class);
    }

    @Benchmark
    public void hessianSerializeTest() {
        SerializeFactory serializeFactory = new HessianSerializeFactory();
        User user = buildUserDefault();
        byte[] result = serializeFactory.serialize(user);
        User deserializeUser = serializeFactory.deserialize(result, User.class);
    }

    @Benchmark
    public void fastJsonSerializeTest() {
        SerializeFactory serializeFactory = new FastJsonSerializeFactory();
        User user = buildUserDefault();
        byte[] result = serializeFactory.serialize(user);
        User deserializeUser = serializeFactory.deserialize(result, User.class);
    }

    @Benchmark
    public void kryoSerializeTest() {
        SerializeFactory serializeFactory = new KryoSerializeFactory();
        User user = buildUserDefault();
        byte[] result = serializeFactory.serialize(user);
        User deserializeUser = serializeFactory.deserialize(result, User.class);
    }

    /**
     * # JMH version: 1.21
     * # VM version: JDK 1.8.0_261, Java HotSpot(TM) 64-Bit Server VM, 25.261-b12
     * # VM invoker: /Library/Java/JavaVirtualMachines/jdk1.8.0_261.jdk/Contents/Home/jre/bin/java
     * # VM options: -Dvisualvm.id=19503923551972 -javaagent:/Applications/开发工具/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=52009:/Applications/开发工具/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8
     * # Warmup: 2 iterations, 10 s each
     * # Measurement: 5 iterations, 10 s each, 2 calls per op
     * # Timeout: 10 min per iteration
     * # Threads: 1 thread, will synchronize iterations
     * # Benchmark mode: Throughput, ops/time
     * # Benchmark: com.atlihao.lrpc.framework.jmh.serialize.SerializeCompareTest.fastJsonSerializeTest
     *
     * # Run progress: 20.00% complete, ETA 00:01:07
     * # Fork: 1 of 1
     * # Warmup Iteration   1: 504203.318 ops/s
     * # Warmup Iteration   2: 545068.947 ops/s
     * Iteration   1: 247496.444 ops/s
     * Iteration   2: 217789.949 ops/s
     * Iteration   3: 181216.177 ops/s
     * Iteration   4: 196648.300 ops/s
     * Iteration   5: 204096.864 ops/s
     *
     *
     * Result "com.atlihao.lrpc.framework.jmh.serialize.SerializeCompareTest.fastJsonSerializeTest":
     *   209449.547 ±(99.9%) 96396.450 ops/s [Average]
     *   (min, avg, max) = (181216.177, 209449.547, 247496.444), stdev = 25033.852
     *   CI (99.9%): [113053.097, 305845.997] (assumes normal distribution)
     *
     *
     * # JMH version: 1.21
     * # VM version: JDK 1.8.0_261, Java HotSpot(TM) 64-Bit Server VM, 25.261-b12
     * # VM invoker: /Library/Java/JavaVirtualMachines/jdk1.8.0_261.jdk/Contents/Home/jre/bin/java
     * # VM options: -Dvisualvm.id=19503923551972 -javaagent:/Applications/开发工具/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=52009:/Applications/开发工具/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8
     * # Warmup: 2 iterations, 10 s each
     * # Measurement: 5 iterations, 10 s each, 2 calls per op
     * # Timeout: 10 min per iteration
     * # Threads: 1 thread, will synchronize iterations
     * # Benchmark mode: Throughput, ops/time
     * # Benchmark: com.atlihao.lrpc.framework.jmh.serialize.SerializeCompareTest.hessianSerializeTest
     *
     * # Run progress: 40.00% complete, ETA 00:02:19
     * # Fork: 1 of 1
     * # Warmup Iteration   1: 147057.434 ops/s
     * # Warmup Iteration   2: 184032.252 ops/s
     * Iteration   1: 95432.629 ops/s
     * Iteration   2: 95966.206 ops/s
     * Iteration   3: 95029.013 ops/s
     * Iteration   4: 95760.755 ops/s
     * Iteration   5: 87592.928 ops/s
     *
     *
     * Result "com.atlihao.lrpc.framework.jmh.serialize.SerializeCompareTest.hessianSerializeTest":
     *   93956.306 ±(99.9%) 13765.519 ops/s [Average]
     *   (min, avg, max) = (87592.928, 93956.306, 95966.206), stdev = 3574.862
     *   CI (99.9%): [80190.787, 107721.825] (assumes normal distribution)
     *
     *
     * # JMH version: 1.21
     * # VM version: JDK 1.8.0_261, Java HotSpot(TM) 64-Bit Server VM, 25.261-b12
     * # VM invoker: /Library/Java/JavaVirtualMachines/jdk1.8.0_261.jdk/Contents/Home/jre/bin/java
     * # VM options: -Dvisualvm.id=19503923551972 -javaagent:/Applications/开发工具/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=52009:/Applications/开发工具/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8
     * # Warmup: 2 iterations, 10 s each
     * # Measurement: 5 iterations, 10 s each, 2 calls per op
     * # Timeout: 10 min per iteration
     * # Threads: 1 thread, will synchronize iterations
     * # Benchmark mode: Throughput, ops/time
     * # Benchmark: com.atlihao.lrpc.framework.jmh.serialize.SerializeCompareTest.jdkSerializeTest
     *
     * # Run progress: 60.00% complete, ETA 00:01:52
     * # Fork: 1 of 1
     * # Warmup Iteration   1: 27914.702 ops/s
     * # Warmup Iteration   2: 29797.139 ops/s
     * Iteration   1: 15031.051 ops/s
     * Iteration   2: 15175.073 ops/s
     * Iteration   3: 15720.167 ops/s
     * Iteration   4: 16289.031 ops/s
     * Iteration   5: 17529.847 ops/s
     *
     *
     * Result "com.atlihao.lrpc.framework.jmh.serialize.SerializeCompareTest.jdkSerializeTest":
     *   15949.034 ±(99.9%) 3902.593 ops/s [Average]
     *   (min, avg, max) = (15031.051, 15949.034, 17529.847), stdev = 1013.491
     *   CI (99.9%): [12046.441, 19851.627] (assumes normal distribution)
     *
     *
     * # JMH version: 1.21
     * # VM version: JDK 1.8.0_261, Java HotSpot(TM) 64-Bit Server VM, 25.261-b12
     * # VM invoker: /Library/Java/JavaVirtualMachines/jdk1.8.0_261.jdk/Contents/Home/jre/bin/java
     * # VM options: -Dvisualvm.id=19503923551972 -javaagent:/Applications/开发工具/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=52009:/Applications/开发工具/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8
     * # Warmup: 2 iterations, 10 s each
     * # Measurement: 5 iterations, 10 s each, 2 calls per op
     * # Timeout: 10 min per iteration
     * # Threads: 1 thread, will synchronize iterations
     * # Benchmark mode: Throughput, ops/time
     * # Benchmark: com.atlihao.lrpc.framework.jmh.serialize.SerializeCompareTest.kryoSerializeTest
     *
     * # Run progress: 80.00% complete, ETA 00:01:01
     * # Fork: 1 of 1
     * # Warmup Iteration   1: 310048.500 ops/s
     * # Warmup Iteration   2: 366031.248 ops/s
     * Iteration   1: 189184.074 ops/s
     * Iteration   2: 177431.715 ops/s
     * Iteration   3: 177508.055 ops/s
     * Iteration   4: 156231.102 ops/s
     * Iteration   5: 144137.285 ops/s
     *
     *
     * Result "com.atlihao.lrpc.framework.jmh.serialize.SerializeCompareTest.kryoSerializeTest":
     *   168898.446 ±(99.9%) 70268.329 ops/s [Average]
     *   (min, avg, max) = (144137.285, 168898.446, 189184.074), stdev = 18248.462
     *   CI (99.9%): [98630.117, 239166.775] (assumes normal distribution)
     *
     *
     * # Run complete. Total time: 00:05:21
     *
     * REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
     * why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
     * experiments, perform baseline and negative tests that provide experimental control, make sure
     * the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
     * Do not assume the numbers tell you what you want them to tell.
     *
     * Benchmark                                    Mode  Cnt       Score       Error  Units
     * SerializeCompareTest.fastJsonSerializeTest  thrpt    5  209449.547 ± 96396.450  ops/s
     * SerializeCompareTest.hessianSerializeTest   thrpt    5   93956.306 ± 13765.519  ops/s
     * SerializeCompareTest.jdkSerializeTest       thrpt    5   15949.034 ±  3902.593  ops/s
     * SerializeCompareTest.kryoSerializeTest      thrpt    5  168898.446 ± 70268.329  ops/s
     * @param args
     * @throws RunnerException
     */
    public static void main(String[] args) throws RunnerException {
        // 配置进行2轮热数 测试2轮 1个线程
        // 预热的原因 是JVM在代码执行多次会有优化
        Options options = new OptionsBuilder().warmupIterations(2).measurementBatchSize(2)
                .forks(1).build();
        new Runner(options).run();
    }
}
