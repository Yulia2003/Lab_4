import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {

    public static final int MAX_ITERATIONS = 2000;
//    метод позволяет генератору фракталов определить наиболее «интересную» область комплексной плоскости
//для конкретного фрактала
    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.height = 3;
        range.width = 3;
    }
//реализует итеративную функцию для фрактала Мандельброта
    @Override
    public int numIterations(double x, double y) {
        double r = 0;
        double i = 0;
        int numIters = 0;
        /**
         * Вычисляем Zn = Zn-1 ^ 2 + c, где значения представляют собой комплексные числа, представленные
         * по zreal и zimaginary, Z0 = 0, а c - особая точка в
         * фрактал, который мы показываем (заданный x и y). Это повторяется
         * до Z ^ 2> 4 (абсолютное значение Z больше 2) или максимум
         * достигнуто количество итераций.
         */
        while (numIters < MAX_ITERATIONS) {
            numIters++;
            //(a+ib)^2 = a*a + 2*a*b*i + i*b*i*b =
            //= a*a + 2*a*b*i - b*b
            double k = (r) * (r) - i*i+x;//мнимое
            double m = (2) * r * i + y; //действительное число
            r = k;
            i = m;
            if (r*r+i*i > 4)//|z|>2
                break;
        }
        //нужно вернуть -1, чтобы показать, что точка не выходит за границы.
        if (numIters == MAX_ITERATIONS)
            return -1;
        return numIters;
    }
    @Override
    public String toString() {
        return "Mandelbrot";
    }
}

