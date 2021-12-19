package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitmapTest {

    @Test
    void getBitmap() {
        Bitmap bitmap = new Bitmap(8);
        bitmap.add(0);
        bitmap.delete(0);

//        System.out.println(bitmap.getLength());

        for (int i = 0; i < bitmap.getLength(); i++) {
            System.out.println(bitmap.get(i));
        }

//        for (int i = 0; i < bitmap.getBitmap().length; i++) {
//            System.out.println(bitmap.getBitmap()[i]);
//        }
    }
}