package packTest;

import packWork.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String inputFileName = readInput("Introdu numele fisierului de intrare:");
        String outputFileName = readInput("Introdu numele fisierului de iesire:");


        BMP image = ReadBMPFile.readBinaryImageBMP(inputFileName);

        if (image == null) {
            System.out.println("cannot read file input image");

            return;
        }

        ImageEditorWithMirroring imageEditor = new ImageEditorWithMirroring();
        imageEditor.setImage(image);
        imageEditor.mirrorBMP();

        image = imageEditor.getImage();

        WriteBMPFile.writeBinaryImageBMP(image, outputFileName);
    }

    public static String readInput(String message) {
        System.out.println(message);

        Scanner sc = new Scanner(System.in);

        return sc.next();
    }
}