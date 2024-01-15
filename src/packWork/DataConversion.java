package packWork;

public class DataConversion {
    public int convertBytesToInt(byte[] byteArray, int offset, int length) {
        int result = 0;

        for (int i = length - 1; i >= 0; i--) {
            result <<= 8;
            result += byteArray[offset + i] & 0xFF;
        }

        return result;
    }

    public void intToByteArray(byte[] byteArray, int offset, int value, int length) {
        for (int i = 0; i < length; i++) {
            byteArray[offset + i] = (byte) (value & 0xFF);
            value >>= 8;
        }
    }

    public void intToByteArray(byte[] byteArray, int offset, int value) {
        intToByteArray(byteArray, offset, value, 4);
    }

    public void intToByteArray(byte[] byteArray, int value) {
        intToByteArray(byteArray, 0, value, 4);
    }
}