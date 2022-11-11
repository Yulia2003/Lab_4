import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends JComponent {
    private BufferedImage image;
    // объявление изображения и его параметров.
    //когда ваш компонент будет включен в пользовательский интерфейс, он
    //отобразит на экране все изображение.
    public JImageDisplay(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Dimension dimension = new Dimension(width, height);
        super.setPreferredSize(dimension);
    }

    // метод отрисовки изображения.(Мы передаем значение null для ImageObserver, поскольку данная
    //функциональность не требуется.)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }

    // Очистка изображения.устанавливает все пиксели
    //изображения в черный цвет (значение RGB 0)
    public void clearImage() {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                drawPixel(i, j, 0);
            }
        }
    }

    // устанавливает пиксель в определенный цвет
    public void drawPixel(int x, int y, int rgbColor) {
        image.setRGB(x, y, rgbColor);
    }

    public BufferedImage getImage() {
        return image;
    }
}