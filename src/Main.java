import java.util.ArrayList;
import java.util.Arrays;

public class Main {


    public static void main(String[] args) {
        int[] array = {1, 2, 3, 10, -12, 4, 85, 5};

        int max = 0;
        int maxValue = array[0];
        int min = 0;
        int minValues = array[0];

        for (int i = 0; i < array.length; i++) {
            if (array[i] > max) {
                array[i] = maxValue;
                max = i;
            }
        }

        System.out.println(max);

        for (int i = 0; i < array.length; i++) {
            if (array[i] < minValues) {
                array[i] = minValues;
                min = i;
            }
        }

        System.out.println(min);

        Arrays.sort(array);

        int min1 = array[0];
        int max1 = array[array.length - 1];

        System.out.println("min " + min1 + " max " + max1);
        // алгоритм по нахождению минимального и максимального элемента в массиве


        int[] ar = {1, 2, 3, 4, 12, 50, 100, 7, 16, 25, 23, 5, 6, 7, 90, 22};
        Arrays.sort(ar);

        int index = binarySearch(ar, 1);
        System.out.println(index);

    }



    public static int binarySearch(int[] ar, int element) {
        int left = 0;
        int right = ar.length - 1;

        while (left <= right) {
            int middle = left + (right - left) / 2;
            int current = ar[middle];

            if (current == element) {
                return middle;
            } else if (current < element) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return 1;
    }
}


