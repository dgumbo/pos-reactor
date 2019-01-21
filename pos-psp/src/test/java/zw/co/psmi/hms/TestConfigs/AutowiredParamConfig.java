
package zw.co.psmi.hms.TestConfigs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

//@Configuration
//@EnableWebMvc
public class AutowiredParamConfig extends WebMvcConfigurerAdapter {

    

    @Bean
    @Scope(WebApplicationContext.SCOPE_REQUEST)
    public Date date1() {
        return new Date();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public Map<String, String> map1() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("key", "value");
        return map;
    }

    private int count = 0;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ArrayList<String> list1() {
        count++;
        return Lists.newArrayList("X", String.valueOf(count));
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ImmutableList<String> list2() {
        count++;
        return ImmutableList.of("Y", String.valueOf(count));
    }

}
