/**
 * This class contains the various encryption and decryption algorithms implemented.
 * It also counts how many messages have been encoded or decoded per scheme.
 *
 * @author Sonya Pathania
 * @version 1.0
 */
public class Encryptor {
    /**
     * The encryption scheme chosen for this Encryptor instance.
     */
    private Scheme encryptionScheme;

    /**
     * Counter for how many messages have been processed using Base-N encoding/decoding.
     */
    private static int countBaseN = 0;

    /**
     * Counter for how many messages have been processed using Caesar shift encoding/decoding.
     */
    private static int countCaesar = 0;

    /**
     * Counter for how many messages have been processed using block rotation encoding/decoding.
     */
    private static int countRotate = 0;

    /**
     * Constructor to create an Encryptor Object.
     *
     * @param es The encryption scheme provided by the client.
     */
    public Encryptor(Scheme es) {
        encryptionScheme = es;
    }

    /**
     * No argument constructor that asks user for Encryption Scheme.
     * (client must provide an encryption scheme).
     */
    public Encryptor() {
        System.out.println("The client must provide an Encryption Scheme to instantiate an Encryptor.");
    }

    /**
     * Method that encodes a string based on the scheme decided by the user.
     *
     * @param input The string to encode.
     * @param parameter The parameter needed for encryption (depends on scheme).
     * @return Returns the encoded string.
     */
    public String encode(String input, int parameter) {
        if (encryptionScheme == Scheme.BASE_N) {
            return this.encodeBaseN(input, parameter);
        } else if (encryptionScheme == Scheme.CAESAR) {
            return this.encodeCaesar(input, parameter);
        } else {
            return this.encodeRotate(input, parameter);
        }
    }

    /**
     * Encodes a string using Base-N encoding (binary or hexadecimal).
     *
     * @param input The string to encode.
     * @param base The base used (2 for binary, 16 for hexadecimal).
     * @return The encoded string in binary or hexadecimal form.
     */
    private String encodeBaseN(String input, int base) {
        String combinedString = "";
        int unicode = 0;
        String binaryHex = "";
        if (base != 2 && base != 16) {
            base = 16;
        }
        for (char c : input.toCharArray()) {
            if (base == 2) {
                unicode = (int) c;
                binaryHex = Integer.toBinaryString(unicode);
                binaryHex = String.format("%8s", binaryHex).replace(' ', '0');
                combinedString += binaryHex;
            } else {
                unicode = (int) c;
                binaryHex = Integer.toHexString(unicode);
                binaryHex = String.format("%2s", binaryHex).replace(' ', '0');
                combinedString += binaryHex;
            }
        }
        countBaseN++;
        return combinedString;
    }

    /**
     * Encodes a string using Caesar cipher shift.
     *
     * @param input The string to encode.
     * @param shift The number of positions to shift each letter.
     * @return The encoded string with shifted characters.
     */
    private String encodeCaesar(String input, int shift) {
        String finalString = "";
        int position = 0;
        char base = 0;
        int finalPos = 0;
        if (shift < 0) {
            shift = 12;
        }
        for (char c : input.toCharArray()) {
            if (!(Character.isAlphabetic(c))) {
                finalString += c;
            } else {
                if (Character.isUpperCase(c)) {
                    base = 'A';
                } else if (Character.isLowerCase(c)) {
                    base = 'a';
                }
                position = (int) c;
                position += shift;
                if (position > (base + 25)) {
                    finalPos = position % (25 + base) - 1;
                    base += finalPos;
                    finalString += (char) base;
                } else {
                    finalString += (char) position;
                }
            }
        }
        countCaesar++;
        return finalString;
    }

    /**
     * Encodes a string using block rotation on chunks of five characters.
     *
     * @param input The string to encode.
     * @param rotation The number of positions to rotate within each block.
     * @return The encoded string after rotation.
     */
    private String encodeRotate(String input, int rotation) {
        int remainder = input.length() % 5;
        String chunk = "";
        String combinedString = "";
        String newInput = input.substring(0, input.length() - remainder);
        char[] rotatedChars;
        if (rotation < 0) {
            rotation = 3;
        }
        for (int i = 0; i < newInput.length(); i += 5) {
            chunk = newInput.substring(i, i + 5);
            rotatedChars = new char[5];
            for (int k = 0; k < chunk.length(); k++) {
                int rolloverIndex = (k + rotation) % 5;
                rotatedChars[rolloverIndex] = chunk.toCharArray()[k];
            }
            String rotatedString = "";
            for (char c : rotatedChars) {
                rotatedString += c;
            }
            combinedString += rotatedString;
        }
        if (remainder != 0) {
            chunk = input.substring(input.length() - remainder);
            rotatedChars = new char[chunk.length()];
            for (int n = 0; n < chunk.length(); n++) {
                int rolloverIndex = (n + rotation) % chunk.length();
                rotatedChars[rolloverIndex] = chunk.toCharArray()[n];
            }
            for (char c : rotatedChars) {
                combinedString += c;
            }
        }
        countRotate++;
        return combinedString;
    }

