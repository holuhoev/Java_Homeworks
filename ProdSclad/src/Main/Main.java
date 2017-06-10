package Main;
import java.util.Scanner;
public class Main {

    private enum parametrs{}
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        scanner.reset();
        int N = 0, M = 0, p = 0;
        float t1 = 0, t2 = 0, T1 = 0, T2 = 0;
        String str ="";
        while (str != "end") {
            System.out.println("Введите данные, N = " + N+ "; M = " +M+ "; t1 = " +t1+ " ; t2 = " +t2+ "; T1 = " + T1+ "; T2 = " +T2+  "; p = " + p + "\nВыберите параметр :");
                scanner.reset();
                str = scanner.nextLine();
                switch (str){
                    case "N":
                        System.out.println("\nВведите N :");
                        if (scanner.hasNextInt())
                            N = scanner.nextInt();
                        break;
                    case "M":
                        System.out.println("\nВведите M :");
                        if (scanner.hasNextInt())
                            M = scanner.nextInt();
                        break;
                    case "t1":
                        System.out.println("\nВведите t1 :");
                        if (scanner.hasNextInt())
                            t1 = scanner.nextFloat();
                        break;
                    case "t2":
                        System.out.println("\nВведите t2 :");
                        if (scanner.hasNextInt())
                            t2 = scanner.nextFloat();
                        break;
                    case "T1":
                        System.out.println("\nВведите T1 :");
                        if (scanner.hasNextInt())
                            T1 = scanner.nextFloat();
                        break;
                    case "T2":
                        System.out.println("\nВведите T2 :");
                        if (scanner.hasNextFloat())
                            T2 = scanner.nextFloat();
                        break;
                    case "p":
                        System.out.println("\nВведите p :");
                        if (scanner.hasNextInt())
                            p = scanner.nextInt();
                        break;
                    case "end":
                        System.out.println("Стартуем со следующеми параметрами: N = " + N+ "; M = " +M+ "; t1 = " +t1+ " ; t2 = " +t2+ "; T1 = " + T1+ "; T2 = " +T2+  "; p = " + p);
                        break;
                    default:
                        System.out.println("\nНеверный ввод! Повторите");
                        break;
                }
            System.out.println();

        }
        StoreHouse storeHouse = new StoreHouse(N, M, t1, t2, T1, T2, p);
        storeHouse.start();
    }
}
