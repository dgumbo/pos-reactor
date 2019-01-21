package zw.co.hisolutions.pos.reports.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query; 
import org.springframework.stereotype.Service; 
import zw.co.hisolutions.pos.common.util.Utils;
import zw.co.hisolutions.pos.reports.entity.ColumnMetadata;
import zw.co.hisolutions.pos.reports.entity.ParameterHolder;
import zw.co.hisolutions.pos.reports.entity.ReportConfig;
import zw.co.hisolutions.pos.reports.entity.ReportConfigParameter;
import zw.co.hisolutions.pos.reports.entity.ReportParameterType;
import zw.co.hisolutions.pos.reports.entity.ReportData; 
import zw.co.hisolutions.pos.reports.service.ReportDataService;

/**
 *
 * @author dgumbo
 */
@Service
public class ReportDataServiceImpl implements ReportDataService {

    private boolean bReportColumnsPropertiesParametersAsigned = false;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ReportData getReportResultData(ReportConfig reportConfig, ParameterHolder holder, List<ReportConfigParameter> configParameters) {
        boolean isReportPreview = false;

        Query nQuery = prepareNQuery(reportConfig, holder, configParameters, isReportPreview);

        String[] columns = reportConfig.getColumns().split(",");
        return getReportResult(nQuery, columns);

    }

    @Override
    public ReportData getReportPreviewData(ReportConfig reportConfig, ParameterHolder holder, List<ReportConfigParameter> configParameters) {
        boolean isReportPreview = true;

        Query nQuery = prepareNQuery(reportConfig, holder, configParameters, isReportPreview);

        String[] columns = reportConfig.getColumns().split(",");
        ReportData result = getReportResult(nQuery, columns);

        return result;
    }

    private ReportData getReportResult(Query nQuery, String[] columns) {
        List<Object[]> results = nQuery.getResultList();

        ReportData reportResult = new ReportData();
        reportResult.setColumnNames(columns);
        if (results.isEmpty()) {
            reportResult.setColumnNames(new String[]{"No Data"});
            reportResult.setData(new String[][]{{"No Data"}});
            return reportResult;
        }

        Object[][] res2 = new Object[results.size()][];
        for (int i = 0; i < results.size(); i++) {
            res2[i] = results.get(i);
        }
        reportResult.setData(res2);
        columnLength(reportResult);

        return reportResult;
    }

