package dev.memocode.application.question.service;

import dev.memocode.application.core.PageResponse;
import dev.memocode.application.question.converter.QuestionDTOConverter;
import dev.memocode.application.question.dto.*;
import dev.memocode.application.question.repository.QuestionRepository;
import dev.memocode.application.question.repository.SearchQuestionRepository;
import dev.memocode.application.question.usecase.QuestionUseCase;
import dev.memocode.application.tag.InternalTagService;
import dev.memocode.application.user.InternalUserService;
import dev.memocode.domain.question.CreateQuestionDomainDTO;
import dev.memocode.domain.question.Question;
import dev.memocode.domain.question.QuestionDomainService;
import dev.memocode.domain.question.UpdateQuestionDomainDTO;
import dev.memocode.domain.question.immutable.ImmutableQuestion;
import dev.memocode.domain.tag.Tag;
import dev.memocode.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService implements QuestionUseCase {

    private final InternalUserService internalUserService;
    private final InternalTagService internalTagService;
    private final InternalQuestionService internalQuestionService;

    private final QuestionDomainService questionDomainService;

    private final QuestionRepository questionRepository;
    private final SearchQuestionRepository searchQuestionRepository;

    private final QuestionDTOConverter questionDTOConverter;

    @Override
    public UUID createQuestion(CreateQuestionRequest request) {
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());
        Set<Tag> tags = request.getTags().stream()
                .map(internalTagService::createTagOrGetTag)
                .collect(Collectors.toSet());

        CreateQuestionDomainDTO dto = CreateQuestionDomainDTO.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .tags(tags)
                .build();

        Question question = questionDomainService.createQuestion(user, dto);
        questionRepository.save(question);

        return question.getId();
    }

    @Override
    public void updateQuestion(UpdateQuestionRequest request) {
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());
        Set<Tag> tags = request.getTags().stream()
                .map(internalTagService::createTagOrGetTag)
                .collect(Collectors.toSet());
        Question question = internalQuestionService.findByIdElseThrow(request.getQuestionId());

        UpdateQuestionDomainDTO dto = UpdateQuestionDomainDTO.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .tags(tags)
                .build();

        questionDomainService.updateQuestion(question, user, dto);
    }

    @Override
    public void deleteQuestion(DeleteQuestionRequest request) {
        User user = internalUserService.findByIdEnabledUserElseThrow(request.getUserId());
        Question question = internalQuestionService.findByIdElseThrow(request.getQuestionId());

        questionDomainService.deleteQuestion(question, user);
    }

    @Override
    public FindQuestion_QuestionResult findQuestion(UUID questionId) {
        Question question = internalQuestionService.findByIdElseThrow(questionId);

        Question validatedQuestion = questionDomainService.findQuestion(question);

        return questionDTOConverter.toFindQuestion_QuestionResult(validatedQuestion);
    }

    @Override
    public PageResponse<SearchQuestion_QuestionResult> searchQuestion(SearchQuestionRequest request) {
        Page<ImmutableQuestion> page =
                searchQuestionRepository.searchQuestion(request.getKeyword(), request.getPage(), request.getPageSize());

        List<ImmutableQuestion> validatedQuestions = questionDomainService.searchQuestion(page.getContent());

        return PageResponse.<SearchQuestion_QuestionResult>builder()
                .page(page.getNumber())
                .pageSize(page.getSize())
                .totalCount(page.getTotalElements())
                .content(questionDTOConverter.toSearchQuestion_QuestionResult(validatedQuestions))
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
