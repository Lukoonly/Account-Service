package account.api.controller;

import account.api.dto.ou.LogDTO;
import account.api.mapper.LogMapper;
import account.service.AuditorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@NoArgsConstructor
@RestController
public class AuditorController {

    AuditorService auditorService;
    LogMapper logMapper;

    @GetMapping("api/security/events")
    public List<LogDTO> getAllEvents() {
        return auditorService.getEventsList().stream()
                .map(log -> logMapper.toLogDTO(log))
                .collect(Collectors.toList());
    }
}