    private Query prepareNQuery(ReportConfig reportConfig, ParameterHolder holder, List<ReportConfigParameter> configParameters, boolean isReportPreview) {
        String sql = reportConfig.getNativeQuery();
        Query nQuery = entityManager.createNativeQuery(sql);

//        if (reportConfig.isUnitQuery()) {
//            if ((reportConfig.getNativeQuery()).contains(":unitCode")) {
//                UserLogin user = getCurrentAuditor();
//                if (user != null && user.getUnitCode() != null) {
//                    nQuery.setParameter("unitCode", user.getUnitCode());
//                }
//            }
//            if ((reportConfig.getNativeQuery()).contains(":UnitCode")) {
//                UserLogin user = getCurrentAuditor();
//                if (user != null && user.getUnitCode() != null) {
//                    nQuery.setParameter("UnitCode", user.getUnitCode());
//                }
//            }
//            if ((reportConfig.getNativeQuery()).contains(":IssuingUnitCode")) {
//                UserLogin user = getCurrentAuditor();
//                if (user != null && user.getUnitCode() != null) {
//                    nQuery.setParameter("IssuingUnitCode", user.getUnitCode());
//                }
//            }
//        }

        for (ReportConfigParameter reportConfigParameter : configParameters) {
            String value = getDataValue(holder, reportConfigParameter.getParameter());
            value = value == null ? "" : value;
            value = value.replace("'", "''");

            //System.out.println("reportConfigParameter.getColumnName() : [" + reportConfigParameter.getColumnName() + "], value : [" + value + "]");                
            if (!value.isEmpty() || (value.isEmpty() && reportConfigParameter.getParameterType() == ReportParameterType.STRING_LIKE)) {
                if (null == reportConfigParameter.getParameterType()) {
                    // cond += " AND "+ reportConfigParameter.getColumnName()+" LIKE '%"+value+"%'";
                    if ((reportConfig.getNativeQuery()).contains(":" + reportConfigParameter.getColumnName())) {
                        nQuery.setParameter(reportConfigParameter.getColumnName(), value);
                    }
                } else //   filtered = true;
                {
                    switch (reportConfigParameter.getParameterType()) {
                        case SELECT_STATEMENT:
                        case SINGLE_DATE:
                            // cond += " AND "+ reportConfigParameter.getColumnName()+" = '"+value+"'";
                            if ((reportConfig.getNativeQuery()).contains(":" + reportConfigParameter.getColumnName())) {
                                nQuery.setParameter(reportConfigParameter.getColumnName(), value);
                            }
                            break;
                        case SINGLE_DATE_TIME:
                            // cond += " AND "+ reportConfigParameter.getColumnName()+" = '"+value+"'";
                            if ((reportConfig.getNativeQuery()).contains(":" + reportConfigParameter.getColumnName())) {
                                nQuery.setParameter(reportConfigParameter.getColumnName(), value);
                            }
                            break;
                        case STRING_LIKE:
                            // cond += " AND "+ reportConfigParameter.getColumnName()+" LIKE '%"+value+"%'";
                            if ((reportConfig.getNativeQuery()).contains(":" + reportConfigParameter.getColumnName())) {
                                value = value.equals(" ") ? "" : value.trim();
                                nQuery.setParameter(reportConfigParameter.getColumnName(), value);
                            }
                            break;
                        case DATE_RANGE_INCLUSIVE: {
                            String value_ = getDataValue(holder, reportConfigParameter.getParameter() + "_");
                            if ((reportConfig.getNativeQuery()).contains(":" + reportConfigParameter.getColumnName())) {
                                nQuery.setParameter(reportConfigParameter.getColumnName(), value);
                                nQuery.setParameter(reportConfigParameter.getColumnName() + "_", value_);
                            }
                            break;
                        }
                        case DATE_RANGE_EXCLUSIVE: {
                            String value_ = getDataValue(holder, reportConfigParameter.getParameter() + "_");
                            //cond += " AND "+ reportConfigParameter.getColumnName()+" BETWEEN '"+value+"' AND '"+value_+"' ";
                            if ((reportConfig.getNativeQuery()).contains(":" + reportConfigParameter.getColumnName())) {
                                nQuery.setParameter(reportConfigParameter.getColumnName(), value);
                                nQuery.setParameter(reportConfigParameter.getColumnName() + "_", value_);
                            }
                            break;
                        }
                        case BOOLEAN:
                            // cond += " AND "+ reportConfigParameter.getColumnName()+" LIKE '%"+value+"%'";
                            if ((reportConfig.getNativeQuery()).contains(":" + reportConfigParameter.getColumnName())) {
                                //System.out.println("\nreportConfigParameter.getColumnName() : " + reportConfigParameter.getColumnName());
                                //System.out.println("value : " + value);

                                boolean bValue = value.equals("1") ? Boolean.TRUE : Boolean.FALSE;
                                bValue = value.equals(-1) ? null : bValue;

                                nQuery.setParameter(reportConfigParameter.getColumnName(), bValue);
                            }
                            break;
                        case NUMERIC:
                            // cond += " AND "+ reportConfigParameter.getColumnName()+" LIKE '%"+value+"%'";
                            if ((reportConfig.getNativeQuery()).contains(":" + reportConfigParameter.getColumnName())) {
                                nQuery.setParameter(reportConfigParameter.getColumnName(), value);
                            }
                            break;
                        default:
                            // cond += " AND "+ reportConfigParameter.getColumnName()+" LIKE '%"+value+"%'";
                            if ((reportConfig.getNativeQuery()).contains(":" + reportConfigParameter.getColumnName())) {
                                nQuery.setParameter(reportConfigParameter.getColumnName(), value);
                            }
                            break;
                    }
                }
            }
        }

        return nQuery;
    }

