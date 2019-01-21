
package zw.co.psmi.hms.stocks.service.impl;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.*;
import zw.co.psmi.hms.authentication.entity.Role;
import zw.co.psmi.hms.authentication.entity.Unit;
import zw.co.psmi.hms.authentication.service.RoleService;
import zw.co.psmi.hms.stocks.dao.ApprovalConfigDao;
import zw.co.psmi.hms.stocks.dao.ApprovalTemplateDao;
import zw.co.psmi.hms.stocks.entity.ApprovalConfig;
import zw.co.psmi.hms.stocks.entity.ApprovalTemplate;
import zw.co.psmi.hms.stocks.entity.WorkFlowType;
import zw.co.psmi.hms.stocks.service.ApprovalConfigService;


public class ApprovalConfigServiceImplTest {

    private ApprovalConfigService approvalConfigService; 
    private ApprovalConfigDao approvalConfigDao;
    private ApprovalTemplateDao approvalTemplateDao;
    private RoleService roleService;
    
    @Before
    public void setUp() {
        this.approvalConfigDao = mock(ApprovalConfigDao.class);
        this.approvalTemplateDao = mock(ApprovalTemplateDao.class);
        this.roleService = mock(RoleService.class);
        
        this.approvalConfigService = new ApprovalConfigServiceImpl(  approvalConfigDao, approvalTemplateDao, roleService);
    }

    @Test 
    public void testGetApprovalList() {
        Unit unit = new Unit();
        unit.setId(1L);
        approvalConfigService.getApprovalList(WorkFlowType.STOCKREQUEST, unit);
        //verify(approvalConfigDao, times(1)).findAll();
    }

    @Test
    public void testGetDao() {
        assertEquals(approvalConfigDao, approvalConfigService.getDao());
    }

    @Test
    public void testGetByID() {
        approvalConfigService.getByID(1L);
        verify(approvalConfigDao, times(1)).findOne(any(Long.class));

        when(approvalConfigDao.findOne(any(Long.class))).thenReturn(null);
        assertNotNull(approvalConfigService.getByID(2L));
    }

    @Test
    public void testSave() {
        Role role = new Role();
        role.setId(1L);
        List<ApprovalTemplate> list = new ArrayList<>();
        ApprovalTemplate approvalTemplate = new ApprovalTemplate(); 
        approvalTemplate.setId(1L); 
        approvalTemplate.setRole(role);
        list.add(approvalTemplate);
        when(roleService.getByID(any(Long.class))).thenReturn(role);
        
        approvalConfigService.save(new ApprovalConfig(), list);

        verify(approvalConfigDao, times(1)).save(any(ApprovalConfig.class));
        verify(approvalTemplateDao, times(1)).save(any(list.getClass()));
    }

    @Test
    public void testFindAll() {
        approvalConfigService.findAll();
        verify(approvalConfigDao, times(1)).findAll();
    }

    @Test
    public void testGetApprovalTemplateByApprovalConfigId() {
        approvalConfigService.getApprovalTemplateByApprovalConfigId(1L);
        verify(approvalTemplateDao, times(1)).findByApprovalConfigId(any(Long.class));
    }
    
}
