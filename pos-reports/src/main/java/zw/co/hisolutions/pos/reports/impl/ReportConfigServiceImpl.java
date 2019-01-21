package zw.co.hisolutions.pos.reports.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List; 
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.stereotype.Service; 
import zw.co.hisolutions.pos.reports.dao.ReportConfigDao;
import zw.co.hisolutions.pos.reports.dao.ReportConfigParameterDao;
import zw.co.hisolutions.pos.reports.entity.ParameterHolder;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;
import zw.co.hisolutions.pos.reports.entity.ReportConfigParameter;
import zw.co.hisolutions.pos.reports.service.ReportConfigService;

@Service
public class ReportConfigServiceImpl implements ReportConfigService {

    private final ReportConfigDao reportConfigDao;
    private final ReportConfigParameterDao reportConfigParameterDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ReportConfigServiceImpl(ReportConfigDao reportConfigDao, ReportConfigParameterDao reportConfigParameterDao) {
        this.reportConfigDao = reportConfigDao;
        this.reportConfigParameterDao = reportConfigParameterDao;
    }

    @Override
    public JpaRepository<ReportConfig, Long> getDao() {
        return reportConfigDao;
    }

    /**
     *
     * @param reportConfig
     * @param list
     * @return
     */ 
    @Override
    public ReportConfig save(ReportConfig reportConfig, List<ReportConfigParameter> list) {

        //List<String> columns = extractColumnsFromSqlStatement(reportConfig.getNativeQuery());
        List<String> columns = Arrays.asList(reportConfig.getColumns());
        String reportColumns = "";
        for (String column : columns) {
            //System.out.println(column); 
            reportColumns += reportColumns.equals("") ? column : ", " + column;
        }
        reportConfig.setColumns(reportColumns);

        reportConfig = reportConfigDao.save(reportConfig);

        List<ReportConfigParameter> oldReportConfigParameters = getParametersByReportConfig(reportConfig);

        if (list == null) {
            list = new ArrayList<>();
        }

        /* Report Config Params Added when no params where defined */
        if (!list.isEmpty() && oldReportConfigParameters.isEmpty()) {
//            System.out.println("Report Config Params Added when no params where defined");
            list.forEach(parameter -> {
//                parameter.setReportConfig(reportConfig);
            });
        }

        /* All Report Config Params Removed when they previously existed. */
        if (list.isEmpty() && !oldReportConfigParameters.isEmpty()) {
//            System.out.println("Report Config Params Removed when they previously existed.");
            oldReportConfigParameters.forEach(currentParameter -> {
                currentParameter.setActiveStatus(false);
                reportConfigParameterDao.save(currentParameter);
            });
        }

        /* Existing Report Config Params Modified, Added or Removed */
        if (!list.isEmpty() && !oldReportConfigParameters.isEmpty()) {
//            System.out.println("Existing Report Config Params Modified, Added or Removed");
//            System.out.println("Existing Report Config Params : " + oldReportConfigParameters.size());
//            System.out.println("New Report Config Params : " + list.size());
            oldReportConfigParameters.stream().forEach(currentParameter -> {
                currentParameter.setActiveStatus(false);
                //reportConfigParameterDao.save(currentParameter) ;
            });

            list.forEach(parameter -> {
                if (oldReportConfigParameters.stream().anyMatch(p -> p.getId() == parameter.getId())) {
                    ReportConfigParameter existingParam = oldReportConfigParameters.stream().filter(p -> p.getId() == parameter.getId()).findFirst().get();
                    existingParam.setActiveStatus(true);
                    existingParam.setColumnName(parameter.getColumnName());
                    existingParam.setName(parameter.getName());
                    existingParam.setParameter(parameter.getParameter());
                    existingParam.setSelectSql(parameter.getSelectSql());
                    existingParam.setSelectValueField(parameter.getSelectValueField());
                    existingParam.setParameterType(parameter.getParameterType());
                    existingParam.setHtmlElements(parameter.getHtmlElements());
                } else {
                    parameter.setActiveStatus(true);
//                    parameter.setReportConfig(reportConfig);
                    oldReportConfigParameters.add(parameter);
                }

            });

//            reportConfigParameterDao.save(oldReportConfigParameters);
            list = oldReportConfigParameters;
        }

//        reportConfigParameterDao.save(list);
//        System.out.println("\nCurrent Position 9 \n");
        return reportConfig;
    }
 
    
    @Override
    public List<ReportConfigParameter> getAllReportConfigParameters() {
        return reportConfigParameterDao.findAll();
    }

    @Override
    public List<ReportConfigParameter> getParametersByReportConfig(ReportConfig reportConfig, ParameterHolder holder) {
        Long reportConfigId = reportConfig.getId();
        List<ReportConfigParameter> reportConfigParameters = reportConfigParameterDao.getByReportConfigIdAndActiveStatusTrue(reportConfigId);
        reportConfigParameters.stream().forEach(line -> {
            line.setHtmlElements(this.preProcess(line, line.getParameter(), this.getDataValue(holder, line.getParameter())));
        });

        return reportConfigParameters;
    }

