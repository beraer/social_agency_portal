package myproject.spektif_agency_application.service.impl;

import lombok.RequiredArgsConstructor;
import myproject.spektif_agency_application.dto.CommentDTO;
import myproject.spektif_agency_application.mapper.CommentMapper;
import myproject.spektif_agency_application.model.Card;
import myproject.spektif_agency_application.model.Comment;
import myproject.spektif_agency_application.model.User;
import myproject.spektif_agency_application.repository.CardRepository;
import myproject.spektif_agency_application.repository.CommentRepository;
import myproject.spektif_agency_application.repository.UserRepository;
import myproject.spektif_agency_application.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CommentDTO addComment(Long cardId, String content, String username) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCard(card);
        comment.setUser(user);
        
        comment = commentRepository.save(comment);
        return CommentMapper.toDTO(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
} 