    /**
     * Method that decodes a string based on the scheme decided by the user.
     *
     * @param input The string to decode.
     * @param parameter The parameter needed for decoding (depends on scheme).
     * @return The decoded string.
     */
    public String decode(String input, int parameter) {
        if (encryptionScheme == Scheme.BASE_N) {
            return this.decodeBaseN(input, parameter);
        } else if (encryptionScheme == Scheme.CAESAR) {
            return this.decodeCaesar(input, parameter);
        } else {
            return this.decodeRotate(input, parameter);
        }
    }

    /**
     * Decodes a string that was encoded with Caesar cipher.
     *
     * @param input The string to decode.
     * @param shift The shift value originally used for encoding.
     * @return The decoded string.
     */
    private String decodeCaesar(String input, int shift) {
        String finalString = "";
        int position = 0;
        char base = 0;
        int finalPos = 0;
        int newShift = 0;
        if (shift < 0) {
            shift = 12;
        }
        for (char c : input.toCharArray()) {
            if (!(Character.isAlphabetic(c))) {
                finalString += c;
            } else {
                if (Character.isUpperCase(c)) {
                    base = 'A';
                } else if (Character.isLowerCase(c)) {
                    base = 'a';
                }
                if (shift > 26) {
                    newShift = 26 - (shift % 26);
                } else {
                    newShift = 26 - shift;
                }
                position = (int) c;
                position += newShift;
                if (position > (base + 25)) {
                    finalPos = position % (25 + base) - 1;
                    base += finalPos;
                    finalString += (char) base;
                } else {
                    finalString += (char) position;
                }
            }
        }
        countCaesar++;
        return finalString;
    }

    /**
     * Decodes a string that was encoded using Base-N (binary or hexadecimal).
     *
     * @param input The encoded string.
     * @param base The base used (2 for binary, 16 for hexadecimal).
     * @return The decoded string.
     */
    private String decodeBaseN(String input, int base) {
        String eachBin = "";
        int letterNum = 0;
        String finalString = "";
        if (base == 2) {
            for (int i = 0; i < input.length(); i += 8) {
                eachBin = input.substring(i, i + 8);
                letterNum = Integer.parseInt(eachBin, 2);
                finalString += (char) letterNum;
            }
        } else {
            for (int i = 0; i < input.length(); i += 2) {
                eachBin = input.substring(i, i + 2);
                letterNum = Integer.parseInt(eachBin, 16);
                finalString += (char) letterNum;
            }
        }
        countBaseN++;
        return finalString;
    }

    /**
     * Decodes a string that was encoded with block rotation.
     *
     * @param input The encoded string.
     * @param rotation The rotation value originally used.
     * @return The decoded string.
     */
    private String decodeRotate(String input, int rotation) {
        int remainder = input.length() % 5;
        String chunk = "";
        String combinedString = "";
        String newInput = input.substring(0, input.length() - remainder);
        char[] rotatedChars;
        if (rotation < 0) {
            rotation = 3;
        }
        int newRotate = 0;
        if (rotation > 5) {
            newRotate = 5 - (rotation % 5);
        } else {
            newRotate = 5 - rotation;
        }
        for (int i = 0; i < newInput.length(); i += 5) {
            chunk = newInput.substring(i, i + 5);
            rotatedChars = new char[5];
            for (int k = 0; k < chunk.length(); k++) {
                int rolloverIndex = (k + newRotate) % 5;
                rotatedChars[rolloverIndex] = chunk.toCharArray()[k];
            }
            String rotatedString = "";
            for (char c : rotatedChars) {
                rotatedString += c;
            }
            combinedString += rotatedString;
        }
        if (remainder != 0) {
            chunk = input.substring(input.length() - remainder);
            if (rotation > chunk.length()) {
                newRotate = chunk.length() - (rotation % chunk.length());
            } else {
                newRotate = chunk.length() - rotation;
            }
            rotatedChars = new char[chunk.length()];
            for (int n = 0; n < chunk.length(); n++) {
                int rolloverIndex = (n + newRotate) % chunk.length();
                rotatedChars[rolloverIndex] = chunk.toCharArray()[n];
            }
            for (char c : rotatedChars) {
                combinedString += c;
            }
        }
        countRotate++;
        return combinedString;
    }

    /**
     * Method returns a String that includes the class’ three counter values separated by spaces.
     *
     * @return Returns a String with the class's three counter values separated by spaces.
     */
    public static String encryptionTallyString() {
        return "Base-n count: " + countBaseN +
                "\n" + "Caesar count: "
                + countCaesar + "\n" + "Block " +
                "rotation count: " + countRotate;
    }
}