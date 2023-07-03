package spw4.game2048;

import java.util.Iterator;
import java.util.Random;

public class RandomStub<T> extends Random {
    private Iterator<T> iterator;

    public RandomStub(Iterable<T> values){
        iterator = values.iterator();
    }

    @Override
    public int nextInt(){
        return (int)iterator.next();
    }

    @Override
    public int nextInt(int ignored){
        return nextInt();
    }

    @Override
    public double nextDouble(){
        return (double)iterator.next();
    }
}
