package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.entity.user.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user/chat")
@Api("GroupChat Api")
public class ChatResourceController {

    @GetMapping("/group")
    @ApiOperation("Возращает все сообщения как объект класса PageDto<GroupChatDto> с учетом заданных параметров пагинации.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Получены все сообщения с учетом заданных параметров пагинации."),
            @ApiResponse(code = 400, message = "Необходимо ввести обязательный параметр: номер страницы."),
            @ApiResponse(code = 500, message = "Страницы под номером page=* пока не существует")
    })
    public ResponseEntity<GroupChatDto> getGroupChatOutPutWithAllMessage(
            @RequestParam("currentPage") Integer currentPage,
            @RequestParam(value = "items") Integer items)
    {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Map<String, Object> params = new HashMap<>();
        params.put("currentPageNumber", currentPage);
        params.put("itemsOnPage", items);
        params.put("userId", userId);
        params.put("trackedTag", null);
        params.put("ignoredTag", null);

        return null;
    }
}