    @Override
    public List<ColumnMetadata> getReportColumnsProperties(ReportConfig reportConfig, ParameterHolder holder, List<ReportConfigParameter> configParameters) {

        String qry = reportConfig.getNativeQuery();
        qry = AssignStringQueryParameters(reportConfig, qry, holder, configParameters);
        qry = qry.replaceAll("'", "''");

        String sql = " Declare @Qry nVarChar(Max) = N'" + qry + "' " + "\n\n"
                + " Select cm.column_ordinal, cm.name, cm.system_type_name, cm.system_type_id " + "\n"
                + " From sys.dm_exec_describe_first_result_set ( @Qry, null, 0 ) cm " + "\n"
                + " Order By cm.column_ordinal Asc " + "\n";

        // System.out.println("\n\nQuery to Extract Results Metadata of given SQL : \n\n" + sql + "\n\n");
        Query nQuery = entityManager.createNativeQuery(sql);

        List<ColumnMetadata> columnProperties = new ArrayList();

        if ((bReportColumnsPropertiesParametersAsigned == true && !configParameters.isEmpty()) || (configParameters == null || configParameters.isEmpty())) {
            List<Object[]> results = nQuery.getResultList();

            if (results.size() <= 2 && results.get(0)[1] == null) {
                // Set columnProperties to empty list since query does not return any data
                columnProperties = new ArrayList();
            } else {
                for (int i = 0; i < results.size(); i++) {
                    ColumnMetadata property = new ColumnMetadata();
                    Object[] res = results.get(i);
                    property.setPosition(res[0].toString());
                    property.setName(res[1].toString());
                    property.setTitle(res[1].toString());

                    String type = res[2].toString();
                    if (type.toLowerCase().contains("varchar")) {
                        type = "string";
                    } else if (type.toLowerCase().contains("int") || type.toLowerCase().contains("numeric")) {
                        type = "number";
                    } else if (type.toLowerCase().contains("date") || type.toLowerCase().contains("time")) {
                        type = "date";
                    }

                    property.setType(type);
                    columnProperties.add(property);
                }
            }
        }

//        System.out.println("\n\n\n");
        return columnProperties;
    }

    private void columnLength(ReportData reportResult) {
        String[] r1 = Arrays.copyOf(reportResult.getColumnNames(), reportResult.getData()[0].length);
        for (int i = 0; i < r1.length; i++) {
            r1[i] = Utils.empty(r1[i]) ? "header" + i : r1[i];
        }
        reportResult.setColumnNames(r1);
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

    private String AssignStringQueryParameters(ReportConfig reportConfig, String query, ParameterHolder holder, List<ReportConfigParameter> configParameters) {
        boolean parametersSet = holder.getParameter1() != null;

//        UserLogin user = getCurrentAuditor();
//        if (user != null && user.getUnitCode() != null) {
//            if (reportConfig.isUnitQuery()) {
//                if ((reportConfig.getNativeQuery()).contains(":unitCode")) {
//                    query = query.replace(":unitCode", "'" + user.getUnitCode() + "'");
//                }
//                if ((reportConfig.getNativeQuery()).contains(":UnitCode")) {
//                    query = query.replace(":UnitCode", "'" + user.getUnitCode() + "'");
//                }
//            }
//
//            if ((reportConfig.getNativeQuery()).contains(":IssuingUnitCode")) {
//                query = query.replace(":IssuingUnitCode", "'" + user.getUnitCode() + "'");
//            }
//        }

        bReportColumnsPropertiesParametersAsigned = false;
        if (parametersSet) {
            for (ReportConfigParameter reportConfigParameter : configParameters) {
                String configParameterColumnName = reportConfigParameter.getColumnName();
                String value = getDataValue(holder, reportConfigParameter.getParameter());
                value = value == null ? "null" : "'" + value + "'";

                //System.out.println("configParameterColumnName : [" + configParameterColumnName + "], value : [" + value + "]");                
                if (!value.isEmpty() || (value.isEmpty() && reportConfigParameter.getParameterType() == ReportParameterType.STRING_LIKE)) {
                    if ((reportConfig.getNativeQuery()).contains(":" + configParameterColumnName)) {
                        switch (reportConfigParameter.getParameterType()) {
                            case BOOLEAN:
                                value = value.equals("1") ? "true" : "false";
                                break;
                            case STRING_LIKE:
                                value = value.equals(" ") ? "" : value.trim();
                                break;
                        }

                        query = query.replace(":" + configParameterColumnName, value);
                        bReportColumnsPropertiesParametersAsigned = true;
                    }
                }
            }
        }

        return query;
    }

}