    @Override
    public List<ReportConfigParameter> getParametersByReportConfig(ReportConfig reportConfig) {
        return getParametersByReportConfig(reportConfig.getId());
    }

    @Override
    public List<ReportConfigParameter> getParametersByReportConfig(Long reportConfigId) {
        List<ReportConfigParameter> reportConfigParameters = reportConfigParameterDao.getByReportConfigIdAndActiveStatusTrue(reportConfigId);
        return reportConfigParameters;
    }

    @Override
    public List<ReportConfigParameter> getParametersByReportConfig(String reportConfigName) {
        return reportConfigParameterDao.getByReportConfigNameAndActiveStatusTrue(reportConfigName);
    }

    @Override
    public List< String> getReportConfigParameterOptions(Long reportConfigParameterId) {
        ReportConfigParameter reportConfigParameter = reportConfigParameterDao.getById(reportConfigParameterId);

        Query nQuery = entityManager.createNativeQuery(reportConfigParameter.getSelectSql());

        //UserLogin user = getCurrentAuditor();
        //String unitCode = (user != null && user.getUnitCode() != null) ? user.getUnitCode() : "";

//        if ((reportConfigParameter.getSelectSql()).contains(":unitCode")) {
//            nQuery.setParameter("unitCode", unitCode);
//        } else if ((reportConfigParameter.getSelectSql()).contains(":UnitCode")) {
//            nQuery.setParameter("UnitCode", unitCode);
//        }

        return nQuery.getResultList();
    }

    @Override
    public ReportConfig getByName(String name) {
        List<ReportConfig> list = reportConfigDao.findByNameAndActiveStatusTrue(name);
        return list.isEmpty() ? null : list.get(0);
    }

