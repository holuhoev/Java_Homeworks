package holuhoev;


import java.lang.reflect.Array;
import java.math.BigInteger;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class CountSimpleNumbers implements ICountSimpleNumbers
{

    @Override
    public List countLongParallel(long N, long M, byte C)
    {
        long start = System.currentTimeMillis();

        List<Long> list = LongStream
                .rangeClosed(N, M)
                .parallel()
                .filter(i -> isLastDigit(i, C) && isPrimeNumber(i))
                .boxed()
                .collect(Collectors.toList());

        long end = System.currentTimeMillis();

        System.out.format("Parallel long: %d millis\n", (end - start));

        return list;
    }

    @Override
    public List countLong(long N, long M, byte C)
    {
        long start = System.currentTimeMillis();
        List<Long> list = LongStream
                .rangeClosed(N, M)
                .filter(i -> isLastDigit(i, C) && isPrimeNumber(i))
                .boxed()
                .collect(Collectors.toList());


        long end = System.currentTimeMillis();

        System.out.format("Consistently long: %d millis\n", (end - start));

        return list;
    }

    @Override
    public List countBigIntegerParallel(BigInteger N, BigInteger M, byte C)
    {
        long start = System.currentTimeMillis();

        System.out.println("Limit: " + Long.parseLong(M.toString()));

        List<BigInteger> list = Stream
                .iterate(N, x -> x.add(BigInteger.ONE)).limit(Long.parseLong((M.add(BigInteger.ONE).subtract(N)).toString()))
                .parallel()
                .filter(i -> isLastDigit(i, C) && isPrimeNumber(i))
                .collect(Collectors.toList());

        long end = System.currentTimeMillis();

        System.out.format("Parallel BigInteger: %d millis\n", (end - start));

        return list;
    }

    @Override
    public List countBigInteger(BigInteger N, BigInteger M, byte C)
    {
        long start = System.currentTimeMillis();


        List<BigInteger> list = Stream
                .iterate(N, x -> x = x.add(BigInteger.ONE)).limit(Long.parseLong(M.toString()))
                .filter(i -> isLastDigit(i, C) && isPrimeNumber(i))
                .collect(Collectors.toList());


        long end = System.currentTimeMillis();

        System.out.format("Consistently BigInteger: %d millis\n", (end - start));

        return list;
    }


    private boolean isLastDigit(BigInteger i, byte C)
    {
        return i.remainder(BigInteger.TEN).equals(BigInteger.valueOf(C));
    }

    private boolean isLastDigit(long i, byte C)
    {
        return i % 10 == C;
    }

    private boolean isPrimeNumber(BigInteger i)
    {
        BigInteger size = i.divide(BigInteger.valueOf(2));


        return Stream.
                iterate(BigInteger.valueOf(2), x -> x = x.add(BigInteger.ONE))
                .limit((size.subtract(BigInteger.valueOf(2)).longValue()))
                .noneMatch(x -> i.remainder(x).equals(BigInteger.ZERO));

    }

    private boolean isPrimeNumber(long i)
    {
        return LongStream.range(2, i / 2)
                .noneMatch(j -> i % j == 0);
    }
}
