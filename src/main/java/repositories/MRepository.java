package repositories;
import entities.messages.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MRepository extends Repository<Message>{

    List<Message> getConversationBefore(long userId, long friendId, LocalDateTime before, int pageSize);

}
