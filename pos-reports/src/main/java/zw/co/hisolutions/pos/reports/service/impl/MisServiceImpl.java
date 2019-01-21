package zw.co.hisolutions.pos.reports.service.impl;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource; 
import org.springframework.stereotype.Service; 
import zw.co.hisolutions.pos.reports.entity.ParameterHolder;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;
import zw.co.hisolutions.pos.reports.entity.ReportConfigParameter;
import zw.co.hisolutions.pos.reports.service.ReportConfigService;
import zw.co.hisolutions.pos.reports.entity.ColumnMetadata;
import zw.co.hisolutions.pos.reports.entity.ReportInformation;
import zw.co.hisolutions.pos.reports.service.MisService;
import zw.co.hisolutions.pos.reports.entity.ReportParameterType;
import zw.co.hisolutions.pos.reports.service.ReportDataService; 

@Service
@Slf4j
public class MisServiceImpl implements MisService { 
    private final ReportConfigService reportConfigService;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate; 
    private final ReportDataService reportResultService;

    @PersistenceContext
    private EntityManager entityManager;

    public MisServiceImpl(ReportConfigService reportConfigService, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, ReportDataService reportResultService) {
        this.reportConfigService = reportConfigService;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate; 
        this.reportResultService=reportResultService;
    }

    @Override
    public List<ReportConfig> getReportList() {
        List<ReportConfig> reportConfigList = reportConfigService.findAll();

        return reportConfigList;
    }

    @Override
    public ReportInformation getReportInformation(String reportName, ParameterHolder holder) {
        ReportInformation reportInformation = new ReportInformation();
        
        ReportConfig reportConfig = reportConfigService.getByName(reportName);
        List<ReportConfigParameter> reportConfigParameters = reportConfigService.getParametersByReportConfig(reportName) ;
        List<ColumnMetadata> reportColumnsProperties = reportResultService.getReportColumnsProperties(reportConfig, holder, reportConfigParameters);
        List<Map<String, Object>> reportData = getReportDataJDBCTemplate(reportName, holder);

        reportInformation.setColumnMetadata(reportColumnsProperties);
        reportInformation.setReportData(reportData);

        return reportInformation;
    }

    @Override
    public ReportInformation getReportPreviewInformation(String reportName, ParameterHolder holder) {
        ReportInformation reportInformation = new ReportInformation(); 
        
        ReportConfig reportConfig = reportConfigService.getByName(reportName);
        List<ReportConfigParameter> configParameters = reportConfigService.getParametersByReportConfig(reportConfig, holder);
        
        List<ColumnMetadata> reportColumnsProperties =reportResultService. getReportColumnsProperties(reportConfig, holder, configParameters);
        List<Map<String, Object>> reportData = getReportDataJDBCTemplate(reportName, holder);
        reportData = reportData.size() > 100 ? reportData.subList(0, 100) : reportData;

        reportInformation.setColumnMetadata(reportColumnsProperties);
        reportInformation.setReportData(reportData);

        return reportInformation;
    }

    @Override
    public List<Map<String, Object>> getReportDataJDBCTemplate(String reportName, ParameterHolder holder) {
        ReportConfig reportConfig = reportConfigService.getByName(reportName);

        String qry = reportConfig.getNativeQuery();

        SqlParameterSource namedParameters = AssignMapSqlParameterSource(reportConfig, holder);
        List<Map<String, Object>> res = this.namedParameterJdbcTemplate.queryForList(qry, namedParameters);

        return res;
    }

    @Override
    public List<Object[]> getReportDataNQuery(String reportName, ParameterHolder holder) {
        ReportConfig reportConfig = reportConfigService.getByName(reportName);

        String qry = reportConfig.getNativeQuery();

        System.out.println("\n\nreportConfig.getNativeQuery() : \n\n" + qry + "\n\n");

        Query nQuery = entityManager.createNativeQuery(qry);
        nQuery = AssignQueryParameters(reportConfig, nQuery, holder);

        String hibernateQuery = nQuery.unwrap(org.hibernate.Query.class).getQueryString();
        System.out.println("\n\nHibernate Query : \n\n" + hibernateQuery + "\n\n");

        // nQuery.get 
        List<ReportConfigParameter> configParameters = reportConfigService.getParametersByReportConfig(reportConfig, holder);
        if ((bQueryParametersAsigned == true && !configParameters.isEmpty()) || (configParameters == null || configParameters.isEmpty())) {
            List<Object[]> results = nQuery.getResultList();
            System.out.println("\n\nList<Object[]> results = nQuery.getResultList() : \n\nResults Count :" + results.size() + "\n\n");
            return results;
        }
        return null;
    } 
    
