package holuhoev;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            if (args.length != 4)
                throw new IllegalArgumentException("Неверный формат данных: количество аргументов должно быть 4\n в виде: \"1 60 9 result.txt\"");

            //parse Long
            long N = Long.parseLong(args[0]);
            long M = Long.parseLong(args[1]);

            byte C = Byte.parseByte(args[2]);

            String fileName = args[3];

            if (N < 0 | M < 0)
                throw new IllegalArgumentException("N и M  должны быть положительными целыми числами");

            if (N >= M)
                throw new IllegalArgumentException("N должно быть строго меньше M");

            if (C < 0 || C >= 10)
                throw new IllegalArgumentException("C должно быть цифрой");

            CountSimpleNumbers counter = new CountSimpleNumbers();

            Scanner scanner = new Scanner(System.in);


            while (true)
            {
                System.out.format("N: %d | M: %d | C: %d | Output file: %s \n\n",N,M,C,fileName);
                System.out.println("Чтобы выйти из программы введите \"q\"");
                System.out.println("Выполнять параллельно или последовательно? Использовать BigInteger или long?" +
                        "\n ( par_long / con_long / par_big / con_big )");
                List list = null;
                String line = scanner.nextLine();

                if (line.equals("q"))
                    break;

                if (line.equals("par_long"))
                {
                    list = counter.countLongParallel(N,M,C);
                }
                if (line.equals("con_long"))
                {
                    list = counter.countLong(N,M,C);
                }
                if (line.equals("par_big"))
                {
                    BigInteger Nbig = new BigInteger(args[0]);
                    BigInteger Mbig = new BigInteger(args[1]);

                    list = counter.countBigIntegerParallel(Nbig,Mbig,C);
                }
                if (line.equals("con_big"))
                {
                    BigInteger Nbig = new BigInteger(args[0]);
                    BigInteger Mbig = new BigInteger(args[1]);

                    list = counter.countBigInteger(Nbig,Mbig,C);
                }

                if(list != null)
                {
                    writeResultsToFile(list,fileName);
                }
            }

        } catch (NumberFormatException ex)
        {
            System.out.println("Неверный формат данных! Аргументы должны быть в виде: \"1 60 9 result.txt\"");
        } catch (IllegalArgumentException ex)
        {
            System.out.println(ex.getMessage());
        }

    }

    private static void writeResultsToFile(List list, String fileName)
    {
        PrintWriter writer;

        try
        {
            writer = new PrintWriter(new File("").getAbsolutePath() + "/" + fileName);
        } catch (Exception ex)
        {
            System.out.println("Error: can't write to file.");
            return;
        }

        writer.print(list.size() + ":<");


        list.forEach(i -> writer.print(i + ", "));
        writer.print(">");

        System.out.println("\n");
        writer.flush();
        writer.close();
    }
}
