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
        double r = x;
        double i = y;
        int numIters = 0;
        while (numIters < MAX_ITERATIONS) {
            numIters++;
            double k = r * r - i*i+x;
            double m = 2 * r * i + y;
            r = k;
            i = m;
            if (r*r+i*i > 4)
                break;
        }
        //нужно вернуть -1, чтобы показать, что точка не выходит за границы.
        if (numIters == MAX_ITERATIONS)
            return -1;
        return numIters;
    }
}