    private SqlParameterSource AssignMapSqlParameterSource(ReportConfig reportConfig, ParameterHolder holder) {

        MapSqlParameterSource in = new MapSqlParameterSource();
        //.addValue("in_id", id);

        List<ReportConfigParameter> configParameters = reportConfigService.getParametersByReportConfig(reportConfig, holder);
        boolean parametersSet = holder.getParameter1() != null;

//        UserLogin user = getCurrentAuditor();
//        if (user != null && user.getUnitCode() != null) {
//            if (reportConfig.isUnitQuery()) {
//                if ((reportConfig.getNativeQuery()).contains(":unitCode")) {
//                    in.addValue("unitCode", user.getUnitCode());
//                }
//                if ((reportConfig.getNativeQuery()).contains(":UnitCode")) {
//                    in.addValue("UnitCode", user.getUnitCode());
//                }
//            }
//            if ((reportConfig.getNativeQuery()).contains(":IssuingUnitCode")) {
//                in.addValue("IssuingUnitCode", user.getUnitCode());
//            }
//        }

        // bSqlParameterSourceMapAsigned = false;
        // boolean filtered = false;
        if (parametersSet) {
            for (ReportConfigParameter reportConfigParameter : configParameters) {
                String value = getDataValue(holder, reportConfigParameter.getParameter());
                value = value == null ? "" : value;

                //System.out.println("configParameterColumnName : [" + configParameterColumnName + "], value : [" + value + "]");                
                if (!value.isEmpty() || (value.isEmpty() && reportConfigParameter.getParameterType() == ReportParameterType.STRING_LIKE)) {
                    if ((reportConfig.getNativeQuery()).contains(":" + reportConfigParameter.getColumnName())) {
                        String value_ = getDataValue(holder, reportConfigParameter.getParameter() + "_");

                        switch (reportConfigParameter.getParameterType()) {
                            case STRING_LIKE:
                                value = value.equals(" ") ? "" : value.trim();
                                in.addValue(reportConfigParameter.getColumnName(), value);
                                break;
                            case DATE_RANGE_INCLUSIVE:
                            case DATE_RANGE_EXCLUSIVE:
                                in.addValue(reportConfigParameter.getColumnName(), value);
                                in.addValue(reportConfigParameter.getColumnName() + "_", value_);
                                break;
                            case BOOLEAN:
                                boolean bValue = value.equals("1") ? Boolean.TRUE : Boolean.FALSE;
                                bValue = value.equals(-1) ? null : bValue;
                                in.addValue(reportConfigParameter.getColumnName(), bValue);
                                break;
                            default:
                                in.addValue(reportConfigParameter.getColumnName(), value);
                                break;
                        }
                        //  bSqlParameterSourceMapAsigned = true;
                    }
                }
            }
        }

        return in;
    }

    private boolean bQueryParametersAsigned = false;

    private Query AssignQueryParameters(ReportConfig reportConfig, Query nQuery, ParameterHolder holder) {
        List<ReportConfigParameter> configParameters = reportConfigService.getParametersByReportConfig(reportConfig, holder);
        boolean parametersSet = holder.getParameter1() != null;

//        UserLogin user = getCurrentAuditor();
//        if (user != null && user.getUnitCode() != null) {
//            if (reportConfig.isUnitQuery()) {
//                if ((reportConfig.getNativeQuery()).contains(":unitCode")) {
//                    nQuery.setParameter("unitCode", user.getUnitCode());
//                }
//                if ((reportConfig.getNativeQuery()).contains(":UnitCode")) {
//                    nQuery.setParameter("UnitCode", user.getUnitCode());
//                }
//            }
//            if ((reportConfig.getNativeQuery()).contains(":IssuingUnitCode")) {
//                nQuery.setParameter("IssuingUnitCode", user.getUnitCode());
//            }
//        }

        bQueryParametersAsigned = false;
        // boolean filtered = false;
        if (parametersSet) {
            for (ReportConfigParameter reportConfigParameter : configParameters) {
                String value = getDataValue(holder, reportConfigParameter.getParameter());
                value = value == null ? "" : value;

                //System.out.println("configParameterColumnName : [" + configParameterColumnName + "], value : [" + value + "]");                
                if (!value.isEmpty() || (value.isEmpty() && reportConfigParameter.getParameterType() == ReportParameterType.STRING_LIKE)) {
                    if ((reportConfig.getNativeQuery()).contains(":" + reportConfigParameter.getColumnName())) {
                        String value_ = getDataValue(holder, reportConfigParameter.getParameter() + "_");

                        switch (reportConfigParameter.getParameterType()) {
                            case STRING_LIKE:
                                value = value.equals(" ") ? "" : value.trim();
                                nQuery.setParameter(reportConfigParameter.getColumnName(), value);
                                break;
                            case DATE_RANGE_INCLUSIVE:
                            case DATE_RANGE_EXCLUSIVE:
                                nQuery.setParameter(reportConfigParameter.getColumnName(), value);
                                nQuery.setParameter(reportConfigParameter.getColumnName() + "_", value_);
                                break;
                            case BOOLEAN:
                                boolean bValue = value.equals("1") ? Boolean.TRUE : Boolean.FALSE;
                                bValue = value.equals(-1) ? null : bValue;
                                nQuery.setParameter(reportConfigParameter.getColumnName(), bValue);
                                break;
                            default:
                                nQuery.setParameter(reportConfigParameter.getColumnName(), value);
                                break;
                        }
                        bQueryParametersAsigned = true;
                    }
                }
            }
        }

        return nQuery;
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

}
