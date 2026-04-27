package tn.esprit.disasteraidbackend.alert.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.alert.dto.RuleExecutionResult;
import tn.esprit.disasteraidbackend.alert.entity.AlertRule;
import tn.esprit.disasteraidbackend.alert.service.RuleEngineService;
import tn.esprit.disasteraidbackend.common.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class RuleEngineController {

    private final RuleEngineService ruleService;

    public RuleEngineController(RuleEngineService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping
    public ApiResponse<List<AlertRule>> getAll() {
        return ApiResponse.ok(ruleService.getAllRules());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AlertRule>> create(@RequestBody AlertRule rule) {
        AlertRule created = ruleService.createRule(rule);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Rule created", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AlertRule>> update(@PathVariable Integer id, @RequestBody AlertRule rule) {
        AlertRule updated = ruleService.updateRule(id, rule);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Rule not found"));
        }
        return ResponseEntity.ok(ApiResponse.ok("Rule updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        if (!ruleService.deleteRule(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Rule not found"));
        }
        return ResponseEntity.ok(ApiResponse.ok("Rule deleted", null));
    }

    @PostMapping("/evaluate")
    public ApiResponse<List<RuleExecutionResult>> evaluate() {
        return ApiResponse.ok(ruleService.evaluateRules());
    }
}
