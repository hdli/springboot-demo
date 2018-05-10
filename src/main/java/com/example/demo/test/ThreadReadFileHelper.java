package com.example.demo.test;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程读取文件
 * Created by hdli on 2018-5-9.
 */
public class ThreadReadFileHelper {

    // 模拟测试数据
    private static void writeData() throws FileNotFoundException, IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\yanmengya\\Desktop\\file.txt");
        Random random = new Random();
        System.out.println("开始");
        for (int n = 0; n < 1000000; n++){
            int count = random.nextInt(10)+1;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < count; i++){
                builder.append(UUID.randomUUID().toString());
            }
            builder.append("\n");
            fileOutputStream.write(builder.toString().getBytes());
        }
        fileOutputStream.close();
        System.out.println("ok");
    }

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        start();

    }

    public static void start() throws Exception{
        long beginTime = System.currentTimeMillis();
        ThreadReadFileHelper helper = new ThreadReadFileHelper();
        helper.read("C:\\Users\\yanmengya\\Desktop\\file.txt", Runtime.getRuntime().availableProcessors(), '\n', new StringCallback("UTF-8") {
            @Override
            void callback(String data) {
                int count = atomicInteger.incrementAndGet();
                System.out.println(count);
                System.out.println(data);
                if (count == 100000) {
                    System.out.println("总耗时毫秒：" + (System.currentTimeMillis() - beginTime));
                }
            }
        });
    }

    public void read(String path, int threadCount, char separator, StringCallback callback) throws IOException {
        if (threadCount < 1) {
            throw new InvalidParameterException("The threadCount can not be less than 1");
        }
        if (callback == null) {
            throw new InvalidParameterException("The callback can not be null");
        }
        //RandomAccessFile操作文件就像操作一个byte数组一样,可以通过一个文件指针(就像数组的索引),来实现从文件随机位置读写
        //"r"　　 只读模式，如果对文件进行写操作，会报IOException错误
        //"rw"　　读写模式,如果文件不存在,将会创建文件
        //"rws"　 同步读写模式,任何对文件的修改(内容和元数据),都会立即被同步到底层存储设备ddd
        //"rwｄ"  同步读写模式,任何对文件的修改(内容),都会立即被同步到底层存储设备
        RandomAccessFile randomAccessFile = new RandomAccessFile(path, "r");

        long fileTotalLength = randomAccessFile.length();
        long gap = fileTotalLength / threadCount;
        long checkIndex = 0;
        long[] beginIndexs = new long[threadCount];
        long[] endIndexs = new long[threadCount];

        for (int n = 0; n < threadCount; n++) {
            beginIndexs[n] = checkIndex;
            if (n + 1 == threadCount) {
                endIndexs[n] = fileTotalLength;
                break;
            }
            checkIndex += gap;
            long gapToEof = getGapToEof(checkIndex, randomAccessFile, separator);

            checkIndex += gapToEof;
            endIndexs[n] = checkIndex;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        executorService.execute(() -> {
            try {
                readData(beginIndexs[0], endIndexs[0], path, randomAccessFile, separator, callback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        for (int n = 1; n < threadCount; n++) {
            long begin = beginIndexs[n];
            long end = endIndexs[n];
            executorService.execute(() -> {
                try {
                    readData(begin, end, path, null, separator, callback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }


    }

    private long getGapToEof(long beginIndex, RandomAccessFile randomAccessFile, char separator) throws IOException {
        randomAccessFile.seek(beginIndex);
        long count = 0;

        while (randomAccessFile.read() != separator) {
            count++;
        }

        count++;

        return count;
    }

    private void readData(long begin, long end, String path, RandomAccessFile randomAccessFile, char separator, StringCallback callback) throws FileNotFoundException, IOException {
        System.out.println("开始工作" + Thread.currentThread().getName());
        if (randomAccessFile == null) {
            randomAccessFile = new RandomAccessFile(path, "r");
        }

        randomAccessFile.seek(begin);
        StringBuilder builder = new StringBuilder();

        while (true) {
            int read = randomAccessFile.read();
            begin++;
            if (separator == read) {
                if (callback != null) {
                    callback.callback0(builder.toString());
                }
                builder = new StringBuilder();
            } else {
                builder.append((char) read);
            }

            if (begin >= end) {
                break;
            }
        }
        randomAccessFile.close();
    }


    public static abstract class StringCallback {
        private String charsetName;
        private ExecutorService executorService = Executors.newSingleThreadExecutor();

        public StringCallback(String charsetName) {
            this.charsetName = charsetName;
        }

        private void callback0(String data) {
            executorService.execute(() -> {
                try {
                    callback(new String(data.getBytes("ISO-8859-1"), charsetName));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });

        }

        abstract void callback(String data);
    }
}
