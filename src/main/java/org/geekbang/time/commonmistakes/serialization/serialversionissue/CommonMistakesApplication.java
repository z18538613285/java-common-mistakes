package org.geekbang.time.commonmistakes.serialization.serialversionissue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootApplication
public class CommonMistakesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonMistakesApplication.class, args);
        //
        //String reskey = "123_copy_1";
        //String oldresKey = "123";
        //
        //String copy_ = reskey.split("_copy_")[0];
        //String copy_1 = oldresKey.split("_copy_")[0];
        //System.out.println(copy_1);
    }
}

