package me.muse.CrezyBackend.utility;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class RandomValue {

    public int[] randomValue(int size){
        Random random = new Random();
        int[] numArray = new int[10];
        for(int i=0; i<10; i++){
            int number = random.nextInt(size);
            log.info("random number: {}", number);
            numArray[i] = number;

            for(int j=0; j<i; j++){
                if(numArray[i] == numArray[j]){
                    i--;
                    break;
                }
            }
        }
        return numArray;
    }
}
