 import java.awt.geom.Rectangle2D;

 public class Tricorn extends FractalGenerator {

     public static final int MAX_ITERATIONS = 2000;
     //    метод позволяет генератору фракталов определить наиболее «интересную» область комплексной плоскости
//для конкретного фрактала
     @Override
     public void getInitialRange(Rectangle2D.Double range) {
         range.x = -2;
         range.y = -2;
         range.height = 4;
         range.width = 4;
     }
     //реализует итеративную функцию для фрактала Мандельброта
     @Override
     public int numIterations(double x, double y) {
         /** Начните с итераций с 0. */
         int numIters = 0;
         /** Инициализируйте реальное и мнимое. */
         double zreal = 0;
         double zimaginary = 0;
         /**
          * Вычисляем Zn = Zn-1 ^ 2 + c, где значения представляют собой комплексные числа, представленные
          * по zreal и zimaginary, Z0 = 0, а c - особая точка в
          * фрактал, который мы показываем (заданный x и y). Это повторяется
          * до Z ^ 2> 4 (абсолютное значение Z больше 2) или максимум
          * достигнуто количество итераций.
          */
         while ((numIters < MAX_ITERATIONS) && (zreal * zreal + zimaginary * zimaginary < 4)) {
             double zrealUpdated = zreal * zreal - zimaginary * zimaginary + x;
             double zimaginaryUpdated = (-2) * zreal * zimaginary + y;
             zreal = zrealUpdated;
             zimaginary = zimaginaryUpdated;
             numIters ++;

         }

         /**
          * Если количество максимальных итераций достигнуто, возвращаем -1, чтобы
          * указать, что точка не вышла за границу.
          */
            if (numIters == MAX_ITERATIONS)
         {
             return -1;
         }

            return numIters;
         }
     @Override
     public String toString() {
         return "Tricorn";
     }
 }