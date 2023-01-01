import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.geom.Rectangle2D;
public class FractalExplorer {
    /** Целочисленный размер отображения - это ширина и высота отображения в пикселях. **/
    private int displaySize;
    /**
     * Ссылка JImageDisplay для обновления отображения с помощью различных методов как
     * фрактал вычислен.
     */
    private JImageDisplay imageDisplay;
    /** Объект FractalGenerator для каждого типа фрактала. **/
    private FractalGenerator fractalGenerator;
    /**
     * Объект Rectangle2D.Double, который определяет диапазон
     * то, что мы в настоящее время показываем.
     */
    private Rectangle2D.Double range;
//по нажатию которой необходимо вернуть выбранный из списка элемент.
// В качестве элементов JComboBox там содержится список элементов класса Item,
// к котором определено некое поле private String name.
    private JComboBox comboBox;

//    6 lab
    private int rowsRemaining;
    private JButton buttonReset;
    private JButton buttonSave;

    private FractalExplorer (int displaySize) {
        /** Размер дисплея  **/
        this.displaySize = displaySize;
        /** Инициализирует фрактальный генератор и объекты диапазона. **/
        this.fractalGenerator = new Mandelbrot();
        this.range = new Rectangle2D.Double(0,0,0,0);
        fractalGenerator.getInitialRange(this.range);
    }

