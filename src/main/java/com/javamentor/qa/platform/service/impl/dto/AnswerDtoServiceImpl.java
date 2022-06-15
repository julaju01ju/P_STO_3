package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.AnswerDtoDao;
import com.javamentor.qa.platform.models.dto.AnswerDto;
import com.javamentor.qa.platform.models.entity.question.answer.Answer;
import com.javamentor.qa.platform.service.abstracts.dto.AnswerDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerDtoServiceImpl implements AnswerDtoService {

    private AnswerDtoDao answerDto;

    @Autowired
    public AnswerDtoServiceImpl(AnswerDtoDao answerDto){
        this.answerDto = answerDto;
    }

    public AnswerDtoServiceImpl() {

    }

    @Override
    public List<AnswerDto> getAllAnswersByQuestionId(Long id) {
        return answerDto.getAllByQuestionId(id);
    }

    @Override
    public List<AnswerDto> getDeletedAnswersByUserId(Long id) {
        return answerDto.getDeletedAnswersByUserId(id);
    }

    @Override
    public Long getAmountAllAnswersByUserId(Long id) {
        return answerDto.getAmountAllAnswersByUserId(id);
    }
}
