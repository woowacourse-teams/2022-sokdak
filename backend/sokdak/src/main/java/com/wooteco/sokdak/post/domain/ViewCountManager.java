package com.wooteco.sokdak.post.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ViewCountManager {

    private static final String DATE_LOG_DELIMITER = "&";
    private static final String DATE_AND_ID_DELIMITER = ":";
    private static final String ID_DELIMITER = "/";
    private static final int DATE_INDEX = 0;
    private static final int LOG_INDEX = 1;

    // <DATE>:1/2/3&
    public boolean isFirstAccess(String logs, Long postId) {
        Map<Integer, String> dateLogs = extractDateLogs(logs);
        int today = LocalDateTime.now().getDayOfMonth();
        String todayLog = dateLogs.get(today);
        if (Objects.isNull(todayLog)) {
            return true;
        }
        return isLogNonExist(todayLog, postId);
    }

    private Map<Integer, String> extractDateLogs(String logs) {
        Map<Integer, String> dateLogs = new HashMap<>();
        String[] logsPerDate = logs.split(DATE_LOG_DELIMITER);
        for (String logPerDate : logsPerDate) {
            dateLogs.putAll(divideDateAndLog(logPerDate));
        }
        return dateLogs;
    }

    private boolean isLogNonExist(String log, Long postId) {
        List<Long> loggedPostIds = Arrays.stream(log.split(ID_DELIMITER))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return !loggedPostIds.contains(postId);
    }

    private Map<Integer, String> divideDateAndLog(String logPerDate) {
        if (logPerDate.isEmpty()) {
            return Collections.emptyMap();
        }
        String[] dateAndLog = logPerDate.split(DATE_AND_ID_DELIMITER);
        int date = Integer.parseInt(dateAndLog[DATE_INDEX]);
        String log = dateAndLog[LOG_INDEX];
        return Map.of(date, log);
    }

    public String getUpdatedLog(String logs, Long postId) {
        if (!isFirstAccess(logs, postId)) {
            return logs;
        }
        Map<Integer, String> dateLogs = extractDateLogs(logs);
        int today = LocalDateTime.now().getDayOfMonth();
        String updatedLog = appendLog(dateLogs.get(today), postId);
        return today + DATE_AND_ID_DELIMITER + updatedLog;
    }

    private String appendLog(String log, Long postId) {
        if (Objects.isNull(log) || log.isEmpty()) {
            return Long.toString(postId);
        }
        return log + ID_DELIMITER + postId;
    }
}
