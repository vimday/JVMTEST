/**
 * 加载方法不等于执行方法，初始化变量则会赋值
 *             类加载顺序应为 加载静态方法-初始化静态变量-执行静态代码块
 *             实例化时 先加载非静态方法-实例化非静态变量-执行构造代码块-执行构造函数
    1.首先加载所有的静态方法但不执行，即print方法
    2.public static int k = 0;
    3.其次按照静态变量的顺序开始初始化，从上面往下，到第二个时public static LoadAndExecOrderTest t1 = new LoadAndExecOrderTest(“t1”);
    这个地方new了一个实例，所以开始实例化：（int型静态变量初值默认为0）
    （1）加载非静态方法
    （2）初始化非静态变量，即 public int j = print(“j”); //1:j i=0 n=0
    （3）然后执行构造代码块 //2:构造快 i=1 n=1
    （4）然后是执行构造函数 //3:t1 i=2 n=2
    4.继续加载类到public static LoadAndExecOrderTest t2 = new LoadAndExecOrderTest(“t2”);
    这个地方new了一个实例，所以开始实例化：
    （1）加载非静态方法
    （2）初始化非静态变量，即 public int j = print(“j”); //4:j i=3 n=3
    （3）然后执行构造代码块 //5:构造快 i=4 n=4
    （4）然后是执行构造函数 //6:t2 i=5 n=5
    5.继续类加载到 public static int i = print(“i”); //7:i i=6 n=6
    6.继续加载，public static int n = 99;
    7.继续加载到静态代码块：
    static{
    print(“静态块”); //8:静态块 i=7 n=99
    }
    8.类加载完毕，开始执行主函数。主函数中： LoadAndExecOrderTest t = new LoadAndExecOrderTest(“init”);
    这个地方new了一个实例，所以开始实例化：
    （1）加载非静态方法
    （2）初始化非静态变量，即 public int j = print(“j”); //9:j i=8 n=100
    （3）然后执行构造代码块 //10:构造快 i=9 n=101
    （4）然后是执行构造函数 //11:init i=10 n=102
 *
 *
 */
public class LoadAndExecOrderTest {
    /**第一个加载*/
    public static int k = 0;
    /**第二个加载，因为是new一个实例，
     * 首先初始化j 打印出  1:j i=0 n=0
     * 执行构造块     打印出  2:构造快 i=1 n=1
     * 执行构造方法 打印出  3:t1 i=2 n=2
     * 实例化完成
     */
    public static LoadAndExecOrderTest t1 = new LoadAndExecOrderTest("t1");
    /**第三个加载 过程同上
     * 首先初始化j 打印出  4:j i=3 n=3
     * 执行构造块     打印出  5:构造快 i=4 n=4
     * 执行构造方法 打印出  6:t2 i=5 n=5
     */
    public static LoadAndExecOrderTest t2 = new LoadAndExecOrderTest("t2");
    /**第四个加载
     * 打印出  7:i i=6 n=6
     */
    public static int i = print("i");
    /**
     * 第五个加载
     */
    public static int n = 99;
    /**
     * 此变量在类加载的时候并不初始化，在实例化类的时候初始化（构造块，实例化的时候执行）
     */
    public int j = print("j");

    {
        print("构造快");
    }
    /**
     * 第六个加载 此时，n已经被初始化  所以打印出
     * 8:静态块 i=7 n=99
     * （静态块类加载的时候加载，随着类的加载而加载，只加载一次）
     */
    static{
        print("静态块");
    }
    //-----------以上属于类加载---------------------
    /**
     * 实例化过程：
     *         首先加载非静态方法集；
     *         初始化非静态变量：9:j i=8 n=100
     *         执行构造块：10:构造快 i=9 n=101
     *         执行构造方法:11:init i=10 n=102
     * 实例化完成
     */

    /**
     * 执行构造函数  实例化完成
     * @param str
     */
    public LoadAndExecOrderTest(String str) {
        System.out.println((++k) + ":" + str + " i=" + i + " n=" + n);
        ++n;
        ++i;

    }
    /**
     * 这个应该是最先加载 但是，加载不等于执行
     * 因为如果不加载此函数，静态变量是无法初始化的
     * @param str
     * @return
     */
    public static int print(String str) {
        System.out.println((++k) + ":" + str + " i=" + i + " n=" + n);
        ++i;

        return ++n;
    }

    public static void main(String[] args) {
        /**首先加载类，然后实例化：
         * 类加载过程：
         *         首先加载所有的静态方法，但不执行；
         *         然后按照静态变量的顺序开始初始化
         *         静态变量初始化完毕后执行静态构造块（不执行构造块）
         *         此时类加载完毕
         * 实例化过程：
         *         加载非静态方法
         *         初始化非静态变量
         *         执行构造代码块
         *         执行构造函数
         *         此时实例化完毕
         */
         LoadAndExecOrderTest t = new LoadAndExecOrderTest("init");
    }
    static{
        print("静态块2");
        //类加载完即执行完所有的静态块后才会进入main函数;
    }

}
