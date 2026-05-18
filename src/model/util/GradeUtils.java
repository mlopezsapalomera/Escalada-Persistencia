package model.util;

import java.util.Arrays;
import java.util.List;

public class GradeUtils {
    public static final List<String> ORDER = Arrays.asList(
            "4","4+","5","5+","6a","6a+","6b","6b+","6c","6c+",
            "7a","7a+","7b","7b+","7c","7c+","8a","8a+","8b","8b+",
            "8c","8c+","9a","9a+","9b","9b+","9c","9c+"
    );

    public static int rank(String grau) {
        if (grau == null) return -1;
        String g = grau.trim().toLowerCase().replaceAll("\\s+", "");
        return ORDER.indexOf(g);
    }

    public static boolean isValid(String grau) {
        return rank(grau) != -1;
    }

    public static boolean lessOrEqual(String g1, String g2) {
        int r1 = rank(g1);
        int r2 = rank(g2);
        if (r1 == -1 || r2 == -1) return false;
        return r1 <= r2;
    }
}
