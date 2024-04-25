package dev.memocode.application.question.service;

import dev.memocode.application.question.converter.QuestionCommentDTOConverter;
import dev.memocode.application.question.dto.*;
import dev.memocode.application.question.repository.QuestionCommentRepository;
import dev.memocode.application.question.usecase.QuestionCommentUseCase;
import dev.memocode.application.user.InternalUserService;
import dev.memocode.domain.core.NotFoundException;
import dev.memocode.domain.question.*;
import dev.memocode.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static dev.memocode.domain.question.QuestionDomainErrorCode.QUESTION_COMMENT_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionCommentService implements QuestionCommentUseCase {

    private final QuestionCommentDomainService questionCommentDomainService;

    private final InternalUserService internalUserService;
    private final InternalQuestionService internalQuestionService;

    private final QuestionCommentRepository questionCommentRepository;

    private final QuestionCommentDTOConverter questionCommentDTOConverter;

    @Override
    @Transactional
    public UUID createQuestionComment(CreateQuestionCommentRequest request) {
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());
        Question question = internalQuestionService.findByIdElseThrow(request.getQuestionId());

        CreateQuestionCommentDomainDTO dto = CreateQuestionCommentDomainDTO.builder()
                .content(request.getContent())
                .build();
        QuestionComment questionComment = questionCommentDomainService.createQuestionComment(question, user, dto);

        QuestionComment savedQuestionComment = questionCommentRepository.save(questionComment);
        return savedQuestionComment.getId();
    }

    @Override
    @Transactional
    public void updateQuestionComment(UpdateQuestionCommentRequest request) {
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());
        Question question = internalQuestionService.findByIdElseThrow(request.getQuestionId());
        QuestionComment questionComment = this.findByIdElseThrow(request.getQuestionCommentId());

        UpdateQuestionCommentDomainDTO dto = UpdateQuestionCommentDomainDTO.builder()
                .content(request.getContent())
                .build();
        questionCommentDomainService.updateQuestionComment(question, questionComment, user, dto);
    }

    @Override
    @Transactional
    public void deleteQuestionComment(DeleteQuestionCommentRequest request) {
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());
        Question question = internalQuestionService.findByIdElseThrow(request.getQuestionId());
        QuestionComment questionComment = this.findByIdElseThrow(request.getQuestionCommentId());

        questionCommentDomainService.deleteMemoComment(question, questionComment, user);
    }

    @Override
    @Transactional
    public UUID createChildQuestionComment(CreateChildQuestionCommentRequest request) {
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());
        Question question = internalQuestionService.findByIdElseThrow(request.getQuestionId());
        QuestionComment questionComment = this.findByIdElseThrow(request.getQuestionCommentId());

        CreateQuestionCommentDomainDTO dto = CreateQuestionCommentDomainDTO.builder()
                .content(request.getContent())
                .build();

        QuestionComment childMemoComment = questionCommentDomainService.createChildMemoComment(question, questionComment, user, dto);
        QuestionComment savedChildMemoComment = questionCommentRepository.save(childMemoComment);

        return savedChildMemoComment.getId();
    }

    @Override
    public List<FindAllQuestionComment_QuestionCommentResult> findAllQuestionComment(UUID questionId) {
        Question question = internalQuestionService.findByIdElseThrow(questionId);

        List<QuestionComment> questionComments = questionCommentDomainService.findAll(question);

        return questionCommentDTOConverter.toResult(questionComments);
    }

    private QuestionComment findByIdElseThrow(UUID questionCommentId) {
        return questionCommentRepository.findById(questionCommentId)
                .orElseThrow(() -> new NotFoundException(QUESTION_COMMENT_NOT_FOUND));
    }
}
