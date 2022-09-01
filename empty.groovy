import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.liferay.portal.kernel.util.GetterUtil;

long groupId = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_GROUP_ID));
long companyId = GetterUtil.getLong((String)workflowContext.get(WorkflowConstants.CONTEXT_COMPANY_ID));
Company company = CompanyLocalServiceUtil.getCompany(companyId);
String portalURL = company.getPortalURL(groupId);
final long creatorUserId = GetterUtil.getLong((String)workflowContext.get("adminUserId"));


ServiceContext serviceContext  = new ServiceContext();
serviceContext.setCompanyId(companyId);
serviceContext.setUserId(creatorUserId);
serviceContext.setPortalURL(portalURL);
serviceContext.setPathMain(PortalUtil.getPathMain());
serviceContext.setScopeGroupId(groupId);

final boolean autoPassword = false;
final String tempPassword = "test";
final boolean autoScreenName = true;
final boolean male = false;
final boolean sendAccountCreationEmail = false;
final long prefixId = -1L;
final long suffixId = -1L;
final long[] siteIds = [ groupId ];
final Calendar dob = new GregorianCalendar(1970, 1, 1);
locale = LocaleUtil.getDefault();

User newUser = UserLocalServiceUtil.addUser(serviceContext.getUserId(), companyId, autoPassword, tempPassword, tempPassword,
        autoScreenName, null, workflowContext.get("email"), locale, workflowContext.get("firstName"), null,
        workflowContext.get("lastName"), prefixId, suffixId, male, (dob.get(Calendar.MONTH) - 1),
        dob.get(Calendar.DATE), dob.get(Calendar.YEAR), null, siteIds, null, null, null, sendAccountCreationEmail,
        serviceContext);

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
UserLocalServiceUtil.addRoleUser(87262L,newUser.getUserId())
UserLocalServiceUtil.addRoleUser(newRole.getRoleId(),newUser.getUserId());

workflowContext.put("userRole",newRole.getRoleId());

returnValue = "User Created"