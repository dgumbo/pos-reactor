package zw.co.hisolutions.pos.reports.entity;

/**
 *
 * @author dgumbo
 */
public enum ReportColumnNumberFormat {
 
    None(""), Integer( "#" ), IntegerKilo( "#, ###" ), Number2Dec("#.00"), NumberKilo2Dec( "#, ###.00")  
    , Currency( "$#.00"), CurrencyKilo( "$#, ###.00")
    ;

    private final String value;

    ReportColumnNumberFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