    // точка входа
    /**
     * Статический метод main() для запуска FractalExplorer. Инициализирует новый
     * Экземпляр FractalExplorer с размером дисплея 600, вызывает
     * setGUI() в объекте проводника, а затем вызывает
     * drawFractal() в проводнике, чтобы увидеть исходный вид.
     */
// точка входа
    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(600);
        fractalExplorer.setGUI();
        fractalExplorer.drawFractal();
    }

    // задание интерфейса
    public void setGUI() {
        // для frame использование java.awt.BorderLayout для своего содержимого. **/
        JFrame frame = new JFrame("Fractal Generator");
//        JButton buttonReset = new JButton("Reset");
//        JButton buttonSave = new JButton("Save image");
        JPanel jPanel_1 = new JPanel();
        JPanel jPanel_2 = new JPanel();
        JLabel label = new JLabel("Fractal:");

        imageDisplay = new JImageDisplay(displaySize, displaySize);
        /** Экземпляр MouseHandler в компоненте фрактального отображения. **/
        imageDisplay.addMouseListener(new MouseListener());

        // выпадающий список
        comboBox = new JComboBox();
        comboBox.addItem(new Mandelbrot());
        comboBox.addItem(new Tricorn());
        comboBox.addItem(new BurningShip());
        comboBox.addActionListener(new ActionHandler());

        // кнопка reset
        buttonReset = new JButton("Reset");//6
        buttonReset.setActionCommand("Reset");
        buttonReset.addActionListener(new ActionHandler());

        // кнопка сохранить
        buttonSave = new JButton("Save image"); //
        buttonSave.setActionCommand("Save");
        buttonSave.addActionListener(new ActionHandler());
        //добавить реализации  генератора фракталов
        jPanel_1.add(label, BorderLayout.CENTER);
        jPanel_1.add(comboBox, BorderLayout.CENTER);
        jPanel_2.add(buttonReset, BorderLayout.CENTER);
        jPanel_2.add(buttonSave, BorderLayout.CENTER);

        //окно верхнего уровня (не содержащееся в другом окне)
        //заносит в макет макет границы;
        frame.setLayout(new BorderLayout());
        frame.add(imageDisplay, BorderLayout.CENTER);
        frame.add(jPanel_1, BorderLayout.NORTH);
        frame.add(jPanel_2, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Размер рамки
        frame.pack();
        //показывет ее
        frame.setVisible(true);
        //уст изменения
        frame.setResizable(false);
    }

    // отрисовка фрактала в JImageDisplay 6
//    запустит фоновый поток и
//    запустит задачу в фоновом режиме
    private void drawFractal() {
        // отключаем интерфейс на момент рисования
        enableGUI(false);
        rowsRemaining = displaySize;
        for (int i = 0; i < displaySize; i++) {
            FractalWorker drawRow = new FractalWorker(i);
            drawRow.execute();
        }
    }

    // включение - отключение gui 6
    public void enableGUI(boolean b) {
        buttonSave.setEnabled(b);
        buttonReset.setEnabled(b);
        comboBox.setEnabled(b);
    }

//    обработчик кнопок
//    Внутренний класс для обработки событий ActionListener.

    public class ActionHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            /**
             * Если источником является кнопка сброса, сбросьте дисплей и нарисуйте
             * фрактал.
             */
            if (e.getActionCommand().equals("Reset")) {
                // перерисовка фрактала
                fractalGenerator.getInitialRange(range);
                drawFractal();
            } else if (e.getActionCommand().equals("Save")) {
                // сохранение
                //Средство выбора файлов,чтобы
                //сохранять изображения только в формате PNG
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("PNG Images", "png");
                fileChooser.setFileFilter(fileFilter);
                //Последняя строка гарантирует, что средство выбора не разрешит
                //пользователю использование отличных от png форматов.
                fileChooser.setAcceptAllFileFilterUsed(false);

                //возможность указания пользователем, в какой файл он будет сохранять изображение.
                int t = fileChooser.showSaveDialog(imageDisplay);
                if (t == JFileChooser.APPROVE_OPTION) {

                    try {
                        //возвращает объект типа File
                        //обеспечивает простые операции загрузки и сохранения
                        //изображения
                        ImageIO.write(imageDisplay.getImage(), "png", fileChooser.getSelectedFile());
                        //проинформировать пользователя об ошибке через диалоговое окно
                    } catch (NullPointerException | IOException ee) {
                        JOptionPane.showMessageDialog(imageDisplay, ee.getMessage(), "Cannot save image", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
            //если событие поступило от выпадающего списка, вы можете извлечь выбранный элемент из виджета
            //и установить его в качестве текущего генератора фракталов
                fractalGenerator = (FractalGenerator) comboBox.getSelectedItem();
                range = new Rectangle2D.Double(0,0,0,0);
                fractalGenerator.getInitialRange(range);
                drawFractal();
            }
        }
    }
    /**
     * Внутренний класс для обработки событий MouseListener с дисплея.
     */

    public class MouseListener extends MouseAdapter {
        /**
         * Когда обработчик получает событие щелчка мыши, он отображает пиксель-
         * координаты щелчка в области фрактала, который
         * отображается, а затем вызывает функцию RecenterAndZoomRange () генератора
         * метод с координатами, по которым был выполнен щелчок, и шкалой 0,5.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            /** Получаем координату x области отображения щелчка мыши. **/
            double x = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, e.getX());
            /** Получаем координату y области отображения щелчка мышью. **/
            double y = FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, e.getY());
            /**
             * Вызывааем метод генератора RecenterAndZoomRange() с помощью
             * координаты, по которым был выполнен щелчок, и шкала 0,5.
             */
            fractalGenerator.recenterAndZoomRange(range, x, y, 0.5);
            drawFractal();
        }
    }
    //вычисление значений цвета для одной строки фрактала
    public class FractalWorker extends SwingWorker<Object, Object> { //6
        private int y_coord; //целочисленная y координата вычисляемой строки
        private int[] rgb;//массив чисел типа int для хранения
                          // вычисленных значений RGB для каждого пикселя в этой строке

        public FractalWorker(int y_coord) {
            this.y_coord = y_coord;
        }
    //Bызывается в фоновом потоке и отвечает за выполнение длительной задачи
        @Override
        protected Object doInBackground() throws Exception { // 6
            rgb = new int[displaySize];
            for (int i = 0; i < displaySize; i++) { //цикл долже будет сохранить каждое значение RGB в соответствующем элементе
//                целочисленного массива
                int count = fractalGenerator.numIterations(FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, i),
                        FractalGenerator.getCoord(range.y, range.y+range.width, displaySize, y_coord));
                if (count == -1)
                    rgb[i] = 0;
                else {
                    double hue = 0.7f + (float) count / 200f;
                    int rgbColor = Color.HSBtoRGB((float) hue, 1f, 1f);
                    rgb[i] = rgbColor;
                }
            }
            return null;
        }
// вызывается, когда фоновая задача завершена
        @Override
        protected void done() { //6
            //перебирать массив строк данных, рисуя пиксели, которые
            //были вычислены в doInBackground ().
            for (int i = 0; i < displaySize; i++) {
                imageDisplay.drawPixel(i, y_coord, rgb[i]);
            }
            imageDisplay.repaint(0,0,y_coord,displaySize,1);
            rowsRemaining--;
            if (rowsRemaining == 0)
                enableGUI(true);
        }
    }
}
