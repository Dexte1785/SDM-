public class Variabletest {
    public static void main(String[] args) {
        /*一开始0
        上1
        上2下1
        上2下1
        下1
        上1
         */
        int count=0;
        count=count+1;
        count=count+2-1;
        count=count+2-1;
        count=count-1;
        count=count+1;
        System.out.println(count);
    }
}