    public String preProcess(ReportConfigParameter reportConfigParameter, String parameter, String parameterValue) {
        String inputElements = "";
        switch (reportConfigParameter.getParameterType()) {
            case DATE_RANGE_INCLUSIVE:
                parameterValue = parameterValue == null ? getCurrDate(reportConfigParameter) : parameterValue;
                inputElements = "<label for='" + parameter + "' class='control-label col-sm-2' style='text-align: right;'>" + reportConfigParameter.getName() + " : </label>";
                inputElements += "<div class='col-sm-4'><input type='text' name='" + parameter + "' id='startDate' class='form-control'  value='" + parameterValue + "' /></div>";
                inputElements += "<div class='col-sm-1'><i class='fa fa-window-minimize'></i></div>";
                inputElements += "<div class='col-sm-4'><input type='text' name='" + parameter + "_' id='endDate' class='form-control' value='" + parameterValue + "' /></div>";
                break;
            case DATE_RANGE_EXCLUSIVE:
                parameterValue = parameterValue == null ? getCurrDate(reportConfigParameter) : parameterValue;
                inputElements = "<label for='" + parameter + "' class='control-label col-sm-3' style='text-align: right;'>" + reportConfigParameter.getName() + " : </label><div class='col col-sm-9'><input type='text' name='" + parameter + "' id='startDate' class='form-control hasDatepicker'  value='" + parameterValue + "' /></div>";
                inputElements += "<label for='" + parameter + "' class='control-label col-sm-3' style='text-align: right;'>End Date : </label><div class='col col-sm-9'><input type='text' name='" + parameter + "_' id='endDate' class='form-control hasDatepicker' value='" + parameterValue + "' /></div>";
                break;
            case SINGLE_DATE:
                parameterValue = parameterValue == null ? getCurrDate(reportConfigParameter) : parameterValue;
                inputElements = "<label for='" + parameter + "' class='control-label col-sm-3' style='text-align: right;'>" + reportConfigParameter.getName() + " : </label><div class='col col-sm-9'><input type='text' id='" + parameter + "' name='" + parameter + "' class='form-control' value='" + parameterValue + "' /></div>";
                break;
            case SINGLE_DATE_TIME:
                parameterValue = parameterValue == null ? getCurrDateTime(reportConfigParameter) : parameterValue;
                inputElements = "<label for='" + parameter + "' class='control-label col-sm-3' style='text-align: right;'>" + reportConfigParameter.getName() + " : </label><div class='col col-sm-9'><input type='text' id='" + parameter + "' name='" + parameter + "' class='form-control' value='" + parameterValue + "' /></div>";
                break;
            case STRING_LIKE:
                inputElements = "<label for='" + parameter + "' class='control-label col-sm-3' style='text-align: right;'>" + reportConfigParameter.getName() + " : </label><div class='col col-sm-9'><input type='text' name='" + parameter + "' class='form-control' value='" + (parameterValue == null ? "" : parameterValue) + "' /></div>";
                break;
            case NUMERIC:
                inputElements = "<label for='" + parameter + "' class='control-label col-sm-3' style='text-align: right;'>" + reportConfigParameter.getName() + " : </label><div class='col col-sm-9'><input type='text' name='" + parameter + "' class='form-control' value='" + parameterValue + "' /></div>";
                break;
            case BOOLEAN:
                inputElements = "<label for='" + parameter + "' class='control-label col-sm-3' style='text-align: right;'>" + reportConfigParameter.getName() + " : </label><div class='col col-sm-9'><select name='" + parameter + "' class='form-control'>";

                inputElements += "<option value=''>Choose " + reportConfigParameter.getName() + ".</option>";

//                System.out.println("All ______________ -1");
//                System.out.println("True ______________ 1");
//                System.out.println("False ______________ 0");
                inputElements += "<option value='1' " + (parameterValue.equals("1") ? "selected='selected'" : "") + " >True</option>";
                inputElements += "<option value='0' " + (parameterValue.equals("0") ? "selected='selected'" : "") + " >False</option>";
                inputElements += "<option value='-1'>All</option>";

                inputElements += "</select></div>";
                break;
            case SELECT_STATEMENT:
                //  para.add(i, Utils.parseDateDayEnd(parameters.get(i), config.getDefaultValue()));
                inputElements = "<label for='" + parameter + "' class='control-label col-sm-3' style='text-align: right;'>" + reportConfigParameter.getName() + "</label>";
                inputElements += "<div class='col col-sm-6'>";
                inputElements += "<select name='" + parameter + "' id='" + parameter + "' style='width:95%;' class='form-control' required='required' placeholder='Select " + reportConfigParameter.getName() + ".' > ";
                Query nQuery = entityManager.createNativeQuery(reportConfigParameter.getSelectSql());
                //UserLogin user = getCurrentAuditor();
//                System.out.println("User : " + user);
//                if (user != null && user.getUnitCode() != null) {
//                    if ((reportConfigParameter.getSelectSql()).contains(":unitCode")) {
//                        nQuery.setParameter("unitCode", user.getUnitCode());
//                    } else if ((reportConfigParameter.getSelectSql()).contains(":UnitCode")) {
//                        nQuery.setParameter("UnitCode", user.getUnitCode());
//                    }
//                }

                inputElements += "<option value='-9999999'>Choose " + reportConfigParameter.getName() + "</option>";

                List<String> qResultList = nQuery.getResultList();
                for (String qResult : qResultList) {
                    if (qResult != null) {
                        String selected = "";

                        //System.out.println("\n\n\nparameterValue : " + parameterValue);
                        //System.out.println("qResult : " + qResult + "\n\n");
                        if (qResult.equals(parameterValue)) {
                            selected = "selected='selected'";
                        }
                        inputElements += "<option " + selected + " value='" + qResult + "'>" + qResult + "</option>";
                    }
                }
                inputElements += "</select>";
                inputElements += "</div>"; //; display:none;  hidden='true'
                inputElements += "<div class='col col-sm-2' id='" + parameter + "-validator' name='" + parameter + "-validator' style='color:red;display:none;text-align:left;'> <-- * Select " + reportConfigParameter.getName() + " !</div>";
                break;
            default:
                inputElements = "<label for='" + parameter + "' class='control-label col-sm-3' style='text-align: right;'>" + reportConfigParameter.getName() + "</label><div class='col col-sm-9'><input type='text' name='" + parameter + "' class='form-control' value='" + parameterValue + "' /></div>";
                break;
        }
        return inputElements;
    }

//    private UserLogin getCurrentAuditor() {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            if (authentication != null && authentication.isAuthenticated()) {
//                if (authentication.getPrincipal() instanceof UserLogin) {
//                    return (UserLogin) authentication.getPrincipal();
//                }
//            }
//        } catch (Exception e) {
//            //log.error("Authentication:", e);
//        }
//        return null;
//    }

    private String getDataValue(ParameterHolder holder, String parameter) {
        String value = "";
        switch (parameter) {
            case "parameter1":
                value = holder.getParameter1();
        }
        switch (parameter) {
            case "parameter2":
                value = holder.getParameter2();
        }
        switch (parameter) {
            case "parameter3":
                value = holder.getParameter3();
        }
        switch (parameter) {
            case "parameter4":
                value = holder.getParameter4();
        }
        switch (parameter) {
            case "parameter5":
                value = holder.getParameter5();
        }
        switch (parameter) {
            case "parameter6":
                value = holder.getParameter6();
        }
        return value;
    }

    public static String getCurrDate(ReportConfigParameter param) {
        String mydate = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        mydate = df.format(c.getTime());
        return mydate;
    }

    public static String getCurrDateTime(ReportConfigParameter param) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
        
        DateTime dateTime;
        if (param.getName().toLowerCase().contains("from") || param.getParameter().toLowerCase().contains("from") || param.getName().toLowerCase().contains("start") || param.getParameter().toLowerCase().contains("start")) {
            dateTime = DateTime.now().withTimeAtStartOfDay();
        } else if (param.getName().toLowerCase().contains("end") || param.getParameter().toLowerCase().contains("end") || param.getName().toLowerCase().contains("to") || param.getParameter().toLowerCase().contains("to")) {
            dateTime = DateTime.now().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
        } else {
            dateTime = DateTime.now();
        }

        Date date = dateTime.toDate();
        return df.format(date);
    }

    @Override
    public ReportConfig getByID(Long reportConfigId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
