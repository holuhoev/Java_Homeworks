package holuhoev;

import java.math.BigInteger;
import java.util.List;

public interface ICountSimpleNumbers
{
    List countLongParallel(long N, long M, byte C);

    List countLong(long N, long M, byte C);

    List countBigIntegerParallel(BigInteger N, BigInteger M, byte C);

    List countBigInteger(BigInteger N, BigInteger M, byte C);
}
