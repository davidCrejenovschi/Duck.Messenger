package entities.pairs;


import entities.pairs.abstracts.AbstractPair;

public class Friendship extends AbstractPair<Long, Long> {

    long id;

    public Friendship(Long left_init, Long right_init) {
        left = left_init;
        right = right_init;
        id = buildId();
    }

    private long buildId() {
        String idStr;
        if (left.compareTo(right) >= 0) {
            idStr = left + "0" + right;
        } else {
            idStr = right + "0" + left;
        }
        return Long.parseLong(idStr);
    }


    public long getId() {
        return id;
    }

}
