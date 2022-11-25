import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

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
    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(600);
        fractalExplorer.setGUI();
        fractalExplorer.drawFractal();
    }

    // задание интерфейса
    public void setGUI() {
        /** для frame использование java.awt.BorderLayout для своего содержимого. **/
        JFrame frame = new JFrame("Fractal Generator");
        JButton buttonReset = new JButton("Reset");
        JButton buttonSave = new JButton("Save image");
        JPanel jPanel_1 = new JPanel();
        JPanel jPanel_2 = new JPanel();
        JLabel label = new JLabel("Fractal:");

        imageDisplay = new JImageDisplay(displaySize, displaySize);
        imageDisplay.addMouseListener(new MouseListener());

        // выпадающий список
        comboBox = new JComboBox();
        comboBox.addItem(new Mandelbrot());
        comboBox.addItem(new Tricorn());
        comboBox.addItem(new BurningShip());
        comboBox.addActionListener(new ActionHandler());

        // кнопка reset
        buttonReset.setActionCommand("Reset");
        buttonReset.addActionListener(new ActionHandler());

        // кнопка сохранить
        buttonSave.setActionCommand("Save");
        buttonSave.addActionListener(new ActionHandler());

        jPanel_1.add(label, BorderLayout.CENTER);
        jPanel_1.add(comboBox, BorderLayout.CENTER);
        jPanel_2.add(buttonReset, BorderLayout.CENTER);
        jPanel_2.add(buttonSave, BorderLayout.CENTER);

        frame.setLayout(new BorderLayout());
        frame.add(imageDisplay, BorderLayout.CENTER);
        frame.add(jPanel_1, BorderLayout.NORTH);
        frame.add(jPanel_2, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    // отрисовка фрактала в JImageDisplay
    private void drawFractal() {
        for (int x = 0; x < displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {
                int numIters = fractalGenerator.numIterations(FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, x),
                        fractalGenerator.getCoord(range.y, range.y + range.width, displaySize, y));
                if (numIters == -1) {
                    imageDisplay.drawPixel(x, y, 0);
                }
                else {
                    float hue = 0.7f + (float) numIters / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    imageDisplay.drawPixel(x, y, rgbColor);
                }
            }
        }
        imageDisplay.repaint();
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
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("PNG Images", "png");
                fileChooser.setFileFilter(fileFilter);
                fileChooser.setAcceptAllFileFilterUsed(false);
                int t = fileChooser.showSaveDialog(imageDisplay);
                if (t == JFileChooser.APPROVE_OPTION) {
                    try {
                        ImageIO.write(imageDisplay.getImage(), "png", fileChooser.getSelectedFile());
                    } catch (NullPointerException | IOException ee) {
                        JOptionPane.showMessageDialog(imageDisplay, ee.getMessage(), "Cannot save image", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
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
}