
public class AutoConversion {
    public static void main(String[] args) {

        int a = 6;
        // int 类型可以自动转换为 float 类型
        float f = a;
        // 下面将输出 6.0
        System.out.println(f);
        // 定义一个byte 类型的整数变量
        byte b = 9;
        // 下面代码将出错， byte类型不能自动类型转换为 char类型
// char c = b;
// byte 类型变量可以自动类型转换为 double 类型
        double d = b;
// 下面将输出 9.0
        System.out.println(d);
    }

}
