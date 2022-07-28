package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.AnswerDto;

import java.util.List;


public interface AnswerDtoDao {
    List<AnswerDto> getAllByQuestionId(Long id);
    List<AnswerDto> getDeletedAnswersByUserId(Long id);
    Long getAmountAllAnswersByUserId(Long id);
}
