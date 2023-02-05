/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.util;

import java.util.ArrayList;
/**
 * Класс проверки контрольной суммы ИНН для России с помощью Java.
 * @autor Oleg Chirukhin
 * @version 2.0
 */
public class InnChecker {
    public static final Integer[] MULT_N1 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    public static final Integer[] MULT_N2 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    public static final Integer[] MULT_N =  {2, 4, 10, 3, 5, 9, 4, 6, 8};

    public static boolean checkInn(String innStr) {
        Boolean valid;
        Integer[] inn = stringToIntArray(innStr);

        Integer innSize = inn.length;

        switch (innSize) {
            case 12:
                Integer N1 = getChecksum(inn,MULT_N1);
                Integer N2 = getChecksum(inn,MULT_N2);

                valid = (inn[inn.length-1].equals(N2) && inn[inn.length-2].equals(N1));
                break;
            case 10:
                Integer N = getChecksum(inn,MULT_N);
                valid = (inn[inn.length-1].equals(N));
                break;
            default:
                valid = false;
                break;
        }
        return valid;
    }

    public static Integer[] stringToIntArray(String src) {
        char[] chars = src.toCharArray();
        ArrayList<Integer> digits = new ArrayList<Integer>();
        for (char aChar : chars) {
            digits.add(Character.getNumericValue(aChar));
        }
        return digits.toArray(new Integer[digits.size()]);
    }

    public static Integer getChecksum(Integer[] digits, Integer[] multipliers) {
        int checksum = 0;
        for (int i=0; i<multipliers.length; i++) {
            checksum+=(digits[i]*multipliers[i]);
        }
        return (checksum % 11) % 10;
    }
}
