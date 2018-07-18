package org.xi.quick.utils.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

public class CaptchaUtils {

    /**
     * 使用到Algerian字体，系统里没有的话需要安装字体
     */
    public static final String CAPTCHA_CODES = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static Random random = new Random();

    /**
     * 使用系统默认字符源生成验证码
     *
     * @param captchaSize 验证码长度
     * @return
     */
    public static String generateCaptcha(int captchaSize) {
        return generateCaptcha(captchaSize, CAPTCHA_CODES);
    }

    /**
     * 使用指定源生成验证码
     *
     * @param captchaSize 验证码长度
     * @param sources     验证码字符源
     * @return
     */
    public static String generateCaptcha(int captchaSize, String sources) {

        if (sources == null || sources.length() == 0) {
            sources = CAPTCHA_CODES;
        }
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder captcha = new StringBuilder(captchaSize);
        for (int i = 0; i < captchaSize; i++) {
            captcha.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return captcha.toString();
    }

    /**
     * 输出随机验证码图片流,并返回验证码值
     *
     * @param width
     * @param height
     * @param outputStream
     * @param captchaSize
     * @return
     * @throws IOException
     */
    public static String outputCaptchaImage(int width, int height, OutputStream outputStream, int captchaSize) throws IOException {
        String captcha = generateCaptcha(captchaSize);
        outputImage(width, height, outputStream, captcha);
        return captcha;
    }

    /**
     * 输出指定验证码图片流
     *
     * @param width
     * @param height
     * @param outputStream
     * @param code
     * @throws IOException
     */
    public static void outputImage(int width, int height, OutputStream outputStream, String code) throws IOException {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        Color color = getRandColor(200, 250);
        graphics2D.setColor(color);
        graphics2D.fillRect(0, 0, width, height);

        // 绘制干扰线
        int lineWidth = (int) (height * 0.05f);
        if (lineWidth == 0) lineWidth = 1;
        for (int i = 0; i < 20; i++) {
            if (i == 10) shear(graphics2D, width, height, color);// 使图片扭曲
            graphics2D.setColor(getRandColor(160, 220));
            graphics2D.setStroke(new BasicStroke(random.nextFloat() * lineWidth));
            graphics2D.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
        }

        int fontSize = height - (lineWidth << 1);
        Font font = new Font("Algerian", Font.ITALIC, fontSize);
        graphics2D.setFont(font);
        char[] chars = code.toCharArray();
        int captchaSize = code.length();
        int width2 = width - lineWidth * 3;
        int step = width2 / captchaSize;
        for (int i = 0, positionX = lineWidth, positionY = fontSize - lineWidth; i < captchaSize; i++, positionX += step) {
            graphics2D.setColor(getRandColor(100, 160));
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * random.nextDouble() * (random.nextBoolean() ? 1 : -1), (width / captchaSize) * i + fontSize / 2, height / 2);
            // 设置旋转角度
            graphics2D.setTransform(affine);
            graphics2D.drawChars(chars, i, 1, positionX, positionY);
        }

        // 添加噪点
        float noiseRate = 0.05f;// 噪声率
        int area = (int) (noiseRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }

        graphics2D.dispose();
        ImageIO.write(image, "jpg", outputStream);
    }

    private static Color getRandColor(int fc, int bc) {
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int bound = bc - fc;
        int r = fc + random.nextInt(bound);
        int g = fc + random.nextInt(bound);
        int b = fc + random.nextInt(bound);
        return new Color(r, g, b);
    }

    private static int getRandomIntColor() {
        int color = 0;
        // RGB 为3个数字
        for (int i = 0; i < 3; i++) {
            color <<= 8;
            color |= random.nextInt(255);
        }
        return color;
    }

    private static void shear(Graphics graphics, int width, int height, Color color) {

        int period = random.nextInt(width / 3);
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < width; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                    + (Math.PI * 2 * (double) phase)
                    / (double) frames);
            graphics.copyArea(i, 0, 1, height, 0, (int) d);
            graphics.setColor(color);
            graphics.drawLine(i, (int) d, i, 0);
            graphics.drawLine(i, (int) d + height, i, height);
        }
    }
}
