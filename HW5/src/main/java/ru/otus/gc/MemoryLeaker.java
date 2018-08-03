package ru.otus.gc;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class MemoryLeaker {

    private int byteSize;


    public MemoryLeaker(int byteSize){
        this.byteSize=byteSize;
    }

    @SuppressWarnings({"InfiniteLoopStatement", "MismatchedQueryAndUpdateOfCollection"})
    public void leak(){
        Map<BadKey,Integer> map = new HashMap<BadKey, Integer>();
        int i=0;
        while (true) {

            BadKey key = new BadKey(new byte[byteSize]);
            map.put(key, i);
            //           System.out.println(map.get(key));
            i++;
        }
    }



    public class BadKey {

        private final byte[] bytes;

        public BadKey(byte[] bytes){this.bytes = bytes;}

        @Override
        public int hashCode() {
            return 5;
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }
}
