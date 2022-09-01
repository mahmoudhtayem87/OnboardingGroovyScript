import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;

long groupId = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_GROUP_ID));
long companyId = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
Company company = CompanyLocalServiceUtil.getCompany(companyId);
String portalURL = company.getPortalURL(groupId);
final long creatorUserId = GetterUtil.getLong((String)workflowContext.get("adminUserId"));
final long newUserId = GetterUtil.getLong((String)workflowContext.get("newUserId"));




ServiceContext serviceContext  = new ServiceContext();
serviceContext.setCompanyId(companyId);
serviceContext.setUserId(creatorUserId);
serviceContext.setPortalURL(portalURL);
serviceContext.setPathMain(PortalUtil.getPathMain());
serviceContext.setScopeGroupId(groupId);

Role role = RoleLocalServiceUtil.fetchRole(80647).cloneWithOriginalValues();
role.setTitle(workflowContext.get("companyName"));
role.setName(workflowContext.get("companyName"));
Role newRole = RoleLocalServiceUtil.addRole(
        role.getUserId(),
        role.getClassName(),
        role.getClassPK(),
        workflowContext.get("companyName"),
        role.getTitleMap(),
        role.getDescriptionMap(),
        role.getType(),
        role. getSubtype(),
        serviceContext);

System.out.println("user role id: " +  newRole.getRoleId());

UserLocalServiceUtil.addRoleUser(newRole.getRoleId(),newUserId);

workflowContext.put("userRole",newRole.getRoleId());

returnValue = "User Created"