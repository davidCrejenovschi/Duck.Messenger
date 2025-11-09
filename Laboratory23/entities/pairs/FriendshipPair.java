package entities.pairs;


public class FriendshipPair extends AbstractPair<Long, Long> {

    long id;

    public FriendshipPair(Long left_init, Long right_init) {
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
