
package zw.co.hisolutions.pos.reports.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnknownMatchException extends RuntimeException {
    public UnknownMatchException(String matchId) {
        super("Unknown match: " + matchId);
    }
}
