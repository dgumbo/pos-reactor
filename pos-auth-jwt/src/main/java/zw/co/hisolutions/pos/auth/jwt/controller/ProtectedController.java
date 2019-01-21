package zw.co.hisolutions.pos.auth.jwt.controller;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import zw.co.hisolutions.pos.auth.jwt.entity.ResponseWrapper;

@RestController
@RequestMapping("protected")
public class ProtectedController {

    @RequestMapping(method = GET)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseWrapper getDaHoney() {

        // fake class just for testing purpose
        @AllArgsConstructor
        @Data
        class SensitiveInformation {
            String foo;
            String bar;

//            public SensitiveInformation(String foo, String bar) {
//               this. foo=foo;
//               this. bar=bar;
//            }
        }

        return new ResponseWrapper(new SensitiveInformation("ABCD", "XYZ"), null, null);
    }


}
