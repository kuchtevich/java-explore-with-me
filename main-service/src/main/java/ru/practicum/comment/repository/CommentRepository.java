package ru.practicum.comment.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEvent(final Event event, final PageRequest pageRequest);

    List<Comment> findByAuthorAndEvent(final User author, final Event event, final PageRequest pageRequest);

    List<Comment> findAllByAuthorIdAndEventIdAndTextContainingIgnoreCase(Long authorId, Long eventId,
                                                                         String text, PageRequest request);
}
