import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
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


import com.liferay.commerce.product.service.CommerceCatalogServiceUtil;
import com.liferay.commerce.product.model.CommerceCatalog;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.service.ResourcePermissionServiceUtil;

Role adminRole = RoleLocalServiceUtil.getRole(company.getCompanyId(),"Administrator");
List adminUsers = UserLocalServiceUtil.getRoleUsers(adminRole.getRoleId());

PrincipalThreadLocal.setName(adminUsers[0].getUserId());
PermissionChecker permissionChecker =PermissionCheckerFactoryUtil.create(adminUsers.get(0));
PermissionThreadLocal.setPermissionChecker(permissionChecker);

CommerceCatalog newCatalog = CommerceCatalogServiceUtil.addCommerceCatalog(
        workflowContext.get("companyName"),
        workflowContext.get("companyName"),
        "SAR",
        "en_US",
        serviceContext);


long roleId = GetterUtil.getLong((String)workflowContext.get("userRole"));;
String className = newCatalog.getModelClassName();
String[] actionIds=["VIEW","UPDATE","DELETE"];

ResourcePermissionServiceUtil.setIndividualResourcePermissions(
        groupId,
        companyId,
        className,
        String.valueOf(newCatalog.getPrimaryKey()),
        roleId,
        actionIds
);

returnValue = "Product Catalog Created";