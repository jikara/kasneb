/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jikara
 */
public class DigitUtil {

    public static List<Integer> split(Integer number) {
        List<Integer> digits = new ArrayList<>();
        while (number > 0) {
            digits.add(number % 10);
            number /= 10;
        }
        return digits;
    }
}
