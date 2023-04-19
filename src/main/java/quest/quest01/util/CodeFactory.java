package quest.quest01.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class CodeFactory {

    public static int page = 0;

    public static String itemCodeFactory(){
        Random random = new Random();
        String newCode = "";

        while(newCode.length() < 9) {
            newCode += random.nextInt(10);
        }
        log.info("createdItemCode = [{}]", newCode);
        return newCode;
    }

    public static String storeCodeFactory() {
        Random random = new Random();
        String newCode = "";

        while (newCode.length() < 6) {
            int i = random.nextInt(58) + 'A';
            if (i > 'Z' && i < 'a') {
                continue;
            } else {
                newCode += (char) i;
            }
        }

        log.info("createdStoreCode = {}", newCode);
        return newCode;
    }
}
