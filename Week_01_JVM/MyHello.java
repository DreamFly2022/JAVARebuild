public class MyHello{

    public static void main(String[] args){
        int a = 2;
        int b = 7;
        int c = 6;
        int r = 0;
        for (int i=0; i<c;i++){
            if (a>b){
                r = r+b;
                a ++;
            }else{
                r = r + a;
            }
        }
        System.out.println(r);
    }
}