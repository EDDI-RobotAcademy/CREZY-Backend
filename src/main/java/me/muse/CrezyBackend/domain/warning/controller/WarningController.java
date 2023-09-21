package me.muse.CrezyBackend.domain.warning.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.muse.CrezyBackend.domain.report.controller.form.ReportRegisterForm;
import me.muse.CrezyBackend.domain.warning.entity.Warning;
import me.muse.CrezyBackend.domain.warning.service.WarningService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/warning")
public class WarningController {
    final private WarningService warningService;

    @PostMapping("regist-warning")
    public Warning registWarning(@RequestHeader HttpHeaders headers, @RequestBody ReportRegisterForm requestForm){
        return warningService.registWarning(headers, requestForm);
    }

    @DeleteMapping("/delete-warning")
    public void deleteWarning(@RequestParam("warningId")Long warningId, @RequestHeader HttpHeaders headers){
        log.info("deleteWarning()");
        warningService.deleteWarning(warningId, headers);
    }
}
