package sk.actplus.slime.version2;

/**
 * Created by Ja on 19.2.2018.
 */

public class MyMath {
    public static float getSmallestNumber(float [] number) {

        float smallest = number[0];
        for (int i = 1; i < number.length-1; i++) {
            if(smallest>number[i]) smallest = number[i];
        }
        return smallest;
    }

    public static float getBiggestNumber(float [] number) {

        float biggest = number[0];
        for (int i = 1; i < number.length-1; i++) {
            if(biggest<number[i]) biggest = number[i];
        }
        return biggest;
    }
}
